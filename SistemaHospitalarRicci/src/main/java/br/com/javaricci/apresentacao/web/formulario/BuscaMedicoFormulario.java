package br.com.javaricci.apresentacao.web.formulario;

import br.com.javaricci.dominio.modelo.CampoBuscaMedico;

/** Parâmetros da pesquisa de médicos. Termo {@code null} significa que nenhuma pesquisa foi feita ainda. */
public class BuscaMedicoFormulario {

    private CampoBuscaMedico campo = CampoBuscaMedico.NOME;
    private String termo;

    public CampoBuscaMedico getCampo() { return campo; }
    public void setCampo(CampoBuscaMedico campo) { this.campo = campo; }
    public String getTermo() { return termo; }
    public void setTermo(String termo) { this.termo = termo; }
}
