package edu.uclm.esi.listasbe.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.springframework.stereotype.Service;

@Service
public class ProxyDEU {
	
	public Map<String, Boolean> validar(String token) {
	    String url = "http://localhost:8080/tokens/validar";
	    Map<String, Boolean> resultado = new HashMap<>();
	    resultado.put("isValid", false);
	    resultado.put("isPremium", false);
	    
	    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
	        HttpPut httpPut = new HttpPut(url);
	        httpPut.setEntity(new StringEntity(token));
	        httpPut.setHeader("Content-Type", "text/plain");

	        try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
	            if (response.getCode() == 200) {
	                String responseBody = EntityUtils.toString(response.getEntity());
	                
	                resultado.put("isValid", true);
	                resultado.put("isPremium", Boolean.parseBoolean(responseBody));
	            }           
	        }
	    } catch (Exception e) {
	        System.err.println("Error al validar el token: " + e.getMessage());
	    }
	    
	    return resultado;
	}
}