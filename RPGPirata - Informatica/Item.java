public class Item {

    private String nombre;

    public Item(String nombre) {
        setNombre(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El objeto necesita un nombre."
            );
        }

        this.nombre = nombre.trim();
    }
}