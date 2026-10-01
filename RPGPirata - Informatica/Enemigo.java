// NOTA: Modelo de enemigo. Sus estadísticas iniciales vienen del constructor, no del menú.
public class Enemigo {

    private String nombre;
    private final int nivel;
    private int vida;
    private final int vidaMaxima;
    private final int ataque;
    private final int defensa;

    // NOTA: Constructor. Orden de argumentos: nombre, nivel, vida, ataque, defensa. Valida estadísticas; fija la vida máxima antes de llamar a setVida().
    public Enemigo(
            String nombre,
            int nivel,
            int vida,
            int ataque,
            int defensa
    ) {
        if (nivel < 1 || vida < 1 || ataque < 1 || defensa < 0) {
            throw new IllegalArgumentException(
                    "Las estadísticas del enemigo son inválidas."
            );
        }

        setNombre(nombre);

        this.nivel = nivel;
        this.vidaMaxima = vida;
        this.ataque = ataque;
        this.defensa = defensa;

        setVida(vida);
    }

    // NOTA: Getter: permite consultar el nombre sin acceder directamente al atributo privado.
    public String getNombre() {
        return nombre;
    }

    // NOTA: Setter validado: rechaza null o texto vacío y guarda el nombre sin espacios exteriores. this.nombre es el atributo; nombre es el parámetro.
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El enemigo necesita un nombre."
            );
        }

        this.nombre = nombre.trim();
    }

    // NOTA: Consulta el nivel actual; no permite modificarlo desde fuera.
    public int getNivel() {
        return nivel;
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

    // NOTA: Consulta el ataque del enemigo; en Personaje lo calcula con nivel y grado de espada.
    public int getAtaque() {
        return ataque;
    }

    // NOTA: Consulta la defensa que se resta al ataque recibido.
    public int getDefensa() {
        return defensa;
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
}
