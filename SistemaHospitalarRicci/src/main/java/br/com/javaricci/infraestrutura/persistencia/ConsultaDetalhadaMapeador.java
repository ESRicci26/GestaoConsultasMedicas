package br.com.javaricci.infraestrutura.persistencia;

import br.com.javaricci.dominio.modelo.ConsultaDetalhada;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.springframework.jdbc.core.RowMapper;

/** Converte o resultado das consultas com JOIN em {@link ConsultaDetalhada}. */
final class ConsultaDetalhadaMapeador implements RowMapper<ConsultaDetalhada> {

    static final ConsultaDetalhadaMapeador INSTANCIA = new ConsultaDetalhadaMapeador();

    private ConsultaDetalhadaMapeador() {
    }

    @Override
    public ConsultaDetalhada mapRow(ResultSet rs, int numeroLinha) throws SQLException {
        return new ConsultaDetalhada(
                rs.getLong("id"),
                rs.getString("crm_medico"),
                rs.getString("nome_medico"),
                rs.getString("cpf_paciente"),
                rs.getString("nome_paciente"),
                rs.getString("plano_paciente"),
                rs.getObject("data_consulta", LocalDate.class));
    }
}
