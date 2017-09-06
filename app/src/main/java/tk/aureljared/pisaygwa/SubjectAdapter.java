package tk.aureljared.pisaygwa;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private final Activity ctx;

    private final ArrayList<HashMap<String, String>> subjects;

    private static double[] grades;

    private static double units;

    public SubjectAdapter(Activity ctx, ArrayList<HashMap<String, String>> subjects) {
        this.ctx = ctx;
        this.subjects = subjects;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mask;
        public TextView name, units, grade;
        public ImageView icon, plus, minus;

        public ViewHolder(View v) {
            super(v);
            mask = v.findViewById(R.id.subjectIconMask);
            name = v.findViewById(R.id.subjectName);
            units = v.findViewById(R.id.subjectUnits);
            grade = v.findViewById(R.id.subjectGradeValue);
            icon = v.findViewById(R.id.subjectIcon);
            plus = v.findViewById(R.id.subjectGradePlus);
            minus = v.findViewById(R.id.subjectGradeMinus);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return (subjects == null) ? 0 : subjects.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // Get data
        HashMap<String, String> subject = subjects.get(position);
        String subjectName = subject.get("subject");
        String subjectUnits = subject.get("units").concat(" units");
        int subjectIcon = this.getIcon(subjectName);

        // Determine color
        int level = MainActivity.getGradeLevel();
        int color = SubjectParser.getColor(ctx, level);
        holder.mask.setCardBackgroundColor(color);

        // Set color
        String savedGrade = formatGrade(grades[position]);
        holder.plus.setColorFilter(color);
        holder.minus.setColorFilter(color);
        holder.grade.setTextColor(color);
        holder.grade.setText(savedGrade);

        // Populate data
        holder.name.setText(subjectName);
        holder.units.setText(subjectUnits);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.icon.setImageDrawable(ctx.getResources().getDrawable(subjectIcon, ctx.getTheme()));
        } else {
            holder.icon.setImageDrawable(ctx.getResources().getDrawable(subjectIcon));
        }

        // Set listeners for +/-
        final TextView grade = holder.grade;
        holder.plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Double currentGrade = Double.valueOf((String) grade.getText());
                if (currentGrade != 1.0) {
                    Double increment;
                    if (currentGrade == 5.0 || currentGrade == 4.0)
                        increment = 1.0;
                    else
                        increment = 0.25;

                    String newGrade = formatGrade(currentGrade - increment);
                    grade.setText(newGrade);

                    // Add grade to array
                    grades[position] = Double.valueOf(newGrade);
                    MainActivity.updateGwa(getGwa());
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Double currentGrade = Double.valueOf((String) grade.getText());
                if (currentGrade != 5.0) {
                    Double decrement;
                    if (currentGrade == 3.0 || currentGrade == 4.0)
                        decrement = 1.0;
                    else
                        decrement = 0.25;

                    String newGrade = formatGrade(currentGrade + decrement);
                    grade.setText(newGrade);

                    // Add grade to array
                    grades[position] = Double.valueOf(newGrade);
                    MainActivity.updateGwa(getGwa());
                }
            }
        });
    }

    private int getIcon(String subject) {
        int icon = R.drawable.pencil;

        switch(subject) {
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

    public static void restoreGrades(String saved) {
        emptyGrades();
        if (!saved.equals("null") && saved.length() >= 5) {
            saved = saved.replaceAll("\\s+", "");
            saved = saved.substring(1, saved.length() - 1);
            String[] savedGrades = saved.split(",");
            for (int i = 0; i < savedGrades.length; i++)
                grades[i] = Double.valueOf(savedGrades[i]);
        }
    }

    public static String getGwa() {
        // Save grades first
        MainActivity.saveGrades(grades);

        // Get sum
        int gradeLevel = MainActivity.getGradeLevel();
        Double total = 0.0;
        for (int i = 0; i < grades.length; i++) {
            Double grade = grades[i];
            Double subUnits = SubjectParser.getUnits(gradeLevel, i);
            Double weighted = subUnits * grade;
            total += weighted;
        }

        // Get average
        Double average = total / units;
        DecimalFormat df = new DecimalFormat("#.#####");
        df.setMinimumFractionDigits(3);
        df.setMaximumFractionDigits(3);
        return df.format(average);
    }

    protected static void emptyGrades() {
        int gradeLevel = MainActivity.getGradeLevel();
        int num = SubjectParser.getNumOfSubjects(gradeLevel);
        grades = new double[num];
        units = SubjectParser.getTotalUnits(gradeLevel);
        for (int i = 0; i < num; i++)
            grades[i] = 1.0;
    }

    private String formatGrade(Double grade) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setMinimumFractionDigits(2);
        return df.format(grade);
    }

}
