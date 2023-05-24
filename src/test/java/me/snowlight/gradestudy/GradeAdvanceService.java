package me.snowlight.gradestudy;

import com.spencerwi.either.Either;
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

        Either<AdvanceResult, Targets> targetsEither = states.get() == AdvanceStatus.APPLY_FAILED ?
                importTargets() : genAndExport();

        Either<AdvanceResult, ApplyResult> applyEither =
                targetsEither.flatMapRight(this::applyAdvanceTargets);

        if (applyEither.isLeft()) {
            return applyEither.getLeft();
        } else {
            return AdvanceResult.SUCCESS;
        }
    }

    private Either<AdvanceResult, Targets> importTargets() {
        try {
            return Either.right(targetImporter.importTargets(path));
        } catch (Exception e) {
            return Either.left(AdvanceResult.TARGET_IMPORT_FAILED);
        }
    }

    private Either<AdvanceResult, Targets> genAndExport() {
        Either<AdvanceResult, Targets> genEither = genTargets();
        Either<AdvanceResult, Targets> expEither = genEither.flatMapRight(this::exportTargets);
        return expEither;
    }

    private Either<AdvanceResult, Targets> genTargets() {
        try {
            return Either.right(targetsGen.gen());
        } catch (Exception e) {
            return Either.left(AdvanceResult.TARGET_GEN_FAILED);
        }
    }

    private Either<AdvanceResult, Targets> exportTargets(Targets ts) {
        try {
            targetExporter.export(path, ts);
            return Either.right(ts);
        } catch (Exception e) {
            return Either.left(AdvanceResult.TARGET_EXPORT_FAILED);
        }
    }

    private Either<AdvanceResult, ApplyResult> applyAdvanceTargets(Targets ts) {
        try {
            return Either.right(advanceApplier.apply(ts));
        } catch (Exception e) {
            states.set(AdvanceStatus.APPLY_FAILED);
            return Either.left(AdvanceResult.TARGET_APPLY_FAILED);
        }
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
