package Dominio;
public interface Observer {
    void onScoreUpdated(String jugador, int nuevosPuntos);
    // podrías añadir más métodos, p.ej. onPlayerRegistered(...)
}