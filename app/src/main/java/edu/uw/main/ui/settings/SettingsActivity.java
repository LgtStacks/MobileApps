package edu.uw.main.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import edu.uw.main.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
//        String wantedTheme = pref.getString()
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preference, rootKey);
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            if (preference.getKey().equals("Tropical")) {

            } else if (preference.getKey().equals("Muted")) {

            } else if (preference.getKey().equals("Dark Blue")) {

            }
            return super.onPreferenceTreeClick(preference);
        }
    }
}