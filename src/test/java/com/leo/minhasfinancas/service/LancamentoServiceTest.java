package com.leo.minhasfinancas.service;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.leo.minhasfinancas.exceptions.RegraNegocioException;
import com.leo.minhasfinancas.model.entity.Lancamento;
import com.leo.minhasfinancas.model.entity.Usuario;
import com.leo.minhasfinancas.model.enums.TipoLancamento;
import com.leo.minhasfinancas.model.repositorys.LancamentoRepository;
import com.leo.minhasfinancas.model.repositorys.LancamentoRepositoryTest;
import com.leo.minhasfinancas.service.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {
	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveLancarErrosAoValidarUmLancamento() {
		Lancamento lancamento = new Lancamento();
		
		Throwable erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descricao válida.");
		
		lancamento.setDescricao("");
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descricao válida.");
		
		lancamento.setDescricao("Salario");
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");
		
		lancamento.setMes(0);
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");
		
		lancamento.setMes(13);
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");
		
		lancamento.setMes(1);
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");
		
		lancamento.setAno(123);
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");
		
		lancamento.setAno(2019);
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");
		
		lancamento.setUsuario(new Usuario());
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");
		
		lancamento.getUsuario().setId(1l);
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");
		
		lancamento.setValor(BigDecimal.ZERO);
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");
		
		lancamento.setValor(BigDecimal.valueOf(10));
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Tipo válido.");
		
		lancamento.setTipo(TipoLancamento.DESPESA);
		
	}
	
	@Test
	public void deveSalvarUmLancamento() {
		//canario
		Lancamento lancamentoAsalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoAsalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		Mockito.when(repository.save(lancamentoAsalvar)).thenReturn(lancamentoSalvo);
		
		//execucao
		Lancamento lancamento = service.salvar(lancamentoAsalvar);
		
		//verificacao
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
	}
	
	
}
