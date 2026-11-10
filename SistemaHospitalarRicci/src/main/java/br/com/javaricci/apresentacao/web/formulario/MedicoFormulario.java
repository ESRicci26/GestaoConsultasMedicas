package br.com.javaricci.apresentacao.web.formulario;

import br.com.javaricci.aplicacao.comando.DadosMedicoComando;
import br.com.javaricci.dominio.modelo.Medico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Dados do formulário de cadastro e edição de médico. */
public class MedicoFormulario {

    @NotBlank(message = "Informe o CRM.")
    @Size(max = 20, message = "O CRM deve ter no máximo 20 caracteres.")
    private String crm;

    @NotBlank(message = "Informe o nome.")
    @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
    private String nome;

    @NotBlank(message = "Informe a especialidade.")
    @Size(max = 80, message = "A especialidade deve ter no máximo 80 caracteres.")
    private String especialidade;

    @Size(max = 80, message = "O plano deve ter no máximo 80 caracteres.")
    private String planoAtendido;

    /** Vazia na edição significa "manter a senha atual"; a obrigatoriedade no cadastro é regra do serviço. */
    @Pattern(regexp = "|.{6,72}", message = "A senha deve ter entre 6 e 72 caracteres.")
    private String senha;

    /** Cria o formulário preenchido com os dados atuais do médico (a senha nunca é preenchida). */
    public static MedicoFormulario de(Medico medico) {
        MedicoFormulario formulario = new MedicoFormulario();
        formulario.crm = medico.getCrm();
        formulario.nome = medico.getNome();
        formulario.especialidade = medico.getEspecialidade();
        formulario.planoAtendido = medico.getPlanoAtendido();
        return formulario;
    }

    public DadosMedicoComando paraComando() {
        return new DadosMedicoComando(crm, nome, especialidade, planoAtendido, senha);
    }

    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public String getPlanoAtendido() { return planoAtendido; }
    public void setPlanoAtendido(String planoAtendido) { this.planoAtendido = planoAtendido; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
