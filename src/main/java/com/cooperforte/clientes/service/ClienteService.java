package com.cooperforte.clientes.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.cooperforte.clientes.model.entity.Cliente;
import com.cooperforte.clientes.model.enums.StatusCliente;

public interface ClienteService {

	Cliente salvar(Cliente cliente);
	
	Cliente atualizar(Cliente cliente);
	
	void deletar(Cliente cliente);
	
	List<Cliente> buscar( Cliente clienteFiltro );
	
	void atualizarStatus(Cliente cliente, StatusCliente status);
	
	void validar(Cliente cliente);
	
	Optional<Cliente> obterPorId(Long id);
	
	BigDecimal obterDiferencaPorUsuario(Long id);
}