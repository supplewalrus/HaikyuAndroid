package processor.haikyuapp.utils;

import android.content.Context;
import android.content.res.Resources;

import com.firebase.ui.auth.AuthUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import processor.haikyuapp.R;

public class Utils {

    public static boolean isGoogleMisconfigured(Context context) {
        return AuthUI.UNCONFIGURED_CONFIG_VALUE.equals(
                context.getString(R.string.default_web_client_id));
    }

    public static boolean isFacebookMisconfigured(Context context) {
        return AuthUI.UNCONFIGURED_CONFIG_VALUE.equals(
                context.getString(R.string.facebook_application_id));
    }


//    public static boolean isGitHubMisconfigured(Context context) {
//        List<String> gitHubConfigs = Arrays.asList(
//                context.getString(R.string.firebase_web_host),
//                context.getString(R.string.github_client_id),
//                context.getString(R.string.github_client_secret)
//        );
//
//        return gitHubConfigs.contains(AuthUI.UNCONFIGURED_CONFIG_VALUE);
//    }

    public static List<AuthUI.IdpConfig> getConfiguredProviders(Context context) {
        List<AuthUI.IdpConfig> providers = new ArrayList<>();
        providers.add(new AuthUI.IdpConfig.EmailBuilder().build());
        providers.add(new AuthUI.IdpConfig.PhoneBuilder().build());

        if (!isGoogleMisconfigured(context)) {
            providers.add(new AuthUI.IdpConfig.GoogleBuilder().build());
        }

        if (!isFacebookMisconfigured(context)) {
            providers.add(new AuthUI.IdpConfig.FacebookBuilder().build());
        }

        return providers;
    }
}