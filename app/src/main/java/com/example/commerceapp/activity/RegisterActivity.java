package com.example.commerceapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.commerceapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    StorageReference storageRef;
    private Uri uriImage,url;
    String email,sifre,sifre2,ad;
    ProgressDialog progressDialog;
    FirebaseFirestore firestore;
    String userId;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();

        storageRef=FirebaseStorage.getInstance().getReference();
        firestore=FirebaseFirestore.getInstance();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        binding.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choseImage();
            }
        });

        binding.registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=binding.emailtxt.getText().toString();
                sifre=binding.passwordtxt.getText().toString();
                sifre2=binding.confirmpasstxt.getText().toString();
                ad=binding.nametxt.getText().toString();
                if(sifre.length()==6 && sifre.equals(sifre2)){
                    Toast.makeText(RegisterActivity.this,"Sifreler Ayni Degil ve 6 Karakterden Fazla Olmalı",Toast.LENGTH_SHORT).show();

                }
                else if(TextUtils.isEmpty(email) || TextUtils.isEmpty(sifre)){
                    Toast.makeText(RegisterActivity.this,"Email ve Sifre Boş Bırakılamaz.",Toast.LENGTH_SHORT).show();

                }
                else {
                    progressDialog=new ProgressDialog(RegisterActivity.this);
                    progressDialog.setTitle("Hesap Oluşturuluyor....");
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(email, sifre).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                uploadToImage(uriImage);
                                Toast.makeText(RegisterActivity.this, "Hesap Oluşuturuldu.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,"Hesap Oluşturulamadi.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
    private void choseImage(){
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
                uriImage=data.getData();
                binding.userImage.setImageURI(uriImage);

        }
    }

    private void uploadToImage(Uri uriImage) {
        final StorageReference fileRef  = FirebaseStorage.getInstance().getReference(System.currentTimeMillis()+getFileExtension(uriImage) );
        fileRef.putFile(uriImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url=uri;
                        userId=mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference=firestore.collection("users").document(userId);
                        Map<String,Object>user=new HashMap<>();
                        user.put("email",email);
                        user.put("sifre",sifre);
                        user.put("ad",ad);
                        user.put("image",url.toString());
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(RegisterActivity.this,"Resim Yüklendi." + url.toString(),Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
            }
        });
    }

    public  String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


}