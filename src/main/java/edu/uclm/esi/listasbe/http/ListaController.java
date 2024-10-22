package edu.uclm.esi.listasbe.http;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.listasbe.model.Lista;
import edu.uclm.esi.listasbe.model.Producto;
import edu.uclm.esi.listasbe.services.ListaService;

@RestController
@RequestMapping("lista") //Nombre publico de donde vamos a hacer las peticiones
@CrossOrigin("*") //Sirve para que el servidor o controlador que permita perticiones de cualquier lado
public class ListaController {
	@Autowired //instanciar este objeto sin llamar al constructor
	private ListaService listaService;
	
	@PostMapping("/crearLista")
	public Lista crearLista(@RequestBody String nombre) {
		nombre = nombre.trim();
		if(nombre.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre no puede estar vacio");
		}
		
		if(nombre.length() > 80) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la lista esta limitado a 80 caracteres");
		}
		
		return this.listaService.crearLista(nombre, "1234");
	}
	
	@PostMapping("/addProducto")
	public Lista addProducto(HttpServletRequest request, @RequestBody Producto producto) {
				
		if(producto.getNombre().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto no puede estar vacio");

		}
		
		if(producto.getNombre().length() > 80) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la lista esta limitado a 80 caracteres");
		}
		
		
		String idLista = request.getHeader("idLista");
		
		return this.listaService.addProducto(idLista,producto);
		
	}
	
	@PutMapping("/comprar")
	public Producto comprar(@RequestBody Map<String, Object> compra) {
		String idProducto = compra.get("idProducto").toString();
		float udsCompradas = (float) compra.get("udsCompradas");
		
		return this.listaService.comprar(idProducto,udsCompradas);
	}
	
	@GetMapping("/obtenerListas")
	public Iterable<Lista> obtenerListas() {
		return this.listaService.obtenerListas();
	}
	
	
}
















