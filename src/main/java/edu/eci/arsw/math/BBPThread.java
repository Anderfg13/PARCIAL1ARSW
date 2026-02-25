package edu.eci.arsw.math;

import java.util.ArrayList;

class BBPThread extends Thread {

    public static final Object wait = new Object(); // Con esto todos saben cuando esperar
    private ArrayList<Integer> digitsNumber = new ArrayList<>();
    private int begin;
    private int end;

    @Override
    public void run(){
        calc(begin, end);
    }

    private void calc(int begin, int end){
        long lastPause = System.currentTimeMillis();
        int process = 0;
        for (int i = begin; i < end; i++){
            double sum = 4 * PiDigits.sum(1, i)- 2 * PiDigits.sum(4, i)- PiDigits.sum(5, i)- PiDigits.sum(6, i);
            sum = sum - Math.floor(sum);
            digitsNumber.add((int)(16 * sum));
            process++;

            if (System.currentTimeMillis() - lastPause >= 5000) { 
                System.out.println("Hilo " + this.getName() + " ha procesado " + process + " dígitos.");
                synchronized (wait) {
                    try {
                        wait.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Ha ocurrido un error al pausar el hilo " + this.getName(), e);
                    }
                }
                lastPause = System.currentTimeMillis();
            }
        }
        
        if (process > 0 && System.currentTimeMillis() - lastPause < 5000) {
            System.out.println("Hilo " + this.getName() + " ha procesado " + process + " dígitos.");
        }
    }

    public void setBegin(int begin){
        this.begin = begin;
    }

    public void setEnd(int end){
        this.end = end;
    }

    public ArrayList<Integer> getDigits(){
        return this.digitsNumber;
    }
}
