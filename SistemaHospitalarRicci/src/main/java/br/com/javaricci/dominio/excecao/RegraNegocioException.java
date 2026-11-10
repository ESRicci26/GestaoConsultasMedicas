package br.com.javaricci.dominio.excecao;

/** Violação de uma regra de negócio; a mensagem é segura para ser exibida ao usuário. */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
