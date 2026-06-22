package integrado.prog2.entities;

import integrado.prog2.exception.CantidadInvalidaException;
import integrado.prog2.exception.PrecioInvalidoException;
import integrado.prog2.exception.StockInvalidoException;
import integrado.prog2.exception.ValidacionException;

public class Producto extends Base {

    private String nombre;
    private Double precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private boolean disponible;
    private Categoria categoria;

    public Producto(String nombre, Double precio, String descripcion, int stock, String imagen,
            boolean disponible, Categoria categoria) throws PrecioInvalidoException, ValidacionException {
        super();
        setNombre(nombre);
        setPrecio(precio);
        setDescripcion(descripcion);
        setStock(stock);
        setImagen(imagen);
        setDisponible(disponible);
        setCategoria(categoria);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws ValidacionException {
        if (!textoValido(nombre)) {
            throw new ValidacionException("El nombre del producto no puede estar vacio.");
        }
        this.nombre = nombre.trim();
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) throws PrecioInvalidoException {
        if (precio == null || precio < 0) {
            throw new PrecioInvalidoException("El precio debe ser mayor o igual a 0.");
        }
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) throws ValidacionException {
        if (!textoValido(descripcion)) {
            throw new ValidacionException("La descripcion del producto no puede estar vacia.");
        }
        this.descripcion = descripcion.trim();
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) throws StockInvalidoException {
        if (stock < 0) {
            throw new StockInvalidoException("El stock debe ser mayor o igual a 0.");
        }
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) throws ValidacionException {
        if (!textoValido(imagen)) {
            throw new ValidacionException("La imagen del producto no puede estar vacia.");
        }
        this.imagen = imagen.trim();
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        if (this.categoria != null && this.categoria != categoria) {
            this.categoria.quitarProducto(this);
        }
        this.categoria = categoria;
        if (categoria != null) {
            categoria.agregarProducto(this);
        }
    }

    public void descontarStock(int cantidad) throws CantidadInvalidaException, StockInvalidoException {
        if (cantidad <= 0) {
            throw new CantidadInvalidaException("La cantidad a descontar debe ser mayor a 0.");
        }
        if (cantidad > stock) {
            throw new StockInvalidoException("Stock insuficiente para el producto " + nombre + ".");
        }
        this.stock -= cantidad;
    }

    public void restaurarStock(int cantidad) {
        if (cantidad > 0) {
            this.stock += cantidad;
        }
    }

    private static boolean textoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    @Override
    public String toString() {
        return String.format(
                "Producto{id=%d, nombre='%s', precio=%.2f, stock=%d, categoria=%s, disponible=%s}",
                getId(), nombre, precio, stock,
                categoria != null ? categoria.getNombre() : "sin categoria", disponible);
    }
}
