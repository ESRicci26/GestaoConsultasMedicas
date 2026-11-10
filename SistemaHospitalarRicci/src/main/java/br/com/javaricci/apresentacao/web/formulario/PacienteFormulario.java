package br.com.javaricci.apresentacao.web.formulario;

import br.com.javaricci.aplicacao.comando.DadosPacienteComando;
import br.com.javaricci.aplicacao.util.Documentos;
import br.com.javaricci.dominio.modelo.Paciente;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Dados do formulário de cadastro e edição de paciente. */
public class PacienteFormulario {

    @NotBlank(message = "Informe o CPF.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos.")
    private String cpf;

    @NotBlank(message = "Informe o nome.")
    @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
    private String nome;

    @NotNull(message = "Informe a idade.")
    @Min(value = 0, message = "Idade inválida.")
    @Max(value = 130, message = "Idade inválida.")
    private Integer idade;

    @Size(max = 80, message = "O plano deve ter no máximo 80 caracteres.")
    private String plano;

    /** Vazia na edição significa "manter a senha atual"; a obrigatoriedade no cadastro é regra do serviço. */
    @Pattern(regexp = "|.{6,72}", message = "A senha deve ter entre 6 e 72 caracteres.")
    private String senha;

    /** Cria o formulário preenchido com os dados atuais do paciente (a senha nunca é preenchida). */
    public static PacienteFormulario de(Paciente paciente) {
        PacienteFormulario formulario = new PacienteFormulario();
        formulario.cpf = paciente.getCpf();
        formulario.nome = paciente.getNome();
        formulario.idade = paciente.getIdade();
        formulario.plano = paciente.getPlano();
        return formulario;
    }

    public DadosPacienteComando paraComando() {
        return new DadosPacienteComando(cpf, nome, idade, plano, senha);
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = Documentos.somenteDigitos(cpf); }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }
    public String getPlano() { return plano; }
    public void setPlano(String plano) { this.plano = plano; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
