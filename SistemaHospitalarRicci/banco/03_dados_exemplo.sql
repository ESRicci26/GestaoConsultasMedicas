-- =====================================================================
-- Sistema Hospitalar Ricci - Dados de exemplo (OPCIONAL, apenas para testes)
-- As senhas estão criptografadas com BCrypt.
--   Médico   : CRM 123456       / senha medico123
--   Paciente : CPF 52998224725  / senha paciente123  (plano "Unimed")
--   Paciente : CPF 11144477735  / senha paciente123  (sem plano)
-- O administrador é configurado em application.properties (padrão admin / admin123).
-- =====================================================================

INSERT INTO medicos (crm, nome, especialidade, plano_atendido, senha) VALUES
    ('123456', 'Dra. Helena Ricci', 'Cardiologia', 'Unimed', '$2a$10$wur4Gctz.WQBxFUHvvAlAe.zad5IK9NS6ys0ra7wPOB6sGchHt0cu'),
    ('654321', 'Dr. Paulo Andrade', 'Clínica Geral', NULL,   '$2a$10$wur4Gctz.WQBxFUHvvAlAe.zad5IK9NS6ys0ra7wPOB6sGchHt0cu')
ON CONFLICT (crm) DO NOTHING;

INSERT INTO pacientes (cpf, nome, idade, plano, senha) VALUES
    ('52998224725', 'Maria Souza', 34, 'Unimed', '$2a$10$4/ATR3lWwqxQrpM2McqW2urKhj6nARQ3zwChb.goGnepNYXUyXY5y'),
    ('11144477735', 'João Lima',   52, NULL,     '$2a$10$4/ATR3lWwqxQrpM2McqW2urKhj6nARQ3zwChb.goGnepNYXUyXY5y')
ON CONFLICT (cpf) DO NOTHING;
