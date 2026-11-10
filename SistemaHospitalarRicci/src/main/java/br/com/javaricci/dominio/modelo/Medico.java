package br.com.javaricci.dominio.modelo;

/** Médico cadastrado no hospital. O CRM é o identificador. */
public class Medico {

    private String crm;
    private String nome;
    private String especialidade;
    private String planoAtendido;
    private String senha;

    public Medico(String crm, String nome, String especialidade, String planoAtendido, String senha) {
        this.crm = crm;
        this.nome = nome;
        this.especialidade = especialidade;
        this.planoAtendido = planoAtendido;
        this.senha = senha;
    }

    /**
     * Pacientes sem plano podem ser atendidos por qualquer médico (consulta particular);
     * pacientes com plano só por médicos que atendem o mesmo plano.
     */
    public boolean aceitaPaciente(Paciente paciente) {
        if (!paciente.possuiPlano()) {
            return true;
        }
        return planoAtendido != null && planoAtendido.equalsIgnoreCase(paciente.getPlano());
    }

    public String getCrm() { return crm; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public String getPlanoAtendido() { return planoAtendido; }
    public void setPlanoAtendido(String planoAtendido) { this.planoAtendido = planoAtendido; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
