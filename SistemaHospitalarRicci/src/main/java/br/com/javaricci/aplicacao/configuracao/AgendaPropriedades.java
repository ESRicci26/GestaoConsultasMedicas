package br.com.javaricci.aplicacao.configuracao;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/** Regras da agenda; lidas de {@code hospital.agenda.*} no application.properties. */
@ConfigurationProperties(prefix = "hospital.agenda")
public record AgendaPropriedades(@DefaultValue("4") int limiteDiarioPorMedico) {
}
