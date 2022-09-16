package com.dblibrary.service.library;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.dblibrary.model.library.Author;
import com.dblibrary.model.library.dto.AuthorDTO;
import com.dblibrary.repository.AuthorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;

	public Optional<AuthorDTO> addAuthor(Author author) {
		//Questo IF serve per evitare che dalla rotta "AGGIUNGI AUTORE" si possa modificare oltre ad aggiungere gli autori aggiugnendo l'ID di un autore già presente. 
		if (author.getIdAuthor() == null) {
			// Pulisco la lista di libri onde evitare che si aggiungano libri dalla parte di
			// autori
			author.setBooks(new HashSet<>());
			author.setName(author.getName().toLowerCase());
			author.setSurname(author.getSurname().toLowerCase());
			// Onde evitare di aggiungere più volte lo stesso autore con ID differente
			Optional<Author> SingleAuthor = authorRepository.findByNameAndSurname(author.getName().toLowerCase(),
					author.getSurname().toLowerCase());
			if (SingleAuthor.isPresent()) {
				return Optional.of(new AuthorDTO(SingleAuthor.get()));
			} else {
				authorRepository.save(author);
				return Optional.of(new AuthorDTO(author));
			}
		}else {
			return Optional.of(new AuthorDTO(authorRepository.findById(author.getIdAuthor()).get()));
		}

	}

//Il metodo tiene conto dell'omonimia degli AUTORI. Non puoi modificare un autore con uno già presente.
	// Noi diamo per certo che non ci siano Autori differenti ma con lo stesso Nome
	// e cognome
	public Optional<AuthorDTO> editAuthor(Author author) {
		author.setName(author.getName().toLowerCase());
		author.setSurname(author.getSurname().toLowerCase());
		Optional<Author> aut = authorRepository.findById(author.getIdAuthor());
		Optional<Author> autNameSurname = authorRepository.findByNameAndSurname(author.getName(), author.getSurname());
		if (aut.isPresent() && autNameSurname.isPresent()) {
			if (aut.get().getIdAuthor() == autNameSurname.get().getIdAuthor()) {
				authorRepository.save(aut.get());
				return Optional.of(new AuthorDTO(aut.get()));
			}
		} else if (aut.isPresent() && autNameSurname.isEmpty()) {
			aut.get().setName(author.getName());
			aut.get().setSurname(author.getSurname());
			authorRepository.save(aut.get());
			return Optional.of(new AuthorDTO(aut.get()));
		}
		author.setName("id errato o nome gia' esistente");
		author.setSurname("");
		return Optional.of(new AuthorDTO(author));
	}

	public String deleteAuthor(Long id) {
		Optional<Author> SingleAuthor = authorRepository.findById(id);
		if (SingleAuthor.isPresent()) {
			if (authorRepository.deleteAuthorBook(id) > 0 && authorRepository.deleteAuthorBook(id) > 0)
				return SingleAuthor.get().getName();
		}
		return "Categoria non trovata";
	}

	public Optional<Set<AuthorDTO>> getAll() {
		Set<AuthorDTO> authors = new HashSet<>();
		for (Author c : authorRepository.findAll()) {
			authors.add(new AuthorDTO(c));
		}
		return Optional.of(authors);
	}

	public Optional<AuthorDTO> getById(Long id) {
		return Optional.of(new AuthorDTO(authorRepository.findById(id).get()));
	}

}
