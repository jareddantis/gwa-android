package gq.jared.pisaygwa;

import java.util.ArrayList;
import java.util.List;

public class YearLevel {

    public static class Level {

        private List<Subject> subjects = new ArrayList<>();

        public List<Subject> subjects() {
            return subjects;
        }

    }

    public static class First extends Level {

        private final List<Subject> subjects = new ArrayList<>();
        private final int color = R.color.colorGrade7;

        public First() {
            this.subjects.add(new Subject("Integrated Science", 1.7, color));
            this.subjects.add(new Subject("Mathematics", 1.7, color));
            this.subjects.add(new Subject("Computer Science", 1.0, color));
            this.subjects.add(new Subject("English", 1.3, color));
            this.subjects.add(new Subject("Filipino", 1.0, color));
            this.subjects.add(new Subject("Social Science", 1.0, color));
            this.subjects.add(new Subject("PEHM", 1.0, color));
            this.subjects.add(new Subject("Values Education", 0.7, color));
            this.subjects.add(new Subject("AdTech", 1.0, color));
        }

        public final List<Subject> subjects() {
            return subjects;
        }

    }

    public static class Second extends Level {

        private final List<Subject> subjects = new ArrayList<>();
        private final int color = R.color.colorGrade8;

        public Second() {
            this.subjects.add(new Subject("Integrated Science", 2.0, color));
            this.subjects.add(new Subject("Mathematics", 1.7, color));
            this.subjects.add(new Subject("Computer Science", 1.0, color));
            this.subjects.add(new Subject("English", 1.3, color));
            this.subjects.add(new Subject("Filipino", 1.0, color));
            this.subjects.add(new Subject("Social Science", 1.0, color));
            this.subjects.add(new Subject("PEHM", 1.0, color));
            this.subjects.add(new Subject("Values Education", 0.7, color));
            this.subjects.add(new Subject("AdTech", 1.0, color));
            this.subjects.add(new Subject("Earth Science", 0.7, color));
        }

        public final List<Subject> subjects() {
            return subjects;
        }

    }

    public static class Third extends Level {

        private final List<Subject> subjects = new ArrayList<>();
        private final int color = R.color.colorGrade9;

        public Third() {
            this.subjects.add(new Subject("Biology", 1.0, color));
            this.subjects.add(new Subject("Chemistry", 1.0, color));
            this.subjects.add(new Subject("Physics", 1.0, color));
            this.subjects.add(new Subject("Mathematics", 1.0, color));
            this.subjects.add(new Subject("English", 1.0, color));
            this.subjects.add(new Subject("Filipino", 1.0, color));
            this.subjects.add(new Subject("Social Science", 1.0, color));
            this.subjects.add(new Subject("PEHM", 1.0, color));
            this.subjects.add(new Subject("Statistics", 1.0, color));
            this.subjects.add(new Subject("Computer Science", 1.0, color));
        }

        public final List<Subject> subjects() {
            return subjects;
        }

    }

    public static class Fourth extends Level {

        private final List<Subject> subjects = new ArrayList<>();
        private final int color = R.color.colorGrade10;

        public Fourth() {
            this.subjects.add(new Subject("Biology", 1.0, color));
            this.subjects.add(new Subject("Chemistry", 1.0, color));
            this.subjects.add(new Subject("Physics", 1.0, color));
            this.subjects.add(new Subject("Mathematics", 1.3, color));
            this.subjects.add(new Subject("English", 1.0, color));
            this.subjects.add(new Subject("Filipino", 1.0, color));
            this.subjects.add(new Subject("Social Science", 1.0, color));
            this.subjects.add(new Subject("PEHM", 1.0, color));
            this.subjects.add(new Subject("Research", 1.0, color));
            this.subjects.add(new Subject("Computer Science", 1.0, color));
            this.subjects.add(new Subject("Elective", 1.0, color));
        }

        public final List<Subject> subjects() {
            return subjects;
        }

    }

    public static class Syp extends Level {

        private final List<Subject> subjects = new ArrayList<>();
        private final int color = R.color.colorGradeSyp;

        public Syp() {
            this.subjects.add(new Subject("Research", 2.0, color));
            this.subjects.add(new Subject("Core", 1.7, color));
            this.subjects.add(new Subject("Elective", 1.7, color));
            this.subjects.add(new Subject("Mathematics", 1.0, color));
            this.subjects.add(new Subject("English", 1.0, color));
            this.subjects.add(new Subject("Filipino", 1.0, color));
            this.subjects.add(new Subject("Social Science", 1.0, color));
        }

        public final List<Subject> subjects() {
            return subjects;
        }

    }

}
