// NOTA: Modelo de una pieza del barco. Atributo privado + constructor + getter/setter validado.
public class Item {

    private String nombre;

    // NOTA: Constructor: se ejecuta con new Item(...). Usa el setter para validar el nombre desde el nacimiento del objeto.
    public Item(String nombre) {
        setNombre(nombre);
    }

    // NOTA: Getter: permite consultar el nombre sin acceder directamente al atributo privado.
    public String getNombre() {
        return nombre;
    }

    // NOTA: Setter validado: rechaza null o texto vacío y guarda el nombre sin espacios exteriores. this.nombre es el atributo; nombre es el parámetro.
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El objeto necesita un nombre."
            );
        }

        this.nombre = nombre.trim();
    }
}
