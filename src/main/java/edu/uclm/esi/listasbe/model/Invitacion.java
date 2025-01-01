package edu.uclm.esi.listasbe.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class Invitacion {
    @Id
    private String id;

    private String token; // Token único para la invitación

    @ManyToOne
    @JoinColumn(name = "lista_id", nullable = false)
    private Lista lista; 

    private boolean usada = false; 

    // Fecha de expiración de la invitación 
    private LocalDateTime fechaExpiracion;

    private String emailUsuario; // Email del usuario aceptado
    
    public Invitacion() {
    	this.id = UUID.randomUUID().toString();
    }
    
    public String getId() {
		return id;
	}
    
    public String getToken() {
		return token;
	}
    
	@JsonIgnore
    public Lista getLista() {
		return lista;
	}
    
    public boolean isUsada() {
		return usada;
	}
    
    public LocalDateTime getFechaExpiracion() {
		return fechaExpiracion;
	}
    
    public String getEmailUsuario() {
        return emailUsuario;
    }
    
    public void setId(String id) {
		this.id = id;
	}
    
    public void setToken(String token) {
		this.token = token;
	}
    
    public void setLista(Lista lista) {
		this.lista = lista;
	}
    
    public void setUsada(boolean usada) {
		this.usada = usada;
	}
    
    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}
    
    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

}