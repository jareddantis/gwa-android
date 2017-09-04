package tk.aureljared.pisaygwa;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class SubjectParser {

    protected static ArrayList<ArrayList<HashMap<String, String>>> subjects = null;

    protected static void parse(Context ctx) {
        try {
            InputStream is = ctx.getAssets().open("subjects.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonData = new String(buffer, "UTF-8");
            subjects = jsonToArrayList(jsonData);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected static ArrayList<HashMap<String, String>> getSubjects(int gradeLevel) {
        return subjects.get(gradeLevel);
    }

    protected static Double getUnits(int gradeLevel, int pos) {
        ArrayList<HashMap<String, String>> subjects = getSubjects(gradeLevel);
        return Double.valueOf(subjects.get(pos).get("units"));
    }

    protected static Double getTotalUnits(int gradeLevel) {
        ArrayList<HashMap<String, String>> subjects = getSubjects(gradeLevel);
        Double units = 0.0;
        for (int i = 0; i < subjects.size(); i++)
            Double.valueOf(subjects.get(i).get("units"));
        return units;
    }

    protected static int getColor(Context ctx, int gradeLevel) {
        int color;

        switch (gradeLevel) {
            case 0:
                color = ContextCompat.getColor(ctx, R.color.colorGrade7);
                break;
            case 1:
                color = ContextCompat.getColor(ctx, R.color.colorGrade8);
                break;
            case 2:
                color = ContextCompat.getColor(ctx, R.color.colorGrade9);
                break;
            case 3:
                color = ContextCompat.getColor(ctx, R.color.colorGrade10);
                break;
            case 4:
                color = ContextCompat.getColor(ctx, R.color.colorGradeSyp);
                break;
            default:
                color = ContextCompat.getColor(ctx, R.color.colorGrade7);
                break;
        }

        return color;
    }

    private static ArrayList<ArrayList<HashMap<String, String>>> jsonToArrayList(String json) {
        ArrayList<ArrayList<HashMap<String, String>>> subjects = new ArrayList<>();

        try {
            JSONArray levels = new JSONObject(json).getJSONArray("subjects");

            for (int i = 0; i < levels.length(); i++) {
                JSONArray level = levels.getJSONArray(i);
                ArrayList<HashMap<String, String>> levelSubjects = new ArrayList<>();

                for (int j = 0; j < level.length(); j++) {
                    JSONObject subject = level.getJSONObject(j);
                    HashMap<String, String> subjectItem = new HashMap<>();
                    //Log.d("Details", subject.getString("subject"));

                    String subjectName = subject.getString("subject");
                    String subjectUnits = Double.toString(subject.getDouble("units"));
                    subjectItem.put("subject", subjectName);
                    subjectItem.put("units", subjectUnits);

                    levelSubjects.add(subjectItem);
                }

                subjects.add(levelSubjects);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return subjects;
    }

}
