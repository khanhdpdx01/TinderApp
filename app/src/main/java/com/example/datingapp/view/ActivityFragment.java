package com.example.datingapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.datingapp.R;
import com.example.datingapp.adapters.ViewPagerAdapter;
import com.example.datingapp.databinding.FragmentActivityBinding;

import java.util.ArrayList;

public class ActivityFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private FragmentActivityBinding binding;


    public ActivityFragment() {
        // Required empty public constructor
    }


    public static ActivityFragment newInstance(String param1, String param2) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding = FragmentActivityBinding.bind(view);

        ArrayList<Fragment> fragList = new ArrayList<>();
        fragList.add(new ChatFragment());
        fragList.add(new FeedFragment());

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(fragList, getActivity()
                .getSupportFragmentManager());

        binding.viewPagerFrag.setAdapter(pagerAdapter);
        binding.viewPagerFrag.addOnPageChangeListener(this);

        binding.layoutChat.setOnClickListener(this);
        binding.layoutFeed.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_chat:
                binding.viewPagerFrag.setCurrentItem(0);
                binding.textChat.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.textFeed.setTextColor(getResources().getColor(R.color.light_gray));
                break;

            case R.id.layout_feed:
                binding.viewPagerFrag.setCurrentItem(1);
                binding.textChat.setTextColor(getResources().getColor(R.color.light_gray));
                binding.textFeed.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                binding.textChat.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.textFeed.setTextColor(getResources().getColor(R.color.light_gray));
                break;

            case 1:
                binding.textChat.setTextColor(getResources().getColor(R.color.light_gray));
                binding.textFeed.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}