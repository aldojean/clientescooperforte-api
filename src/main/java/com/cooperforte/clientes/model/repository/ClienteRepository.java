package com.cooperforte.clientes.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cooperforte.clientes.model.entity.Cliente;
import com.cooperforte.clientes.model.enums.StatusCliente;
import com.cooperforte.clientes.model.enums.TipoCliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	@Query( value = 
			  " select sum(l.valor) from Cliente l join l.usuario u "
			+ " where u.id = :idUsuario and l.tipo =:tipo and l.status = :status group by u " )
	BigDecimal obterSaldoPorTipoClienteEUsuarioEStatus(
			@Param("idUsuario") Long idUsuario, 
			@Param("tipo") TipoCliente tipo,
			@Param("status") StatusCliente status);
	
}
