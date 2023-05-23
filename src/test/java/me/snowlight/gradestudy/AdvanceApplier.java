package me.snowlight.gradestudy;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

public class AdvanceApplier {
    private JdbcTemplate jdbcTemplate;

    public AdvanceApplier(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ApplyResult apply(Targets targets) {
        Map<Integer, GradeCount> gradeCountMap = new HashMap<>();
        for (User user : targets.getUsers()) {
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
