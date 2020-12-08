package com.wi11san.minhasfinancas.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wi11san.minhasfinancas.exception.ErroAutenticacao;
import com.wi11san.minhasfinancas.exception.RegraNegocioException;
import com.wi11san.minhasfinancas.model.entity.Usuario;
import com.wi11san.minhasfinancas.model.repository.UsuarioRepository;
import com.wi11san.minhasfinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;

	@MockBean
	UsuarioRepository repository;

	@Test
	public void deveSalvarUmUsuario() throws Exception {
		//cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1l)
				.nome("nome")
				.email("email@email.com")
				.senha("senha").build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//acao
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//verificacao
		Assertions.assertNotNull(usuarioSalvo);
		Assertions.assertEquals(1l, usuarioSalvo.getId());
		Assertions.assertEquals("nome", usuarioSalvo.getNome());
		Assertions.assertEquals("email@email.com", usuarioSalvo.getEmail());
		Assertions.assertEquals("senha", usuarioSalvo.getSenha());
	}
	
	@Test
	public void naoDeveSalvarUsuarioComEmailJaCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {

		//cenario
		String email = "email@email.com"; 
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//acao
		service.salvarUsuario(usuario);
		
		//verificacao
		Mockito.verify(repository, Mockito.never()).save(usuario);
		});
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		Assertions.assertThrows(ErroAutenticacao.class, () -> {
			// cenario
			String senha = "senha";
			Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

			// acao
			service.autenticar("email@email.com", "123");
		});
	}

	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		Assertions.assertThrows(ErroAutenticacao.class, () -> {
			// cenario
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

			// acao
			service.autenticar("email@email.com", "senha");
		});
	}

	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		Assertions.assertDoesNotThrow(() -> {
			// cenario
			String email = "email@email.com";
			String senha = "senha";

			Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
			Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

			// acao
			service.autenticar(email, senha);

		});
	}

	@Test
	public void deveValidaEmail() {
		Assertions.assertDoesNotThrow(() -> {
			// cenario
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

			// ação
			service.validarEmail("usuario@gmail.com");
		});
	}

	@Test
	public void deveLancarErroAoValidarEmailQuandoExisitirEmailCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			// cenario
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

			// acao
			service.validarEmail("usuario@gmail.com");
		});
	}
}
