package com.leo.minhasfinancas.model.repositoys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leo.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,Long>{

}
