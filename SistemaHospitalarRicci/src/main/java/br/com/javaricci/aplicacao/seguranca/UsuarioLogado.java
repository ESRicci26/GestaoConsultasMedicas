package br.com.javaricci.aplicacao.seguranca;

import br.com.javaricci.dominio.modelo.Medico;
import br.com.javaricci.dominio.modelo.Paciente;
import br.com.javaricci.dominio.modelo.TipoUsuario;
import java.io.Serializable;

/** Identidade do usuário autenticado, guardada na sessão HTTP (substitui os campos estáticos do JavaFX). */
public class UsuarioLogado implements Serializable {

    /** Nome do atributo de sessão que guarda o usuário autenticado. */
    public static final String ATRIBUTO_SESSAO = "usuarioLogado";

    private static final long serialVersionUID = 1L;

    private final TipoUsuario tipo;
    private final String identificador;
    private final String nome;
    private final String plano;

    private UsuarioLogado(TipoUsuario tipo, String identificador, String nome, String plano) {
        this.tipo = tipo;
        this.identificador = identificador;
        this.nome = nome;
        this.plano = plano;
    }

    public static UsuarioLogado doPaciente(Paciente paciente) {
        return new UsuarioLogado(TipoUsuario.PACIENTE, paciente.getCpf(), paciente.getNome(), paciente.getPlano());
    }

    public static UsuarioLogado doMedico(Medico medico) {
        return new UsuarioLogado(TipoUsuario.MEDICO, medico.getCrm(), medico.getNome(), medico.getPlanoAtendido());
    }

    public static UsuarioLogado administrador(String usuario) {
        return new UsuarioLogado(TipoUsuario.ADMINISTRADOR, usuario, "Administrador", null);
    }

    public TipoUsuario getTipo() { return tipo; }
    public String getIdentificador() { return identificador; }
    public String getNome() { return nome; }
    public String getPlano() { return plano; }
}
