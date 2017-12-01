package com.example.rodrigo.at_monetizacao_rodrigo_filomeno;

import java.io.Serializable;

/**
 * Created by Rodrigo on 01/12/2017.
 */

public class Contato  implements Serializable {

    String Nome;
    String Email;
    String Senha;
    String ConfirmarSenha;
    String Cpf;

    public Contato (String nome, String email, String senha, String confirmarSenha, String cpf){

        Nome = nome;
        Email = email;
        Senha = senha;
        ConfirmarSenha = confirmarSenha;
        Cpf = cpf;

    }

}
