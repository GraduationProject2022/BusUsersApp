package hai2022.team.bususersapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import hai2022.team.bususersapp.R;
import hai2022.team.bususersapp.databases.firebase.CloudStorage;
import hai2022.team.bususersapp.databinding.ActivityMainBinding;
import hai2022.team.bususersapp.interfaces.StorageListener;
import hai2022.team.bususersapp.ui.fragments.BusDetailsFragment;
import hai2022.team.bususersapp.ui.fragments.EditEmailAndPasswordFragment;
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
import hai2022.team.bususersapp.utils.Constants;
import hai2022.team.bususersapp.utils.Utils;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    private Authentication authentication;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //To Apply Settings on the application
        applySettings();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        super.onCreate(savedInstanceState);
        setContentView(view);
        Toolbar toolbar = binding.maintoolbar.maintoolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        binding.maintoolbar.maintoolbarTvTitle.setText(R.string.Home);
        drawerToggle = new ActionBarDrawerToggle(this, binding.drawaerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        binding.drawaerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        View view1 = binding.MainActivityNavdrawer.getHeaderView(0);
        TextView tv_header_name = view1.findViewById(R.id.navigation_header_tv);
        ImageView iv_user = view1.findViewById(R.id.MainActivity_navdrawer_iv);
        binding.MainActivityNavdrawer.setNavigationItemSelectedListener(this);

        authentication = new Authentication();
        tv_header_name.setText(authentication.firebaseUser().getEmail());

        if (authentication.firebaseUser().getDisplayName().equals("driver")) {
            binding.MainActivityBnv.getMenu().removeItem(R.id.mainmenu_chat);
            getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, CoHomeFragment.newInstance("")).commit();
        } else if (authentication.firebaseUser().getDisplayName().equals("bus")) {
            Toast.makeText(this, "Bus Driver", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, new ChatFragment()).commit();
        } else if (authentication.firebaseUser().getDisplayName().equals("student")) {
            Toast.makeText(this, "Student", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, StudentHomeFragment.newInstance()).commit();
        }


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

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainmenu_home:
                if (authentication.firebaseUser().getDisplayName().equals("driver")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, CoHomeFragment.newInstance("")).commit();
                } else if (authentication.firebaseUser().getDisplayName().equals("bus")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, BusDetailsFragment.newInstance(null)).commit();
                } else if (authentication.firebaseUser().getDisplayName().equals("student")) {
                    Toast.makeText(this, "Student", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, StudentHomeFragment.newInstance()).commit();
                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, EditEmailAndPasswordFragment.newInstance()).commit();

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

                } else if (authentication.firebaseUser().getDisplayName().equals("student")) {
                    Toast.makeText(this, "Student", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, CoHomeFragment.newInstance("student")).commit();
                }
                return true;
            case R.id.mainmenu_profile:
                binding.maintoolbar.maintoolbarTvTitle.setText(R.string.Profile);
                getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity_layout_container, ProfileFragment.newInstance()).commit();
                return true;
            case R.id.nav_drawer_about:
                Intent i = new Intent(getBaseContext(), ContainerActivity.class);
                i.putExtra(Constants.FRAGMENT, Constants.ABOUT_FRAGMENT);
                startActivity(i);
                return true;
            case R.id.nav_drawer_logout:
                authentication.logout();
                finish();
                startActivity(new Intent(getBaseContext(), SplashActivity.class));
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}