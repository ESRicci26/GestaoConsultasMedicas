package br.com.javaricci.dominio.modelo;

import java.time.LocalDate;

/** Paciente aguardando vaga na agenda de um médico em uma data lotada. */
public class EntradaListaEspera {

    private final Long id;
    private final String crmMedico;
    private final String cpfPaciente;
    private final LocalDate dataConsulta;

    public EntradaListaEspera(Long id, String crmMedico, String cpfPaciente, LocalDate dataConsulta) {
        this.id = id;
        this.crmMedico = crmMedico;
        this.cpfPaciente = cpfPaciente;
        this.dataConsulta = dataConsulta;
    }

    public Long getId() { return id; }
    public String getCrmMedico() { return crmMedico; }
    public String getCpfPaciente() { return cpfPaciente; }
    public LocalDate getDataConsulta() { return dataConsulta; }
}
