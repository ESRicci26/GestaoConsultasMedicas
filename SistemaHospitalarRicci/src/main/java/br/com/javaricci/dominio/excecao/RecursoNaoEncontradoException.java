package br.com.javaricci.dominio.excecao;

/** Registro solicitado não existe (ou não pertence ao usuário autenticado). */
public class RecursoNaoEncontradoException extends RegraNegocioException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
