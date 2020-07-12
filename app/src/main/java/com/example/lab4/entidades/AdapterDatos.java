package com.example.lab4.entidades;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4.R;
import com.example.lab4.entidades.ComentariosDTO;

import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> {

    ArrayList<ComentariosDTO> listComentarios;

    public AdapterDatos(ArrayList<ComentariosDTO> listComentarios) {
        this.listComentarios = listComentarios;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentarios, null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {

        holder.nombreCom.setText(listComentarios.get(position).getNombre());
        holder.fechaCom.setText(listComentarios.get(position).getFecha());
        holder.contenidoCom.setText(listComentarios.get(position).getContenido());
    }

    @Override
    public int getItemCount() {
        return listComentarios.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView nombreCom, fechaCom, contenidoCom;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombreCom = itemView.findViewById(R.id.textViewNombreCom);
            fechaCom = itemView.findViewById(R.id.textViewFechaCom);
            contenidoCom = itemView.findViewById(R.id.textViewContenidoCom);

        }
    }
}

