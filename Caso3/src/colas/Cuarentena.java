package colas;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Cuarentena basada en una cola.
 * - Almacena correos SPAM con un TimeToLive en segundos (de 10 a 20).
 */
public class Cuarentena {

    private static int MIN_SEG = 10;
    private static int MAX_SEG = 20;

    private static class MensajeCuarentena {
        String correo;
        int Segundos;
        MensajeCuarentena(String correo, int Segundos) {
            this.correo = correo;
            this.Segundos = Segundos;
        }
    }

    private Queue<MensajeCuarentena> colaCuarentena = new LinkedList<>();
    private Random random = new Random();
    private boolean finMarcado = false;

    public synchronized void guardarCorreo(String correo) {
        int TimeToLive = MIN_SEG + random.nextInt(MAX_SEG - MIN_SEG + 1);
        colaCuarentena.add(new MensajeCuarentena(correo, TimeToLive));
    }

    public synchronized void enviarFin() {
        finMarcado = true;
        notifyAll();
    }

    public synchronized boolean finMarcado() {
        return finMarcado;
    }

    public synchronized boolean estaVacia() {
        return colaCuarentena.isEmpty();
    }

    public synchronized List<String> tickYExtraerListos() {
        for (MensajeCuarentena item : colaCuarentena) {
            item.Segundos -= 1;
        }

        List<String> correosListos = new ArrayList<>();
        List<MensajeCuarentena> aEliminar = new ArrayList<>();
        for (MensajeCuarentena item : colaCuarentena) {
            if (item.Segundos <= 0) {
                correosListos.add(item.correo);
                aEliminar.add(item);
            }
        }
        colaCuarentena.removeAll(aEliminar);

        return correosListos;
    }
}

