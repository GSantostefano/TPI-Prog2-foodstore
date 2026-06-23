package integrado.prog2.entities;

import integrado.prog2.exception.ValidacionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Categoria extends Base {

    private String nombre;
    private String descripcion;
    private List<Producto> productos;

    public Categoria(String nombre, String descripcion) {
        super();
        setNombre(nombre);
        setDescripcion(descripcion);
        this.productos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (!textoValido(nombre)) {
            throw new ValidacionException("El nombre de la categoria no puede estar vacio.");
        }
        this.nombre = nombre.trim();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (!textoValido(descripcion)) {
            throw new ValidacionException("La descripcion de la categoria no puede estar vacia.");
        }
        this.descripcion = descripcion.trim();
    }

    public List<Producto> getProductos() {
        return Collections.unmodifiableList(productos);
    }

    public void agregarProducto(Producto producto) {
        if (producto != null && !productos.contains(producto)) {
            productos.add(producto);
            if (producto.getCategoria() != this) {
                producto.setCategoria(this);
            }
        }
    }

    public void quitarProducto(Producto producto) {
        productos.remove(producto);
    }

    public long contarProductosActivos() {
        return productos.stream().filter(p -> !p.isEliminado()).count();
    }

    private static boolean textoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("Categoria{id=%d, nombre='%s', descripcion='%s'}",
                getId(), nombre, descripcion);
    }
}
