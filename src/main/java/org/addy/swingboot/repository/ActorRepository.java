package org.addy.swingboot.repository;

import org.addy.swingboot.model.Actor;
import org.addy.swingboot.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    @Query("SELECT a FROM Actor a JOIN a.films f WHERE f = :film")
    List<Actor> findByFilm(@Param("film") Film film);
}
