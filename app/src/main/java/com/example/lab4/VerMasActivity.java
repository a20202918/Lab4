package com.example.lab4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.lab4.entidades.AdapterDatos;
import com.example.lab4.entidades.ComentariosDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerMasActivity extends AppCompatActivity {

    ArrayList<ComentariosDTO> listComentarios;
    RecyclerView recyclerViewComentarios;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mas);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String username = user.getDisplayName();
        Log.d("username", ""+user.getDisplayName() );

        Toolbar toolbar = findViewById(R.id.username_toolbar);
        TextView userName = findViewById(R.id.userName);

        //userName.setText(username_toolbar);
        //setSupportActionBar(toolbar);

        databaseReference =FirebaseDatabase.getInstance().getReference();

        listComentarios = new ArrayList<>();

        recyclerViewComentarios = findViewById(R.id.recyclerView);
        recyclerViewComentarios.setLayoutManager(new LinearLayoutManager(this));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() != null){

                    ComentariosDTO comentario = dataSnapshot.getValue(ComentariosDTO.class);
                    Log.d("infoApp", comentario.getContenido());

                    listComentarios.add(new ComentariosDTO(comentario.getContenido(),comentario.getFecha(),comentario.getNombre()));

                    AdapterDatos adapter = new AdapterDatos(listComentarios);
                    recyclerViewComentarios.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void btnAgregarComentario(View view){
        Intent intent = new Intent(this, AgregarComentarioActivity.class);
        int requestCode = 1;
        startActivityForResult(intent, requestCode);
    }
}