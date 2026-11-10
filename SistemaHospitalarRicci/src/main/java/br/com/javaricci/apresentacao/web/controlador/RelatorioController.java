package br.com.javaricci.apresentacao.web.controlador;

import br.com.javaricci.aplicacao.seguranca.UsuarioLogado;
import br.com.javaricci.aplicacao.servico.RelatorioServico;
import br.com.javaricci.apresentacao.web.formulario.RelatorioFormulario;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import br.com.javaricci.dominio.modelo.ConsultaDetalhada;
import br.com.javaricci.dominio.modelo.TipoRelatorio;
import br.com.javaricci.dominio.modelo.TipoUsuario;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

/** Relatórios de consultas e agendamentos por período, para pacientes e médicos. */
@Controller
public class RelatorioController {

    private static final String VISAO_RELATORIO = "relatorio/relatorio";

    private final RelatorioServico relatorioServico;

    public RelatorioController(RelatorioServico relatorioServico) {
        this.relatorioServico = relatorioServico;
    }

    @GetMapping("/paciente/relatorios")
    public String relatorioDoPaciente(@ModelAttribute("formulario") RelatorioFormulario formulario,
                                      @RequestParam(name = "gerar", required = false) String gerar,
                                      @SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario,
                                      Model model) {
        prepararPagina(model, TipoUsuario.PACIENTE);
        if (gerar != null) {
            gerarRelatorio(model, formulario, () -> relatorioServico.gerarParaPaciente(
                    usuario.getIdentificador(), formulario.getTipo(), formulario.getInicio(), formulario.getFim()));
        }
        return VISAO_RELATORIO;
    }

    @GetMapping("/medico/relatorios")
    public String relatorioDoMedico(@ModelAttribute("formulario") RelatorioFormulario formulario,
                                    @RequestParam(name = "gerar", required = false) String gerar,
                                    @SessionAttribute(UsuarioLogado.ATRIBUTO_SESSAO) UsuarioLogado usuario,
                                    Model model) {
        prepararPagina(model, TipoUsuario.MEDICO);
        if (gerar != null) {
            gerarRelatorio(model, formulario, () -> relatorioServico.gerarParaMedico(
                    usuario.getIdentificador(), formulario.getTipo(), formulario.getInicio(), formulario.getFim()));
        }
        return VISAO_RELATORIO;
    }

    private void prepararPagina(Model model, TipoUsuario perfil) {
        model.addAttribute("perfil", perfil);
        model.addAttribute("acao", perfil.getCaminhoBase() + "/relatorios");
        model.addAttribute("tipos", TipoRelatorio.values());
    }

    private void gerarRelatorio(Model model, RelatorioFormulario formulario, Supplier<List<ConsultaDetalhada>> consulta) {
        if (!formulario.estaCompleto()) {
            model.addAttribute(AtributosModelo.MENSAGEM_ERRO, "Informe o tipo de relatório e o período.");
            return;
        }
        try {
            model.addAttribute("resultados", consulta.get());
        } catch (RegraNegocioException e) {
            model.addAttribute(AtributosModelo.MENSAGEM_ERRO, e.getMessage());
        }
    }
}
