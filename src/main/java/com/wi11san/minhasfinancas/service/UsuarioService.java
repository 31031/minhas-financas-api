package com.wi11san.minhasfinancas.service;

import java.util.Optional;

import com.wi11san.minhasfinancas.exception.RegraNegocioException;
import com.wi11san.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha) throws RegraNegocioException;
	
	Usuario salvarUsuario(Usuario usuario) throws RegraNegocioException;
	
	void validarEmail(String email) throws RegraNegocioException;
	
	Optional<Usuario> buscarPorId(Long id);
}
