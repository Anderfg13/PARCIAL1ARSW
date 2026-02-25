/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.math;

import java.util.Arrays;

/**
 * Clase principal para ejecutar el cálculo de los dígitos de PI en paralelo usando hilos.
 * Permite pausar y reanudar el cálculo presionando Enter.
 * Muestra el resultado en hexadecimal.
 * @author hcadavid
 */
public class Main {

    /**
     * Método principal. Lanza el cálculo en un hilo y permite pausar/reanudar con Enter.
     */
    public static void main(String a[]) {
        //System.out.println(bytesToHex(PiDigits.getDigits(0, 10)));
        // Lanzar el cálculo en un hilo aparte para poder leer Enter
        Thread calcThread = new Thread(() -> {
            System.out.println(bytesToHex(PiDigits.getDigits(1, 10000, 4)));
        });
        calcThread.start();

        // Esperar Enter y notificar a los hilos cada vez
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        while (calcThread.isAlive()) {
            scanner.nextLine();
            synchronized (PiThread.bell) {
                PiThread.bell.notifyAll();
            }
        }
        //System.out.println(bytesToHex(PiDigits.getDigits(1, 1000000)));
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Convierte un arreglo de bytes a una cadena hexadecimal.
     * @param bytes Arreglo de bytes con los dígitos de PI.
     * @return Cadena en base 16.
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<hexChars.length;i=i+2){
            //sb.append(hexChars[i]);
            sb.append(hexChars[i+1]);            
        }
        return sb.toString();
    }

}
