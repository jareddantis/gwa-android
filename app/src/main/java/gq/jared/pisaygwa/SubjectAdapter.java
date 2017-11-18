package gq.jared.pisaygwa;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private List<Subject> subjectList;
    private Context ctx;
    private String gwa;
    private boolean isDark = false;

    SubjectAdapter(Context ctx, List<Subject> subjectList) {
        this.ctx = ctx;
        this.subjectList = subjectList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView mask;
        TextView name, units, grade;
        ImageView icon, plus, minus;

        ViewHolder(View v) {
            super(v);
            mask = v.findViewById(R.id.subjectIconMask);
            name = v.findViewById(R.id.subjectName);
            units = v.findViewById(R.id.subjectUnits);
            grade = v.findViewById(R.id.subjectGradeValue);
            icon = v.findViewById(R.id.subjectIcon);
            plus = v.findViewById(R.id.subjectGradePlus);
            minus = v.findViewById(R.id.subjectGradeMinus);
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double newGrade = subjectList.get(getAdapterPosition()).increaseGrade();
                    grade.setText(Subject.formatGrade(newGrade));
                    computeGwa();
                    saveGrades();
                }
            });
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double newGrade = subjectList.get(getAdapterPosition()).decreaseGrade();
                    grade.setText(Subject.formatGrade(newGrade));
                    computeGwa();
                    saveGrades();
                }
            });
        }

    }

    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_item, parent, false);
        return new SubjectAdapter.ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    @Override
    public void onBindViewHolder(SubjectAdapter.ViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        String name = subject.name(),
               units = Double.toString(subject.units()).concat(" units");
        int icon = subject.icon(),
            white = ContextCompat.getColor(ctx, android.R.color.white),
            black = ContextCompat.getColor(ctx, android.R.color.black),
            color = ContextCompat.getColor(ctx, subject.color());
        double grade = subject.getGrade();

        // Set color
        holder.mask.setCardBackgroundColor(color);
        holder.plus.setColorFilter(isDark ? white : color);
        holder.minus.setColorFilter(isDark ? white : color);
        holder.grade.setTextColor(isDark ? white : color);
        holder.name.setTextColor(isDark ? white : black);
        holder.units.setTextColor(isDark ? white : black);

        // Populate data
        String savedGrade = Subject.formatGrade(grade);
        holder.grade.setText(savedGrade);
        holder.name.setText(name);
        holder.units.setText(units);
        holder.icon.setImageResource(icon);
    }

    private void computeGwa() {
        TextView result = ((Activity) ctx).findViewById(R.id.result);

        double total = 0.0, units = 0.0, average;
        for (int i = 0; i < subjectList.size(); i++) {
            total += subjectList.get(i).getWeightedGrade();
            units += subjectList.get(i).units();
        }

        DecimalFormat df = new DecimalFormat("#.000");
        df.setRoundingMode(RoundingMode.DOWN);
        average = total / units;
        gwa = df.format(average);
        result.setText(gwa);
    }

    private void saveGrades() {
        String[] data = new String[subjectList.size()];
        for (int i = 0; i < subjectList.size(); i++)
            data[i] = Double.toString(subjectList.get(i).getGrade());

        SharedPreferences sharedPrefs = ctx.getSharedPreferences("pisaygwa",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("grades", Arrays.toString(data).replace("[", "")
                .replace("]", "").replaceAll("\\s", ""));
        editor.putString("gwa", gwa);
        editor.apply();
    }

    void clearGrades() {
        for (int i = 0; i < subjectList.size(); i++)
            subjectList.get(i).setGrade(1.0);
        computeGwa();
        saveGrades();
        notifyDataSetChanged();
    }

    String dumpGrades() {
        computeGwa();
        saveGrades();
        StringBuilder grades = new StringBuilder();

        for (int i = 0; i < getItemCount(); i++) {
            Subject subject = subjectList.get(i);
            grades.append(subject.name());
            grades.append(": ");
            grades.append(subject.getGrade());
            grades.append("\n");
        }

        grades.append("\n").append("GWA: ").append(gwa);
        return grades.toString();
    }

    public void switchTheme(boolean isDark) {
        this.isDark = isDark;
        notifyDataSetChanged();
    }

}
