package com.leo.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.leo.minhasfinancas.exceptions.RegraNegocioException;
import com.leo.minhasfinancas.model.entity.Usuario;
import com.leo.minhasfinancas.model.repositorys.UsuarioRepository;
import com.leo.minhasfinancas.service.impl.UsuarioServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
//teste unitario
//Utiliza-se mocks pois os testes de integracao já utilizam as entidades reais pra simular a persistência	

	
	UsuarioService service;
	
	@MockBean
	UsuarioRepository repository;
	
	@Before
	public void setUp()
	{
		service = new UsuarioServiceImpl(repository);
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenario
		String email = "leo@email.com";
		String senha = "123";
		
		Usuario usuario = Usuario.builder()
				.email(email)
				.senha(senha)
				.build();
		
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//acao
		Usuario result = service.autenticar(email,senha);
		
		//verificacao
		Assertions.assertThat(result).isNotNull();
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