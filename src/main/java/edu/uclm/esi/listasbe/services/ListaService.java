package edu.uclm.esi.listasbe.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.listasbe.dao.ListaDao;
import edu.uclm.esi.listasbe.dao.ProductoDao;
import edu.uclm.esi.listasbe.model.Invitacion;
import edu.uclm.esi.listasbe.model.Lista;
import edu.uclm.esi.listasbe.model.Producto;
import edu.uclm.esi.listasbe.ws.WsListas;
import jakarta.annotation.PostConstruct;


@Service //anotaciones para que se identifique que es cada cosa
public class ListaService {

	@Autowired
	private ListaDao listaDao;

	//@Autowired
	//private ProxyDEU proxy;

	@Autowired
	private ProductoDao productoDao;

	@Autowired
	private WsListas wsListas;

	public List<Lista> obtenerListas(String email) {
	    return this.listaDao.findListasByEmailUsuario(email);
	}

	public Lista crearLista(String nombre, String email, Boolean isPremium) {
		if (!isPremium) {
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

	public Lista addProducto(String idLista, Producto producto, String email, Boolean isPremium) {
		Optional<Lista> optLista = this.listaDao.findById(idLista);

		if(optLista.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista.");
		}

		Lista lista = optLista.get();

		// Verificar que el usuario tiene acceso a la lista
		if (!lista.getEmailsUsuarios().contains(email)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta lista.");
		}

		if (!isPremium && lista.getProductos().size() >= 10) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Los usuarios no premium solo pueden tener hasta 10 productos.");
		}

		producto.setLista(lista);
		this.productoDao.save(producto);

		lista.add(producto);

		this.wsListas.notificar(idLista, producto, "nuevoProducto");

		return lista;
	}

	public Producto comprar(String idProducto, Float udsCompradas, String email) {
		Optional<Producto> optProducto = this.productoDao.findById(idProducto);

		if (optProducto.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el producto.");
		}

		Producto producto = optProducto.get();
		Lista lista = producto.getLista();

		// Verificar que el usuario tiene acceso a la lista
		if (!lista.getEmailsUsuarios().contains(email)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta lista.");
		}

		producto.comprar(udsCompradas);

		this.productoDao.save(producto);

		this.wsListas.notificar(lista.getId(), producto, "actualizacionProducto");

		return producto;
	}

	public Producto actualizarProducto(Producto producto, String email) {
		Optional<Producto> optProductoExistente = this.productoDao.findById(producto.getId());

		if (optProductoExistente.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el producto.");
		}

		Producto productoExistente = optProductoExistente.get();
		Lista lista = productoExistente.getLista();

		// Verificar que el usuario tiene acceso a la lista
		if (!lista.getEmailsUsuarios().contains(email)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta lista.");
		}
		
		if (productoExistente.getUdsPedidas() > producto.getUdsPedidas()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes disminuir las unidades pedidas.");
		}

		productoExistente.setNombre(producto.getNombre());
		productoExistente.setUdsPedidas(producto.getUdsPedidas());
		productoExistente.setUdsPendientes(producto.getUdsPedidas() - producto.getUdsCompradas());

		this.productoDao.save(productoExistente);

		this.wsListas.notificar(lista.getId(), productoExistente, "actualizacionProducto");

		return productoExistente;
	}

	public void borrarLista(String idLista, String email) {
		Optional<Lista> optLista = this.listaDao.findById(idLista);

		if (optLista.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista.");
		}

		Lista lista = optLista.get();

		// Solo el propietario puede borrar la lista
		if (!lista.getPropietario().equals(email)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para borrar esta lista.");
		}

		this.listaDao.delete(lista);

		this.wsListas.notificar(idLista, null, "borradoLista");
	}

	public void borrarProducto(String idProducto, String email) {
		Optional<Producto> optProducto = this.productoDao.findById(idProducto);

		if (optProducto.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el producto.");
		}

		Producto producto = optProducto.get();
		Lista lista = producto.getLista();

		// Verificar que el usuario tiene acceso a la lista
		if (!lista.getEmailsUsuarios().contains(email)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta lista.");
		}

		this.productoDao.delete(producto);

		this.wsListas.notificar(lista.getId(), producto, "borradoProducto");
	}
}














