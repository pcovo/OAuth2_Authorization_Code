package com.oauth.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class TokenController {

	/**
	 * Documentação Brasil Cidadão (brasilcidadao-software-development-kit-v1.8.pdf – página 13)
		A requisição é feita através de um GET para o endereço 
		https://(url do provider brasil cidadão)/scp/authorize  
		passando as seguintes informações: 
		1. Parâmetros query: 
		◦ response_type: code; 
		◦ client_id: <CLIENT_ID> fornecido pelo Brasil Cidadão para a aplicação cadastrada; 
		◦ scope: um ou mais escopos inseridos para a aplicação cadastrada. Se for mais de um, esta informação deve vir separada pelo caracter "+". 
		◦ redirect_uri: <URI de retorno cadastrada para a aplicação cliente>. 
		◦ nonce: <sequência alfa numérica aleatória>. 
		◦ state: <sequência alfa numérica aleatória>. Item não obrigatório.
		Exemplo de requisição:
		https://(url do provider brasil cidadão)/scp/authorize?response_type=code&client_id=ec4318d6f797-4d65-b4f7-39a33bf4d544&scope=openid+brasil_cidadao& redirect_uri=http://appcliente.com.br/phpcliente/loginecidadao.Php&nonce=3ed8657fd74c&state=3 58578ce6728b
	 */
	@RequestMapping(value = "/getTokenService", method = RequestMethod.GET)
	public ResponseEntity<Object> getTokenService() throws Exception {
			URI uri = null;
			try {
				uri = new URI("http://localhost:8080/oauth/authorize?response_type=code&client_id=client-id&redirect_uri=http://localhost:8090/setToken&scope=read&state=123456");
			} catch (URISyntaxException e) {
				e.printStackTrace();
				throw e;
			}
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(uri);
			return (new ResponseEntity<>(httpHeaders, HttpStatus.TEMPORARY_REDIRECT));
	}
	
	@RequestMapping(value = "/setToken", method = RequestMethod.GET)
	@ResponseBody
	public String setToken(@RequestParam("code") String code, @RequestParam("state") String state) throws JsonProcessingException, IOException {
		ResponseEntity<String> response = null;

		RestTemplate restTemplate = new RestTemplate();

		String credentials = "client-id:client-secret";
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Basic " + encodedCredentials);

		HttpEntity<String> request = new HttpEntity<String>(headers);

		String access_token_url = "http://localhost:8080/oauth/token";
		access_token_url += "?code=" + code;
		access_token_url += "&grant_type=authorization_code";
		access_token_url += "&redirect_uri=http://localhost:8090/setToken";

		response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(response.getBody());
		String token = node.path("access_token").asText();
		
		return token;
	}
}

