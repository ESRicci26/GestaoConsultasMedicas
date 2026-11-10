package br.com.javaricci.apresentacao.web.formulario;

import br.com.javaricci.dominio.modelo.TipoRelatorio;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/** Filtros dos relatórios: tipo e período (padrão: 30 dias antes e depois de hoje). */
public class RelatorioFormulario {

    private TipoRelatorio tipo = TipoRelatorio.CONSULTAS;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate inicio = LocalDate.now().minusDays(30);

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fim = LocalDate.now().plusDays(30);

    public boolean estaCompleto() {
        return tipo != null && inicio != null && fim != null;
    }

    public TipoRelatorio getTipo() { return tipo; }
    public void setTipo(TipoRelatorio tipo) { this.tipo = tipo; }
    public LocalDate getInicio() { return inicio; }
    public void setInicio(LocalDate inicio) { this.inicio = inicio; }
    public LocalDate getFim() { return fim; }
    public void setFim(LocalDate fim) { this.fim = fim; }
}
