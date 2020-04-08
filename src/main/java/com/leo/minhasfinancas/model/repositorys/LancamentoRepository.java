package com.leo.minhasfinancas.model.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leo.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento,Long>{

}
