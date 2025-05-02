
package Persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import Dominio.Excepciones.BolsaNoEncontradaException;
import Dominio.Excepciones.BolsaYaExistenteException;
import Dominio.Excepciones.DiccionarioNoEncontradoException;
import Dominio.Excepciones.DiccionarioYaExistenteException;
import Dominio.Excepciones.NoHayPartidaGuardadaException;
import Dominio.Excepciones.PartidaNoEncontradaException;
import Dominio.Excepciones.PartidaYaExistenteException;
import Dominio.Excepciones.PuntuacionInvalidaException;
import Dominio.Excepciones.RankingVacioException;
import Dominio.Excepciones.UsuarioNoEncontradoException;
import Dominio.Excepciones.UsuarioYaRegistradoException;
import Dominio.Modelos.Jugador;
import Dominio.Modelos.Partida;
import Dominio.Observer;
public class CtrlPersistencia2 implements Observer {
    // ... tus campos y constructores actuales ...

    @Override
    public void onScoreUpdated(String jugador, int nuevosPuntos) {
        // cuando le llegue la notificación, llama a tu método habitual
    
    }
}