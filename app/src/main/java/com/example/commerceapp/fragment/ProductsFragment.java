package com.example.commerceapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.Toast;


import com.example.commerceapp.R;
import com.example.commerceapp.activity.ProductDetail;
import com.example.commerceapp.adapter.CustomAdapter;
import com.example.commerceapp.api.ApiServis;
import com.example.commerceapp.api.RetrofitInstance;
import com.example.commerceapp.model.Products;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductsFragment extends Fragment implements CustomAdapter.onItemClicked {
    ProgressDialog progressDialog;
    Context context;
    CustomAdapter adapter;
    private List<Products> itemsInCartList;
    private int totalItemInCart = 0;
    List<Products> productsList;

    public ProductsFragment() {

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.my_menu2,menu);
        MenuItem menuItem= menu.findItem(R.id.ara);
        androidx.appcompat.widget.SearchView searchView= (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_products, container, false);
        setHasOptionsMenu(true);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Listeleniyor...");
        progressDialog.show();


        ApiServis apiServis = RetrofitInstance.getRetrofitInstance().create(ApiServis.class);
        Call<List<Products>> call = apiServis.getProducts();
        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful()) {
                    RecyclerView recyclerView = rootview.findViewById(R.id.recycler);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new CustomAdapter(getActivity(), response.body(), ProductsFragment.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();

                } else {
                    Toast.makeText(getActivity(), "Veri Yüklenemiyor.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
            }
        });
        return rootview;
    }

    @Override
    public void onItemClick(Products model) {
        Intent intent = new Intent(getActivity(), ProductDetail.class).putExtra("data", model);
        startActivity(intent);
    }



}