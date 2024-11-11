package edu.uclm.esi.listasbe.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.uclm.esi.listasbe.ws.WsListas;
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

	@Autowired
	private WsListas wsListas;
	
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
	
	public Lista addProducto(String idLista, Producto producto) throws IOException {
		Optional<Lista> optLista = this.listaDao.findById(idLista);
		
		if(optLista.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista");
		}
		
		Lista lista = optLista.get();
		lista.add(producto);
		
		producto.setLista(lista);
		this.productoDao.save(producto);

		this.wsListas.notificar(idLista, producto);
		
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

	public List<Lista> obtenerListas(String email) {
		List<Lista> result = new ArrayList<>();
		List<String> ids =  this.listaDao.getListasDe(email);

		for (String id : ids) {
			result.add(this.listaDao.findById(id).get()); // Se puede poner get porque sabemos que esos ids existen
		}

		return result;

		
	}
	

}














