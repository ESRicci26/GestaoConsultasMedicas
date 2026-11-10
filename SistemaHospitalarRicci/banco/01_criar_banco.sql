-- =====================================================================
-- Sistema Hospitalar Ricci - Criação do banco de dados (PostgreSQL 14+)
-- Execute conectado ao banco "postgres" com um usuário administrador:
--   psql -U postgres -f banco/01_criar_banco.sql
-- O comando \gexec cria o banco apenas se ele ainda não existir.
-- =====================================================================

SELECT 'CREATE DATABASE hospital WITH ENCODING ''UTF8'' TEMPLATE template0'
WHERE NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'hospital')

