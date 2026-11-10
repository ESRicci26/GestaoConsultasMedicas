package br.com.javaricci.dominio.repositorio;

import br.com.javaricci.dominio.modelo.CampoBuscaMedico;
import br.com.javaricci.dominio.modelo.Medico;
import java.util.List;
import java.util.Optional;

/** Porta de saída para persistência de médicos. */
public interface MedicoRepositorio {

    void inserir(Medico medico);

    void atualizar(Medico medico);

    void excluir(String crm);

    List<Medico> listar();

    List<Medico> listarPorPlano(String plano);

    /** Pesquisa por trecho do campo informado; se {@code plano} não for nulo, restringe aos médicos desse plano. */
    List<Medico> pesquisar(CampoBuscaMedico campo, String termo, String plano);

    Optional<Medico> buscarPorCrm(String crm);

    boolean existePorCrm(String crm);
}
