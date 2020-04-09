package com.leo.minhasfinancas.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.leo.minhasfinancas.exceptions.RegraNegocioException;
import com.leo.minhasfinancas.model.repositorys.UsuarioRepository;
import com.leo.minhasfinancas.service.impl.UsuarioServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
//teste unitario
//Utiliza-se mocks pois os testes de integracao já utilizam as entidades reais pra simular a persistência	

	
	UsuarioService service;
	UsuarioRepository repository;
	
	@Before
	public void setUp()
	{
		repository = Mockito.mock(UsuarioRepository.class);
		service = new UsuarioServiceImpl(repository);
	}
	
	@Test(expected = Test.None.class)//verificacao
	public void deveValidarEmail() {
		//cenaroio - evitando o retorno de exception
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//acao/execucao
		service.validarEmail("leo@email.com");		
	}
	
	@Test(expected = RegraNegocioException.class)//verificacao
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//acao/execucao
		service.validarEmail("leo@email.com");
		
	}
}
