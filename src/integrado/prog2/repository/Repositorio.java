package integrado.prog2.repository;

import integrado.prog2.entities.Base;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositorio generico en memoria para entidades que extienden Base.
 */
public class Repositorio<T extends Base> {

    private final List<T> elementos = new ArrayList<>();

    public void guardar(T entidad) {
        elementos.add(entidad);
    }

    public List<T> listarActivos() {
        return elementos.stream()
                .filter(e -> !e.isEliminado())
                .collect(Collectors.toList());
    }

    public List<T> listarTodos() {
        return new ArrayList<>(elementos);
    }

    public Optional<T> buscarActivoPorId(Long id) {
        return elementos.stream()
                .filter(e -> !e.isEliminado() && e.getId().equals(id))
                .findFirst();
    }

    public Optional<T> buscarPorId(Long id) {
        return elementos.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }
}
