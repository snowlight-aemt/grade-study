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
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class StatesTest {
    Path path = Paths.get("build/status");
    States states = new States(path);

    @BeforeEach
    public void setUp() throws IOException {
        Files.deleteIfExists(path);
    }

    @Test
    public void noStatusFile() {
        AdvanceStatus advanceStatus = states.get();
        assertThat(advanceStatus).isNull();
    }

    @Test
    public void set() throws IOException {
        states.set(AdvanceStatus.GENERATING);

        List<String> statuses = Files.readAllLines(path);

        Assertions.assertThat(statuses.get(0)).isEqualTo(AdvanceStatus.GENERATING.name());
    }

    @Test
    public void get() throws IOException {
        FileCopyUtils.copy(AdvanceStatus.GENERATING.name(), new FileWriter(path.toFile()));

        AdvanceStatus advanceStatus = states.get();
        Assertions.assertThat(advanceStatus).isEqualTo(AdvanceStatus.GENERATING);
    }

}
