import java.util.Random;

import javax.swing.JPanel;

public class Jugador {

    private final int TOTAL_CARTAS = 10;
    private final int MARGEN_SUPERIOR = 10;
    private final int MARGEN_IZQUIERDA = 10;
    private final int DISTANCIA = 40;

    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private Random r = new Random();

   
    private boolean[] enGrupo = new boolean[TOTAL_CARTAS];

  
    public void repartir() {
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        int posicion = MARGEN_IZQUIERDA + DISTANCIA * (TOTAL_CARTAS - 1);
        for (Carta carta : cartas) {
            carta.mostrar(pnl, posicion, MARGEN_SUPERIOR);
            posicion -= DISTANCIA;
        }
        pnl.repaint();
    }

  
    public String getGrupos() {

        enGrupo = new boolean[TOTAL_CARTAS];

        String grupos    = buscarGruposPorValor();
        String escaleras = buscarEscaleras();
        String sobran    = calcularPuntaje();

        if (grupos.isEmpty() && escaleras.isEmpty()) {
            return "No se encontraron grupos.\n" + sobran;
        }

        String resultado = grupos;
        if (!grupos.isEmpty() && !escaleras.isEmpty()) {
            resultado += "\n";
        }
        resultado += escaleras;
        resultado += sobran;

        return resultado;
    }

   
    private String buscarGruposPorValor() {

        
        int[] contadores = new int[NombreCarta.values().length];

        for (int i = 0; i < TOTAL_CARTAS; i++) {
            
            int indiceValor = cartas[i].getNombre().ordinal();
            contadores[indiceValor]++;
        }

        String grupos = ""; 

        for (int indiceValor = 0; indiceValor < contadores.length; indiceValor++) {
            int cantidad = contadores[indiceValor];

            if (cantidad >= 2) {
                NombreCarta valorCarta = NombreCarta.values()[indiceValor];
                grupos += nombreGrupo(cantidad) + " de " + nombreCarta(valorCarta) + "\n";

                for (int i = 0; i < TOTAL_CARTAS; i++) {
                    if (cartas[i].getNombre().ordinal() == indiceValor) {
                        enGrupo[i] = true;
                    }
                }
            }
        }

        return grupos;
    }

    
    private String buscarEscaleras() {

      
        boolean[][] tieneCarta = new boolean[4][13];

        for (int i = 0; i < TOTAL_CARTAS; i++) {
            int indicePinta = cartas[i].getPinta().ordinal();
            int indiceValor = cartas[i].getNombre().ordinal();
            tieneCarta[indicePinta][indiceValor] = true;
        }

        String escaleras = "";

        for (int indicePinta = 0; indicePinta < 4; indicePinta++) {

            int largoSecuencia  = 0; 
            int inicioSecuencia = 0; 

       
            for (int indiceValor = 0; indiceValor <= 13; indiceValor++) {

                boolean hayCartaAqui = (indiceValor < 13) && tieneCarta[indicePinta][indiceValor];

                if (hayCartaAqui) {
                    if (largoSecuencia == 0) {
                        inicioSecuencia = indiceValor; 
                    }
                    largoSecuencia++;

                } else if (largoSecuencia >= 2) {
                    int finSecuencia = inicioSecuencia + largoSecuencia - 1;

                    NombreCarta cartaInicio = NombreCarta.values()[inicioSecuencia];
                    NombreCarta cartaFin    = NombreCarta.values()[finSecuencia];
                    Pinta pinta             = Pinta.values()[indicePinta];

                    escaleras += nombreGrupo(largoSecuencia) + " de "
                            + nombrePinta(pinta) + " de "
                            + nombreCarta(cartaInicio) + " a "
                            + nombreCarta(cartaFin) + "\n";

                    for (int i = 0; i < TOTAL_CARTAS; i++) {
                        if (cartas[i].getPinta().ordinal() == indicePinta) {
                            int v = cartas[i].getNombre().ordinal();
                            if (v >= inicioSecuencia && v <= finSecuencia) {
                                enGrupo[i] = true;
                            }
                        }
                    }

                    largoSecuencia = 0;

                } else {
                    largoSecuencia = 0;
                }
            }
        }

        return escaleras;
    }

    
    private String calcularPuntaje() {

        int puntaje = 0;
        String listaSobran = "";

        for (int i = 0; i < TOTAL_CARTAS; i++) {
            if (!enGrupo[i]) {
                int valor = getValor(cartas[i]);
                puntaje  += valor;
                listaSobran += nombreCarta(cartas[i].getNombre())
                        + " de " + nombrePinta(cartas[i].getPinta()) + "\n";
            }
        }

        if (listaSobran.isEmpty()) {
            return "\nTodas las cartas forman grupos.\nPuntos:\n0";
        }

        return "\nSobran:\n" + listaSobran + "\nPuntos:\n" + puntaje;
    }

