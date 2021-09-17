package com.retos.app.subscripciones.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.retos.app.subscripciones.clients.EstadisticasRetosFeignClient;
import com.retos.app.subscripciones.models.Comentarios;
import com.retos.app.subscripciones.models.Ideas;
import com.retos.app.subscripciones.models.Suscripciones;
import com.retos.app.subscripciones.repository.SuscripcionesRepository;

@RestController
public class SuscripcionesController {

	@Autowired
	SuscripcionesRepository suscripcionesRepository;

	@Autowired
	EstadisticasRetosFeignClient estadistica;

	@GetMapping("/suscripciones/listar")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Suscripciones> ver() {
		return suscripcionesRepository.findAll();
	}

	@GetMapping("/suscripciones/verNombre/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public Suscripciones verNombre(@PathVariable("nombre") String nombre) {
		return suscripcionesRepository.findByNombre(nombre);
	}

	@PostMapping("/suscripciones/crear")
	@ResponseStatus(code = HttpStatus.CREATED)
	public void crearSuscripciones(@RequestParam String nombre) {
		Suscripciones sus = new Suscripciones();
		sus.setNombre(nombre);
		sus.setCreadorIdea(new ArrayList<String>());
		sus.setIdeas(new ArrayList<String>());
		sus.setComentarios(new ArrayList<List<List<String>>>());
		sus.setLikes(new ArrayList<List<String>>());
		sus.setDislikes(new ArrayList<List<String>>());
		sus.setSuscripciones(new ArrayList<List<String>>());
		suscripcionesRepository.save(sus);
	}

	@PutMapping("/suscripciones/ponerIdeas/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public void ponerIdeas(@PathVariable("nombre") String nombre, @RequestBody Ideas ideas) {
		Suscripciones sus = suscripcionesRepository.findByNombre(nombre);
		List<String> credorIdeas = sus.getCreadorIdea();
		List<String> listaIdeas = sus.getIdeas();

		if (!listaIdeas.contains(ideas.getIdeas())) {
			List<List<String>> listaSuscripciones = sus.getSuscripciones();
			List<List<List<String>>> comentarios = sus.getComentarios();
			List<List<String>> likes = sus.getLikes();
			List<List<String>> disLikes = sus.getDislikes();

			List<List<String>> coments = new ArrayList<List<String>>();
			List<String> like = new ArrayList<String>();
			List<String> dislike = new ArrayList<String>();
			List<String> suscrip = new ArrayList<String>();

			suscrip.add(ideas.getUsername());
			credorIdeas.add(ideas.getUsername());
			listaIdeas.add(ideas.getIdeas());
			comentarios.add(coments);
			listaSuscripciones.add(suscrip);
			likes.add(like);
			disLikes.add(dislike);
			sus.setCreadorIdea(credorIdeas);
			sus.setIdeas(listaIdeas);
			sus.setComentarios(comentarios);
			sus.setLikes(likes);
			sus.setDislikes(disLikes);
			sus.setSuscripciones(listaSuscripciones);
			suscripcionesRepository.save(sus);
			estadistica.editarLikes(nombre, sus);
		}

	}

	@PutMapping("/suscripciones/ponerComentarios/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public String ponerComentarios(@PathVariable("nombre") String nombre, @RequestBody Comentarios comentarios) {
		Suscripciones sus = suscripcionesRepository.findByNombre(nombre);
		List<String> ideas = sus.getIdeas();
		Integer value = ideas.indexOf(comentarios.getIdea());
		List<List<List<String>>> comentario = sus.getComentarios();
		List<List<String>> coment = comentario.get(value);
		coment.add(comentarios.getComentario());
		comentario.set(value, coment);
		sus.setComentarios(comentario);
		suscripcionesRepository.save(sus);
		return "Comentario agregado correctamente a la idea" + comentarios.getIdea();
	}

	@PutMapping("/suscripciones/darLike/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public String darLike(@PathVariable("nombre") String nombre, @RequestBody Comentarios likes) {
		Suscripciones sus = suscripcionesRepository.findByNombre(nombre);
		List<String> ideas = sus.getIdeas();
		List<List<String>> like = sus.getLikes();
		List<List<String>> dislike = sus.getDislikes();
		Integer value = ideas.indexOf(likes.getIdea());
		List<String> ponLike = like.get(value);
		List<String> ponDislike = dislike.get(value);
		String usuario = likes.getUsername();
		if (likes.getLikes() == 0) {
			if (ponLike.contains(usuario))
				ponLike.remove(usuario);
			if (ponDislike.contains(usuario))
				ponDislike.remove(usuario);
		} else if (likes.getLikes() == 1) {
			if (ponDislike.contains(usuario))
				ponDislike.remove(usuario);
			if (!ponLike.contains(usuario)) {
				ponLike.add(usuario);
			}
		} else if (likes.getLikes() == 2) {
			if (ponLike.contains(usuario))
				ponLike.remove(usuario);
			if (!ponDislike.contains(usuario)) {
				ponDislike.add(usuario);
			}
		}

		like.set(value, ponLike);
		dislike.set(value, ponDislike);
		sus.setLikes(like);
		sus.setDislikes(dislike);
		suscripcionesRepository.save(sus);
		estadistica.editarLikes(nombre, sus);
		return "Like correcto";
	}

	@GetMapping("/suscripciones/revisarLike/{nombre}")
	@ResponseStatus()
	public Boolean revisarLike(@PathVariable("nombre") String nombre, @RequestBody Comentarios usuario) {
		Suscripciones sus = suscripcionesRepository.findByNombre(nombre);
		List<String> ideas = sus.getIdeas();
		Integer value = ideas.indexOf(usuario.getIdea());
		List<String> likes = sus.getLikes().get(value);
		if (likes.contains(usuario.getUsername())) {
			return true;
		} else {
			return false;
		}
	}

	@GetMapping("/suscripciones/verComentarios/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<List<String>> verComentarios(@PathVariable("nombre") String nombre, @RequestBody Comentarios likes) {
		Suscripciones sus = suscripcionesRepository.findByNombre(nombre);
		List<String> ideas = sus.getIdeas();
		Integer value = ideas.indexOf(likes.getIdea());
		return sus.getComentarios().get(value);
	}

	@GetMapping("/suscripciones/verIdeas/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<String> verIdeas(@PathVariable("nombre") String nombre) {
		Suscripciones sus = suscripcionesRepository.findByNombre(nombre);
		return sus.getIdeas();
	}

	@PutMapping("/suscripciones/suscribirse/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public String suscribirse(@PathVariable String nombre, @RequestBody Comentarios usuario) {
		Suscripciones sus = suscripcionesRepository.findByNombre(nombre);
		List<String> ideas = sus.getIdeas();
		List<List<String>> suscrip = sus.getSuscripciones();
		Integer value = ideas.indexOf(usuario.getIdea());
		List<String> susIdea = suscrip.get(value);
		susIdea.add(usuario.getUsername());
		suscrip.set(value, susIdea);
		sus.setSuscripciones(suscrip);
		suscripcionesRepository.save(sus);
		return "Usuario Registrado correctamente";
	}

	@GetMapping("/suscripciones/revisarSuscripciones/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean revisarSuscripciones(@PathVariable String nombre, @RequestBody Comentarios usuario) {
		Suscripciones sus = suscripcionesRepository.findByNombre(nombre);
		List<String> ideas = sus.getIdeas();
		Integer value = ideas.indexOf(usuario.getIdea());
		List<String> suscrip = sus.getSuscripciones().get(value);
		if (suscrip.contains(usuario.getUsername())) {
			return true;
		} else {
			return false;
		}
	}

}
