package me.snowlight.gradestudy;

import java.util.Collection;
import java.util.Map;

public class ApplyResult {
    private final Map<Integer, GradeCount> map;

    public ApplyResult(Map<Integer, GradeCount> map) {
        this.map = map;
    }

    public Collection<GradeCount> getGradeCounts() {
        return map.values();
    }
}
