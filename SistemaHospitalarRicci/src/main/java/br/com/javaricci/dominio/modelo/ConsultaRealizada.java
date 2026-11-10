package br.com.javaricci.dominio.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Registro clínico de uma consulta já realizada, incluindo a avaliação feita pelo paciente. */
public class ConsultaRealizada {

    public static final int NOTA_MINIMA = 1;
    public static final int NOTA_MAXIMA = 5;

    private final Long idConsulta;
    private final String crmMedico;
    private final String cpfPaciente;
    private final LocalDate dataConsulta;
    private BigDecimal valorConsulta;
    private String sintomas;
    private String tratamento;
    private String exames;
    private String medicamentos;
    private String textoAvaliacao;
    private int estrelas;

    public ConsultaRealizada(Long idConsulta, String crmMedico, String cpfPaciente, LocalDate dataConsulta,
                             BigDecimal valorConsulta, String sintomas, String tratamento, String exames,
                             String medicamentos, String textoAvaliacao, int estrelas) {
        this.idConsulta = idConsulta;
        this.crmMedico = crmMedico;
        this.cpfPaciente = cpfPaciente;
        this.dataConsulta = dataConsulta;
        this.valorConsulta = valorConsulta;
        this.sintomas = sintomas;
        this.tratamento = tratamento;
        this.exames = exames;
        this.medicamentos = medicamentos;
        this.textoAvaliacao = textoAvaliacao;
        this.estrelas = estrelas;
    }

    public boolean foiAvaliada() {
        return estrelas > 0;
    }

    public boolean pertenceAoPaciente(String cpf) {
        return cpfPaciente.equals(cpf);
    }

    /** Registra a avaliação do paciente; a nota deve estar entre {@link #NOTA_MINIMA} e {@link #NOTA_MAXIMA}. */
    public void avaliar(int estrelas, String texto) {
        this.estrelas = estrelas;
        this.textoAvaliacao = texto == null ? "" : texto.trim();
    }

    public Long getIdConsulta() { return idConsulta; }
    public String getCrmMedico() { return crmMedico; }
    public String getCpfPaciente() { return cpfPaciente; }
    public LocalDate getDataConsulta() { return dataConsulta; }
    public BigDecimal getValorConsulta() { return valorConsulta; }
    public String getSintomas() { return sintomas; }
    public String getTratamento() { return tratamento; }
    public String getExames() { return exames; }
    public String getMedicamentos() { return medicamentos; }
    public String getTextoAvaliacao() { return textoAvaliacao; }
    public int getEstrelas() { return estrelas; }
}
