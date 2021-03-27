package Task_1;

import java.util.concurrent.*;

public class Main {

        private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        private static final ExecutorService executorService = Executors.newCachedThreadPool();
        private static int amountOfMolecules;

        public static void main(String[] args) {
            String inputStr = "HHOOHHHHO";
            DataAnalise dataAnalise = new DataAnalise(inputStr);
            Hydrogen hydrogen = new Hydrogen();
            Oxygen oxygen = new Oxygen();
            for (int i = 0; i < amountOfMolecules; i++) {
                executorService.submit(hydrogen);
                executorService.submit(oxygen);
                executorService.submit(hydrogen);
            }
            executorService.shutdown();
        }

        public static class DataAnalise{
            public DataAnalise(String input){
                char[] chars = input.toCharArray();
                int amountOfOxygen = 0;
                for (char ch:chars) {
                    if (ch == 'O'){
                        amountOfOxygen++;
                    }
                }
                amountOfMolecules = amountOfOxygen;
            }
        }

        public static class Hydrogen implements Runnable{

            Semaphore semaphoreForHydrogen = new Semaphore(2);

            @Override
            public void run() {
                releaseHydrogen();
            }

            private void releaseHydrogen()  {
                try {
                    semaphoreForHydrogen.acquire();
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.out.println(e.getMessage());
                }

                System.out.print("H");
                semaphoreForHydrogen.release();
            }
        }

        public static class Oxygen implements Runnable{

            Semaphore semaphoreForOxygen = new Semaphore(1);

            @Override
            public void run() {
                releaseOxygen();
            }

            private void releaseOxygen(){

                try {
                    semaphoreForOxygen.acquire();
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.out.println(e.getMessage());
                }

                System.out.print("O");
                semaphoreForOxygen.release();
            }
        }
    }
