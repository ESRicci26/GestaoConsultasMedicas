package br.com.javaricci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/** Ponto de entrada da aplicação web do Sistema Hospitalar Ricci. */
@SpringBootApplication
@ConfigurationPropertiesScan
public class SistemaHospitalarRicciApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaHospitalarRicciApplication.class, args);
    }
}
