package integrado.prog2.services;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.repository.Repositorio;
import java.util.List;
import java.util.stream.Collectors;

public class ProductoService {

    private final Repositorio<Producto> productoRepo;
    private final Repositorio<Categoria> categoriaRepo;

    public ProductoService(Repositorio<Producto> productoRepo, Repositorio<Categoria> categoriaRepo) {
        this.productoRepo = productoRepo;
        this.categoriaRepo = categoriaRepo;
    }

    public List<Producto> listar() {
        return productoRepo.listarActivos();
    }

    public List<Producto> listarDisponiblesParaPedido() {
        return productoRepo.listarActivos().stream()
                .filter(p -> p.isDisponible() && p.getStock() > 0)
                .collect(Collectors.toList());
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        Categoria categoria = categoriaRepo.buscarActivoPorId(categoriaId)
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "No se encontro la categoria con id " + categoriaId + "."));

        return productoRepo.listarActivos().stream()
                .filter(p -> p.getCategoria() != null && p.getCategoria().getId().equals(categoria.getId()))
                .collect(Collectors.toList());
    }

    public boolean existenProductosEnCategoria(Long categoriaId) {
        return !listarPorCategoria(categoriaId).isEmpty();
    }

    public Producto crear(String nombre, String descripcion, Double precio, int stock,
            String imagen, boolean disponible, Long categoriaId) {
        Categoria categoria = categoriaRepo.buscarActivoPorId(categoriaId)
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "La categoria con id " + categoriaId + " no existe o esta eliminada."));

        Producto producto = new Producto(nombre, precio, descripcion, stock, imagen, disponible, categoria);
        productoRepo.guardar(producto);
        return producto;
    }

    public Producto editar(Long id, String nombre, String descripcion, Double precio, Integer stock,
            String imagen, Boolean disponible, Long categoriaId) {
        Producto producto = obtenerActivo(id);

        if (nombre != null && !nombre.trim().isEmpty()) {
            producto.setNombre(nombre);
        }
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            producto.setDescripcion(descripcion);
        }
        if (precio != null) {
            producto.setPrecio(precio);
        }
        if (stock != null) {
            producto.setStock(stock);
        }
        if (imagen != null && !imagen.trim().isEmpty()) {
            producto.setImagen(imagen);
        }
        if (disponible != null) {
            producto.setDisponible(disponible);
        }
        if (categoriaId != null) {
            Categoria categoria = categoriaRepo.buscarActivoPorId(categoriaId)
                    .orElseThrow(() -> new EntidadNoEncontradaException(
                            "La categoria con id " + categoriaId + " no existe o esta eliminada."));
            producto.setCategoria(categoria);
        }

        return producto;
    }

    public void eliminar(Long id) {
        Producto producto = obtenerActivo(id);
        producto.setEliminado(true);
    }

    public Producto obtenerActivo(Long id) {
        return productoRepo.buscarActivoPorId(id)
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "No se encontro el producto con id " + id + " o esta eliminado."));
    }
}
