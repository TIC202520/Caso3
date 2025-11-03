package actores;

import colas.Cuarentena;
import colas.BuzonEntrega;

import java.util.List;

public class ManejadorCuarentena extends Thread {

    private final Cuarentena cuarentena;
    private final BuzonEntrega buzonEntrega;
    private int contadorProcesados = 0;

    public ManejadorCuarentena(Cuarentena cuarentena, BuzonEntrega buzonEntrega) {
        super("ManejadorCuarentena");
        this.cuarentena = cuarentena;
        this.buzonEntrega = buzonEntrega;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000); // Paso de 1 segundo
                List<String> correosListos = cuarentena.tickYExtraerListos();

                for (String correo : correosListos) {
                    contadorProcesados++;
                    boolean descartar = (contadorProcesados <= 21) && (contadorProcesados % 7 == 0);
                    if (!descartar) {
                        buzonEntrega.guardarCorreo(correo);
                    }
                }
                if (cuarentena.finMarcado() && cuarentena.estaVacia()) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

