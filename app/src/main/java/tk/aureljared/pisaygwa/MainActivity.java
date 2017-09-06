package tk.aureljared.pisaygwa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static int gradeLevel;

    private ArrayList<HashMap<String, String>> level;

    private static SharedPreferences appPrefs;

    private static TextView result;

    private SubjectAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        result = findViewById(R.id.result);

        // Action bar
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get the set level or use the default
        appPrefs = this.getPreferences(Context.MODE_PRIVATE);
        gradeLevel = appPrefs.getInt("gradeLevel", 1);

        // Parse subjects from JSON
        SubjectParser.parse(this);
        level = new ArrayList<>();
        retrieveSubjects();
        sa = new SubjectAdapter(this, level);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        RecyclerView subjectList = findViewById(R.id.subjectsList);
        subjectList.setAdapter(sa);
        subjectList.setLayoutManager(lm);
        subjectList.setItemAnimator(new DefaultItemAnimator());

        // Dropdown
        ArrayAdapter<CharSequence> adapter = LevelAdapter.createFromResource(this, R.array.levels, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.levelchooser);
        spinner.setAdapter(adapter);
        spinner.setSelection(gradeLevel);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Change UI colors
                gradeLevel = position;
                setColor();

                // Regenerate subject list
                retrieveSubjects();
                sa.notifyDataSetChanged();
                sa.emptyGrades();

                // Save
                SharedPreferences.Editor editor = appPrefs.edit();
                editor.putInt("gradeLevel", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // stub
            }
        });

        // Restore saved grades
        sa.restoreGrades(appPrefs.getString("grades", ""));
        //subjectList.getRecycledViewPool().clear();
        //updateGwa(sa.getGwa());
        updateGwa("1.000");
    }

    protected void retrieveSubjects() {
        ArrayList<HashMap<String, String>> source = SubjectParser.getSubjects(gradeLevel);
        level.clear();
        for (int i = 0; i < source.size(); i++)
            level.add(source.get(i));
    }

    protected void setColor() {
        int color = SubjectParser.getColor(this, gradeLevel);
        getWindow().setNavigationBarColor(color);
        result.setTextColor(color);
    }

    protected static int getGradeLevel() {
        return gradeLevel;
    }

    protected static void updateGwa(String r) {
        result.setText(r);
    }

    protected static void saveGrades(double[] grades) {
        String gradeStr = Arrays.toString(grades);
        SharedPreferences.Editor e = appPrefs.edit();
        e.putString("grades", gradeStr);
        e.apply();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("gradeLevel", gradeLevel);
    }

}
