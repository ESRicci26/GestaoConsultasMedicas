package br.com.javaricci.apresentacao.web.controlador;

import br.com.javaricci.aplicacao.seguranca.UsuarioLogado;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Disponibiliza o usuário autenticado (se houver) a todos os templates, para montar a barra de navegação. */
@ControllerAdvice
public class AtributosGlobais {

    @ModelAttribute("usuarioAtual")
    public UsuarioLogado usuarioAtual(HttpServletRequest requisicao) {
        HttpSession sessao = requisicao.getSession(false);
        return sessao == null ? null : (UsuarioLogado) sessao.getAttribute(UsuarioLogado.ATRIBUTO_SESSAO);
    }
}
