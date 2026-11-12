# Gestão Consultas Médicas

Aplicação web para gestão de consultas médicas: pacientes agendam, cancelam e avaliam consultas;
médicos registram os atendimentos e emitem relatórios; o administrador gerencia os cadastros com Clean Code
com SOLID, organizada em camadas inspiradas em Arquitetura Limpa (Clean Architecture / Hexagonal)

> Para aprender a usar as telas, consulte o **[manual.md](manual.md)**.

## Sumário
1. [Tecnologias](#tecnologias)
2. [Requisitos](#requisitos)
3. [Como executar](#como-executar)
4. [Configuração](#configuração)
5. [Acessos de exemplo](#acessos-de-exemplo)
6. [Funcionalidades](#funcionalidades)
7. [Regras de negócio](#regras-de-negócio)
8. [Arquitetura](#arquitetura)
9. [Modelo de dados](#modelo-de-dados)
10. [Rotas da aplicação](#rotas-da-aplicação)
11. [Testes](#testes)
12. [Segurança](#segurança)
13. [Solução de problemas](#solução-de-problemas)

## Tecnologias
| Item | Versão / descrição |
|---|---|
| Linguagem | Java 21 |
| Build | Maven |
| Framework | Spring Boot 3.3.x (Web, Validation, JDBC) |
| Acesso a dados | `JdbcTemplate` (SQL explícito, sem ORM) |
| Banco de dados | PostgreSQL 14+ |
| Frontend | Thymeleaf + CSS próprio responsivo (sem bibliotecas externas) |
| Senhas | BCrypt (`spring-security-crypto`) |
| Testes | JUnit 5 e Mockito |

Não utiliza Lombok: construtores, getters e setters são escritos explicitamente.

## Requisitos
- JDK 21
- Maven 3.9 ou superior
- PostgreSQL 14+ **ou** Docker

## Como executar

### 1. Banco de dados

**Opção A – PostgreSQL instalado**
```bash
psql -U postgres -f banco/01_criar_banco.sql
psql -U postgres -d hospital -f banco/02_criar_tabelas.sql
psql -U postgres -d hospital -f banco/03_dados_exemplo.sql   # opcional (dados de teste)
Se utilizar o DUMP na raiz do projeto hospital.sql não precisa executar os script acima, exceto a criação do database.
```

**Opção B – Docker** (cria banco, tabelas e dados de exemplo)
```bash
docker compose up -d
```

### 2. Aplicação
```bash
mvn spring-boot:run
```
Acesse **http://localhost:8080**.

Para gerar o pacote executável:
```bash
mvn clean package
java -jar target/sistema-hospitalar-ricci-1.0.0.jar
```

## Configuração
Arquivo `src/main/resources/application.properties`. Os valores abaixo podem ser sobrescritos por variáveis de ambiente.

| Propriedade / variável | Padrão | Descrição |
|---|---|---|
| `BANCO_URL` | `jdbc:postgresql://localhost:5432/hospital` | URL de conexão |
| `BANCO_USUARIO` | `postgres` | Usuário do banco |
| `BANCO_SENHA` | `postgres` | Senha do banco |
| `ADMIN_USUARIO` | `admin` | Login do administrador |
| `ADMIN_SENHA` | `admin123` | Senha do administrador (**altere em produção**) |
| `hospital.agenda.limite-diario-por-medico` | `4` | Consultas pendentes por médico/dia antes de abrir lista de espera |
| `server.port` | `8080` | Porta HTTP |

## Acessos de exemplo
Disponíveis após executar `banco/03_dados_exemplo.sql` (ou `docker compose up`).

| Perfil | Usuário | Senha | Observação |
|---|---|---|---|
| Médico | CRM `123456` | `medico123` | Dra. Helena Ricci – Cardiologia – plano Unimed |
| Médico | CRM `654321` | `medico123` | Dr. Paulo Andrade – Clínica Geral – sem plano |
| Paciente | CPF `52998224725` | `paciente123` | Maria Souza – plano Unimed |
| Paciente | CPF `11144477735` | `paciente123` | João Lima – sem plano (particular) |
| Administrador | `admin` | `admin123` | Definido em `application.properties` |

## Funcionalidades

**Paciente:** cadastro e edição de dados · agendamento de consultas · cancelamento · pesquisa de médicos
(com nota média e comentários) · avaliação de consultas realizadas · relatórios por período.

**Médico:** cadastro e edição de dados · realização de consultas (registro de sintomas, tratamento,
medicamentos e exames) · relatórios por período.

**Administrador:** listagem, cadastro e exclusão de pacientes e médicos.

## Regras de negócio
- **Plano de saúde:** paciente com plano só agenda com médicos que atendem o mesmo plano; paciente sem plano
  (particular) pode agendar com qualquer médico e a consulta é cobrada.
- **Lista de espera:** cada médico atende até 4 consultas pendentes por dia (configurável). Acima disso o
  paciente entra na lista de espera daquele médico e data.
- **Cancelamento:** se houver lista de espera para o mesmo médico e data, a vaga passa ao primeiro da fila;
  caso contrário a consulta é removida.
- **Realização:** pacientes com plano não pagam; para pacientes particulares o médico informa o valor.
- **Avaliação:** de 1 a 5 estrelas mais comentário opcional, uma única vez por consulta realizada.
- **Nota do médico:** média das avaliações recebidas.
- **Isolamento de dados:** cada usuário só acessa as próprias consultas.

O detalhamento passo a passo está no [manual.md](manual.md).

## Arquitetura
Organizada em camadas inspiradas em **Clean Architecture / Hexagonal**. As dependências apontam para dentro:

```
apresentacao  →  aplicacao  →  dominio  ←  infraestrutura
```

```
br.com.javaricci
├── dominio                      # regras e conceitos de negócio (sem dependência de framework web/banco)
│   ├── modelo                   # Paciente, Medico, Consulta, ConsultaRealizada, EntradaListaEspera, ...
│   ├── repositorio              # portas de saída (interfaces)
│   └── excecao                  # RegraNegocioException, RecursoNaoEncontradoException
├── aplicacao                    # casos de uso
│   ├── servico                  # Autenticacao, Paciente, Medico, Agendamento, Atendimento,
│   │                            # Avaliacao, BuscaMedico, Relatorio
│   ├── comando                  # objetos de entrada dos casos de uso
│   ├── configuracao             # propriedades tipadas (agenda e administrador)
│   ├── seguranca                # UsuarioLogado (guardado na sessão HTTP)
│   └── util
├── infraestrutura               # adaptadores de saída
│   ├── persistencia             # repositórios JDBC (JdbcTemplate)
│   └── configuracao             # codificador de senhas (BCrypt)
└── apresentacao.web             # adaptador de entrada (Spring MVC + Thymeleaf)
    ├── controlador              # controllers e tratamento global de erros
    ├── formulario               # formulários com Bean Validation
    └── configuracao             # interceptador de autenticação por perfil
```

**Princípios SOLID aplicados**
- *Responsabilidade única:* cada serviço trata de um caso de uso; formulários, comandos e modelos são separados.
- *Aberto/fechado:* novos perfis de pesquisa ou relatório entram por enums, sem alterar a lógica existente.
- *Substituição e segregação de interfaces:* repositórios pequenos e específicos por agregado.
- *Inversão de dependência:* serviços dependem das interfaces do domínio; os adaptadores JDBC as implementam.

Cada repositório expõe métodos separados: `inserir`, `atualizar`, `excluir` e `listar*`.

Estrutura de pastas do projeto:
```
SistemaHospitalarRicci
├── banco/                 # scripts SQL (banco, tabelas, dados de exemplo)
├── src/main/java/         # código-fonte
├── src/main/resources/    # application.properties, templates Thymeleaf e CSS
├── src/test/java/         # testes
├── docker-compose.yml
├── manual.md
├── pom.xml
└── README.md
```

## Modelo de dados
| Tabela | Finalidade | Chave |
|---|---|---|
| `pacientes` | Cadastro de pacientes (CPF, nome, idade, plano, senha) | `cpf` |
| `medicos` | Cadastro de médicos (CRM, nome, especialidade, plano atendido, senha) | `crm` |
| `consultas` | Consultas agendadas; `realizada` indica se já ocorreu | `id` |
| `lista_espera` | Fila de pacientes por médico e data | `id` |
| `consultas_realizadas` | Registro clínico, valor cobrado e avaliação (estrelas e texto) | `id_consulta` |

Relacionamentos: `consultas`, `lista_espera` e `consultas_realizadas` referenciam `medicos` e `pacientes`;
`consultas_realizadas.id_consulta` referencia `consultas.id`. Um médico ou paciente com consultas vinculadas
não pode ser excluído. Índices e restrições (`CHECK`) estão em `banco/02_criar_tabelas.sql`.

## Rotas da aplicação
| Área | Rota | Descrição |
|---|---|---|
| Pública | `GET /` | Página inicial |
| Pública | `/paciente/login`, `/medico/login`, `/admin/login` | Login por perfil |
| Pública | `/cadastro/paciente`, `/cadastro/medico` | Auto-cadastro |
| Pública | `POST /sair` | Encerrar sessão |
| Paciente | `/paciente/menu`, `/paciente/dados` | Menu e edição de dados |
| Paciente | `/paciente/consultas/agendar`, `/paciente/consultas/cancelar` | Agendar e cancelar |
| Paciente | `/paciente/medicos/buscar` | Pesquisar médicos |
| Paciente | `/paciente/avaliacoes`, `/paciente/avaliacoes/{id}` | Avaliar consultas |
| Paciente | `/paciente/relatorios` | Relatórios |
| Médico | `/medico/menu`, `/medico/dados` | Menu e edição de dados |
| Médico | `/medico/consultas`, `/medico/consultas/{id}/realizar` | Realizar consultas |
| Médico | `/medico/relatorios` | Relatórios |
| Admin | `/admin/menu` | Menu |
| Admin | `/admin/pacientes`, `/admin/pacientes/novo` | Gerenciar pacientes |
| Admin | `/admin/medicos`, `/admin/medicos/novo` | Gerenciar médicos |

As áreas `/paciente/**`, `/medico/**` e `/admin/**` exigem login com o perfil correspondente; caso contrário
o usuário é redirecionado para a tela de login da área.

## Testes
```bash
mvn test
```
Cobrem a regra de compatibilidade de plano (`MedicoTest`) e o agendamento, a lista de espera e o
cancelamento com repasse de vaga (`AgendamentoServicoTest`).

## Segurança
- Senhas armazenadas com **BCrypt**.
- Troca do identificador de sessão no login (proteção contra fixação de sessão).
- Acesso restrito por perfil e verificação de propriedade dos dados (um paciente não cancela nem avalia
  consulta de outro; um médico não atende consulta de outro).
- SQL parametrizado; a coluna de pesquisa de médicos é escolhida por enum, evitando injeção de SQL.
- **Pendências recomendadas para produção:** habilitar HTTPS, adicionar proteção CSRF (por exemplo com
  Spring Security), trocar as credenciais padrão do administrador e do banco, e limitar tentativas de login.

## Solução de problemas
| Sintoma | Causa provável / solução |
|---|---|
| `Connection refused` ao iniciar | PostgreSQL não está rodando ou a porta/URL em `BANCO_URL` está incorreta |
| `password authentication failed` | Ajuste `BANCO_USUARIO` e `BANCO_SENHA` |
| `relation "pacientes" does not exist` | Execute `banco/02_criar_tabelas.sql` no banco `hospital` |
| Porta 8080 ocupada | Altere `server.port` ou finalize o processo que a utiliza |
| Login do administrador falha | Confira `ADMIN_USUARIO` / `ADMIN_SENHA` (ou os valores em `application.properties`) |
