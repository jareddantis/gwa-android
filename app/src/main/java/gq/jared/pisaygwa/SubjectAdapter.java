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

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private List<Subject> subjectList;
    private Context ctx;

    public SubjectAdapter(Context ctx, List<Subject> subjectList) {
        this.ctx = ctx;
        this.subjectList = subjectList;
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
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double newGrade = subjectList.get(getAdapterPosition()).increaseGrade();
                    grade.setText(Subject.formatGrade(newGrade));
                    saveGrades();
                    computeGwa();
                }
            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double newGrade = subjectList.get(getAdapterPosition()).decreaseGrade();
                    grade.setText(Subject.formatGrade(newGrade));
                    saveGrades();
                    computeGwa();
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
            color = ContextCompat.getColor(ctx, subject.color());
        double grade = subject.getGrade();

        // Set color
        holder.mask.setCardBackgroundColor(color);
        holder.plus.setColorFilter(color);
        holder.minus.setColorFilter(color);
        holder.grade.setTextColor(color);

        // Populate data
        String savedGrade = Subject.formatGrade(grade);
        holder.grade.setText(savedGrade);
        holder.name.setText(name);
        holder.units.setText(units);
        holder.icon.setImageDrawable(ContextCompat.getDrawable(ctx, icon));
    }

    private void computeGwa() {
        TextView result = ((Activity) ctx).findViewById(R.id.result);

        double total = 0.0, units = 0.0, average;
        for (int i = 0; i < subjectList.size(); i++) {
            total += subjectList.get(i).getWeightedGrade();
            units += subjectList.get(i).units();
        }

        DecimalFormat df = new DecimalFormat("#.#####");
        df.setMinimumFractionDigits(3);
        df.setMaximumFractionDigits(3);
        average = total / units;
        result.setText(df.format(average));
    }

    private void saveGrades() {
        String[] data = new String[subjectList.size()];
        for (int i = 0; i < subjectList.size(); i++)
            data[i] = subjectList.get(i).toString();

        SharedPreferences sharedPrefs = ctx.getSharedPreferences("pisaygwa",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("grades", Arrays.toString(data));
        editor.apply();
    }

}
