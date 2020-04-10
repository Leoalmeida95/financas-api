package com.leo.minhasfinancas.api.controllers;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leo.minhasfinancas.api.dto.UsuarioDTO;
import com.leo.minhasfinancas.exceptions.ErroAutenticacao;
import com.leo.minhasfinancas.exceptions.RegraNegocioException;
import com.leo.minhasfinancas.model.entity.Usuario;
import com.leo.minhasfinancas.service.LancamentoService;
import com.leo.minhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
		private final UsuarioService service;
		private final LancamentoService serviceLancamentos;
		
		@PostMapping("/autenticar")
		public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
			try {
				Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
				return ResponseEntity.ok(usuarioAutenticado);
			}
			catch(ErroAutenticacao ex) {
				return ResponseEntity.badRequest().body(ex.getMessage());
			}
		}

		@PostMapping("/salvar")
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
		
		@GetMapping("{id}/saldo")
		public ResponseEntity obterSaldo(@PathVariable("id") Long id) {
			Optional<Usuario> usuario = service.obterPorId(id);
			
			if(!usuario.isPresent()) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			
			BigDecimal saldo = serviceLancamentos.obterSaldoPorUsuario(id);
			return ResponseEntity.ok(saldo);
		}
}
