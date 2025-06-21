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
} 