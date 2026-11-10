package br.com.javaricci.aplicacao.servico;

import br.com.javaricci.dominio.modelo.CampoBuscaMedico;
import br.com.javaricci.dominio.modelo.ConsultaRealizada;
import br.com.javaricci.dominio.modelo.Medico;
import br.com.javaricci.dominio.modelo.Paciente;
import br.com.javaricci.dominio.modelo.ResumoMedico;
import br.com.javaricci.dominio.repositorio.ConsultaRealizadaRepositorio;
import br.com.javaricci.dominio.repositorio.MedicoRepositorio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/** Caso de uso: pesquisa de médicos exibindo nota média e comentários dos pacientes. */
@Service
public class BuscaMedicoServico {

    private final MedicoRepositorio medicoRepositorio;
    private final ConsultaRealizadaRepositorio consultaRealizadaRepositorio;

    public BuscaMedicoServico(MedicoRepositorio medicoRepositorio,
                              ConsultaRealizadaRepositorio consultaRealizadaRepositorio) {
        this.medicoRepositorio = medicoRepositorio;
        this.consultaRealizadaRepositorio = consultaRealizadaRepositorio;
    }

    /** Pacientes com plano só enxergam médicos do seu plano; termo vazio lista todos os elegíveis. */
    public List<ResumoMedico> pesquisar(CampoBuscaMedico campo, String termo, Paciente paciente) {
        String plano = paciente.possuiPlano() ? paciente.getPlano() : null;
        String termoNormalizado = termo == null ? "" : termo.trim();
        List<Medico> medicos = medicoRepositorio.pesquisar(campo, termoNormalizado, plano);

        Map<String, List<ConsultaRealizada>> avaliacoesPorMedico = agruparAvaliacoesPorMedico();
        return medicos.stream()
                .map(medico -> montarResumo(medico, avaliacoesPorMedico.getOrDefault(medico.getCrm(), List.of())))
                .toList();
    }

    private Map<String, List<ConsultaRealizada>> agruparAvaliacoesPorMedico() {
        Map<String, List<ConsultaRealizada>> agrupadas = new HashMap<>();
        for (ConsultaRealizada avaliada : consultaRealizadaRepositorio.listarAvaliadas()) {
            agrupadas.computeIfAbsent(avaliada.getCrmMedico(), crm -> new ArrayList<>()).add(avaliada);
        }
        return agrupadas;
    }

    private ResumoMedico montarResumo(Medico medico, List<ConsultaRealizada> avaliacoes) {
        double notaMedia = avaliacoes.stream().mapToInt(ConsultaRealizada::getEstrelas).average().orElse(0.0);
        List<String> comentarios = avaliacoes.stream()
                .map(ConsultaRealizada::getTextoAvaliacao)
                .filter(texto -> texto != null && !texto.isBlank())
                .toList();
        return new ResumoMedico(medico, notaMedia, comentarios);
    }
}
