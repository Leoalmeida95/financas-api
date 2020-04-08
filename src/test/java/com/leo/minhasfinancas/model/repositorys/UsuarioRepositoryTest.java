package com.leo.minhasfinancas.model.repositorys;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.leo.minhasfinancas.model.entity.Usuario;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UsuarioRepositoryTest {
//teste de integração
	
		@Autowired
		UsuarioRepository repository;

		@Test
		public void deveVerificarAExistenciaDeUmEmail() {
			//cenário
			Usuario usu = Usuario.builder().nome("Leo").email("leo@email.com").build();
			repository.save(usu);
			
			//ação/execução
			boolean result = repository.existsByEmail("leo@email.com");
			
			//verificacao
			Assertions.assertThat(result).isTrue();
		}
}
