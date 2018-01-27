package com.reminders.location.locatoinreminder.view.ui.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.pojo.User;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;
import com.reminders.location.locatoinreminder.util.Utils;
import com.reminders.location.locatoinreminder.view.BaseModel.BaseActivity;
import com.reminders.location.locatoinreminder.view.adapters.WalkThroughViewPagerAdapter;
import com.reminders.location.locatoinreminder.viewmodel.WalkthroughActivityViewModel;

import java.io.FileNotFoundException;
import java.util.Collections;

import butterknife.BindView;

public class WalkthroughActivity extends BaseActivity {

    private static final int REQUEST_PERMISSION = 0;
    String[] permissionsRequired = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION};
    @BindView(R.id.cardView)
    CardView name_card;
    @BindView(R.id.edit_name)
    EditText editText;
    @BindView(R.id.bell)
    ImageView initial_background;
    @BindView(R.id.initials)
    TextView initials;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.start)
    Button start;
    @BindView(R.id.next)
    FloatingActionButton next;
    private SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();
    private int[] buttons = {R.id.b1, R.id.b2, R.id.b3};
    private int currentAPIVersion;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private WalkthroughActivityViewModel walkthroughActivityViewModel;

    public WalkthroughActivity() throws FileNotFoundException {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        walkthroughActivityViewModel = ViewModelProviders.of(this).get(WalkthroughActivityViewModel.class);

        findViewById(buttons[0]).setSelected(true);
        databaseReference = FirebaseDatabase.getInstance().getReference(ConstantVar.USERS);

        viewPager.setAdapter(new WalkThroughViewPagerAdapter(this, getLayoutInflater(), walkthroughActivityViewModel));
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 3; i++) {
                    findViewById(buttons[i]).setSelected(false);
                }
                findViewById(buttons[position]).setSelected(true);
                if (position == 2) {
                    start.setVisibility(View.VISIBLE);
                    next.hide();
                } else {
                    next.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        currentAPIVersion = Build.VERSION.SDK_INT;

        if (currentAPIVersion < android.os.Build.VERSION_CODES.M) {
            start.setText(ConstantVar.VERIFY_PHN);
        }
    }

    public void buttonClicked(View view) {      //Step 1
        if (Utils.isConnectedToNetwork(this)) {
            if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
                checkPermission();
            } else {
                startVerification();
            }
        } else
            Snackbar.make(viewPager, getResources().getString(R.string.no_net_connect), Snackbar.LENGTH_SHORT).show();
    }

    public void checkPermission() {
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])
                        || ContextCompat.checkSelfPermission(this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                        ) {
                    Snackbar.make(findViewById(android.R.id.content),
                            R.string.permission_rational,
                            Snackbar.LENGTH_INDEFINITE).setAction(getResources().getString(R.string.enable),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(WalkthroughActivity.this,
                                            permissionsRequired,
                                            REQUEST_PERMISSION);
                                }
                            }).show();
                } else {
                    ActivityCompat.requestPermissions(this, permissionsRequired, REQUEST_PERMISSION);
                }
            } else {
                startVerification();
            }

        } else {
            startVerification();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startVerification();
                } else {
                    Snackbar.make(viewPager, R.string.permission_rational,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(WalkthroughActivity.this,
                                            permissionsRequired,
                                            REQUEST_PERMISSION);
                                }
                            }).show();
                }
                break;
        }
    }

    public void startVerification() {


        mAuth = FirebaseAuth.getInstance();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(
                                Collections.singletonList(
                                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                ))
                        .build(),
                ConstantVar.RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantVar.RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                currentUser = mAuth.getCurrentUser();
                // FirebaseMessaging.getInstance().subscribeToTopic(currentUser.getUid());
                if (currentUser.getDisplayName() == null)
                    viewCardView();
                else
                    //Toast.makeText(this, ""+currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                    loginUserOnServer();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, getResources().getString(R.string.login_canceled), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, getResources().getString(R.string.no_net_connect), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    return;
                }
            }
        }
    }

    private void viewCardView() {
        name_card.setVisibility(View.VISIBLE);
        editText.requestFocus();
    }

    public void doneAll(final View view) {
        if (editText.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, getResources().getString(R.string.enter_name), Toast.LENGTH_SHORT).show();
        } else {
            final String Name = editText.getText().toString();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(Name)
                    .build();
            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            initials.setText(new Utils().getInitial(Name));
                            initial_background.startAnimation(AnimationUtils.loadAnimation(WalkthroughActivity.this, R.anim.selection));
                            initial_background.setVisibility(View.VISIBLE);
                            view.setClickable(false);
                            loginUserOnServer();
                        }
                    });


        }
    }

    private void loginUserOnServer() {
        boolean checkUser = checkUserInDatabase();
        if (!checkUser)
            insertUserOnDatabase();
        gotoMain();
    }

    public boolean checkUserInDatabase() {
        //User user = new User(currentUser.getDisplayName(), currentUser.getPhoneNumber());
        final boolean[] userRegistered = new boolean[1];
        databaseReference.child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        if (u == null) {
                            userRegistered[0] = false;
                        } else {
                            userRegistered[0] = true;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(WalkthroughActivity.this, getResources().getString(R.string.failed_to_read), Toast.LENGTH_LONG).show();
                        userRegistered[0] = false;
                    }
                });
        return userRegistered[0];
    }

    public void insertUserOnDatabase() {
        User user = new User(currentUser.getDisplayName(), currentUser.getPhoneNumber());
        databaseReference.child(currentUser.getUid()).setValue(user);


    }


    public void gotoMain() {
        editText.clearFocus();
        sharedPreferenceSingleton.saveAs(this, ConstantVar.CONSTANT_SELF_NAME, currentUser.getDisplayName());
        sharedPreferenceSingleton.saveAs(this, ConstantVar.CONTACT_SELF_NUMBER, currentUser.getPhoneNumber());
        sharedPreferenceSingleton.saveAs(this, ConstantVar.LOGGED, true);
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.view_enter, R.anim.view_exit);
        finish();
    }


    public void nextPage(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_walkthrough;
    }
}