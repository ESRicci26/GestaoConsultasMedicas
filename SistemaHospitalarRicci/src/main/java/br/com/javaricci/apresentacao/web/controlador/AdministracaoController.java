package br.com.javaricci.apresentacao.web.controlador;

import br.com.javaricci.aplicacao.servico.MedicoServico;
import br.com.javaricci.aplicacao.servico.PacienteServico;
import br.com.javaricci.apresentacao.web.formulario.MedicoFormulario;
import br.com.javaricci.apresentacao.web.formulario.PacienteFormulario;
import br.com.javaricci.apresentacao.web.formulario.PaginaFormulario;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Área do administrador: lista, cadastra e exclui pacientes e médicos. */
@Controller
@RequestMapping("/admin")
public class AdministracaoController {

    private static final String VISAO_FORMULARIO_PACIENTE = "paciente/formulario";
    private static final String VISAO_FORMULARIO_MEDICO = "medico/formulario";

    private final PacienteServico pacienteServico;
    private final MedicoServico medicoServico;

    public AdministracaoController(PacienteServico pacienteServico, MedicoServico medicoServico) {
        this.pacienteServico = pacienteServico;
        this.medicoServico = medicoServico;
    }

    @GetMapping("/menu")
    public String exibirMenu() {
        return "admin/menu";
    }

    // ---------------- Pacientes ----------------

    @GetMapping("/pacientes")
    public String listarPacientes(Model model) {
        model.addAttribute("pacientes", pacienteServico.listar());
        return "admin/pacientes";
    }

    @GetMapping("/pacientes/novo")
    public String exibirNovoPaciente(Model model) {
        model.addAttribute("formulario", new PacienteFormulario());
        model.addAttribute("pagina", paginaNovoPaciente());
        return VISAO_FORMULARIO_PACIENTE;
    }

    @PostMapping("/pacientes")
    public String cadastrarPaciente(@Valid @ModelAttribute("formulario") PacienteFormulario formulario,
                                    BindingResult resultado, Model model, RedirectAttributes redirecionamento) {
        if (!resultado.hasErrors()) {
            try {
                pacienteServico.cadastrar(formulario.paraComando());
                redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_SUCESSO,
                        "Cadastro de paciente realizado com sucesso!");
                return "redirect:/admin/pacientes";
            } catch (RegraNegocioException e) {
                resultado.reject("cadastro.invalido", e.getMessage());
            }
        }
        model.addAttribute("pagina", paginaNovoPaciente());
        return VISAO_FORMULARIO_PACIENTE;
    }

    @PostMapping("/pacientes/{cpf}/excluir")
    public String excluirPaciente(@PathVariable String cpf, RedirectAttributes redirecionamento) {
        try {
            pacienteServico.excluir(cpf);
            redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_SUCESSO, "Paciente excluído com sucesso!");
        } catch (RegraNegocioException e) {
            redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_ERRO, e.getMessage());
        }
        return "redirect:/admin/pacientes";
    }

    // ---------------- Médicos ----------------

    @GetMapping("/medicos")
    public String listarMedicos(Model model) {
        model.addAttribute("medicos", medicoServico.listar());
        return "admin/medicos";
    }

    @GetMapping("/medicos/novo")
    public String exibirNovoMedico(Model model) {
        model.addAttribute("formulario", new MedicoFormulario());
        model.addAttribute("pagina", paginaNovoMedico());
        return VISAO_FORMULARIO_MEDICO;
    }

    @PostMapping("/medicos")
    public String cadastrarMedico(@Valid @ModelAttribute("formulario") MedicoFormulario formulario,
                                  BindingResult resultado, Model model, RedirectAttributes redirecionamento) {
        if (!resultado.hasErrors()) {
            try {
                medicoServico.cadastrar(formulario.paraComando());
                redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_SUCESSO,
                        "Cadastro de médico realizado com sucesso!");
                return "redirect:/admin/medicos";
            } catch (RegraNegocioException e) {
                resultado.reject("cadastro.invalido", e.getMessage());
            }
        }
        model.addAttribute("pagina", paginaNovoMedico());
        return VISAO_FORMULARIO_MEDICO;
    }

    @PostMapping("/medicos/{crm}/excluir")
    public String excluirMedico(@PathVariable String crm, RedirectAttributes redirecionamento) {
        try {
            medicoServico.excluir(crm);
            redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_SUCESSO, "Médico excluído com sucesso!");
        } catch (RegraNegocioException e) {
            redirecionamento.addFlashAttribute(AtributosModelo.MENSAGEM_ERRO, e.getMessage());
        }
        return "redirect:/admin/medicos";
    }

    private PaginaFormulario paginaNovoPaciente() {
        return new PaginaFormulario("Novo paciente", "/admin/pacientes", "/admin/pacientes", false);
    }

    private PaginaFormulario paginaNovoMedico() {
        return new PaginaFormulario("Novo médico", "/admin/medicos", "/admin/medicos", false);
    }
}
