package com.example.commerceapp.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commerceapp.R;
import com.example.commerceapp.adapter.CustomAdapter;
import com.example.commerceapp.databinding.ActivityMainBinding;
import com.example.commerceapp.model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements  CustomAdapter.onItemClicked{
    NavController navController;
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    String sifre_1;
    String email_1;
    Map<String, Object> sayac;
    List<Products>list;
    BadgeDrawable badge;
    CustomAdapter adapter;
    FirebaseFirestore db;
    String userId;
    int a1;
    Products products;
    TextView badgetxt;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.my_menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View alertdialog = getLayoutInflater().inflate(R.layout.custom_alert, null);
        EditText ad = alertdialog.findViewById(R.id.dialogad);
        EditText email = alertdialog.findViewById(R.id.dialogemail);
        EditText sifre = alertdialog.findViewById(R.id.dialogpass);
        Button button = alertdialog.findViewById(R.id.button2);
        String ad_1 = ad.getText().toString();
        badgetxt=findViewById(R.id.textView5);


        builder.setView(alertdialog);
        builder.setTitle("Ayarlar");
        builder.setMessage("Ad ve Email Değiştir.");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_1 = email.getText().toString();
                sifre_1 = sifre.getText().toString();
                mAuth = FirebaseAuth.getInstance();
                mAuth.getCurrentUser().updateEmail(email_1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ayarlar:
                //   alertDialog();
                break;
            case R.id.cıkıs:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        products= (Products) getIntent().getSerializableExtra("data");


        badge=binding.bottomNavigationView.getOrCreateBadge(R.id.basketFragment);
        badge.setNumber(0);
        badge=binding.bottomNavigationView.getBadge(2);



        db=FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("temp").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String s= String.valueOf(task.getResult().get("sayac"));
                        System.out.println("S:"+s);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    @Override
    public void onItemClick(Products model) {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostResume() {

        super.onPostResume();
    }
}

