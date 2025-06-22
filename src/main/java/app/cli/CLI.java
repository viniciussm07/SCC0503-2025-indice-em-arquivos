package app.cli;

import app.controller.MangaController;
import app.index.PrimaryIndex;
import app.index.SecondaryIndex;
import app.model.Manga;
import java.util.Scanner;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class CLI {
    public void run() {
        String arquivoDeDados = "data/mangas.txt";
        PrimaryIndex primaryIndex = new PrimaryIndex();
        SecondaryIndex secondaryIndex = new SecondaryIndex();
        
        try {
            // Carregar índices existentes
            System.out.println("Carregando índices existentes...");
            
            // Gerar índice primário sempre a partir do arquivo de dados
            System.out.println("Gerando índice primário...");
            primaryIndex.gerarIndiceDeArquivoDeDados(arquivoDeDados);
            primaryIndex.salvarParaArquivo("data/indice_primario.idx");
            System.out.println("Índice primário gerado e salvo.");
            
            // Gerar índice secundário sempre a partir do arquivo de dados
            System.out.println("Gerando índice secundário...");
            secondaryIndex.gerarIndiceDeArquivoDeDados(arquivoDeDados);
            secondaryIndex.salvarParaArquivo("data/indice_secundario.idx");
            System.out.println("Índice secundário gerado e salvo.");
            
            System.out.println("Índices carregados com sucesso!\n");
            
        } catch (Exception e) {
            System.out.println("Erro ao carregar os índices: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Menu interativo
        MangaController controller = new MangaController(primaryIndex, secondaryIndex);
        Scanner scanner = new Scanner(System.in);
        int opcao;
        
        // Verificar se há console disponível (evita erro em execução não-interativa)
        if (System.console() != null) {
            do {
                System.out.println("==== Organizador de Mangás ====");
                System.out.println("1. Cadastrar mangá");
                System.out.println("2. Buscar mangá por ISBN");
                System.out.println("3. Buscar mangá por título");
                System.out.println("4. Atualizar mangá");
                System.out.println("5. Remover mangá");
                System.out.println("6. Compactar arquivo");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                
                try {
                    opcao = scanner.nextInt();
                    scanner.nextLine(); // Limpar buffer

                    switch (opcao) {
                        case 1:
                            cadastrarManga(scanner, controller);
                            break;
                        case 2:
                            buscarMangaPorISBN(scanner, controller);
                            break;
                        case 3:
                            buscarMangaPorTitulo(scanner, controller);
                            break;
                        case 4:
                            atualizarManga(scanner, controller);
                            break;
                        case 5:
                            removerManga(scanner, controller);
                            break;
                        case 6:
                            compactarArquivo(scanner, controller);
                            break;
                        case 0:
                            System.out.println("Saindo...");
                            break;
                        default:
                            System.out.println("Opção inválida!");
                    }
                } catch (java.util.NoSuchElementException e) {
                    System.out.println("Erro: Não foi possível ler a entrada. Saindo...");
                    break;
                }
            } while (opcao != 0);
        } else {
            System.out.println("Execução não-interativa detectada. Índices carregados com sucesso!");
        }

        scanner.close();
    }

    private void buscarMangaPorISBN(Scanner scanner, MangaController controller) {
        System.out.print("Digite o ISBN do mangá: ");
        String isbn = scanner.nextLine().trim();
        
        if (isbn.isEmpty()) {
            System.out.println("ISBN não pode estar vazio!");
            return;
        }
        
        Manga manga = controller.lerMangaPorISBN(isbn);
        if (manga != null) {
            System.out.println("\n" + manga);
        } else {
            System.out.println("Mangá com ISBN '" + isbn + "' não encontrado.");
        }
    }

    private void buscarMangaPorTitulo(Scanner scanner, MangaController controller) {
        System.out.print("Digite parte do título do mangá: ");
        String termo = scanner.nextLine().trim();
        
        if (termo.isEmpty()) {
            System.out.println("O termo não pode estar vazio!");
            return;
        }
        
        var mangas = controller.buscarMangasPorParteDoTitulo(termo);
        if (!mangas.isEmpty()) {
            System.out.println("\nMangás encontrados:");
            for (Manga manga : mangas) {
                System.out.println(manga);
                System.out.println(); // Linha em branco entre mangás
            }
        } else {
            System.out.println("Nenhum mangá encontrado contendo '" + termo + "' no título.");
        }
    }

    private void cadastrarManga(Scanner scanner, MangaController controller) {
        System.out.println("\n=== Cadastrar Novo Mangá ===");
        
        try {
            // Coletar ISBN com validação
            String isbn;
            do {
                System.out.print("ISBN: ");
                isbn = scanner.nextLine().trim();
                if (isbn.isEmpty()) {
                    System.out.println("❌ ISBN é obrigatório! Tente novamente.");
                } else if (controller.lerMangaPorISBN(isbn) != null) {
                    System.out.println("❌ ISBN '" + isbn + "' já existe no sistema! Tente outro.");
                    isbn = ""; // Força nova tentativa
                }
            } while (isbn.isEmpty());
            
            // Coletar título com validação
            String titulo;
            do {
                System.out.print("Título: ");
                titulo = scanner.nextLine().trim();
                if (titulo.isEmpty()) {
                    System.out.println("❌ Título é obrigatório! Tente novamente.");
                }
            } while (titulo.isEmpty());
            
            // Coletar autores com validação
            String autores;
            do {
                System.out.print("Autores: ");
                autores = scanner.nextLine().trim();
                if (autores.isEmpty()) {
                    System.out.println("❌ Autores são obrigatórios! Tente novamente.");
                }
            } while (autores.isEmpty());
            
            // Coletar ano de início com validação
            int anoInicio;
            do {
                System.out.print("Ano de início: ");
                try {
                    anoInicio = Integer.parseInt(scanner.nextLine().trim());
                    if (anoInicio <= 0) {
                        System.out.println("❌ Ano deve ser maior que zero! Tente novamente.");
                        anoInicio = 0; // Força nova tentativa
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Digite um ano válido! Tente novamente.");
                    anoInicio = 0; // Força nova tentativa
                }
            } while (anoInicio <= 0);
            
            // Coletar ano de fim com validação
            Integer anoFim = null;
            System.out.print("Ano de fim (deixe vazio se ainda está em andamento): ");
            String anoFimStr = scanner.nextLine().trim();
            if (!anoFimStr.isEmpty()) {
                boolean anoFimValido = false;
                do {
                    try {
                        anoFim = Integer.parseInt(anoFimStr);
                        if (anoFim < anoInicio) {
                            System.out.println("❌ Ano de fim não pode ser anterior ao ano de início! Tente novamente.");
                            System.out.print("Ano de fim: ");
                            anoFimStr = scanner.nextLine().trim();
                            if (anoFimStr.isEmpty()) {
                                anoFim = null;
                                anoFimValido = true;
                            }
                        } else {
                            anoFimValido = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Ano inválido! Tente novamente.");
                        System.out.print("Ano de fim: ");
                        anoFimStr = scanner.nextLine().trim();
                        if (anoFimStr.isEmpty()) {
                            anoFim = null;
                            anoFimValido = true;
                        }
                    }
                } while (!anoFimValido);
            }
            
            // Coletar gênero com validação
            String genero;
            do {
                System.out.print("Gênero: ");
                genero = scanner.nextLine().trim();
                if (genero.isEmpty()) {
                    System.out.println("❌ Gênero é obrigatório! Tente novamente.");
                }
            } while (genero.isEmpty());
            
            // Coletar revista com validação
            String revista;
            do {
                System.out.print("Revista: ");
                revista = scanner.nextLine().trim();
                if (revista.isEmpty()) {
                    System.out.println("❌ Revista é obrigatória! Tente novamente.");
                }
            } while (revista.isEmpty());
            
            // Coletar editora com validação
            String editora;
            do {
                System.out.print("Editora: ");
                editora = scanner.nextLine().trim();
                if (editora.isEmpty()) {
                    System.out.println("❌ Editora é obrigatória! Tente novamente.");
                }
            } while (editora.isEmpty());
            
            // Coletar ano da edição com validação
            int anoEdicao;
            do {
                System.out.print("Ano da edição: ");
                try {
                    anoEdicao = Integer.parseInt(scanner.nextLine().trim());
                    if (anoEdicao <= 0) {
                        System.out.println("❌ Ano deve ser maior que zero! Tente novamente.");
                        anoEdicao = 0; // Força nova tentativa
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Digite um ano válido! Tente novamente.");
                    anoEdicao = 0; // Força nova tentativa
                }
            } while (anoEdicao <= 0);
            
            // Coletar quantidade total de volumes com validação
            int qtdVolumes;
            do {
                System.out.print("Quantidade total de volumes: ");
                try {
                    qtdVolumes = Integer.parseInt(scanner.nextLine().trim());
                    if (qtdVolumes <= 0) {
                        System.out.println("❌ Quantidade deve ser maior que zero! Tente novamente.");
                        qtdVolumes = 0; // Força nova tentativa
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Digite um número válido! Tente novamente.");
                    qtdVolumes = 0; // Força nova tentativa
                }
            } while (qtdVolumes <= 0);
            
            // Coletar volumes adquiridos com validação
            List<Integer> volumesAdquiridos = new ArrayList<>();
            System.out.print("Volumes adquiridos (separados por vírgula, ex: 1,2,3): ");
            String volumesStr = scanner.nextLine().trim();
            if (!volumesStr.isEmpty()) {
                boolean volumesValidos = false;
                do {
                    try {
                        volumesAdquiridos.clear(); // Limpar lista anterior
                        String[] volumes = volumesStr.split(",");
                        for (String volume : volumes) {
                            int vol = Integer.parseInt(volume.trim());
                            if (vol <= 0 || vol > qtdVolumes) {
                                System.out.println("❌ Volume " + vol + " é inválido (deve estar entre 1 e " + qtdVolumes + ")!");
                                volumesValidos = false;
                                break;
                            }
                            if (volumesAdquiridos.contains(vol)) {
                                System.out.println("❌ Volume " + vol + " já foi informado!");
                                volumesValidos = false;
                                break;
                            }
                            volumesAdquiridos.add(vol);
                            volumesValidos = true;
                        }
                        if (!volumesValidos) {
                            System.out.print("Digite novamente os volumes adquiridos: ");
                            volumesStr = scanner.nextLine().trim();
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Formato inválido! Use números separados por vírgula.");
                        System.out.print("Digite novamente os volumes adquiridos: ");
                        volumesStr = scanner.nextLine().trim();
                        volumesValidos = false;
                    }
                } while (!volumesValidos);
            }
            
            // Calcular quantidade de volumes adquiridos automaticamente
            int qtdVolumesAdquiridos = volumesAdquiridos.size();
            
            // Mostrar resumo antes de confirmar
            System.out.println("\n=== Resumo do Mangá ===");
            System.out.println("ISBN: " + isbn);
            System.out.println("Título: " + titulo);
            System.out.println("Autores: " + autores);
            System.out.println("Período: " + anoInicio + (anoFim != null ? " - " + anoFim : " - Em andamento"));
            System.out.println("Gênero: " + genero);
            System.out.println("Revista: " + revista);
            System.out.println("Editora: " + editora + " (" + anoEdicao + ")");
            System.out.println("Volumes: " + qtdVolumesAdquiridos + "/" + qtdVolumes);
            if (qtdVolumesAdquiridos > 0) {
                System.out.println("Volumes adquiridos: " + volumesAdquiridos);
            }
            
            System.out.print("\nConfirmar cadastro? (s/n): ");
            String confirmacao = scanner.nextLine().trim().toLowerCase();
            if (!confirmacao.equals("s") && !confirmacao.equals("sim")) {
                System.out.println("Cadastro cancelado.");
                return;
            }
            
            // Criar objeto Manga
            Manga manga = new Manga(isbn, titulo, autores, anoInicio, anoFim, genero, 
                                   revista, editora, anoEdicao, qtdVolumes, qtdVolumesAdquiridos, 
                                   volumesAdquiridos);
            
            // Cadastrar no sistema
            controller.criarManga(manga);
            
        } catch (Exception e) {
            System.out.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    private void removerManga(Scanner scanner, MangaController controller) {
        System.out.println("\n=== Remover Mangá ===");
        
        // Primeiro, buscar o mangá para mostrar informações
        System.out.print("Digite o ISBN do mangá a ser removido: ");
        String isbn = scanner.nextLine().trim();
        
        if (isbn.isEmpty()) {
            System.out.println("❌ ISBN não pode estar vazio!");
            return;
        }
        
        // Buscar o mangá para mostrar informações
        Manga manga = controller.lerMangaPorISBN(isbn);
        if (manga == null) {
            System.out.println("❌ Mangá com ISBN '" + isbn + "' não encontrado.");
            return;
        }
        
        // Mostrar informações do mangá
        System.out.println("\nMangá encontrado:");
        System.out.println(manga);
        
        // Confirmar remoção
        System.out.print("\nTem certeza que deseja remover este mangá? (s/n): ");
        String confirmacao = scanner.nextLine().trim().toLowerCase();
        if (!confirmacao.equals("s") && !confirmacao.equals("sim")) {
            System.out.println("Remoção cancelada.");
            return;
        }
        
        try {
            controller.deletarManga(isbn);
            System.out.println("✅ Mangá removido com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    private void atualizarManga(Scanner scanner, MangaController controller) {
        System.out.println("\n=== Atualizar Mangá ===");
        
        // Primeiro, buscar o mangá a ser atualizado
        System.out.print("Digite o ISBN do mangá a ser atualizado: ");
        String isbn = scanner.nextLine().trim();
        
        if (isbn.isEmpty()) {
            System.out.println("❌ ISBN não pode estar vazio!");
            return;
        }
        
        // Buscar o mangá existente
        Manga mangaExistente = controller.lerMangaPorISBN(isbn);
        if (mangaExistente == null) {
            System.out.println("❌ Mangá com ISBN '" + isbn + "' não encontrado.");
            return;
        }
        
        // Mostrar dados atuais
        System.out.println("\nDados atuais do mangá:");
        System.out.println(mangaExistente);
        
        System.out.println("\nDigite os novos dados (deixe vazio para manter o valor atual):");
        
        try {
            // Coletar novos dados com valores padrão dos dados atuais
            System.out.print("Título [" + mangaExistente.getTitulo() + "]: ");
            String titulo = scanner.nextLine().trim();
            if (titulo.isEmpty()) titulo = mangaExistente.getTitulo();
            
            System.out.print("Autores [" + mangaExistente.getAutores() + "]: ");
            String autores = scanner.nextLine().trim();
            if (autores.isEmpty()) autores = mangaExistente.getAutores();
            
            System.out.print("Ano de início [" + mangaExistente.getAnoInicio() + "]: ");
            String anoInicioStr = scanner.nextLine().trim();
            int anoInicio = anoInicioStr.isEmpty() ? mangaExistente.getAnoInicio() : Integer.parseInt(anoInicioStr);
            
            System.out.print("Ano de fim [" + (mangaExistente.getAnoFim() != null ? mangaExistente.getAnoFim() : "em andamento") + "]: ");
            String anoFimStr = scanner.nextLine().trim();
            Integer anoFim = null;
            if (!anoFimStr.isEmpty()) {
                if (anoFimStr.equalsIgnoreCase("em andamento")) {
                    anoFim = null;
                } else {
                    anoFim = Integer.parseInt(anoFimStr);
                }
            } else {
                anoFim = mangaExistente.getAnoFim();
            }
            
            System.out.print("Gênero [" + mangaExistente.getGenero() + "]: ");
            String genero = scanner.nextLine().trim();
            if (genero.isEmpty()) genero = mangaExistente.getGenero();
            
            System.out.print("Revista [" + mangaExistente.getRevista() + "]: ");
            String revista = scanner.nextLine().trim();
            if (revista.isEmpty()) revista = mangaExistente.getRevista();
            
            System.out.print("Editora [" + mangaExistente.getEditora() + "]: ");
            String editora = scanner.nextLine().trim();
            if (editora.isEmpty()) editora = mangaExistente.getEditora();
            
            System.out.print("Ano da edição [" + mangaExistente.getAnoEdicao() + "]: ");
            String anoEdicaoStr = scanner.nextLine().trim();
            int anoEdicao = anoEdicaoStr.isEmpty() ? mangaExistente.getAnoEdicao() : Integer.parseInt(anoEdicaoStr);
            
            System.out.print("Quantidade total de volumes [" + mangaExistente.getQtdVolumes() + "]: ");
            String qtdVolumesStr = scanner.nextLine().trim();
            int qtdVolumes = qtdVolumesStr.isEmpty() ? mangaExistente.getQtdVolumes() : Integer.parseInt(qtdVolumesStr);
            
            // Coletar volumes adquiridos
            List<Integer> volumesAdquiridos = new ArrayList<>();
            System.out.print("Volumes adquiridos [" + mangaExistente.getVolumesAdquiridos() + "]: ");
            String volumesStr = scanner.nextLine().trim();
            if (volumesStr.isEmpty()) {
                volumesAdquiridos = new ArrayList<>(mangaExistente.getVolumesAdquiridos());
            } else {
                String[] volumes = volumesStr.split(",");
                for (String volume : volumes) {
                    volumesAdquiridos.add(Integer.parseInt(volume.trim()));
                }
            }
            
            // Calcular quantidade de volumes adquiridos automaticamente
            int qtdVolumesAdquiridos = volumesAdquiridos.size();
            
            // Criar objeto Manga atualizado
            Manga mangaAtualizado = new Manga(isbn, titulo, autores, anoInicio, anoFim, genero, 
                                             revista, editora, anoEdicao, qtdVolumes, qtdVolumesAdquiridos, 
                                             volumesAdquiridos);
            
            // Mostrar resumo das alterações
            System.out.println("\n=== Resumo das Alterações ===");
            System.out.println("ISBN: " + isbn + " (não pode ser alterado)");
            if (!titulo.equals(mangaExistente.getTitulo())) {
                System.out.println("Título: " + mangaExistente.getTitulo() + " → " + titulo);
            }
            if (!autores.equals(mangaExistente.getAutores())) {
                System.out.println("Autores: " + mangaExistente.getAutores() + " → " + autores);
            }
            if (anoInicio != mangaExistente.getAnoInicio()) {
                System.out.println("Ano de início: " + mangaExistente.getAnoInicio() + " → " + anoInicio);
            }
            if (!Objects.equals(anoFim, mangaExistente.getAnoFim())) {
                System.out.println("Ano de fim: " + mangaExistente.getAnoFim() + " → " + anoFim);
            }
            if (!genero.equals(mangaExistente.getGenero())) {
                System.out.println("Gênero: " + mangaExistente.getGenero() + " → " + genero);
            }
            if (!revista.equals(mangaExistente.getRevista())) {
                System.out.println("Revista: " + mangaExistente.getRevista() + " → " + revista);
            }
            if (!editora.equals(mangaExistente.getEditora())) {
                System.out.println("Editora: " + mangaExistente.getEditora() + " → " + editora);
            }
            if (anoEdicao != mangaExistente.getAnoEdicao()) {
                System.out.println("Ano da edição: " + mangaExistente.getAnoEdicao() + " → " + anoEdicao);
            }
            if (qtdVolumes != mangaExistente.getQtdVolumes()) {
                System.out.println("Quantidade de volumes: " + mangaExistente.getQtdVolumes() + " → " + qtdVolumes);
            }
            if (!volumesAdquiridos.equals(mangaExistente.getVolumesAdquiridos())) {
                System.out.println("Volumes adquiridos: " + mangaExistente.getVolumesAdquiridos() + " → " + volumesAdquiridos);
            }
            
            System.out.print("\nConfirmar atualização? (s/n): ");
            String confirmacao = scanner.nextLine().trim().toLowerCase();
            if (!confirmacao.equals("s") && !confirmacao.equals("sim")) {
                System.out.println("Atualização cancelada.");
                return;
            }
            
            // Atualizar no sistema
            controller.atualizarManga(mangaAtualizado);
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Erro: Digite um número válido!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    private void compactarArquivo(Scanner scanner, MangaController controller) {
        System.out.println("\n=== Compactar Arquivo ===");
        System.out.println("A compactação irá:");
        System.out.println("- Remover fisicamente os registros marcados como removidos");
        System.out.println("- Regenerar os índices");
        System.out.println("- Liberar espaço no arquivo");
        System.out.println();
        System.out.println("⚠️  ATENÇÃO: Esta operação é irreversível!");
        System.out.println("   Os registros removidos logicamente serão perdidos permanentemente.");
        System.out.println();
        
        System.out.print("Tem certeza que deseja compactar o arquivo? (s/n): ");
        String confirmacao = scanner.nextLine().trim().toLowerCase();
        if (!confirmacao.equals("s") && !confirmacao.equals("sim")) {
            System.out.println("Compactação cancelada.");
            return;
        }
        
        try {
            controller.compactarArquivo();
            System.out.println("✅ Compactação realizada com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro durante a compactação: " + e.getMessage());
        }
    }
} 