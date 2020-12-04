package com.example.xvso.uifirebase;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.xvso.R;
import com.example.xvso.databinding.ActivityResetPasswordBinding;
import com.example.xvso.ui.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

import static com.example.xvso.ui.BaseActivity.isValidEmail;

public class ResetPasswordActivity extends BaseActivity {

    ActivityResetPasswordBinding resetPasswordBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);

        setupPositiveSound();
        setupNegativeSound();

        resetPasswordBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSoundOn) {
                    negativeSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    negativeSound.start();
                }
            }
        });

        resetPasswordBinding.btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSoundOn) {
                    positiveSound.start();
                }

                String email = resetPasswordBinding.email.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), R.string.enter_email_id, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isValidEmail(email)) {

                    resetPasswordBinding.progressBar.setVisibility(View.VISIBLE);
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ResetPasswordActivity.this, R.string.instructions_sent, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(ResetPasswordActivity.this, R.string.email_send_failed, Toast.LENGTH_LONG).show();
                                    }
                                    resetPasswordBinding.progressBar.setVisibility(View.GONE);
                                }
                            });
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "The introduced e-mail is not valid", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }
}
