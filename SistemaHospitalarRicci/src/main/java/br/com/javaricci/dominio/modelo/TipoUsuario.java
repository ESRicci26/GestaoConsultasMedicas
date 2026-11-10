package br.com.javaricci.dominio.modelo;

/** Perfis de acesso ao sistema, cada um associado a uma área (prefixo de URL). */
public enum TipoUsuario {

    PACIENTE("Paciente", "/paciente", "CPF"),
    MEDICO("Médico", "/medico", "CRM"),
    ADMINISTRADOR("Administrador", "/admin", "Usuário");

    private final String rotulo;
    private final String caminhoBase;
    private final String rotuloIdentificador;

    TipoUsuario(String rotulo, String caminhoBase, String rotuloIdentificador) {
        this.rotulo = rotulo;
        this.caminhoBase = caminhoBase;
        this.rotuloIdentificador = rotuloIdentificador;
    }

    /** Descobre o perfil exigido por um caminho de URL (ex.: "/medico/menu" exige MEDICO). */
    public static TipoUsuario doCaminho(String caminho) {
        for (TipoUsuario tipo : values()) {
            if (caminho.equals(tipo.caminhoBase) || caminho.startsWith(tipo.caminhoBase + "/")) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Caminho sem perfil associado: " + caminho);
    }

    public String getRotulo() { return rotulo; }
    public String getCaminhoBase() { return caminhoBase; }
    public String getRotuloIdentificador() { return rotuloIdentificador; }
}
