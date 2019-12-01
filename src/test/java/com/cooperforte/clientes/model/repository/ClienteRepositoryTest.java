package com.cooperforte.clientes.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.cooperforte.clientes.model.entity.Cliente;
import com.cooperforte.clientes.model.enums.StatusCliente;
import com.cooperforte.clientes.model.enums.TipoCliente;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class ClienteRepositoryTest {

	@Autowired
	ClienteRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvarUmCliente() {
		Cliente cliente = criarCliente();
		
		cliente = repository.save(cliente);
		
		assertThat(cliente.getId()).isNotNull();
	}

	@Test
	public void deveDeletarUmCliente() {
		Cliente cliente = criarEPersistirUmCliente();
		
		cliente = entityManager.find(Cliente.class, cliente.getId());
		
		repository.delete(cliente);
		
		Cliente clienteInexistente = entityManager.find(Cliente.class, cliente.getId());
		assertThat(clienteInexistente).isNull();
	}

	
	@Test
	public void deveAtualizarUmCliente() {
		Cliente cliente = criarEPersistirUmCliente();
		
		cliente.setAno(2018);
		cliente.setNome("Teste Atualizar");
		cliente.setStatus(StatusCliente.CANCELADO);
		
		repository.save(cliente);
		
		Cliente clienteAtualizado = entityManager.find(Cliente.class, cliente.getId());
		
		assertThat(clienteAtualizado.getAno()).isEqualTo(2018);
		assertThat(clienteAtualizado.getNome()).isEqualTo("Teste Atualizar");
		assertThat(clienteAtualizado.getStatus()).isEqualTo(StatusCliente.CANCELADO);
	}
	
	@Test
	public void deveBuscarUmClientePorId() {
		Cliente cliente = criarEPersistirUmCliente();
		
		Optional<Cliente> clienteEncontrado = repository.findById(cliente.getId());
		
		assertThat(clienteEncontrado.isPresent()).isTrue();
	}

	private Cliente criarEPersistirUmCliente() {
		Cliente cliente = criarCliente();
		entityManager.persist(cliente);
		return cliente;
	}
	
	private Cliente criarCliente() {
		return Cliente.builder()
									.ano(2019)
									.mes(1)
									.nome("cliente qualquer")
									.valor(BigDecimal.valueOf(10))
									.tipo(TipoCliente.PUBLICO)
									.status(StatusCliente.CADASTRAMENTO)
									.dataCadastro(LocalDate.now())
									.build();
	}
	
	
	
	
	
}
