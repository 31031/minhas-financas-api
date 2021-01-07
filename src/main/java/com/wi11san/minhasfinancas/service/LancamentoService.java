package com.wi11san.minhasfinancas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.wi11san.minhasfinancas.exception.RegraNegocioException;
import com.wi11san.minhasfinancas.model.entity.Lancamento;
import com.wi11san.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {

	List<Lancamento> buscar(Lancamento lancamentoFiltro);

	Lancamento salvar (Lancamento lancamento) throws RegraNegocioException;
	
	Lancamento atualizar (Lancamento lancamento) throws RegraNegocioException;
	
	void deletar(Lancamento lancamento);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status) throws RegraNegocioException;
	
	void validar(Lancamento lancamento) throws RegraNegocioException;
	
	Optional<Lancamento> obterPorId(Long id);
	
	BigDecimal obterSaldoPorUsuario(Long id);
}
