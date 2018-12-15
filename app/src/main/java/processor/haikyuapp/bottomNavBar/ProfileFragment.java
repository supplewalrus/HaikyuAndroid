package processor.haikyuapp.bottomNavBar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import processor.haikyuapp.Chat.ChatActivity;
import processor.haikyuapp.LoginActivity;
import processor.haikyuapp.MainActivity;
import processor.haikyuapp.R;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    @BindView(R.id.user_profile_picture) ImageView mUserProfilePicture;
    @BindView(R.id.user_email) TextView mUserEmail;
    @BindView(R.id.user_display_name) TextView mUserDisplayName;
    @BindView(R.id.user_phone_number) TextView mUserPhoneNumber;
    //@BindView(R.id.user_is_new) TextView mIsNewUser;


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        IdpResponse response = getActivity().getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);
        populateProfile(response);
        return view;
    }

//    @OnClick(R.id.goto_chat)
//    public void goToChatActivity()
//    {
//        IdpResponse response = getActivity().getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);
//        startChatActivity(response);
//    }

    private void startChatActivity(IdpResponse response)
    {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("eventType", "chatsTwo");

        startActivity(intent);
    }

    private void populateProfile(@Nullable IdpResponse response) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "populateProfile: ");

        populateProfileImage();

        mUserEmail.setText(
                TextUtils.isEmpty(user.getEmail()) ? "No email" : user.getEmail());
        mUserPhoneNumber.setText(
                TextUtils.isEmpty(user.getPhoneNumber()) ? "No phone number" : user.getPhoneNumber());
        mUserDisplayName.setText(
                TextUtils.isEmpty(user.getDisplayName()) ? "No display name" : user.getDisplayName());
    }

    public void populateProfileImage()
    {
        String facebookUserId = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // find the Facebook profile and get the user's id
        for(UserInfo profile : user.getProviderData()) {
            // check if the provider id matches "facebook.com"
            if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                facebookUserId = profile.getUid();
            }
        }

        // construct the URL to the profile picture, with a custom height
        // alternatively, use '?type=small|medium|large' instead of ?height=
        String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";

        // (optional) use Picasso to download and show to image
        //Picasso.with(this).load(photoUrl).into(profilePicture);
        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(photoUrl)
                    .into(mUserProfilePicture);
        }
    }
}