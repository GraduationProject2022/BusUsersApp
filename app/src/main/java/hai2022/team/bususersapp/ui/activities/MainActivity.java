package hai2022.team.bususersapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import hai2022.team.bususersapp.R;
import hai2022.team.bususersapp.databinding.ActivityMainBinding;
import hai2022.team.bususersapp.ui.fragments.ProfileFragment;
import hai2022.team.bususersapp.ui.fragments.StudentHomeFragment;
import hai2022.team.bususersapp.databases.firebase.Authentication;
import hai2022.team.bususersapp.databases.firebase.Realtime;
import hai2022.team.bususersapp.interfaces.BusListiner;
import hai2022.team.bususersapp.models.Bus;
import hai2022.team.bususersapp.models.Chat;
import hai2022.team.bususersapp.models.User;
import hai2022.team.bususersapp.ui.fragments.ChatFragment;
import hai2022.team.bususersapp.ui.fragments.CoHomeFragment;
import hai2022.team.bususersapp.utils.Utils;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private ActivityMainBinding binding;
    private Authentication authentication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //To Apply Settings on the application
        applySettings();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        super.onCreate(savedInstanceState);
        setContentView(view);
        authentication = new Authentication();
        if (authentication.firebaseUser().getDisplayName().equals("driver")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, CoHomeFragment.newInstance()).commit();
        }else if (authentication.firebaseUser().getDisplayName().equals("bus")){
            Toast.makeText(this, "Bus Driver", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, new ChatFragment()).commit();
        }else if(authentication.firebaseUser().getDisplayName().equals("student")){
            Toast.makeText(this, "Student", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, StudentHomeFragment.newInstance()).commit();
        }
        Toolbar toolbar = binding.maintoolbar.maintoolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        binding.maintoolbar.maintoolbarTvTitle.setText(R.string.Home);

        Realtime realtime = new Realtime(getBaseContext(), new BusListiner() {

            @Override
            public void ceatebus(@NonNull Task<Void> task) {

            }

            @Override
            public void getUser(User user) {

            }

            @Override
            public void getBus(Bus bus) {
                binding.maintoolbar.maintoolbarTvTitle.setText("Welcome " + "Bus Driver" + " " + bus.getDriverName());
                Bundle b = new Bundle();
                b.putSerializable("User", bus);
//                settingsFragment.setArguments(b);
            }

            @Override
            public void getAdmins(ArrayList<User> users) {
                /**/
            }

            @Override
            public void getDrivers(ArrayList<User> users) {

            }

            @Override
            public void getStudents(ArrayList<User> users) {

            }

            @Override
            public void getBuses(ArrayList<Bus> buses) {

            }

            @Override
            public void getMsgs(ArrayList<Chat> chats) {

            }

            @Override
            public void updateUser(boolean state) {

            }
        });
        realtime.getUser(authentication.firebaseUser().getDisplayName(), authentication.firebaseUser().getUid());

        binding.MainActivityBnv.setOnItemSelectedListener(this);
    }


    private void applySettings() {
        if (Utils.getSettingsPreferences(getBaseContext()).getBoolean("isDarkMode", false)) {
            setTheme(R.style.Theme_Dark);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            setTheme(R.style.Theme_Light);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

//        if (Utils.getSettingsPreferences(MainActivity.class))
//        if (Utils.getSettingsPreferences(getBaseContext()).getString("lang", null).equalsIgnoreCase("en")) {
//            Utils.setLanguage((Activity) getApplicationContext(),"en");
//        } else if (Utils.getSettingsPreferences(getBaseContext()).getString("lang", null).equalsIgnoreCase("ar")) {
//            Utils.setLanguage((Activity) getApplicationContext(),"ar");
//        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainmenu_home:
                if (authentication.firebaseUser().getDisplayName().equals("driver")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, CoHomeFragment.newInstance()).commit();
                }else if (authentication.firebaseUser().getDisplayName().equals("bus")){
                    Toast.makeText(this, "Bus Driver", Toast.LENGTH_SHORT).show();
                }else if(authentication.firebaseUser().getDisplayName().equals("student")){
                    Toast.makeText(this, "Student", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, StudentHomeFragment.newInstance()).commit();
                }
                binding.maintoolbar.maintoolbarTvTitle.setText(R.string.Home);
                return true;
            case R.id.mainmenu_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, ChatFragment.newInstance()).commit();
                binding.maintoolbar.maintoolbarTvTitle.setText(R.string.Message);
                return true;
            case R.id.mainmenu_agenda:
                binding.maintoolbar.maintoolbarTvTitle.setText(R.string.Agenda);
                if (authentication.firebaseUser().getDisplayName().equals("driver")) {

                }else if(authentication.firebaseUser().getDisplayName().equals("student")){
                    Toast.makeText(this, "Student", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, CoHomeFragment.newInstance()).commit();
                }
                return true;
            case R.id.mainmenu_profile:
                binding.maintoolbar.maintoolbarTvTitle.setText(R.string.Profile);
                getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, ProfileFragment.newInstance()).commit();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }
}