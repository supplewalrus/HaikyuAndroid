package processor.haikyuapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity
{
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            //if already logged in. might not need this
            Log.d("AUTH", auth.getCurrentUser().getEmail());
        } else {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.FacebookBuilder().build());

            // Create and launch sign-in intent. Uncomment when we have logo and theme
            // also if we get privacy policy and terms of service
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    //.setLogo(R.drawable.my_great_logo)      // Set logo drawable
                    //.setTheme(R.style.MySuperAppTheme)      // Set theme
                    //.setTosUrl("https://superapp.example.com/terms-of-service.html")
                    //.setPrivacyPolicyUrl("https://superapp.example.com/privacy-policy.html")
                    .build(),
                RC_SIGN_IN);
        }
//        findViewById(R.id.log_out_button).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("AUTH", auth.getCurrentUser().getEmail());

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Log.d("AUTH", "NOT AUTHENTICATED");
            }
        }
    }


    //Move this to other activities.
//    @Override
//    public void onClick(View view)
//    {
//        if(view.getId() == R.id.log_out_button){
//            AuthUI.getInstance()
//                    .signOut(this)
//                    .addOnCompleteListener(new OnCompleteListener<Void>()
//                    {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task)
//                        {
//                            Log.d("AUTH", "USER LOGGED OUT!");
//                            finish();
//                        }
//                    });
//
//        }
//    }
}
