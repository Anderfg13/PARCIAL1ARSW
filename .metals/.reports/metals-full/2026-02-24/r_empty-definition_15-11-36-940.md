error id: file:///D:/ander/Documents/SEMESTRE%207/ARSW/ESTUDIAR%20PARCIAL%201/Parcial1/src/main/java/edu/eci/arsw/math/PiDigits.java:_empty_/PiThread#getDigits#
file:///D:/ander/Documents/SEMESTRE%207/ARSW/ESTUDIAR%20PARCIAL%201/Parcial1/src/main/java/edu/eci/arsw/math/PiDigits.java
empty definition using pc, found symbol in pc: _empty_/PiThread#getDigits#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 1792
uri: file:///D:/ander/Documents/SEMESTRE%207/ARSW/ESTUDIAR%20PARCIAL%201/Parcial1/src/main/java/edu/eci/arsw/math/PiDigits.java
text:
```scala
package edu.eci.arsw.math;

import java.util.ArrayList;

///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;
    private static ArrayList<PiThread> threads = new ArrayList<>();

    
    /**
     * Returns a range of hexadecimal digits of pi.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int start, int count, int N) {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        for (int i = 0; i < N; i++){
            PiThread thread = new PiThread();
            thread.setStart(start + i*DigitsPerSum);
            thread.setEnd(start + (i+1)*DigitsPerSum);
            threads.add(thread);
            thread.start();
        }
        
        for (int i = 0; i < N; i++){
            try {
                threads.get(i).join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        byte[] digits = new byte[count];
        double sum = 0;

        for (int i = 0; i < count; i++) {
            
            for (int j = 0; j < threads.get(i).getDigits().size();j++){
                sum += threads.get(j).getD@@igits().get(i);
            }
           
            digits[i] = (byte) (sum % 16);
             sum = 0;

        }

        return digits;
    }




    /// <summary>
    /// Returns the sum of 16^(n - k)/(8 * k + m) from 0 to k.
    /// </summary>
    /// <param name="m"></param>
    /// <param name="n"></param>
    /// <returns></returns>
    private static double sum(int m, int n) {
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

    /// <summary>
    /// Return 16^p mod m.
    /// </summary>
    /// <param name="p"></param>
    /// <param name="m"></param>
    /// <returns></returns>
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

```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/PiThread#getDigits#