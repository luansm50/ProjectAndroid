package com.castgroup.projeto.lm.cadastrodecursos.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.castgroup.projeto.lm.cadastrodecursos.R;
import com.castgroup.projeto.lm.cadastrodecursos.config.ConfiguracaoFirebase;
import com.castgroup.projeto.lm.cadastrodecursos.model.Categoria;
import com.castgroup.projeto.lm.cadastrodecursos.model.Curso;
import com.castgroup.projeto.lm.cadastrodecursos.utils.ValidateDate;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CadastroCursoActivity extends AppCompatActivity {

    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    private EditText editTextCursoDescricaoAssunto;
    private EditText editTextCursoDataInicio;
    private EditText editTextCursoDataTermino;
    private EditText editTextCursoQtAlunosTurma;
    private EditText editTextCursoCategoria;

    private TextView txtErroDescricaoAssunto;
    private TextView txtErroDataInicio;
    private TextView txtErroDataTermino;
    private TextView txtErroCategoria;

    private Curso curso;
    private Boolean atualizar;
    private Categoria categoria;

    private DatabaseReference database;
    private DatabaseReference cursoRef;
    private DatabaseReference categoriaRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_curso);
        inicializarComponentes();

        database = ConfiguracaoFirebase.getFirebaseDatabase();
        cursoRef = database.child("cursos");

        Bundle bundle = getIntent().getExtras();
        curso = new Curso();
        if(bundle != null)
        {
            Curso c = (Curso) bundle.get("curso");
            if(c != null)
            {
                atualizar = true;
                inicializarComponentesWithValues(c);
                curso = c;
            }
        }
    }

    private void inicializarComponentes()
    {
        editTextCursoDescricaoAssunto   = findViewById(R.id.editTextCursoDescricaoAssunto);
        editTextCursoDataInicio         = findViewById(R.id.editTextCursoDataInicio);
        editTextCursoDataTermino        = findViewById(R.id.editTextCursoDataTermino);
        editTextCursoQtAlunosTurma      = findViewById(R.id.editTextCursoQtAlunosTurma);
        editTextCursoCategoria          = findViewById(R.id.editTextCursoCategoria);
        txtErroDescricaoAssunto         = findViewById(R.id.txtErroDescricaoAssunto);
        txtErroDataInicio               = findViewById(R.id.txtErroDataInicio);
        txtErroDataTermino              = findViewById(R.id.txtErroDataTermino);
        txtErroCategoria                = findViewById(R.id.txtErroCategoria);

        txtErroDescricaoAssunto.setVisibility(View.GONE);
        txtErroDataInicio.setVisibility(View.GONE);
        txtErroDataTermino.setVisibility(View.GONE);
        txtErroCategoria.setVisibility(View.GONE);

        txtErroDataInicio.setText("*Data de inicio invalida");
        txtErroDataTermino.setText("*Data de termino invalida");

        SimpleMaskFormatter smfData = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwCursoDataInicio = new MaskTextWatcher(editTextCursoDataInicio, smfData);
        MaskTextWatcher mtwCursoDataTermino = new MaskTextWatcher(editTextCursoDataTermino, smfData);

        editTextCursoDataInicio.addTextChangedListener(mtwCursoDataInicio);
        editTextCursoDataTermino.addTextChangedListener(mtwCursoDataTermino);
    }

    private void inicializarComponentesWithValues(Curso curso)
    {
        editTextCursoDescricaoAssunto.setText(curso.getDescricaoAssunto());
        editTextCursoDataInicio.setText(curso.getDataInicio());
        editTextCursoDataTermino.setText(curso.getDataTermino());
        Log.i("TAG", curso.toString());
        editTextCursoQtAlunosTurma.setText(curso.getQtAlunosPorTurma());

        categoriaRef = database.child("categorias");
        categoriaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("TAG", snapshot.hasChildren() + "");
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    Categoria categoria = ds.getValue(Categoria.class);
                    Log.i("TAG", categoria.toString() + "");
                    if(categoria.getCodigo().equals(curso.getCategoria()))
                    {
                       editTextCursoCategoria.setText(categoria.getDescricao());
                       break;
                    }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void salvarAlterarCurso(View view)
        throws ParseException
    {
        txtErroDataInicio.setText("*Data de inicio invalida");
        txtErroDataTermino.setText("*Data de termino invalida");

        List<String> erros = new ArrayList<String>();
        erros.clear();

        Boolean datasOk = true;

        if(editTextCursoDescricaoAssunto.getText().toString().isEmpty())
        {
            txtErroDescricaoAssunto.setVisibility(View.VISIBLE);
            erros.add("Descricao do assunto do curso deve ser informada");
        }

        if(!ValidateDate.validateDate(editTextCursoDataInicio.getText().toString()))
        {
            txtErroDataInicio.setVisibility(View.VISIBLE);
            erros.add("Data de inicio invalida");
        }

        if(!ValidateDate.validateDate(editTextCursoDataTermino.getText().toString()))
        {
            txtErroDataTermino.setVisibility(View.VISIBLE);
            erros.add("Data de termino invalida");
            datasOk = false;
        }

        if(curso.getCategoria() == null)
        {
            txtErroCategoria.setVisibility(View.VISIBLE);
            erros.add("Voce deve selecionar uma categoria para este curso");
            datasOk = false;
        }

        if(datasOk)
        {
            String dataInicio = editTextCursoDataInicio.getText().toString();
            String dataTermino = editTextCursoDataTermino.getText().toString();

            if(!ValidateDate.compararDatas(dataInicio, dataTermino) && !dataInicio.equals(dataTermino))
            {
                txtErroDataInicio.setVisibility(View.VISIBLE);
                txtErroDataTermino.setVisibility(View.VISIBLE);

                txtErroDataInicio.setText("*Data de inicio deve ser inferior ou igual a data termino");
                txtErroDataTermino.setText("*Data de termino deve ser superior ou igual a data inicio");

                erros.add("Data de termino deve ser superior ou igual a data inicio");
            }
        }

        if(erros.size() > 1)
        {
            exibirDisplayAlert("Verificar campos em vermelho");
        }
        else if(erros.size() > 0)
        {
            exibirDisplayAlert(erros.get(0));
        }
        else
        {
            salvarCurso(curso);
        }
    }

    private void salvarCurso(Curso curso)
    {
        cursoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String dataInicio = editTextCursoDataInicio.getText().toString();
                String dataTermino = editTextCursoDataTermino.getText().toString();
                boolean dataOk = true;

                for(DataSnapshot ds : snapshot.getChildren())
                {
                    Curso curso1 = ds.getValue(Curso.class);
                    try {
                        boolean compareData = ValidateDate.compararDatas(curso1.getDataTermino(), dataInicio);
                        boolean compareData2 = ValidateDate.compararDatas(curso1.getDataInicio(), dataInicio);
                        if(!compareData && compareData2)
                        {
                            if(atualizar)
                            {
                                if(!curso.getCodigoCurso().equals(curso1.getCodigoCurso()))
                                {
                                    dataOk = false;
                                    break;
                                }
                            }
                            else
                            {
                                dataOk = false;
                                break;
                            }

                        }
                    } catch (ParseException e) {
                       dataOk = false;
                    }
                }

                if(dataOk) {
                    String qtAlunos = editTextCursoQtAlunosTurma.getText().toString();
                    String descricao = editTextCursoDescricaoAssunto.getText().toString();

                    curso.setDescricaoAssunto(descricao);
                    curso.setDataInicio(dataInicio);
                    curso.setDataTermino(dataTermino);

                    if (!qtAlunos.isEmpty()) {
                        curso.setQtAlunosPorTurma(qtAlunos);
                    }
                    if(atualizar)
                    {
                        curso.atualizarCurso();
                    }
                    else
                    {
                        curso.salvarCurso();
                    }

                    finish();
                }
                else
                {
                    exibirDisplayAlert("“Existe(m) curso(s) planejados(s) dentro do período informado.”.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void exibirDisplayAlert(String message)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(false);

        builder1.setPositiveButton(
            "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void buscarCategoria(View view)
    {
        Intent intent = new Intent(this, CategoriaActivity.class);
        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
    }

    // This method is called when the second activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                String descricao = data.getStringExtra("descricao");
                String id = data.getStringExtra("id");

                curso.setCategoria(id);
                editTextCursoCategoria.setText(descricao);
            }
        }
    }


}