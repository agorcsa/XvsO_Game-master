package com.example.xvso.uifirebase;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.xvso.ui.HomeActivity;
import com.example.xvso.object.User;
import com.example.xvso.R;
import com.example.xvso.databinding.ActivitySignupBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Pattern;

import static com.example.xvso.ui.BaseActivity.isValidEmail;

public class SignupActivity extends BaseActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +           // beginning of the String
                    "(?=.*[0-9])" +       // at least 1 digit
                    "(?=.*[a-z])" +       // at least 1 lower case letter
                    //"(?=.*[A-Z])" +       // at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      // any letter (upper or lower case)
                    //"(?=.*[@#$%^&+=])" +    // at least 1 special character
                    //"(?=\\S+$)" +           // no white spaces
                    //".{6,}" +             // at least 6 characters
                    "$");                   // end of the String
    //private SignUpViewModel signUpViewModel;
    private ActivitySignupBinding signupBinding;
    private FirebaseAuth auth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        auth = FirebaseAuth.getInstance();

        setupPositiveSound();
        setupNegativeSound();

        if (!isGooglePlayServicesAvailable(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), R.string.services_not_available, Toast.LENGTH_LONG).show();
        }

        signupBinding.resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSoundOn) {
                    positiveSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
                            finish();
                        }
                    });
                    positiveSound.start();
                }
            }
        });

        signupBinding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSoundOn) {
                    positiveSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    positiveSound.start();
                }
            }
        });

        signupBinding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = signupBinding.inputEmail.getText().toString().trim();
                final String password = signupBinding.inputPassword.getText().toString().trim();

                // if empty - Toast empty
                // else if not empty AND invalid - Toast invalid
                // else - Login

                if (TextUtils.isEmpty(email)) {
                    if (isSoundOn) {
                        negativeSound.start();
                    }
                    Toast.makeText(getApplicationContext(), R.string.enter_email_address, Toast.LENGTH_SHORT).show();

                } else if (!TextUtils.isEmpty((email)) && (!isValidEmail(email))) {
                    if (isSoundOn) {
                        negativeSound.start();
                    }
                    //signupBinding.inputEmail.setError(getString(R.string.enter_valid_email));
                    Toast toast = Toast.makeText(getApplicationContext(), "The introduced e-mail is not valid", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (TextUtils.isEmpty(password)) {
                    if (isSoundOn) {
                        negativeSound.start();
                    }
                    Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    if (isSoundOn) {
                        negativeSound.start();
                    }
                    Toast.makeText(getApplicationContext(), R.string.minimum_password, Toast.LENGTH_SHORT).show();
                } else {
                    signupBinding.progressBar.setVisibility(View.VISIBLE);
                    //create user
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    signupBinding.progressBar.setVisibility(View.GONE);
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, R.string.email_already_used,
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        String id = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                                        String name = convertEmailToString(Objects.requireNonNull(getFirebaseUser().getEmail()));

                                        User user = new User(id, name, email, password);

                                        databaseReference = FirebaseDatabase.getInstance().getReference("users");
                                        databaseReference.child(getFirebaseUser().getUid()).setValue(user);

                                        startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        signupBinding.progressBar.setVisibility(View.GONE);
    }

    public boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }

    private String convertEmailToString(String email) {

        return email.substring(0, Objects.requireNonNull(getFirebaseUser().getEmail()).indexOf("@"));
    }
}
