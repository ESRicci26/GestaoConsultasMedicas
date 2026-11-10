package br.com.javaricci.dominio.repositorio;

import br.com.javaricci.dominio.modelo.Paciente;
import java.util.List;
import java.util.Optional;

/** Porta de saída para persistência de pacientes. */
public interface PacienteRepositorio {

    void inserir(Paciente paciente);

    void atualizar(Paciente paciente);

    void excluir(String cpf);

    List<Paciente> listar();

    Optional<Paciente> buscarPorCpf(String cpf);

    boolean existePorCpf(String cpf);
}
