package com.leo.minhasfinancas.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leo.minhasfinancas.api.dto.LancamentoDTO;
import com.leo.minhasfinancas.exceptions.RegraNegocioException;
import com.leo.minhasfinancas.model.entity.Lancamento;
import com.leo.minhasfinancas.model.entity.Usuario;
import com.leo.minhasfinancas.model.enums.StatusLancamento;
import com.leo.minhasfinancas.model.enums.TipoLancamento;
import com.leo.minhasfinancas.service.LancamentoService;
import com.leo.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {
	
	private LancamentoService serviceLancamento;
	private UsuarioService serviceUsuario;
	
	public LancamentoController(LancamentoService service) {
		this.serviceLancamento = service;
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento lancamento = converter(dto);
			lancamento = serviceLancamento.salvar(lancamento);
			return new ResponseEntity(lancamento, HttpStatus.CREATED);		
		}
		catch(RegraNegocioException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
			return serviceLancamento.obterPorId(id).map(e -> {
				try {
					Lancamento lancamento = converter(dto);
					lancamento.setId(e.getId());
					serviceLancamento.atualizar(lancamento);
					return ResponseEntity.ok(lancamento);
				}
				catch(RegraNegocioException ex) {
					return ResponseEntity.badRequest().body(ex.getMessage());
				}
			}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	private Lancamento converter(LancamentoDTO dto) {
		
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		Usuario usuario = serviceUsuario.obterPorId(dto.getUsuario())
		.orElseThrow(() -> new RegraNegocioException("Usuario do Lançamento não foi encontrado."));
		
		lancamento.setUsuario(usuario);
		lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		
		return lancamento;
	}
}
