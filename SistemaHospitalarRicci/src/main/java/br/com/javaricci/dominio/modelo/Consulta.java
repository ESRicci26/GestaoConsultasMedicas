package br.com.javaricci.dominio.modelo;

import java.time.LocalDate;

/** Consulta agendada entre um paciente e um médico em uma data. */
public class Consulta {

    private Long id;
    private String crmMedico;
    private String cpfPaciente;
    private LocalDate dataConsulta;
    private boolean realizada;

    public Consulta(Long id, String crmMedico, String cpfPaciente, LocalDate dataConsulta, boolean realizada) {
        this.id = id;
        this.crmMedico = crmMedico;
        this.cpfPaciente = cpfPaciente;
        this.dataConsulta = dataConsulta;
        this.realizada = realizada;
    }

    /** Cria uma nova consulta ainda não realizada e sem identificador. */
    public static Consulta agendar(String crmMedico, String cpfPaciente, LocalDate dataConsulta) {
        return new Consulta(null, crmMedico, cpfPaciente, dataConsulta, false);
    }

    public boolean pertenceAoPaciente(String cpf) {
        return cpfPaciente.equals(cpf);
    }

    public boolean pertenceAoMedico(String crm) {
        return crmMedico.equals(crm);
    }

    public Long getId() { return id; }
    public String getCrmMedico() { return crmMedico; }
    public void setCrmMedico(String crmMedico) { this.crmMedico = crmMedico; }
    public String getCpfPaciente() { return cpfPaciente; }
    public void setCpfPaciente(String cpfPaciente) { this.cpfPaciente = cpfPaciente; }
    public LocalDate getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(LocalDate dataConsulta) { this.dataConsulta = dataConsulta; }
    public boolean isRealizada() { return realizada; }
    public void setRealizada(boolean realizada) { this.realizada = realizada; }
}
