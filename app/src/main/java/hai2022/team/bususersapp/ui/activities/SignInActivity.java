package hai2022.team.bususersapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import hai2022.team.bususersapp.databases.firebase.Authentication;
import hai2022.team.bususersapp.databinding.ActivitySignInBinding;
import hai2022.team.bususersapp.interfaces.AuthListiner;
import hai2022.team.bususersapp.utils.Utils;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private Authentication authentication;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        authentication = new Authentication(getBaseContext(), new AuthListiner() {
            @Override
            public void Signup(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    try {

                        if (!task.getResult().getUser().getDisplayName().equalsIgnoreCase("Admin")) {
                        } else {
                            authentication.logout();
                            Toast.makeText(SignInActivity.this, "Login Failled", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                    }
                } else {
                    Toast.makeText(SignInActivity.this, "Login Failled", Toast.LENGTH_SHORT).show();
                }
                Utils.moveIntoActivity(getBaseContext(), SplashActivity.class);
                finish();
            }

            @Override
            public void editInfo(@NonNull Task<Void> task, String edit) {

            }
        });

        signin();

    }

    private void signin() {
        binding.SigninBtnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.SigninEtEmail.getText().toString();
                password = binding.SigninEtPassword.getText().toString();
//                Toast.makeText(SigninActivity.this, email1 + password1, Toast.LENGTH_SHORT).show();
                authentication.Signin(email, password);
            }
        });
    }
}