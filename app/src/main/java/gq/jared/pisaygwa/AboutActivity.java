package gq.jared.pisaygwa;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Populate app version
        String appVersion = "Release ";
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
                startUpdateCheck();
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

    private void startUpdateCheck() {
        String url = "https://raw.githubusercontent.com/illustra/gwa-android/master/update.json";
        Log.d("pisaygwa", "Started update check");
        AppUpdater updater = new AppUpdater(this)
                .setDisplay(Display.DIALOG)
                .setUpdateFrom(UpdateFrom.JSON)
                .showAppUpdated(true)
                .setUpdateJSON(url)
                .setButtonDoNotShowAgain(null)
                .setTitleOnUpdateAvailable(R.string.update_avail)
                .setTitleOnUpdateNotAvailable(R.string.update_unavail)
                .setContentOnUpdateNotAvailable(R.string.update_unavail_sub);
        updater.start();
    }

}
