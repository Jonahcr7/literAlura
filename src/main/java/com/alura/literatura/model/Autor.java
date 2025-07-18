package com.alura.literatura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autor")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAutor;
    @Column(unique = true)
    private String nombreAutor;
    private Integer anioDeNacimiento;
    private Integer anioDeMuerte;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor() {}

    public Autor(DatosAutor datosAutor) {
        this.nombreAutor = (datosAutor.autor() == null || datosAutor.autor().isEmpty())
                ? "Desconocido"
                : datosAutor.autor();
        this.anioDeNacimiento = datosAutor.anioDeNacimiento();
        this.anioDeMuerte = datosAutor.anioDeMuerte();
    }

    @Override
    public String toString() {
        return nombreAutor + " (" + anioDeNacimiento + " - " + anioDeMuerte + ")";
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public Integer getAnioDeNacimiento() {
        return anioDeNacimiento;
    }

    public void setAnioDeNacimiento(Integer anioDeNacimiento) {
        this.anioDeNacimiento = anioDeNacimiento;
    }

    public Integer getAnioDeMuerte() {
        return anioDeMuerte;
    }

    public void setAnioDeMuerte(Integer anioDeMuerte) {
        this.anioDeMuerte = anioDeMuerte;
    }
}
