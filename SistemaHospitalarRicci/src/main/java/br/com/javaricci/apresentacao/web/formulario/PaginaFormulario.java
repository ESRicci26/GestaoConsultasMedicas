package br.com.javaricci.apresentacao.web.formulario;

/** Parâmetros de exibição de um formulário reutilizável (título, destino do envio e link de retorno). */
public class PaginaFormulario {

    private final String titulo;
    private final String acao;
    private final String urlVoltar;
    private final boolean edicao;

    public PaginaFormulario(String titulo, String acao, String urlVoltar, boolean edicao) {
        this.titulo = titulo;
        this.acao = acao;
        this.urlVoltar = urlVoltar;
        this.edicao = edicao;
    }

    public String getTitulo() { return titulo; }
    public String getAcao() { return acao; }
    public String getUrlVoltar() { return urlVoltar; }
    public boolean isEdicao() { return edicao; }
}
