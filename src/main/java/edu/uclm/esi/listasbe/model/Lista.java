package edu.uclm.esi.listasbe.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Lista {
	
	@Id @Column(length = 36)
	private String id;
	@Column(length = 80)
	private String nombre;
	
	private String propietario;

	@OneToMany(mappedBy = "lista", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Producto> productos;
	
	@OneToMany(mappedBy = "lista", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Invitacion> invitaciones;
	
	@ElementCollection
	@CollectionTable(name = "lista_emails_usuarios", joinColumns = @JoinColumn(name = "lista_id"))
	@Column(name = "emails_usuarios")
	private List<String> emailsUsuarios;
	
	public Lista() {
		this.id = UUID.randomUUID().toString();
		this.productos = new ArrayList<>();
		this.emailsUsuarios = new ArrayList<>();
		this.invitaciones = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getPropietario() {
		return propietario;
	}
	
	public void setPropietario(String propietario) {
		this.propietario = propietario;
	}
	
	public void add(Producto producto) {
		if (!this.productos.contains(producto)) {
			this.productos.add(0, producto);
	    }
	}
	
	public List<Producto> getProductos() {
		return productos;
	}
	
	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}
	
	public List<Invitacion> getInvitaciones() {
		return invitaciones;
	}
	
	public void setInvitaciones(List<Invitacion> invitaciones) {
		this.invitaciones = invitaciones;
	}
	
	public List<String> getEmailsUsuarios() {
		return emailsUsuarios;
	}
	
	public void setEmailsUsuarios(List<String> emailsUsuarios) {
		this.emailsUsuarios = emailsUsuarios;
	}
	
	public void addEmailUsuario(String emailUsuario) {
	    if (!this.emailsUsuarios.contains(emailUsuario)) {
	        this.emailsUsuarios.add(emailUsuario);
	    }
	}

}
