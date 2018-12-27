package com.oauth.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class TokenController {

	@RequestMapping(value="/usuario", method = RequestMethod.GET)
	public Principal usuario(Principal principal) {
	    return principal;
	}
	
	@RequestMapping(value = "/getToken", method = RequestMethod.GET)
	public ModelAndView getEmployeeInfo() {
		return new ModelAndView("solicitarToken");
	}
	
	@RequestMapping(value = "/setToken", method = RequestMethod.GET)
	public ModelAndView setToken(@RequestParam("code") String code, @RequestParam("state") String state) throws JsonProcessingException, IOException {
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
		
		ModelAndView model = new ModelAndView("receberToken");
		model.addObject("token",token);
		return model;
	}
}

