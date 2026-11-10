package br.com.javaricci.dominio;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.javaricci.dominio.modelo.Medico;
import br.com.javaricci.dominio.modelo.Paciente;
import org.junit.jupiter.api.Test;

class MedicoTest {

    private final Medico medicoUnimed = new Medico("123", "Dra. Helena", "Cardiologia", "Unimed", "hash");

    @Test
    void pacienteSemPlanoPodeSerAtendidoPorQualquerMedico() {
        Paciente particular = new Paciente("11111111111", "João", 40, null, "hash");
        assertTrue(medicoUnimed.aceitaPaciente(particular));
    }

    @Test
    void pacienteComPlanoSoEAtendidoPorMedicoDoMesmoPlano() {
        Paciente mesmoPlano = new Paciente("22222222222", "Maria", 30, "unimed", "hash");
        Paciente outroPlano = new Paciente("33333333333", "Ana", 25, "Bradesco", "hash");

        assertTrue(medicoUnimed.aceitaPaciente(mesmoPlano));
        assertFalse(medicoUnimed.aceitaPaciente(outroPlano));
    }
}
