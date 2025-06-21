package app.controller;

import app.model.Manga;
import app.index.PrimaryIndex;
import app.index.SecondaryIndex;
import java.util.List;

public class MangaController {
    private PrimaryIndex primaryIndex;
    private SecondaryIndex secondaryIndex;
    // private RandomAccessFile dataFile; // para manipulação do arquivo mangas.txt

    public MangaController(PrimaryIndex primaryIndex, SecondaryIndex secondaryIndex) {
        this.primaryIndex = primaryIndex;
        this.secondaryIndex = secondaryIndex;
        // Inicializar/manipular arquivo de dados mangas.txt aqui
    }

    public void criarManga(Manga manga) {
        // TODO: implementar lógica de escrita no arquivo mangas.txt e atualização dos índices
    }

    public Manga lerMangaPorISBN(String isbn) {
        // TODO: implementar leitura do arquivo mangas.txt usando índice primário
        return null;
    }

    public List<Manga> lerMangasPorTitulo(String titulo) {
        // TODO: implementar leitura do arquivo mangas.txt usando índice secundário
        return null;
    }

    public void atualizarManga(Manga manga) {
        // TODO: implementar atualização no arquivo mangas.txt
    }

    public void deletarManga(String isbn) {
        // TODO: implementar exclusão com confirmação no arquivo mangas.txt
    }
} 