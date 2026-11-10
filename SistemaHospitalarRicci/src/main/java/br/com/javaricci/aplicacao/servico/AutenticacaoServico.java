package br.com.javaricci.aplicacao.servico;

import br.com.javaricci.aplicacao.configuracao.AdministradorPropriedades;
import br.com.javaricci.aplicacao.seguranca.UsuarioLogado;
import br.com.javaricci.aplicacao.util.Documentos;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import br.com.javaricci.dominio.repositorio.MedicoRepositorio;
import br.com.javaricci.dominio.repositorio.PacienteRepositorio;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Caso de uso: autenticação de pacientes, médicos e administrador. */
@Service
public class AutenticacaoServico {

    private static final String MENSAGEM_CREDENCIAIS_INVALIDAS = "Credenciais inválidas. Tente novamente.";

    private final PacienteRepositorio pacienteRepositorio;
    private final MedicoRepositorio medicoRepositorio;
    private final PasswordEncoder codificadorSenha;
    private final AdministradorPropriedades administrador;

    public AutenticacaoServico(PacienteRepositorio pacienteRepositorio, MedicoRepositorio medicoRepositorio,
                               PasswordEncoder codificadorSenha, AdministradorPropriedades administrador) {
        this.pacienteRepositorio = pacienteRepositorio;
        this.medicoRepositorio = medicoRepositorio;
        this.codificadorSenha = codificadorSenha;
        this.administrador = administrador;
    }

    public UsuarioLogado autenticarPaciente(String cpf, String senha) {
        return pacienteRepositorio.buscarPorCpf(Documentos.somenteDigitos(cpf))
                .filter(paciente -> codificadorSenha.matches(senha, paciente.getSenha()))
                .map(UsuarioLogado::doPaciente)
                .orElseThrow(() -> new RegraNegocioException(MENSAGEM_CREDENCIAIS_INVALIDAS));
    }

    public UsuarioLogado autenticarMedico(String crm, String senha) {
        return medicoRepositorio.buscarPorCrm(crm.trim())
                .filter(medico -> codificadorSenha.matches(senha, medico.getSenha()))
                .map(UsuarioLogado::doMedico)
                .orElseThrow(() -> new RegraNegocioException(MENSAGEM_CREDENCIAIS_INVALIDAS));
    }

    public UsuarioLogado autenticarAdministrador(String usuario, String senha) {
        boolean usuarioConfere = iguais(usuario, administrador.usuario());
        boolean senhaConfere = iguais(senha, administrador.senha());
        if (!usuarioConfere || !senhaConfere) {
            throw new RegraNegocioException(MENSAGEM_CREDENCIAIS_INVALIDAS);
        }
        return UsuarioLogado.administrador(administrador.usuario());
    }

    /** Comparação em tempo constante para não vazar informação por diferença de tempo. */
    private boolean iguais(String informado, String esperado) {
        return MessageDigest.isEqual(
                informado.getBytes(StandardCharsets.UTF_8), esperado.getBytes(StandardCharsets.UTF_8));
    }
}
