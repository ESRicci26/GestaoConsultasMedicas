package br.com.javaricci.infraestrutura.persistencia;

import br.com.javaricci.dominio.excecao.RecursoNaoEncontradoException;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import br.com.javaricci.dominio.modelo.CampoBuscaMedico;
import br.com.javaricci.dominio.modelo.Medico;
import br.com.javaricci.dominio.repositorio.MedicoRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/** Adaptador JDBC da porta {@link MedicoRepositorio} (tabela {@code medicos}). */
@Repository
public class MedicoRepositorioJdbc implements MedicoRepositorio {

    private static final String COLUNAS = "crm, nome, especialidade, plano_atendido, senha";

    private static final RowMapper<Medico> MAPEADOR = (rs, numeroLinha) -> new Medico(
            rs.getString("crm"),
            rs.getString("nome"),
            rs.getString("especialidade"),
            rs.getString("plano_atendido"),
            rs.getString("senha"));

    private final JdbcTemplate jdbc;

    public MedicoRepositorioJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void inserir(Medico medico) {
        jdbc.update("INSERT INTO medicos (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?)",
                medico.getCrm(), medico.getNome(), medico.getEspecialidade(), medico.getPlanoAtendido(), medico.getSenha());
    }

    @Override
    public void atualizar(Medico medico) {
        int afetados = jdbc.update(
                "UPDATE medicos SET nome = ?, especialidade = ?, plano_atendido = ?, senha = ? WHERE crm = ?",
                medico.getNome(), medico.getEspecialidade(), medico.getPlanoAtendido(), medico.getSenha(), medico.getCrm());
        if (afetados == 0) {
            throw new RecursoNaoEncontradoException("Médico não encontrado.");
        }
    }

    @Override
    public void excluir(String crm) {
        try {
            int afetados = jdbc.update("DELETE FROM medicos WHERE crm = ?", crm);
            if (afetados == 0) {
                throw new RecursoNaoEncontradoException("Médico não encontrado.");
            }
        } catch (DataIntegrityViolationException e) {
            throw new RegraNegocioException("Não é possível excluir o médico: existem consultas vinculadas a ele.");
        }
    }

    @Override
    public List<Medico> listar() {
        return jdbc.query("SELECT " + COLUNAS + " FROM medicos ORDER BY nome", MAPEADOR);
    }

    @Override
    public List<Medico> listarPorPlano(String plano) {
        return jdbc.query("SELECT " + COLUNAS + " FROM medicos WHERE LOWER(plano_atendido) = LOWER(?) ORDER BY nome",
                MAPEADOR, plano);
    }

    @Override
    public List<Medico> pesquisar(CampoBuscaMedico campo, String termo, String plano) {
        StringBuilder sql = new StringBuilder("SELECT " + COLUNAS + " FROM medicos WHERE ")
                .append(campo.getColuna()).append(" ILIKE ?");
        List<Object> parametros = new ArrayList<>();
        parametros.add("%" + escaparCuringas(termo) + "%");

        if (plano != null) {
            sql.append(" AND LOWER(plano_atendido) = LOWER(?)");
            parametros.add(plano);
        }
        sql.append(" ORDER BY nome");
        return jdbc.query(sql.toString(), MAPEADOR, parametros.toArray());
    }

    @Override
    public Optional<Medico> buscarPorCrm(String crm) {
        return jdbc.query("SELECT " + COLUNAS + " FROM medicos WHERE crm = ?", MAPEADOR, crm).stream().findFirst();
    }

    @Override
    public boolean existePorCrm(String crm) {
        Boolean existe = jdbc.queryForObject("SELECT EXISTS (SELECT 1 FROM medicos WHERE crm = ?)", Boolean.class, crm);
        return Boolean.TRUE.equals(existe);
    }

    /** Impede que '%' e '_' digitados pelo usuário sejam tratados como curingas do LIKE. */
    private String escaparCuringas(String termo) {
        return termo.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}
