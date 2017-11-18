package gq.jared.pisaygwa;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import java.lang.reflect.Field;

public class Updater {

    private Context ctx;

    public Updater(Context ctx) {
        this.ctx = ctx;
    }

    public void startUpdateCheck(boolean silent) {
        String url = "https://raw.githubusercontent.com/illustra/gwa-android/master/update.json";
        Log.d("pisaygwa", "Started update check");

        String msg;
        boolean err = false;
        Activity a = (Activity) ctx;
        if (isOnline()) {
            msg = a.getResources().getString(R.string.update_check_started);
            AppUpdater updater = new AppUpdater(ctx)
                    .setDisplay(Display.DIALOG)
                    .setUpdateFrom(UpdateFrom.JSON)
                    .showAppUpdated(!silent)
                    .setUpdateJSON(url)
                    .setButtonDoNotShowAgain(null)
                    .setTitleOnUpdateAvailable(R.string.update_avail)
                    .setTitleOnUpdateNotAvailable(R.string.update_unavail)
                    .setContentOnUpdateNotAvailable(R.string.update_unavail_sub);
            updater.start();
        } else {
            err = true;
            msg = a.getResources().getString(R.string.err_no_connection);
        }

        if (BuildConfig.DEBUG || !silent) {
            View rootView = a.findViewById(R.id.snackbar_view);
            Snackbar snackbar = Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT);
            forceAnimateSnackbar(snackbar);
            snackbar.show();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        } else {
            Log.e("pisaygwa", "NetworkInfo is null");
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void forceAnimateSnackbar(Snackbar snackbar) {
        // https://stackoverflow.com/a/43811447/3350320
        try {
            Field mAccessibilityManagerField = BaseTransientBottomBar.class
                    .getDeclaredField("mAccessibilityManager");
            mAccessibilityManagerField.setAccessible(true);
            AccessibilityManager accessibilityManager = (AccessibilityManager)
                    mAccessibilityManagerField.get(snackbar);
            Field mIsEnabledField = AccessibilityManager.class.getDeclaredField("mIsEnabled");
            mIsEnabledField.setAccessible(true);
            mIsEnabledField.setBoolean(accessibilityManager, false);
            mAccessibilityManagerField.set(snackbar, accessibilityManager);
        } catch (Exception e) {
            Log.d("pisaygwa", "Snackbar reflection error: " + e.toString());
        }
    }

}
