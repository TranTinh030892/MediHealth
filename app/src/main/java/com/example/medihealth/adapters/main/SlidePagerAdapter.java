package com.example.medihealth.adapters.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.medihealth.fragments.CustomerFragment.slide1;
import com.example.medihealth.fragments.CustomerFragment.slide2;
import com.example.medihealth.fragments.CustomerFragment.slide3;
import com.example.medihealth.fragments.CustomerFragment.slide4;
import com.example.medihealth.fragments.CustomerFragment.slide5;
import com.example.medihealth.fragments.CustomerFragment.slide6;

public class SlidePagerAdapter extends FragmentStateAdapter {
    public SlidePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            return new slide1();
        }
        if (position == 1){
            return new slide2();
        }
        if (position == 2){
            return new slide3();
        }
        if (position == 3){
            return new slide4();
        }
        if (position == 4){
            return new slide5();
        }
        if (position == 5){
            return new slide6();
        }
        else return null;
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
