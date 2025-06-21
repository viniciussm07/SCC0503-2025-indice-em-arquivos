# TODO List — Organizador de Mangás

## 1. CRUD dos Mangás

- [ ] Implementar a escrita de um novo mangá no arquivo `data/mangas.dat`
- [ ] Implementar a leitura de mangá(s) por ISBN (índice primário)
- [ ] Implementar a leitura de mangá(s) por título (índice secundário)
- [ ] Implementar a atualização de um mangá existente
- [ ] Implementar a exclusão de um mangá (com confirmação)
- [ ] Garantir que os índices primário e secundário sejam atualizados em todas as operações

## 2. Gerenciamento dos Arquivos de Índice

- [ ] Implementar a gravação e leitura do índice primário (`data/indice_primario.idx`)
- [ ] Implementar a gravação e leitura do índice secundário (`data/indice_secundario.idx`)
- [ ] Carregar os índices ao iniciar o programa e salvar ao modificar

## 3. Interface Gráfica (Qt Jambi)

- [ ] Criar tela principal com menu ou abas para CRUD
- [ ] Criar formulário para cadastro/edição de mangá
- [ ] Listar mangás cadastrados (com busca por ISBN e título)
- [ ] Adicionar botão de exclusão com confirmação
- [ ] Exibir detalhes do mangá selecionado (incluindo imagem)
- [ ] Exibir mensagens de sucesso/erro para o usuário

## 4. Integração com API de Imagens

- [ ] Implementar busca de imagem do mangá pela API (ex: Kitsu)
- [ ] Fazer download e salvar imagem em `src/main/resources/imagens/`
- [ ] Exibir imagem na interface gráfica

## 5. Utilitários

- [ ] Implementar métodos utilitários para leitura/escrita dos arquivos de dados e índices (`FileUtil`)
- [ ] Serializar e desserializar objetos Manga de/para arquivo

## 6. Extras e Boas Práticas

- [ ] Tratar erros de I/O e exibir mensagens amigáveis
- [ ] Validar dados do formulário antes de salvar
- [ ] Comentar o código e documentar as funções principais
- [ ] (Opcional) Implementar testes unitários para as classes principais

## 7. Entrega

- [ ] Gravar vídeo de até 10 minutos demonstrando o CRUD e explicando o gerenciamento dos índices