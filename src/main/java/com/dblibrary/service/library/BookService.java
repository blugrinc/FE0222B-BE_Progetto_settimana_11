package com.dblibrary.service.library;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.dblibrary.model.library.Author;
import com.dblibrary.model.library.Book;
import com.dblibrary.model.library.Category;
import com.dblibrary.model.library.dto.BookDTO;
import com.dblibrary.repository.AuthorRepository;
import com.dblibrary.repository.BookRepository;
import com.dblibrary.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private AuthorRepository authorRepository;

//Aggiungi Libro
	public Optional<Book> addBook(Book book) {
		/*
		 * Onde evitare che il cascade.all brasi tutto quando cencello un libro faccio
		 * fare due controlli per Authore e Categorie
		 */
		Set<Author> authors = new HashSet<>();
		Set<Category> categories = new HashSet<>();
		
		for (Author a : book.getAuthors()) {
			a.setName(a.getName().toLowerCase());
			a.setSurname(a.getSurname().toLowerCase());
			Optional<Author> author = authorRepository.findByNameAndSurname(a.getName(), a.getSurname());
			if (author.isPresent()) {
				authors.add(author.get());
			} else {
				a.setBooks(new HashSet<>());
				authors.add(a);
				authorRepository.save(a);
			}

		}
		for (Category c : book.getCategories()) {
			c.setName(c.getName().toLowerCase());
			// Se la categoria che sto pushando è gia presente in elenco
			Optional<Category> category = categoryRepository.findByName(c.getName());
			if (category.isPresent()) { //Riprende quella già esistente, onde evitare di duplicare la stessa categoria				
				categories.add(category.get());;
			} else {
				c.setBooks(new HashSet<>()); //Evito che si creano altri libri a partire dall'oggetto categoria
				categories.add(c);
				categoryRepository.save(c);
			}
		}
		book.setAuthors(authors);
		book.setCategories(categories);
		bookRepository.save(book);
		return Optional.of(book);
	}

	public Optional<Book> editBook(Book book) {
		Optional<Book> oldBook = bookRepository.findById(book.getIdBook());
		Set<Category> categories = new HashSet<>();
		Set<Author> authors = new HashSet<>();

		for (Category c : book.getCategories()) {
			Optional<Category> category = categoryRepository.findByName(c.getName());
			if (category.isEmpty() && c.getIdCategory() != null) {
				category = categoryRepository.findById(c.getIdCategory());
			}
			if (category.isPresent()) {
				categories.add(category.get());
			}
		}
		for (Author a : book.getAuthors()) {
			Optional<Author> author = authorRepository.findByNameAndSurname(a.getName(), a.getSurname());
			if (author.isEmpty() && a.getIdAuthor() != null) {
				author = authorRepository.findById(a.getIdAuthor());
			}
			if (author.isPresent()) {
				authors.add(author.get());
			}
		}
		if (authors.size() > 0) {
			oldBook.get().setAuthors(authors);
		}
		if (categories.size() > 0) {
			oldBook.get().setCategories(categories);
		}
		if (authors.size() == 0 || categories.size() == 0) {
			return Optional.empty();
		} else {
			bookRepository.save(oldBook.get());
			oldBook.get().setData(book.getData());
			oldBook.get().setName(book.getName());
			oldBook.get().setPrice(book.getPrice());
			return oldBook;
		}

	}

	public Optional<BookDTO> getBookById(Long id) {
		Optional<Book> book = bookRepository.findById(id);
		BookDTO bookDTO = new BookDTO(book.get());
		return Optional.of(bookDTO);
	}

	public Optional<Set<BookDTO>> getBookByAuthor(Long idAuthor) {
		Optional<Set<Book>> books = bookRepository.findByAuthor(idAuthor);
		Set<BookDTO> booksDTO = new HashSet<>();

		for (Book b : books.get()) {
			booksDTO.add(new BookDTO(b));
		}
		return Optional.of(booksDTO);
	}

	public Optional<Set<BookDTO>> getBookByCategory(Integer idCategory) {
		Optional<Set<Book>> books = bookRepository.findByCategory(idCategory);
		Set<BookDTO> booksDTO = new HashSet<>();

		for (Book b : books.get()) {
			booksDTO.add(new BookDTO(b));
		}
		return Optional.of(booksDTO);
	}

	public void delete(Long id) {
		if (getBookById(id).isPresent()) {
			bookRepository.deleteById(id);
		}

	}

	public Optional<Set<BookDTO>> getAll() {
		Set<Book> books = Set.copyOf(bookRepository.findAll());
		Set<BookDTO> booksDTO = new HashSet<>();

		for (Book b : books) {
			booksDTO.add(new BookDTO(b));
		}
		return Optional.of(booksDTO);
	}
}
