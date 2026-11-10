package br.com.javaricci.dominio.modelo;

/** Campos pelos quais um médico pode ser pesquisado. A coluna é fixa para evitar injeção de SQL. */
public enum CampoBuscaMedico {

    NOME("Nome", "nome"),
    ESPECIALIDADE("Especialidade", "especialidade");

    private final String rotulo;
    private final String coluna;

    CampoBuscaMedico(String rotulo, String coluna) {
        this.rotulo = rotulo;
        this.coluna = coluna;
    }

    public String getRotulo() { return rotulo; }
    public String getColuna() { return coluna; }
}