    // Métodos auxiliares para mostrar nombres de forma legible

    // Convierte la cantidad de cartas al nombre del grupo.
    // Ejemplo: 2 → "Par", 3 → "Terna", 4 → "Cuarta"
    private String nombreGrupo(int cantidad) {
        if (cantidad == 2)      return "Par";
        else if (cantidad == 3) return "Terna";
        else if (cantidad == 4) return "Cuarta";
        else if (cantidad == 5) return "Quinta";
        else if (cantidad == 6) return "Sexta";
        else if (cantidad == 7) return "Séptima";
        else if (cantidad == 8) return "Octava";
        else if (cantidad == 9) return "Novena";
        else                    return "Décima";
    }

    // Convierte un valor de NombreCarta.java al texto corto para mostrar.
    // Ejemplo: AS → "As", DIEZ → "10", JACK → "J", QUEEN → "Q", KING → "K"
    private String nombreCarta(NombreCarta nombre) {
        if (nombre == NombreCarta.AS)          return "As";
        else if (nombre == NombreCarta.DOS)    return "2";
        else if (nombre == NombreCarta.TRES)   return "3";
        else if (nombre == NombreCarta.CUATRO) return "4";
        else if (nombre == NombreCarta.CINCO)  return "5";
        else if (nombre == NombreCarta.SEIS)   return "6";
        else if (nombre == NombreCarta.SIETE)  return "7";
        else if (nombre == NombreCarta.OCHO)   return "8";
        else if (nombre == NombreCarta.NUEVE)  return "9";
        else if (nombre == NombreCarta.DIEZ)   return "10";
        else if (nombre == NombreCarta.JACK)   return "J";
        else if (nombre == NombreCarta.QUEEN)  return "Q";
        else                                   return "K";
    }

    // Convierte un valor de Pinta.java al texto con tildes.
    // Ejemplo: CORAZON → "Corazón", TREBOL → "Trébol"
    private String nombrePinta(Pinta pinta) {
        if (pinta == Pinta.TREBOL)       return "Trébol";
        else if (pinta == Pinta.PICA)    return "Pica";
        else if (pinta == Pinta.CORAZON) return "Corazón";
        else                             return "Diamante";
    }

    // Calcula el valor en puntos de una carta según las reglas del juego.
    // As, J, Q, K = 10 puntos.
    // Para el resto: ordinal() da su posición en NombreCarta.java (DOS=1, TRES=2, ..., DIEZ=9)
    // y sumándole 1 obtenemos el valor real (DOS=2, TRES=3, ..., DIEZ=10).
    private int getValor(Carta carta) {
        NombreCarta nombre = carta.getNombre();

        if (nombre == NombreCarta.AS
                || nombre == NombreCarta.JACK
                || nombre == NombreCarta.QUEEN
                || nombre == NombreCarta.KING) {
            return 10;
        }

        return nombre.ordinal() + 1;
    }
}

