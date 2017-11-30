package com.apps.huyenpham.memories.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.huyenpham.memories.R;
import com.apps.huyenpham.memories.fragment.FragmentPhoto;
import com.apps.huyenpham.memories.model.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ShareActivity extends Activity {
    private LoginButton loginButton;
    private ImageView userPicture;
    private TextView userName;
    private Button logoutButton;
    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private ShareDialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initView();
        supportView();
        shareDialog = new ShareDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("aaa", "onSuccess: ");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("aaa", "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("aaa", "onError: ");
                Toast.makeText(ShareActivity.this, "Check your connection.", Toast.LENGTH_SHORT).show();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.login = false;
                firebaseAuth.signOut();
                LoginManager.getInstance().logOut();
                updateUserProfile(null);
                loginButton.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Utils.login) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            updateUserProfile(user);
            loginButton.setVisibility(View.INVISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
        } else {
            LoginManager.getInstance().logOut();
            updateUserProfile(null);
            loginButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(final AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Utils.login = true;
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUserProfile(user);
                            loginButton.setVisibility(View.INVISIBLE);
                            logoutButton.setVisibility(View.VISIBLE);
                            postPhoto();
                        } else {
                            Utils.login = false;
                            updateUserProfile(null);
                        }
                    }
                });
    }

    private void updateUserProfile(FirebaseUser user) {
        if (user != null) {
            Picasso.with(getApplicationContext()).load(user.getPhotoUrl()).into(userPicture);
            userName.setText(user.getDisplayName());
        } else {
            userPicture.setImageResource(R.drawable.icon_user);
            userName.setText("");
        }

    }

    private void postPhoto() {
        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(FragmentPhoto.bitmapPhoto)
                .build();
        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .build();
        shareDialog.show(sharePhotoContent);
    }

    private void initView() {
        loginButton = (LoginButton) findViewById(R.id.login_facebook);
        userName = (TextView) findViewById(R.id.user_name);
        userPicture = (ImageView) findViewById(R.id.user_picture);
        logoutButton = (Button) findViewById(R.id.logout_facebook);
    }

    private void supportView() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int picHeight = height/4;
        int picWidth = width/2;
        LinearLayout.LayoutParams layoutPicture = new LinearLayout.LayoutParams(picWidth, picHeight);
        layoutPicture.gravity = Gravity.CENTER_HORIZONTAL;
        int marginHor = width/20;
        int marginVer = height/50;
        layoutPicture.setMargins(marginHor, marginVer, marginHor, marginVer);
        userPicture.setLayoutParams(layoutPicture);

        LinearLayout.LayoutParams layoutName = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        float nameSize = height/45;
        userName.setTextSize(TypedValue.COMPLEX_UNIT_SP, nameSize);
        layoutName.gravity = Gravity.CENTER;
        layoutName.setMargins(marginHor, marginVer, marginHor, marginVer);
        userName.setLayoutParams(layoutName);

        LinearLayout.LayoutParams layoutLogin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutLogin.gravity = Gravity.CENTER;
        layoutLogin.setMargins(marginHor, marginVer, marginHor, marginVer);
        loginButton.setLayoutParams(layoutLogin);

        float logoutSize = height/60;
        logoutButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, logoutSize);
        int paddingVer = height/230;
        int paddingHor = width/150;
        logoutButton.setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
        logoutButton.setLayoutParams(layoutLogin);
    }
}
