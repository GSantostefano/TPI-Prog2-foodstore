package integrado.prog2.services;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.DuplicadoException;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.ValidacionException;
import integrado.prog2.repository.Repositorio;
import java.util.List;

public class CategoriaService {

    private final Repositorio<Categoria> categoriaRepo;
    private final Repositorio<Producto> productoRepo;

    public CategoriaService(Repositorio<Categoria> categoriaRepo, Repositorio<Producto> productoRepo) {
        this.categoriaRepo = categoriaRepo;
        this.productoRepo = productoRepo;
    }

    public List<Categoria> listar() {
        return categoriaRepo.listarActivos();
    }

    public Categoria crear(String nombre, String descripcion) {
        validarTexto(nombre, "El nombre de la categoria no puede estar vacio.");
        validarTexto(descripcion, "La descripcion de la categoria no puede estar vacia.");

        if (existeNombre(nombre.trim())) {
            throw new DuplicadoException("Ya existe una categoria con el nombre '" + nombre.trim() + "'.");
        }

        Categoria categoria = new Categoria(nombre.trim(), descripcion.trim());
        categoriaRepo.guardar(categoria);
        return categoria;
    }

    public Categoria editar(Long id, String nombre, String descripcion) {
        Categoria categoria = obtenerActiva(id);

        if (nombre != null && !nombre.trim().isEmpty()) {
            if (existeNombreExcepto(nombre.trim(), id)) {
                throw new DuplicadoException("Ya existe otra categoria con el nombre '" + nombre.trim() + "'.");
            }
            categoria.setNombre(nombre.trim());
        }

        if (descripcion != null && !descripcion.trim().isEmpty()) {
            categoria.setDescripcion(descripcion.trim());
        }

        return categoria;
    }

    public void eliminar(Long id) {
        Categoria categoria = obtenerActiva(id);

        if (categoria.contarProductosActivos() > 0) {
            throw new ValidacionException(
                    "No se puede eliminar la categoria porque tiene productos asociados activos.");
        }

        categoria.setEliminado(true);
    }

    public Categoria obtenerActiva(Long id) {
        return categoriaRepo.buscarActivoPorId(id)
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "No se encontro la categoria con id " + id + " o esta eliminada."));
    }

    private boolean existeNombre(String nombre) {
        return categoriaRepo.listarActivos().stream()
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(nombre));
    }

    private boolean existeNombreExcepto(String nombre, Long id) {
        return categoriaRepo.listarActivos().stream()
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(nombre) && !c.getId().equals(id));
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacionException(mensaje);
        }
    }
}
