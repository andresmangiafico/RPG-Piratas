import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Personaje {

    private String nombre;
    private int nivel = 1;
    private int experiencia = 0;
    private int monedas = 0;

    private int vidaMaxima = 120;
    private int vida = 120;

    private int espada = 1;
    private int proteccion = 1;

    // Estructura de datos estándar de Java.
    private final ArrayList<Item> inventario = new ArrayList<>();

    public Personaje(String nombre) {
        setNombre(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El personaje necesita un nombre."
            );
        }

        this.nombre = nombre.trim();
    }

    public int getNivel() {
        return nivel;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public int getMonedas() {
        return monedas;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        if (vida < 0 || vida > vidaMaxima) {
            throw new IllegalArgumentException(
                    "La vida está fuera del rango permitido."
            );
        }

        this.vida = vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getEspada() {
        return espada;
    }

    public int getProteccion() {
        return proteccion;
    }

    public int getAtaque() {
        return 8 + nivel * 3 + espada * 4;
    }

    // Devuelve copias para proteger el inventario original.
    public List<Item> getInventario() {
        List<Item> copia = new ArrayList<>();

        for (Item item : inventario) {
            copia.add(new Item(item.getNombre()));
        }

        return Collections.unmodifiableList(copia);
    }

    public void agregarItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "No puedes agregar un objeto nulo."
            );
        }

        inventario.add(new Item(item.getNombre()));
    }

    // Algoritmo de búsqueda lineal: recorre los objetos uno por uno.
    public Item buscarItem(String nombreBuscado) {
        if (nombreBuscado == null) {
            return null;
        }

        for (Item item : inventario) {
            if (item.getNombre().equalsIgnoreCase(nombreBuscado.trim())) {
                return new Item(item.getNombre());
            }
        }

        return null;
    }

    public void recibirDano(int dano) {
        if (dano < 0) {
            throw new IllegalArgumentException(
                    "El daño no puede ser negativo."
            );
        }

        setVida(Math.max(0, vida - dano));
    }

    public void descansar() {
        setVida(vidaMaxima);
    }

    public void recompensar(int nivelEnemigo) {
        if (nivelEnemigo < 1) {
            throw new IllegalArgumentException(
                    "El nivel del enemigo debe ser positivo."
            );
        }

        monedas += nivelEnemigo * 25;
        experiencia += nivelEnemigo * 30;

        while (experiencia >= nivel * 40) {
            experiencia -= nivel * 40;
            nivel++;

            vidaMaxima += 20;
            descansar();
        }
    }

    public void mejorarEquipo(boolean mejorarEspada)
            throws AccionInvalidaException {

        int costo;

        if (mejorarEspada) {
            costo = espada * 30;
        } else {
            costo = proteccion * 30;
        }

        if (monedas < costo) {
            throw new AccionInvalidaException(
                    "No tienes suficientes monedas."
            );
        }

        monedas -= costo;

        if (mejorarEspada) {
            espada++;
        } else {
            proteccion++;
        }
    }

    public void validarReparacion() throws AccionInvalidaException {
        boolean tieneMadera = buscarItem("Madera") != null;
        boolean tieneVela = buscarItem("Vela") != null;
        boolean tieneTimon = buscarItem("Timón") != null;

        if (!tieneMadera || !tieneVela || !tieneTimon) {
            throw new AccionInvalidaException(
                    "Necesitas Madera, Vela y Timón para reparar el barco."
            );
        }
    }
}