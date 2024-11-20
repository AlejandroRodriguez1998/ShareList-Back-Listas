package edu.uclm.esi.listasbe.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import edu.uclm.esi.listasbe.model.Invitacion;
import edu.uclm.esi.listasbe.model.Lista;

public interface InvitacionDao extends CrudRepository<Invitacion, String> {

	Optional<Invitacion> findByToken(String token);
	
	List<Invitacion> findByListaIdAndUsadaTrue(String listaId);

}

