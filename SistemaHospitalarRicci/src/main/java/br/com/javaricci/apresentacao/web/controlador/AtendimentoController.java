package br.com.javaricci.apresentacao.web.controlador;

import br.com.javaricci.aplicacao.seguranca.UsuarioLogado;
import br.com.javaricci.aplicacao.servico.AtendimentoServico;
import br.com.javaricci.apresentacao.web.formulario.AtendimentoFormulario;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Realização de consultas pelo médico: escolha da consulta e registro do atendimento. */
@Controller
@RequestMapping("/medico/consultas")
public class AtendimentoController {

    private final AtendimentoServico atendimentoServico;

    public AtendimentoController(AtendimentoServico atendimentoServico) {
        this.atendimentoServico = atendimentoServico;
    }

    @GetMapping
    public String listarPendentes(@SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario,
                                  Model model) {
        model.addAttribute("consultas", atendimentoServico.listarPendentes(usuario.getIdentificador()));
        return "medico/consultas";
    }

    @GetMapping("/{id}/realizar")
    public String exibirAtendimento(@PathVariable Long id,
                                    @SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario,
                                    Model model) {
        model.addAttribute("formulario", new AtendimentoFormulario());
        return montarPaginaAtendimento(id, usuario, model);
    }

    @PostMapping("/{id}/realizar")
    public String realizar(@PathVariable Long id,
                           @Valid @ModelAttribute("formulario") AtendimentoFormulario formulario,
                           BindingResult resultado,
                           @SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario,
                           Model model, RedirectAttributes redirecionamento) {
        if (!resultado.hasErrors()) {
            try {
                atendimentoServico.realizar(usuario.getIdentificador(), id, formulario.paraComando());
                redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_SUCESSO,
                        "Consulta finalizada com sucesso!");
                return "redirect:/medico/consultas";
            } catch (RegraNegocioException e) {
                resultado.reject("atendimento.invalido", e.getMessage());
            }
        }
        return montarPaginaAtendimento(id, usuario, model);
    }

    private String montarPaginaAtendimento(Long id, UsuarioLogado usuario, Model model) {
        model.addAttribute("consulta", atendimentoServico.buscarPendente(usuario.getIdentificador(), id));
        return "medico/realizar";
    }
}
