package integrado.prog2.services;

import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.exception.DuplicadoException;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.repository.Repositorio;
import java.util.List;

public class UsuarioService {

    private final Repositorio<Usuario> usuarioRepo;

    public UsuarioService(Repositorio<Usuario> usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public List<Usuario> listar() {
        return usuarioRepo.listarActivos();
    }

    public Usuario crear(String nombre, String apellido, String mail, String celular,
            String contrasenia, Rol rol) {
        if (existeMail(mail)) {
            throw new DuplicadoException("Ya existe un usuario con el mail '" + mail.trim().toLowerCase() + "'.");
        }

        Usuario usuario = new Usuario(nombre, apellido, mail, celular, contrasenia, rol);
        usuarioRepo.guardar(usuario);
        return usuario;
    }

    public Usuario editar(Long id, String nombre, String apellido, String mail, String celular,
            String contrasenia, Rol rol) {
        Usuario usuario = obtenerActivo(id);

        if (nombre != null && !nombre.trim().isEmpty()) {
            usuario.setNombre(nombre);
        }
        if (apellido != null && !apellido.trim().isEmpty()) {
            usuario.setApellido(apellido);
        }
        if (mail != null && !mail.trim().isEmpty()) {
            if (existeMailExcepto(mail, id)) {
                throw new DuplicadoException("Ya existe otro usuario con el mail '" + mail.trim().toLowerCase() + "'.");
            }
            usuario.setMail(mail);
        }
        if (celular != null && !celular.trim().isEmpty()) {
            usuario.setCelular(celular);
        }
        if (contrasenia != null && !contrasenia.trim().isEmpty()) {
            usuario.setContrasenia(contrasenia);
        }
        if (rol != null) {
            usuario.setRol(rol);
        }

        return usuario;
    }

    public void eliminar(Long id) {
        Usuario usuario = obtenerActivo(id);
        usuario.setEliminado(true);
    }

    public Usuario obtenerActivo(Long id) {
        return usuarioRepo.buscarActivoPorId(id)
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "No se encontro el usuario con id " + id + " o esta eliminado."));
    }

    private boolean existeMail(String mail) {
        if (mail == null) {
            return false;
        }
        String normalizado = mail.trim().toLowerCase();
        return usuarioRepo.listarActivos().stream()
                .anyMatch(u -> u.getMail().equalsIgnoreCase(normalizado));
    }

    private boolean existeMailExcepto(String mail, Long id) {
        if (mail == null) {
            return false;
        }
        String normalizado = mail.trim().toLowerCase();
        return usuarioRepo.listarActivos().stream()
                .anyMatch(u -> u.getMail().equalsIgnoreCase(normalizado) && !u.getId().equals(id));
    }
}
