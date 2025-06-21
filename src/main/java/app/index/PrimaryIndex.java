package app.index;

import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class PrimaryIndex {
    private Map<String, Long> index = new HashMap<>(); // ISBN -> posição no arquivo

    public void add(String isbn, long pos) {
        index.put(isbn, pos);
    }

    public void remove(String isbn) {
        index.remove(isbn);
    }

    public Long get(String isbn) {
        return index.get(isbn);
    }

    public Map<String, Long> getIndex() {
        return index;
    }

    // Salva o índice em um arquivo txt padrão (ISBN;posição)
    public void salvarParaArquivo(String caminho) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
            for (Map.Entry<String, Long> entry : index.entrySet()) {
                writer.write(entry.getKey() + ";" + entry.getValue());
                writer.newLine();
            }
        }
    }

    // Carrega o índice de um arquivo txt padrão (ISBN;posição)
    public void carregarDeArquivo(String caminho) throws IOException {
        index.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 2) {
                    index.put(partes[0], Long.parseLong(partes[1]));
                }
            }
        }
    }

    // Gera o índice a partir do arquivo de dados (mangas.txt), usando o byte offset como posição
    public void gerarIndiceDeArquivoDeDados(String caminhoMangas) throws IOException {
        index.clear();
        try (RandomAccessFile raf = new RandomAccessFile(caminhoMangas, "r")) {
            String linha;
            long offset = 0;
            while ((linha = raf.readLine()) != null) {
                String[] partes = linha.split(";", 2); // ISBN é o primeiro campo
                if (partes.length > 0) {
                    String isbn = partes[0].trim();
                    index.put(isbn, offset);
                }
                offset = raf.getFilePointer();
            }
        }
    }
} 