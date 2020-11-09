package com.castgroup.projeto.lm.cadastrodecursos.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.castgroup.projeto.lm.cadastrodecursos.R;
import com.castgroup.projeto.lm.cadastrodecursos.adapter.AdapterCurso;
import com.castgroup.projeto.lm.cadastrodecursos.config.ConfiguracaoFirebase;
import com.castgroup.projeto.lm.cadastrodecursos.model.Curso;
import com.castgroup.projeto.lm.cadastrodecursos.utils.RecyclerItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private RecyclerView recyclerCurso;
    private AdapterCurso adapterCurso;
    private SearchView searchViewPesquisa;

    private ValueEventListener valueEventListenerCourso;
    private DatabaseReference cursoRef;

    private List<Curso> listaCoursos = new ArrayList<Curso>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        cursoRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("cursos");

        //Inicializar componentes
        recyclerCurso = findViewById(R.id.recyclerCourso);
        swipe();

        //Configura recyclerview
        adapterCurso = new AdapterCurso(listaCoursos, getApplicationContext());
        recyclerCurso.setHasFixedSize(true);
        recyclerCurso.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerCurso.setAdapter(adapterCurso);

        recyclerCurso.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerCurso,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Log.i("TAG", "entrei");

                                List<Curso> listaCurso = adapterCurso.getCursoList();
                                Curso cursoSelecionado = listaCurso.get(position);

                                Intent intent = new Intent(getApplicationContext(), CadastroCursoActivity.class);
                                intent.putExtra("curso", cursoSelecionado);
                                startActivity(intent);
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

        //Configura searchView
        searchViewPesquisa = findViewById(R.id.searchViewPesquisa);
        searchViewPesquisa.setQueryHint("Buscar cursos");
        searchViewPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String textoDigitado = newText.toUpperCase();
                pesquisarCurso(textoDigitado);
                return true;
            }
        });


    }

    public void swipe() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PrincipalActivity.this);
                alertDialog.setTitle("Excluir Curso");
                alertDialog.setTitle("Voce tem certeza que deseja realmente excluir o curso?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<Curso> listaCurso = adapterCurso.getCursoList();
                        Curso cursoSelecionado = listaCurso.get(viewHolder.getAdapterPosition());
                        cursoSelecionado.remove();
                    }
                });

                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerCurso);
    }

    @Override
    public void onStart() {
        super.onStart();
        listingCourses();
    }

    @Override
    public void onResume() {
        super.onResume();
        listingCourses();
    }

    private void listingCourses()
    {
        valueEventListenerCourso = cursoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCoursos.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    listaCoursos.add(ds.getValue(Curso.class));
                }

                adapterCurso.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        cursoRef.removeEventListener(valueEventListenerCourso);
    }

    public void adicionarNovoCurso(View view)
    {
        Intent intent = new Intent(this, CadastroCursoActivity.class);
        startActivity(intent);
    }

    public void pesquisarCurso(String textoPesquisa)
    {

        cursoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCoursos.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Curso curso = ds.getValue(Curso.class);
                    if(textoPesquisa.isEmpty())
                    {
                        listaCoursos.add(curso);
                    }
                    else
                    {
                        String texto = curso.getDescricaoAssunto();
                        if(texto.toUpperCase().contains(textoPesquisa.toUpperCase()))
                        {
                            listaCoursos.add(curso);
                        }
                    }

                }
                adapterCurso.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}