package edu.eci.arsw.math;

import java.util.ArrayList;

/**
 * Hilo que calcula una parte de los dígitos de PI en paralelo.
 * Se detiene cada 5 segundos para reportar progreso y espera la señal del jefe para continuar.
 */
class PiThread extends Thread {

    private int start;
    private int end;
    private ArrayList<Integer> digits = new ArrayList<>();
    /**
     * Objeto compartido para sincronización de pausas y reanudación entre hilos.
     */
    public static final Object bell = new Object();

    @Override
    public void run(){
        calculate(start, end);

        
    }

    private void calculate(int start, int end){
        long lastPause = System.currentTimeMillis();
        int processed = 0;
        for (int i = start; i < end; i++){
            double sum = 4 * PiDigits.sum(1, i)
                        - 2 * PiDigits.sum(4, i)
                        - PiDigits.sum(5, i)
                        - PiDigits.sum(6, i);
            sum = sum - Math.floor(sum);
            digits.add((int)(16 * sum));
            processed++;

            if (System.currentTimeMillis() - lastPause >= 5000) {
                System.out.println("Hilo " + this.getName() + " ha procesado " + processed + " dígitos.");
                synchronized (bell) {
                    try {
                        bell.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                lastPause = System.currentTimeMillis();
            }
        }
        
        if (processed > 0 && System.currentTimeMillis() - lastPause < 5000) {
            System.out.println("Hilo " + this.getName() + " ha procesado " + processed + " dígitos.");
        }
    }

    /**
     * Define el dígito inicial que debe calcular este hilo.
     * @param start Índice inicial (incluido).
     */
    public void setStart(int start){
        this.start = start;
    }

    /**
     * Define el dígito final (no incluido) que debe calcular este hilo.
     * @param end Índice final (no incluido).
     */
    public void setEnd(int end){
        this.end = end;
    }

    /**
     * Devuelve la lista de dígitos calculados por este hilo.
     * @return Lista de enteros (0-15) con los dígitos hexadecimales.
     */
    public ArrayList<Integer> getDigits(){
        return this.digits;
    }
}
