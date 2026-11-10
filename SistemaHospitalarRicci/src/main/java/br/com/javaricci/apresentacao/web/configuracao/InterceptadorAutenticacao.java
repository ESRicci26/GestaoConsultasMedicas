package br.com.javaricci.apresentacao.web.configuracao;

import br.com.javaricci.aplicacao.seguranca.UsuarioLogado;
import br.com.javaricci.dominio.modelo.TipoUsuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.web.servlet.HandlerInterceptor;

/** Garante que cada área (/paciente, /medico, /admin) só seja acessada pelo perfil correspondente. */
public class InterceptadorAutenticacao implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest requisicao, HttpServletResponse resposta, Object manipulador)
            throws IOException {
        String caminho = requisicao.getRequestURI().substring(requisicao.getContextPath().length());
        TipoUsuario perfilExigido = TipoUsuario.doCaminho(caminho);
        UsuarioLogado usuario = obterUsuario(requisicao);

        if (usuario == null || usuario.getTipo() != perfilExigido) {
            resposta.sendRedirect(requisicao.getContextPath() + perfilExigido.getCaminhoBase() + "/login");
            return false;
        }
        return true;
    }

    private UsuarioLogado obterUsuario(HttpServletRequest requisicao) {
        HttpSession sessao = requisicao.getSession(false);
        return sessao == null ? null : (UsuarioLogado) sessao.getAttribute(UsuarioLogado.ATRIBUTO_SESSAO);
    }
}
