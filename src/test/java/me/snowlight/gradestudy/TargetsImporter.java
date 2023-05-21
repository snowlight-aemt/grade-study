package me.snowlight.gradestudy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class TargetsImporter {
    public Targets importTargets(Path path) {
        if (!Files.exists(path)) throw new NoTargetsFileException();
        try {
            List<String> lines = Files.readAllLines(path);
            List<User> users = new ArrayList<>();
            for (String items : lines) {
                String[] item = items.split("=");
                if (item.length != 2) throw new TargetsFileBadFormatException();
                users.add(new User(Integer.parseInt(item[0]), Integer.parseInt(item[1])));
            }
            return new Targets(users);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
