package edu.uclm.esi.listasbe.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import edu.uclm.esi.listasbe.model.Lista;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListaDao extends CrudRepository<Lista, String> {

    @Query(value = "select lista_id from lista_emails_usuarios where emails_usuarios = :email",
            nativeQuery = true)
    List<String> getListasDe(@Param("email") String email);

}
