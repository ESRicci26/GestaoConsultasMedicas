package br.com.javaricci.dominio.modelo;

import java.time.LocalDate;

/** Visão de leitura de uma consulta, já com os nomes do médico e do paciente (usada em listas e relatórios). */
public class ConsultaDetalhada {

    private final Long id;
    private final String crmMedico;
    private final String nomeMedico;
    private final String cpfPaciente;
    private final String nomePaciente;
    private final String planoPaciente;
    private final LocalDate dataConsulta;

    public ConsultaDetalhada(Long id, String crmMedico, String nomeMedico, String cpfPaciente,
                             String nomePaciente, String planoPaciente, LocalDate dataConsulta) {
        this.id = id;
        this.crmMedico = crmMedico;
        this.nomeMedico = nomeMedico;
        this.cpfPaciente = cpfPaciente;
        this.nomePaciente = nomePaciente;
        this.planoPaciente = planoPaciente;
        this.dataConsulta = dataConsulta;
    }

    public boolean pacienteTemPlano() {
        return planoPaciente != null && !planoPaciente.isBlank();
    }

    public Long getId() { return id; }
    public String getCrmMedico() { return crmMedico; }
    public String getNomeMedico() { return nomeMedico; }
    public String getCpfPaciente() { return cpfPaciente; }
    public String getNomePaciente() { return nomePaciente; }
    public String getPlanoPaciente() { return planoPaciente; }
    public LocalDate getDataConsulta() { return dataConsulta; }
}
