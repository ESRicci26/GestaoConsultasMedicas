package br.com.javaricci.aplicacao.comando;

/** Dados necessários para cadastrar ou atualizar um paciente. Senha em branco mantém a atual na atualização. */
public record DadosPacienteComando(String cpf, String nome, int idade, String plano, String senha) {
}
