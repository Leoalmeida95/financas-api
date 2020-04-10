package com.leo.minhasfinancas.api.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leo.minhasfinancas.api.controllers.UsuarioController;
import com.leo.minhasfinancas.api.dto.UsuarioDTO;
import com.leo.minhasfinancas.exceptions.ErroAutenticacao;
import com.leo.minhasfinancas.exceptions.RegraNegocioException;
import com.leo.minhasfinancas.model.entity.Usuario;
import com.leo.minhasfinancas.service.LancamentoService;
import com.leo.minhasfinancas.service.UsuarioService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc
public class UsuarioControllerTest {
	static final String API = "/api/usuarios";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UsuarioService service;
	
	@MockBean
	LancamentoService serviceLancamento;
	
	@Test
	public void deveAutenticarUmUsuario() throws Exception{
		//cenario
		String email = "ana@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();	
		Usuario u = Usuario.builder().id(1l).email(email).senha(senha).build();
		
		Mockito.when(service.autenticar(email, senha)).thenReturn(u);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//execucao e verificacao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/autenticar"))
																	.accept(JSON)
																	.contentType(JSON)
																	.content(json);
		
		mvc.perform(request)
		    .andExpect(MockMvcResultMatchers.status().isOk())
		    .andExpect(MockMvcResultMatchers.jsonPath("id").value(u.getId()))
		    .andExpect(MockMvcResultMatchers.jsonPath("nome").value(u.getNome()))
		    .andExpect(MockMvcResultMatchers.jsonPath("email").value(u.getEmail()));
	}
	
	@Test
	public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception{
		//cenario
		String email = "ana@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();	

		
		Mockito.when(service.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//execucao e verificacao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/autenticar"))
																	.accept(JSON)
																	.contentType(JSON)
																	.content(json);
		
		mvc.perform(request)
		    .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void deveCriarUmNovoUsuario() throws Exception{
		//cenario
		String email = "ana@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();	
		Usuario u = Usuario.builder().id(10l).email(email).senha(senha).build();
		
		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(u);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//execucao e verificacao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/salvar"))
																	.accept(JSON)
																	.contentType(JSON)
																	.content(json);
		
		mvc.perform(request)
		    .andExpect(MockMvcResultMatchers.status().isCreated())
		    .andExpect(MockMvcResultMatchers.jsonPath("id").value(u.getId()))
		    .andExpect(MockMvcResultMatchers.jsonPath("nome").value(u.getNome()))
		    .andExpect(MockMvcResultMatchers.jsonPath("email").value(u.getEmail()));
	}
	
	@Test
	public void deveRetornarBadRequestAoTentarCriarNovoUsuario() throws Exception{
		//cenario
		String email = "ana@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();	
		
		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//execucao e verificacao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/salvar"))
																	.accept(JSON)
																	.contentType(JSON)
																	.content(json);
		
		mvc.perform(request)
		    .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
}
