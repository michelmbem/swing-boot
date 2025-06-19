package org.addy.swingboot.repository;

import org.addy.swingboot.model.Category;
import org.addy.swingboot.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Integer> {
    @Query("SELECT f FROM Film f JOIN f.categories c WHERE c = :category")
    List<Film> findByCategory(@Param("category") Category category);
}
