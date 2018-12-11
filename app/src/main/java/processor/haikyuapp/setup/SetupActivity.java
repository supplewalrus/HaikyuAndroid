package processor.haikyuapp.setup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import processor.haikyuapp.LoginActivity;
import processor.haikyuapp.MainActivity;
import processor.haikyuapp.R;

/**
 * Created by brand on 12/4/2018.
 */

public class SetupActivity extends AppCompatActivity
{
    private static final String TAG = "SetupActivity";
    @BindView(android.R.id.content)
    View mRootView;

    @BindView(R.id.user_profile_picture)
    ImageView mUserProfilePicture;
    @BindView(R.id.user_email)
    TextView mUserEmail;
    @BindView(R.id.user_display_name) TextView mUserDisplayName;
    @BindView(R.id.user_phone_number) TextView mUserPhoneNumber;
    //@BindView(R.id.user_enabled_providers) TextView mEnabledProviders;
    //@BindView(R.id.user_is_new) TextView mIsNewUser;


    public static Intent createIntent(Context context, IdpResponse idpResponse)
    {
        return new Intent().setClass(context, SetupActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, idpResponse);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            //IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.continue_to_main)
    public void continueToMainActivity()
    {
        IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);
        startMainActivity(response);
    }

    private void startMainActivity(IdpResponse response)
    {
        startActivity(MainActivity.createIntent(this, response));
    }
}
