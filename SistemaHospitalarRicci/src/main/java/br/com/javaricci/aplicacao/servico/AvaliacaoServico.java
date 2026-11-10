package br.com.javaricci.aplicacao.servico;

import br.com.javaricci.dominio.excecao.RecursoNaoEncontradoException;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import br.com.javaricci.dominio.modelo.ConsultaDetalhada;
import br.com.javaricci.dominio.modelo.ConsultaRealizada;
import br.com.javaricci.dominio.repositorio.ConsultaRealizadaRepositorio;
import java.util.List;
import org.springframework.stereotype.Service;

/** Caso de uso: o paciente avalia (1 a 5 estrelas e comentário) uma consulta realizada. */
@Service
public class AvaliacaoServico {

    private final ConsultaRealizadaRepositorio consultaRealizadaRepositorio;

    public AvaliacaoServico(ConsultaRealizadaRepositorio consultaRealizadaRepositorio) {
        this.consultaRealizadaRepositorio = consultaRealizadaRepositorio;
    }

    public List<ConsultaDetalhada> listarPendentes(String cpfPaciente) {
        return consultaRealizadaRepositorio.listarNaoAvaliadasDoPaciente(cpfPaciente);
    }

    public ConsultaDetalhada buscarPendente(String cpfPaciente, Long idConsulta) {
        return listarPendentes(cpfPaciente).stream()
                .filter(consulta -> consulta.getId().equals(idConsulta))
                .findFirst()
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada ou já avaliada."));
    }

    public void avaliar(String cpfPaciente, Long idConsulta, int estrelas, String texto) {
        if (estrelas < ConsultaRealizada.NOTA_MINIMA || estrelas > ConsultaRealizada.NOTA_MAXIMA) {
            throw new RegraNegocioException("A nota deve estar entre 1 e 5 estrelas.");
        }
        ConsultaRealizada consulta = consultaRealizadaRepositorio.buscarPorId(idConsulta)
                .filter(encontrada -> encontrada.pertenceAoPaciente(cpfPaciente) && !encontrada.foiAvaliada())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada ou já avaliada."));

        consulta.avaliar(estrelas, texto);
        consultaRealizadaRepositorio.atualizar(consulta);
    }
}
