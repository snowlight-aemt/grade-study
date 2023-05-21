package me.snowlight.gradestudy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileCopyUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class TargetsImporterTest {
    private final Path path = Paths.get("build/targets");
    TargetsImporter targetsImporter = new TargetsImporter();

    @BeforeEach
    public void setUp() throws IOException {
        Files.deleteIfExists(path);
    }

    @Test
    void importTargets_given_noFile() {
        Assertions.assertThatCode(
                            () -> targetsImporter.importTargets(path))
                            .isInstanceOf(NoTargetsFileException.class);
    }
    @Test
    void importTargets_given_badFormatFile() throws IOException {
        Path path = Paths.get("build/targets");
        FileCopyUtils.copy("101=1\n1022", new FileWriter(path.toFile()));

        Assertions.assertThatCode(
                            () -> targetsImporter.importTargets(path))
                            .isInstanceOf(TargetsFileBadFormatException.class);
    }

    @Test
    void importTargets_given_emptyFile() throws IOException {
        FileCopyUtils.copy("", new FileWriter(path.toFile()));

        Targets targets = targetsImporter.importTargets(path);

        Assertions.assertThat(targets.getUsers()).isEmpty();
    }

    @Test
    void importTargets() throws IOException {
        FileCopyUtils.copy("101=1\n102=2", new FileWriter(path.toFile()));

        Targets targets = targetsImporter.importTargets(path);

        Assertions.assertThat(targets.getUsers().get(0)).isEqualTo(new User(101, 1));
        Assertions.assertThat(targets.getUsers().get(1)).isEqualTo(new User(102, 2));
    }
}