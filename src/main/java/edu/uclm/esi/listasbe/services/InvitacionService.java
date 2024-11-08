package edu.uclm.esi.listasbe.services;

import java.time.LocalDateTime;
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

    public String crearInvitacion(String listaId) {
        Optional<Lista> listaOpt = listaDao.findById(listaId);
        
        if (listaOpt.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista.");
        }

        String token = UUID.randomUUID().toString();
        
        Invitacion invitacion = new Invitacion();
        invitacion.setToken(token);
        invitacion.setLista(listaOpt.get());
        invitacion.setFechaExpiracion(LocalDateTime.now().plusDays(7));
        invitacionDao.save(invitacion);

        return token;
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
        lista.addUsuario(emailUsuario); 
        listaDao.save(lista);

        invitacion.setUsada(true);
        invitacionDao.save(invitacion);
    }
}