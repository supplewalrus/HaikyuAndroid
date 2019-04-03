package processor.haikyuapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

import processor.haikyuapp.bottomNavBar.ProfileFragment;

public class FakeLoginActivity extends AppCompatActivity {
    private ImageView facebooklogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            startActivity(MainActivity.createIntent(this, null));
            finish();
            return;
        }
        setContentView(R.layout.activity_fake_login);

        final LinearLayout spinner = (LinearLayout)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        final Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        final Animation andFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        this.facebooklogin = (ImageView)this.findViewById(R.id.facebooklogin);

        this.facebooklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                startActivity(LoginActivity.createIntent(getBaseContext()));
                new Thread(new Runnable() {
                    public void run() {
                        facebooklogin.startAnimation(andFade);
                    }
                }).start();

                new Thread(new Runnable() {
                    public void run() {
                        facebooklogin.startAnimation(aniFade);
                    }
                }).start();
                finish();
            }
        });



    }

    public static Intent createIntent(Context context)
    {
        return new Intent(context, FakeLoginActivity.class);
    }

}
