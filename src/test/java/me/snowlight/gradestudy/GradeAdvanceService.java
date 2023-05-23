package me.snowlight.gradestudy;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
 class GradeAdvanceService {
    public final static Path DEFAULT_TARGETS_PATH = Paths.get("build", "/targets");
    private final States states;
    private final TargetsGen targetsGen;
    private final TargetExporter targetExporter;
    private final TargetsImporter targetImporter;
    private final AdvanceApplier advanceApplier;
    private Path path = DEFAULT_TARGETS_PATH;

    public GradeAdvanceService(States states,
                               TargetsGen targetsGen,
                               TargetExporter targetExporter,
                               TargetsImporter targetImporter,
                               AdvanceApplier advanceApplier) {
        this.states = states;
        this.targetsGen = targetsGen;
        this.targetExporter = targetExporter;
        this.targetImporter = targetImporter;
        this.advanceApplier = advanceApplier;
    }

    public AdvanceResult advance() {
        if (states.get() == AdvanceStatus.COMPLETED)
            return AdvanceResult.ALREADY_COMPLETED;

        Targets targets;
        if (states.get() == AdvanceStatus.APPLY_FAILED) {
            targets = targetImporter.importTargets(path);
        } else {
            try {
                targets = targetsGen.gen();
            } catch (Exception e) {
                return AdvanceResult.TARGET_GEN_FAILED;
            }

            try {
                targetExporter.export(path, targets);
            } catch (Exception e) {
                return AdvanceResult.TARGET_EXPORT_FAILED;
            }
        }

        try {
            advanceApplier.apply(targets);
        } catch (Exception e) {
            states.set(AdvanceStatus.APPLY_FAILED);
            return AdvanceResult.TARGET_APPLY_FAILED;
        }

        return AdvanceResult.SUCCESS;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
