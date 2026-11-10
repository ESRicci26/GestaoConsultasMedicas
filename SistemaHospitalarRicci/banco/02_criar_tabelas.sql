-- =====================================================================
-- Sistema Hospitalar Ricci - Criação das tabelas
-- Execute conectado ao banco "hospital":
--   psql -U postgres -d hospital -f banco/02_criar_tabelas.sql
--
-- Correspondência com o projeto JavaFX/MySQL original:
--   pacientes            <- pacientes
--   medicos              <- medicoscadastrados
--   consultas            <- consultas            (realizada 'n'/'s' virou BOOLEAN)
--   lista_espera         <- consultasespera
--   consultas_realizadas <- consultasrealizadas
-- =====================================================================

CREATE TABLE IF NOT EXISTS pacientes (
    cpf     VARCHAR(11)  PRIMARY KEY,
    nome    VARCHAR(120) NOT NULL,
    idade   SMALLINT     NOT NULL,
    plano   VARCHAR(80),
    senha   VARCHAR(100) NOT NULL,
    CONSTRAINT ck_pacientes_cpf   CHECK (cpf ~ '^[0-9]{11}$'),
    CONSTRAINT ck_pacientes_idade CHECK (idade BETWEEN 0 AND 130)
);

CREATE TABLE IF NOT EXISTS medicos (
    crm             VARCHAR(20)  PRIMARY KEY,
    nome            VARCHAR(120) NOT NULL,
    especialidade   VARCHAR(80)  NOT NULL,
    plano_atendido  VARCHAR(80),
    senha           VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS consultas (
    id             BIGSERIAL   PRIMARY KEY,
    crm_medico     VARCHAR(20) NOT NULL REFERENCES medicos (crm)    ON UPDATE CASCADE ON DELETE RESTRICT,
    cpf_paciente   VARCHAR(11) NOT NULL REFERENCES pacientes (cpf)  ON UPDATE CASCADE ON DELETE RESTRICT,
    data_consulta  DATE        NOT NULL,
    realizada      BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS lista_espera (
    id             BIGSERIAL    PRIMARY KEY,
    crm_medico     VARCHAR(20)  NOT NULL REFERENCES medicos (crm)   ON UPDATE CASCADE ON DELETE RESTRICT,
    cpf_paciente   VARCHAR(11)  NOT NULL REFERENCES pacientes (cpf) ON UPDATE CASCADE ON DELETE RESTRICT,
    data_consulta  DATE         NOT NULL,
    criada_em      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS consultas_realizadas (
    id_consulta      BIGINT        PRIMARY KEY REFERENCES consultas (id) ON DELETE RESTRICT,
    crm_medico       VARCHAR(20)   NOT NULL REFERENCES medicos (crm)   ON UPDATE CASCADE ON DELETE RESTRICT,
    cpf_paciente     VARCHAR(11)   NOT NULL REFERENCES pacientes (cpf) ON UPDATE CASCADE ON DELETE RESTRICT,
    data_consulta    DATE          NOT NULL,
    valor_consulta   NUMERIC(10,2) NOT NULL DEFAULT 0,
    sintomas         TEXT,
    tratamento       TEXT,
    exames           TEXT,
    medicamentos     TEXT,
    texto_avaliacao  TEXT          NOT NULL DEFAULT '',
    estrelas         SMALLINT      NOT NULL DEFAULT 0,
    CONSTRAINT ck_consultas_realizadas_estrelas CHECK (estrelas BETWEEN 0 AND 5),
    CONSTRAINT ck_consultas_realizadas_valor    CHECK (valor_consulta >= 0)
);

CREATE INDEX IF NOT EXISTS idx_medicos_especialidade           ON medicos (especialidade);
CREATE INDEX IF NOT EXISTS idx_consultas_medico_data           ON consultas (crm_medico, data_consulta, realizada);
CREATE INDEX IF NOT EXISTS idx_consultas_paciente              ON consultas (cpf_paciente, realizada);
CREATE INDEX IF NOT EXISTS idx_lista_espera_medico_data        ON lista_espera (crm_medico, data_consulta, id);
CREATE INDEX IF NOT EXISTS idx_consultas_realizadas_paciente   ON consultas_realizadas (cpf_paciente, estrelas);
CREATE INDEX IF NOT EXISTS idx_consultas_realizadas_medico     ON consultas_realizadas (crm_medico);
