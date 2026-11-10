package br.com.javaricci.infraestrutura.persistencia;

import br.com.javaricci.dominio.excecao.RecursoNaoEncontradoException;
import br.com.javaricci.dominio.modelo.ConsultaDetalhada;
import br.com.javaricci.dominio.modelo.ConsultaRealizada;
import br.com.javaricci.dominio.repositorio.ConsultaRealizadaRepositorio;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/** Adaptador JDBC da porta {@link ConsultaRealizadaRepositorio} (tabela {@code consultas_realizadas}). */
@Repository
public class ConsultaRealizadaRepositorioJdbc implements ConsultaRealizadaRepositorio {

    private static final String COLUNAS = "id_consulta, crm_medico, cpf_paciente, data_consulta, valor_consulta, "
            + "sintomas, tratamento, exames, medicamentos, texto_avaliacao, estrelas";

    private static final RowMapper<ConsultaRealizada> MAPEADOR = (rs, numeroLinha) -> new ConsultaRealizada(
            rs.getLong("id_consulta"),
            rs.getString("crm_medico"),
            rs.getString("cpf_paciente"),
            rs.getObject("data_consulta", LocalDate.class),
            rs.getBigDecimal("valor_consulta"),
            rs.getString("sintomas"),
            rs.getString("tratamento"),
            rs.getString("exames"),
            rs.getString("medicamentos"),
            rs.getString("texto_avaliacao"),
            rs.getInt("estrelas"));

    private final JdbcTemplate jdbc;

    public ConsultaRealizadaRepositorioJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void inserir(ConsultaRealizada consulta) {
        jdbc.update("INSERT INTO consultas_realizadas (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                consulta.getIdConsulta(), consulta.getCrmMedico(), consulta.getCpfPaciente(),
                consulta.getDataConsulta(), consulta.getValorConsulta(), consulta.getSintomas(),
                consulta.getTratamento(), consulta.getExames(), consulta.getMedicamentos(),
                consulta.getTextoAvaliacao(), consulta.getEstrelas());
    }

    @Override
    public void atualizar(ConsultaRealizada consulta) {
        int afetados = jdbc.update("UPDATE consultas_realizadas SET valor_consulta = ?, sintomas = ?, tratamento = ?, "
                        + "exames = ?, medicamentos = ?, texto_avaliacao = ?, estrelas = ? WHERE id_consulta = ?",
                consulta.getValorConsulta(), consulta.getSintomas(), consulta.getTratamento(), consulta.getExames(),
                consulta.getMedicamentos(), consulta.getTextoAvaliacao(), consulta.getEstrelas(),
                consulta.getIdConsulta());
        if (afetados == 0) {
            throw new RecursoNaoEncontradoException("Consulta realizada não encontrada.");
        }
    }

    @Override
    public void excluir(Long idConsulta) {
        int afetados = jdbc.update("DELETE FROM consultas_realizadas WHERE id_consulta = ?", idConsulta);
        if (afetados == 0) {
            throw new RecursoNaoEncontradoException("Consulta realizada não encontrada.");
        }
    }

    @Override
    public Optional<ConsultaRealizada> buscarPorId(Long idConsulta) {
        return jdbc.query("SELECT " + COLUNAS + " FROM consultas_realizadas WHERE id_consulta = ?",
                MAPEADOR, idConsulta).stream().findFirst();
    }

    @Override
    public List<ConsultaDetalhada> listarNaoAvaliadasDoPaciente(String cpfPaciente) {
        return jdbc.query("""
                        SELECT cr.id_consulta AS id, cr.crm_medico, m.nome AS nome_medico, cr.cpf_paciente,
                               p.nome AS nome_paciente, p.plano AS plano_paciente, cr.data_consulta
                          FROM consultas_realizadas cr
                          JOIN medicos m ON m.crm = cr.crm_medico
                          JOIN pacientes p ON p.cpf = cr.cpf_paciente
                         WHERE cr.cpf_paciente = ? AND cr.estrelas = 0
                         ORDER BY cr.data_consulta DESC, cr.id_consulta DESC
                        """,
                ConsultaDetalhadaMapeador.INSTANCIA, cpfPaciente);
    }

    @Override
    public List<ConsultaRealizada> listarAvaliadas() {
        return jdbc.query("SELECT " + COLUNAS + " FROM consultas_realizadas WHERE estrelas > 0 "
                + "ORDER BY data_consulta DESC, id_consulta DESC", MAPEADOR);
    }
}
