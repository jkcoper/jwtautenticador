package com.example.demo2;

public class Producto {
    private String id_producto;
    private String nombre;
    private String id_user;

    public Producto() {}

    public Producto(String id_producto, String nombre, String id_user) {
        this.id_producto = id_producto;
        this.nombre = nombre;
        this.id_user = id_user;
    }

    // Getters y setters
    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
