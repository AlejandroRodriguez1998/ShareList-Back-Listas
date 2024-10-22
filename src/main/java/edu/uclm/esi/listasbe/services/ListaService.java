package edu.uclm.esi.listasbe.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.listasbe.dao.ListaDao;
import edu.uclm.esi.listasbe.dao.ProductoDao;
import edu.uclm.esi.listasbe.model.Lista;
import edu.uclm.esi.listasbe.model.Producto;


@Service //anotaciones para que se identifique que es cada cosa
public class ListaService {
	
	@Autowired
	private ListaDao listaDao;
	
	@Autowired
	private ProxyDEU proxy;
	
	@Autowired
	private ProductoDao productoDao;
	
	public Lista crearLista(String nombre, String token) {
		/*boolean correcto = this.proxy.validar(token);
		
		if(!correcto)
			throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED);
		*/
		Lista lista = new Lista();
		lista.setNombre(nombre);
		
		this.listaDao.save(lista);
		
		return lista;
	}
	
	public Lista addProducto(String idLista, Producto producto) {
		Optional<Lista> optLista = this.listaDao.findById(idLista);
		
		if(optLista.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista");
		}
		
		Lista lista = optLista.get();
		lista.add(producto);
		
		producto.setLista(lista);
		this.productoDao.save(producto);
		
		return lista;
	}
	
	public Producto comprar(String idProducto, float udsCompradas) {
		Optional<Producto> optProducto = this.productoDao.findById(idProducto);
		
		if(optProducto.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el producto");
		}
		
		Producto producto = optProducto.get();
		producto.comprar(udsCompradas);
		
		this.productoDao.save(producto);
		
		return producto;
		
	}

	public Iterable<Lista> obtenerListas() {
		return this.listaDao.findAll();
		
	}
	

}














