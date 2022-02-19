package com.example.commerceapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commerceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class ProfilFragment extends Fragment {
    Context context;
    ProgressDialog progressDialog;
    public ProfilFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView email,uudi,ad;
        ImageView imageView;
        View v= inflater.inflate(R.layout.fragment_profil, container, false);
        email=v.findViewById(R.id.userEmail);
      //  uudi=v.findViewById(R.id.uuid);
        ad=v.findViewById(R.id.ad);
        imageView=v.findViewById(R.id.userPhoto);


        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String currentid=user.getUid();
        DocumentReference reference;
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();


        progressDialog=new ProgressDialog(getActivity());
        progressDialog.show();
        reference=firestore.collection("users").document(currentid);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.getResult().exists()){
                        String name=task.getResult().getString("ad");
                        String em=task.getResult().getString("email");
                        String url=task.getResult().getString("image");
                        progressDialog.dismiss();
                        Picasso.get().load(url).into(imageView);
                        email.setText("E-Mail:"+" "+em);
                        ad.setText("Kullanıcı Adi:"+" "+name);
                    }else{
                        Toast.makeText(context,"Hata Oluştu",Toast.LENGTH_SHORT).show();
                    }
            }
        });

      return v;
    }
}