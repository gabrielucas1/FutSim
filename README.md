# ⚽ FutSim: Simulador Mobile para Campeonato Futebolístico

## 📋 Sobre o Projeto

O **FutSim** é um aplicativo mobile para Android, desenvolvido em Kotlin, que permite criar, gerenciar e simular campeonatos de futebol em diferentes formatos. Com interface moderna, responsiva e persistência local, o app facilita a vida de organizadores e entusiastas de futebol amador, promovendo entretenimento, interação social e redução de custos na organização de torneios.

O desenvolvimento ocorreu de forma remota (Discord) e presencial (IFTM UPT), com testes reais, coleta de feedbacks e atualizações iterativas. O FutSim incentiva a gestão de campeonatos amadores e pode ser expandido para monetização futura.

---

## 👥 Time de Desenvolvimento

| Nome                                   | Usuário no GitHub         |
|-----------------------------------------|---------------------------|
| Luiz Alberto Cury Andalécio             | [Luiz-Andalezio](https://github.com/Luiz-Andalezio)         |
| Gabriel Lucas Silva Seabra              | [gabrielucas1](https://github.com/gabrielucas1)             |
| Vitor Gabriel Resende Lopes Oliveira    | [VitorGabrielRLO](https://github.com/VitorGabrielRLO)       |
| Jussie Lopes Da Silva                   | [jussie-lopes23](https://github.com/jussie-lopes23)         |

---

## 📝 Descrição do Software

O FutSim permite ao usuário:

- Montar tabelas e inserir times no simulador
- Automatizar a tabela e o chaveamento do torneio a partir dos resultados
- Alterar placares, visualizar histórico de jogos e próximos confrontos
- Gerenciar campeonatos nos formatos Pontos Corridos, Mata-Mata e Fase de Grupos
- Persistir dados localmente, com interface intuitiva e responsiva

O sistema foi projetado para ser fácil de usar, com foco em usabilidade, praticidade e flexibilidade para diferentes tipos de torneios.

---

## 🛠️ Plataformas Alvo e Tecnologias

- **Plataforma:** Android (minSdk 29)
- **Linguagem:** Kotlin
- **IDE:** Android Studio (principal), Visual Studio Code (secundário)
- **Frameworks e Bibliotecas:**
    - Jetpack Compose (UI)
    - Room (persistência SQLite)
    - ViewModel + StateFlow (gerenciamento de estado)
    - Navigation Compose (navegação)
    - Material Design 3
    - Coroutines
    - AndroidX Lifecycle
    - JUnit, Espresso (testes)
    - Kotlinx Serialization (expansão futura)

---

## ✅ Requisitos Funcionais

- Montar tabela e inserir times no simulador
- Automatizar a tabela a partir de resultados
- Montar chaveamento do torneio de forma aleatória (mata-mata)
- Automatizar a chave a partir de resultados
- Alterar placares
- Histórico de jogos
- Mostrar próximo jogo
- Design do aplicativo moderno e intuitivo

## ✅ Requisitos Não Funcionais

- Plataforma Android
- Transições simultâneas com o decorrer dos jogos
- Front-End intuitivo
- Controle de acesso para funcionalidades restritas (ex: alteração de placares)

---

## 📁 Estrutura do Projeto

Organização modular: dados, lógica, UI e testes separados. Código disponível no [GitHub](https://github.com/Luiz-Andalezio/FutSim), com branches e tags documentando o progresso.

## 📂 Estrutura de Pastas do Projeto

```
app/
 └── src/
    ├── main/
    │   ├── java/com/example/futsim/
    │   │   ├── data/               # 📦 DAOs, Database, Repositórios
    │   │   ├── model/              # 🗂️ Entidades e modelos de dados
    │   │   ├── ui/
    │   │   │   ├── componentes/    # 🧩 Botões, Cards, etc.
    │   │   │   ├── telas/          # 📱 Telas Compose (UI)
    │   │   │   ├── theme/          # 🎨 Temas, cores, tipografia
    │   │   │   └── viewmodel/      # 🧠 ViewModels e Providers
    │   ├── res/                    # 🖼️ Imagens, strings, cores, layouts
    │   └── AndroidManifest.xml
    └── test/                       # 🧪 Testes unitários
```

---

## 🏗️ Atividades e Cronograma

| Atividade                        | Estimado   | Realizado   | Justificativa de Atrasos                                                                 |
|-----------------------------------|------------|-------------|------------------------------------------------------------------------------------------|
| Concepção e planejamento          | 1 semana   | 1 semana    | -                                                                                        |
| Estruturação do projeto           | 1 semana   | 1 semana    | -                                                                                        |
| Criação de entidades/modelos      | 1 semana   | 1 semana    | -                                                                                        |
| Criação do banco de dados (Room)  | 1 semana   | 1 semana    | -                                                                                        |
| Implementação Pontos Corridos     | 2 semanas  | 2,5 semanas | Ajustes de UI, persistência e outros trabalhos de disciplinas                             |
| Implementação Mata-Mata           | 2 semanas  | 3 semanas   | Ajustes de lógica, persistência e acúmulo de trabalhos de outras disciplinas             |
| Implementação Fase de Grupos      | 2 semanas  | 2 semanas   | -                                                                                        |
| Integração Room/ViewModel         | 1 semana   | 1,5 semanas | Adaptação para persistência completa e commits agrupados após features prontas           |
| Manipulação do simulador (CRUD)   | 1 semana   | 1 semana    | -                                                                                        |
| Automatização de tabelas/chaveamentos | 1 semana | 1 semana    | -                                                                                        |
| Testes e refino                   | 1 semana   | 1 semana    | -                                                                                        |
| Documentação e apresentação       | 1 semana   | 1 semana    | -                                                                                        |

**Principais atrasos:**

- Muitos trabalhos de outras disciplinas impactaram o ritmo do desenvolvimento.
- Alguns membros optaram por realizar commits apenas após finalizarem grandes features.
- Ajustes extras na lógica do mata-mata para garantir persistência e avanço automático de fases.

---

## 💡 Expectativas vs. Realidade

**Implementado conforme esperado:**

- Todas as telas principais: inicial, criação, campeonatos, pontos corridos, mata-mata, fase de grupos.
- Todas os campeonatos inicialmente planejados: Pontos Corridos e Mata-Mata.
- Histórico de partidas para o Mata-Mata.
- CRUD de campeonatos.
- CRUD de times em campeonatos.
- Persistência local robusta com Room (SQLite).
- UI responsiva e intuitiva com Jetpack Compose.
- Navegação fluida e feedback visual para o usuário.

**Parcial ou não implementado:**

- Históricos de jogos para Pontos Corridos: Apenas a tabela.
- Internacionalização completa: Apenas parte das strings estão em inglês.
- Testes automatizados: Cobertura parcial, priorizando funcionalidades principais.
- Exportação/importação de dados: Não implementado, foco em persistência local.
- Notificações e integração com serviços externos: Fora do escopo.

> **Justificativas:** Priorização da experiência do usuário, persistência e lógica de simulação local. Limitação de tempo devido a outras disciplinas e à estratégia de commits agrupados.

---

## 🔗 Tecnologias: Planejado vs. Utilizado

| Tecnologia             | Planejado | Utilizado |
|------------------------|-----------|-----------|
| Jetpack Compose        | Sim       | Sim       |
| Room (SQLite)          | Sim       | Sim       |
| ViewModel/StateFlow    | Sim       | Sim       |
| Navigation Compose     | Sim       | Sim       |
| Material Design        | Sim       | Sim       |
| Testes JUnit/Espresso  | Sim       | Parcial   |
| Internacionalização    | Sim       | Parcial   |

---

## ⚙️ Como rodar localmente

1. Clone o repositório.
2. Abra no Android Studio.
3. Sincronize as dependências (Gradle).
4. Rode o app em um emulador ou dispositivo físico.

---

## 📓 Padrão de Commits

Este repositório segue o padrão [Conventional Commits](https://www.conventionalcommits.org/).

**Tipos mais comuns:**

- `feat`: Nova funcionalidade
- `fix`: Correção de bugs
- `docs`: Alterações na documentação
- `style`: Ajustes de estilização
- `refactor`: Refatoração sem mudança de comportamento
- `perf`: Melhorias de performance
- `test`: Criação ou modificação de testes
- `build`: Mudanças que afetam o build
- `ci`: Configurações de integração contínua

**Escopo:** Define a parte do projeto afetada (ex: módulo, página, feature).

---

## 📢 Contato

Dúvidas ou sugestões? Abra uma issue ou entre em contato com a equipe:

- gl9736387@gmail.com
