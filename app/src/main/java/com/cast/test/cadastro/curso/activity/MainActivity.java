package com.cast.test.cadastro.curso.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cast.test.cadastro.curso.R;
import com.cast.test.cadastro.curso.config.ConfiguracaoFirebase;
import com.cast.test.cadastro.curso.config.Permissao;
import com.cast.test.cadastro.curso.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private Retrofit retrofit;

    private EditText    campoEmail;
    private EditText    campoSenha;
    private Button      botaoLogin;
    private ProgressBar progressBar;

    private Usuario usuario;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Validar Permissoes
        Permissao.validarPermissoes(this, permissoesNecessarias, 1);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        inicializarComponentes();

    }

    public void inicializarComponentes()
    {
        campoEmail      = findViewById(R.id.editLoginEmail);
        campoSenha      = findViewById(R.id.editLoginSenha);
        botaoLogin      = findViewById(R.id.buttonEntrar);
        progressBar     = findViewById(R.id.progressLogin);

        campoEmail.requestFocus();

        usuario = new Usuario();
        usuario.setEmail("castgroup@test.com.br");
        usuario.setSenha("12345678");
        validarLogin(usuario);
    }

    public void onClickBotaoEntrar(View view)
    {
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();


        usuario = new Usuario();
        usuario.setEmail(textoEmail);
        usuario.setSenha(textoSenha);
        validarLogin(usuario);
    }

    public void validarLogin(Usuario usuario)
    {
        progressBar.setVisibility(View.VISIBLE);
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                Intent i = new Intent(getApplicationContext(), PrincipalActivity.class);
                startActivity(i);
                finish();
            }
        });
    }



    public void verificarUsuarioLogado() {
        if (autenticacao.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}