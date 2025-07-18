package com.alura.literatura.repository;

import com.alura.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query("SELECT a.nombreAutor from Autor a")
    List<String> mostrarAutores();
    @Query("SELECT a.nombreAutor FROM Autor a WHERE :anio BETWEEN anioDeNacimiento AND anioDeMuerte")
    List<String> autoresVivos(Integer anio);
    @Query("SELECT l.nombre, l.lenguaje FROM Libro l WHERE l.lenguaje = :idioma")
    List<String> librosPorIdioma(String idioma);
}
