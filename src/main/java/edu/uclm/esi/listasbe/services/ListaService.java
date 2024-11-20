package edu.uclm.esi.listasbe.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.listasbe.dao.ListaDao;
import edu.uclm.esi.listasbe.dao.ProductoDao;
import edu.uclm.esi.listasbe.model.Lista;
import edu.uclm.esi.listasbe.model.Producto;
import edu.uclm.esi.listasbe.ws.WsListas;
import jakarta.annotation.PostConstruct;


@Service //anotaciones para que se identifique que es cada cosa
public class ListaService {
	
	@Autowired
	private ListaDao listaDao;
	
	@Autowired
	private ProxyDEU proxy;
	
	@Autowired
	private ProductoDao productoDao;
	
	@Autowired
	private WsListas wsListas;
	
	public List<Lista> obtenerListas(String email) {
	    return this.listaDao.findListasByEmailUsuario(email);
	}
	
	public Lista crearLista(String nombre, String token, String email) {
		Map<String, Boolean> resultado = this.proxy.validar(token);

		if (!resultado.get("isValid")) {
		    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene permisos para crear listas.");
		}
		
		if (!resultado.get("isPremium")) {
		    List<Lista> cantidadListas = this.listaDao.findByPropietario(email);

		    if (cantidadListas.size() >= 2) {
		        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Los usuarios no premium solo pueden tener hasta 2 listas.");
		    }
		}
		
		Lista lista = new Lista();
		lista.setNombre(nombre);
		lista.setPropietario(email);
		lista.addEmailUsuario(email);
		
		this.listaDao.save(lista);
		
		return lista;
	}
	
	public Lista addProducto(String idLista, Producto producto, String token) {
		Map<String, Boolean> resultado = this.proxy.validar(token);
		Optional<Lista> optLista = this.listaDao.findById(idLista);

		if (!resultado.get("isValid")) {
		    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes permisos para añadir un producto.");
		}
		
		if(optLista.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista.");
		}
		
		Lista lista = optLista.get();
		
		if (!resultado.get("isPremium") && lista.getProductos().size() >= 10) {
		    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Los usuarios no premium solo pueden tener hasta 10 productos.");
		}
		
		producto.setLista(lista);
		this.productoDao.save(producto);
		
		lista.add(producto);
		
		this.wsListas.notificar(idLista, producto, "nuevoProducto");
		
		return lista;
	}
	
	public Producto comprar(String idProducto, Float udsCompradas, String token) {
		Map<String, Boolean> resultado = this.proxy.validar(token);

		if (!resultado.get("isValid")) {
		    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes permisos para comprar.");
		}
		
		Optional<Producto> optProducto = this.productoDao.findById(idProducto);
		
		if(optProducto.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el producto.");
		}
		
		Producto producto = optProducto.get();
		producto.comprar(udsCompradas);
		
		this.productoDao.save(producto);
		
		System.out.println("Notificando compra de producto " + producto.getId() + " en lista " + producto.getLista().getId());
		
		this.wsListas.notificar(producto.getLista().getId(), producto, "actualizacionProducto");
		
		return producto;
		
	}
	
	public Lista actualizarLista(Lista lista, String token) {
		Map<String, Boolean> resultado = this.proxy.validar(token);

		if (!resultado.get("isValid")) {
		    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes permisos para actualizar un producto.");
		}
		
		Optional<Lista> optListaExistente = this.listaDao.findById(lista.getId());

	    if (optListaExistente.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La lista no existe.");
	    }

	    Lista listaExistente = optListaExistente.get();
	    
	    listaExistente.getProductos().clear();

	    for (Producto producto : lista.getProductos()) {
	        producto.setLista(listaExistente); 	        
	        listaExistente.getProductos().add(producto);
	    }

	    return this.listaDao.save(listaExistente);
	}

	public void borrarLista(String idLista, String token) {
		Map<String, Boolean> resultado = this.proxy.validar(token);

		if (!resultado.get("isValid")) {
		    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes permisos para borrar una lista.");
		}
		
	    this.listaDao.deleteById(idLista);
	    
	    this.wsListas.notificar(idLista, null, "borradoLista");
	}
	
	public void borrarProducto(String idProducto, String token) {
		Map<String, Boolean> resultado = this.proxy.validar(token);

		if (!resultado.get("isValid")) {
		    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes permisos para borrar un producto.");
		}
		
		Producto producto = this.productoDao.findById(idProducto).get();
		
	    this.productoDao.deleteById(idProducto);
	    
	    this.wsListas.notificar(producto.getLista().getId(), producto, "borradoProducto");
	}
	
}














