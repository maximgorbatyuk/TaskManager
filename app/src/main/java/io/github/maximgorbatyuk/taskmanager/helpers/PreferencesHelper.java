package io.github.maximgorbatyuk.taskmanager.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by Maxim on 22.04.2016.
 */
public class PreferencesHelper {

    Context context;
    SharedPreferences preferences;

    public PreferencesHelper(Context context) {
        this.context = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void SaveSettings(boolean notificationEnabled, int workHours, int workDays){
        try {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("notifications", notificationEnabled);
            editor.putInt("work_hours", workHours);
            editor.putInt("work_days", workDays);

            editor.apply();
        } catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean getEnableNotifications(){
        boolean isChecked = false;
        return preferences.getBoolean("notifications", isChecked);
    }

    public int getWorkHours(){
        return preferences.getInt("work_hours", -1);
    }

    public int getWorkDays(){
        return preferences.getInt("work_days", -1);
    }


}
