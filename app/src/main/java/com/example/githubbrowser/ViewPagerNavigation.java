package com.example.githubbrowser;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.githubbrowser.profile.ProfileFragment;

public class ViewPagerNavigation extends FragmentStateAdapter {


    public ViewPagerNavigation(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       switch (position)
       {
           case 0:
               return new HomeFragment();
           case 3:
                return new ProfileFragment();
           default:
               return new HomeFragment();
       }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
