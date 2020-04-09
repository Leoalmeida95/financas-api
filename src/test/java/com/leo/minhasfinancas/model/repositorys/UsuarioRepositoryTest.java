package com.leo.minhasfinancas.model.repositorys;

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

import com.leo.minhasfinancas.model.entity.Usuario;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
//teste de integração
	
		String email = "leo@email.com";
		String senha = "123";
	
		@Autowired
		UsuarioRepository repository;
		
		@Autowired
		TestEntityManager manager;

		@Test
		public void deveVerificarAExistenciaDeUmEmail() {
			//cenário
			Usuario usu = criarUsuario();
			manager.persist(usu);
			
			//ação/execução
			boolean result = repository.existsByEmail(email);
			
			//verificacao
			Assertions.assertThat(result).isTrue();
		}
		
		@Test
		public void DeveRetornarFalsoQuandoNaoHouverUsuarioCadastroComOEmail() {
			//cenaroio - nao mais necessario devido ao uso do TestEntityManager
			//repository.deleteAll();
			
			//ação/execução
			boolean result = repository.existsByEmail(email);
			
			//verificacao
			Assertions.assertThat(result).isFalse();
		}
		
		@Test
		public void devePersistirUmUsuarioNaBaseDeDados() {
			//cenário
			Usuario usu = criarUsuario();
			
			//ação/execução
			manager.persist(usu);
			
			//verificacao
			Optional<Usuario> result = repository.findByEmail(email);
			Assertions.assertThat(result.isPresent()).isTrue();
		}
		
		@Test
		public void deveBuscarUmUsuarioPorEmail() {
			//cenário
			Usuario usu = criarUsuario();
			
			//ação/execução
			Usuario usuario = manager.persist(usu);
			
			//verificacao
			Assertions.assertThat(usuario.getId()).isNotNull();
		}
		
		@Test
		public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
			
			//verificacao
			Optional<Usuario> result = repository.findByEmail(email);
			Assertions.assertThat(result.isPresent()).isFalse();
		}
		
		public static Usuario criarUsuario() {
			return Usuario.builder()
					.nome("Leo")
					.email("leo@email.com")
					.senha("123")
					.build();
		}

}
