package processor.haikyuapp.setup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;
import processor.haikyuapp.LoginActivity;
import processor.haikyuapp.MainActivity;
import processor.haikyuapp.R;

/**
 * Created by brand on 12/4/2018.
 */

public class SetupActivity extends AppCompatActivity
{
    public static Intent createIntent(Context context, IdpResponse idpResponse)
    {
        return new Intent().setClass(context, MainActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, idpResponse);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null)
        {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);
        startMainActivity(response);
        finish();

    }

    private void startMainActivity(IdpResponse response)
    {
        startActivity(MainActivity.createIntent(this, response));
    }
}
