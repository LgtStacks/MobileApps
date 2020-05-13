package edu.uw.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import edu.uw.main.ui.settings.SettingsActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The main activity to handle login and register services.
 * @author Group 3
 * @version 5/5
 */
public class AuthActivity extends AppCompatActivity {

    public static int theTheme = R.style.Original;

    public static boolean changed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(theTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(changed) {
            changed = false;
            finish();
            startActivity(getIntent());
        }
    }
}
