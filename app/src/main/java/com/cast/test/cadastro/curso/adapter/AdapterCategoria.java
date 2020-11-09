package com.cast.test.cadastro.curso.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cast.test.cadastro.curso.R;
import com.cast.test.cadastro.curso.model.Categoria;

import java.text.SimpleDateFormat;
import java.util.List;

import lombok.Data;

@Data
public class AdapterCategoria extends RecyclerView.Adapter<AdapterCategoria.MyViewHolder> {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    List<Categoria> categoriaList;
    private Context context;

    public AdapterCategoria(List<Categoria> categoriaList, Context context)
    {
        this.categoriaList = categoriaList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_categoria, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Categoria categoria = categoriaList.get(position);

        holder.txtCategoriaDescricao.setText(categoria.getDescricao());
    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtCategoriaDescricao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCategoriaDescricao = itemView.findViewById(R.id.txtCategoriaDescricao);
        }
    }
}
