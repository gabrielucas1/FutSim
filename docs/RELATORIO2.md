# 💡 Expectativas vs. Realidade & Tecnologias Utilizadas

> Para mais informações detalhadas, consulte o [README.md](../README.md) na pasta raiz do projeto.

## Expectativas e Realidade do Projeto

### Funcionalidades Implementadas Conforme Esperado

- Todas as telas principais: inicial, criação, campeonatos, pontos corridos, mata-mata, fase de grupos.
- Todas os campeonatos inicialmente planejados: Pontos Corridos e Mata-Mata.
- Histórico de partidas para o Mata-Mata.
- CRUD de campeonatos.
- CRUD de times em campeonatos.
- Persistência local robusta com Room (SQLite).
- UI responsiva e intuitiva com Jetpack Compose.
- Navegação fluida e feedback visual para o usuário.

### Funcionalidades Parcialmente ou Não Implementadas

- Históricos de jogos para Pontos Corridos: Apenas a tabela.
- Internacionalização completa: Apenas parte das strings estão em inglês.
- Testes automatizados: Cobertura parcial, priorizando funcionalidades principais.
- Exportação/importação de dados: Não implementado, foco em persistência local.
- Notificações e integração com serviços externos: Fora do escopo.

#### Justificativas para Não Implementação

- **Priorização da experiência do usuário, persistência e lógica de simulação local.**
- **Limitação de tempo** devido a outras disciplinas e à estratégia de commits agrupados.
- **Exportação/importação e notificações** ficaram fora do escopo para garantir entrega das funcionalidades essenciais.

---

## Tecnologias: Planejado vs. Utilizado

| Tecnologia             | Planejado | Utilizado |
|------------------------|-----------|-----------|
| Jetpack Compose        | Sim       | Sim       |
| Room (SQLite)          | Sim       | Sim       |
| ViewModel/StateFlow    | Sim       | Sim       |
| Navigation Compose     | Sim       | Sim       |
| Material Design        | Sim       | Sim       |
| Testes JUnit/Espresso  | Sim       | Parcial   |
| Internacionalização    | Sim       | Parcial   |
| Exportação/Importação  | Sim       | Não       |

---

## Conclusão

Apesar dos desafios e ajustes de cronograma, o FutSim atingiu seus objetivos principais, entregando um simulador funcional para campeonatos de futebol amador, com potencial de expansão futura.

---