package me.snowlight.gradestudy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class States {
    private final Path path;

    public States(Path path) {
        this.path = path;
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
