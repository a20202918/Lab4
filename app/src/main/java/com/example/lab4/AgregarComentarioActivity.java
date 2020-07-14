package com.example.lab4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab4.entidades.ComentariosDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AgregarComentarioActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_comentario);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String username = user.getDisplayName();
        Log.e("username", ""+user.getDisplayName() );


        Toolbar toolbar = findViewById(R.id.username_toolbar);
        TextView userName = findViewById(R.id.userName);

        userName.setText(username);
        setSupportActionBar(toolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

    }

    public void btnPublicar(View view){
        EditText editTextComentario = findViewById(R.id.editTextComentario);
        String contenido = editTextComentario.getText().toString();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String date = df.format(c.getTime());

        ComentariosDTO comentario = new ComentariosDTO();
        comentario.setContenido(contenido);
        comentario.setFecha(date);
        comentario.setNombre(user.getDisplayName());

        String idfoto = "1";

        databaseReference.child(idfoto).push().setValue(comentario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Comentario guardado con éxito", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getApplicationContext(),"Comentario no guardado", Toast.LENGTH_SHORT).show();
                Log.d("infoAppError", e.getMessage());
            }
        });

        finish();

    }

}