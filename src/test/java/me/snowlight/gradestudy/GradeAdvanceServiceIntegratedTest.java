package me.snowlight.gradestudy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class GradeAdvanceServiceIntegratedTest {
    @Autowired
    private GradeAdvanceService gradeAdvanceService;
    @Autowired
    private GivenAndAssertHelper givenAndAssertHelper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() throws IOException {
        Files.deleteIfExists(GradeAdvanceService.DEFAULT_TARGETS_PATH);
    }

    @Test
    public void alreadyCompleted() {
        givenAndAssertHelper.clearStu();
        givenAndAssertHelper.givenStu(601, 2);
        givenAndAssertHelper.givenStu(602, 3);
        givenAndAssertHelper.givenStu(603, 4);

        AdvanceResult result = gradeAdvanceService.advance();
        Assertions.assertThat(result).isEqualTo(AdvanceResult.SUCCESS);

        List<User> users = jdbcTemplate.query(
                "select * from student",
                (rs, rowNum) -> new User(rs.getInt("stu_id"), rs.getInt("grade")));

        givenAndAssertHelper.assertStuGrade(601, 3);
        givenAndAssertHelper.assertStuGrade(602, 4);
        givenAndAssertHelper.assertStuGrade(603, 5);
    }

}
