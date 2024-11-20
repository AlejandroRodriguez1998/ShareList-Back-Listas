package edu.uclm.esi.listasbe.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.listasbe.dao.InvitacionDao;
import edu.uclm.esi.listasbe.dao.ListaDao;
import edu.uclm.esi.listasbe.model.Invitacion;
import edu.uclm.esi.listasbe.model.Lista;

@Service
public class InvitacionService {

    @Autowired
    private ListaDao listaDao;

    @Autowired
    private InvitacionDao invitacionDao;
    
    @Autowired
	private ProxyDEU proxy;
    
    private String baseUrl = "https://localhost:4200";

    public String crearInvitacion(String listaId, String token) {
		Map<String, Boolean> resultado = this.proxy.validar(token);

		if (!resultado.get("isValid")) {
		    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene permisos para crear listas.");
		}
		
		if (!resultado.get("isPremium")) {
		    List<Invitacion> cantidadInvitaciones = this.invitacionDao.findByListaIdAndUsadaTrue(listaId);

		    if (cantidadInvitaciones.size() >= 1) {
		        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Los usuarios no premium solo pueden tener hasta 1 amigo invitado.");
		    }
		}
		
        Optional<Lista> listaOpt = listaDao.findById(listaId);
        
        if (listaOpt.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista.");
        }

        String tokenInvitacion = UUID.randomUUID().toString();
        
        Invitacion invitacion = new Invitacion();
        invitacion.setToken(tokenInvitacion);
        invitacion.setLista(listaOpt.get());
        invitacion.setFechaExpiracion(LocalDateTime.now().plusDays(7));
        invitacionDao.save(invitacion);

        String urlInvitacion = baseUrl + "/Invitacion?token=" + token;
        
        return urlInvitacion;
    }
    
    public void aceptarInvitacion(String token, String emailUsuario) {
        Invitacion invitacion = invitacionDao.findByToken(token)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invitacion no valida."));


        if (invitacion.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.GONE, "La invitación ha expirado.");
        }

        if (invitacion.isUsada()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La invitación ya ha sido usada.");
        }
        
       
        Lista lista = invitacion.getLista();
 
        
        if(lista.getPropietario().equalsIgnoreCase(emailUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puede aceptar tu propia invitación.");
        }
        
        if (lista.getEmailsUsuarios().contains(emailUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ya tienes acceso a esta lista.");
        }
        
        lista.addEmailUsuario(emailUsuario); 
        listaDao.save(lista);

        invitacion.setUsada(true);
        invitacionDao.save(invitacion);
    }
}