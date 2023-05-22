package me.snowlight.gradestudy;

import java.util.Objects;

class GradeCount {
    private int grade;
    private int count;

    public GradeCount(int grade, int count) {
        this.grade = grade;
        this.count = count;
    }

    public void inc() {
        this.count++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeCount that = (GradeCount) o;
        return grade == that.grade && count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(grade, count);
    }
}
