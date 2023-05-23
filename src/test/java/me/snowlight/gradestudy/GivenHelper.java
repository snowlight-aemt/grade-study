package me.snowlight.gradestudy;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class GivenHelper {
    private final JdbcTemplate jdbcTemplate;

    public GivenHelper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void clearStu() {
        this.jdbcTemplate.update("truncate table student");
    }

    public void givenStu(int id, int grade) {
        this.jdbcTemplate.update("insert into student values (?, ?)", id, grade);
    }
}
