package tk.aureljared.pisaygwa;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private final Activity ctx;

    private final ArrayList<HashMap<String, String>> subjects;

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
        int size = (subjects == null) ? 0 : subjects.size();
        return size;
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
        holder.plus.setColorFilter(color);
        holder.minus.setColorFilter(color);
        holder.grade.setTextColor(color);

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
                if (currentGrade == 1.0)
                    return;
                else {
                    Double increment;
                    if (currentGrade == 5.0 || currentGrade == 4.0)
                        increment = 1.0;
                    else
                        increment = 0.25;

                    String newGrade = formatGrade(currentGrade - increment);
                    grade.setText(newGrade);
                    calculateGwa();
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Double currentGrade = Double.valueOf((String) grade.getText());
                if (currentGrade == 5.0)
                    return;
                else {
                    Double decrement;
                    if (currentGrade == 3.0 || currentGrade == 4.0)
                        decrement = 1.0;
                    else
                        decrement = 0.25;

                    String newGrade = formatGrade(currentGrade + decrement);
                    grade.setText(newGrade);
                    calculateGwa();
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

    // TODO: Correctly calculate GWA for offscreen grades
    public void calculateGwa() {
        int gradeLevel = MainActivity.getGradeLevel();

        // Get sum
        RecyclerView rv = ((Activity) ctx).findViewById(R.id.subjectsList);
        Double total = 0.0, units = 0.0;
        Log.d("PisayGWA", "------------------");
        int count = MainActivity.getItemCount();
        for (int i = 0; i < count; i++) {
            ViewHolder holder = (ViewHolder) rv.findViewHolderForAdapterPosition(i);
            try {
                Double grade = Double.valueOf((String) holder.grade.getText());
                Double subUnits = SubjectParser.getUnits(gradeLevel, i);
                Double weighted = subUnits * grade;
                Log.d("PisayGWA", "sub = ".concat(Integer.toString(i)).concat(", units = ").concat(Double.toString(subUnits)));
                Log.d("PisayGWA", "grade = ".concat(Double.toString(grade)).concat(", weighted = ").concat(Double.toString(weighted)));
                total += weighted;
                units += subUnits;
            } catch (NullPointerException ex) {
                Log.w("PisayGWA", "Null pointer at i = ".concat(Integer.toString(i)));
                Log.w("PisayGWA", "where item count = ".concat(Integer.toString(count)));
                return;
            }
        }

        // Get average
        Log.d("PisayGWA", "------------------");
        Double average = total / units;
        Log.d("PisayGWA", "total = ".concat(Double.toString(total)));
        Log.d("PisayGWA", "units = ".concat(Double.toString(units)));
        Log.d("PisayGWA", "average = ".concat(Double.toString(average)));
        TextView result = ((Activity) ctx).findViewById(R.id.result);
        DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.DOWN);
        df.setMinimumFractionDigits(3);
        result.setText(df.format(average));
    }

    private String formatGrade(Double grade) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setMinimumFractionDigits(2);
        return df.format(grade);
    }

}
