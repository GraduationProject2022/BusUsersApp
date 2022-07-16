package hai2022.team.bususersapp.databases.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import hai2022.team.bususersapp.interfaces.AuthListiner;

public class Authentication {
    private FirebaseAuth mAuth;
    AuthListiner authListiner;
    Context context;

    public Authentication(){
        this.mAuth = FirebaseAuth.getInstance();
    }

    public Authentication(Context context,AuthListiner authListiner) {
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.authListiner = authListiner;
    }

    public void Signup(String email, String password) {


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                authListiner.Signup(task);
                logout();
            }
        });
    }

    public void Signin(String email, String Password) {
        mAuth.signInWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                authListiner.Signup(task);
            }
        });
    }

    public void EditEmailAndPass(String email, String password) {
        if (!email.equals("")) {
            firebaseUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    authListiner.editInfo(task, "email");
                }
            });
        }
        if (!password.equals("")) {
            firebaseUser().updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    authListiner.editInfo(task, "password");
                }
            });
        }
    }

    public FirebaseUser firebaseUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser;
    }

    public void logout(){
        mAuth.signOut();
    }

}
