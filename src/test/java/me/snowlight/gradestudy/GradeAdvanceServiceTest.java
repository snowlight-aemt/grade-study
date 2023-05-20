package me.snowlight.gradestudy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GradeAdvanceServiceTest {
    Path path = Paths.get("build/status");
    Status status = new Status(path);

    TargetsGen mockGen = Mockito.mock(TargetsGen.class);
    TargetExporter mockExporter = Mockito.mock(TargetExporter.class);
    AdvanceApplier mockApplier = Mockito.mock(AdvanceApplier.class);
    TargetsImporter mockImporter = Mockito.mock(TargetsImporter.class);;
    GradeAdvanceService gradeAdvanceService = new GradeAdvanceService(status, mockGen, mockExporter, mockImporter, mockApplier);

    @BeforeEach
    public void setUp() throws IOException {
        Files.deleteIfExists(path);
    }

    @Test
    public void alreadyCompleted() {
        status.set(AdvanceStatus.COMPLETED);
        AdvanceResult result = gradeAdvanceService.advance();
        Assertions.assertThat(result).isEqualTo(AdvanceResult.ALREADY_COMPLETED);
    }

    @Test
    public void targetsGenFail() {
        BDDMockito
                .given(mockGen.gen())
                .willThrow(new RuntimeException("!"));

        AdvanceResult result = gradeAdvanceService.advance();
        Assertions.assertThat(result).isEqualTo(AdvanceResult.TARGET_GEN_FAILED);
    }

    @Test
    public void targetExporterFail() {
        BDDMockito
                .given(mockGen.gen())
                .willReturn(Mockito.mock(Targets.class));
        BDDMockito
                .willThrow(new RuntimeException("!"))
                .given(mockExporter).export(Mockito.any(Path.class), Mockito.any(Targets.class));

        AdvanceResult result = gradeAdvanceService.advance();
        Assertions.assertThat(result).isEqualTo(AdvanceResult.TARGET_EXPORT_FAILED);
    }

    @Test
    public void applyFail() {
        BDDMockito
                .given(mockGen.gen())
                .willReturn(Mockito.mock(Targets.class));
        BDDMockito
                .given(mockApplier.apply(Mockito.any(Targets.class)))
                .willThrow(new RuntimeException("!"));

        AdvanceResult result = gradeAdvanceService.advance();
        Assertions.assertThat(result).isEqualTo(AdvanceResult.TARGET_APPLY_FAILED);
    }

    @Test
    public void applyFail_then_states_applyFailed() {
        BDDMockito
                .given(mockGen.gen())
                .willReturn(Mockito.mock(Targets.class));
        BDDMockito
                .given(mockApplier.apply(Mockito.any(Targets.class)))
                .willThrow(new RuntimeException("!"));

        gradeAdvanceService.advance();
        Assertions.assertThat(status.get()).isEqualTo(AdvanceStatus.APPLY_FAILED);
    }

    @Test
    public void applySuccess() {
        BDDMockito
                .given(mockGen.gen())
                .willReturn(Mockito.mock(Targets.class));
        BDDMockito
                .given(mockApplier.apply(Mockito.any(Targets.class)))
                .willReturn(Mockito.mock(ApplyResult.class));

        AdvanceResult result = gradeAdvanceService.advance();
        Assertions.assertThat(result).isEqualTo(AdvanceResult.SUCCESS);
    }

    @Test
    public void states_applySuccess_when_applyFailed() {
        status.set(AdvanceStatus.APPLY_FAILED);
        Targets targets = new Targets();
        BDDMockito.given(mockImporter.importTargets(Mockito.any(Path.class))).willReturn(targets);

        gradeAdvanceService.advance();

        BDDMockito.then(mockGen).shouldHaveNoInteractions();
        BDDMockito.then(mockExporter).shouldHaveNoInteractions();
        BDDMockito
                .then(mockApplier)
                .should()
                .apply(Mockito.eq(targets));

    }

}
