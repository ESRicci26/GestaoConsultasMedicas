package br.com.javaricci.dominio.repositorio;

import br.com.javaricci.dominio.modelo.EntradaListaEspera;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Porta de saída para persistência da lista de espera dos médicos. */
public interface ListaEsperaRepositorio {

    void inserir(EntradaListaEspera entrada);

    void excluir(Long id);

    /** Lista a fila de um médico em uma data, do mais antigo para o mais recente. */
    List<EntradaListaEspera> listarPorMedicoEData(String crmMedico, LocalDate data);

    Optional<EntradaListaEspera> buscarPrimeiroDaFila(String crmMedico, LocalDate data);
}
