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
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.MoreObjects;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final String GOOGLE_PRIVACY_POLICY_URL = "https://www.google.com/policies/privacy/";

    private static final int RC_SIGN_IN = 100;

    @BindView(R.id.root) View mRootView;

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
                        .setIsSmartLockEnabled(true)
                        .build(),
                RC_SIGN_IN);
    }

//    @OnClick(R.id.sign_in_silent)
//    public void silentSignIn(View view) {
//        AuthUI.getInstance().silentSignIn(this, getSelectedProviders())
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            startMainActivity(null);
//                        } else {
//                            showSnackbar(R.string.sign_in_failed);
//                        }
//                    }
//                });
//    }

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
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Successfully signed in. Determine if it's first time or not. Take to a different screen if first time
        // How to protect from null?
        if (resultCode == RESULT_OK) {
            FirebaseUserMetadata metadata = auth.getCurrentUser().getMetadata();
            if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                // The user is new, show them a fancy intro screen!
                System.out.println("FIRST TIME");
                startMainActivity(response);
                finish();
            } else {
                // This is an existing user, show them a welcome back screen.
                System.out.println("SECOND TIME");
                startMainActivity(response);
                finish();
            }

        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                // This is gross. Fix this.
                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                                .setTheme(R.style.GreenTheme)
                                .setLogo(R.drawable.ic_googleg_color_144dp)
                                .setAvailableProviders(getSelectedProviders())
                                .setTosAndPrivacyPolicyUrls(GOOGLE_TOS_URL,
                                        GOOGLE_PRIVACY_POLICY_URL)
                                .setIsSmartLockEnabled(true)
                                .build(),
                        RC_SIGN_IN);

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

            selectedProviders.add(new IdpConfig.FacebookBuilder()
                    .setPermissions(getFacebookPermissions())
                    .build());

            selectedProviders.add(new IdpConfig.PhoneBuilder().build());

        return selectedProviders;
    }

    //need to do Oauth stuff to get this working? For user_photos they have to agree?
    private List<String> getFacebookPermissions() {
        List<String> result = new ArrayList<>();

            result.add("user_friends");
            result.add("user_photos");

        return result;
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}
