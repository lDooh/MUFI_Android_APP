package com.mufi.mufi.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mufi.mufi.DTO.PhotoInfo;
import com.mufi.mufi.MufiTag;
import com.mufi.mufi.R;
import com.mufi.mufi.databinding.ActivityMainBinding;
import com.mufi.mufi.ui.fragment.CardFragment;
import com.mufi.mufi.ui.fragment.HomeFragment;
import com.mufi.mufi.ui.fragment.PhotoFeedFragment;
import com.mufi.mufi.ui.fragment.PhotoOriginalFragment;
import com.mufi.mufi.ui.fragment.PhotoPaymentFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    DrawerLayout drawerLayout;
    HomeFragment homeFragment;
    PhotoFeedFragment photoFeedFragment;
    CardFragment cardFragment;
    PhotoPaymentFragment photoPaymentFragment;
    PhotoOriginalFragment photoOriginalFragment;
    private PhotoInfo photoInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 파이어베이스 푸시 알림 설정
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(MufiTag.DEBUG_TAG_FCM, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(MufiTag.DEBUG_TAG_FCM, msg);
                    }
                });

        Toolbar toolbar = binding.toolbar;
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 햄버거 아이콘 클릭 리스너
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        drawerLayout = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);

        homeFragment = new HomeFragment();
        photoFeedFragment = new PhotoFeedFragment();
        cardFragment = new CardFragment();
        photoPaymentFragment = new PhotoPaymentFragment();
        photoOriginalFragment = new PhotoOriginalFragment();

        // 홈 프래그먼트부터 시작
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.nav_host_fragment_content_main, homeFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 네비게이션 드로어에서 메뉴 선택 시
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, homeFragment)
                    .commit();
        } else if (id == R.id.nav_photo) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, photoFeedFragment)
                    .commit();
        } else if (id == R.id.nav_card) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, cardFragment)
                    .commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // PhotoFragment에서 PhotoViewFragment로 진입하기 위해 호출
    public void createPhotoPaymentFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, photoPaymentFragment)
                .addToBackStack(null)
                .commit();
    }

    public void createPhotoOriginalFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, photoOriginalFragment)
                .addToBackStack(null)
                .commit();
    }

    // 뒤로가기 버튼 클릭 시
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.exit);
            builder.setMessage(R.string.app_exit);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    finish();
                    return;
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });

            builder.show();

            return true;
        }

        return false;
    }

    public void setPhotoInfo(PhotoInfo photoInfo) {
        this.photoInfo = photoInfo;
    }

    public PhotoInfo getPhotoInfo() {
        return photoInfo;
    }
}