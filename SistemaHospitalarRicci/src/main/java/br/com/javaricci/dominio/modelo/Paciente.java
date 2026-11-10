package br.com.javaricci.dominio.modelo;

/** Paciente atendido pelo hospital. O CPF (somente dígitos) é o identificador. */
public class Paciente {

    private String cpf;
    private String nome;
    private int idade;
    private String plano;
    private String senha;

    public Paciente(String cpf, String nome, int idade, String plano, String senha) {
        this.cpf = cpf;
        this.nome = nome;
        this.idade = idade;
        this.plano = plano;
        this.senha = senha;
    }

    /** Indica se o paciente possui plano de saúde; sem plano, a consulta é cobrada. */
    public boolean possuiPlano() {
        return plano != null && !plano.isBlank();
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public String getPlano() { return plano; }
    public void setPlano(String plano) { this.plano = plano; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
