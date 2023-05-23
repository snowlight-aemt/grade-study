package me.snowlight.gradestudy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TargetsGenTest {
    private final TargetsGen targetsGen;
    private final GivenAndAssertHelper givenAndAssertHelper;

    @Autowired
    public TargetsGenTest(TargetsGen targetsGen, GivenAndAssertHelper givenAndAssertHelper) {
        this.targetsGen = targetsGen;
        this.givenAndAssertHelper = givenAndAssertHelper;
    }

    @Test
    void gen() {
        givenAndAssertHelper.clearStu();
        givenAndAssertHelper.givenStu(101, 1);
        givenAndAssertHelper.givenStu(102, 2);
        givenAndAssertHelper.givenStu(103, 3);

        Targets targets = targetsGen.gen();

        Assertions.assertThat(targets.getUsers()).hasSize(3);
        Assertions.assertThat(targets.getUsers()).contains(new User(101, 1),
                                                            new User(102, 2),
                                                            new User(103, 3));
    }
}
