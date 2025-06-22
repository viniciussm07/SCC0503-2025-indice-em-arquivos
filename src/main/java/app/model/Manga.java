package app.model;

import java.util.List;

public class Manga {
    private String isbn;
    private String titulo;
    private String autores;
    private int anoInicio;
    private Integer anoFim; // pode ser null
    private String genero;
    private String revista;
    private String editora;
    private int anoEdicao;
    private int qtdVolumes;
    private int qtdVolumesAdquiridos;
    private List<Integer> volumesAdquiridos;
    private boolean removido; // Campo para remoção lógica

    public Manga(String isbn, String titulo, String autores, int anoInicio, Integer anoFim, String genero, String revista, String editora, int anoEdicao, int qtdVolumes, int qtdVolumesAdquiridos, List<Integer> volumesAdquiridos) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autores = autores;
        this.anoInicio = anoInicio;
        this.anoFim = anoFim;
        this.genero = genero;
        this.revista = revista;
        this.editora = editora;
        this.anoEdicao = anoEdicao;
        this.qtdVolumes = qtdVolumes;
        this.qtdVolumesAdquiridos = qtdVolumesAdquiridos;
        this.volumesAdquiridos = volumesAdquiridos;
        this.removido = false; // Por padrão, não está removido
    }

    // Getters e setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutores() { return autores; }
    public void setAutores(String autores) { this.autores = autores; }
    public int getAnoInicio() { return anoInicio; }
    public void setAnoInicio(int anoInicio) { this.anoInicio = anoInicio; }
    public Integer getAnoFim() { return anoFim; }
    public void setAnoFim(Integer anoFim) { this.anoFim = anoFim; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getRevista() { return revista; }
    public void setRevista(String revista) { this.revista = revista; }
    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }
    public int getAnoEdicao() { return anoEdicao; }
    public void setAnoEdicao(int anoEdicao) { this.anoEdicao = anoEdicao; }
    public int getQtdVolumes() { return qtdVolumes; }
    public void setQtdVolumes(int qtdVolumes) { this.qtdVolumes = qtdVolumes; }
    public int getQtdVolumesAdquiridos() { return qtdVolumesAdquiridos; }
    public void setQtdVolumesAdquiridos(int qtdVolumesAdquiridos) { this.qtdVolumesAdquiridos = qtdVolumesAdquiridos; }
    public List<Integer> getVolumesAdquiridos() { return volumesAdquiridos; }
    public void setVolumesAdquiridos(List<Integer> volumesAdquiridos) { this.volumesAdquiridos = volumesAdquiridos; }
    public boolean isRemovido() { return removido; }
    public void setRemovido(boolean removido) { this.removido = removido; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (removido) {
            sb.append("=== ").append(titulo).append(" (REMOVIDO) ===\n");
        } else {
            sb.append("=== ").append(titulo).append(" ===\n");
        }
        sb.append("ISBN: ").append(isbn).append("\n");
        sb.append("Autores: ").append(autores).append("\n");
        sb.append("Período: ").append(anoInicio);
        if (anoFim != null) {
            sb.append(" - ").append(anoFim);
        } else {
            sb.append(" - Em andamento");
        }
        sb.append("\n");
        sb.append("Gênero: ").append(genero).append("\n");
        sb.append("Revista: ").append(revista).append("\n");
        sb.append("Editora: ").append(editora).append(" (").append(anoEdicao).append(")\n");
        sb.append("Volumes: ").append(qtdVolumesAdquiridos).append("/").append(qtdVolumes);
        if (qtdVolumesAdquiridos > 0) {
            sb.append(" (").append(String.format("%.1f", (double)qtdVolumesAdquiridos/qtdVolumes * 100)).append("% completo)");
        }
        sb.append("\n");
        if (qtdVolumesAdquiridos > 0) {
            sb.append("Volumes adquiridos: ").append(volumesAdquiridos).append("\n");
        } else {
            sb.append("Nenhum volume adquirido ainda.\n");
        }
        return sb.toString();
    }
} 