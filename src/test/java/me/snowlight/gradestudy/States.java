package me.snowlight.gradestudy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class States {
    private final Path path;

    // TODO @Value 사용 방법 (의존성 주입)
    public States(@Value("${states.path}") Path path) {
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
