package br.com.javaricci.apresentacao.web.controlador;

import br.com.javaricci.aplicacao.seguranca.UsuarioLogado;
import br.com.javaricci.aplicacao.servico.MedicoServico;
import br.com.javaricci.apresentacao.web.formulario.MedicoFormulario;
import br.com.javaricci.apresentacao.web.formulario.PaginaFormulario;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import br.com.javaricci.dominio.modelo.Medico;
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

/** Menu do médico e edição dos seus próprios dados. */
@Controller
@RequestMapping("/medico")
public class MedicoPerfilController {

    private static final String VISAO_FORMULARIO = "medico/formulario";

    private final MedicoServico medicoServico;

    public MedicoPerfilController(MedicoServico medicoServico) {
        this.medicoServico = medicoServico;
    }

    @GetMapping("/menu")
    public String exibirMenu() {
        return "medico/menu";
    }

    @GetMapping("/dados")
    public String exibirEdicao(@SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario, Model model) {
        Medico medico = medicoServico.buscar(usuario.getIdentificador());
        model.addAttribute("formulario", MedicoFormulario.de(medico));
        model.addAttribute("pagina", paginaEdicao());
        return VISAO_FORMULARIO;
    }

    @PostMapping("/dados")
    public String atualizar(@Valid @ModelAttribute("formulario") MedicoFormulario formulario,
                            BindingResult resultado,
                            @SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario,
                            HttpSession sessao, Model model, RedirectAttributes redirecionamento) {
        if (!resultado.hasErrors()) {
            try {
                Medico atualizado = medicoServico.atualizar(usuario.getIdentificador(), formulario.paraComando());
                sessao.setAttribute(UsuarioLogado.ATRIBUTO_SESSAO, UsuarioLogado.doMedico(atualizado));
                redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_SUCESSO, "Dados atualizados com sucesso!");
                return "redirect:/medico/dados";
            } catch (RegraNegocioException e) {
                resultado.reject("edicao.invalida", e.getMessage());
            }
        }
        model.addAttribute("pagina", paginaEdicao());
        return VISAO_FORMULARIO;
    }

    private PaginaFormulario paginaEdicao() {
        return new PaginaFormulario("Meus dados", "/medico/dados", "/medico/menu", true);
    }
}
