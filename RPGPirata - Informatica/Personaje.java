// NOTA: Modelo del jugador: guarda estadísticas e inventario y aplica las reglas de evolución.
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Personaje {

    private String nombre;
    // NOTA: Nivel inicial. Afecta al ataque, habilidad y experiencia necesaria.
    private int nivel = 1;
    // NOTA: Experiencia disponible para avanzar al siguiente nivel.
    private int experiencia = 0;
    // NOTA: PERSONALIZAR: dinero inicial, por ejemplo 100 para facilitar una demostración de mejoras.
    private int monedas = 0;

    // NOTA: PERSONALIZAR: límite inicial de vida. Si quieres empezar completamente curado, cambia también vida.
    private int vidaMaxima = 120;
    // NOTA: PERSONALIZAR: vida al empezar una partida nueva.
    private int vida = 120;

    // NOTA: Grado inicial de espada. No es el daño completo: getAtaque() calcula el daño base.
    private int espada = 1;
    // NOTA: Grado inicial de protección. El combate usa este valor para reducir daño.
    private int proteccion = 1;

    // Estructura de datos estándar de Java.
    // NOTA: ArrayList<Item> admite objetos Item. final impide reasignar la lista, pero permite añadir objetos.
    private final ArrayList<Item> inventario = new ArrayList<>();

    // NOTA: Constructor: crea un pirata con el nombre recibido y los valores iniciales de los atributos.
    public Personaje(String nombre) {
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
                    "El personaje necesita un nombre."
            );
        }

        this.nombre = nombre.trim();
    }

    // NOTA: Consulta el nivel actual; no permite modificarlo desde fuera.
    public int getNivel() {
        return nivel;
    }

    // NOTA: Devuelve la experiencia acumulada para el siguiente nivel, no toda la experiencia histórica.
    public int getExperiencia() {
        return experiencia;
    }

    // NOTA: Consulta el dinero disponible. Solo las recompensas y compras lo modifican.
    public int getMonedas() {
        return monedas;
    }

    // NOTA: Consulta los puntos de vida que quedan.
    public int getVida() {
        return vida;
    }

    // NOTA: Setter validado: solo acepta vida entre 0 y vidaMaxima. Protege el estado del objeto.
    public void setVida(int vida) {
        if (vida < 0 || vida > vidaMaxima) {
            throw new IllegalArgumentException(
                    "La vida está fuera del rango permitido."
            );
        }

        this.vida = vida;
    }

    // NOTA: Consulta el límite de vida. Aumenta al subir de nivel.
    public int getVidaMaxima() {
        return vidaMaxima;
    }

    // NOTA: Consulta el grado de la espada; se usa tanto en el ataque como en el precio de mejora.
    public int getEspada() {
        return espada;
    }

    // NOTA: Consulta el grado de protección; cada grado reduce el daño enemigo en el cálculo del combate.
    public int getProteccion() {
        return proteccion;
    }

    // NOTA: Consulta el ataque del enemigo; en Personaje lo calcula con nivel y grado de espada.
    public int getAtaque() {
        // NOTA: Fórmula del ataque base: 8 fijos + 3 por nivel + 4 por grado de espada.
        return 8 + nivel * 3 + espada * 4;
    }

    // Devuelve copias para proteger el inventario original.
    // NOTA: Devuelve una lista no modificable con copias de los Item. No entrega los objetos internos: así se mantiene la encapsulación.
    public List<Item> getInventario() {
        List<Item> copia = new ArrayList<>();

        for (Item item : inventario) {
            copia.add(new Item(item.getNombre()));
        }

        // NOTA: Bloquea añadir o quitar elementos en la lista entregada. Los elementos también son copias.
        return Collections.unmodifiableList(copia);
    }

    // NOTA: Valida el objeto y guarda una copia de su nombre. Cambiar el objeto original después no altera el inventario.
    public void agregarItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "No puedes agregar un objeto nulo."
            );
        }

        inventario.add(new Item(item.getNombre()));
    }

    // Algoritmo de búsqueda lineal: recorre los objetos uno por uno.
    // NOTA: Búsqueda lineal O(n): compara uno por uno hasta encontrar el nombre. Ignora mayúsculas y espacios exteriores, pero no tildes. Devuelve copia o null.
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

    // NOTA: Rechaza daño negativo y descuenta vida. Math.max(0, ...) impide que la vida quede por debajo de cero.
    public void recibirDano(int dano) {
        if (dano < 0) {
            throw new IllegalArgumentException(
                    "El daño no puede ser negativo."
            );
        }

        setVida(Math.max(0, vida - dano));
    }

    // NOTA: Cura hasta vidaMaxima. No aumenta la vida máxima y actualmente no cuesta monedas.
    public void descansar() {
        setVida(vidaMaxima);
    }

    // NOTA: Otorga monedas y experiencia según el nivel enemigo. El while permite subir varios niveles si alcanza la experiencia; conserva el sobrante.
    public void recompensar(int nivelEnemigo) {
        if (nivelEnemigo < 1) {
            throw new IllegalArgumentException(
                    "El nivel del enemigo debe ser positivo."
            );
        }

        // NOTA: PERSONALIZAR: monedas por victoria. Ajusta también el mensaje en RPGPirata.explorar().
        monedas += nivelEnemigo * 25;
        // NOTA: PERSONALIZAR: experiencia por victoria. Ajusta también el mensaje en RPGPirata.explorar().
        experiencia += nivelEnemigo * 30;

        // NOTA: Umbral de subida. Si cambias 40, cambia también la resta siguiente y el indicador en mostrarPersonaje().
        while (experiencia >= nivel * 40) {
            experiencia -= nivel * 40;
            nivel++;

            // NOTA: PERSONALIZAR: vida máxima adicional por cada nivel ganado.
            vidaMaxima += 20;
            descansar();
        }
    }

    // NOTA: Regla de compra: true mejora espada; false, protección. Comprueba el dinero ANTES de descontar. throws avisa que puede fallar con nuestra excepción.
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

    // NOTA: Busca las tres piezas necesarias. Si falta cualquiera, lanza la excepción y evita la victoria. Solo valida: no imprime ni consume objetos.
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
