package com.example.commerceapp.adapter;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commerceapp.R;
import com.example.commerceapp.activity.ProductDetail;
import com.example.commerceapp.fragment.BasketFragment;
import com.example.commerceapp.model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements Filterable {
    Context context;
    List<Products>productsList;
    onItemClicked onItemClick;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    int count=0;
    int total=0;
    String key1;
    Products products;
    List<Products>productsListfull;
    public CustomAdapter(Context context, List<Products> productsList,onItemClicked onItemClick) {
        this.context = context;
        this.productsList = productsList;
        this.onItemClick=onItemClick;
        productsListfull=new ArrayList<>(productsList);

    }
    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mAuth=FirebaseAuth.getInstance();
        String userId=mAuth.getCurrentUser().getUid();
        holder.title.setText("Ürün Adı:"+" "+productsList.get(position).getTitle());
        holder.rate.setText("Ürün Puanı:" + " " + productsList.get(position).getRating().rate);
        holder.ratingBar.setRating((float) productsList.get(position).getRating().rate);
        holder.price.setText("TL"+" "+String.valueOf(productsList.get(position).getPrice()));
        Picasso.get().load(productsList.get(position).getImage()).into(holder.image);
        holder.cat.setText(productsList.get(position).getCategory());
        holder.descp.setText(productsList.get(position).getDescription());

        double fiyat=productsList.get(position).getPrice();
        System.out.println("Fiyat:"+fiyat);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(userId).push();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(productsList.get(position));

            }
        });

        holder.buy.setOnClickListener(view -> {
            total= (int) (total+fiyat);
            System.out.println("Toplam Total Fiyat:"+total);
            mDatabase.child("users").child(userId).setValue(total);

            count=count+1;
            key1=FirebaseDatabase.getInstance().getReference().push().getKey();
            productsList.get(position).setKey(key1);
            myRef.setValue(productsList.get(position));
            Toast.makeText(context,"Ürün Eklendi.",Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public Filter getFilter() {
        return  productsListFilter;
    }
    public Filter productsListFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Products>filterList=new ArrayList<>();
            if(charSequence==null || charSequence.length()==0){
                filterList.addAll(productsListfull);
            }else{
                String filtpattern=charSequence.toString().toLowerCase().trim();
                for(Products item:productsListfull){
                    if(item.getTitle().toLowerCase().contains(filtpattern)){
                        filterList.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filterList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            productsList.clear();
            productsList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,rate,price,idtxt,cat,descp,badge;
        ImageView image;
        RatingBar ratingBar;
        Button buy;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            rate=itemView.findViewById(R.id.detailrate);
            image=itemView.findViewById(R.id.image);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            price=itemView.findViewById(R.id.price);
            idtxt=itemView.findViewById(R.id.idtxt);
            cat=itemView.findViewById(R.id.catTxt);
            descp=itemView.findViewById(R.id.descTxt);
            buy=itemView.findViewById(R.id.buy);
            badge=itemView.findViewById(R.id.badge);
        }

    }
    public  interface  onItemClicked {
        void onItemClick(Products model); //recycler item tıklama

    }
}
