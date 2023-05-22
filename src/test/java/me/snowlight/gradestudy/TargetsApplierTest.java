package me.snowlight.gradestudy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.*;

@SpringBootTest
public class TargetsApplierTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void apply() {
        clearStu();
        int id = 101;
        int grade = 1;
        givenStu(id, grade);

        TargetsApplier targetsApplier = new TargetsApplier(jdbcTemplate);
        Targets targets = new Targets(List.of(new User(id, grade)));
        targetsApplier.apply(targets);

        // TODO DB 에 데이터와 검점을 할 때 사용할 수 있는 방법
        assertStuGrade(id, 2);
    }

    @Test
    void applyResult() {
        clearStu();
        givenStu(101, 1);

        TargetsApplier targetsApplier = new TargetsApplier(jdbcTemplate);
        Targets targets = new Targets(List.of(new User(101, 1),
                                                new User(102, 2)));

        ApplyResult applyResult = targetsApplier.apply(targets);
        Collection<GradeCount> cnts = applyResult.getGradeCounts();

        Assertions.assertThat(cnts).contains(new GradeCount(2, 1), new GradeCount(3, 1));
    }
    private void assertStuGrade(int id, int expGrade) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select stu_id, grade from student where stu_id = ?", id);
        sqlRowSet.next();
        Assertions.assertThat(sqlRowSet.getInt("grade")).isEqualTo(expGrade);
    }


    private void clearStu() {
        this.jdbcTemplate.update("truncate table student");
    }

    private void givenStu(int id, int grade) {
        this.jdbcTemplate.update("insert into student values (?, ?)", id, grade);
    }

    public class TargetsApplier {
        private JdbcTemplate jdbcTemplate;

        public TargetsApplier(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        public ApplyResult apply(Targets targets) {
            Map<Integer, GradeCount> gradeCountMap = new HashMap<>();
            for (User user: targets.getUsers()) {
                int nextGrade = user.getGrade() + 1;
                jdbcTemplate.update("update student set grade = ? where stu_id = ?",
                                        nextGrade,
                                        user.getId());

                GradeCount gradeCount = gradeCountMap.get(nextGrade);
                if (gradeCount == null) {
                    gradeCount = new GradeCount(nextGrade, 0);
                     gradeCountMap.put(nextGrade, gradeCount);
                }
                gradeCount.inc();
            }

            return new ApplyResult(gradeCountMap);
        }
    }

}
