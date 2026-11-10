package br.com.javaricci.dominio.repositorio;

import br.com.javaricci.dominio.modelo.ConsultaDetalhada;
import br.com.javaricci.dominio.modelo.ConsultaRealizada;
import java.util.List;
import java.util.Optional;

/** Porta de saída para persistência de consultas realizadas e suas avaliações. */
public interface ConsultaRealizadaRepositorio {

    void inserir(ConsultaRealizada consultaRealizada);

    void atualizar(ConsultaRealizada consultaRealizada);

    void excluir(Long idConsulta);

    Optional<ConsultaRealizada> buscarPorId(Long idConsulta);

    List<ConsultaDetalhada> listarNaoAvaliadasDoPaciente(String cpfPaciente);

    List<ConsultaRealizada> listarAvaliadas();
}
