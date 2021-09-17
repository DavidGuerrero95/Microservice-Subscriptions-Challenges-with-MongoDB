package com.retos.app.subscripciones.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.retos.app.subscripciones.models.Suscripciones;

@FeignClient(name = "app-estadisticaretos")
public interface EstadisticasRetosFeignClient {

	@PutMapping("/estadistica/editarLikes/{nombre}")
	public void editarLikes(@PathVariable("nombre") String nombre, @RequestBody Suscripciones suscripciones);

}
