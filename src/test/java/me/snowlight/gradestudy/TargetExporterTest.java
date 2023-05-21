package me.snowlight.gradestudy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class TargetExporterTest {
    @Test
    void export() throws IOException {
        TargetExporter targetExporter = new TargetExporter();
        Path path = Paths.get("build/targets");
        targetExporter.export(path, new Targets(
                List.of(new User(101, 1),
                        new User(102, 2))));

        List<String> lines = Files.readAllLines(path);
        Assertions.assertThat(lines.get(0)).isEqualTo("101=1");
        Assertions.assertThat(lines.get(1)).isEqualTo("102=2");
    }
}