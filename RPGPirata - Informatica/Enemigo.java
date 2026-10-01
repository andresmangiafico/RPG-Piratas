public class Enemigo {

    private String nombre;
    private final int nivel;
    private int vida;
    private final int vidaMaxima;
    private final int ataque;
    private final int defensa;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El enemigo necesita un nombre."
            );
        }

        this.nombre = nombre.trim();
    }

    public int getNivel() {
        return nivel;
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

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public void recibirDano(int dano) {
        if (dano < 0) {
            throw new IllegalArgumentException(
                    "El daño no puede ser negativo."
            );
        }

        setVida(Math.max(0, vida - dano));
    }
}