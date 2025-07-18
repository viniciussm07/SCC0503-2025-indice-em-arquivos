# TODO List — Organizador de Mangás

## 1. Gerenciamento dos Arquivos de Índice

- [x] Implementar a gravação e leitura do índice primário (`data/indice_primario.idx`)
- [x] Implementar a gravação e leitura do índice secundário (`data/indice_secundario.idx`)
- [x] Carregar os índices ao iniciar o programa e salvar ao modificar

## 2. CRUD dos Mangás

- [x] Implementar a escrita de um novo mangá no arquivo `mangas.txt`
- [x] Implementar a leitura de mangá(s) por ISBN (índice primário)
- [x] Implementar a leitura de mangá(s) por título (índice secundário)
- [x] Implementar a atualização de um mangá existente (remover lógicamente adicionando '*' no início da linha correspondente e adicionar uma linha ao final do arquivo com o mangá editado)
- [x] Implementar a exclusão (lógica) de um mangá (com confirmação)
- [x] Implementar a compactação do arquivo `mangas.txt` (remover de fato as linhas removidas logicamente e atualizar os índices)
- [x] Garantir que os índices primário e secundário sejam atualizados em todas as operações


## 3. Interface Gráfica (JavaFX ou outra)

- [ ] Criar tela principal com menu ou abas para CRUD
- [ ] Criar formulário para cadastro/edição de mangá
- [ ] Listar mangás cadastrados (com busca por ISBN e título)
- [ ] Adicionar botão de exclusão com confirmação
- [ ] Adicionar botão de compactação do mangass.txt
- [ ] Exibir detalhes do mangá selecionado (incluindo imagem)
- [ ] Exibir mensagens de sucesso/erro para o usuário

## 4. Integração com API de Imagens

- [ ] Implementar busca de imagem do mangá pela API (ex: Kitsu)
- [ ] Fazer download e salvar imagem em `src/main/resources/imagens/`
- [ ] Exibir imagem na interface gráfica

## 5. Entrega

- [ ] Gravar vídeo de até 10 minutos demonstrando o CRUD e explicando o gerenciamento dos índices