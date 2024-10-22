package edu.uclm.esi.listasbe.dao;

import org.springframework.data.repository.CrudRepository;

import edu.uclm.esi.listasbe.model.Lista;

public interface ListaDao extends CrudRepository<Lista, String> {

}
