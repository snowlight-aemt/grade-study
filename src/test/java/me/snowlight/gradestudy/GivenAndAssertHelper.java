package me.snowlight.gradestudy;

import org.assertj.core.api.Assertions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class GivenAndAssertHelper {
    private final JdbcTemplate jdbcTemplate;

    public GivenAndAssertHelper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void clearStu() {
        this.jdbcTemplate.update("truncate table student");
    }

    public void givenStu(int id, int grade) {
        this.jdbcTemplate.update("insert into student values (?, ?)", id, grade);
    }

    public void assertStuGrade(int id, int expGrade) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select stu_id, grade from student where stu_id = ?", id);
        sqlRowSet.next();
        Assertions.assertThat(sqlRowSet.getInt("grade")).isEqualTo(expGrade);
    }
}
