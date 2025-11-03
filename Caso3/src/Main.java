import colas.BuzonEntrada;
import colas.BuzonEntrega;
import colas.Cuarentena;

import actores.Clientes;
import actores.FiltroSpam;
import actores.ManejadorCuarentena;
import actores.ServidorEntrega;

public class Main {
    public static void main(String[] args) throws Exception {
        final int numMensajes   = 100; // por cliente
        final int numClientes   = 10;
        final int numFiltros    = 5;
        final int numManejadores= 1;   
        final int numServidores = 3;   

        final int capEntrada = 20;
        final int capEntrega = 20;

        // Buzones y cuarentena
        BuzonEntrada  buzonEntrada = new BuzonEntrada(capEntrada);
        BuzonEntrega  buzonEntrega = new BuzonEntrega(capEntrega, numServidores);
        Cuarentena    cuarentena   = new Cuarentena();

        // Actores 
        Thread[] clientes  = new Thread[numClientes];
        Thread[] filtros   = new Thread[numFiltros];
        Thread[] servidores= new Thread[numServidores];

        // Clientes 
        for (int i = 0; i < numClientes; i++) {
            clientes[i] = new Clientes(buzonEntrada, numMensajes);
        }

        // Filtros 
        for (int i = 0; i < numFiltros; i++) {
            filtros[i] = new FiltroSpam(buzonEntrada, buzonEntrega, cuarentena, numClientes, "Filtro-" + (i + 1));
        }

        // Manejador de Cuarentena 
        Thread manejador = new ManejadorCuarentena(cuarentena, buzonEntrega);

        // Servidores 
        for (int i = 0; i < numServidores; i++) {
            servidores[i] = new ServidorEntrega("Servidor-" + (i + 1), buzonEntrega);
        }
        // Iniciar 
        manejador.start();
        for (Thread t : clientes)   t.start();
        for (Thread t : filtros)    t.start();
        for (Thread t : servidores) t.start();

        // Esperar fin
        for (Thread t : clientes)   t.join();
        for (Thread t : filtros)    t.join();
        manejador.join();
        for (Thread t : servidores) t.join();

        System.out.println("[Main] Terminado.");
    }
}

