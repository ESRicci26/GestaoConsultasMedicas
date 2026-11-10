# Manual do Usuário – Sistema Hospitalar Ricci

Este manual explica, de forma simples, como usar o sistema e quais regras ele segue.
Escolha a seção do seu perfil:

- [1. Conhecendo o sistema](#1-conhecendo-o-sistema)
- [2. Paciente](#2-paciente)
- [3. Médico](#3-médico)
- [4. Administrador](#4-administrador)
- [5. Regras de negócio (resumo)](#5-regras-de-negócio-resumo)
- [6. Mensagens do sistema](#6-mensagens-do-sistema)
- [7. Perguntas frequentes](#7-perguntas-frequentes)
- [8. Glossário](#8-glossário)

---

## 1. Conhecendo o sistema

### 1.1 Os três perfis
| Perfil | Para quem é | O que faz |
|---|---|---|
| **Paciente** | Quem busca atendimento | Agenda, cancela e avalia consultas; pesquisa médicos; vê relatórios |
| **Médico** | Profissionais de saúde | Realiza consultas agendadas e vê relatórios |
| **Administrador** | Equipe de gestão | Cadastra, lista e exclui pacientes e médicos |

### 1.2 Como entrar
1. Abra o endereço do sistema (por exemplo, `http://localhost:8080`).
2. Na **página inicial**, clique no seu perfil: **Paciente**, **Médico** ou **Administrador**.
3. Informe o identificador e a senha:

| Perfil | Identificador | Observação |
|---|---|---|
| Paciente | **CPF** | Pode digitar com ou sem pontos e traço |
| Médico | **CRM** | Exatamente como foi cadastrado |
| Administrador | **Usuário** | Fornecido pela equipe técnica |

4. Clique em **Entrar**. Você será levado ao **menu** do seu perfil.

> Ainda não tem conta? Na tela de login de pacientes e médicos há o link **Cadastre-se**.

### 1.3 Como sair
Clique em **Sair**, no canto superior direito. Em telas pequenas (celular), toque em **☰** para abrir o menu e depois em **Sair**.
Feche sempre a sessão ao usar computadores compartilhados.

### 1.4 Navegação
- A **barra superior** mostra seu nome, o link **Menu** (volta ao menu do seu perfil) e o botão **Sair**.
- O nome **Sistema Hospitalar Ricci** leva à página inicial.
- Cada tela tem o botão **Voltar**.
- Datas são exibidas como **dd/mm/aaaa**.
- O sistema se adapta a computador, tablet e celular; nas listas, em telas pequenas, cada linha vira um cartão.

### 1.5 Áreas protegidas
Cada perfil só acessa a própria área. Se tentar abrir uma página de outro perfil (ou estiver sem login),
o sistema leva você para a tela de login correspondente.

### 1.6 Campos e erros de preenchimento
- Campos com erro aparecem com uma mensagem **em vermelho** logo abaixo.
- Mensagens verdes indicam sucesso; amarelas, avisos; vermelhas, erros.
- Nada é gravado enquanto houver erro: corrija e envie novamente.

---

## 2. Paciente

### 2.1 Cadastro (primeiro acesso)
**Caminho:** página inicial → **Paciente** → **Cadastre-se**

| Campo | Regra |
|---|---|
| CPF | Obrigatório; **11 dígitos** (pontos e traços são removidos automaticamente). Não pode já estar cadastrado |
| Nome | Obrigatório; até 120 caracteres |
| Idade | Obrigatória; de 0 a 130 |
| Plano de saúde | **Opcional**. Preencha com o nome do seu plano (ex.: *Unimed*). Deixe em branco se não tem plano |
| Senha | Obrigatória; de 6 a 72 caracteres |

Clique em **Cadastrar**. Ao concluir, você volta à tela de login com a mensagem de sucesso.

> **Por que o plano importa?**
> - **Com plano:** você só consegue agendar com médicos que atendem **o mesmo plano**, e a consulta **não é cobrada**.
> - **Sem plano (particular):** você pode agendar com **qualquer médico**, e o médico informará o **valor** a pagar.
>
> Escreva o plano igual ao que o médico tem cadastrado (maiúsculas/minúsculas não fazem diferença, mas o texto deve ser o mesmo).

### 2.2 Menu do paciente
Seis opções: **Agendar consulta**, **Cancelar consulta**, **Pesquisar médicos**, **Avaliar consulta**,
**Relatórios** e **Editar meus dados**.

### 2.3 Agendar consulta
**Caminho:** Menu → **Agendar consulta**

1. No campo **Médico**, escolha um profissional. A lista mostra *Nome – Especialidade* e já vem filtrada
   conforme o seu plano.
2. Escolha a **Data** (de hoje em diante; datas passadas não são aceitas).
3. Clique em **Agendar consulta**.

**Dois resultados possíveis:**

| Situação | Resultado |
|---|---|
| O médico tem vaga na data | Mensagem verde: *Agendamento realizado com sucesso!* A consulta fica **pendente** |
| O médico já tem **4 consultas pendentes** na data | Mensagem amarela: *A agenda desse médico está lotada... Você foi adicionado à lista de espera.* |

**Como funciona a lista de espera**
- É uma fila **por médico e por data**, respeitando a ordem de chegada.
- Você **não precisa fazer nada**: se alguém cancelar a consulta naquele médico e data, a vaga passa
  **automaticamente para o primeiro da fila** e a consulta aparece em **Cancelar consulta** e nos **Relatórios**
  (tipo *Agendamentos pendentes*).
- A vaga só é repassada quando há um **cancelamento**. O sistema não tem uma tela para consultar ou sair da fila.

**Se aparecer "Nenhum médico disponível para o seu plano no momento"**: não há médicos cadastrados que atendam
o seu plano. Procure a administração ou confira o plano informado em *Editar meus dados*.

### 2.4 Cancelar consulta
**Caminho:** Menu → **Cancelar consulta**

1. A tela lista suas consultas **pendentes** (nº, data e médico).
2. Clique em **Desmarcar** na consulta desejada e confirme na janela exibida.

O que acontece com a vaga:
- Se havia gente na lista de espera **daquele médico e data**, a vaga vai para o primeiro da fila.
- Se não havia ninguém, a consulta simplesmente deixa de existir.

> Só é possível cancelar consultas **suas** e que **ainda não foram realizadas**.

### 2.5 Pesquisar médicos
**Caminho:** Menu → **Pesquisar médicos**

1. Escolha **Buscar por**: *Nome* ou *Especialidade*.
2. Digite parte do texto (ex.: *card* encontra *Cardiologia*). **Deixe em branco para listar todos** os médicos disponíveis para você.
3. Clique em **Buscar**.

Cada resultado mostra nome, especialidade, CRM, plano atendido e:
- a **nota média** (★ de 1,0 a 5,0), calculada com as avaliações dos pacientes; ou *Ainda sem avaliações*;
- os **comentários** deixados por outros pacientes.

Se você tem plano, a busca mostra **somente** médicos do seu plano.

### 2.6 Avaliar consulta
**Caminho:** Menu → **Avaliar consulta**

A lista traz as consultas **já realizadas** pelo médico e **ainda não avaliadas**.

1. Clique em **Avaliar** na consulta desejada.
2. Escolha de **1 a 5 estrelas** (passe o mouse para ver o significado):
   1 = Ruim · 2 = Regular · 3 = Bom · 4 = Muito bom · 5 = Perfeito.
3. Se quiser, escreva um **comentário** (até 1000 caracteres).
4. Clique em **Enviar avaliação**.

Regras:
- A **nota é obrigatória**; o comentário é opcional.
- Cada consulta só pode ser avaliada **uma vez** e a avaliação **não pode ser alterada depois**.
- A nota e o comentário passam a aparecer na pesquisa de médicos.

### 2.7 Relatórios
**Caminho:** Menu → **Relatórios**

1. Escolha o **Tipo**:
   - *Consultas realizadas* – atendimentos que já aconteceram;
   - *Agendamentos pendentes* – consultas marcadas que ainda não aconteceram.
2. Informe a **Data inicial** e a **Data final** (por padrão, 30 dias antes e 30 dias depois de hoje).
3. Clique em **Gerar relatório**.

A tabela mostra: **Nº**, **Médico**, **CRM** e **Data**, e o total de registros. A data inicial não pode ser
posterior à final.

### 2.8 Editar meus dados
**Caminho:** Menu → **Editar meus dados**

- Você pode alterar **nome, idade, plano e senha**.
- O **CPF não pode ser alterado** (campo bloqueado).
- **Senha em branco = mantém a senha atual.**
- Se mudar o **plano**, a lista de médicos disponíveis muda a partir de então. Consultas já agendadas continuam valendo.

---

## 3. Médico

### 3.1 Cadastro
**Caminho:** página inicial → **Médico** → **Cadastre-se**

| Campo | Regra |
|---|---|
| CRM | Obrigatório; até 20 caracteres; não pode já estar cadastrado |
| Nome | Obrigatório; até 120 caracteres |
| Especialidade | Obrigatória; até 80 caracteres |
| Plano atendido | **Opcional**. Um plano de saúde que você atende (ex.: *Unimed*) |
| Senha | Obrigatória; de 6 a 72 caracteres |

**Como o "Plano atendido" afeta quem agenda com você**
- Preenchido (ex.: *Unimed*): atende pacientes **Unimed** e pacientes **particulares** (sem plano).
- Em branco: atende **somente pacientes particulares**. Pacientes com plano não veem você.

### 3.2 Menu do médico
Três opções: **Realizar consulta**, **Gerar relatórios** e **Editar meus dados**.

### 3.3 Realizar consulta
**Caminho:** Menu → **Realizar consulta**

1. A tela lista as consultas **pendentes** com você (nº, data, paciente e CPF), em ordem de data.
2. Clique em **Atender** na consulta desejada.
3. Confira, no topo, o paciente, a data e o **plano** (ou *Particular – consulta cobrada*).
4. Preencha o registro clínico:

| Campo | Obrigatório? |
|---|---|
| Sintomas | Sim |
| Tratamento | Sim |
| Medicamentos | Não |
| Exames | Não |
| Valor a pagar (R$) | **Somente se o paciente não tem plano** (o campo só aparece nesse caso) |

5. Clique em **Finalizar consulta**.

O que acontece ao finalizar:
- O registro é gravado e a consulta passa a **realizada**, saindo da lista de pendentes.
- Para paciente **com plano**, o valor registrado é **R$ 0,00**.
- A consulta fica disponível para o paciente **avaliar**.
- A operação é **definitiva**: não é possível reabrir a consulta. Revise os dados antes de finalizar.

### 3.4 Gerar relatórios
**Caminho:** Menu → **Gerar relatórios**

Funciona como o relatório do paciente (tipo, data inicial e final), mas a tabela mostra
**Nº, Paciente, CPF e Data** das consultas **suas** no período:
- *Consultas realizadas* – atendimentos finalizados;
- *Agendamentos pendentes* – consultas marcadas que ainda não foram atendidas.

### 3.5 Editar meus dados
Altere **nome, especialidade, plano atendido e senha**. O **CRM não pode ser alterado**.
Senha em branco mantém a atual.

---

## 4. Administrador

### 4.1 Entrar
Página inicial → **Administrador**. O administrador **não tem cadastro**: as credenciais são definidas pela equipe técnica na configuração do sistema.

### 4.2 Menu
Duas opções: **Pacientes** e **Médicos**.

### 4.3 Gerenciar pacientes
**Caminho:** Menu → **Pacientes**

| Ação | Como fazer |
|---|---|
| **Listar** | A tela mostra CPF, nome, idade e plano de todos (ordem alfabética). *Particular* = sem plano |
| **Cadastrar** | Clique em **Cadastrar paciente** e preencha o formulário (mesmas regras da seção 2.1) |
| **Excluir** | Clique em **Excluir** na linha e confirme |

### 4.4 Gerenciar médicos
**Caminho:** Menu → **Médicos**

Mesmas ações: listar (CRM, nome, especialidade, plano), **Cadastrar médico** (regras da seção 3.1) e **Excluir**.

### 4.5 Regra importante sobre exclusão
Não é possível excluir um paciente ou médico que tenha **consultas ou lista de espera vinculadas**. O sistema
exibirá: *"Não é possível excluir o paciente (ou médico): existem consultas vinculadas..."*. Isso protege o histórico clínico.

> O administrador não edita dados de terceiros; cada paciente ou médico altera as próprias informações.

---

## 5. Regras de negócio (resumo)

| Nº | Regra |
|---|---|
| 1 | CPF (paciente) e CRM (médico) são **únicos** e **não podem ser alterados** depois do cadastro |
| 2 | Senhas têm de 6 a 72 caracteres e são guardadas de forma criptografada |
| 3 | Paciente **com plano** só agenda com médico que atende **o mesmo plano** |
| 4 | Paciente **sem plano** agenda com **qualquer** médico e a consulta é **cobrada** |
| 5 | Só é possível agendar para **hoje ou datas futuras** |
| 6 | Cada médico comporta **até 4 consultas pendentes por dia** (valor configurável) |
| 7 | Com a agenda cheia, o paciente entra na **lista de espera** (por médico e data, por ordem de chegada) |
| 8 | Ao **cancelar**, a vaga vai ao primeiro da fila daquele médico e data; sem fila, a consulta é removida |
| 9 | Só se cancela consulta **própria** e **ainda não realizada** |
| 10 | Só o **médico da consulta** pode realizá-la; sintomas e tratamento são obrigatórios |
| 11 | Paciente com plano **não paga** (R$ 0,00); particular tem o **valor informado pelo médico** |
| 12 | Consulta realizada **não pode ser desfeita** |
| 13 | A avaliação (1 a 5 estrelas) é feita **uma vez** por consulta realizada, apenas pelo paciente atendido |
| 14 | A **nota média** do médico é a média das avaliações recebidas |
| 15 | Pacientes e médicos só veem **os próprios** dados, consultas e relatórios |
| 16 | Pacientes/médicos com consultas vinculadas **não podem ser excluídos** |

### Ciclo de vida de uma consulta
```
Agendar ──► Pendente ──► Realizada (médico) ──► Avaliada (paciente)
              │
              └─► Cancelada (paciente): vaga vai para a lista de espera ou a consulta é removida
```
Agenda cheia: **Agendar ──► Lista de espera ──(alguém cancela)──► Pendente**

---

## 6. Mensagens do sistema

| Mensagem | O que significa / o que fazer |
|---|---|
| *Credenciais inválidas. Tente novamente.* | Identificador ou senha incorretos. Confira e tente de novo |
| *CPF já cadastrado. Tente novamente com outro CPF.* | Já existe um paciente com esse CPF; use o login |
| *CRM já cadastrado. Tente novamente com outro CRM.* | Já existe um médico com esse CRM |
| *O CPF deve conter 11 dígitos.* | Digite os 11 números do CPF |
| *A senha deve ter entre 6 e 72 caracteres.* | Escolha uma senha maior ou menor, conforme o caso |
| *Informe uma senha para o cadastro.* | A senha é obrigatória no cadastro |
| *Selecione um médico.* / *Informe a data da consulta.* | Preencha os dois campos do agendamento |
| *Escolha uma data a partir de hoje.* | Não é possível agendar no passado |
| *Este médico não atende o plano de saúde do paciente.* | Escolha um médico compatível com o seu plano |
| *A agenda desse médico está lotada... lista de espera.* | Você entrou na fila; aguarde um cancelamento |
| *Consulta não encontrada ou já realizada.* | A consulta não existe, não é sua ou já foi finalizada |
| *Consulta não encontrada ou já avaliada.* | A consulta já recebeu avaliação ou não é sua |
| *Selecione de 1 a 5 estrelas.* | Marque uma nota antes de enviar a avaliação |
| *Informe o valor a pagar: o paciente não possui plano de saúde.* | Preencha o valor da consulta particular |
| *A data inicial não pode ser posterior à data final.* | Corrija o período do relatório |
| *Informe o tipo de relatório e o período.* | Preencha tipo, data inicial e data final |
| *Não é possível excluir... existem consultas vinculadas.* | Cadastros com histórico não podem ser excluídos |

---

## 7. Perguntas frequentes

**Esqueci minha senha. O que faço?**
O sistema não possui recuperação automática. Procure a administração para orientação.

**Posso agendar duas consultas no mesmo dia?**
O sistema não impede. O limite considerado é o de consultas por médico/dia (4).

**Fui para a lista de espera. Onde vejo minha posição?**
Não há tela para isso. Quando a vaga for repassada a você, a consulta aparecerá em **Cancelar consulta** e em **Relatórios → Agendamentos pendentes**.

**Quando uma vaga reaparece para novos agendamentos?**
Quando uma consulta pendente é cancelada ou realizada, a contagem de pendentes do médico naquele dia diminui. Se havia fila, o repasse automático acontece **somente no cancelamento**.

**Não vejo nenhum médico na lista de agendamento.**
Com plano, apenas médicos do mesmo plano aparecem. Confirme o nome do plano em *Editar meus dados*.

**Por que não consigo avaliar uma consulta?**
A consulta só aparece na lista de avaliação depois que o médico a **finaliza**, e apenas uma vez.

**Posso alterar o CPF ou o CRM?**
Não. São os identificadores no sistema. Em caso de erro, procure a administração.

**O que é "Particular"?**
Paciente sem plano de saúde (ou médico que não atende plano). A consulta é cobrada.

**Digitei o CPF com pontos e traço. Tem problema?**
Não, o sistema os remove automaticamente.

---

## 8. Glossário
| Termo | Significado |
|---|---|
| **CPF** | Documento do paciente, usado como login |
| **CRM** | Registro do médico, usado como login |
| **Plano (de saúde)** | Convênio do paciente; define com quais médicos ele pode agendar sem pagar |
| **Plano atendido** | Convênio aceito pelo médico |
| **Particular** | Atendimento sem plano, com cobrança de valor |
| **Pendente** | Consulta marcada que ainda não aconteceu |
| **Realizada** | Consulta já finalizada pelo médico |
| **Lista de espera** | Fila de pacientes aguardando vaga em agenda cheia |
| **Avaliação** | Nota (1 a 5 estrelas) e comentário do paciente sobre a consulta |
