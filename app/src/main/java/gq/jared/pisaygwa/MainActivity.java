package gq.jared.pisaygwa;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.ArgbEvaluator;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private boolean isDark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // Action bar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayShowTitleEnabled(false);
        } else {
            Log.e("pisaygwa", "Null support action bar");
        }

        // Get the set level or use the default
        result = findViewById(R.id.result);
        sharedPrefs = this.getSharedPreferences("pisaygwa", Context.MODE_PRIVATE);
        gradeLevel = sharedPrefs.getInt("gradeLevel", 4);

        // Switch theme if necessary
        isDark = sharedPrefs.getBoolean("isDark", false);

        // Setup view
        subjectList = new ArrayList<>();
        subjectAdapter = new SubjectAdapter(this, subjectList);
        LinearLayoutManager lm = new GwaLinearLayoutManager(getApplicationContext());
        RecyclerView sView = findViewById(R.id.subjectsList);
        sView.setLayoutManager(lm);
        sView.setItemAnimator(new DefaultItemAnimator());
        sView.setNestedScrollingEnabled(false);
        sView.setAdapter(subjectAdapter);

        // Check for updates
        Updater updater = new Updater(this);
        updater.startUpdateCheck(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        // Setup dropdown
        MenuItem item = menu.findItem(R.id.levelchooser);
        Spinner spinner = (Spinner) item.getActionView();
        ArrayAdapter<CharSequence> adapter = LevelAdapter.createFromResource(this,
                R.array.levels, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        spinner.setGravity(Gravity.END);
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
                loadTheme();
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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                subjectAdapter.clearGrades();
                break;
            case R.id.action_share:
                createShareIntent();
                break;
            case R.id.action_theme:
                isDark = !isDark;
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("isDark", isDark);
                editor.apply();
                loadTheme();
                break;
            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
        return false;
    }

    private void createShareIntent() {
        String grades = subjectAdapter.dumpGrades();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Grades");
        sendIntent.putExtra(Intent.EXTRA_TEXT, grades);
        startActivity(Intent.createChooser(sendIntent, getResources()
                .getString(R.string.action_share)));
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
        // Change action bar background
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(color));
        }

        // Change system bars background
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(darker);
            window.setStatusBarColor(darker);
        }
    }

    private void restoreGrades() {
        String defaultGwa = getResources().getString(R.string.default_gwa);

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
            result.setText(sharedPrefs.getString("gwa", defaultGwa));
        } else
            result.setText(defaultGwa);
    }

    private void scrollToTop() {
        NestedScrollView sv = findViewById(R.id.scrollView);
        sv.fullScroll(NestedScrollView.FOCUS_UP);
    }

    private void loadTheme() {
        final View rootView = getWindow().getDecorView(),
             subjectLayout = findViewById(R.id.subjectsListLayout);
        int gray = ContextCompat.getColor(this, R.color.darkGradeSyp),
            white = ContextCompat.getColor(this, android.R.color.white);
        int from = isDark ? white : gray,
            to = isDark ? gray : white;

        @SuppressLint("RestrictedApi")
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), from, to);
        colorAnimation.setDuration(400);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int bgColor = (int) animator.getAnimatedValue();
                rootView.setBackgroundColor(bgColor);
                subjectLayout.setBackgroundColor(bgColor);
            }

        });
        colorAnimation.start();
        subjectAdapter.switchTheme(isDark);
    }

}
