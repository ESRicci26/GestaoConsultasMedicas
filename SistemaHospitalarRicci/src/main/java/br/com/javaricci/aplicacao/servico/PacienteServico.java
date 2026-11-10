package br.com.javaricci.aplicacao.servico;

import br.com.javaricci.aplicacao.comando.DadosPacienteComando;
import br.com.javaricci.aplicacao.util.Documentos;
import br.com.javaricci.dominio.excecao.RecursoNaoEncontradoException;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import br.com.javaricci.dominio.modelo.Paciente;
import br.com.javaricci.dominio.repositorio.PacienteRepositorio;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Caso de uso: gerenciamento do cadastro de pacientes. */
@Service
public class PacienteServico {

    private final PacienteRepositorio pacienteRepositorio;
    private final PasswordEncoder codificadorSenha;

    public PacienteServico(PacienteRepositorio pacienteRepositorio, PasswordEncoder codificadorSenha) {
        this.pacienteRepositorio = pacienteRepositorio;
        this.codificadorSenha = codificadorSenha;
    }

    public void cadastrar(DadosPacienteComando dados) {
        String cpf = Documentos.somenteDigitos(dados.cpf());
        if (pacienteRepositorio.existePorCpf(cpf)) {
            throw new RegraNegocioException("CPF já cadastrado. Tente novamente com outro CPF.");
        }
        if (dados.senha() == null || dados.senha().isBlank()) {
            throw new RegraNegocioException("Informe uma senha para o cadastro.");
        }
        Paciente paciente = new Paciente(cpf, dados.nome().trim(), dados.idade(),
                Documentos.nuloSeVazio(dados.plano()), codificadorSenha.encode(dados.senha()));
        pacienteRepositorio.inserir(paciente);
    }

    /** Atualiza os dados do paciente. O CPF é o identificador e não pode ser alterado. */
    public Paciente atualizar(String cpf, DadosPacienteComando dados) {
        Paciente paciente = buscar(cpf);
        paciente.setNome(dados.nome().trim());
        paciente.setIdade(dados.idade());
        paciente.setPlano(Documentos.nuloSeVazio(dados.plano()));
        if (dados.senha() != null && !dados.senha().isBlank()) {
            paciente.setSenha(codificadorSenha.encode(dados.senha()));
        }
        pacienteRepositorio.atualizar(paciente);
        return paciente;
    }

    public void excluir(String cpf) {
        pacienteRepositorio.excluir(cpf);
    }

    public List<Paciente> listar() {
        return pacienteRepositorio.listar();
    }

    public Paciente buscar(String cpf) {
        return pacienteRepositorio.buscarPorCpf(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado."));
    }
}
