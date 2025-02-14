package com.example.dairydelight.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dairydelight.Activity.MainActivity;
import com.example.dairydelight.Adapter.OrderFargAdapter;
import com.example.dairydelight.Models.OrderClass;
import com.example.dairydelight.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    View view;
    FirebaseFirestore db;
    FirebaseAuth fb;
    List<OrderClass> list;
    OrderFargAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        progress=view.findViewById(R.id.progress);
        db=FirebaseFirestore.getInstance();
        fb=FirebaseAuth.getInstance();
        fetchOrderdetails();

        return view;
    }

    private void fetchOrderdetails() {
        String userId=fb.getCurrentUser().getUid();
        db.collection("user").document(userId).collection("order").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            list=new ArrayList<>();
                            for (DocumentSnapshot snapshot:queryDocumentSnapshots){
                                OrderClass orderClass=snapshot.toObject(OrderClass.class);
                                list.add(orderClass);
                            }
                            adapter=new OrderFargAdapter(list,getContext());
                            recyclerView.setAdapter(adapter);
                            progress.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).hideToolbar();
    }
}