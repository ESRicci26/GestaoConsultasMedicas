package br.com.javaricci.apresentacao.web.formulario;

import br.com.javaricci.aplicacao.comando.DadosAtendimentoComando;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/** Dados do formulário de realização de consulta preenchido pelo médico. */
public class AtendimentoFormulario {

    @NotBlank(message = "Descreva os sintomas.")
    @Size(max = 2000, message = "Máximo de 2000 caracteres.")
    private String sintomas;

    @NotBlank(message = "Informe o tratamento sugerido.")
    @Size(max = 2000, message = "Máximo de 2000 caracteres.")
    private String tratamento;

    @Size(max = 2000, message = "Máximo de 2000 caracteres.")
    private String medicamentos;

    @Size(max = 2000, message = "Máximo de 2000 caracteres.")
    private String exames;

    @DecimalMin(value = "0.00", message = "O valor não pode ser negativo.")
    private BigDecimal valor;

    public DadosAtendimentoComando paraComando() {
        return new DadosAtendimentoComando(sintomas, tratamento, medicamentos, exames, valor);
    }

    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }
    public String getTratamento() { return tratamento; }
    public void setTratamento(String tratamento) { this.tratamento = tratamento; }
    public String getMedicamentos() { return medicamentos; }
    public void setMedicamentos(String medicamentos) { this.medicamentos = medicamentos; }
    public String getExames() { return exames; }
    public void setExames(String exames) { this.exames = exames; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}
