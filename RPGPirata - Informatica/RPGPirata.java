// NOTA: Controlador del juego: coordina menús, historia, turnos y finales usando los modelos.
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class RPGPirata {

    // NOTA: Lee lo que escribes en la terminal. final impide reemplazar esta referencia después del constructor.
    private final Scanner teclado;
    // NOTA: Genera variaciones de daño. nextInt(6) produce 0, 1, 2, 3, 4 o 5.
    private final Random azar;

    // NOTA: Lista dinámica con el orden de los combates. El primer índice es 0.
    private final ArrayList<Enemigo> enemigos = new ArrayList<>();

    // NOTA: Referencia al jugador de la partida actual; se crea en nuevaPartida().
    private Personaje jugador;
    // NOTA: Cuenta victorias y también indica el índice del próximo enemigo.
    private int progreso;
    // NOTA: Bandera: true mantiene el campamento abierto; false termina la partida.
    private boolean partidaActiva;

    // NOTA: Constructor: recibe Scanner y Random y guarda sus referencias. Permite separar la lectura y el azar de las reglas.
    public RPGPirata(Scanner teclado, Random azar) {
        this.teclado = teclado;
        this.azar = azar;
    }

    // NOTA: Prepara los recursos y abre el menú principal. try con recursos cierra el Scanner al salir. El catch maneja el cierre de la entrada.
    public static void iniciar() {
        try (Scanner teclado = new Scanner(System.in)) {
            RPGPirata juego = new RPGPirata(teclado, new Random());
            juego.mostrarMenuPrincipal();

        } catch (NoSuchElementException e) {
            System.out.println("\nEntrada cerrada. Juego terminado.");
        }
    }

    // NOTA: Bucle exterior: iniciar otra partida o salir. return sale del método; terminar una partida permite volver a este menú.
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

    // NOTA: Restablece al jugador, progreso y enemigos. AQUÍ puedes editar nombres y estadísticas enemigas, además de la introducción.
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

    // NOTA: Bucle del campamento. switch elige la acción; break sale del switch, no de toda la partida. El catch informa de acciones inválidas y permite continuar.
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

    // NOTA: Selecciona enemigo por progreso, crea una copia para combatir y recompensa solo si combatir() devuelve true. Huir reinicia la vida enemiga para el siguiente intento.
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

        // NOTA: Obtiene el enemigo de la posición actual. El límite de la lista se comprobó antes.
        Enemigo modelo = enemigos.get(progreso);

        // Cada intento tiene un enemigo con la vida completa.
        Enemigo enemigo = new Enemigo(
                modelo.getNombre(),
                modelo.getNivel(),
                modelo.getVida(),
                modelo.getAtaque(),
                modelo.getDefensa()
        );

        // NOTA: Solo entra si el combate devolvió true, es decir, victoria.
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

    // NOTA: Añade Madera tras la victoria 2, Vela tras la 4 y Timón tras la 6. null significa que este combate no entrega pieza.
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

    // NOTA: Combate por turnos. Devuelve true al ganar y false al huir o perder. Controla ataque, habilidad, defensa, recarga, vida y dibujo de derrota.
    private boolean combatir(Enemigo enemigo) {
        // NOTA: Habilidad lista al comenzar cada combate. Es variable local, no atributo permanente.
        int enfriamiento = 0;

        System.out.println(
                "\nAparece " + enemigo.getNombre()
                        + " — Nivel " + enemigo.getNivel()
        );

        // NOTA: && exige que ambos sigan vivos para comenzar otro turno.
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
                // NOTA: Vuelve al inicio del bucle: la acción inválida no permite atacar al enemigo ni consume recarga.
                continue;
            }

            // NOTA: Guarda true si el jugador eligió defenderse en este turno.
            boolean defendiendo = opcion == 3;

            if (defendiendo) {
                System.out.println("Preparas tu defensa.");

            } else {
                // NOTA: Ataque base más azar entre 0 y 5; todavía falta restar la defensa enemiga.
                int dano = jugador.getAtaque() + azar.nextInt(6);

                if (opcion == 2) {
                    // NOTA: Bonificación del Corte del corsario; también crece con el nivel.
                    dano += 10 + jugador.getNivel() * 4;
                    // NOTA: PERSONALIZAR: acciones válidas necesarias para recuperar la habilidad después de usarla.
                    enfriamiento = 3;

                    System.out.println("¡Usas Corte del corsario!");
                }

                // NOTA: La defensa reduce daño, pero un ataque siempre causa al menos 1.
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
                // NOTA: Defender reduce el daño a la mitad con división entera, mínimo 1.
                danoRecibido = Math.max(1, danoRecibido / 2);
            }

            jugador.recibirDano(danoRecibido);

            System.out.println(
                    "Recibes " + danoRecibido + " puntos de daño."
            );

            // La habilidad se recarga realizando otras tres acciones.
            if (enfriamiento > 0 && opcion != 2) {
                // NOTA: Resta un turno pendiente de recarga. Solo ocurre bajo la condición que lo rodea.
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

    // NOTA: Menú de compra: muestra precios y solicita a Personaje que realice el cobro y la mejora. Si cambias precios, ajusta también Personaje.mejorarEquipo().
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

        // NOTA: La comparación produce un boolean: opción 1 significa mejorar espada.
        jugador.mejorarEquipo(opcion == 1);

        System.out.println("¡Equipo mejorado!");
    }

    // NOTA: Consulta getters para imprimir el estado. Los textos no modifican los atributos. Si cambias reglas, revisa los valores mostrados.
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

    // NOTA: Comprueba si está vacío y recorre cada Item para imprimir su nombre.
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

    // NOTA: Solicita un nombre y usa Personaje.buscarItem(). Interpreta null como objeto no encontrado.
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

    // NOTA: Primero valida las piezas. Si falla, la excepción impide imprimir el dibujo de victoria. Si pasa, muestra tu dibujo y termina la partida.
    private void repararBarco() throws AccionInvalidaException {
    // NOTA: Si lanza una excepción, se salta el resto de repararBarco() y la captura jugar().
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
    // NOTA: Final común para victoria, derrota y abandono. Desactiva el campamento e imprime puntaje. No cierra el menú principal.
    private void terminar(String resultado) {
        // NOTA: Hace que el while de jugar() deje de repetirse al volver a comprobar la condición.
        partidaActiva = false;

        // NOTA: Puntaje: 100 por enemigo vencido + 50 por pieza. Máximo actual: 750.
        int puntaje = progreso * 100
                + jugador.getInventario().size() * 50;

        System.out.println("\n================================");
        System.out.println(resultado);
        System.out.println("Pirata: " + jugador.getNombre());
        System.out.println("Puntaje final: " + puntaje);
        System.out.println("================================");
    }

    // NOTA: Repite la lectura hasta recibir un entero dentro del rango inclusivo. Captura texto no numérico y opciones inválidas sin cerrar el juego.
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
