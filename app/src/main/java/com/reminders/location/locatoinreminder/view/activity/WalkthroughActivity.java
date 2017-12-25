package com.reminders.location.locatoinreminder.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reminders.location.locatoinreminder.R;
import com.reminders.location.locatoinreminder.adapters.WalkThroughViewPagerAdapter;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;
import com.reminders.location.locatoinreminder.util.Utils;
import com.reminders.location.locatoinreminder.view.BaseModel.BaseActivity;

import butterknife.BindView;

public class WalkthroughActivity extends BaseActivity {

    private SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();
    private static final int REQUEST_PERMISSION = 0;
    String[] permissionsRequired = new String[]{Manifest.permission.READ_CONTACTS};
    @BindView(R.id.cardView)
    CardView name_card;
    @BindView(R.id.edit_name)
    EditText editText;
    @BindView(R.id.bell)
    ImageView initial_background;
    @BindView(R.id.initials)
    TextView initials;
    private int[] buttons = {R.id.b1, R.id.b2, R.id.b3};
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.start)
    Button start;
    private int currentAPIVersion;
    @BindView(R.id.next)
    FloatingActionButton next;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(buttons[0]).setSelected(true);
        viewPager.setAdapter(new WalkThroughViewPagerAdapter(getLayoutInflater()));
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
            start.setText("Verify Phone Number");
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
            Snackbar.make(viewPager, "Not connected to the internet", Snackbar.LENGTH_SHORT).show();
    }

    public void checkPermission() {

        if (ContextCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])) {
                Snackbar.make(findViewById(android.R.id.content),
                        R.string.permission_rational,
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
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


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

    }
    public void doneAll(final View view) {
        if (editText.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else {
            final String Name = editText.getText().toString();


        }
    }


    public void nextPage(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_walkthrough;
    }
}
