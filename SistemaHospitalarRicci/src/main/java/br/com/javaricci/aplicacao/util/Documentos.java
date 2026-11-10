package br.com.javaricci.aplicacao.util;

/** Utilidades para normalização de documentos (CPF) e textos opcionais. */
public final class Documentos {

    private Documentos() {
    }

    /** Remove tudo que não for dígito (pontos, traços e espaços). */
    public static String somenteDigitos(String valor) {
        return valor == null ? null : valor.replaceAll("\\D", "");
    }

    /** Converte texto vazio ou só com espaços em {@code null}. */
    public static String nuloSeVazio(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
