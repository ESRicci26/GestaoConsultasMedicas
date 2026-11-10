package br.com.javaricci.infraestrutura.persistencia;

import br.com.javaricci.dominio.excecao.RecursoNaoEncontradoException;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import br.com.javaricci.dominio.modelo.Paciente;
import br.com.javaricci.dominio.repositorio.PacienteRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/** Adaptador JDBC da porta {@link PacienteRepositorio} (tabela {@code pacientes}). */
@Repository
public class PacienteRepositorioJdbc implements PacienteRepositorio {

    private static final String COLUNAS = "cpf, nome, idade, plano, senha";

    private static final RowMapper<Paciente> MAPEADOR = (rs, numeroLinha) -> new Paciente(
            rs.getString("cpf"),
            rs.getString("nome"),
            rs.getInt("idade"),
            rs.getString("plano"),
            rs.getString("senha"));

    private final JdbcTemplate jdbc;

    public PacienteRepositorioJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void inserir(Paciente paciente) {
        jdbc.update("INSERT INTO pacientes (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?)",
                paciente.getCpf(), paciente.getNome(), paciente.getIdade(), paciente.getPlano(), paciente.getSenha());
    }

    @Override
    public void atualizar(Paciente paciente) {
        int afetados = jdbc.update("UPDATE pacientes SET nome = ?, idade = ?, plano = ?, senha = ? WHERE cpf = ?",
                paciente.getNome(), paciente.getIdade(), paciente.getPlano(), paciente.getSenha(), paciente.getCpf());
        if (afetados == 0) {
            throw new RecursoNaoEncontradoException("Paciente não encontrado.");
        }
    }

    @Override
    public void excluir(String cpf) {
        try {
            int afetados = jdbc.update("DELETE FROM pacientes WHERE cpf = ?", cpf);
            if (afetados == 0) {
                throw new RecursoNaoEncontradoException("Paciente não encontrado.");
            }
        } catch (DataIntegrityViolationException e) {
            throw new RegraNegocioException("Não é possível excluir o paciente: existem consultas vinculadas a ele.");
        }
    }

    @Override
    public List<Paciente> listar() {
        return jdbc.query("SELECT " + COLUNAS + " FROM pacientes ORDER BY nome", MAPEADOR);
    }

    @Override
    public Optional<Paciente> buscarPorCpf(String cpf) {
        return jdbc.query("SELECT " + COLUNAS + " FROM pacientes WHERE cpf = ?", MAPEADOR, cpf).stream().findFirst();
    }

    @Override
    public boolean existePorCpf(String cpf) {
        Boolean existe = jdbc.queryForObject("SELECT EXISTS (SELECT 1 FROM pacientes WHERE cpf = ?)", Boolean.class, cpf);
        return Boolean.TRUE.equals(existe);
    }
}
