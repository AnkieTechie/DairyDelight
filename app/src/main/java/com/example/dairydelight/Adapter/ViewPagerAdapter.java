package com.example.dairydelight.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dairydelight.Fragments.CommonTabFragment;
import com.example.dairydelight.Models.TabModel;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final List<List<TabModel>> modelLists;

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<List<TabModel>> modelLists) {
        super(fragmentManager, lifecycle);
        this.modelLists = modelLists;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return CommonTabFragment.newInstance(modelLists.get(position));
    }

    @Override
    public int getItemCount() {
        return modelLists.size();
    }
}
