package com.castgroup.projeto.lm.cadastrodecursos.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.castgroup.projeto.lm.cadastrodecursos.R;
import com.castgroup.projeto.lm.cadastrodecursos.adapter.AdapterCategoria;
import com.castgroup.projeto.lm.cadastrodecursos.config.ConfiguracaoFirebase;
import com.castgroup.projeto.lm.cadastrodecursos.model.Categoria;
import com.castgroup.projeto.lm.cadastrodecursos.utils.RecyclerItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoriaActivity extends AppCompatActivity {

    private RecyclerView recyclerCategoria;
    private AdapterCategoria adapterCategoria;

    private ValueEventListener valueEventListenerCategoria;
    private DatabaseReference categoriaRef;

    private List<Categoria> listaCategoria = new ArrayList<Categoria>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        categoriaRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("categorias");

        //Inicializar componentes
        recyclerCategoria = findViewById(R.id.recyclerCategoria);

        //Configura recyclerview
        adapterCategoria = new AdapterCategoria(listaCategoria, this);
        recyclerCategoria.setHasFixedSize(true);
        recyclerCategoria.setLayoutManager(new LinearLayoutManager(this));
        recyclerCategoria.setAdapter(adapterCategoria);

        recyclerCategoria.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerCategoria,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Log.i("TAG", "entrei");

                                List<Categoria> listaCategoria = adapterCategoria.getCategoriaList();
                                Categoria categoriaSelecionada = listaCategoria.get(position);

                                Intent intent = new Intent();
                                intent.putExtra("descricao", categoriaSelecionada.getDescricao());
                                intent.putExtra("id", categoriaSelecionada.getCodigo());
                                setResult(RESULT_OK, intent);
                                finish();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        listingCourses();
    }

    private void listingCourses()
    {
        valueEventListenerCategoria = categoriaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCategoria.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    listaCategoria.add(ds.getValue(Categoria.class));
                }

                adapterCategoria.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        categoriaRef.removeEventListener(valueEventListenerCategoria);
    }



}