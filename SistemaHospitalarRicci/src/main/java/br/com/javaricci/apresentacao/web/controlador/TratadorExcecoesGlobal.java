package br.com.javaricci.apresentacao.web.controlador;

import br.com.javaricci.dominio.excecao.RecursoNaoEncontradoException;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Converte exceções não tratadas pelos controllers em uma página de erro amigável. */
@ControllerAdvice
public class TratadorExcecoesGlobal {

    private static final Logger REGISTRO = LoggerFactory.getLogger(TratadorExcecoesGlobal.class);
    private static final String VISAO_ERRO = "error";

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String tratarNaoEncontrado(RecursoNaoEncontradoException excecao, Model model) {
        model.addAttribute("mensagem", excecao.getMessage());
        return VISAO_ERRO;
    }

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String tratarRegraNegocio(RegraNegocioException excecao, Model model) {
        model.addAttribute("mensagem", excecao.getMessage());
        return VISAO_ERRO;
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String tratarErroDeBanco(DataAccessException excecao, Model model) {
        REGISTRO.error("Falha ao acessar o banco de dados", excecao);
        model.addAttribute("mensagem", "Não foi possível acessar o banco de dados. Tente novamente mais tarde.");
        return VISAO_ERRO;
    }
}
