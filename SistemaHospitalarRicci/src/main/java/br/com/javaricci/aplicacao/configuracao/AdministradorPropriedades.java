package br.com.javaricci.aplicacao.configuracao;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/** Credenciais do administrador; lidas de {@code hospital.administrador.*} no application.properties. */
@ConfigurationProperties(prefix = "hospital.administrador")
public record AdministradorPropriedades(@DefaultValue("admin") String usuario,
                                        @DefaultValue("admin123") String senha) {
}
