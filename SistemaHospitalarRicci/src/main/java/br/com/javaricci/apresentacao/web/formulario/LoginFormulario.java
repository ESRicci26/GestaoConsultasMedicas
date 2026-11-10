package br.com.javaricci.apresentacao.web.formulario;

import jakarta.validation.constraints.NotBlank;

/** Dados da tela de login (usuário é o CPF, o CRM ou o login do administrador). */
public class LoginFormulario {

    @NotBlank(message = "Informe o usuário.")
    private String usuario;

    @NotBlank(message = "Informe a senha.")
    private String senha;

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
