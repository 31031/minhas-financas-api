package com.wi11san.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wi11san.minhasfinancas.model.entity.Lancamento;
import com.wi11san.minhasfinancas.model.enums.StatusLancamento;
import com.wi11san.minhasfinancas.model.enums.TipoLancamento;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamento = criarLamcamento();
		
		lancamento = repository.save(lancamento);
		
		Assertions.assertThat(lancamento.getId()).isNotNull();
	}

	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = criarPersistirLancamento();
		
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
	
		repository.delete(lancamento);
		
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
		Assertions.assertThat(lancamentoInexistente).isNull();
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamento = criarPersistirLancamento();
		
		lancamento.setAno(2020);
		lancamento.setDescricao("Teste atualizado");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		repository.save(lancamento);
			
		Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
	
		Assertions.assertThat(lancamentoAtualizado.getAno()).isEqualTo(2020);
		Assertions.assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste atualizado");
		Assertions.assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
	}

	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lancamento lancamento = criarPersistirLancamento();
		
		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		Assertions.assertThat(lancamentoEncontrado.isPresent()).isTrue();
	}
	
	private Lancamento criarPersistirLancamento() {
		Lancamento lancamento = criarLamcamento();
		entityManager.persist(lancamento);
		return lancamento;
	}
	
	public static Lancamento criarLamcamento() {
		return Lancamento.builder()
				.ano(2021)
				.mes(1)
				.descricao("Lançamento Qualquer")
				.valor(BigDecimal.valueOf(10))
				.tipo(TipoLancamento.RECEITA)
				.status(StatusLancamento.PENDENTE)
				.dataCadastro(LocalDate.now()).build();
	}
	
}
