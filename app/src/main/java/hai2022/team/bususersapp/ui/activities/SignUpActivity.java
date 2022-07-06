package hai2022.team.bususersapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;

import hai2022.team.bususersapp.databases.firebase.Authentication;
import hai2022.team.bususersapp.databases.firebase.Realtime;
import hai2022.team.bususersapp.databinding.ActivitySignUpBinding;
import hai2022.team.bususersapp.interfaces.AuthListiner;
import hai2022.team.bususersapp.interfaces.BusListiner;
import hai2022.team.bususersapp.models.Bus;
import hai2022.team.bususersapp.models.Chat;
import hai2022.team.bususersapp.models.User;
import hai2022.team.bususersapp.utils.Utils;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    String username, email, password;
    Authentication authentication;
    Realtime realtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        realtime = new Realtime(getBaseContext(), new BusListiner() {
            @Override
            public void ceatebus(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Utils.moveIntoActivity(getBaseContext(), MainActivity.class);
            }

            @Override
            public void getUser(User user) {

            }

            @Override
            public void getBus(Bus bus) {

            }

            @Override
            public void getAdmins(ArrayList<User> users) {

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

        authentication = new Authentication(getBaseContext(), new AuthListiner() {
            @Override
            public void Signup(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    task.getResult().getUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName("student").build());
                    realtime.createUser(task.getResult().getUser().getUid(), new User(task.getResult().getUser().getUid(), binding.SignupEtUsername.getText().toString(), "", task.getResult().getUser().getEmail().toString(), "", "", "student", Utils.currentDate(), Utils.currentDate()));
                } else {

                }
            }
        });


        signup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.SignupTvAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),SignInActivity.class));
                finish();
            }
        });
    }

    private void signup() {
        binding.SignupBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                username = binding.SignupEtUsername.getText().toString();
                email = binding.SignupEtEmail.getText().toString();
                password = binding.SignupEtPassword.getText().toString();
//                Log.d("DataReception",email);
                authentication.Signup(email, password);
            }
        });
    }


}