package me.snowlight.gradestudy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

@SpringBootTest
@ActiveProfiles("test")
public class AdvanceApplierTest {
    @Autowired
    private AdvanceApplier advanceApplier;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GivenHelper givenHelper;

    @Test
    void apply() {
        givenHelper.clearStu();
        int id = 101;
        int grade = 1;
        givenHelper.givenStu(id, grade);

        Targets targets = new Targets(List.of(new User(id, grade)));
        advanceApplier.apply(targets);

        // TODO DB 에 데이터와 검점을 할 때 사용할 수 있는 방법
        assertStuGrade(id, 2);
    }

    @Test
    void applyResult() {
        givenHelper.clearStu();
        givenHelper.givenStu(101, 1);

        Targets targets = new Targets(List.of(new User(101, 1),
                                                new User(102, 2)));

        ApplyResult applyResult = advanceApplier.apply(targets);
        Collection<GradeCount> cnts = applyResult.getGradeCounts();

        Assertions.assertThat(cnts).contains(new GradeCount(2, 1), new GradeCount(3, 1));
    }
    private void assertStuGrade(int id, int expGrade) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select stu_id, grade from student where stu_id = ?", id);
        sqlRowSet.next();
        Assertions.assertThat(sqlRowSet.getInt("grade")).isEqualTo(expGrade);
    }
}
