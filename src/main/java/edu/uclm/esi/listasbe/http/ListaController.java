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
@CrossOrigin(origins = { "https://localhost:4200" }, allowCredentials = "true")

public class ListaController {
	@Autowired //instanciar este objeto sin llamar al constructor
	private ListaService listaService;
	
    @GetMapping("/obtenerListas")
    public List<Lista> obtenerListas(HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado.");
        }
        return this.listaService.obtenerListas(email);
    }
	

    @PostMapping("/crearLista")
    public Lista crearLista(HttpServletRequest request, @RequestBody Map<String, String> result) {
        String email = (String) request.getAttribute("userEmail");
        Boolean isPremium = (Boolean) request.getAttribute("isPremium");

        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene permisos para crear listas.");
        }
        String nombre = result.get("nombre");

        return this.listaService.crearLista(nombre, email, isPremium);
    }
	
	@PutMapping("/comprar")
    public Producto comprar(HttpServletRequest request, @RequestBody Map<String, Object> compra) {
        String email = (String) request.getAttribute("userEmail");
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene permisos para comprar.");
        }

        Float udsCompradas = ((Number) compra.get("udsCompradas")).floatValue();
        String idProducto = (String) compra.get("idProducto");

        if (idProducto == null || idProducto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto no puede estar vacío.");
        }

        if (udsCompradas == null || udsCompradas <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad de unidades compradas es inválida.");
        }

        return this.listaService.comprar(idProducto, udsCompradas, email);
    }
	
    @PostMapping("/addProducto")
    public Lista addProducto(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        String email = (String) request.getAttribute("userEmail");
        Boolean isPremium = (Boolean) request.getAttribute("isPremium");

        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene permisos para añadir un producto.");
        }

        String idLista = (String) body.get("idLista");
        Map<String, Object> productoMap = (Map<String, Object>) body.get("producto");

        if (idLista == null || idLista.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La lista no puede estar vacía.");
        }

        Producto producto = new Producto();
        producto.setNombre((String) productoMap.get("nombre"));
        producto.setUdsPedidas(((Number) productoMap.get("udsPedidas")).floatValue());

        if (producto.getNombre().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto no puede estar vacío.");
        }

        if (producto.getNombre().length() > 80) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto está limitado a 80 caracteres.");
        }

        return this.listaService.addProducto(idLista, producto, email, isPremium);
    }

	
    @PutMapping("/actualizarProducto")
    public Producto actualizarProducto(HttpServletRequest request, @RequestBody Producto producto) {
        String email = (String) request.getAttribute("userEmail");
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene permisos para actualizar un producto.");
        }

        if (producto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto está vacío.");
        }

        return this.listaService.actualizarProducto(producto, email);
    }
	
    @DeleteMapping("/borrarLista")
    public void borrarLista(HttpServletRequest request, @RequestBody String idLista) {
        String email = (String) request.getAttribute("userEmail");
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene permisos para borrar una lista.");
        }

        if (idLista.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La lista seleccionada está vacía.");
        }

        this.listaService.borrarLista(idLista, email);
    }
	
    @DeleteMapping("/borrarProducto")
    public void borrarProducto(HttpServletRequest request, @RequestBody String idProducto) {
        String email = (String) request.getAttribute("userEmail");
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene permisos para borrar un producto.");
        }

        if (idProducto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto seleccionado está vacío.");
        }

        this.listaService.borrarProducto(idProducto, email);
    }
}















