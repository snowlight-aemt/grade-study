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
    GradeAdvanceService gradeAdvanceService = new GradeAdvanceService(status, mockGen, mockExporter);

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

    private class GradeAdvanceService {
        private final Status status;
        private final TargetsGen targetsGen;
        private final TargetExporter targetExporter;

        public GradeAdvanceService(Status status, TargetsGen targetsGen, TargetExporter targetExporter) {
            this.status = status;
            this.targetsGen = targetsGen;
            this.targetExporter = targetExporter;
        }

        public AdvanceResult advance() {
            if (status.get() == AdvanceStatus.COMPLETED)
                return AdvanceResult.ALREADY_COMPLETED;

            Targets targets;
            try {
                targets = targetsGen.gen();
            } catch (Exception e) {
                return AdvanceResult.TARGET_GEN_FAILED;
            }

            try {
                targetExporter.export(Paths.get("build/targets"), targets);
            } catch (Exception e) {
                return AdvanceResult.TARGET_EXPORT_FAILED;
            }

            return null;
        }
    }

    private class TargetsGen {
        public Targets gen() {
            return null;
        }
    }

    private class Targets {
    }

    private class TargetExporter {
        public void export(Path path, Targets targets) {
        }
    }
}
