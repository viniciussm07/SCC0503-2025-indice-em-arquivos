package app.index;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.io.*;

public class SecondaryIndex {
    private Map<String, Set<String>> index = new HashMap<>(); // título -> conjunto de ISBNs

    public void add(String titulo, String isbn) {
        index.computeIfAbsent(titulo, k -> new HashSet<>()).add(isbn);
    }

    public void remove(String titulo, String isbn) {
        Set<String> isbns = index.get(titulo);
        if (isbns != null) {
            isbns.remove(isbn);
            if (isbns.isEmpty()) {
                index.remove(titulo);
            }
        }
    }

    public Set<String> get(String titulo) {
        return index.getOrDefault(titulo, new HashSet<>());
    }

    public Map<String, Set<String>> getIndex() {
        return index;
    }

    // Salva o índice em um arquivo txt padrão (título;ISBN1,ISBN2,...)
    public void salvarParaArquivo(String caminho) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
            for (Map.Entry<String, Set<String>> entry : index.entrySet()) {
                String titulo = entry.getKey();
                String isbns = String.join(",", entry.getValue());
                writer.write(titulo + ";" + isbns);
                writer.newLine();
            }
        }
    }

    // Carrega o índice de um arquivo txt padrão (título;ISBN1,ISBN2,...)
    public void carregarDeArquivo(String caminho) throws IOException {
        index.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminho, java.nio.charset.StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";", 2);
                if (partes.length == 2) {
                    String titulo = partes[0].trim();
                    String[] isbns = partes[1].split(",");
                    Set<String> isbnSet = new HashSet<>();
                    for (String isbn : isbns) {
                        isbnSet.add(isbn.trim());
                    }
                    index.put(titulo, isbnSet);
                }
            }
        }
    }

    // Gera o índice secundário a partir do arquivo de dados (mangas.txt)
    public void gerarIndiceDeArquivoDeDados(String caminhoMangas) throws IOException {
        index.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoMangas))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Verificar se o registro não foi removido logicamente
                if (!linha.trim().startsWith("*")) {
                    String[] partes = linha.split(";", 3); // título é o segundo campo
                    if (partes.length > 1) {
                        String titulo = partes[1].trim();
                        String isbn = partes[0].trim();
                        add(titulo, isbn);
                    }
                }
            }
        }
    }
} 