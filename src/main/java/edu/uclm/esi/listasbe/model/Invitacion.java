package edu.uclm.esi.listasbe.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Invitacion {
    @Id
    private String id;

    private String token; // Token único para la invitación

    @ManyToOne
    @JoinColumn(name = "lista_id", nullable = false)
    private Lista lista; // Lista asociada a la invitación

    private boolean usada = false; // Marca si la invitación ya ha sido usada

    // Fecha de expiración de la invitación (opcional, para controlar caducidad)
    private LocalDateTime fechaExpiracion;

    // Getters y setters
    
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

}