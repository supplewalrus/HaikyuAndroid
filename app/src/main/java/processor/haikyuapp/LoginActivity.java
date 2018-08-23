package processor.haikyuapp;

/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import processor.haikyuapp.utils.Utils;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final String GOOGLE_PRIVACY_POLICY_URL = "https://www.google.com/policies/privacy/";

    private static final int RC_SIGN_IN = 100;

    @BindView(R.id.root) View mRootView;

    @BindView(R.id.google_provider) CheckBox mUseGoogleProvider;
    @BindView(R.id.facebook_provider) CheckBox mUseFacebookProvider;
    @BindView(R.id.email_provider) CheckBox mUseEmailProvider;
    @BindView(R.id.phone_provider) CheckBox mUsePhoneProvider;

    @BindView(R.id.default_theme) RadioButton mDefaultTheme;
    @BindView(R.id.green_theme) RadioButton mGreenTheme;
    @BindView(R.id.purple_theme) RadioButton mPurpleTheme;
    @BindView(R.id.dark_theme) RadioButton mDarkTheme;

    @BindView(R.id.firebase_logo) RadioButton mFirebaseLogo;
    @BindView(R.id.google_logo) RadioButton mGoogleLogo;
    @BindView(R.id.no_logo) RadioButton mNoLogo;

    @BindView(R.id.google_tos) RadioButton mUseGoogleTos;
    @BindView(R.id.firebase_tos) RadioButton mUseFirebaseTos;

    @BindView(R.id.google_privacy) RadioButton mUseGooglePrivacyPolicy;
    @BindView(R.id.firebase_privacy) RadioButton mUseFirebasePrivacyPolicy;

    @BindView(R.id.google_scopes_header) TextView mGoogleScopesHeader;
    @BindView(R.id.google_scope_drive_file) CheckBox mGoogleScopeDriveFile;
    @BindView(R.id.google_scope_youtube_data) CheckBox mGoogleScopeYoutubeData;

    @BindView(R.id.facebook_permissions_header) TextView mFacebookPermissionsHeader;
    @BindView(R.id.facebook_permission_friends) CheckBox mFacebookPermissionFriends;
    @BindView(R.id.facebook_permission_photos) CheckBox mFacebookPermissionPhotos;

    @BindView(R.id.credential_selector_enabled) CheckBox mEnableCredentialSelector;
    @BindView(R.id.hint_selector_enabled) CheckBox mEnableHintSelector;
    @BindView(R.id.allow_new_email_accounts) CheckBox mAllowNewEmailAccounts;
    @BindView(R.id.require_name) CheckBox mRequireName;

    public static Intent createIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(R.style.GreenTheme)
                        .setLogo(R.drawable.ic_googleg_color_144dp)
                        .setAvailableProviders(getSelectedProviders())
                        .setTosAndPrivacyPolicyUrls(GOOGLE_TOS_URL,
                                GOOGLE_PRIVACY_POLICY_URL)
                        .setIsSmartLockEnabled(mEnableCredentialSelector.isChecked(),
                                mEnableHintSelector.isChecked())
                        .build(),
                RC_SIGN_IN);

        if (Utils.isFacebookMisconfigured(this)) {
            mUseFacebookProvider.setChecked(false);
            mUseFacebookProvider.setEnabled(false);
            mUseFacebookProvider.setText(R.string.facebook_label_missing_config);
            setFacebookPermissionsEnabled(false);
        } else {
            setFacebookPermissionsEnabled(mUseFacebookProvider.isChecked());
            mUseFacebookProvider.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    setFacebookPermissionsEnabled(checked);
                }
            });
        }

        if (Utils.isGoogleMisconfigured(this)
                || Utils.isFacebookMisconfigured(this)) {
            showSnackbar(R.string.configuration_required);
        }
    }

    //DO THIS FROM THE START

    @OnClick(R.id.sign_in)
    public void signIn(View view) {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(R.style.GreenTheme)
                        .setLogo(R.drawable.ic_googleg_color_144dp)
                        .setAvailableProviders(getSelectedProviders())
                        .setTosAndPrivacyPolicyUrls(GOOGLE_TOS_URL,
                                GOOGLE_PRIVACY_POLICY_URL)
                        .setIsSmartLockEnabled(mEnableCredentialSelector.isChecked(),
                                mEnableHintSelector.isChecked())
                        .build(),
                RC_SIGN_IN);
    }

    @OnClick(R.id.sign_in_silent)
    public void silentSignIn(View view) {
        AuthUI.getInstance().silentSignIn(this, getSelectedProviders())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startMainActivity(null);
                        } else {
                            showSnackbar(R.string.sign_in_failed);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startMainActivity(null);
            finish();
        }
    }

    private void handleSignInResponse(int resultCode, Intent data) {
        final IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == RESULT_OK) {
            startMainActivity(response);
            finish();
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            showSnackbar(R.string.unknown_error);
            Log.e(TAG, "Sign-in error: ", response.getError());
        }
    }

    private void startMainActivity(IdpResponse response) {
        startActivity(MainActivity.createIntent(this, response));
    }

    private List<IdpConfig> getSelectedProviders() {
        List<IdpConfig> selectedProviders = new ArrayList<>();

        if (mUseFacebookProvider.isChecked()) {
            selectedProviders.add(new IdpConfig.FacebookBuilder()
                    .setPermissions(getFacebookPermissions())
                    .build());
        }

        if (mUsePhoneProvider.isChecked()) {
            selectedProviders.add(new IdpConfig.PhoneBuilder().build());
        }

        return selectedProviders;
    }

    private void setFacebookPermissionsEnabled(boolean enabled) {
        mFacebookPermissionsHeader.setEnabled(enabled);
        mFacebookPermissionFriends.setEnabled(enabled);
        mFacebookPermissionPhotos.setEnabled(enabled);
    }

    private List<String> getFacebookPermissions() {
        List<String> result = new ArrayList<>();
        if (mFacebookPermissionFriends.isChecked()) {
            result.add("user_friends");
        }
        if (mFacebookPermissionPhotos.isChecked()) {
            result.add("user_photos");
        }
        return result;
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}
