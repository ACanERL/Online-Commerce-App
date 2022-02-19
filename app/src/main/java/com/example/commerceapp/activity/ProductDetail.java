package com.example.commerceapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.commerceapp.R;
import com.example.commerceapp.databinding.ActivityProductDetailBinding;
import com.example.commerceapp.model.Products;
import com.squareup.picasso.Picasso;

public class ProductDetail extends AppCompatActivity {
    private ActivityProductDetailBinding binding;
    Products products;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_product_detail);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

         products= (Products) getIntent().getSerializableExtra("data");
         binding.id.setText("Ürün Id:"+" "+String.valueOf(products.getId()));
         binding.detailCat.setText("Kategori :"+" "+products.getCategory());
         binding.detailDesc.setText("Açıklama:"+" "+products.getDescription());
         binding.detailTitle.setText("Ürün Adi:"+" "+products.getTitle());
         binding.detailRate.setText("Ürün Puanı : "+products.getRating().rate);
         binding.ratingBar2.setRating((float) products.getRating().rate);
         binding.detailPrice.setText(String.valueOf(products.getPrice())+" TL");
         Picasso.get().load(products.getImage()).into(binding.imageView);

    }
}