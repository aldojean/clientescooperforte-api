package com.cooperforte.clientes.api.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

	private Long id;
	private String nome;
	private Integer mes;
	private Integer ano;
	private BigDecimal valor;
	private Long usuario;
	private String tipo;
	private String status;
}
