package processor.haikyuapp.Chat;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import processor.haikyuapp.R;


public class ChatHolder extends RecyclerView.ViewHolder {
    private final TextView mNameField;
    private final TextView mTextField;
    private final FrameLayout mLeftArrow;
    private final FrameLayout mRightArrow;
    private final RelativeLayout mMessageContainer;
    private final LinearLayout mMessage;
    private final int mGreen300;
    private final int mGray300;
    private final int mWhite;
    private final int mBlack;

    public ChatHolder(View itemView) {
        super(itemView);
        mNameField = itemView.findViewById(R.id.name_text);
        mTextField = itemView.findViewById(R.id.message_text);
        mLeftArrow = itemView.findViewById(R.id.left_arrow);
        mRightArrow = itemView.findViewById(R.id.right_arrow);
        mMessageContainer = itemView.findViewById(R.id.message_container);
        mMessage = itemView.findViewById(R.id.message);
        mGreen300 = ContextCompat.getColor(itemView.getContext(), R.color.headings);
        mGray300 = ContextCompat.getColor(itemView.getContext(), R.color.material_gray_300);
        mWhite = ContextCompat.getColor(itemView.getContext(), R.color.white);
        mBlack = ContextCompat.getColor(itemView.getContext(), R.color.black);
    }

    public void bind(AbstractChat chat) {
        setName(chat.getName());
        setText(chat.getMessage());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        setIsSender(currentUser != null && chat.getUid().equals(currentUser.getUid()));
    }

    private void setName(String name) {
        mNameField.setText(name);
    }

    private void setText(String text) {
        mTextField.setText(text);
    }

    private void setIsSender(boolean isSender) {
        final int color;
        final int textColor;
        if (isSender) {
            color = mGreen300;
            textColor = mWhite;
            mLeftArrow.setVisibility(View.GONE);
            mRightArrow.setVisibility(View.VISIBLE);
            mMessageContainer.setGravity(Gravity.END);
        } else {
            color = mGray300;
            textColor = mBlack;
            mLeftArrow.setVisibility(View.VISIBLE);
            mRightArrow.setVisibility(View.GONE);
            mMessageContainer.setGravity(Gravity.START);
        }

        ((GradientDrawable) mMessage.getBackground()).setColor(color);
        mTextField.setTextColor(textColor);
        mNameField.setTextColor(textColor);
        ((RotateDrawable) mLeftArrow.getBackground()).getDrawable()
                .setColorFilter(color, PorterDuff.Mode.SRC);
        ((RotateDrawable) mRightArrow.getBackground()).getDrawable()
                .setColorFilter(color, PorterDuff.Mode.SRC);
    }
}