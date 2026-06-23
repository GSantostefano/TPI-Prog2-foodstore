# Food Store — Trabajo Práctico Integrador de Programación II

Sistema de gestión de pedidos de comida (**Food Store**) desarrollado como
aplicación de consola en **Java**. El trabajo integra los contenidos de
Programación II: Programación Orientada a Objetos (herencia, abstracción,
interfaces, genéricos, enumerados y excepciones propias), manejo de colecciones,
validaciones y una arquitectura en capas.

## Integrantes

- Juan Santostefano
- Gabriel Santostefano

## Enlaces de la entrega

- **Repositorio en GitHub:** https://github.com/GSantostefano/TPI-Prog2-foodstore
- **Documentación (PDF):**
- **Documentación (Video):**


---

## ¿Qué hace el sistema?

Food Store permite administrar el circuito completo de pedidos de un local de
comidas, desde una interfaz de consola con menús:

- **Categorías:** crear, listar, editar y eliminar (con baja lógica). No se puede
  eliminar una categoría que tenga productos activos asociados.
- **Productos:** crear, listar, listar disponibles para pedido, listar por
  categoría, editar y eliminar. Cada producto pertenece a una categoría.
- **Usuarios:** crear, listar, editar y eliminar. Cada usuario tiene un rol
  (`ADMIN` o `USUARIO`).
- **Pedidos:** crear un pedido con uno o varios detalles (descontando stock),
  listar, listar por usuario, actualizar estado/forma de pago y eliminar. Al
  cancelar o eliminar un pedido se **repone** el stock.

Al iniciar, la clase `CargaInicial` carga **datos de ejemplo** (3 categorías,
5 productos y 2 usuarios) para facilitar la demostración.

---

## Conceptos de POO aplicados

| Concepto | Dónde se aplica |
|---|---|
| **Abstracción / Herencia** | Clase abstracta `Base` (id, `eliminado`, `createdAt`) de la que heredan todas las entidades. |
| **Polimorfismo** | Método `toString()` abstracto en `Base`, redefinido por cada entidad; captura unificada de errores por la clase base `FoodStoreException`. |
| **Interfaces** | `Calculable` (método `calcularTotal()`), implementada por `Pedido`. |
| **Genéricos** | `Repositorio<T extends Base>`: un único repositorio en memoria reutilizable para todas las entidades. |
| **Enumerados** | `Rol`, `Estado` y `FormaPago`. |
| **Excepciones personalizadas** | Jerarquía con raíz `FoodStoreException` (extiende `RuntimeException`): `ValidacionException`, `DuplicadoException`, `EntidadNoEncontradaException`, `PrecioInvalidoException`, `StockInvalidoException`, `CantidadInvalidaException`. |
| **Encapsulamiento** | Atributos privados con getters/setters que validan; listas expuestas como inmutables (`Collections.unmodifiableList`). |
| **Colecciones y Streams** | `List`/`ArrayList`, filtrado y consultas con la API de Streams. |
| **Relaciones bidireccionales** | Categoría ↔ Producto, Usuario ↔ Pedido, Pedido ↔ DetallePedido. |
| **Baja lógica (soft delete)** | Campo `eliminado` en `Base`; se listan solo los registros activos. |

---

## Reglas de negocio

- El **precio** de un producto debe ser **≥ 0** y el **stock ≥ 0**.
- El **nombre de la categoría** es obligatorio y **único**.
- El **mail del usuario** es obligatorio, con formato válido y **único**.
- **No se puede crear un pedido sin un usuario válido**, y un pedido debe tener
  al menos un detalle.
- En un detalle, la **cantidad debe ser > 0** y el **subtotal = cantidad × precio**.
- Un mismo producto **no se duplica** dentro de un pedido (se acumula la cantidad).
- Al crear un pedido se **descuenta el stock**; si falla algún detalle, se
  **revierte** el stock ya descontado (*rollback*).
- El **total del pedido** es la suma de los subtotales de sus detalles activos.

---

## Estructura del proyecto

```
src/integrado/prog2/
├── Main.java                 Punto de entrada (arranca la carga inicial y el menú)
├── config/
│   ├── AppContext.java       Wiring: instancia repositorios y servicios
│   └── CargaInicial.java     Datos de ejemplo al iniciar
├── entities/
│   ├── Base.java             Clase abstracta base (id, eliminado, createdAt)
│   ├── Categoria.java
│   ├── Producto.java
│   ├── Usuario.java
│   ├── Pedido.java           Implementa Calculable
│   └── DetallePedido.java
├── enums/
│   ├── Rol.java              ADMIN, USUARIO
│   ├── Estado.java           PENDIENTE, CONFIRMADO, TERMINADO, CANCELADO
│   └── FormaPago.java        TARJETA, TRANSFERENCIA, EFECTIVO
├── exception/                Jerarquía de excepciones de dominio (FoodStoreException)
├── interfaces/
│   └── Calculable.java
├── repository/
│   └── Repositorio.java      Repositorio genérico en memoria
├── services/                 Lógica de negocio (Categoria/Producto/Usuario/Pedido)
└── ui/                       Menús de consola (Principal, Categorias, Productos, Usuarios, Pedidos)
```

---

## Requisitos

- **JDK 21** (el proyecto está configurado con `source`/`target` 21 en NetBeans).
- **NetBeans** o **Apache Ant** para compilar y ejecutar.

## Cómo ejecutar

**Con NetBeans:** abrir el proyecto, verificar JDK 21 en propiedades y ejecutar *Run* (F6). Clase principal: `integrado.prog2.Main`.

**Con Ant (consola):**

```bash
# compilar y generar el .jar
ant clean jar

# ejecutar el .jar generado
java -jar dist/TPI-Prog2-foodstore.jar

# o, alternativamente, compilar y ejecutar en un paso
ant run
```

**Con javac (PowerShell):**

```powershell
javac -d build/classes -encoding UTF-8 --release 21 (Get-ChildItem -Path src -Recurse -Filter *.java).FullName
java -cp build/classes integrado.prog2.Main
```

Al iniciar, se muestra el menú principal:

```
=== SISTEMA DE PEDIDOS (FOOD STORE) ===
1. Categorias
2. Productos
3. Usuarios
4. Pedidos
0. Salir
```

---

## Tecnologías

- **Java 21**
- **NetBeans / Apache Ant** (estructura de proyecto y build)
- Programación Orientada a Objetos · API de Colecciones y Streams · `java.time`
