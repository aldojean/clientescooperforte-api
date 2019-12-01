package com.cooperforte.clientes.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooperforte.clientes.exception.RegraNegocioException;
import com.cooperforte.clientes.model.entity.Cliente;
import com.cooperforte.clientes.model.enums.StatusCliente;
import com.cooperforte.clientes.model.enums.TipoCliente;
import com.cooperforte.clientes.model.repository.ClienteRepository;
import com.cooperforte.clientes.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {
	
	private ClienteRepository repository;
	
	public ClienteServiceImpl(ClienteRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public Cliente salvar(Cliente cliente) {
		validar(cliente);
		cliente.setStatus(StatusCliente.CADASTRAMENTO);
		return repository.save(cliente);
	}

	@Override
	@Transactional
	public Cliente atualizar(Cliente cliente) {
		Objects.requireNonNull(cliente.getId());
		validar(cliente);
		return repository.save(cliente);
	}

	@Override
	@Transactional
	public void deletar(Cliente cliente) {
		Objects.requireNonNull(cliente.getId());
		repository.delete(cliente);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> buscar(Cliente clienteFiltro) {
		Example example = Example.of( clienteFiltro, 
				ExampleMatcher.matching()
					.withIgnoreCase()
					.withStringMatcher(StringMatcher.CONTAINING) );
		
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Cliente cliente, StatusCliente status) {
		cliente.setStatus(status);
		atualizar(cliente);
	}

	@Override
	public void validar(Cliente cliente) {
		
		if(cliente.getNome() == null || cliente.getNome().trim().equals("")) {
			throw new RegraNegocioException("Informe um nome válido.");
		}
		
		if(cliente.getMes() == null || cliente.getMes() < 1 || cliente.getMes() > 12) {
			throw new RegraNegocioException("Informe um Mês válido.");
		}
		
		if(cliente.getAno() == null || cliente.getAno().toString().length() != 4 ) {
			throw new RegraNegocioException("Informe um Ano válido.");
		}
		
		if(cliente.getUsuario() == null || cliente.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um Usuário.");
		}
		
		if(cliente.getValor() == null || cliente.getValor().compareTo(BigDecimal.ZERO) < 1 ) {
			throw new RegraNegocioException("Informe um Valor válido.");
		}
		
		if(cliente.getTipo() == null) {
			throw new RegraNegocioException("Informe um tipo de Lançamento.");
		}
	}

	@Override
	public Optional<Cliente> obterPorId(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterDiferencaPorUsuario(Long id) {
		
		BigDecimal privado = repository.obterSaldoPorTipoClienteEUsuarioEStatus(id, TipoCliente.PRIVADO, StatusCliente.EFETIVADO);
		BigDecimal publico = repository.obterSaldoPorTipoClienteEUsuarioEStatus(id, TipoCliente.PUBLICO, StatusCliente.EFETIVADO);
		
		if(privado == null) {
			privado = BigDecimal.ZERO;
		}
		
		if(publico == null) {
			publico = BigDecimal.ZERO;
		}
		
		return privado.subtract(publico);
	}

}
