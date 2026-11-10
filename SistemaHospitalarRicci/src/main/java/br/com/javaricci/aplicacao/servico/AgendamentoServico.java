package br.com.javaricci.aplicacao.servico;

import br.com.javaricci.aplicacao.configuracao.AgendaPropriedades;
import br.com.javaricci.dominio.excecao.RecursoNaoEncontradoException;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import br.com.javaricci.dominio.modelo.Consulta;
import br.com.javaricci.dominio.modelo.ConsultaDetalhada;
import br.com.javaricci.dominio.modelo.EntradaListaEspera;
import br.com.javaricci.dominio.modelo.Medico;
import br.com.javaricci.dominio.modelo.Paciente;
import br.com.javaricci.dominio.repositorio.ConsultaRepositorio;
import br.com.javaricci.dominio.repositorio.ListaEsperaRepositorio;
import br.com.javaricci.dominio.repositorio.MedicoRepositorio;
import br.com.javaricci.dominio.repositorio.PacienteRepositorio;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso: agendamento e cancelamento de consultas, com lista de espera por médico e data. */
@Service
public class AgendamentoServico {

    /** Resultado de uma solicitação de agendamento. */
    public enum ResultadoAgendamento { AGENDADA, LISTA_DE_ESPERA }

    private final ConsultaRepositorio consultaRepositorio;
    private final ListaEsperaRepositorio listaEsperaRepositorio;
    private final PacienteRepositorio pacienteRepositorio;
    private final MedicoRepositorio medicoRepositorio;
    private final AgendaPropriedades agenda;

    public AgendamentoServico(ConsultaRepositorio consultaRepositorio, ListaEsperaRepositorio listaEsperaRepositorio,
                              PacienteRepositorio pacienteRepositorio, MedicoRepositorio medicoRepositorio,
                              AgendaPropriedades agenda) {
        this.consultaRepositorio = consultaRepositorio;
        this.listaEsperaRepositorio = listaEsperaRepositorio;
        this.pacienteRepositorio = pacienteRepositorio;
        this.medicoRepositorio = medicoRepositorio;
        this.agenda = agenda;
    }

    /** Agenda a consulta ou, se a agenda do médico estiver lotada na data, coloca o paciente na lista de espera. */
    @Transactional
    public ResultadoAgendamento agendar(String cpfPaciente, String crmMedico, LocalDate data) {
        if (data.isBefore(LocalDate.now())) {
            throw new RegraNegocioException("Escolha uma data a partir de hoje.");
        }
        Paciente paciente = pacienteRepositorio.buscarPorCpf(cpfPaciente)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado."));
        Medico medico = medicoRepositorio.buscarPorCrm(crmMedico)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Médico não encontrado."));
        if (!medico.aceitaPaciente(paciente)) {
            throw new RegraNegocioException("Este médico não atende o plano de saúde do paciente.");
        }

        if (agendaLotada(crmMedico, data)) {
            listaEsperaRepositorio.inserir(new EntradaListaEspera(null, crmMedico, cpfPaciente, data));
            return ResultadoAgendamento.LISTA_DE_ESPERA;
        }
        consultaRepositorio.inserir(Consulta.agendar(crmMedico, cpfPaciente, data));
        return ResultadoAgendamento.AGENDADA;
    }

    /**
     * Cancela a consulta do paciente. Se houver alguém na lista de espera do mesmo médico e data,
     * a vaga é repassada ao primeiro da fila; caso contrário, a consulta é removida.
     */
    @Transactional
    public void cancelar(String cpfPaciente, Long idConsulta) {
        Consulta consulta = consultaRepositorio.buscarPorId(idConsulta)
                .filter(encontrada -> encontrada.pertenceAoPaciente(cpfPaciente) && !encontrada.isRealizada())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada ou já realizada."));

        Optional<EntradaListaEspera> proximoDaFila = listaEsperaRepositorio
                .buscarPrimeiroDaFila(consulta.getCrmMedico(), consulta.getDataConsulta());

        if (proximoDaFila.isPresent()) {
            repassarVaga(consulta, proximoDaFila.get());
        } else {
            consultaRepositorio.excluir(consulta.getId());
        }
    }

    public List<ConsultaDetalhada> listarPendentes(String cpfPaciente) {
        return consultaRepositorio.listarPendentesDoPaciente(cpfPaciente);
    }

    private boolean agendaLotada(String crmMedico, LocalDate data) {
        return consultaRepositorio.contarPendentesDoMedicoNaData(crmMedico, data) >= agenda.limiteDiarioPorMedico();
    }

    private void repassarVaga(Consulta consulta, EntradaListaEspera entrada) {
        consulta.setCpfPaciente(entrada.getCpfPaciente());
        consultaRepositorio.atualizar(consulta);
        listaEsperaRepositorio.excluir(entrada.getId());
    }
}
