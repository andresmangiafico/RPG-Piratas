// NOTA: Error propio del juego. Hereda de Exception; los métodos deben capturarlo o declararlo con throws.
public class AccionInvalidaException extends Exception {
    // NOTA: Constructor de nuestra excepción comprobada. super(mensaje) entrega el texto a Exception; getMessage() permitirá recuperarlo.
    public AccionInvalidaException(String mensaje) {
        // NOTA: Llama al constructor de Exception y conserva el mensaje del error.
        super(mensaje);
    }
}