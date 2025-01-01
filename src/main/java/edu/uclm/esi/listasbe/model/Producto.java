package edu.uclm.esi.listasbe.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Producto {

	@Id @Column(length=36)
	private String id;
	@Column(length=80, nullable = false)
	private String nombre;
	
	private float udsPedidas; // Cantidad inicial
    private float udsPendientes; // Cantidad pendiente de compra
    private float udsCompradas;  // Cantidad ya comprada
	
	@ManyToOne
	private Lista lista;
	
	public Producto() {
        this.id = UUID.randomUUID().toString();
        this.udsPedidas = 0;
        this.udsPendientes = 0;
        this.udsCompradas = 0;
    }

    public void inicializar(float udsOriginales) {
        this.udsPedidas = udsOriginales;
        this.udsPendientes = udsOriginales;
        this.udsCompradas = 0;
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

	public float getUdsPedidas() {
		return udsPedidas;
	}

	public void setUdsPedidas(float udsPedidas) {
		this.udsPedidas = udsPedidas;
	}

	public float getUdsPendientes() {
		return udsPendientes;
	}

	public void setUdsPendientes(float udsPendientes) {
		this.udsPendientes = udsPendientes;
	}

	public float getUdsCompradas() {
		return udsCompradas;
	}

	public void setUdsCompradas(float udsCompradas) {
		this.udsCompradas = udsCompradas;
	}

	@JsonIgnore
    public Lista getLista() {
        return lista;
    }

    public void setLista(Lista lista) {
        this.lista = lista;
    }
	
    public void comprar(float cantidadComprar) {
        if (cantidadComprar > udsPendientes) {
            throw new IllegalArgumentException("No puedes comprar más de las unidades pendientes.");
        }
        this.udsCompradas += cantidadComprar;
        this.udsPendientes -= cantidadComprar;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return id != null && id.equals(producto.id); // Compara por ID
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
