package com.example.dairydelight.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dairydelight.Adapter.ItemAdapter;
import com.example.dairydelight.Models.TabModel;
import com.example.dairydelight.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommonTabFragment extends Fragment {

    private List<TabModel> tabModels = new ArrayList<>(); // Initialize to avoid null issues

    public static CommonTabFragment newInstance(List<TabModel> tabModels) {
        CommonTabFragment fragment = new CommonTabFragment();
        Bundle args = new Bundle();
        args.putSerializable("tab_position", (Serializable) tabModels);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tabModels = (List<TabModel>) getArguments().getSerializable("tab_position");
            if (tabModels == null) {
                tabModels = new ArrayList<>(); // Fallback to avoid null pointer exception
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_tab, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        if (tabModels != null && !tabModels.isEmpty()) {
            ItemAdapter itemAdapter = new ItemAdapter(tabModels, getContext());
            recyclerView.setAdapter(itemAdapter);
        } else {
            Log.d("CommonTabFragment", "No data to display");
        }

        return view;
    }
}
