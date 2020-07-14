package com.example.lab4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ActivitySubirFoto extends AppCompatActivity {

    ImageView imageSubida;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_foto);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        imageSubida = (ImageView) findViewById(R.id.imageViewFoto);
    }

    public void subirImagenStream(View view) {
        EditText descrip = findViewById(R.id.editTextDescrip);
        if (descrip.getText().length() == 0) {
            descrip.setError("Debe llenar la descripción de la foto");
        }else{

            if (validarPermisos(1)) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                int requestCode = 1;
                intent.setType("image/");
                startActivityForResult(intent, requestCode);
            }

        }

    }

    public boolean validarPermisos(int requestCode) {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestCode);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                subirImagenStream(null);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (resultCode == RESULT_OK) {
            Uri path = data.getData();
            imageSubida.setImageURI(path);
            StorageReference imagenesRef = storageReference.child("imagenes/ydecian.jpeg");

            EditText descrip = findViewById(R.id.editTextDescrip);
            String descripcion = String.valueOf(descrip.getText());
//            String uid = currentUser.getUid();
//            String displayName = currentUser.getDisplayName();
            String fecha = "12/07/2020";
//            StorageMetadata metadata = new StorageMetadata.Builder()
//                    .setCustomMetadata("ID",uid)
//                    .setCustomMetadata("Usuario",displayName)
//                    .setCustomMetadata("fecha",fecha)
//                    .setCustomMetadata("Descripción",descripcion).build();
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("id","12345789")
                    .setCustomMetadata("usuario","Brayan Dadick")
                    .setCustomMetadata("fecha",fecha)
                    .setCustomMetadata("descripcion",descripcion).build();

            imagenesRef.putFile(path,metadata)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("infoAapp", "subida exitosa");
                            Toast.makeText(ActivitySubirFoto.this, "subida exitosa", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("infoAapp", "error al subir");
                    Toast.makeText(ActivitySubirFoto.this, "error al subir", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



}