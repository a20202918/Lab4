package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerMasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mas);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        TextView textViewNombre = findViewById(R.id.textViewNombre);
        textViewNombre.setText(user.getDisplayName());

    }

    public void btnAgregarComentario(View view){
        Intent intent = new Intent(this, AgregarComentarioActivity.class);
        int requestCode = 1;
        startActivityForResult(intent, requestCode);
    }



}