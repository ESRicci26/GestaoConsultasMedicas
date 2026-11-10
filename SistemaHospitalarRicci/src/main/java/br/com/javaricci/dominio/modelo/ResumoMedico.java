package br.com.javaricci.dominio.modelo;

import java.util.List;
import java.util.Locale;

/** Médico acompanhado da nota média e dos comentários deixados pelos pacientes. */
public class ResumoMedico {

    private static final Locale LOCALE_BRASIL = Locale.of("pt", "BR");

    private final String crm;
    private final String nome;
    private final String especialidade;
    private final String planoAtendido;
    private final double notaMedia;
    private final List<String> comentarios;

    public ResumoMedico(Medico medico, double notaMedia, List<String> comentarios) {
        this.crm = medico.getCrm();
        this.nome = medico.getNome();
        this.especialidade = medico.getEspecialidade();
        this.planoAtendido = medico.getPlanoAtendido();
        this.notaMedia = notaMedia;
        this.comentarios = List.copyOf(comentarios);
    }

    public boolean possuiAvaliacoes() {
        return notaMedia > 0;
    }

    public String getNotaMediaFormatada() {
        return String.format(LOCALE_BRASIL, "%.1f", notaMedia);
    }

    public String getCrm() { return crm; }
    public String getNome() { return nome; }
    public String getEspecialidade() { return especialidade; }
    public String getPlanoAtendido() { return planoAtendido; }
    public double getNotaMedia() { return notaMedia; }
    public List<String> getComentarios() { return comentarios; }
}
