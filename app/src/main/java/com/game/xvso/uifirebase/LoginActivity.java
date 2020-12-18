package com.game.xvso.uifirebase;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.game.xvso.R;
import com.game.xvso.databinding.ActivityLoginBinding;
import com.game.xvso.ui.HomeActivity;
import com.game.xvso.viewmodel.LoginViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import static com.game.xvso.ui.BaseActivity.isValidEmail;


public class LoginActivity extends BaseActivity {

    ActivityLoginBinding loginBinding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        if (getFirebaseUser() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

            finish();
        }

        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        setupPositiveSound();
        setupNegativeSound();


        if (!isGooglePlayServicesAvailable(getApplicationContext())) {
            GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
            int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
            if (status != ConnectionResult.SUCCESS) {
                if (googleApiAvailability.isUserResolvableError(status)) {
                    googleApiAvailability.getErrorDialog(this, status, 1000).show();
                }
            }
        }

        loginBinding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSoundOn) {
                    positiveSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    positiveSound.start();
                } else {
                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        loginBinding.btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSoundOn) {
                    positiveSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    positiveSound.start();
                } else {
                    Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        loginBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginViewModel.getEmail().setValue(loginBinding.loginEmail.getText().toString());
                loginViewModel.getPassword().setValue(loginBinding.loginPassword.getText().toString());
                loginBinding.progressBar.setVisibility(View.VISIBLE);

                // if empty - Toast empty
                // else if not empty AND invalid - Toast invalid
                // else - Login
                String email = loginBinding.loginEmail.getText().toString();
                String password = loginBinding.loginPassword.getText().toString();

                if (TextUtils.isEmpty((email))) {
                    if (isSoundOn) {
                        negativeSound.start();
                    }
                    Toast.makeText(getApplicationContext(), R.string.enter_email_address, Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty((email)) && (!isValidEmail(email))) {
                    if (isSoundOn) {
                        negativeSound.start();
                    }
                    Toast toast = Toast.makeText(getApplicationContext(), "The introduced e-mail is not valid", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    //authenticate user
                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                        // adds a Toast message in case of Internet connection
                        if (isNetworkConnected()) {
                            auth.signInWithEmailAndPassword(loginBinding.loginEmail.getText().toString(), loginBinding.loginPassword.getText().toString())
                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            loginBinding.progressBar.setVisibility(View.GONE);
                                            if (!task.isSuccessful()) {
                                                // there was an error
                                                if (loginViewModel.getPassword().toString().length() < 6) {
                                                    loginBinding.loginPassword.setError(getString(R.string.minimum_password));
                                                } else {
                                                    Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                if (isSoundOn) {
                                                    positiveSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                        @Override
                                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                                    if (isSoundOn) {
                                                        positiveSound.start();
                                                    }
                                                }
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(LoginActivity.this, "No Internet connection!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.enter_email_and_password, Toast.LENGTH_SHORT).show();
                        if (isSoundOn) {
                            negativeSound.start();
                        }
                    }
                }
            }
        });

        if (TextUtils.isEmpty((loginViewModel.getPassword().toString()))) {
            Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_SHORT).show();
            negativeSound.start();
        }
    }

    public boolean isGooglePlayServicesAvailable(Context context){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}