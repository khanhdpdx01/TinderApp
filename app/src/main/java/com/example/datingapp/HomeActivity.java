package com.example.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.datingapp.adapters.ViewPagerAdapter;
import com.example.datingapp.view.AccountFragment;
import com.example.datingapp.view.ActivityFragment;
import com.example.datingapp.view.ChatFragment;
import com.example.datingapp.view.SwipeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Context context;
//    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;

        BottomNavigationView bnv = findViewById(R.id.bottom_navigation);
        bnv.setVisibility(View.VISIBLE);

//        ArrayList<Fragment> fragList = new ArrayList<>();
//        fragList.add(new AccountFragment());
//        fragList.add(new SwipeFragment());
//        fragList.add(new ActivityFragment());

//        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(fragList, getSupportFragmentManager());
//        viewPager = findViewById(R.id.view_pager);
//        viewPager.setAdapter(pagerAdapter);
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.setCurrentItem(1);

        bnv.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account:
//                viewPager.setCurrentItem(0);
                loadFragment(new AccountFragment());
                break;
            case R.id.fire:
//                viewPager.setCurrentItem(1);
                loadFragment(new SwipeFragment());
                break;
            case R.id.chat:
//                viewPager.setCurrentItem(2);
                loadFragment(new ChatFragment());
                break;
        }

        return true;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_container_home, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}