package br.com.javaricci.apresentacao.web.controlador;

import br.com.javaricci.aplicacao.seguranca.UsuarioLogado;
import br.com.javaricci.aplicacao.servico.AgendamentoServico;
import br.com.javaricci.aplicacao.servico.AgendamentoServico.ResultadoAgendamento;
import br.com.javaricci.aplicacao.servico.MedicoServico;
import br.com.javaricci.aplicacao.servico.PacienteServico;
import br.com.javaricci.apresentacao.web.formulario.AgendamentoFormulario;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import jakarta.validation.Valid;
import java.time.LocalDate;
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

/** Agendamento e cancelamento de consultas pelo paciente. */
@Controller
@RequestMapping("/paciente/consultas")
public class AgendamentoController {

    private static final String URL_AGENDAR = "redirect:/paciente/consultas/agendar";
    private static final String URL_CANCELAR = "redirect:/paciente/consultas/cancelar";

    private final AgendamentoServico agendamentoServico;
    private final PacienteServico pacienteServico;
    private final MedicoServico medicoServico;

    public AgendamentoController(AgendamentoServico agendamentoServico, PacienteServico pacienteServico,
                                 MedicoServico medicoServico) {
        this.agendamentoServico = agendamentoServico;
        this.pacienteServico = pacienteServico;
        this.medicoServico = medicoServico;
    }

    @GetMapping("/agendar")
    public String exibirAgendamento(@SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario,
                                    Model model) {
        model.addAttribute("formulario", new AgendamentoFormulario());
        return montarPaginaAgendamento(usuario, model);
    }

    @PostMapping("/agendar")
    public String agendar(@Valid @ModelAttribute("formulario") AgendamentoFormulario formulario,
                          BindingResult resultado,
                          @SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario,
                          Model model, RedirectAttributes redirecionamento) {
        if (!resultado.hasErrors()) {
            try {
                ResultadoAgendamento agendamento = agendamentoServico.agendar(
                        usuario.getIdentificador(), formulario.getCrmMedico(), formulario.getDataConsulta());
                registrarMensagem(redirecionamento, agendamento);
                return URL_AGENDAR;
            } catch (RegraNegocioException e) {
                resultado.reject("agendamento.invalido", e.getMessage());
            }
        }
        return montarPaginaAgendamento(usuario, model);
    }

    @GetMapping("/cancelar")
    public String exibirCancelamento(@SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario,
                                     Model model) {
        model.addAttribute("consultas", agendamentoServico.listarPendentes(usuario.getIdentificador()));
        return "paciente/cancelar";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id,
                           @SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario,
                           RedirectAttributes redirecionamento) {
        try {
            agendamentoServico.cancelar(usuario.getIdentificador(), id);
            redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_SUCESSO, "Consulta desmarcada com sucesso!");
        } catch (RegraNegocioException e) {
            redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_ERRO, e.getMessage());
        }
        return URL_CANCELAR;
    }

    private String montarPaginaAgendamento(UsuarioLogado usuario, Model model) {
        var paciente = pacienteServico.buscar(usuario.getIdentificador());
        model.addAttribute("medicos", medicoServico.listarDisponiveisPara(paciente));
        model.addAttribute("hoje", LocalDate.now());
        return "paciente/agendar";
    }

    private void registrarMensagem(RedirectAttributes redirecionamento, ResultadoAgendamento agendamento) {
        if (agendamento == ResultadoAgendamento.LISTA_DE_ESPERA) {
            redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_AVISO,
                    "A agenda desse médico está lotada nessa data! Você foi adicionado à lista de espera.");
        } else {
            redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_SUCESSO, "Agendamento realizado com sucesso!");
        }
    }
}
