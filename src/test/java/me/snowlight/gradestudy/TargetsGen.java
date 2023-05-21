package me.snowlight.gradestudy;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

class TargetsGen {
    private final JdbcTemplate jdbcTemplate;

    public TargetsGen(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Targets gen() {
        List<User> users = this.jdbcTemplate.query("select * from student",
                (rs, rowNum) -> {
                    int id = rs.getInt("stu_id");
                    int grade = rs.getInt("grade");
                    return new User(id, grade);
                });
        return new Targets(users);
    }
}
