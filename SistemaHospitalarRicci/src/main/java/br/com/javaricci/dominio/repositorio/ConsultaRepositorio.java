package br.com.javaricci.dominio.repositorio;

import br.com.javaricci.dominio.modelo.Consulta;
import br.com.javaricci.dominio.modelo.ConsultaDetalhada;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Porta de saída para persistência de consultas agendadas. */
public interface ConsultaRepositorio {

    /** Insere a consulta e devolve o identificador gerado. */
    Long inserir(Consulta consulta);

    void atualizar(Consulta consulta);

    void excluir(Long id);

    Optional<Consulta> buscarPorId(Long id);

    int contarPendentesDoMedicoNaData(String crmMedico, LocalDate data);

    List<ConsultaDetalhada> listarPendentesDoPaciente(String cpfPaciente);

    List<ConsultaDetalhada> listarPendentesDoMedico(String crmMedico);

    List<ConsultaDetalhada> listarDoPacienteNoPeriodo(String cpfPaciente, boolean realizada,
                                                      LocalDate inicio, LocalDate fim);

    List<ConsultaDetalhada> listarDoMedicoNoPeriodo(String crmMedico, boolean realizada,
                                                    LocalDate inicio, LocalDate fim);
}
