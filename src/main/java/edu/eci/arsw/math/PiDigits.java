package edu.eci.arsw.math;

import java.util.ArrayList;

/**
 * Implementación de la fórmula Bailey-Borwein-Plouffe (BBP) para calcular dígitos hexadecimales de PI.
 * Permite paralelizar el cálculo usando múltiples hilos.
 * https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
 * Basado en: https://github.com/mmoroney/DigitsOfPi
 */
public class PiDigits {

    private static double Epsilon = 1e-17;
    private static ArrayList<BBPThread> threads = new ArrayList<>();

    
    public static byte[] getDigits(int start, int count, int N) {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        int digitsPerThread = count / N; //Divido por partes iguales
        int remainder = count % N; //Lo que el profe decía si era impar
        int currentStart = start;
        for (int i = 0; i < N; i++){
            int threadDigits = digitsPerThread + (i < remainder ? 1 : 0); //Por si sobran uno o mas
            BBPThread thread = new BBPThread();
            thread.setBegin(currentStart); //Creo los hilos y les asigno el inicio del rango
            thread.setEnd(currentStart + threadDigits); //Creo los hilos y les asigno el final del rango
            threads.add(thread); //Creo el nuevo hilo al array de hilos
            thread.start(); //Inicial el hilo
            currentStart += threadDigits;
        }
        
        for (BBPThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        byte[] digits = new byte[count];
        int pos = 0;
        for (BBPThread thread : threads) {
            ArrayList<Integer> workerDigits = thread.getDigits();
            for (int j = 0; j < workerDigits.size() && pos < count; j++) {
                digits[pos] = workerDigits.get(j).byteValue();
                pos++;
            }
        }
        return digits;
    }



    public static double sum(int m, int n) {
        double sum = 0;
        int d = m;
        int power = n;

        while (true) {
            double term;

            if (power > 0) {
                term = (double) hexExponentModulo(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < Epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            d += 8;
        }

        return sum;
    }
    
    private static int hexExponentModulo(int p, int m) {
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }

        int result = 1;

        while (power > 0) {
            if (p >= power) {
                result *= 16;
                result %= m;
                p -= power;
            }

            power /= 2;

            if (power > 0) {
                result *= result;
                result %= m;
            }
        }

        return result;
    }

}
