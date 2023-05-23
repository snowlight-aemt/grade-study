package me.snowlight.gradestudy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TargetsGenTest {
    private final JdbcTemplate jdbcTemplate;
    private final GivenHelper givenHelper;

    @Autowired
    public TargetsGenTest(JdbcTemplate jdbcTemplate, GivenHelper givenHelper) {
        this.jdbcTemplate = jdbcTemplate;
        this.givenHelper = givenHelper;
    }

    @Test
    void gen() {
        givenHelper.clearStu();
        givenHelper.givenStu(101, 1);
        givenHelper.givenStu(102, 2);
        givenHelper.givenStu(103, 3);

        TargetsGen targetsGen = new TargetsGen(jdbcTemplate);
        Targets targets = targetsGen.gen();

        Assertions.assertThat(targets.getUsers()).hasSize(3);
        Assertions.assertThat(targets.getUsers()).contains(new User(101, 1),
                                                            new User(102, 2),
                                                            new User(103, 3));
    }
}
