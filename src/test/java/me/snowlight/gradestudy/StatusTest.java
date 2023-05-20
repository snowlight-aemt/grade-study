package me.snowlight.gradestudy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Copy;
import org.springframework.util.FileCopyUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class StatusTest {
    Path path = Paths.get("build/status");
    Status status = new Status(path);

    @BeforeEach
    public void setUp() throws IOException {
        Files.deleteIfExists(path);
    }

    @Test
    public void noStatusFile() {
        AdvanceStatus advanceStatus = status.get();
        assertThat(advanceStatus).isNull();
    }

    @Test
    public void set() throws IOException {
        status.set(AdvanceStatus.GENERATING);

        List<String> statuses = Files.readAllLines(path);

        Assertions.assertThat(statuses.get(0)).isEqualTo(AdvanceStatus.GENERATING.name());
    }

    @Test
    public void get() throws IOException {
        FileCopyUtils.copy(AdvanceStatus.GENERATING.name(), new FileWriter(path.toFile()));

        AdvanceStatus advanceStatus = status.get();
        Assertions.assertThat(advanceStatus).isEqualTo(AdvanceStatus.GENERATING);
    }

    private class Status {
        public Status(Path path) {

        }

        public AdvanceStatus get() {
            if (!Files.exists(path)) return null;
            try {
                List<String> status = Files.readAllLines(path);
                return AdvanceStatus.valueOf(status.get(0));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void set(AdvanceStatus status) {
            try {
                Files.write(path, status.name().getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public enum AdvanceStatus {
        GENERATING
    }
}
