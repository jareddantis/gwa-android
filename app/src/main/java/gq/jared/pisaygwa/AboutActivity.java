package gq.jared.pisaygwa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.psdev.licensesdialog.LicensesDialog;

public class AboutActivity extends AppCompatActivity {

    LinearLayout actionUpdate, actionGithub, actionReport, actionLicenses,
            actionTwitter, actionWebsite;
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Action bar
        Toolbar myToolbar = findViewById(R.id.aboutToolbar);
        setSupportActionBar(myToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e("pisaygwa", "Null support action bar");
        }

        // Switch theme if necessary
        SharedPreferences sharedPrefs = this.getSharedPreferences("pisaygwa", Context.MODE_PRIVATE);
        boolean isDark = sharedPrefs.getBoolean("isDark", false);
        loadTheme(isDark);

        // Populate app version
        String appVersion = BuildConfig.DEBUG ? "Debug release " : "Release ";
        try {
            appVersion += getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            appVersion += "0";
        }
        version = findViewById(R.id.app_version);
        version.setText(appVersion);

        // Actions
        actionUpdate = findViewById(R.id.action_update);
        actionGithub = findViewById(R.id.action_github);
        actionReport = findViewById(R.id.action_report);
        actionLicenses = findViewById(R.id.action_licenses);
        actionTwitter = findViewById(R.id.action_twitter);
        actionWebsite = findViewById(R.id.action_website);

        // Listeners
        actionUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updater updater = new Updater(AboutActivity.this);
                updater.startUpdateCheck(false);
            }
        });
        actionGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://github.com/illustra/gwa-android");
            }
        });
        actionReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // From https://github.com/kabouzeid/Phonograph
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:me@contact.jared.gq"));
                intent.putExtra(Intent.EXTRA_EMAIL, "me@contact.jared.gq");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Pisay GWA");
                startActivity(Intent.createChooser(intent, "Send e-mail"));
            }
        });
        actionLicenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLicenseDialog();
            }
        });
        actionTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTwitterInstalled())
                    openUrl("twitter://user?screen_name=aureljared");
                else
                    openUrl("https://twitter.com/#!/aureljared");
            }
        });
        actionWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://jared.gq");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // From https://github.com/kabouzeid/Phonograph
    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    // From https://github.com/kabouzeid/Phonograph
    private void showLicenseDialog() {
        LicensesDialog licensesDialog = new LicensesDialog.Builder(this)
                .setNotices(R.raw.licenses)
                .setTitle(R.string.license)
                .setIncludeOwnLicense(true).build();
        licensesDialog.showAppCompat();
    }

    private boolean isTwitterInstalled() {
        try {
            return getPackageManager().getApplicationInfo("com.twitter.android", 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void loadTheme(boolean isDark) {
        View rootView = getWindow().getDecorView(),
                rootLayout = findViewById(R.id.rootLayout);
        int color = isDark ? R.color.darkGradeSyp : android.R.color.white,
                bgColor = ContextCompat.getColor(this, color);
        rootView.setBackgroundColor(bgColor);
        rootLayout.setBackgroundColor(bgColor);
    }

}
