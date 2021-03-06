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
import processor.haikyuapp.setup.SetupActivity;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "LoginActivity";

    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final String GOOGLE_PRIVACY_POLICY_URL = "https://www.google.com/policies/privacy/";

    private static final int RC_SIGN_IN = 100;

    @BindView(R.id.root)
    View mRootView;

    public static Intent createIntent(Context context)
    {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        //.setTheme(R.style.MintTheme)
//                        .setTheme(R.style.WhiteTheme)
                        .setLogo(R.mipmap.uticklogo)
                        .setAvailableProviders(getSelectedProviders())
                        .setTosAndPrivacyPolicyUrls(GOOGLE_TOS_URL,
                                GOOGLE_PRIVACY_POLICY_URL)
                        .setIsSmartLockEnabled(true)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            handleSignInResponse(resultCode, data);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null)
        {
            startMainActivity(null);
            finish();
        }
    }


    private void handleSignInResponse(int resultCode, @Nullable Intent data)
    {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == RESULT_OK)
        {
            //NEW USER. SETUP WORKFLOW
            if(response.isNewUser())
            {
                //startMainActivity(response);
                startSetupActivity(response);
                finish();
            }
            else
            {
                startMainActivity(response);
                finish();
            }
        }
        else
        {            // Sign in failed
            if (response == null)
            {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK)
            {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            showSnackbar(R.string.unknown_error);
            Log.e(TAG, "Sign-in error: ", response.getError());
        }
    }


    private void startMainActivity(IdpResponse response)
    {
        startActivity(MainActivity.createIntent(this, response));
    }

    private void startSetupActivity(IdpResponse response)
    {
        startActivity(MainActivity.createIntent(this, response));
        //startActivity(SetupActivity.createIntent(this, response));
    }

    private List<IdpConfig> getSelectedProviders()
    {
        List<IdpConfig> selectedProviders = new ArrayList<>();

        selectedProviders.add(new IdpConfig.FacebookBuilder()
                .setPermissions(getFacebookPermissions())
                .build());

//        selectedProviders.add(new IdpConfig.GoogleBuilder().build());
//        selectedProviders.add(new IdpConfig.PhoneBuilder().build());
//        selectedProviders.add(new IdpConfig.TwitterBuilder().build());

        return selectedProviders;
    }

    //need to do Oauth stuff to get this working? For user_photos they have to agree?
    private List<String> getFacebookPermissions()
    {
        List<String> result = new ArrayList<>();

        result.add("user_friends");
        result.add("user_photos");

        return result;
    }

    private void showSnackbar(@StringRes int errorMessageRes)
    {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}