package br.com.javaricci.apresentacao.web.controlador;

import br.com.javaricci.aplicacao.servico.MedicoServico;
import br.com.javaricci.aplicacao.servico.PacienteServico;
import br.com.javaricci.apresentacao.web.formulario.MedicoFormulario;
import br.com.javaricci.apresentacao.web.formulario.PaginaFormulario;
import br.com.javaricci.apresentacao.web.formulario.PacienteFormulario;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Auto-cadastro público de pacientes e médicos (link "Cadastre-se" das telas de login). */
@Controller
@RequestMapping("/cadastro")
public class CadastroController {

    private static final String VISAO_PACIENTE = "paciente/formulario";
    private static final String VISAO_MEDICO = "medico/formulario";

    private final PacienteServico pacienteServico;
    private final MedicoServico medicoServico;

    public CadastroController(PacienteServico pacienteServico, MedicoServico medicoServico) {
        this.pacienteServico = pacienteServico;
        this.medicoServico = medicoServico;
    }

    @GetMapping("/paciente")
    public String exibirCadastroPaciente(Model model) {
        model.addAttribute("formulario", new PacienteFormulario());
        model.addAttribute("pagina", paginaPaciente());
        return VISAO_PACIENTE;
    }

    @PostMapping("/paciente")
    public String cadastrarPaciente(@Valid @ModelAttribute("formulario") PacienteFormulario formulario,
                                    BindingResult resultado, Model model, RedirectAttributes redirecionamento) {
        if (!resultado.hasErrors()) {
            try {
                pacienteServico.cadastrar(formulario.paraComando());
                redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_SUCESSO,
                        "Cadastro de paciente realizado com sucesso! Faça seu login.");
                return "redirect:/paciente/login";
            } catch (RegraNegocioException e) {
                resultado.reject("cadastro.invalido", e.getMessage());
            }
        }
        model.addAttribute("pagina", paginaPaciente());
        return VISAO_PACIENTE;
    }

    @GetMapping("/medico")
    public String exibirCadastroMedico(Model model) {
        model.addAttribute("formulario", new MedicoFormulario());
        model.addAttribute("pagina", paginaMedico());
        return VISAO_MEDICO;
    }

    @PostMapping("/medico")
    public String cadastrarMedico(@Valid @ModelAttribute("formulario") MedicoFormulario formulario,
                                  BindingResult resultado, Model model, RedirectAttributes redirecionamento) {
        if (!resultado.hasErrors()) {
            try {
                medicoServico.cadastrar(formulario.paraComando());
                redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_SUCESSO,
                        "Cadastro de médico realizado com sucesso! Faça seu login.");
                return "redirect:/medico/login";
            } catch (RegraNegocioException e) {
                resultado.reject("cadastro.invalido", e.getMessage());
            }
        }
        model.addAttribute("pagina", paginaMedico());
        return VISAO_MEDICO;
    }

    private PaginaFormulario paginaPaciente() {
        return new PaginaFormulario("Cadastro de paciente", "/cadastro/paciente", "/paciente/login", false);
    }

    private PaginaFormulario paginaMedico() {
        return new PaginaFormulario("Cadastro de médico", "/cadastro/medico", "/medico/login", false);
    }
}
