package com.alura.literatura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String nombre;
    private String lenguaje;
    private Integer descargas;
    @ManyToOne
    private Autor autor;

    public Libro(){}

    public Libro(DatosLibro datosLibro) {
        this.nombre = datosLibro.nombre();
        this.lenguaje = datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty() ? datosLibro.idiomas().get(0) : "Desconocido";
        this.descargas = datosLibro.descargas();
        // Solo se toma el primer autor de la lista, si existe

    }

    @Override
    public String toString() {
        return "\tLIBRO\n" +
                "  Título: " + nombre + "\n" +
                "  Autor: " + autor + '\n' +
                "  Lenguaje: " + lenguaje + '\n' +
                "  Descargas: " + descargas;
    }


    public Long getId() {
        return Id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
