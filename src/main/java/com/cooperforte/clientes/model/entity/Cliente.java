package com.cooperforte.clientes.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.cooperforte.clientes.model.enums.StatusCliente;
import com.cooperforte.clientes.model.enums.TipoCliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cliente", schema = "clientes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "mes")
	private Integer mes;
	
	@Column(name = "ano")
	private Integer ano;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	
	@Column(name = "valor")
	private BigDecimal valor;
	
	@Column(name = "data_cadastro")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	private LocalDate dataCadastro;
	
	@Column(name = "tipo")
	@Enumerated(value = EnumType.STRING)
	private TipoCliente tipo;
	
	@Column(name = "status")
	@Enumerated(value = EnumType.STRING)
	private StatusCliente status;

}
