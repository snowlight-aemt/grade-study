package me.snowlight.gradestudy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class AdvanceApplierTest {
    @Autowired
    private AdvanceApplier advanceApplier;
    @Autowired
    private GivenAndAssertHelper givenAndAssertHelper;

    @Test
    void apply() {
        givenAndAssertHelper.clearStu();
        int id = 101;
        int grade = 1;
        givenAndAssertHelper.givenStu(id, grade);

        Targets targets = new Targets(List.of(new User(id, grade)));
        advanceApplier.apply(targets);

        // TODO DB 에 데이터와 검점을 할 때 사용할 수 있는 방법
        givenAndAssertHelper.assertStuGrade(id, 2);
    }

    @Test
    void applyResult() {
        givenAndAssertHelper.clearStu();
        givenAndAssertHelper.givenStu(101, 1);

        Targets targets = new Targets(List.of(new User(101, 1),
                                                new User(102, 2)));

        ApplyResult applyResult = advanceApplier.apply(targets);
        Collection<GradeCount> cnts = applyResult.getGradeCounts();

        Assertions.assertThat(cnts).contains(new GradeCount(2, 1), new GradeCount(3, 1));
    }
}
