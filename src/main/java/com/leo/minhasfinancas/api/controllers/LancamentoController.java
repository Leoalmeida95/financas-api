package com.leo.minhasfinancas.api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leo.minhasfinancas.api.dto.AtualizaStatusLancamentoDTO;
import com.leo.minhasfinancas.api.dto.LancamentoDTO;
import com.leo.minhasfinancas.exceptions.RegraNegocioException;
import com.leo.minhasfinancas.model.entity.Lancamento;
import com.leo.minhasfinancas.model.entity.Usuario;
import com.leo.minhasfinancas.model.enums.StatusLancamento;
import com.leo.minhasfinancas.model.enums.TipoLancamento;
import com.leo.minhasfinancas.service.LancamentoService;
import com.leo.minhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lancamentos")
public class LancamentoController {
	
	private final LancamentoService serviceLancamento;
	private final UsuarioService serviceUsuario;

	
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
	
	@PutMapping("/{id}/atualizar-status")
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusLancamentoDTO dto)
	{
		return serviceLancamento.obterPorId(id).map(e -> {
			StatusLancamento status = StatusLancamento.valueOf(dto.getStatus());
			if(status == null) {
				return ResponseEntity.badRequest().body("Status inválido!");
			}	
			try {
				serviceLancamento.atualizarStatus(e, status);
				return ResponseEntity.ok(e);
			}
			catch(RegraNegocioException ex) {
				return ResponseEntity.badRequest().body(ex.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@GetMapping("{id}")
	public ResponseEntity obterLancamento(@PathVariable("id") Long id) {
		return serviceLancamento.obterPorId(id).map(lan -> {
			try {
					return new ResponseEntity(converter(lan) ,HttpStatus.OK);
				}
			catch(RegraNegocioException ex) {
				return ResponseEntity.badRequest().body(ex.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));		
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity delete(@PathVariable("id") Long id) {
		return serviceLancamento.obterPorId(id).map(e -> {
			try {
					serviceLancamento.deletar(e);
					return new ResponseEntity(HttpStatus.NO_CONTENT);
				}
			catch(RegraNegocioException ex) {
				return ResponseEntity.badRequest().body(ex.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));		
	}
	
	@GetMapping
	public ResponseEntity buscar(@RequestParam(value ="descricao", required = false) String descricao, 
								@RequestParam(value ="mes", required = false) Integer mes,
								@RequestParam(value ="ano", required = false) Integer ano,
								@RequestParam(value ="tipo", required = false) String tipo,
								@RequestParam("usuario") Long idUsuario) {
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		lancamentoFiltro.setTipo(TipoLancamento.valueOf(tipo));
		
		Optional<Usuario> usuario = serviceUsuario.obterPorId(idUsuario);
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário do Lançamento não encontrado.");
		}else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = serviceLancamento.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
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
		
		if(dto.getTipo() != null)
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		
		if(dto.getStatus() != null)
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		
		return lancamento;
	}
	
	private LancamentoDTO converter(Lancamento entidade) {
		
		LancamentoDTO lancamento = new LancamentoDTO();
		lancamento.setId(entidade.getId());
		lancamento.setDescricao(entidade.getDescricao());
		lancamento.setAno(entidade.getAno());
		lancamento.setMes(entidade.getMes());
		lancamento.setValor(entidade.getValor());
		lancamento.setUsuario((entidade.getUsuario().getId()));
		lancamento.setTipo(entidade.getTipo().name());
		lancamento.setStatus(entidade.getStatus().name());
		
		return lancamento;
	}
}
