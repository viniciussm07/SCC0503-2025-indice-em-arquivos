package app.cli;

import app.controller.MangaController;
import app.index.PrimaryIndex;
import app.index.SecondaryIndex;
import java.util.Scanner;
import java.io.*;

public class CLI {
    public void run() {
        String arquivoDeDados = "data/mangas.txt";
        PrimaryIndex primaryIndex = new PrimaryIndex();
        SecondaryIndex secondaryIndex = new SecondaryIndex();
        try {
            // Gerar e imprimir índice primário
            primaryIndex.gerarIndiceDeArquivoDeDados(arquivoDeDados);
            System.out.println("Índice primário (ISBN;offset):");
            for (var entry : primaryIndex.getIndex().entrySet()) {
                System.out.println(entry.getKey() + ";" + entry.getValue());
            }
            primaryIndex.salvarParaArquivo("data/indice_primario.idx");
            System.out.println("Índice primário salvo em data/indice_primario.idx");

            // Gerar índice secundário a partir do mangas.txt
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivoDeDados))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    String[] partes = linha.split(";", 3); // título é o segundo campo
                    if (partes.length > 1) {
                        String titulo = partes[1].trim();
                        String isbn = partes[0].trim();
                        secondaryIndex.add(titulo, isbn);
                    }
                }
            }
            // Imprimir índice secundário
            System.out.println("\nÍndice secundário (Título;ISBNs):");
            for (var entry : secondaryIndex.getIndex().entrySet()) {
                System.out.println(entry.getKey() + ";" + String.join(",", entry.getValue()));
            }
            secondaryIndex.salvarParaArquivo("data/indice_secundario.idx");
            System.out.println("Índice secundário salvo em data/indice_secundario.idx");
        } catch (Exception e) {
            System.out.println("Erro ao gerar ou imprimir os índices: " + e.getMessage());
            e.printStackTrace();
        }
        // PrimaryIndex primaryIndex = new PrimaryIndex();
        // SecondaryIndex secondaryIndex = new SecondaryIndex();
        // MangaController controller = new MangaController(primaryIndex, secondaryIndex);

        // Scanner scanner = new Scanner(System.in);
        // int opcao;
        // do {
        //     System.out.println("==== Organizador de Mangás ====");
        //     System.out.println("1. Cadastrar mangá");
        //     System.out.println("2. Buscar mangá por ISBN");
        //     System.out.println("3. Buscar mangá por título");
        //     System.out.println("4. Atualizar mangá");
        //     System.out.println("5. Remover mangá");
        //     System.out.println("0. Sair");
        //     System.out.print("Escolha uma opção: ");
        //     opcao = scanner.nextInt();
        //     scanner.nextLine(); // Limpar buffer

        //     switch (opcao) {
        //         case 1:
        //             // controller.criarManga(...);
        //             break;
        //         case 2:
        //             // controller.lerMangaPorISBN(...);
        //             break;
        //         case 3:
        //             // controller.lerMangasPorTitulo(...);
        //             break;
        //         case 4:
        //             // controller.atualizarManga(...);
        //             break;
        //         case 5:
        //             // controller.deletarManga(...);
        //             break;
        //         case 0:
        //             System.out.println("Saindo...");
        //             break;
        //         default:
        //             System.out.println("Opção inválida!");
        //     }
        // } while (opcao != 0);

        // scanner.close();
    }
} 