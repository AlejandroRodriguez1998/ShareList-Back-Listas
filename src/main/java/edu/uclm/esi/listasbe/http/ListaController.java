package edu.uclm.esi.listasbe.http;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.listasbe.model.Lista;
import edu.uclm.esi.listasbe.model.Producto;
import edu.uclm.esi.listasbe.services.ListaService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("lista") //Nombre publico de donde vamos a hacer las peticiones
@CrossOrigin("*") //Sirve para que el servidor o controlador que permita perticiones de cualquier lado
public class ListaController {
	@Autowired //instanciar este objeto sin llamar al constructor
	private ListaService listaService;
	
	@GetMapping("/obtenerListas")
	public List<Lista> obtenerListas(@RequestParam String email) {
		return this.listaService.obtenerListas(email);
	}
	
	@PostMapping("/crearLista")
	public Lista crearLista(HttpServletRequest request, @RequestBody Map<String, String> result) {
		String token = request.getHeader("Authorization").replace("Bearer ", "").trim();
		String nombre = result.get("nombre");
	    String email = result.get("email");
	    
	    if (nombre.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre no puede estar vacío.");
	    }

	    if (nombre.length() > 80) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la lista esta limitado a 80 caracteres.");
	    }

	    return this.listaService.crearLista(nombre, token, email);
	}
	
	@PostMapping("/addProducto")
	public Lista addProducto(HttpServletRequest request, @RequestBody Producto producto) {
				
		if(producto.getNombre().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto no puede estar vacio.");
		}
		
		if(producto.getNombre().length() > 80) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto esta limitado a 80 caracteres.");
		}
		
		String token = request.getHeader("Authorization").replace("Bearer ", "").trim();
		String idLista = request.getHeader("idLista");
		
		return this.listaService.addProducto(idLista,producto,token);
		
	}
	
	@PutMapping("/comprar")
	public Producto comprar(HttpServletRequest request, @RequestBody Map<String, Object> compra) {
	    String token = request.getHeader("Authorization").replace("Bearer ", "").trim();
	    Float udsCompradas = ((Number) compra.get("udsCompradas")).floatValue();
		String idProducto = compra.get("idProducto").toString();
	
		if (idProducto.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto no puede estar vacio.");
		}
		
		if (udsCompradas == null || udsCompradas <= 0) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad de unidades compradas es inválida.");
	    }
		
		return this.listaService.comprar(idProducto,udsCompradas, token);
	}
	
	@PutMapping("/actualizarLista")
	public Lista actualizarLista(HttpServletRequest request, @RequestBody Lista lista) {
	    String token = request.getHeader("Authorization").replace("Bearer ", "").trim();

	    if (lista == null) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La lista está vacía.");
	    }
	    
	    return listaService.actualizarLista(lista, token);
	}
	
	@DeleteMapping("/borrarLista")
	public void borrarLista(HttpServletRequest request, @RequestBody String idLista) {
	    String token = request.getHeader("Authorization").replace("Bearer ", "").trim();
	    
	    if (idLista.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La lista seleccionada está vacía.");
	    }

	    this.listaService.borrarLista(idLista, token);
	}
	
	@DeleteMapping("/borrarProducto")
	public void borrarProducto(HttpServletRequest request, @RequestBody String idProducto) {
	    String token = request.getHeader("Authorization").replace("Bearer ", "").trim();
	    
	    if (idProducto.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto seleccionado está vacío.");
	    }

	    this.listaService.borrarProducto(idProducto, token);
	}
}















