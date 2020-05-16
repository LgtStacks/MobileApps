package edu.uw.main.ui.settings;



import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import edu.uw.main.AuthActivity;
import edu.uw.main.MainActivity;
import edu.uw.main.R;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(AuthActivity.theTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements PreferenceManager.OnPreferenceTreeClickListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preference, rootKey);
            Preference passwordPreference =  findPreference("change_pw");
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            if (!AuthActivity.showChangePW) {
                preferenceScreen.removePreference(passwordPreference);
            } else {
                preferenceScreen.addPreference(passwordPreference);
            }
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            String key = preference.getKey();
            if (key.equals("Tropical")) {
                AuthActivity.theTheme = R.style.Tropical;
                restart();
            } else if (key.equals("Muted")) {
                AuthActivity.theTheme = R.style.muted;
                restart();
            } else if (key.equals("Dark Blue")) {
                AuthActivity.theTheme = R.style.db;
                restart();
            } else if (key.equals("change_pw")) {
                MainActivity.changePassword = true;
                getActivity().finish();
            }
            return true;
        }
        private void restart() {
            AuthActivity.changed = true;
            getActivity().finish();
            getActivity().startActivity(getActivity().getIntent());
        }
    }


}