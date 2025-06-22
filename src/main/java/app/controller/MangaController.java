package app.controller;

import app.model.Manga;
import app.index.PrimaryIndex;
import app.index.SecondaryIndex;
import java.util.List;
import java.util.ArrayList;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class MangaController {
    private PrimaryIndex primaryIndex;
    private SecondaryIndex secondaryIndex;
    private static final String DATA_FILE_PATH = "data/mangas.txt";

    public MangaController(PrimaryIndex primaryIndex, SecondaryIndex secondaryIndex) {
        this.primaryIndex = primaryIndex;
        this.secondaryIndex = secondaryIndex;
    }

    public void criarManga(Manga manga) {
        if (manga == null) {
            throw new IllegalArgumentException("Manga não pode ser null");
        }
        
        // Validações de dados
        if (manga.getIsbn() == null || manga.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN é obrigatório");
        }
        
        if (manga.getTitulo() == null || manga.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }
        
        if (manga.getAutores() == null || manga.getAutores().trim().isEmpty()) {
            throw new IllegalArgumentException("Autores são obrigatórios");
        }
        
        if (manga.getAnoInicio() <= 0) {
            throw new IllegalArgumentException("Ano de início deve ser válido");
        }
        
        if (manga.getAnoFim() != null && manga.getAnoFim() < manga.getAnoInicio()) {
            throw new IllegalArgumentException("Ano de fim não pode ser anterior ao ano de início");
        }
        
        if (manga.getQtdVolumes() <= 0) {
            throw new IllegalArgumentException("Quantidade de volumes deve ser maior que zero");
        }
        
        // Validar consistência entre volumes adquiridos e quantidade
        if (manga.getVolumesAdquiridos() != null) {
            for (Integer volume : manga.getVolumesAdquiridos()) {
                if (volume <= 0 || volume > manga.getQtdVolumes()) {
                    throw new IllegalArgumentException("Volume " + volume + " é inválido (deve estar entre 1 e " + manga.getQtdVolumes() + ")");
                }
            }
            
            // Verificar se a quantidade de volumes adquiridos está correta
            if (manga.getQtdVolumesAdquiridos() != manga.getVolumesAdquiridos().size()) {
                throw new IllegalArgumentException("Quantidade de volumes adquiridos (" + manga.getQtdVolumesAdquiridos() + 
                                                   ") não corresponde à lista de volumes (" + manga.getVolumesAdquiridos().size() + ")");
            }
        }
        
        // Verificar se o ISBN já existe
        if (primaryIndex.get(manga.getIsbn()) != null) {
            throw new IllegalArgumentException("ISBN '" + manga.getIsbn() + "' já existe no sistema");
        }
        
        try {
            // Formatar a linha do mangá
            String linhaManga = formatarLinhaManga(manga);
            
            // Adicionar ao arquivo de dados
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE_PATH, true))) {
                writer.write(linhaManga);
                writer.newLine();
            }
            
            // Atualizar índices
            // Índice primário - usar o tamanho do arquivo como offset
            long offset = Files.size(Paths.get(DATA_FILE_PATH)) - linhaManga.length() - 1; // -1 para a quebra de linha
            primaryIndex.add(manga.getIsbn(), offset);
            
            // Índice secundário
            secondaryIndex.add(manga.getTitulo(), manga.getIsbn());
            
            // Salvar índices atualizados
            primaryIndex.salvarParaArquivo("data/indice_primario.idx");
            secondaryIndex.salvarParaArquivo("data/indice_secundario.idx");
            
            System.out.println("Mangá '" + manga.getTitulo() + "' cadastrado com sucesso!");
            
        } catch (IOException e) {
            System.err.println("Erro ao cadastrar mangá: " + e.getMessage());
            throw new RuntimeException("Falha ao cadastrar mangá", e);
        }
    }

    public Manga lerMangaPorISBN(String isbn) {
        try (RandomAccessFile raf = new RandomAccessFile(DATA_FILE_PATH, "r")) {
            Long offset = primaryIndex.get(isbn);
            if (offset == null) {
                return null; // ISBN não encontrado
            }
            
            raf.seek(offset);
            String linha = raf.readLine();
            if (linha != null) {
                // Converter de ISO-8859-1 para UTF-8 se necessário
                linha = new String(linha.getBytes("ISO-8859-1"), StandardCharsets.UTF_8);
                Manga manga = parsearLinhaManga(linha);
                // Não retornar mangás removidos
                return (manga != null && !manga.isRemovido()) ? manga : null;
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler mangá por ISBN: " + e.getMessage());
        }
        return null;
    }

    public List<Manga> lerMangasPorTitulo(String titulo) {
        List<Manga> mangas = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(DATA_FILE_PATH, "r")) {
            // Buscar ISBNs associados ao título no índice secundário
            var isbns = secondaryIndex.get(titulo);
            if (isbns.isEmpty()) {
                return mangas; // Título não encontrado
            }
            
            // Para cada ISBN encontrado, buscar o mangá correspondente
            for (String isbn : isbns) {
                Manga manga = lerMangaPorISBN(isbn);
                if (manga != null) {
                    mangas.add(manga);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler mangás por título: " + e.getMessage());
        }
        return mangas;
    }

    public void atualizarManga(Manga manga) {
        if (manga == null) {
            throw new IllegalArgumentException("Manga não pode ser null");
        }
        
        if (manga.getIsbn() == null || manga.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN é obrigatório");
        }
        
        // Verificar se o mangá existe
        Manga mangaExistente = lerMangaPorISBN(manga.getIsbn());
        if (mangaExistente == null) {
            throw new IllegalArgumentException("Mangá com ISBN '" + manga.getIsbn() + "' não encontrado");
        }
        
        // Validações de dados (mesmas do cadastro)
        if (manga.getTitulo() == null || manga.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }
        
        if (manga.getAutores() == null || manga.getAutores().trim().isEmpty()) {
            throw new IllegalArgumentException("Autores são obrigatórios");
        }
        
        if (manga.getAnoInicio() <= 0) {
            throw new IllegalArgumentException("Ano de início deve ser válido");
        }
        
        if (manga.getAnoFim() != null && manga.getAnoFim() < manga.getAnoInicio()) {
            throw new IllegalArgumentException("Ano de fim não pode ser anterior ao ano de início");
        }
        
        if (manga.getQtdVolumes() <= 0) {
            throw new IllegalArgumentException("Quantidade de volumes deve ser maior que zero");
        }
        
        // Validar consistência entre volumes adquiridos e quantidade
        if (manga.getVolumesAdquiridos() != null) {
            for (Integer volume : manga.getVolumesAdquiridos()) {
                if (volume <= 0 || volume > manga.getQtdVolumes()) {
                    throw new IllegalArgumentException("Volume " + volume + " é inválido (deve estar entre 1 e " + manga.getQtdVolumes() + ")");
                }
            }
            
            if (manga.getQtdVolumesAdquiridos() != manga.getVolumesAdquiridos().size()) {
                throw new IllegalArgumentException("Quantidade de volumes adquiridos (" + manga.getQtdVolumesAdquiridos() + 
                                                   ") não corresponde à lista de volumes (" + manga.getVolumesAdquiridos().size() + ")");
            }
        }
        
        try {
            // 1. Remover logicamente o mangá existente
            reescreverLinhaComRemocao(mangaExistente);
            
            // 2. Remover dos índices
            primaryIndex.remove(manga.getIsbn());
            secondaryIndex.remove(mangaExistente.getTitulo(), manga.getIsbn());
            
            // 3. Adicionar o novo mangá ao arquivo
            String linhaManga = formatarLinhaManga(manga);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE_PATH, true))) {
                writer.write(linhaManga);
                writer.newLine();
            }
            
            // 4. Atualizar índices com o novo mangá
            long offset = Files.size(Paths.get(DATA_FILE_PATH)) - linhaManga.length() - 1; // -1 para a quebra de linha
            primaryIndex.add(manga.getIsbn(), offset);
            secondaryIndex.add(manga.getTitulo(), manga.getIsbn());
            
            // 5. Salvar índices atualizados
            primaryIndex.salvarParaArquivo("data/indice_primario.idx");
            secondaryIndex.salvarParaArquivo("data/indice_secundario.idx");
            
            System.out.println("Mangá '" + manga.getTitulo() + "' atualizado com sucesso!");
            
        } catch (IOException e) {
            System.err.println("Erro ao atualizar mangá: " + e.getMessage());
            throw new RuntimeException("Falha ao atualizar mangá", e);
        }
    }

    public void deletarManga(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN é obrigatório");
        }
        
        // Verificar se o mangá existe
        Manga manga = lerMangaPorISBN(isbn);
        if (manga == null) {
            throw new IllegalArgumentException("Mangá com ISBN '" + isbn + "' não encontrado");
        }
        
        try {
            // Reescrever a linha no arquivo com '*' no início do ISBN
            reescreverLinhaComRemocao(manga);
            
            // Remover dos índices
            primaryIndex.remove(isbn);
            secondaryIndex.remove(manga.getTitulo(), isbn);
            
            // Salvar índices atualizados
            primaryIndex.salvarParaArquivo("data/indice_primario.idx");
            secondaryIndex.salvarParaArquivo("data/indice_secundario.idx");
            
            System.out.println("Mangá '" + manga.getTitulo() + "' removido com sucesso!");
            
        } catch (IOException e) {
            System.err.println("Erro ao remover mangá: " + e.getMessage());
            throw new RuntimeException("Falha ao remover mangá", e);
        }
    }

    /**
     * Parseia uma linha do arquivo mangas.txt e retorna um objeto Manga
     * Formato: ISBN; Título; Autores; AnoInício; AnoFim; Gênero; Revista; Editora; AnoEdição; QtdVolumes; QtdVolumesAdquiridos; [VolumesAdquiridos]
     */
    private Manga parsearLinhaManga(String linha) {
        try {
            String[] campos = linha.split(";", -1); // -1 para incluir campos vazios
            
            if (campos.length < 12) {
                System.err.println("Linha malformada: " + linha);
                return null;
            }
            
            String isbn = campos[0].trim();
            
            // Verificar se o registro foi removido logicamente
            boolean removido = isbn.startsWith("*");
            if (removido) {
                isbn = isbn.substring(1); // Remove o '*' para obter o ISBN real
            }
            
            String titulo = campos[1].trim();
            String autores = campos[2].trim();
            int anoInicio = Integer.parseInt(campos[3].trim());
            
            // AnoFim pode ser "-" (em andamento)
            Integer anoFim = null;
            if (!campos[4].trim().equals("-")) {
                anoFim = Integer.parseInt(campos[4].trim());
            }
            
            String genero = campos[5].trim();
            String revista = campos[6].trim();
            String editora = campos[7].trim();
            int anoEdicao = Integer.parseInt(campos[8].trim());
            int qtdVolumes = Integer.parseInt(campos[9].trim());
            int qtdVolumesAdquiridos = Integer.parseInt(campos[10].trim());
            
            // Parsear lista de volumes adquiridos
            List<Integer> volumesAdquiridos = new ArrayList<>();
            String volumesStr = campos[11].trim();
            if (volumesStr.startsWith("[") && volumesStr.endsWith("]")) {
                volumesStr = volumesStr.substring(1, volumesStr.length() - 1);
                if (!volumesStr.isEmpty()) {
                    String[] volumes = volumesStr.split(",");
                    for (String volume : volumes) {
                        volumesAdquiridos.add(Integer.parseInt(volume.trim()));
                    }
                }
            }
            
            Manga manga = new Manga(isbn, titulo, autores, anoInicio, anoFim, genero, 
                                   revista, editora, anoEdicao, qtdVolumes, qtdVolumesAdquiridos, 
                                   volumesAdquiridos);
            manga.setRemovido(removido);
            
            return manga;
            
        } catch (NumberFormatException e) {
            System.err.println("Erro ao parsear números na linha: " + linha);
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao parsear linha: " + linha + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Busca mangás por parte do nome (busca parcial, case-insensitive)
     */
    public List<Manga> buscarMangasPorParteDoTitulo(String termo) {
        List<Manga> mangas = new ArrayList<>();
        if (termo == null || termo.isBlank()) return mangas;
        String termoLower = termo.toLowerCase();
        try (RandomAccessFile raf = new RandomAccessFile(DATA_FILE_PATH, "r")) {
            for (String titulo : secondaryIndex.getIndex().keySet()) {
                if (titulo.toLowerCase().contains(termoLower)) {
                    // Para cada título correspondente, buscar todos os ISBNs
                    var isbns = secondaryIndex.get(titulo);
                    for (String isbn : isbns) {
                        Manga manga = lerMangaPorISBN(isbn);
                        if (manga != null) {
                            mangas.add(manga);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro na busca parcial por título: " + e.getMessage());
        }
        return mangas;
    }

    /**
     * Formata um objeto Manga para o formato de linha do arquivo
     * Formato: ISBN; Título; Autores; AnoInício; AnoFim; Gênero; Revista; Editora; AnoEdição; QtdVolumes; QtdVolumesAdquiridos; [VolumesAdquiridos]
     */
    private String formatarLinhaManga(Manga manga) {
        StringBuilder sb = new StringBuilder();
        
        // Substituir primeiro caractere do ISBN por '*' se removido
        String isbn = manga.getIsbn();
        if (manga.isRemovido()) {
            isbn = "*" + isbn.substring(1);
        }
        
        sb.append(isbn).append("; ");
        sb.append(manga.getTitulo()).append("; ");
        sb.append(manga.getAutores()).append("; ");
        sb.append(manga.getAnoInicio()).append("; ");
        
        // AnoFim pode ser null (em andamento)
        if (manga.getAnoFim() != null) {
            sb.append(manga.getAnoFim());
        } else {
            sb.append("-");
        }
        sb.append("; ");
        
        sb.append(manga.getGenero()).append("; ");
        sb.append(manga.getRevista()).append("; ");
        sb.append(manga.getEditora()).append("; ");
        sb.append(manga.getAnoEdicao()).append("; ");
        sb.append(manga.getQtdVolumes()).append("; ");
        sb.append(manga.getQtdVolumesAdquiridos()).append("; ");
        
        // Formatar lista de volumes adquiridos
        sb.append("[");
        if (manga.getVolumesAdquiridos() != null && !manga.getVolumesAdquiridos().isEmpty()) {
            for (int i = 0; i < manga.getVolumesAdquiridos().size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(manga.getVolumesAdquiridos().get(i));
            }
        }
        sb.append("]");
        
        return sb.toString();
    }

    /**
     * Reescreve uma linha específica no arquivo de dados
     */
    private void reescreverLinhaManga(Manga manga) throws IOException {
        // Ler todo o arquivo
        List<String> linhas = Files.readAllLines(Paths.get(DATA_FILE_PATH), StandardCharsets.UTF_8);
        
        // Encontrar e atualizar a linha do mangá
        for (int i = 0; i < linhas.size(); i++) {
            String linha = linhas.get(i);
            String[] campos = linha.split(";", 2);
            if (campos.length > 0 && campos[0].trim().equals(manga.getIsbn())) {
                // Substituir a linha
                linhas.set(i, formatarLinhaManga(manga));
                break;
            }
        }
        
        // Reescrever o arquivo
        Files.write(Paths.get(DATA_FILE_PATH), linhas, StandardCharsets.UTF_8);
    }

    private void reescreverLinhaComRemocao(Manga manga) throws IOException {
        // Ler todo o arquivo
        List<String> linhas = Files.readAllLines(Paths.get(DATA_FILE_PATH), StandardCharsets.UTF_8);
        
        // Encontrar e atualizar a linha do mangá
        for (int i = 0; i < linhas.size(); i++) {
            String linha = linhas.get(i);
            String[] campos = linha.split(";", 2);
            if (campos.length > 0 && campos[0].trim().equals(manga.getIsbn())) {
                // Substituir o primeiro caractere do ISBN por '*'
                String isbnOriginal = campos[0].trim();
                String isbnRemovido = "*" + isbnOriginal.substring(1);
                String linhaRemovida = isbnRemovido + linha.substring(isbnOriginal.length());
                linhas.set(i, linhaRemovida);
                break;
            }
        }
        
        // Reescrever o arquivo
        Files.write(Paths.get(DATA_FILE_PATH), linhas, StandardCharsets.UTF_8);
    }

    /**
     * Compacta o arquivo removendo fisicamente as linhas marcadas com '*'
     * e regenera os índices
     */
    public void compactarArquivo() {
        try {
            System.out.println("Iniciando compactação do arquivo...");
            
            // Ler todas as linhas do arquivo
            List<String> linhas = Files.readAllLines(Paths.get(DATA_FILE_PATH), StandardCharsets.UTF_8);
            List<String> linhasCompactadas = new ArrayList<>();
            int registrosRemovidos = 0;
            
            // Filtrar linhas removidas logicamente
            for (String linha : linhas) {
                if (!linha.trim().startsWith("*")) {
                    linhasCompactadas.add(linha);
                } else {
                    registrosRemovidos++;
                }
            }
            
            // Reescrever o arquivo com apenas as linhas válidas
            Files.write(Paths.get(DATA_FILE_PATH), linhasCompactadas, StandardCharsets.UTF_8);
            
            // Regenerar índices a partir do arquivo compactado
            System.out.println("Regenerando índices...");
            primaryIndex.gerarIndiceDeArquivoDeDados(DATA_FILE_PATH);
            secondaryIndex.gerarIndiceDeArquivoDeDados(DATA_FILE_PATH);
            
            // Salvar índices atualizados
            primaryIndex.salvarParaArquivo("data/indice_primario.idx");
            secondaryIndex.salvarParaArquivo("data/indice_secundario.idx");
            
            System.out.println("Compactação concluída!");
            System.out.println("Registros removidos: " + registrosRemovidos);
            System.out.println("Registros restantes: " + linhasCompactadas.size());
            
        } catch (IOException e) {
            System.err.println("Erro durante a compactação: " + e.getMessage());
            throw new RuntimeException("Falha na compactação", e);
        }
    }
} 