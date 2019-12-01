package com.cooperforte.clientes.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooperforte.clientes.api.dto.AtualizaStatusDTO;
import com.cooperforte.clientes.api.dto.ClienteDTO;
import com.cooperforte.clientes.exception.RegraNegocioException;
import com.cooperforte.clientes.model.entity.Cliente;
import com.cooperforte.clientes.model.entity.Usuario;
import com.cooperforte.clientes.model.enums.StatusCliente;
import com.cooperforte.clientes.model.enums.TipoCliente;
import com.cooperforte.clientes.service.ClienteService;
import com.cooperforte.clientes.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteResource {

	private final ClienteService service;
	private final UsuarioService usuarioService;
	
	@GetMapping
	public ResponseEntity buscar(
			@RequestParam(value ="nome" , required = false) String nome,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam("usuario") Long idUsuario
			) {
		
		Cliente clienteFiltro = new Cliente();
		clienteFiltro.setNome(nome);
		clienteFiltro.setMes(mes);
		clienteFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o Id informado.");
		}else {
			clienteFiltro.setUsuario(usuario.get());
		}
		
		List<Cliente> clientes = service.buscar(clienteFiltro);
		return ResponseEntity.ok(clientes);
	}
	
	@GetMapping("{id}")
	public ResponseEntity obterCliente( @PathVariable("id") Long id ) {
		return service.obterPorId(id)
					.map( cliente -> new ResponseEntity(converter(cliente), HttpStatus.OK) )
					.orElseGet( () -> new ResponseEntity(HttpStatus.NOT_FOUND) );
	}

	@PostMapping
	public ResponseEntity salvar( @RequestBody ClienteDTO dto ) {
		try {
			Cliente entidade = converter(dto);
			entidade = service.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar( @PathVariable("id") Long id, @RequestBody ClienteDTO dto ) {
		return service.obterPorId(id).map( entity -> {
			try {
				Cliente cliente = converter(dto);
				cliente.setId(entity.getId());
				service.atualizar(cliente);
				return ResponseEntity.ok(cliente);
			}catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet( () ->
			new ResponseEntity("Cliente não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
	}
	
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus( @PathVariable("id") Long id , @RequestBody AtualizaStatusDTO dto ) {
		return service.obterPorId(id).map( entity -> {
			StatusCliente statusSelecionado = StatusCliente.valueOf(dto.getStatus());
			
			if(statusSelecionado == null) {
				return ResponseEntity.badRequest().body("Não foi possível atualizar o status do cliente, envie um status válido.");
			}
			
			try {
				entity.setStatus(statusSelecionado);
				service.atualizar(entity);
				return ResponseEntity.ok(entity);
			}catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		
		}).orElseGet( () ->
		new ResponseEntity("Cliente não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar( @PathVariable("id") Long id ) {
		return service.obterPorId(id).map( entidade -> {
			service.deletar(entidade);
			return new ResponseEntity( HttpStatus.NO_CONTENT );
		}).orElseGet( () -> 
			new ResponseEntity("Cliente não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
	}
	
	private ClienteDTO converter(Cliente cliente) {
		return ClienteDTO.builder()
					.id(cliente.getId())
					.nome(cliente.getNome())
					.valor(cliente.getValor())
					.mes(cliente.getMes())
					.ano(cliente.getAno())
					.status(cliente.getStatus().name())
					.tipo(cliente.getTipo().name())
					.usuario(cliente.getUsuario().getId())
					.build();
					
	}
	
	private Cliente converter(ClienteDTO dto) {
		Cliente cliente = new Cliente();
		cliente.setId(dto.getId());
		cliente.setNome(dto.getNome());
		cliente.setAno(dto.getAno());
		cliente.setMes(dto.getMes());
		cliente.setValor(dto.getValor());
		
		Usuario usuario = usuarioService
			.obterPorId(dto.getUsuario())
			.orElseThrow( () -> new RegraNegocioException("Usuário não encontrado para o Id informado.") );
		
		cliente.setUsuario(usuario);

		if(dto.getTipo() != null) {
			cliente.setTipo(TipoCliente.valueOf(dto.getTipo()));
		}
		
		if(dto.getStatus() != null) {
			cliente.setStatus(StatusCliente.valueOf(dto.getStatus()));
		}
		
		return cliente;
	}
}
