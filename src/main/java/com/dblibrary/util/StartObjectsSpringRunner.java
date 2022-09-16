package com.dblibrary.util;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import com.dblibrary.model.auth.Role;
import com.dblibrary.model.auth.Roles;
import com.dblibrary.model.auth.User;
import com.dblibrary.model.library.Author;
import com.dblibrary.model.library.Book;
import com.dblibrary.model.library.Category;
import com.dblibrary.repository.RoleRepository;
import com.dblibrary.repository.UserRepository;
import com.dblibrary.service.library.AuthorService;
import com.dblibrary.service.library.BookService;
import com.dblibrary.service.library.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

public class StartObjectsSpringRunner {

	 @Component
	public class ApplicationStartupRunner implements ApplicationRunner {

		@Autowired
		UserRepository userRepository;

		@Autowired
		RoleRepository roleRepository;

		@Autowired
		BookService bookService;

		@Autowired
		CategoryService categoryService;

		@Autowired
		AuthorService authorService;

		@Override
		public void run(ApplicationArguments args) throws Exception {
			BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
			Role role = new Role();
			role.setRoleName(Roles.ROLE_ADMIN);
			User user = new User();
			Set<Role> roles = new HashSet<>();
			roles.add(role);
			user.setUserName("admin");
			user.setPassword(bCrypt.encode("admin"));
			user.setEmail("admin@admin.com");
			user.setRoles(roles);
			user.setActive(true);

			roleRepository.save(role);
			userRepository.save(user);

			Author firstAuthor = new Author();
			firstAuthor.setName("Luigi");
			firstAuthor.setSurname("Luigiani");
			authorService.addAuthor(firstAuthor);

			Set<Author> authors = new HashSet<>();
			authors.add(firstAuthor);

			Category firstCategory = new Category();
			firstCategory.setName("Fantasy");
			categoryService.addCategory(firstCategory);
			Set<Category> categories = new HashSet<>();
			categories.add(firstCategory);

			Book firstBook = new Book();
			firstBook.setAuthors(authors);
			firstBook.setCategories(categories);
			firstBook.setPrice(29.99);
			firstBook.setName("Avelar");
			firstBook.setData(new Date(22, 05, 1993));
			bookService.addBook(firstBook);
		}
	}

}
