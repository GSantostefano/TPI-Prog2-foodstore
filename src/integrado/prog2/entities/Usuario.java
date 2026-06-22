package integrado.prog2.entities;

import integrado.prog2.enums.Rol;
import integrado.prog2.exception.ValidacionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Usuario extends Base {

    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String contrasenia;
    private Rol rol;
    private List<Pedido> pedidos;

    public Usuario(String nombre, String apellido, String mail, String celular,
            String contrasenia, Rol rol) throws ValidacionException {
        super();
        setNombre(nombre);
        setApellido(apellido);
        setMail(mail);
        setCelular(celular);
        setContrasenia(contrasenia);
        setRol(rol);
        this.pedidos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws ValidacionException {
        if (!textoValido(nombre)) {
            throw new ValidacionException("El nombre no puede estar vacio.");
        }
        this.nombre = nombre.trim();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) throws ValidacionException {
        if (!textoValido(apellido)) {
            throw new ValidacionException("El apellido no puede estar vacio.");
        }
        this.apellido = apellido.trim();
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) throws ValidacionException {
        if (!textoValido(mail) || !mail.contains("@")) {
            throw new ValidacionException("El mail no puede estar vacio y debe tener formato valido.");
        }
        this.mail = mail.trim().toLowerCase();
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) throws ValidacionException {
        if (!textoValido(celular)) {
            throw new ValidacionException("El celular no puede estar vacio.");
        }
        this.celular = celular.trim();
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) throws ValidacionException {
        if (!textoValido(contrasenia)) {
            throw new ValidacionException("La contrasenia no puede estar vacia.");
        }
        this.contrasenia = contrasenia;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) throws ValidacionException {
        if (rol == null) {
            throw new ValidacionException("El rol es obligatorio.");
        }
        this.rol = rol;
    }

    public List<Pedido> getPedidos() {
        return Collections.unmodifiableList(pedidos);
    }

    public void agregarPedido(Pedido pedido) {
        if (pedido != null && !pedidos.contains(pedido)) {
            pedidos.add(pedido);
            if (pedido.getUsuario() != this) {
                pedido.setUsuario(this);
            }
        }
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    private static boolean textoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("Usuario{id=%d, nombre='%s', apellido='%s', mail='%s', rol=%s}",
                getId(), nombre, apellido, mail, rol);
    }
}
