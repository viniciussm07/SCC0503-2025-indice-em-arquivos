# SCC0503-2025 - Índice em Arquivos (Organizador de Mangás)

## Tecnologias Utilizadas
- **Java 17**
- **Qt Jambi** (interface gráfica)
- **HttpClient** (Java) para integração com API de imagens

## Estrutura do Projeto
```
SCC0503-2025-indice-em-arquivos/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── app/
│   │   │       ├── gui/         # Interface gráfica (Qt Jambi)
│   │   │       ├── model/       # Classes de domínio (Manga, etc)
│   │   │       ├── controller/  # Lógica de CRUD e integração GUI
│   │   │       ├── index/       # Gerenciamento dos índices
│   │   │       ├── api/         # Integração com API de imagens
│   │   │       └── util/        # Utilitários
│   │   └── resources/
│   │       └── imagens/         # Imagens baixadas
│
├── data/
│   ├── mangas.dat               # Dados dos mangás (não usar mangas.txt)
│   ├── indice_primario.idx      # Índice primário (ISBN)
│   └── indice_secundario.idx    # Índice secundário (título)
│
├── README.md
├── build.gradle                # Gerenciamento de dependências
└── .gitignore
```

## Observações
- O programa **NÃO** deve carregar o `mangas.txt`.
- Os dados e índices serão gerenciados nos arquivos em `data/`.
- A interface gráfica será feita com Qt Jambi.
- Imagens dos mangás serão baixadas via API e salvas em `src/main/resources/imagens/`.

## Como rodar
(Em breve) 