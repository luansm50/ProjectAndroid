package com.castgroup.projeto.lm.cadastrodecursos.config;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.castgroup.projeto.lm.cadastrodecursos.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase
{
    public static FirebaseUser getUsuarioAtual()
    {
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public static String getIdentificadorUsuario()
    {
        return getUsuarioAtual().getUid();
    }

    public static Usuario getDadosUsuarioLogado()
    {
        FirebaseUser usuarioLogado = getUsuarioAtual();

        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioLogado.getEmail());
        usuario.setNome(usuarioLogado.getDisplayName());
        usuario.setId(usuarioLogado.getUid());
        return usuario;
    }

}
