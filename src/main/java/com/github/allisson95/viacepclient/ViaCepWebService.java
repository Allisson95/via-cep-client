package com.github.allisson95.viacepclient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe resposável por fazer a validação do cep e fazer a comunicação com o
 * api <a href="https://viacep.com.br/">ViaCEP</a>.
 *
 * @author Allisson Alves Batista Nunes
 *
 */
public class ViaCepWebService {

	private final String URL_VIA_CEP = "http://viacep.com.br/ws/%s/piped";
	private final EnderecoUtil enderecoUtil = EnderecoUtil.getInstance();
	private EnderecoViaCep enderecoViaCep;

	/**
	 * Construtor da classe ViaCepWebService. <br />
	 * <br />
	 * Recebe como parâmentro o CEP <b>SEM MÁSCARA (APENAS NUMEROS)</b>. <br />
	 * <br />
	 * Se nenhuma exceção for lançada, o endereço preenchido pode ser obtido através
	 * do método {@link getEnderecoViaCep()}. <br />
	 * <br />
	 *
	 * @param cep
	 *
	 * @throws RuntimeException
	 *             se o CEP for nulo, vazio, inválido ou se não tiver conexão com a
	 *             internet.
	 */
	public ViaCepWebService(String cep) {
		if (validaCep(cep)) {
			this.buscarCep(cep);
		}
	}

	private boolean validaCep(String cep) {
		if (cep == null || cep.trim().isEmpty()) {
			throw new RuntimeException("Cep não pode ser nulo ou vazio.");
		}
		Pattern pCep = Pattern.compile("\\d{8}");
		Matcher matcher = pCep.matcher(cep);
		if (matcher.matches()) {
			return true;
		} else {
			throw new RuntimeException(String.format("Cep %s inválido!", cep));
		}
	}

	private void buscarCep(String cep) {
		String enderecoPiped = this.consultarCep(cep);
		if (enderecoPiped.contains("error:true")) {
			throw new RuntimeException("Cep não encontrado.");
		}
		this.enderecoViaCep = this.enderecoUtil.getEnderecoViaCep(enderecoPiped);
	}

	private String consultarCep(String cep) {
		try {
			URL url = new URL(String.format(URL_VIA_CEP, cep));
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			InputStream is = httpURLConnection.getInputStream();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

			StringBuilder resultado = new StringBuilder();

			bufferedReader.lines().forEach(linha -> resultado.append(linha.trim()));

			return resultado.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public EnderecoViaCep getEnderecoViaCep() {
		return enderecoViaCep;
	}

}
