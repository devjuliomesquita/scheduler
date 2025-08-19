# Scheduler Job - Processamento Distribuído de Usuários

Este projeto é uma aplicação Java com Spring Boot que exemplifica o processamento em lote de usuários em um sistema distribuído, com integração a serviços externos, controle transacional e tratamento robusto de erros.

## Funcionalidades

- **Job Agendado:** Executa diariamente ao meio-dia, processando até 50 usuários com status pendente por lote.
- **Processamento em Lotes:** O job repete a execução até que não haja mais usuários pendentes, garantindo throughput eficiente.
- **Execução Transacional:** Cada lote é processado dentro de uma transação, assegurando a consistência dos dados.
- **Integração Externa:** Para cada usuário, busca informações de processo em um serviço externo via HTTP, com política de retry para falhas temporárias.
- **Persistência:** Salva os dados do processo retornado e atualiza o status do usuário conforme o resultado.
- **Tratamento de Erros:**
    - **Erros 4xx:** Usuários com erro de cliente (ex: dados inválidos) são marcados como ignorados.
    - **Outros erros:** São registrados em log, sem interromper o processamento dos demais usuários.
- **Logs Detalhados:** Registra advertências e erros para facilitar o monitoramento e troubleshooting.

## Principais Componentes

- `JobForUsers`: Classe principal do job, responsável pelo agendamento, controle transacional, integração externa e atualização dos usuários.
- `UserEventsEntity`: Entidade que representa o usuário e seu status de processamento.
- `ProcessEntity`: Entidade que representa o processo externo associado ao usuário.
- `UserRepository`: Repositório para acesso e bloqueio pessimista dos usuários pendentes.
- `ProcessRepository`: Repositório para persistência dos processos retornados.
- `ProcessService` / `ProcessServiceImpl`: Serviço responsável por orquestrar a chamada ao serviço externo, com política de retry.
- `ProcessClient`: Cliente HTTP para comunicação com o serviço externo.

## Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Scheduling
- Spring Transaction
- Spring Retry
- JPA/Hibernate
- Maven
- SLF4J (Logging)
- SQL (persistência)

## Como Executar

1. Clone o repositório:
   ```
   git clone <url-do-repositorio>
   ```
2. Acesse a pasta do projeto:
   ```
   cd scheduler
   ```
3. Compile e execute:
   ```
   ./mvnw spring-boot:run
   ```

## Observações

- O job pode ser facilmente adaptado para outros tipos de processamento em lote e integrações externas.
- A política de retry e o tratamento de erros podem ser customizados conforme a necessidade do negócio.
- O projeto serve como base para sistemas distribuídos que exigem resiliência, consistência e integração com múltiplos serviços.

---

Sinta-se à vontade para contribuir ou adaptar este exemplo para suas necessidades!