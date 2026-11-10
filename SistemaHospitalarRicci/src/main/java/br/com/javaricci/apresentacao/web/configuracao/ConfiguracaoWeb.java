package br.com.javaricci.apresentacao.web.configuracao;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Registra o interceptador que protege as áreas restritas do sistema. */
@Configuration
public class ConfiguracaoWeb implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registro) {
        registro.addInterceptor(new InterceptadorAutenticacao())
                .addPathPatterns("/paciente/**", "/medico/**", "/admin/**")
                .excludePathPatterns("/paciente/login", "/medico/login", "/admin/login");
    }
}
