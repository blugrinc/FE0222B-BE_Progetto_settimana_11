package com.dblibrary.service.library;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.dblibrary.model.library.Category;
import com.dblibrary.model.library.dto.AuthorDTO;
import com.dblibrary.model.library.dto.CategoryDTO;
import com.dblibrary.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public Optional<CategoryDTO> addCategory(Category category) {
		//Serve ad evitare che si modificano le categorie passando l'ID nella rotta AGGIUNGI
		if (category.getIdCategory() == null) {
			// Pulisco la lista di libri onde evitare che si aggiungano libri dalla parte di
			// categoria
			category.setBooks(new HashSet<>());
			category.setName(category.getName().toLowerCase());
			// Onde evitare di aggiungere più volte la stessa categoria con ID differente
			Optional<Category> SingleCategory = categoryRepository.findByName(category.getName());
			if (SingleCategory.isPresent()) {
				return Optional.of(new CategoryDTO(SingleCategory.get()));
			} else {
				categoryRepository.save(category);
				return Optional.of(new CategoryDTO(category));
			}
		} else {
			return Optional.of(new CategoryDTO(categoryRepository.findById(category.getIdCategory()).get()));
		}

	}

	public Optional<CategoryDTO> editCategory(Category category) {
		Optional<Category> SingleCategory = categoryRepository.findById(category.getIdCategory());
		if (SingleCategory.isPresent()) {
			SingleCategory.get().setName(category.getName().toLowerCase());
			categoryRepository.save(SingleCategory.get());
			return Optional.of(new CategoryDTO(SingleCategory.get()));
		} else {
			category.setName("L'id non è stato trovato");
		}
		return Optional.of(new CategoryDTO(category));
	}

	public String deleteCategory(Integer id) {
		Optional<Category> SingleCategory = categoryRepository.findById(id);
		if (SingleCategory.isPresent()) {
			if (categoryRepository.deleteCategoryBook(id) > 0 && categoryRepository.deleteCategory(id) > 0)
				return SingleCategory.get().getName();
		}
		return "Categoria non trovata";
	}

	public Optional<Set<CategoryDTO>> getAll() {
		Set<CategoryDTO> categories = new HashSet<>();
		for (Category c : categoryRepository.findAll()) {
			categories.add(new CategoryDTO(c));
		}
		return Optional.of(categories);
	}

	public Optional<CategoryDTO> getById(Integer id) {
		return Optional.of(new CategoryDTO(categoryRepository.findById(id).get()));
	}
}
