package edu.uw.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.content.pm.PackageManager;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.view.View;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.uw.main.databinding.ActivityMainBinding;
import edu.uw.main.model.NewConnectionCountViewModel;
import edu.uw.main.model.NewMessageCountViewModel;
import edu.uw.main.model.UserInfoViewModel;
import edu.uw.main.services.PushReceiver;
import edu.uw.main.ui.chat.ChatMessage;
import edu.uw.main.ui.chat.ChatViewModel;
import edu.uw.main.ui.chat.Contacts;
import edu.uw.main.ui.connection.ConnectionAddViewModel;
import edu.uw.main.ui.connection.ConnectionListViewModel;
import edu.uw.main.ui.connection.ConnectionPending;
import edu.uw.main.ui.connection.ConnectionPendingViewModel;
import edu.uw.main.ui.connection.ConnectionSentViewModel;
import edu.uw.main.ui.chat.Contacts;
import edu.uw.main.ui.connection.ConnectionListViewModel;
import edu.uw.main.ui.connection.ConnectionPending;
import edu.uw.main.ui.connection.ConnectionPendingViewModel;
import edu.uw.main.ui.connection.ConnectionSentViewModel;
import edu.uw.main.ui.settings.SettingsActivity;

import edu.uw.main.ui.weather.LocationFragment;
import edu.uw.main.ui.weather.WeatherFragment;
import edu.uw.main.ui.weather.WeatherViewModel;
import edu.uw.main.ui.weather.ZipCodeFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


