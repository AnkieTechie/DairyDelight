package com.example.dairydelight.Fragments;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dairydelight.Activity.MainActivity;
import com.example.dairydelight.Activity.SliderDetailsActivity;
import com.example.dairydelight.Adapter.ItemAdapter;
import com.example.dairydelight.Adapter.ViewPagerAdapter;
import com.example.dairydelight.Api.ApiServices;
import com.example.dairydelight.Api.RetrofitClient;
import com.example.dairydelight.Api.SliderRequest;
import com.example.dairydelight.Api.SliderResponse;
import com.example.dairydelight.Api.TabRequest;
import com.example.dairydelight.Api.TabResponse;
import com.example.dairydelight.Models.SliderModel;
import com.example.dairydelight.Models.TabModel;
import com.example.dairydelight.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomFragment extends Fragment {

    View view;
    TabLayout tabLayout;
    ViewPager2 viewPager_tab;
    ImageCarousel carousel;
    ViewPagerAdapter viewPagerAdapter;

    private List<SliderModel> products;
    public List<CarouselItem> carouselItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hom, container, false);
        viewPager_tab = view.findViewById(R.id.viewPager_tab);
        tabLayout = view.findViewById(R.id.tabLayout);
        carousel = view.findViewById(R.id.carousel);
        carousel.registerLifecycle(getLifecycle());
        carouselItems = new ArrayList<>();
        ApiServices apiServices = RetrofitClient.getRetrofit().create(ApiServices.class);
        fetchSlider(apiServices);
        fetchTabList(apiServices);
        return view;
    }

    private void fetchTabList(ApiServices apiServices) {
        TabRequest tabRequest = new TabRequest("49");
        Call<TabResponse> call = apiServices.getTabs(tabRequest);
        call.enqueue(new Callback<TabResponse>() {
            @Override
            public void onResponse(@NonNull Call<TabResponse> call, @NonNull Response<TabResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TabModel> tabModels=response.body().getProducts();
                    List<TabModel> list1 = new ArrayList<>();
                    List<TabModel> list2 = new ArrayList<>();
                    List<TabModel> list3 = new ArrayList<>();

                    for (TabModel tabModel : tabModels) {
                        String title = tabModel.getTitle().toLowerCase(); // Get title in lower case for case insensitive comparison

                        // Check conditions to populate the lists
                        if (title.contains("milk")) {
                            list2.add(tabModel); // Add to list2 for "Milk"
                        } else if (title.contains("ghee")) {
                            list1.add(tabModel); // Example condition for list1
                        } else if (title.contains("curd")) {
                            list3.add(tabModel); // Example condition for list3
                        }
                    }

                    if (!list1.isEmpty() || !list2.isEmpty() || !list3.isEmpty()) {
                        List<List<TabModel>> listList = new ArrayList<>();
                        listList.add(list1);
                        listList.add(list2);
                        listList.add(list3);
                        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle(), listList);
                        viewPager_tab.setAdapter(viewPagerAdapter);
                        new TabLayoutMediator(tabLayout, viewPager_tab, (tab, position) -> {
                            switch (position) {
                                case 0:
                                    tab.setText("Fast selling");
                                    break;
                                case 1:
                                    tab.setText("Milk");
                                    break;
                                case 2:
                                    tab.setText("Curd");
                                    break;
                            }
                        }).attach();
                    } else {
                        Toast.makeText(getContext(), "No tabs available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TabResponse> call, @NonNull Throwable throwable) {
                Log.e("API_ERROR", throwable.getMessage(), throwable);
                Toast.makeText(getContext(), "Failed to retrieve tabs", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void fetchSlider(ApiServices apiServices) {
        SliderRequest request=new SliderRequest("52");
        Call<SliderResponse> call=apiServices.getSlider(request);
        call.enqueue(new Callback<SliderResponse>() {
            @Override
            public void onResponse(@NonNull Call<SliderResponse> call, @NonNull Response<SliderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SliderModel> productList = response.body().getProducts();
                    for (SliderModel product : productList) {
                        if (product.getImage() != null) {
                            // Add image to the carousel list

                            CarouselItem carouselItem=new CarouselItem(product.getImage(),product.getTitle());
                            carousel.setCarouselListener(new CarouselListener() {
                                @Nullable
                                @Override
                                public ViewBinding onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
                                    return null;
                                }

                                @Override
                                public void onBindViewHolder(@NonNull ViewBinding viewBinding, @NonNull CarouselItem carouselItem, int i) {

                                }

                                @Override
                                public void onClick(int i, @NonNull CarouselItem carouselItem) {

                                    Intent intent=new Intent(getContext(), SliderDetailsActivity.class).
                                            putExtra("imageUri",productList.get(i).getImage()).
                                    putExtra("priceee",productList.get(i).getPrice()).
                                            putExtra("title",productList.get(i).getTitle());
                                    startActivity(intent);

                                }

                                @Override
                                public void onLongClick(int i, @NonNull CarouselItem carouselItem) {

                                }
                            });

                            carouselItems.add(carouselItem);
                        }
                    }
                    carousel.addData(carouselItems);
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SliderResponse> call, @NonNull Throwable t) {
                Log.e("API_ERROR", Objects.requireNonNull(t.getMessage()));
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).showToolbar();
    }


}