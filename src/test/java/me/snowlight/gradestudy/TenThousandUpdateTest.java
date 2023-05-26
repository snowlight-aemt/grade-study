package me.snowlight.gradestudy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class TenThousandUpdateTest {
    @Autowired
    private TargetsGen targetsGen;
    @Autowired
    private TargetExporter targetExporter;
    @Autowired
    private AdvanceApplier advanceApplier;

    @Test
    void applyResult_ten_thousand_update() {
        int userSize = 100_000;
        Targets targets = targetsGen.gen();
        Assertions.assertThat(targets.getUsers()).hasSize(userSize);

        Path targetPath = Paths.get("build", "targets");
        targetExporter.export(targetPath, targets);
        Assertions.assertThat(Files.exists(targetPath)).isTrue();

        ApplyResult apply = advanceApplier.apply(targets);
//        Collection<GradeCount> gradeCounts = apply.getGradeCounts();
//        Assertions.assertThat(gradeCounts.stream()
//                .map(GradeCount::getCount)
//                .reduce(0, Integer::sum)).isEqualTo(userSize);
    }
}
// update batch
//2023-05-26T00:20:56.007+09:00  INFO 3524 --- [    Test worker] me.snowlight.gradestudy.AdvanceApplier   : Start Batch Update
//2023-05-26T00:22:54.859+09:00  INFO 3524 --- [    Test worker] me.snowlight.gradestudy.AdvanceApplier   : End Batch Update



//
//2023-05-26T00:23:31.694+09:00  INFO 3553 --- [    Test worker] me.snowlight.gradestudy.AdvanceApplier   : Start Batch Update
//2023-05-26T00:25:32.829+09:00  INFO 3553 --- [    Test worker] me.snowlight.gradestudy.AdvanceApplier   : End Batch Update
