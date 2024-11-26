package edu.uclm.esi.listasbe.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.listasbe.services.InvitacionService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("invitaciones")
@CrossOrigin(origins = { "https://localhost:4200" }, allowCredentials = "true")

public class InvitacionController {
	
	@Autowired
    private InvitacionService invitacionService;

	@PostMapping("/generarInvitacion")
    public String crearInvitacion(HttpServletRequest request, @RequestBody String listaId) {
		String email = (String) request.getAttribute("userEmail");
		boolean isPremium = (boolean) request.getAttribute("isPremium");
		
		if (email == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene permisos para crear invitaciones.");
		}
		
		String invitacion =  invitacionService.crearInvitacion(listaId, email, isPremium);
		System.out.println("Invitacion generada: " + invitacion);
		return invitacion;
    }
	
	

    @PostMapping("/aceptarInvitacion")
    public void aceptarInvitacion(HttpServletRequest request, @RequestBody Map<String, String> response) {
        String emailUsuario = (String) request.getAttribute("userEmail");
        
        if (emailUsuario == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado.");
        }

        String token = response.get("token");

        invitacionService.aceptarInvitacion(token, emailUsuario);
    }
}
    
