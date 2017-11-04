package gq.jared.pisaygwa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs;
    private List<Subject> subjectList;
    private SubjectAdapter subjectAdapter;
    private TextView result;
    private int gradeLevel;
    private boolean firstRun = true;

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
        subjectList = new ArrayList<>();
        subjectAdapter = new SubjectAdapter(this, subjectList);
        RecyclerView sView = findViewById(R.id.subjectsList);
        sView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        sView.setItemAnimator(new DefaultItemAnimator());
        sView.setAdapter(subjectAdapter);

        // Setup dropdown
        ArrayAdapter<CharSequence> adapter = LevelAdapter.createFromResource(this,
                R.array.levels, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.levelchooser);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                // Set grade level
                gradeLevel = position;

                // Regenerate subject list
                subjectAdapter.notifyItemRangeRemoved(0, subjectAdapter.getItemCount());
                switchLevel(gradeLevel);
                restoreGrades();
                subjectAdapter.notifyItemRangeInserted(0, subjectList.size());
                scrollToTop();

                // Save
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt("gradeLevel", gradeLevel);
                editor.apply();
                if (firstRun)
                    firstRun = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // nothing
            }
        });
        spinner.setSelection(gradeLevel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                subjectAdapter.clearGrades();
                break;
            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(intent);
                break;
        }
        return false;
    }

    private YearLevel.Level getYearLevelObj(int gradeLevel) {
        YearLevel.Level level;

        switch (gradeLevel) {
            case 0:
                level = new YearLevel.First();
                break;
            case 1:
                level = new YearLevel.Second();
                break;
            case 2:
                level = new YearLevel.Third();
                break;
            case 3:
                level = new YearLevel.Fourth();
                break;
            default:
                level = new YearLevel.Syp();
        }

        return level;
    }

    private void switchLevel(int gradeLevel) {
        int color, darker;

        switch (gradeLevel) {
            case 0:
                color = R.color.colorGrade7;
                darker = R.color.darkGrade7;
                break;
            case 1:
                color = R.color.colorGrade8;
                darker = R.color.darkGrade8;
                break;
            case 2:
                color = R.color.colorGrade9;
                darker = R.color.darkGrade9;
                break;
            case 3:
                color = R.color.colorGrade10;
                darker = R.color.darkGrade10;
                break;
            default:
                color = R.color.colorGradeSyp;
                darker = R.color.darkGradeSyp;
        }

        setColor(ContextCompat.getColor(this, color),
                ContextCompat.getColor(this, darker));
        subjectList.clear();
        subjectList.addAll(getYearLevelObj(gradeLevel).subjects());
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

    private void restoreGrades() {
        if (firstRun) {
            // Restore grades
            if (sharedPrefs.contains("grades") && sharedPrefs.contains("gwa")) {
                String[] saved = sharedPrefs.getString("grades", "").split(",");
                double sU = getYearLevelObj(sharedPrefs.getInt("gradeLevel", gradeLevel)).units(),
                        cU = getYearLevelObj(gradeLevel).units();
                if (saved.length == subjectList.size() && sU == cU) {
                    for (int i = 0; i < subjectList.size(); i++)
                        subjectList.get(i).setGrade(Double.parseDouble(saved[i]));
                }
            }

            // Restore GWA
            result.setText(sharedPrefs.getString("gwa", "1.000"));
        } else
            result.setText("1.000");
    }

    private void scrollToTop() {
        ScrollView sv = findViewById(R.id.scrollview);
        sv.fullScroll(ScrollView.FOCUS_UP);
    }

}
