package hai2022.team.bususersapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import hai2022.team.bususersapp.R;
import hai2022.team.bususersapp.databases.firebase.Authentication;
import hai2022.team.bususersapp.databinding.ActivitySplashBinding;
import hai2022.team.bususersapp.utils.Utils;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private MediaPlayer soundPlayer;
    private Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        authentication=new Authentication();

        // To Apply Sound and Animation Effects
        applyEffects();
        //To check if the user Logged in
        isLogged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // To Move into Signup activity
        signup();

        //To Move into Signin activity
        signin();
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPlayer.release();
        soundPlayer.stop();
    }


    private void signin() {
        binding.SplashActivityBtnSingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveInto(SignInActivity.class);
            }
        });
    }

    private void signup() {
        binding.SplashActivityBtnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveInto(SignUpActivity.class);
            }
        });
    }

    private void isLogged() {
        if (authentication.firebaseUser()!=null){
            moveInto(MainActivity.class);
            this.finish();
        }
    }

    private void moveInto(Class<? extends Activity> activity) {
        Utils.moveIntoActivity(getApplicationContext(),activity);
        if (authentication.firebaseUser()!=null){
            soundPlayer.release();
            soundPlayer.stop();
            finish();
        }
        finish();
    }

    private void applyEffects() {
        soundPlayer = MediaPlayer.create(this, R.raw.bus);
        soundPlayer.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                soundPlayer.release();
            }
        }, 11000);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_anim);
        binding.SplashActivityIvLogo.startAnimation(animation);
    }

}