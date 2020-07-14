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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivitySubir extends AppCompatActivity {

    ImageView imagen;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_subir);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
//
        imagen = findViewById(R.id.imageView);
    }

    public void subirImagenStream(View view) {
        EditText descrip = findViewById(R.id.editTextDescrip);
        if (descrip.getText().length() == 0) {
            descrip.setError("Debe llenar la descripción de la foto");
        } else {

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
    protected void onActivityResult(int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String uid = currentUser.getUid();
        String displayName = currentUser.getDisplayName();
        StorageReference usuarioRef = storageReference.child(uid);
        usuarioRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                int numImage = listResult.getItems().size();
                Log.d("infoAapp", String.valueOf(numImage));

                firebaseAuth = FirebaseAuth.getInstance();
                currentUser = firebaseAuth.getCurrentUser();
                Button bsubir = findViewById(R.id.buttonSubir);
                bsubir.setEnabled(false);
                if (resultCode == RESULT_OK) {
                    int i = numImage+1;
                    Uri path = data.getData();
                    imagen.setImageURI(path);
                    String uid = currentUser.getUid();
                    String displayName = currentUser.getDisplayName();
                    StorageReference imagenesRef = storageReference.child(uid + "/" + displayName + i);

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String date = df.format(c.getTime());

                    EditText descrip = findViewById(R.id.editTextDescrip);
                    String descripcion = String.valueOf(descrip.getText());


                    StorageMetadata metadata = new StorageMetadata.Builder()
                            .setCustomMetadata("ID", uid)
                            .setCustomMetadata("Usuario", displayName)
                            .setCustomMetadata("fecha", date)
                            .setCustomMetadata("Descripción", descripcion).build();

                    imagenesRef.putFile(path, metadata)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.d("infoAapp", "subida exitosa");
                                    Toast.makeText(MainActivitySubir.this, "subida exitosa", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("infoAapp", "error al subir");
                            Toast.makeText(MainActivitySubir.this, "error al subir", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            long bytetransf = taskSnapshot.getBytesTransferred();
                            long totalByteCount = taskSnapshot.getTotalByteCount();
                            double progreso = (100.0*bytetransf)/totalByteCount;
                            TextView prog = findViewById(R.id.progreso);
                            prog.setText("Porcentaje de subida "+ Math.round(progreso) + " %");
                            Log.d("infoAapp", "Porcentaje de subida "+ Math.round(progreso) + " %");
                            if (progreso == 100){
                                Button bsubir = findViewById(R.id.buttonSubir);
                                bsubir.setEnabled(true);
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                firebaseAuth = FirebaseAuth.getInstance();
                currentUser = firebaseAuth.getCurrentUser();

                Button bsubir = findViewById(R.id.buttonSubir);
                bsubir.setEnabled(false);

                if (resultCode == RESULT_OK) {
                    int i = 1;
                    Uri path = data.getData();
                    imagen.setImageURI(path);
                    String uid = currentUser.getUid();
                    String displayName = currentUser.getDisplayName();
                    StorageReference imagenesRef = storageReference.child(uid + "/" + displayName + i);

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String date = df.format(c.getTime());

                    EditText descrip = findViewById(R.id.editTextDescrip);
                    String descripcion = String.valueOf(descrip.getText());


                    StorageMetadata metadata = new StorageMetadata.Builder()
                            .setCustomMetadata("ID", uid)
                            .setCustomMetadata("Usuario", displayName)
                            .setCustomMetadata("fecha", date)
                            .setCustomMetadata("Descripción", descripcion).build();

                    imagenesRef.putFile(path, metadata)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.d("infoAapp", "subida exitosa");
                                    Toast.makeText(MainActivitySubir.this, "subida exitosa", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("infoAapp", "error al subir");
                            Toast.makeText(MainActivitySubir.this, "error al subir", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            long bytetransf = taskSnapshot.getBytesTransferred();
                            long totalByteCount = taskSnapshot.getTotalByteCount();
                            double progreso = (100.0*bytetransf)/totalByteCount;
                            TextView prog = findViewById(R.id.progreso);
                            prog.setText("Porcentaje de subida "+ Math.round(progreso) + " %");
                            Log.d("infoAapp", "Porcentaje de subida "+ Math.round(progreso) + " %");
                            if (progreso == 100){
                                Button bsubir = findViewById(R.id.buttonSubir);
                                bsubir.setEnabled(true);
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    });

                }
            }
        });

//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        Button bsubir = findViewById(R.id.buttonSubir);
//        bsubir.setEnabled(false);
//        if (resultCode == RESULT_OK) {
//            int i = 0;
//            Uri path = data.getData();
//            imagen.setImageURI(path);
//            String uid = currentUser.getUid();
//            String displayName = currentUser.getDisplayName();
//            StorageReference imagenesRef = storageReference.child(uid + "/" + displayName + i);
//
//            Calendar c = Calendar.getInstance();
//            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//            String date = df.format(c.getTime());
//
//            EditText descrip = findViewById(R.id.editTextDescrip);
//            String descripcion = String.valueOf(descrip.getText());
//
//
//            StorageMetadata metadata = new StorageMetadata.Builder()
//                    .setCustomMetadata("ID", uid)
//                    .setCustomMetadata("Usuario", displayName)
//                    .setCustomMetadata("fecha", date)
//                    .setCustomMetadata("Descripción", descripcion).build();
//
//            imagenesRef.putFile(path, metadata)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Log.d("infoAapp", "subida exitosa");
//                            Toast.makeText(MainActivitySubir.this, "subida exitosa", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d("infoAapp", "error al subir");
//                    Toast.makeText(MainActivitySubir.this, "error al subir", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                    long bytetransf = taskSnapshot.getBytesTransferred();
//                    long totalByteCount = taskSnapshot.getTotalByteCount();
//                    double progreso = (100.0*bytetransf)/totalByteCount;
//                    TextView prog = findViewById(R.id.progreso);
//                    prog.setText("Porcentaje de subida "+ Math.round(progreso) + " %");
//                    Log.d("infoAapp", "Porcentaje de subida "+ Math.round(progreso) + " %");
//                    if (progreso == 100){
//                        Button bsubir = findViewById(R.id.buttonSubir);
//                        bsubir.setEnabled(true);
//                        Intent intent = new Intent();
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//                }
//            });
//
//        }
    }

//    public void cargarFoto(View view) {
//        cargarImagen();
//    }
//
//    private void cargarImagen() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        int requestCode = 1;
//        intent.setType("image/");
//        startActivityForResult(intent, requestCode);
//    }



}