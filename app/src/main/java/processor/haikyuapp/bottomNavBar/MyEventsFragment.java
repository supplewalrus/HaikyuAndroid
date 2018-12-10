package processor.haikyuapp.bottomNavBar;

import android.content.Intent;
import android.os.Bundle;
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
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import processor.haikyuapp.Chat.ChatActivity;
import processor.haikyuapp.LoginActivity;
import processor.haikyuapp.R;

public class MyEventsFragment extends Fragment {
    //@BindView(android.R.id.content) View mRootView;

    private static final String TAG = "EventsFragment";


//    @BindView(R.id.user_profile_picture)
//    ImageView mUserProfilePicture;
//    @BindView(R.id.user_email)
//    TextView mUserEmail;
//    @BindView(R.id.user_display_name) TextView mUserDisplayName;
//    @BindView(R.id.user_phone_number) TextView mUserPhoneNumber;
//    @BindView(R.id.user_is_new) TextView mIsNewUser;

    @BindView(R.id.user_profile_picture) ImageView mUserProfilePicture;
    @BindView(R.id.user_email) TextView mUserEmail;
    @BindView(R.id.user_display_name) TextView mUserDisplayName;
    @BindView(R.id.user_phone_number) TextView mUserPhoneNumber;
    @BindView(R.id.user_is_new) TextView mIsNewUser;



    public static MyEventsFragment newInstance() {
        MyEventsFragment fragment = new MyEventsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
        ButterKnife.bind(this, view);

        IdpResponse response = getActivity().getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);
        populateProfile(response);
        return view;
    }

    @OnClick(R.id.goto_chat)
    public void goToChatActivity()
    {
        IdpResponse response = getActivity().getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);
        startChatActivity(response);
    }

    private void startChatActivity(IdpResponse response)
    {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("eventType", "chatsTwo");

        startActivity(intent);
    }

    private void populateProfile(@Nullable IdpResponse response) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "populateProfile: ");
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
}