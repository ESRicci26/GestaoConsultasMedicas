package br.com.javaricci.aplicacao.servico;

import br.com.javaricci.aplicacao.comando.DadosAtendimentoComando;
import br.com.javaricci.dominio.excecao.RecursoNaoEncontradoException;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import br.com.javaricci.dominio.modelo.Consulta;
import br.com.javaricci.dominio.modelo.ConsultaDetalhada;
import br.com.javaricci.dominio.modelo.ConsultaRealizada;
import br.com.javaricci.dominio.modelo.Paciente;
import br.com.javaricci.dominio.repositorio.ConsultaRealizadaRepositorio;
import br.com.javaricci.dominio.repositorio.ConsultaRepositorio;
import br.com.javaricci.dominio.repositorio.PacienteRepositorio;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso: o médico registra a realização de uma consulta agendada. */
@Service
public class AtendimentoServico {

    private final ConsultaRepositorio consultaRepositorio;
    private final ConsultaRealizadaRepositorio consultaRealizadaRepositorio;
    private final PacienteRepositorio pacienteRepositorio;

    public AtendimentoServico(ConsultaRepositorio consultaRepositorio,
                              ConsultaRealizadaRepositorio consultaRealizadaRepositorio,
                              PacienteRepositorio pacienteRepositorio) {
        this.consultaRepositorio = consultaRepositorio;
        this.consultaRealizadaRepositorio = consultaRealizadaRepositorio;
        this.pacienteRepositorio = pacienteRepositorio;
    }

    public List<ConsultaDetalhada> listarPendentes(String crmMedico) {
        return consultaRepositorio.listarPendentesDoMedico(crmMedico);
    }

    public ConsultaDetalhada buscarPendente(String crmMedico, Long idConsulta) {
        return listarPendentes(crmMedico).stream()
                .filter(consulta -> consulta.getId().equals(idConsulta))
                .findFirst()
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada ou já realizada."));
    }

    /**
     * Grava o registro clínico e marca a consulta como realizada na mesma transação.
     * Pacientes com plano não pagam; os demais têm o valor informado pelo médico.
     */
    @Transactional
    public void realizar(String crmMedico, Long idConsulta, DadosAtendimentoComando dados) {
        Consulta consulta = consultaRepositorio.buscarPorId(idConsulta)
                .filter(encontrada -> encontrada.pertenceAoMedico(crmMedico) && !encontrada.isRealizada())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada ou já realizada."));
        Paciente paciente = pacienteRepositorio.buscarPorCpf(consulta.getCpfPaciente())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado."));

        ConsultaRealizada realizada = new ConsultaRealizada(consulta.getId(), crmMedico, paciente.getCpf(),
                consulta.getDataConsulta(), calcularValor(paciente, dados), dados.sintomas().trim(),
                dados.tratamento().trim(), textoOuVazio(dados.exames()), textoOuVazio(dados.medicamentos()), "", 0);
        consultaRealizadaRepositorio.inserir(realizada);

        consulta.setRealizada(true);
        consultaRepositorio.atualizar(consulta);
    }

    private BigDecimal calcularValor(Paciente paciente, DadosAtendimentoComando dados) {
        if (paciente.possuiPlano()) {
            return BigDecimal.ZERO.setScale(2);
        }
        if (dados.valor() == null) {
            throw new RegraNegocioException("Informe o valor a pagar: o paciente não possui plano de saúde.");
        }
        return dados.valor().setScale(2, RoundingMode.HALF_UP);
    }

    private String textoOuVazio(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
