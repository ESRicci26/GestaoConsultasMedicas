package br.com.javaricci.dominio.modelo;

/** Tipos de relatório de consultas disponíveis para pacientes e médicos. */
public enum TipoRelatorio {

    CONSULTAS("Consultas realizadas", true),
    AGENDAMENTOS("Agendamentos pendentes", false);

    private final String rotulo;
    private final boolean realizada;

    TipoRelatorio(String rotulo, boolean realizada) {
        this.rotulo = rotulo;
        this.realizada = realizada;
    }

    public String getRotulo() { return rotulo; }
    public boolean isRealizada() { return realizada; }
}
