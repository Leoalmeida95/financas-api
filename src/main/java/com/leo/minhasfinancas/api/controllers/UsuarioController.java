package com.leo.minhasfinancas.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leo.minhasfinancas.api.dto.UsuarioDTO;
import com.leo.minhasfinancas.exceptions.RegraNegocioException;
import com.leo.minhasfinancas.model.entity.Usuario;
import com.leo.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
		private UsuarioService service;
		
		public UsuarioController(UsuarioService service) {
			this.service = service;
		}

		@PostMapping
		public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
			Usuario usuario = Usuario.builder()
					.nome(dto.getNome())
					.email(dto.getEmail())
					.senha(dto.getSenha())
					.build();
			
			try {
				Usuario usuarioSalvo = service.salvarUsuario(usuario);
				return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
			}
			catch(RegraNegocioException ex) {
				return ResponseEntity.badRequest().body(ex.getMessage());
			}
		}
}
