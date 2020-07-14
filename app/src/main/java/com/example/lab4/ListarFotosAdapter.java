package com.example.lab4;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4.entidades.Foto;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListarFotosAdapter
        extends RecyclerView.Adapter<ListarFotosAdapter.FotosViewHolder>
        implements View.OnClickListener{

    ArrayList<Foto> listFoto;

    Context context;
     private View.OnClickListener listener;

    public ListarFotosAdapter(Context c){
        //this.listFotos = lista;
        this.context = c;
    }


    @NonNull
    @Override
    public FotosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recyclerinicio, parent, false);
        FotosViewHolder fotosViewHolder = new FotosViewHolder(item);
        item.setOnClickListener(this);
        return fotosViewHolder;
    }

    @Override
    public void onBindViewHolder(FotosViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public void setOnClickListener(){}

    @Override
    public void onClick(View v) {

    }

    public class FotosViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        ImageView photo;
        TextView fecha;
        TextView guion;
        TextView cantidadComent;
        TextView descripcion;
        Button verMas;

        public FotosViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.textView_username);
            photo = itemView.findViewById(R.id.imageView_photo);
            fecha = itemView.findViewById(R.id.textView_fecha);
            guion = itemView.findViewById(R.id.textView_guion);
            cantidadComent = itemView.findViewById(R.id.textView_cantcomen);
            descripcion = itemView.findViewById(R.id.textView_descripcion);
            verMas = itemView.findViewById(R.id.button_vermas);
        }
    }

}
