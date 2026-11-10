package br.com.javaricci.aplicacao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.javaricci.aplicacao.configuracao.AgendaPropriedades;
import br.com.javaricci.aplicacao.servico.AgendamentoServico;
import br.com.javaricci.aplicacao.servico.AgendamentoServico.ResultadoAgendamento;
import br.com.javaricci.dominio.modelo.Consulta;
import br.com.javaricci.dominio.modelo.EntradaListaEspera;
import br.com.javaricci.dominio.modelo.Medico;
import br.com.javaricci.dominio.modelo.Paciente;
import br.com.javaricci.dominio.repositorio.ConsultaRepositorio;
import br.com.javaricci.dominio.repositorio.ListaEsperaRepositorio;
import br.com.javaricci.dominio.repositorio.MedicoRepositorio;
import br.com.javaricci.dominio.repositorio.PacienteRepositorio;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgendamentoServicoTest {

    private static final String CPF = "11111111111";
    private static final String CRM = "123";

    private final LocalDate amanha = LocalDate.now().plusDays(1);

    private ConsultaRepositorio consultaRepositorio;
    private ListaEsperaRepositorio listaEsperaRepositorio;
    private AgendamentoServico servico;

    @BeforeEach
    void preparar() {
        consultaRepositorio = mock(ConsultaRepositorio.class);
        listaEsperaRepositorio = mock(ListaEsperaRepositorio.class);
        PacienteRepositorio pacienteRepositorio = mock(PacienteRepositorio.class);
        MedicoRepositorio medicoRepositorio = mock(MedicoRepositorio.class);

        when(pacienteRepositorio.buscarPorCpf(CPF))
                .thenReturn(Optional.of(new Paciente(CPF, "João", 40, null, "hash")));
        when(medicoRepositorio.buscarPorCrm(CRM))
                .thenReturn(Optional.of(new Medico(CRM, "Dra. Helena", "Cardiologia", "Unimed", "hash")));

        servico = new AgendamentoServico(consultaRepositorio, listaEsperaRepositorio, pacienteRepositorio,
                medicoRepositorio, new AgendaPropriedades(4));
    }

    @Test
    void agendaQuandoHaVaga() {
        when(consultaRepositorio.contarPendentesDoMedicoNaData(CRM, amanha)).thenReturn(3);

        assertEquals(ResultadoAgendamento.AGENDADA, servico.agendar(CPF, CRM, amanha));
        verify(consultaRepositorio).inserir(any(Consulta.class));
        verify(listaEsperaRepositorio, never()).inserir(any());
    }

    @Test
    void colocaNaListaDeEsperaQuandoAgendaLotada() {
        when(consultaRepositorio.contarPendentesDoMedicoNaData(CRM, amanha)).thenReturn(4);

        assertEquals(ResultadoAgendamento.LISTA_DE_ESPERA, servico.agendar(CPF, CRM, amanha));
        verify(listaEsperaRepositorio).inserir(any(EntradaListaEspera.class));
        verify(consultaRepositorio, never()).inserir(any());
    }

    @Test
    void cancelamentoRepassaVagaAoPrimeiroDaFila() {
        Consulta consulta = new Consulta(10L, CRM, CPF, amanha, false);
        when(consultaRepositorio.buscarPorId(10L)).thenReturn(Optional.of(consulta));
        when(listaEsperaRepositorio.buscarPrimeiroDaFila(CRM, amanha))
                .thenReturn(Optional.of(new EntradaListaEspera(5L, CRM, "22222222222", amanha)));

        servico.cancelar(CPF, 10L);

        assertEquals("22222222222", consulta.getCpfPaciente());
        verify(consultaRepositorio).atualizar(consulta);
        verify(listaEsperaRepositorio).excluir(5L);
        verify(consultaRepositorio, never()).excluir(any());
    }

    @Test
    void cancelamentoSemFilaRemoveAConsulta() {
        Consulta consulta = new Consulta(10L, CRM, CPF, amanha, false);
        when(consultaRepositorio.buscarPorId(10L)).thenReturn(Optional.of(consulta));
        when(listaEsperaRepositorio.buscarPrimeiroDaFila(CRM, amanha)).thenReturn(Optional.empty());

        servico.cancelar(CPF, 10L);

        verify(consultaRepositorio).excluir(10L);
    }
}
