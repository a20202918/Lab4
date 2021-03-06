package com.example.lab4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.lab4.entidades.AdapterDatos;
import com.example.lab4.entidades.ComentariosDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.example.lab4.R.id.username_toolbar;

public class VerMasActivity extends AppCompatActivity {

    ArrayList<ComentariosDTO> listComentarios;
    RecyclerView recyclerViewComentarios;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mas);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String username = user.getDisplayName();
        Log.d("username", ""+user.getDisplayName() );

        Toolbar toolbar = findViewById(R.id.username_toolbar);
        //TextView userName = findViewById(R.id.userName);

        //userName.setText(username);
        setSupportActionBar(toolbar);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        String idfoto = "1"; // a cambiar con el recycler view
        String uid = currentUser.getUid();
        String nombre = currentUser.getDisplayName();

        String direccion = uid+"/"+nombre+idfoto;

        StorageReference imagenesRef = storageReference.child(direccion);

        ImageView imageViewFoto = findViewById(R.id.imageViewFoto);
        Glide.with(this).load(imagenesRef).into(imageViewFoto);

        imagenesRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                String usuario = storageMetadata.getCustomMetadata("Usuario");
                String fecha = storageMetadata.getCustomMetadata("fecha");
                String descripcion = storageMetadata.getCustomMetadata("Descripcion");

                //Log.d("infoApp",descripcion);

                TextView textViewNombre = findViewById(R.id.textViewNombre);
                textViewNombre.setText(usuario);

                TextView textViewFecha = findViewById(R.id.textViewFecha);
                textViewFecha.setText(fecha);

                TextView textViewDescripcion = findViewById(R.id.textViewDescripcion);
                textViewDescripcion.setText(descripcion);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("infoApp","error");
            }
        });

        databaseReference =FirebaseDatabase.getInstance().getReference();

        listComentarios = new ArrayList<>();

        recyclerViewComentarios = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerViewComentarios.setLayoutManager(layoutManager);

        databaseReference.child(idfoto).orderByChild("fecha").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                long numComentarios = dataSnapshot.getChildrenCount();
                TextView textViewNumComentarios = findViewById(R.id.textViewNumComentarios);
                String numComentariosStr = String.valueOf(numComentarios)+" comentarios";
                textViewNumComentarios.setText(numComentariosStr);

                if(dataSnapshot.getValue() != null){

                    ComentariosDTO comentario = dataSnapshot.getValue(ComentariosDTO.class);
                    Log.d("infoApp", comentario.getContenido());

                    listComentarios.add(new ComentariosDTO(comentario.getContenido(),comentario.getFecha(),comentario.getNombre()));

                    AdapterDatos adapter = new AdapterDatos(listComentarios);
                    recyclerViewComentarios.setAdapter(adapter);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                long numComentarios = dataSnapshot.getChildrenCount();
                TextView textViewNumComentarios = findViewById(R.id.textViewNumComentarios);
                String numComentariosStr = String.valueOf(numComentarios)+" comentarios";
                textViewNumComentarios.setText(numComentariosStr);

                if(dataSnapshot.getValue() != null){

                    ComentariosDTO comentario = dataSnapshot.getValue(ComentariosDTO.class);
                    Log.d("infoApp", comentario.getContenido());

                    listComentarios.add(new ComentariosDTO(comentario.getContenido(),comentario.getFecha(),comentario.getNombre()));

                    AdapterDatos adapter = new AdapterDatos(listComentarios);
                    recyclerViewComentarios.setAdapter(adapter);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_4vermas,menu);
        return true;
    }

    public void btnAgregarComentario(View view){
        Intent intent = new Intent(this, AgregarComentarioActivity.class);
        int requestCode = 1;
        startActivityForResult(intent, requestCode);
    }

}

