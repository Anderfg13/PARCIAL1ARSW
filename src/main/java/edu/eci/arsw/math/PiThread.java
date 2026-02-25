package edu.eci.arsw.math;

import java.util.ArrayList;

class PiThread extends Thread {

    private int start;
    private int end;
    private ArrayList<Integer> digits = new ArrayList<>();

    public static final Object wait = new Object();

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
                synchronized (wait) {
                    try {
                        wait.wait();
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

    public void setStart(int start){
        this.start = start;
    }

    public void setEnd(int end){
        this.end = end;
    }

    public ArrayList<Integer> getDigits(){
        return this.digits;
    }
}
