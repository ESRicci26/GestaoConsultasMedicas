package br.com.javaricci.aplicacao.servico;

import br.com.javaricci.dominio.excecao.RegraNegocioException;
import br.com.javaricci.dominio.modelo.ConsultaDetalhada;
import br.com.javaricci.dominio.modelo.TipoRelatorio;
import br.com.javaricci.dominio.repositorio.ConsultaRepositorio;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

/** Caso de uso: relatórios de consultas e agendamentos de pacientes e médicos por período. */
@Service
public class RelatorioServico {

    private final ConsultaRepositorio consultaRepositorio;

    public RelatorioServico(ConsultaRepositorio consultaRepositorio) {
        this.consultaRepositorio = consultaRepositorio;
    }

    public List<ConsultaDetalhada> gerarParaPaciente(String cpfPaciente, TipoRelatorio tipo,
                                                     LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        return consultaRepositorio.listarDoPacienteNoPeriodo(cpfPaciente, tipo.isRealizada(), inicio, fim);
    }

    public List<ConsultaDetalhada> gerarParaMedico(String crmMedico, TipoRelatorio tipo,
                                                   LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        return consultaRepositorio.listarDoMedicoNoPeriodo(crmMedico, tipo.isRealizada(), inicio, fim);
    }

    private void validarPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio.isAfter(fim)) {
            throw new RegraNegocioException("A data inicial não pode ser posterior à data final.");
        }
    }
}