/**
 * The main activity to handle the chat, connections, weather and home
 * services.
 * @author Group 3
 * @version 5/5
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private MainPushMessageReceiver mPushMessageReceiver;

    private NewConnectionCountViewModel mNewConnectionModel;

    private NewMessageCountViewModel mNewMessageModel;

    public static boolean changePassword = false;

    public static boolean resetChatRoomList = false;

    public static List<Contacts> myContacts;

    private ActivityMainBinding binding;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // A constant int for the permissions request code. Must be a 16 bit number
    private static final int MY_PERMISSIONS_LOCATIONS = 8414;

    private LocationRequest mLocationRequest;

    // Use a FusedLocationProviderClient to request the location
    private FusedLocationProviderClient mFusedLocationClient;

    // Will use this call back to decide what to do when a location change is detected
    private LocationCallback mLocationCallback;

    // The ViewModel that will store the current location
    private WeatherViewModel mWeatherModel;

    private WeatherFragment weatherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(AuthActivity.theTheme);
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setUpWithViewPager(binding.viewPager);

        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.tabLayout.setVisibility(View.GONE);


        myContacts = new ArrayList<>();
//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        AuthActivity.showChangePW = true;
        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());
        String email = args.getEmail();
        String jwt = args.getJwt();
        //take note that we are not using the constructor explicitly, the no-arg
        //constructor is called implicitly
        //  UserInfoViewModel model = new ViewModelProvider(this).get(UserInfoViewModel.class);
        new ViewModelProvider(
                this,
                new UserInfoViewModel.UserInfoViewModelFactory(email, jwt))
                .get(UserInfoViewModel.class);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,  R.id.navigation_connection, R.id.navigation_chat,
                R.id.weatherMain)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mNewConnectionModel = new ViewModelProvider(this).get(NewConnectionCountViewModel.class);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_connection) {
                //When the user navigates to the connection page, reset the new connection count.
                mNewConnectionModel.reset();
            }
        });

        mNewConnectionModel.addMessageCountObserver(this, count -> {
            BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_connection);
            badge.setMaxCharacterCount(2);

            if (count > 0) {
                //new messages! update and show the notification badge.
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                badge.clearNumber();
                badge.setVisible(false);
            }
        });

        mNewMessageModel = new ViewModelProvider(this).get(NewMessageCountViewModel.class);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_chat) {
                //When the user navigates to the chats page, reset the new message count.
                //This will need some extra logic for your project as it should have
                //multiple chat rooms.
                mNewMessageModel.reset();
            }
        });

        mNewMessageModel.addMessageCountObserver(this, count -> {
            BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_chat);
            badge.setMaxCharacterCount(2);
            if (count > 0) {
                //new messages! update and show the notification badge.
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                //user did some action to clear the new messages, remove the badge
                badge.clearNumber();
                badge.setVisible(false);
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);
        } else {
            // The user has already allowed the use of Locations. Get the current location.
            requestLocation();

        }

        mLocationCallback = new LocationCallback() {
            @Override
            public  void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {

                    Log.d("LOCATION UPDATE", location.toString());
                    if (mWeatherModel == null) {
                        mWeatherModel = new ViewModelProvider(MainActivity.this).get(WeatherViewModel.class);
                    }
                    mWeatherModel.setLocation(location);
                }
            };
        };
        createLocationRequest();
    }

    private void setUpWithViewPager(ViewPager viewPager) {
        MainActivity.SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WeatherFragment(), "Update");
        adapter.addFragment(new LocationFragment(), "Map");
        adapter.addFragment(new ZipCodeFragment(), "Zip Code");
        viewPager.setAdapter(adapter);
    }

    private static class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSIONS_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // locations-related task you need to do.
                    requestLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission
                    Log.d("PERMISSION DENIED", "Nothing to see or do here.");
                    // Shut down the app. In production release, you would let the user
                    // know why the app is shutting down...maybe ask for permission again?
                    finishAndRemoveTask();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d("REQUEST LOCATION", "User did NOT allow permission to request location!");
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.d("LOCATION", location.toString());
                        if (mWeatherModel == null) {
                            mWeatherModel = new ViewModelProvider(MainActivity.this).get(WeatherViewModel.class);
                        }
                        mWeatherModel.setLocation(location);
                    }
                }
            });
        }
    }

    /**
     * Create and configure a Location Request used when retrieving location updates
     */
    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        } else if (id == R.id.action_sign_out) {
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mPushMessageReceiver == null) {
            mPushMessageReceiver = new MainPushMessageReceiver();
        }
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
        registerReceiver(mPushMessageReceiver, iFilter);
        IntentFilter acceptFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_ACCEPTANCE);
        registerReceiver(mPushMessageReceiver, acceptFilter);
        IntentFilter pendingFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_REQUEST);
        registerReceiver(mPushMessageReceiver, pendingFilter);
        IntentFilter declineFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_DECLINE);
        registerReceiver(mPushMessageReceiver, declineFilter);
        IntentFilter removeFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_REMOVE);
        registerReceiver(mPushMessageReceiver, removeFilter);

        if(AuthActivity.changed) {
            AuthActivity.changed = false;
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPushMessageReceiver != null){
            unregisterReceiver(mPushMessageReceiver);
        }
    }

    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class MainPushMessageReceiver extends BroadcastReceiver {
        private ChatViewModel mModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatViewModel.class);

        private ConnectionListViewModel mFriendModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ConnectionListViewModel.class);

        private ConnectionPendingViewModel mPendingModel =
                new ViewModelProvider(MainActivity.this)
                .get(ConnectionPendingViewModel.class);

        private ConnectionSentViewModel mSentModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ConnectionSentViewModel.class);

        private ConnectionPending update = new ConnectionPending();

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("BROADCAST SENT: ", "TRUE");
            NavController nc =
                    Navigation.findNavController(
                            MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();
            String input = null;
            String check = "INTENT FAILED";
            if(intent.hasExtra("accept")){
                Log.e("Intent Sent ", "TRUE");

                check = intent.getStringExtra("accept");

            }
            Log.e("INENT INPUT MAIN ACT: ", check);
            if (intent.hasExtra("chatMessage")) {
                ChatMessage cm = (ChatMessage) intent.getSerializableExtra("chatMessage");
                //If the user is not on the chat screen, update the
                // NewMessageCountView Model
                if (nd.getId() != R.id.navigation_chat) {
                    mNewMessageModel.increment();
                }
                //Inform the view model holding chatroom messages of the new
                //message.
                mModel.addMessage(intent.getIntExtra("chatid", -1), cm);
            }else if (intent.hasExtra("accept")){
                input = intent.getStringExtra("accept");
                Log.e("CHECK FOR ACCEPTANCE: ", input);

                mFriendModel.addFriend(input);
              //  mPendingModel.removePendingRequest(input);
              //  mSentModel.removeSentItem(input);




            }else if (intent.hasExtra("decline")){
                input = intent.getStringExtra("decline");
                Log.e("CHECK FOR ACCEPTANCE: ", input);
             //   mSentModel.removeSentItem(input);
               // mPendingModel.removePendingRequest(input);


            }else if (intent.hasExtra("remove")){
                input = intent.getStringExtra("remove");
                Log.e("CHECK FOR REMOVE ACCEPTANCE: ", input);
                mFriendModel.removeFriend(input);


            }else if (intent.hasExtra("username")){
                input = intent.getStringExtra("username");
                Log.e("CHECK FOR ACCEPTANCE: ", input);
                //mSentModel.addSentItem(input);
                mPendingModel.addPendingRequest(input);

             //   mPendingModel.removePendingRequest(input);
            }
        }
    }


    /**
     * Requests location updates from the FusedLocationApi.
     */
    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdate() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
    private void signOut() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);

        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();
        //End the app completely
        finishAndRemoveTask();
    }
}
