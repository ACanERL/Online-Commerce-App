package com.example.commerceapp.fragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commerceapp.R;
import com.example.commerceapp.adapter.CardAdapter;
import com.example.commerceapp.model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class BasketFragment extends Fragment implements CardAdapter.deleteProduct {
    RecyclerView recyclerView;
    CardAdapter cardAdapter;
    ArrayList<Products>list;
    FirebaseAuth mAuth;
    Context context;
    float totalPrice=0;
    Products products = new Products();
    String key,userId;

    public BasketFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth= FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        View rootView=inflater.inflate(R.layout.fragment_basket, container, false);
        recyclerView=rootView.findViewById(R.id.basketRecy);
        refRes();
        initRecycler();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(userId);

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    products.setTotalPrice(Float.parseFloat(String.valueOf(task.getResult().getValue())));
                }
            }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot data:snapshot.getChildren()){
                    Products products=data.getValue(Products.class);
                    list.add(products);
                    products.setKey(data.getKey());;
                    key=data.getRef().getKey();
                }
                //kaydırarak silme
              ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        Toast.makeText(getActivity(), "on Move", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                              Toast.makeText(getActivity(), "Ürün Silindi. ", Toast.LENGTH_SHORT).show();
                              CardAdapter adapter=new CardAdapter(list,context,BasketFragment.this);
                              myRef.child(list.get(viewHolder.getAdapterPosition()).getKey()).removeValue();
                              delete(viewHolder.getAdapterPosition());
                              adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                              adapter.notifyDataSetChanged();

                    }
                };
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
               itemTouchHelper.attachToRecyclerView(recyclerView);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        return rootView;
    }
    public void initRecycler(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list=new ArrayList<>();
        cardAdapter=new CardAdapter(list,getActivity(),this);
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
    }
    @Override
    public void delete(int position) {
            removeItem(position);
    }
    public void removeItem(int pos){
        list.remove(pos);
        cardAdapter.notifyItemRemoved(pos);
        cardAdapter.notifyDataSetChanged();
}
    public void refRes(){
        recyclerView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}