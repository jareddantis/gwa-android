package gq.jared.pisaygwa;

import java.text.DecimalFormat;

public class Subject {

    private String name;
    private double units;
    private double grade;
    private int icon;
    private int color;

    public Subject(String name, double units, int color) {
        this.name = name;
        this.units = units;
        this.grade = 1.0;
        this.icon = determineIcon(name);
        this.color = color;
    }

    private final int determineIcon(String name) {
        int icon = R.drawable.pencil;

        switch (name) {
            case "Earth Science":
            case "Integrated Science":
                icon = R.drawable.earth;
                break;
            case "Physics":
                icon = R.drawable.body;
                break;
            case "Biology":
                icon = R.drawable.bug;
                break;
            case "Chemistry":
                icon = R.drawable.bubble;
                break;
            case "Research":
                icon = R.drawable.search;
                break;
            case "Mathematics":
                icon = R.drawable.sigma;
                break;
            case "Statistics":
                icon = R.drawable.trend;
                break;
            case "Core":
                icon = R.drawable.dot;
                break;
            case "Elective":
                icon = R.drawable.heart;
                break;
            case "Computer Science":
                icon = R.drawable.memory;
                break;
            case "English":
            case "Filipino":
                icon = R.drawable.quotes;
                break;
            case "Social Science":
                icon = R.drawable.social;
                break;
            case "PEHM":
                icon = R.drawable.run;
                break;
            case "Values Education":
                icon = R.drawable.smile;
                break;
        }

        return icon;
    }

    public final String name() {
        return this.name;
    }

    public final double units() {
        return this.units;
    }

    public final int icon() {
        return this.icon;
    }

    public final int color() {
        return this.color;
    }

    public static final String formatGrade(double grade) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setMinimumFractionDigits(2);
        return df.format(grade);
    }

    public final void setGrade(double grade) {
        this.grade = grade;
    }

    public final double getGrade() {
        return grade;
    }

    public final double getWeightedGrade() {
        return grade * units;
    }

    public final double increaseGrade() {
        if (grade > 1.0) {
            if (grade > 3.0)
                grade -= 1.0;
            else
                grade -= 0.25;
        }
        return grade;
    }

    public final double decreaseGrade() {
        if (grade < 5.0) {
            if (grade > 2.75)
                grade += 1.0;
            else
                grade += 0.25;
        }
        return grade;
    }

    @Override
    public String toString() {
        return name + "=" + Double.toString(units);
    }
}
