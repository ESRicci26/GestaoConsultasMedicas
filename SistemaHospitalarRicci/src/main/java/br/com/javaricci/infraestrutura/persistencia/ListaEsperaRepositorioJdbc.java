package br.com.javaricci.infraestrutura.persistencia;

import br.com.javaricci.dominio.excecao.RecursoNaoEncontradoException;
import br.com.javaricci.dominio.modelo.EntradaListaEspera;
import br.com.javaricci.dominio.repositorio.ListaEsperaRepositorio;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/** Adaptador JDBC da porta {@link ListaEsperaRepositorio} (tabela {@code lista_espera}). */
@Repository
public class ListaEsperaRepositorioJdbc implements ListaEsperaRepositorio {

    private static final String SELECT_BASE =
            "SELECT id, crm_medico, cpf_paciente, data_consulta FROM lista_espera ";

    private static final RowMapper<EntradaListaEspera> MAPEADOR = (rs, numeroLinha) -> new EntradaListaEspera(
            rs.getLong("id"),
            rs.getString("crm_medico"),
            rs.getString("cpf_paciente"),
            rs.getObject("data_consulta", LocalDate.class));

    private final JdbcTemplate jdbc;

    public ListaEsperaRepositorioJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void inserir(EntradaListaEspera entrada) {
        jdbc.update("INSERT INTO lista_espera (crm_medico, cpf_paciente, data_consulta) VALUES (?, ?, ?)",
                entrada.getCrmMedico(), entrada.getCpfPaciente(), entrada.getDataConsulta());
    }

    @Override
    public void excluir(Long id) {
        int afetados = jdbc.update("DELETE FROM lista_espera WHERE id = ?", id);
        if (afetados == 0) {
            throw new RecursoNaoEncontradoException("Entrada da lista de espera não encontrada.");
        }
    }

    @Override
    public List<EntradaListaEspera> listarPorMedicoEData(String crmMedico, LocalDate data) {
        return jdbc.query(SELECT_BASE + "WHERE crm_medico = ? AND data_consulta = ? ORDER BY id",
                MAPEADOR, crmMedico, data);
    }

    @Override
    public Optional<EntradaListaEspera> buscarPrimeiroDaFila(String crmMedico, LocalDate data) {
        return jdbc.query(SELECT_BASE + "WHERE crm_medico = ? AND data_consulta = ? ORDER BY id LIMIT 1",
                MAPEADOR, crmMedico, data).stream().findFirst();
    }
}
