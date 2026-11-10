package br.com.javaricci.apresentacao.web.formulario;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Dados do formulário de avaliação de uma consulta. */
public class AvaliacaoFormulario {

    @NotNull(message = "Selecione de 1 a 5 estrelas.")
    @Min(value = 1, message = "Selecione de 1 a 5 estrelas.")
    @Max(value = 5, message = "Selecione de 1 a 5 estrelas.")
    private Integer estrelas;

    @Size(max = 1000, message = "O comentário deve ter no máximo 1000 caracteres.")
    private String texto;

    public Integer getEstrelas() { return estrelas; }
    public void setEstrelas(Integer estrelas) { this.estrelas = estrelas; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
}
