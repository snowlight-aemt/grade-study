package me.snowlight.gradestudy;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
class TargetExporter {
    public void export(Path path, Targets targets) {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (User user : targets.getUsers()) {
                bw.write(user.getId() + "=" + user.getGrade());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
