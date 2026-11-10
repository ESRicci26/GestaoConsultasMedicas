package br.com.javaricci.infraestrutura.persistencia;

import br.com.javaricci.dominio.excecao.RecursoNaoEncontradoException;
import br.com.javaricci.dominio.modelo.Consulta;
import br.com.javaricci.dominio.modelo.ConsultaDetalhada;
import br.com.javaricci.dominio.repositorio.ConsultaRepositorio;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/** Adaptador JDBC da porta {@link ConsultaRepositorio} (tabela {@code consultas}). */
@Repository
public class ConsultaRepositorioJdbc implements ConsultaRepositorio {

    private static final String SELECT_DETALHADA = """
            SELECT c.id, c.crm_medico, m.nome AS nome_medico, c.cpf_paciente,
                   p.nome AS nome_paciente, p.plano AS plano_paciente, c.data_consulta
              FROM consultas c
              JOIN medicos m ON m.crm = c.crm_medico
              JOIN pacientes p ON p.cpf = c.cpf_paciente
            """;

    private static final RowMapper<Consulta> MAPEADOR = (rs, numeroLinha) -> new Consulta(
            rs.getLong("id"),
            rs.getString("crm_medico"),
            rs.getString("cpf_paciente"),
            rs.getObject("data_consulta", LocalDate.class),
            rs.getBoolean("realizada"));

    private final JdbcTemplate jdbc;

    public ConsultaRepositorioJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Long inserir(Consulta consulta) {
        return jdbc.queryForObject(
                "INSERT INTO consultas (crm_medico, cpf_paciente, data_consulta, realizada) "
                        + "VALUES (?, ?, ?, ?) RETURNING id",
                Long.class,
                consulta.getCrmMedico(), consulta.getCpfPaciente(), consulta.getDataConsulta(), consulta.isRealizada());
    }

    @Override
    public void atualizar(Consulta consulta) {
        int afetados = jdbc.update(
                "UPDATE consultas SET crm_medico = ?, cpf_paciente = ?, data_consulta = ?, realizada = ? WHERE id = ?",
                consulta.getCrmMedico(), consulta.getCpfPaciente(), consulta.getDataConsulta(),
                consulta.isRealizada(), consulta.getId());
        if (afetados == 0) {
            throw new RecursoNaoEncontradoException("Consulta não encontrada.");
        }
    }

    @Override
    public void excluir(Long id) {
        int afetados = jdbc.update("DELETE FROM consultas WHERE id = ?", id);
        if (afetados == 0) {
            throw new RecursoNaoEncontradoException("Consulta não encontrada.");
        }
    }

    @Override
    public Optional<Consulta> buscarPorId(Long id) {
        return jdbc.query("SELECT id, crm_medico, cpf_paciente, data_consulta, realizada FROM consultas WHERE id = ?",
                MAPEADOR, id).stream().findFirst();
    }

    @Override
    public int contarPendentesDoMedicoNaData(String crmMedico, LocalDate data) {
        Integer total = jdbc.queryForObject(
                "SELECT COUNT(*) FROM consultas WHERE crm_medico = ? AND data_consulta = ? AND realizada = FALSE",
                Integer.class, crmMedico, data);
        return total == null ? 0 : total;
    }

    @Override
    public List<ConsultaDetalhada> listarPendentesDoPaciente(String cpfPaciente) {
        return jdbc.query(SELECT_DETALHADA
                        + " WHERE c.cpf_paciente = ? AND c.realizada = FALSE ORDER BY c.data_consulta, c.id",
                ConsultaDetalhadaMapeador.INSTANCIA, cpfPaciente);
    }

    @Override
    public List<ConsultaDetalhada> listarPendentesDoMedico(String crmMedico) {
        return jdbc.query(SELECT_DETALHADA
                        + " WHERE c.crm_medico = ? AND c.realizada = FALSE ORDER BY c.data_consulta, c.id",
                ConsultaDetalhadaMapeador.INSTANCIA, crmMedico);
    }

    @Override
    public List<ConsultaDetalhada> listarDoPacienteNoPeriodo(String cpfPaciente, boolean realizada,
                                                             LocalDate inicio, LocalDate fim) {
        return jdbc.query(SELECT_DETALHADA
                        + " WHERE c.cpf_paciente = ? AND c.realizada = ? AND c.data_consulta BETWEEN ? AND ?"
                        + " ORDER BY c.data_consulta, c.id",
                ConsultaDetalhadaMapeador.INSTANCIA, cpfPaciente, realizada, inicio, fim);
    }

    @Override
    public List<ConsultaDetalhada> listarDoMedicoNoPeriodo(String crmMedico, boolean realizada,
                                                           LocalDate inicio, LocalDate fim) {
        return jdbc.query(SELECT_DETALHADA
                        + " WHERE c.crm_medico = ? AND c.realizada = ? AND c.data_consulta BETWEEN ? AND ?"
                        + " ORDER BY c.data_consulta, c.id",
                ConsultaDetalhadaMapeador.INSTANCIA, crmMedico, realizada, inicio, fim);
    }
}
