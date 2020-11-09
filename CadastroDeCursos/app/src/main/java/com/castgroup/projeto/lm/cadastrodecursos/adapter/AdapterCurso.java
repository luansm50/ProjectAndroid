package com.castgroup.projeto.lm.cadastrodecursos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.castgroup.projeto.lm.cadastrodecursos.R;
import com.castgroup.projeto.lm.cadastrodecursos.model.Curso;

import java.text.SimpleDateFormat;
import java.util.List;

import lombok.Data;

@Data
public class AdapterCurso extends RecyclerView.Adapter<AdapterCurso.MyViewHolder> {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    List<Curso> cursoList;
    private Context context;

    public AdapterCurso(List<Curso> cursoList, Context context)
    {
        this.cursoList = cursoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cursos, parent, false);
        return new AdapterCurso.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Curso curso = cursoList.get(position);

        holder.txtCourseSubjectDescription.setText(curso.getDescricaoAssunto());
        holder.txtCourseStartDate.setText(curso.getDataInicio());
    }

    @Override
    public int getItemCount() {
        return cursoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtCourseSubjectDescription;
        TextView txtCourseStartDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCourseSubjectDescription = itemView.findViewById(R.id.txtCategoriaDescricao);
            txtCourseStartDate = itemView.findViewById(R.id.txtCursoStartDate);
        }
    }
}
