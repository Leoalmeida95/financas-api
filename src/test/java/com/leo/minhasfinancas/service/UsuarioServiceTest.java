package com.leo.minhasfinancas.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.leo.minhasfinancas.model.repositorys.UsuarioRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
//teste unitario
	
	@Autowired
	UsuarioService service;
	
	@Autowired
	UsuarioRepository repository;
	
	
	@Test(expected = Test.None.class)//verificacao
	public void deveValidarEmail() {
		//cenaroio
		repository.deleteAll();
		
		//acao/execucao
		service.validarEmail("leo@email.com");
		
	}
}
