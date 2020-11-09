package com.cast.test.cadastro.curso.config;

import com.cast.test.cadastro.curso.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
