package br.com.javaricci.apresentacao.web.controlador;

import br.com.javaricci.aplicacao.seguranca.UsuarioLogado;
import br.com.javaricci.aplicacao.servico.BuscaMedicoServico;
import br.com.javaricci.aplicacao.servico.PacienteServico;
import br.com.javaricci.apresentacao.web.formulario.BuscaMedicoFormulario;
import br.com.javaricci.dominio.modelo.CampoBuscaMedico;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

/** Pesquisa de médicos por nome ou especialidade, com nota média e comentários. */
@Controller
public class BuscaMedicoController {

    private final BuscaMedicoServico buscaMedicoServico;
    private final PacienteServico pacienteServico;

    public BuscaMedicoController(BuscaMedicoServico buscaMedicoServico, PacienteServico pacienteServico) {
        this.buscaMedicoServico = buscaMedicoServico;
        this.pacienteServico = pacienteServico;
    }

    @GetMapping("/paciente/medicos/buscar")
    public String buscar(@ModelAttribute("formulario") BuscaMedicoFormulario formulario,
                         @SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario, Model model) {
        model.addAttribute("campos", CampoBuscaMedico.values());
        if (formulario.getTermo() != null) {
            var paciente = pacienteServico.buscar(usuario.getIdentificador());
            model.addAttribute("resultados",
                    buscaMedicoServico.pesquisar(formulario.getCampo(), formulario.getTermo(), paciente));
        }
        return "paciente/buscar-medicos";
    }
}
