package com.retos.app.subscripciones.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.retos.app.subscripciones.models.Suscripciones;

public interface SuscripcionesRepository extends MongoRepository<Suscripciones, String>{

	@RestResource(path = "buscar-name")
	public Suscripciones findByNombre(@Param("name") String nombre);
	
}
