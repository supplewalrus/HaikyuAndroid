package processor.haikyuapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import processor.haikyuapp.Chat.ChatActivity;
import processor.haikyuapp.bottomNavBar.BrowseFragment;
import processor.haikyuapp.bottomNavBar.MyEventsFragment;
import processor.haikyuapp.bottomNavBar.SettingsFragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MainActivity";
    @BindView(android.R.id.content) View mRootView;

    @BindView(R.id.user_profile_picture) ImageView mUserProfilePicture;
    @BindView(R.id.user_email) TextView mUserEmail;
    @BindView(R.id.user_display_name) TextView mUserDisplayName;
    @BindView(R.id.user_phone_number) TextView mUserPhoneNumber;
    @BindView(R.id.user_enabled_providers) TextView mEnabledProviders;
    @BindView(R.id.user_is_new) TextView mIsNewUser;


    public static Intent createIntent(Context context, IdpResponse idpResponse)
    {
        return new Intent().setClass(context, MainActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, idpResponse);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        populateProfile(response);
        //populateIdpToken(response);






        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);




        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item)
                    {
                        Fragment selectedFragment = null;
                        switch (item.getItemId())
                        {
                            case R.id.action_item1:
                                selectedFragment = MyEventsFragment.newInstance();
                                break;
                            case R.id.action_item2:
                                selectedFragment = BrowseFragment.newInstance();
                                break;
                            case R.id.action_item3:
                                selectedFragment = SettingsFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, MyEventsFragment.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

    @OnClick(R.id.sign_out)
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(LoginActivity.createIntent(MainActivity.this));
                            finish();
                        } else {
                            Log.w(TAG, "signOut:failure", task.getException());
                            showSnackbar(R.string.sign_out_failed);
                        }
                    }
                });
    }

    @OnClick(R.id.goto_chat)
    public void goToChatActivity()
    {
        IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);
        startChatActivity(response);
    }


    private void populateProfile(@Nullable IdpResponse response) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(mUserProfilePicture);
        }

        mUserEmail.setText(
                TextUtils.isEmpty(user.getEmail()) ? "No email" : user.getEmail());
        mUserPhoneNumber.setText(
                TextUtils.isEmpty(user.getPhoneNumber()) ? "No phone number" : user.getPhoneNumber());
        mUserDisplayName.setText(
                TextUtils.isEmpty(user.getDisplayName()) ? "No display name" : user.getDisplayName());

        if (response == null) {
            mIsNewUser.setVisibility(View.GONE);
        } else {
            mIsNewUser.setVisibility(View.VISIBLE);
            //with this maybe go to different page???
            mIsNewUser.setText(response.isNewUser() ? "New user" : "Existing user");
        }
    }


//    private void populateIdpToken(@Nullable IdpResponse response) {
//        String token = null;
//        String secret = null;
//        if (response != null) {
//            token = response.getIdpToken();
//            secret = response.getIdpSecret();
//        }

//        View idpTokenLayout = findViewById(R.id.idp_token_layout);
//        if (token == null) {
//            idpTokenLayout.setVisibility(View.GONE);
//        } else {
//            idpTokenLayout.setVisibility(View.VISIBLE);
//            ((TextView) findViewById(R.id.idp_token)).setText(token);
//        }
//
//        View idpSecretLayout = findViewById(R.id.idp_secret_layout);
//        if (secret == null) {
//            idpSecretLayout.setVisibility(View.GONE);
//        } else {
//            idpSecretLayout.setVisibility(View.VISIBLE);
//            ((TextView) findViewById(R.id.idp_secret)).setText(secret);
//        }
//    }

    private void startChatActivity(IdpResponse response)
    {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("eventType", "chatsTwo");

        startActivity(intent);
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}