package br.com.javaricci.apresentacao.web.controlador;

import br.com.javaricci.aplicacao.seguranca.UsuarioLogado;
import br.com.javaricci.aplicacao.servico.AutenticacaoServico;
import br.com.javaricci.apresentacao.web.formulario.LoginFormulario;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import br.com.javaricci.dominio.modelo.TipoUsuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.function.Supplier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/** Login e logout de pacientes, médicos e administrador. */
@Controller
public class LoginController {

    private static final String VISAO_LOGIN = "login";

    private final AutenticacaoServico autenticacaoServico;

    public LoginController(AutenticacaoServico autenticacaoServico) {
        this.autenticacaoServico = autenticacaoServico;
    }

    @GetMapping("/paciente/login")
    public String exibirLoginPaciente(Model model) {
        model.addAttribute("formulario", new LoginFormulario());
        return exibirLogin(model, TipoUsuario.PACIENTE, "/cadastro/paciente");
    }

    @PostMapping("/paciente/login")
    public String entrarComoPaciente(@Valid @ModelAttribute("formulario") LoginFormulario formulario,
                                     BindingResult resultado, HttpServletRequest requisicao, Model model) {
        return processarLogin(TipoUsuario.PACIENTE, "/cadastro/paciente", resultado, requisicao, model,
                () -> autenticacaoServico.autenticarPaciente(formulario.getUsuario(), formulario.getSenha()));
    }

    @GetMapping("/medico/login")
    public String exibirLoginMedico(Model model) {
        model.addAttribute("formulario", new LoginFormulario());
        return exibirLogin(model, TipoUsuario.MEDICO, "/cadastro/medico");
    }

    @PostMapping("/medico/login")
    public String entrarComoMedico(@Valid @ModelAttribute("formulario") LoginFormulario formulario,
                                   BindingResult resultado, HttpServletRequest requisicao, Model model) {
        return processarLogin(TipoUsuario.MEDICO, "/cadastro/medico", resultado, requisicao, model,
                () -> autenticacaoServico.autenticarMedico(formulario.getUsuario(), formulario.getSenha()));
    }

    @GetMapping("/admin/login")
    public String exibirLoginAdministrador(Model model) {
        model.addAttribute("formulario", new LoginFormulario());
        return exibirLogin(model, TipoUsuario.ADMINISTRADOR, null);
    }

    @PostMapping("/admin/login")
    public String entrarComoAdministrador(@Valid @ModelAttribute("formulario") LoginFormulario formulario,
                                          BindingResult resultado, HttpServletRequest requisicao, Model model) {
        return processarLogin(TipoUsuario.ADMINISTRADOR, null, resultado, requisicao, model,
                () -> autenticacaoServico.autenticarAdministrador(formulario.getUsuario(), formulario.getSenha()));
    }

    @PostMapping("/sair")
    public String sair(HttpSession sessao) {
        sessao.invalidate();
        return "redirect:/";
    }

    private String processarLogin(TipoUsuario tipo, String urlCadastro, BindingResult resultado,
                                  HttpServletRequest requisicao, Model model, Supplier<UsuarioLogado> autenticador) {
        if (!resultado.hasErrors()) {
            try {
                iniciarSessao(requisicao, autenticador.get());
                return "redirect:" + tipo.getCaminhoBase() + "/menu";
            } catch (RegraNegocioException e) {
                resultado.reject("login.invalido", e.getMessage());
            }
        }
        return exibirLogin(model, tipo, urlCadastro);
    }

    /** Troca o identificador da sessão ao autenticar, evitando fixação de sessão. */
    private void iniciarSessao(HttpServletRequest requisicao, UsuarioLogado usuario) {
        HttpSession sessao = requisicao.getSession(true);
        requisicao.changeSessionId();
        sessao.setAttribute(UsuarioLogado.ATRIBUTO_SESSAO, usuario);
    }

    private String exibirLogin(Model model, TipoUsuario tipo, String urlCadastro) {
        model.addAttribute("tipoUsuario", tipo);
        model.addAttribute("acao", tipo.getCaminhoBase() + "/login");
        model.addAttribute("urlCadastro", urlCadastro);
        return VISAO_LOGIN;
    }
}
