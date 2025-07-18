package com.alura.literatura.principal;

import com.alura.literatura.model.*;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.repository.LibroRepository;
import com.alura.literatura.service.ConsumoAPI;
import com.alura.literatura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "http://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Libro> libros;
    private String busqueda;
    private LibroRepository repository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository repository, AutorRepository autorRepository) { this.repository = repository;
        this.autorRepository = autorRepository;
    }

    public void showMenu() {
        var opcionDelMenu = -1;
        while (opcionDelMenu != 0) {
            System.out.println("Bienvenido a nuestra LiterAlura");
            var menu = """
                Por favor, escoja una opción del menu:
                1) Buscar libro por título en la API de Gutendex
                2) Mostrar libros registrados en la BD
                3) Mostrar autores registrados en la BD
                4) Mostrar autores vivos dado el año ingresado
                5) Mostrar libros por idioma
                
                0) Salir
                """;
            System.out.println(menu);
            opcionDelMenu = Integer.valueOf(teclado.nextLine());
            switch (opcionDelMenu) {
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                case 1:
                    buscarLibroPorAutor();
                    break;
                case 2:
                    mostrarTodosLosLibros();
                    break;
                case 3:
                    mostrarAutores();
                    break;
                case 4:
                    autoresVivos();
                    break;
                case 5:
                    mostrarLibrosPorIdioma();
                    break;
                default:
                    System.out.println("El número que ingresó es erróneo");
            }
        }
    }

    private RespuestaLibros getDatosLibro() {

        System.out.println("Ingresa el nombre del autor o alguna parte del libro que quieras buscar: ");
        busqueda = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + busqueda.replace(" ", "+"));
        System.out.println(json);
        return conversor.obtenerDatos(json, RespuestaLibros.class);

    }
    private void buscarLibroPorAutor() {
        RespuestaLibros respuesta = getDatosLibro();
        Optional<DatosLibro> libroBuscado = respuesta.resultados().stream()
                .filter(l -> l.nombre().toUpperCase().contains(busqueda.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()) {
            System.out.println("Libro encontrado");
            DatosLibro datosLibro = libroBuscado.get();
            // Buscamos el primer autor del libro
            DatosAutor datosAutor = datosLibro.autores().isEmpty()
                    ? new DatosAutor("Desconocido", 0, 0)
                    : datosLibro.autores().get(0);
            // Buscar si ese autor ya existe en la BD
            Autor autor = autorRepository.findByNombreAutor(datosAutor.autor())
                    .orElseGet(() -> autorRepository.save(new Autor(datosAutor)));
            Libro libro = new Libro(datosLibro);
            libro.setAutor(autor);
            // Guadar libro
            repository.save(libro);

        } else {
            System.out.println("Libro no encontrado");
        }
    }
    private void mostrarAutores() {
        List<String> autores = repository.mostrarAutores();
        autores.forEach(System.out::println);
    }
    private void autoresVivos() {
        System.out.println("Ingresa un año: ");
        var fecha = Integer.valueOf(teclado.nextLine());
        List<String> autoresVivos = repository.autoresVivos(fecha);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontró ningún autor vivo en el año que ingresó");
        } else {
            System.out.println("Autores vivos en el año " + fecha);
            autoresVivos.forEach(System.out::println);
        }

    }
    private void mostrarTodosLosLibros() {
        libros = repository.findAll();
        libros.forEach(System.out::println);
    }
    private void mostrarLibrosPorIdioma() {
        System.out.println("""
                Seleccione el idioma:
                en - Inglés
                es - Español
                """);
        var idioma = teclado.nextLine();
        List<String> librosPorIdioma = repository.librosPorIdioma(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No tenemos ningún libro registrado en ese idioma");
        } else {
            System.out.println("LENGUAJE: " + idioma);
            librosPorIdioma.forEach(System.out::println);
        }
    }

}
