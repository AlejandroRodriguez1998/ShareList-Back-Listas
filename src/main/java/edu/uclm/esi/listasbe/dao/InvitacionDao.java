package edu.uclm.esi.listasbe.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import edu.uclm.esi.listasbe.model.Invitacion;

public interface InvitacionDao extends CrudRepository<Invitacion, String> {

	Optional<Invitacion> findByToken(String token);

}

