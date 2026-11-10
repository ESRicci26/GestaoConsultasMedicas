package br.com.javaricci.apresentacao.web.formulario;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/** Dados do formulário de agendamento de consulta. */
public class AgendamentoFormulario {

    @NotBlank(message = "Selecione um médico.")
    private String crmMedico;

    @NotNull(message = "Informe a data da consulta.")
    @FutureOrPresent(message = "Escolha uma data a partir de hoje.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataConsulta;

    public String getCrmMedico() { return crmMedico; }
    public void setCrmMedico(String crmMedico) { this.crmMedico = crmMedico; }
    public LocalDate getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(LocalDate dataConsulta) { this.dataConsulta = dataConsulta; }
}
