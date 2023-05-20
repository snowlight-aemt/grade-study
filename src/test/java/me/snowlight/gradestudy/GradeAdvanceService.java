package me.snowlight.gradestudy;

import java.nio.file.Path;
import java.nio.file.Paths;

class GradeAdvanceService {
    private final Status status;
    private final TargetsGen targetsGen;
    private final TargetExporter targetExporter;
    private final TargetsImporter targetImporter;
    private final AdvanceApplier advanceApplier;
    private final Path path = Paths.get("build/targets");

    public GradeAdvanceService(Status status,
                               TargetsGen targetsGen,
                               TargetExporter targetExporter,
                               TargetsImporter targetImporter,
                               AdvanceApplier advanceApplier) {
        this.status = status;
        this.targetsGen = targetsGen;
        this.targetExporter = targetExporter;
        this.targetImporter = targetImporter;
        this.advanceApplier = advanceApplier;
    }

    public AdvanceResult advance() {
        if (status.get() == AdvanceStatus.COMPLETED)
            return AdvanceResult.ALREADY_COMPLETED;

        Targets targets;
        if (status.get() == AdvanceStatus.APPLY_FAILED) {
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
            status.set(AdvanceStatus.APPLY_FAILED);
            return AdvanceResult.TARGET_APPLY_FAILED;
        }

        return AdvanceResult.SUCCESS;
    }
}
