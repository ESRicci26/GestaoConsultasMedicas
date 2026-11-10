package br.com.javaricci.aplicacao.servico;

import br.com.javaricci.aplicacao.comando.DadosMedicoComando;
import br.com.javaricci.aplicacao.util.Documentos;
import br.com.javaricci.dominio.excecao.RecursoNaoEncontradoException;
import br.com.javaricci.dominio.excecao.RegraNegocioException;
import br.com.javaricci.dominio.modelo.Medico;
import br.com.javaricci.dominio.modelo.Paciente;
import br.com.javaricci.dominio.repositorio.MedicoRepositorio;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Caso de uso: gerenciamento do cadastro de médicos. */
@Service
public class MedicoServico {

    private final MedicoRepositorio medicoRepositorio;
    private final PasswordEncoder codificadorSenha;

    public MedicoServico(MedicoRepositorio medicoRepositorio, PasswordEncoder codificadorSenha) {
        this.medicoRepositorio = medicoRepositorio;
        this.codificadorSenha = codificadorSenha;
    }

    public void cadastrar(DadosMedicoComando dados) {
        String crm = dados.crm().trim();
        if (medicoRepositorio.existePorCrm(crm)) {
            throw new RegraNegocioException("CRM já cadastrado. Tente novamente com outro CRM.");
        }
        if (dados.senha() == null || dados.senha().isBlank()) {
            throw new RegraNegocioException("Informe uma senha para o cadastro.");
        }
        Medico medico = new Medico(crm, dados.nome().trim(), dados.especialidade().trim(),
                Documentos.nuloSeVazio(dados.planoAtendido()), codificadorSenha.encode(dados.senha()));
        medicoRepositorio.inserir(medico);
    }

    /** Atualiza os dados do médico. O CRM é o identificador e não pode ser alterado. */
    public Medico atualizar(String crm, DadosMedicoComando dados) {
        Medico medico = buscar(crm);
        medico.setNome(dados.nome().trim());
        medico.setEspecialidade(dados.especialidade().trim());
        medico.setPlanoAtendido(Documentos.nuloSeVazio(dados.planoAtendido()));
        if (dados.senha() != null && !dados.senha().isBlank()) {
            medico.setSenha(codificadorSenha.encode(dados.senha()));
        }
        medicoRepositorio.atualizar(medico);
        return medico;
    }

    public void excluir(String crm) {
        medicoRepositorio.excluir(crm);
    }

    public List<Medico> listar() {
        return medicoRepositorio.listar();
    }

    public Medico buscar(String crm) {
        return medicoRepositorio.buscarPorCrm(crm)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Médico não encontrado."));
    }

    /** Médicos com os quais o paciente pode agendar: todos, se ele não tem plano; senão os do seu plano. */
    public List<Medico> listarDisponiveisPara(Paciente paciente) {
        return paciente.possuiPlano() ? medicoRepositorio.listarPorPlano(paciente.getPlano()) : medicoRepositorio.listar();
    }
}
