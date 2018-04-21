package com.github.allisson95.viacepclient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe utilitaria, responsável por preencher o objeto EnderecoViaCep com os
 * dados retornados da API.
 *
 * @author Allisson Alves Batista Nunes
 *
 */
public class EnderecoUtil {

	private static EnderecoUtil enderecoUtil;

	private Map<CampoEndereco, String> enderecoMap = new HashMap<>();

	private EnderecoViaCep enderecoViaCep;

	private EnderecoUtil() {
	}

	public static EnderecoUtil getInstance() {
		return Optional.ofNullable(enderecoUtil).orElse(new EnderecoUtil());
	}

	public EnderecoViaCep getEnderecoViaCep(String enderecoPiped) {
		String[] partesDoEndereco = enderecoPiped.split("\\|");
		this.separarEndereco(partesDoEndereco);
		this.preencherEndereco();
		return Optional.ofNullable(this.enderecoViaCep).orElse(new EnderecoViaCep());
	}

	private void separarEndereco(String[] partesDoEndereco) {
		CampoEndereco[] camposEnderecos = CampoEndereco.values();
		for (int i = 0; i < partesDoEndereco.length; i++) {
			String parteDoEndereco = partesDoEndereco[i];
			CampoEndereco campo = camposEnderecos[i];

			String[] split = parteDoEndereco.split(":");

			this.enderecoMap.put(campo, ((split.length == 2) ? split[1] : ""));
		}
	}

	private void preencherEndereco() {
		if (this.enderecoViaCep == null) {
			this.enderecoViaCep = new EnderecoViaCep();
		}

		List<Method> setMethods = Arrays.asList(EnderecoViaCep.class.getDeclaredMethods()).stream()
				.filter(m -> m.getName().startsWith("set")).collect(Collectors.toList());

		setMethods.forEach(sm -> {
			try {
				sm.invoke(this.enderecoViaCep,
						this.enderecoMap.get(CampoEndereco.valueOf(sm.getName().substring(3).toUpperCase())));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		});
	}

}
