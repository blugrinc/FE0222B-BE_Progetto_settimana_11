package com.dblibrary.repository;

import java.util.Optional;

import com.dblibrary.model.library.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional //Mi permette di fare la transazione
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	Optional<Category> findByName(String name);
	
	@Modifying //Permette di fare UPDATE e DELETE
	@Query(value="DELETE FROM category_book as cb "
			+ "WHERE cb.category_id = :idCategory", nativeQuery = true)
	public Integer deleteCategoryBook(@Param("idCategory") Integer id);
	
	@Modifying 
	@Query(value="DELETE FROM category as c "
			+ "WHERE c.id_category = :idCategory", nativeQuery = true)
	public Integer deleteCategory(@Param("idCategory") Integer id);
	
}
