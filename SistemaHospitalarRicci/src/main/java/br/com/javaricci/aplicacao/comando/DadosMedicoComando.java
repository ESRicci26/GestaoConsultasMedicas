package br.com.javaricci.aplicacao.comando;

/** Dados necessários para cadastrar ou atualizar um médico. Senha em branco mantém a atual na atualização. */
public record DadosMedicoComando(String crm, String nome, String especialidade, String planoAtendido, String senha) {
}
