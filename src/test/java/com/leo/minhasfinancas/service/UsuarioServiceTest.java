package com.leo.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.leo.minhasfinancas.exceptions.ErroAutenticacao;
import com.leo.minhasfinancas.exceptions.RegraNegocioException;
import com.leo.minhasfinancas.model.entity.Usuario;
import com.leo.minhasfinancas.model.repositorys.UsuarioRepository;
import com.leo.minhasfinancas.service.impl.UsuarioServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
//teste unitario
//Utiliza-se mocks pois os testes de integracao já utilizam as entidades reais pra simular a persistência	

	String email = "leo@email.com";
	String senha = "123";
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	
	@Test(expected = Test.None.class)
	public void deveSalvarUmUsuario() {
		//cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1l)
				.nome("Leo")
				.email(email)
				.senha(senha)
				.build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//acao
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//verificacao
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("Leo");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo(email);
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo(senha);
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarExceptionAoSalvarUmUsuarioComEmailJaCadastrado() {
		//cenario
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		Usuario usuario = Usuario.builder()
				.email(email)
				.build();
	
		//acao
		service.salvarUsuario(usuario);
		
		//verificacao
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenario	
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
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {
		//cenario		
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//acao
		//acao
		Throwable excp = Assertions.catchThrowable(() ->  service.autenticar(email,senha));
		Assertions.assertThat(excp).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado para o email informado.");
		
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		//cenario	
		String senhaTeste = "1234";
		Usuario usuario = Usuario.builder()
				.email(email)
				.senha(senha)
				.build();
		
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//acao
		Throwable excp = Assertions.catchThrowable(() ->  service.autenticar(email,senhaTeste));
		Assertions.assertThat(excp).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida!");
	}
	
	@Test(expected = Test.None.class)//verificacao
	public void deveValidarEmail() {
		//cenaroio - evitando o retorno de exception
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//acao/execucao
		service.validarEmail(email);		
	}
	
	@Test(expected = RegraNegocioException.class)//verificacao
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//acao/execucao
		service.validarEmail(email);	
	}
}