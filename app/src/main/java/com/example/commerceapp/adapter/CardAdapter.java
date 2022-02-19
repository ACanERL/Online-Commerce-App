package com.example.commerceapp.adapter;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commerceapp.R;

import com.example.commerceapp.model.Products;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.squareup.picasso.Picasso;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    ArrayList<Products> list;
    Context context;
    deleteProduct deleteProduct;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    float totalPrice=0;

    public CardAdapter(ArrayList<Products> list, Context context,deleteProduct deleteProduct) {
        this.list = list;
        this.context = context;
        this.deleteProduct=deleteProduct;
    }

    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {

            int pst=holder.getAdapterPosition();
            final Products products=list.get(position);
            if(list==null){
             Toast.makeText(context,"Sepet Boş.",Toast.LENGTH_SHORT).show();
            }

        holder.urunadi.setText(products.getTitle());
        holder.urunfiyati.setText(String.valueOf(products.getPrice()));
        holder.adet.setText("Ürün Id:"+" "+products.getId());
        holder.ratingBar.setRating((float) products.getRating().rate);
        Picasso.get().load(products.getImage()).into(holder.imageView);
        totalPrice= (float) (totalPrice+list.get(position).getPrice());
        products.setTotalPrice(totalPrice);

        holder.total.setText(String.valueOf(products.getTotalPrice()));
        mAuth= FirebaseAuth.getInstance();
        String userId=mAuth.getCurrentUser().getUid();



        holder.total.setText("Toplam Fiyat:"+" "+String.valueOf(products.getTotalPrice())+"TL");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(userId);

        //tıklayarak silme
        holder.itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(list.get(holder.getAdapterPosition()).getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog=new ProgressDialog(context);
                        progressDialog.setTitle("Ürün Siliniyor...");
                        progressDialog.show();
                        Timer timer=new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        },1500);
                    }
                });
                notifyItemRemoved(position);
                notifyDataSetChanged();
                list.clear();
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView urunadi,urunfiyati,acıklama,adet,urunrate,total;
        ImageView imageView,delete;
        RatingBar ratingBar;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            urunadi=itemView.findViewById(R.id.urunadi);
            urunfiyati=itemView.findViewById(R.id.urunfiyat);
            acıklama=itemView.findViewById(R.id.urunrate);
            adet=itemView.findViewById(R.id.adet);
            imageView=itemView.findViewById(R.id.imageCard);
            urunrate=itemView.findViewById(R.id.urunrate);
            ratingBar=itemView.findViewById(R.id.ratingBar3);
            delete=imageView.findViewById(R.id.delete);
            cardView=itemView.findViewById(R.id.card);
            total=itemView.findViewById(R.id.total);
        }
    }
    public interface deleteProduct{
        void delete(int position);
    }
    public void setTotalPrice(Products products){
      products.setTotalPrice(totalPrice);
    }
}
