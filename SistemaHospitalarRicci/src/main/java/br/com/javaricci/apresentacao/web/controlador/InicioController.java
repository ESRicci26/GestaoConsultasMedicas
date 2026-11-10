package br.com.javaricci.apresentacao.web.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Página inicial com o acesso aos três perfis (substitui o MenuPrincipal do JavaFX). */
@Controller
public class InicioController {

    @GetMapping("/")
    public String inicio() {
        return "inicio";
    }
}
