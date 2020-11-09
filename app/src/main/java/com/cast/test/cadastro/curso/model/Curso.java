package com.cast.test.cadastro.curso.model;

import com.cast.test.cadastro.curso.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Curso implements Serializable
{
    private String  codigoCurso;
    private String  descricaoAssunto;
    private String  dataInicio;
    private String  dataTermino;
    private String  qtAlunosPorTurma;
    private String  categoria;

    public void salvarCurso()
    {
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference cursosRef = database.child("cursos");
        String codigoCurso = cursosRef.push().getKey();
        this.codigoCurso = codigoCurso;
        cursosRef.child(codigoCurso).setValue(this);
    }

    public void atualizarCurso()
    {
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference cursosRef = database.child("cursos");

        cursosRef.child(codigoCurso).updateChildren(converterParaMap());
    }

    public void remove()
    {
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference cursosRef = database.child("cursos");
        cursosRef.child(codigoCurso).removeValue();
    }

    public Map<String, Object> converterParaMap(){
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("codigoCurso", codigoCurso);
        usuarioMap.put("descricaoAssunto", descricaoAssunto);
        usuarioMap.put("dataInicio", dataInicio);
        usuarioMap.put("dataTermino", dataTermino);
        usuarioMap.put("qtAlunosPorTurma", qtAlunosPorTurma);
        usuarioMap.put("categoria", categoria);

        return usuarioMap;
    }

}
