package com.leo.minhasfinancas.model.repositorys;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.leo.minhasfinancas.model.entity.Lancamento;
import com.leo.minhasfinancas.model.enums.StatusLancamento;
import com.leo.minhasfinancas.model.enums.TipoLancamento;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
//teste de integração
	
	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager manager;
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = criarLancamento();
		lancamento = manager.persist(lancamento);
		
		lancamento = manager.find(Lancamento.class, lancamento.getId());
		
		repository.delete(lancamento);
		
		Lancamento inexistente = manager.find(Lancamento.class, lancamento.getId());
		Assertions.assertThat(inexistente).isNull();
	}
	
	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		Assertions.assertThat(lancamento.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		Optional<Lancamento> encontrado = repository.findById(lancamento.getId());
		
		Assertions.assertThat(encontrado.isPresent()).isTrue();
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		lancamento.setAno(2018);
		lancamento.setDescricao("Atualização");
		
		repository.save(lancamento);
		
		Lancamento lancamentoAtualizado = manager.find(Lancamento.class, lancamento.getId());
		
		Assertions.assertThat(lancamentoAtualizado.getAno()).isEqualTo(2018);
	}
	
	private Lancamento criarEPersistirUmLancamento()
	{
		return repository.save(criarLancamento());
	}
	
	public static Lancamento criarLancamento() {
		return Lancamento.builder()
		.ano(2019)
		.mes(1)
		.descricao("lanca")
	    .valor(BigDecimal.valueOf(10))
	    .tipo(TipoLancamento.RECEITA)
	    .status(StatusLancamento.PENDENTE)
	    .dataCadastro(LocalDate.now())
	    .build();
	}
}
