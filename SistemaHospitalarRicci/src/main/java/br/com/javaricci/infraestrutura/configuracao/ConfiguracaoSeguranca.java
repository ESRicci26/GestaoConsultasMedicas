package br.com.javaricci.infraestrutura.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Define o algoritmo de hash das senhas (BCrypt), no lugar das senhas em texto puro do sistema original. */
@Configuration
public class ConfiguracaoSeguranca {

    @Bean
    public PasswordEncoder codificadorSenha() {
        return new BCryptPasswordEncoder();
    }
}
