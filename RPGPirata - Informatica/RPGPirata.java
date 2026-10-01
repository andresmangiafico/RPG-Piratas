import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class RPGPirata {

    private final Scanner teclado;
    private final Random azar;

    private final ArrayList<Enemigo> enemigos = new ArrayList<>();

    private Personaje jugador;
    private int progreso;
    private boolean partidaActiva;

    public RPGPirata(Scanner teclado, Random azar) {
        this.teclado = teclado;
        this.azar = azar;
    }

    public static void iniciar() {
        try (Scanner teclado = new Scanner(System.in)) {
            RPGPirata juego = new RPGPirata(teclado, new Random());
            juego.mostrarMenuPrincipal();

        } catch (NoSuchElementException e) {
            System.out.println("\nEntrada cerrada. Juego terminado.");
        }
    }

    public void mostrarMenuPrincipal() {
        while (true) {
            System.out.println("\n================================");
            System.out.println("       LA ISLA DEL CORSARIO");
            System.out.println("================================");
            System.out.println("1. Iniciar partida");
            System.out.println("0. Salir");

            int opcion = leerOpcion(0, 1);

            if (opcion == 0) {
                System.out.println("¡Hasta la próxima aventura!");
                return;
            }

            nuevaPartida();
            jugar();
        }
    }

    private void nuevaPartida() {
        System.out.print("\nNombre de tu pirata: ");
        String nombre = teclado.nextLine().trim();

        if (nombre.isEmpty()) {
            nombre = "Marinero";
        }

        jugador = new Personaje(nombre);
        progreso = 0;
        partidaActiva = true;

        enemigos.clear();

        enemigos.add(
                new Enemigo("Cangrejo gigante", 1, 52, 18, 1)
        );

        enemigos.add(
                new Enemigo("Saqueador de la playa", 2, 65, 24, 2)
        );

        enemigos.add(
                new Enemigo("Pirata renegado", 3, 89, 32, 3)
        );

        enemigos.add(
                new Enemigo("Guardián de las ruinas", 4, 123, 39, 5)
        );

        enemigos.add(
                new Enemigo("Corsario fantasma", 5, 155, 51, 6)
        );

        enemigos.add(
                new Enemigo("Capitán Maldito", 6, 190, 60, 8)
        );

        System.out.println("\nUna tormenta ha destruido tu barco y acabo con toda tu tripulación, haz quedado varado en una isla desconocida.");
        System.out.println("Cuando despiertas ves que todo esta perdido te preguntas donde estas pero no lo puedes descifrar, te pones en busca de los materiales necesarios para reconstruir tu barco, no sera una mison sencila...");
        System.out.println("Tu espada es lo único que conservas y sera tu unica aliada en esta isla misteriosa, debes derrotar a los enemigos que se interponen en tu camino y recuperar los materiales necesarios para escapar de la isla.");
        System.out.println(
                "Debes recuperar Madera, Vela y Timón para escapar."
        );
        System.out.println(
                "El Capitán Maldito guarda la última pieza..."
        );
    }

    private void jugar() {
        while (partidaActiva) {
            System.out.println("\n========== CAMPAMENTO ==========");
            System.out.println("1. Explorar");
            System.out.println("2. Descansar");
            System.out.println("3. Mejorar equipo");
            System.out.println("4. Ver personaje");
            System.out.println("5. Reparar barco");
            System.out.println("6. Ver inventario");
            System.out.println("7. Buscar objeto");
            System.out.println("0. Abandonar partida");

            try {
                int opcion = leerOpcion(0, 7);

                switch (opcion) {
                    case 1:
                        explorar();
                        break;

                    case 2:
                        jugador.descansar();
                        System.out.println(
                                "Descansas y recuperas toda tu vida."
                        );
                        break;

                    case 3:
                        mejorarEquipo();
                        break;

                    case 4:
                        mostrarPersonaje();
                        break;

                    case 5:
                        repararBarco();
                        break;

                    case 6:
                        mostrarInventario();
                        break;

                    case 7:
                        buscarObjeto();
                        break;

                    case 0:
                        terminar("PARTIDA ABANDONADA");
                        break;
                }

            } catch (AccionInvalidaException e) {
                System.out.println(
                        "Acción inválida: " + e.getMessage()
                );
            }
        }
    }

    private void explorar() {
        if (progreso == enemigos.size()) {
            System.out.println(
                    "Ya venciste a todos los enemigos. ¡Repara tu barco!"
            );
            return;
        }

        if (progreso < 2) {
            System.out.println("\nExploras la playa.");
        } else if (progreso < 4) {
            System.out.println("\nTe adentras en las ruinas de la selva.");
        } else {
            System.out.println("\nEntras en la fortaleza del capitán.");
        }

        Enemigo modelo = enemigos.get(progreso);

        // Cada intento tiene un enemigo con la vida completa.
        Enemigo enemigo = new Enemigo(
                modelo.getNombre(),
                modelo.getNivel(),
                modelo.getVida(),
                modelo.getAtaque(),
                modelo.getDefensa()
        );

        if (combatir(enemigo)) {
            progreso++;

            int nivelAnterior = jugador.getNivel();
            jugador.recompensar(enemigo.getNivel());

            System.out.println(
                    "Ganaste " + enemigo.getNivel() * 25 + " monedas."
            );

            System.out.println(
                    "Ganaste " + enemigo.getNivel() * 30
                            + " puntos de experiencia."
            );

            if (jugador.getNivel() > nivelAnterior) {
                System.out.println(
                        "¡Subiste al nivel " + jugador.getNivel() + "!"
                );

                System.out.println(
                        "Tu vida, ataque y habilidad han mejorado."
                );
            }

            entregarPieza();
        }
    }

    private void entregarPieza() {
        String pieza = null;

        if (progreso == 2) {
            pieza = "Madera";
        } else if (progreso == 4) {
            pieza = "Vela";
        } else if (progreso == 6) {
            pieza = "Timón";
        }

        if (pieza != null) {
            jugador.agregarItem(new Item(pieza));
            System.out.println("¡Recuperaste: " + pieza + "!");
        }
    }

    private boolean combatir(Enemigo enemigo) {
        int enfriamiento = 0;

        System.out.println(
                "\nAparece " + enemigo.getNombre()
                        + " — Nivel " + enemigo.getNivel()
        );

        while (jugador.getVida() > 0 && enemigo.getVida() > 0) {
            System.out.println("\n---------- COMBATE ----------");

            System.out.println(
                    "Tu vida: " + jugador.getVida()
                            + "/" + jugador.getVidaMaxima()
            );

            System.out.println(
                    "Vida del enemigo: " + enemigo.getVida()
            );

            System.out.println("1. Atacar con espada");
            System.out.println(
                    "2. Corte del corsario — Recarga: "
                            + enfriamiento + " turnos"
            );
            System.out.println("3. Defenderse");
            System.out.println("4. Huir");

            int opcion = leerOpcion(1, 4);

            if (opcion == 4) {
                System.out.println(
                        "Regresas al campamento sin recompensas."
                );
                return false;
            }

            try {
                if (opcion == 2 && enfriamiento > 0) {
                    throw new AccionInvalidaException(
                            "La habilidad todavía se está recargando."
                    );
                }

            } catch (AccionInvalidaException e) {
                System.out.println(e.getMessage());
                continue;
            }

            boolean defendiendo = opcion == 3;

            if (defendiendo) {
                System.out.println("Preparas tu defensa.");

            } else {
                int dano = jugador.getAtaque() + azar.nextInt(6);

                if (opcion == 2) {
                    dano += 10 + jugador.getNivel() * 4;
                    enfriamiento = 3;

                    System.out.println("¡Usas Corte del corsario!");
                }

                dano = Math.max(1, dano - enemigo.getDefensa());

                enemigo.recibirDano(dano);

                System.out.println(
                        "Causas " + dano + " puntos de daño."
                );
            }

            if (enemigo.getVida() == 0) {
                System.out.println(
                        "¡Derrotaste a " + enemigo.getNombre() + "!"
                );
                return true;
            }

            int danoRecibido = Math.max(
                    1,
                    enemigo.getAtaque()
                            + azar.nextInt(5)
                            - jugador.getProteccion() * 3
            );

            if (defendiendo) {
                danoRecibido = Math.max(1, danoRecibido / 2);
            }

            jugador.recibirDano(danoRecibido);

            System.out.println(
                    "Recibes " + danoRecibido + " puntos de daño."
            );

            // La habilidad se recarga realizando otras tres acciones.
            if (enfriamiento > 0 && opcion != 2) {
                enfriamiento--;
            }
        }

        System.out.println();
System.out.println("       .----------------.");
System.out.println("      /                  \\");
System.out.println("     |     >      <       |");
System.out.println("     |     |      |       |");
System.out.println("     |     |  __  |       |");
System.out.println("     |     | /  \\ |       |");
System.out.println("      \\    |      |      /");
System.out.println("       '----------------'");
System.out.println();
System.out.println("       La batalla ha sido dura y has caído en combate, no haz podido reparar tu barco y escapar de la isla.");
System.out.println("   El mar tendrá que esperar a que otro corsario se haga con la victoria.");
System.out.println("       Puedes intentarlo de nuevo.");
System.out.println();
terminar("DERROTA: te quedaste sin vida");
        return false;
    }

    private void mejorarEquipo() throws AccionInvalidaException {
        System.out.println("\n========== MEJORAS ==========");

        System.out.println(
                "Monedas disponibles: " + jugador.getMonedas()
        );

        System.out.println(
                "1. Mejorar espada: "
                        + jugador.getEspada() * 30 + " monedas"
        );

        System.out.println(
                "2. Mejorar protección: "
                        + jugador.getProteccion() * 30 + " monedas"
        );

        System.out.println("0. Volver");

        int opcion = leerOpcion(0, 2);

        if (opcion == 0) {
            return;
        }

        jugador.mejorarEquipo(opcion == 1);

        System.out.println("¡Equipo mejorado!");
    }

    private void mostrarPersonaje() {
        System.out.println("\n========== PERSONAJE ==========");

        System.out.println("Nombre: " + jugador.getNombre());
        System.out.println("Nivel: " + jugador.getNivel());

        System.out.println(
                "Vida: " + jugador.getVida()
                        + "/" + jugador.getVidaMaxima()
        );

        System.out.println(
                "Experiencia: " + jugador.getExperiencia()
                        + "/" + jugador.getNivel() * 40
        );

        System.out.println(
                "Espada: grado " + jugador.getEspada()
        );

        System.out.println(
                "Protección: grado " + jugador.getProteccion()
        );

        System.out.println("Monedas: " + jugador.getMonedas());

        System.out.println(
                "Enemigos derrotados: " + progreso + "/6"
        );

        System.out.println(
                "Piezas recuperadas: "
                        + jugador.getInventario().size() + "/3"
        );
    }

    private void mostrarInventario() {
        System.out.println("\n========== INVENTARIO ==========");

        if (jugador.getInventario().isEmpty()) {
            System.out.println("Tu inventario está vacío.");
            return;
        }

        for (Item item : jugador.getInventario()) {
            System.out.println("- " + item.getNombre());
        }
    }

    private void buscarObjeto() {
        System.out.print("Nombre del objeto que quieres buscar: ");

        String nombre = teclado.nextLine();

        // Aplica el algoritmo de búsqueda lineal.
        Item encontrado = jugador.buscarItem(nombre);

        if (encontrado == null) {
            System.out.println("No tienes ese objeto.");
        } else {
            System.out.println(
                    "Objeto encontrado: " + encontrado.getNombre()
            );
        }
    }

    private void repararBarco() throws AccionInvalidaException {
    jugador.validarReparacion();

    System.out.println("\nReparas el casco con la madera.");
    System.out.println("Colocas la vela y ajustas el timón.");
    System.out.println("Al amanecer, tu barco vuelve a navegar.");

    System.out.println();
    System.out.println("           (\\ __ /)");
    System.out.println("              (UwU)");
    System.out.println("       ＿ノ ヽ ノ＼＿");
    System.out.println("    /　`/ ⌒Ｙ⌒ Ｙ　 \\");
    System.out.println(" ( 　(三ヽ人　 /　 　|");
    System.out.println("|　ﾉ⌒＼ ￣￣ヽ　 ノ");
    System.out.println("ヽ＿＿＿＞､＿＿／");
    System.out.println("          ｜( 王 ﾉ〈");
    System.out.println("           /ﾐ`ー―彡\\");
    System.out.println("          |╰         ╯|");
    System.out.println("          |       /\\       |");
    System.out.println("          |      /  \\      |");
    System.out.println("          |    /     \\     |");
    System.out.println();
    System.out.println("    Lo lograste camarada tu barco esta navegando de nuevo");
    System.out.println("       Has escapado de la isla y regresado a casa con vida y tesoros.");
    System.out.println();

    terminar("VICTORIA: escapaste de la isla");
} 
    private void terminar(String resultado) {
        partidaActiva = false;

        int puntaje = progreso * 100
                + jugador.getInventario().size() * 50;

        System.out.println("\n================================");
        System.out.println(resultado);
        System.out.println("Pirata: " + jugador.getNombre());
        System.out.println("Puntaje final: " + puntaje);
        System.out.println("================================");
    }

    private int leerOpcion(int minimo, int maximo) {
        while (true) {
            System.out.print("> ");

            try {
                int opcion = Integer.parseInt(
                        teclado.nextLine().trim()
                );

                if (opcion < minimo || opcion > maximo) {
                    throw new AccionInvalidaException(
                            "Elige un número entre "
                                    + minimo + " y " + maximo + "."
                    );
                }

                return opcion;

            } catch (NumberFormatException e) {
                System.out.println(
                        "Debes escribir un número entero."
                );

            } catch (AccionInvalidaException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}