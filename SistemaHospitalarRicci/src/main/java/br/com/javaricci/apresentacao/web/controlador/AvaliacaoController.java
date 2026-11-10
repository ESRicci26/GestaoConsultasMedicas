package br.com.javaricci.apresentacao.web.controlador;

import br.com.javaricci.aplicacao.seguranca.UsuarioLogado;
import br.com.javaricci.aplicacao.servico.AvaliacaoServico;
import br.com.javaricci.apresentacao.web.formulario.AvaliacaoFormulario;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import jakarta.validation.Valid;
import java.util.List;
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

/** Avaliação, pelo paciente, das consultas já realizadas. */
@Controller
@RequestMapping("/paciente/avaliacoes")
public class AvaliacaoController {

    private static final List<String> ROTULOS_ESTRELAS = List.of("Ruim", "Regular", "Bom", "Muito bom", "Perfeito");

    private final AvaliacaoServico avaliacaoServico;

    public AvaliacaoController(AvaliacaoServico avaliacaoServico) {
        this.avaliacaoServico = avaliacaoServico;
    }

    @GetMapping
    public String listarPendentes(@SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario,
                                  Model model) {
        model.addAttribute("consultas", avaliacaoServico.listarPendentes(usuario.getIdentificador()));
        return "paciente/avaliacoes";
    }

    @GetMapping("/{id}")
    public String exibirAvaliacao(@PathVariable Long id,
                                  @SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario,
                                  Model model) {
        model.addAttribute("formulario", new AvaliacaoFormulario());
        return montarPaginaAvaliacao(id, usuario, model);
    }

    @PostMapping("/{id}")
    public String avaliar(@PathVariable Long id,
                          @Valid @ModelAttribute("formulario") AvaliacaoFormulario formulario,
                          BindingResult resultado,
                          @SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario,
                          Model model, RedirectAttributes redirecionamento) {
        if (!resultado.hasErrors()) {
            try {
                avaliacaoServico.avaliar(usuario.getIdentificador(), id, formulario.getEstrelas(), formulario.getTexto());
                redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_SUCESSO,
                        "Avaliação enviada com sucesso! Obrigado :)");
                return "redirect:/paciente/avaliacoes";
            } catch (RegraNegocioException e) {
                resultado.reject("avaliacao.invalida", e.getMessage());
            }
        }
        return montarPaginaAvaliacao(id, usuario, model);
    }

    private String montarPaginaAvaliacao(Long id, UsuarioLogado usuario, Model model) {
        model.addAttribute("consulta", avaliacaoServico.buscarPendente(usuario.getIdentificador(), id));
        model.addAttribute("rotulosEstrelas", ROTULOS_ESTRELAS);
        return "paciente/avaliar";
    }
}
