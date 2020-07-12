package com.example.lab4.entidades;

public class ComentariosDTO {

    private String contenido;
    private String fecha;
    private String nombre;

    public ComentariosDTO() {
        this.contenido = contenido;
        this.fecha = fecha;
        this.nombre = nombre;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
