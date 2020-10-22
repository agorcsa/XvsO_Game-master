package com.example.xvso.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.xvso.progressdialog.ProfileEditState;
import com.example.xvso.R;
import com.example.xvso.databinding.ActivityProfileBinding;
import com.example.xvso.eventobserver.EventObserver;
import com.example.xvso.viewmodel.ProfileViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.muddzdev.styleabletoast.StyleableToast;


public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    public static final String LOG_TAG = "ProfileActivity";

    // number of images to select
    // only one image will be selected
    private static final int PICK_IMAGE = 1;

    // used to bind the views in activity_profile.xml through <layout>...</Layout>
    private ActivityProfileBinding profileBinding;

    // creates an instance of the ViewModel
    private ProfileViewModel profileViewModel;

    // creates an instance of the storage where we will store our user data
    private StorageReference mStorageRef;

    // creates an instance of the database where our user data will be stored
    private DatabaseReference mDatabaseRef;

    // used for checking if an upload is already running
    private StorageTask mUploadTask;

    // the path of the profile image that we are going to store
    private Uri imagePath;

    private ProgressDialog progressDialog;

    private ProfileEditState profileEditState;
    private ProfileViewModel.NetworkState networkState;

    private MediaPlayer mpAlert;
    private MediaPlayer mpButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // binds the activity_profile.xml with ProfileActivity.java
        profileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        // the profileViewModel instance uses the ProfileViewModel.class
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        mpButton = MediaPlayer.create(this, R.raw.alert);
        mpAlert = MediaPlayer.create(this, R.raw.button);

        observeStatus();
        // sets onClickListener on the submitButton in order to be clickable and run some code
        profileBinding.submitButton.setOnClickListener(this);

        /*profileBinding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpButton.start();
            }
        });
        */
        // sets onClickListener on the profilePicture in order to be clickable and run some code
        profileBinding.profilePicture.setOnClickListener(this);

        profileBinding.setViewModelProfile(profileViewModel);

        profileBinding.setLifecycleOwner(this);

        mStorageRef = FirebaseStorage.getInstance().getReference("users");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");

        setupProgressDialog();

        profileBinding.exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                mpAlert.start();
            }
        });

        isMusicOn = readMusicFromSharedPrefs();
        if (isMusicOn) {
            playMusic();
        }

        isSoundOn = readSoundFromSharedPrefs();
        if (isSoundOn) {
            playSound();
        }
    }

    // called when we want to select a picture from the phone's gallery
    private void selectImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    // if we have an intent and it's not null and contains data,
    // it takes out the data (picture Uri) from the intent and
    // uploads the picture as profile
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && intent != null && intent.getData() != null) {

            // gets the imagePath (Uri) from the intent
            imagePath = intent.getData();

            // uploads the user's profile picture
            profileViewModel.uploadPicture(intent);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_picture:
                selectImage();
                break;
        }
    }

    // auxiliary method for displaying a Toast message, by just giving the message we want to display
    public void showMessage(String message) {
        StyleableToast.makeText(getApplicationContext(), message, R.style.styleableToast).show();
    }

    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.uploading_image));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
    }

    private void observeStatus() {
        profileViewModel.getNetworkState().observe(this, new EventObserver<>(this::handleStatus));
    }

    public void handleStatus(ProfileViewModel.NetworkState networkState) {
        switch (networkState) {
            case LOADED:
                showMessage(createInputText());
                break;
            case IMAGE_UPLOADED:
                showMessage("Profile picture was uploaded");
            case LOADING:
                showMessage("Loading");
                break;
            case UPLOADING:
                showMessage("Uploading");
            case FAILED:
                showMessage("Failed");
                break;
        }
    }

    // creates a String from the user's data
    // used to display a Toast message if fields are validated
    public String createInputText() {

        String input = getResources().getString(R.string.first_name) + ": " + profileViewModel.getUserLiveData().getValue().getFirstName();
        input += "\n";
        input += getResources().getString(R.string.last_name) + ": " + profileViewModel.getUserLiveData().getValue().getLastName();
        input += "\n";
        input += getResources().getString(R.string.e_mail) + ": " + profileViewModel.getUserLiveData().getValue().getEmailAddress();
        input += "\n";
        input += getResources().getString(R.string.password) + ": " + profileViewModel.getUserLiveData().getValue().getPassword();

        return input;
    }

    @Override
    public void onBackPressed() {
        // place your code as needed here
        super.onBackPressed();
    }
}