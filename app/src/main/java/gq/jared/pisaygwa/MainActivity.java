package gq.jared.pisaygwa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs;
    private List<Subject> subjectList;
    private SubjectAdapter subjectAdapter;
    private TextView result;
    private int gradeLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Action bar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get the set level or use the default
        result = findViewById(R.id.result);
        sharedPrefs = this.getSharedPreferences("pisaygwa", Context.MODE_PRIVATE);
        gradeLevel = sharedPrefs.getInt("gradeLevel", 1);

        // Setup view
        subjectList = switchLevel(gradeLevel);
        subjectAdapter = new SubjectAdapter(this, subjectList);
        RecyclerView sView = findViewById(R.id.subjectsList);
        sView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        sView.setItemAnimator(new DefaultItemAnimator());
        sView.addItemDecoration(new DividerItemDecoration(sView.getContext(),
                sView.getLayoutManager().getLayoutDirection()));
        sView.setAdapter(subjectAdapter);

        // Setup dropdown
        ArrayAdapter<CharSequence> adapter = LevelAdapter.createFromResource(this,
                R.array.levels, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.levelchooser);
        spinner.setAdapter(adapter);
        spinner.setSelection(gradeLevel);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                // Set grade level
                gradeLevel = position;

                // Regenerate subject list
                subjectAdapter.notifyItemRangeRemoved(0, subjectList.size() - 1);
                subjectList.clear();
                subjectList.addAll(switchLevel(gradeLevel));
                subjectAdapter.notifyItemRangeInserted(0, subjectList.size() - 1);
                scrollToTop();

                // Save
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt("gradeLevel", gradeLevel);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // stub
            }
        });
    }

    private List<Subject> switchLevel(int gradeLevel) {
        YearLevel.Level level;
        int color, darker;

        switch (gradeLevel) {
            case 0:
                level = new YearLevel.First();
                color = R.color.colorGrade7;
                darker = R.color.darkGrade7;
                break;
            case 1:
                level = new YearLevel.Second();
                color = R.color.colorGrade8;
                darker = R.color.darkGrade8;
                break;
            case 2:
                level = new YearLevel.Third();
                color = R.color.colorGrade9;
                darker = R.color.darkGrade9;
                break;
            case 3:
                level = new YearLevel.Fourth();
                color = R.color.colorGrade10;
                darker = R.color.darkGrade10;
                break;
            default:
                level = new YearLevel.Syp();
                color = R.color.colorGradeSyp;
                darker = R.color.darkGradeSyp;
        }

        setColor(ContextCompat.getColor(this, color),
                ContextCompat.getColor(this, darker));
        List<Subject> subjects = level.subjects();
        return restoreGrades(subjects);
    }

    private void setColor(int color, int darker) {
        CardView barBg = findViewById(R.id.barBg);
        barBg.setCardBackgroundColor(color);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(darker);
            window.setStatusBarColor(darker);
        }
    }

    private List<Subject> restoreGrades(List<Subject> list) {
        if (sharedPrefs.contains("grades") && sharedPrefs.contains("gwa")) {
            // Restore grades
            String[] saved = sharedPrefs.getString("grades", "").split(",");
            if (saved.length == list.size()) {
                for (int i = 0; i < list.size(); i++) {
                    String[] entry = saved[i].split("=");
                    if (list.get(i).name().equals(entry[0]))
                        list.get(i).setGrade(Double.parseDouble(entry[1]));
                }
            }
        }

        // Restore GWA
        result.setText(sharedPrefs.getString("gwa", "1.000"));

        // Return list
        return list;
    }

    private void scrollToTop() {
        ScrollView sv = findViewById(R.id.scrollview);
        sv.fullScroll(ScrollView.FOCUS_UP);
    }

}
