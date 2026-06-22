package integrado.prog2.entities;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Clase abstracta base para todas las entidades del sistema.
 */
public abstract class Base {

    private static final Map<Class<? extends Base>, AtomicLong> CONTADORES = new ConcurrentHashMap<>();

    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;

    protected Base() {
        this.id = siguienteId(getClass());
        this.eliminado = false;
        this.createdAt = LocalDateTime.now();
    }

    private static long siguienteId(Class<? extends Base> tipo) {
        return CONTADORES.computeIfAbsent(tipo, c -> new AtomicLong(1)).getAndIncrement();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Base base = (Base) o;
        return Objects.equals(id, base.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public abstract String toString();
}
