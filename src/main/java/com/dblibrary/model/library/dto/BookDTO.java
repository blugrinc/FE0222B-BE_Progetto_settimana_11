package com.dblibrary.model.library.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.dblibrary.model.library.Author;
import com.dblibrary.model.library.Book;
import com.dblibrary.model.library.Category;

import lombok.Getter;

@Getter
public class BookDTO {
	private long idBook;
	private String name;
	private Map<Long, String> authors;
	private Map<Integer, String> categories;
	private double price;
	private Date data;

	public BookDTO() {
	}

	public BookDTO(Book book) {
		this.idBook = book.getIdBook();
		this.name = book.getName();
		this.authors = new HashMap<>();
		this.categories = new HashMap<>();
		this.price = book.getPrice();
		this.data = book.getData();
		if (book.getAuthors() != null) {
			for (Author a : book.getAuthors()) {
				authors.put(a.getIdAuthor(), a.getName() + " " + a.getSurname());
			}
		}
		if (book.getCategories() != null) {
			for (Category a : book.getCategories()) {
				categories.put(a.getIdCategory(), a.getName());
			}
		}

	}

}
