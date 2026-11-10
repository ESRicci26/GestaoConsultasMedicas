package br.com.javaricci.aplicacao.comando;

import java.math.BigDecimal;

/** Informações registradas pelo médico ao finalizar uma consulta. O valor só é exigido de pacientes sem plano. */
public record DadosAtendimentoComando(String sintomas, String tratamento, String medicamentos,
                                      String exames, BigDecimal valor) {
}
