package edu.uclm.esi.listasbe.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.uclm.esi.listasbe.services.InvitacionService;

@RestController
@RequestMapping("invitaciones")
@CrossOrigin("*")
public class InvitacionController {
	
	@Autowired
    private InvitacionService invitacionService;

	@PostMapping("/generarInvitacion")
    public String crearInvitacion(@RequestBody String listaId) {
		
		String token = invitacionService.crearInvitacion(listaId);
                
		return token;
    }
	
	
    @PostMapping("/aceptarInvitacion")
    public void aceptarInvitacion(@RequestBody Map<String, String> response) {
    	String token = response.get("token");
		String emailUsuario = response.get("email");
    	
        invitacionService.aceptarInvitacion(token, emailUsuario);   
    }
}
