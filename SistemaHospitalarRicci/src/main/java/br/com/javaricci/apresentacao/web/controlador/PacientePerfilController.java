package br.com.javaricci.apresentacao.web.controlador;

import br.com.javaricci.aplicacao.seguranca.UsuarioLogado;
import br.com.javaricci.aplicacao.servico.PacienteServico;
import br.com.javaricci.apresentacao.web.formulario.PacienteFormulario;
import br.com.javaricci.apresentacao.web.formulario.PaginaFormulario;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import br.com.javaricci.dominio.modelo.Paciente;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Menu do paciente e edição dos seus próprios dados. */
@Controller
@RequestMapping("/paciente")
public class PacientePerfilController {

    private static final String VISAO_FORMULARIO = "paciente/formulario";

    private final PacienteServico pacienteServico;

    public PacientePerfilController(PacienteServico pacienteServico) {
        this.pacienteServico = pacienteServico;
    }

    @GetMapping("/menu")
    public String exibirMenu() {
        return "paciente/menu";
    }

    @GetMapping("/dados")
    public String exibirEdicao(@SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario, Model model) {
        Paciente paciente = pacienteServico.buscar(usuario.getIdentificador());
        model.addAttribute("formulario", PacienteFormulario.de(paciente));
        model.addAttribute("pagina", paginaEdicao());
        return VISAO_FORMULARIO;
    }

    @PostMapping("/dados")
    public String atualizar(@Valid @ModelAttribute("formulario") PacienteFormulario formulario,
                            BindingResult resultado,
                            @SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario,
                            HttpSession sessao, Model model, RedirectAttributes redirecionamento) {
        if (!resultado.hasErrors()) {
            try {
                Paciente atualizado = pacienteServico.atualizar(usuario.getIdentificador(), formulario.paraComando());
                sessao.setAttribute(UsuarioLogado.ATRIBUTO_SESSAO, UsuarioLogado.doPaciente(atualizado));
                redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_SUCESSO, "Dados atualizados com sucesso!");
                return "redirect:/paciente/dados";
            } catch (RegraNegocioException e) {
                resultado.reject("edicao.invalida", e.getMessage());
            }
        }
        model.addAttribute("pagina", paginaEdicao());
        return VISAO_FORMULARIO;
    }

    private PaginaFormulario paginaEdicao() {
        return new PaginaFormulario("Meus dados", "/paciente/dados", "/paciente/menu", true);
    }
}
