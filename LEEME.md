# Diagrama UML del juego RPG Pirata

Esta carpeta contiene una propuesta de rediseño orientado a objetos del juego, con clases padre, clases hijas y herencia. El programa original contiene RPGPirata y su clase anidada Enemigo; las herencias propuestas aún no están implementadas. No se modificó el juego original.

## Archivos

- `diagrama.mmd`: diagrama editable en formato Mermaid.
- `diagrama.md`: el mismo diagrama para visualizar en un visor Markdown compatible con Mermaid.
- `LEEME.md`: explicación del diseño.

## Clases padre e hijas

- **Personaje** es una clase abstracta. **Pirata** y **Enemigo** heredan sus atributos y métodos comunes de combate.
- **Escenario** es una clase abstracta. **Campamento** y **ZonaExploracion** heredan nombre y descripción, e implementan el método abstracto entrar según las acciones de cada lugar.
- **RPGPirata** coordina el jugador, el campamento, las zonas y el barco.
- **Barco** registra las piezas recuperadas y permite reparar el barco.

La playa, la selva, las ruinas, la fortaleza y el puerto se representan como instancias de ZonaExploracion. Las seis zonas corresponden a los seis encuentros del juego; dos encuentros pueden compartir ubicación. Los distintos enemigos también son instancias de Enemigo.

## Notación

- `+`: público.
- `-`: privado.
- `#`: protegido, accesible desde clases hijas.
- `abstract`: clase que no se instancia directamente.
- Método en cursiva: método abstracto.
- Flecha con triángulo vacío: herencia; apunta hacia la clase padre.
- Flecha continua: asociación entre objetos.
- Flecha discontinua: dependencia o uso.
- `1` y `6`: cantidad de objetos asociados en este diseño.

Los atributos y métodos heredados se muestran únicamente en la clase padre para evitar repetición.
