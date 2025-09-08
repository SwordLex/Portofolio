/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro Granell Llopis
 */
public class JohnSmithCarsMain {
    //Attributes
    private static final int totalVehicles = 4; 
    private static final Semaphore[] vehiclesNotInTest = new Semaphore[totalVehicles];
    
    public static void main(String[] args) {
        for(int i = 0; i < totalVehicles; i++) {
           vehiclesNotInTest[i] = new Semaphore(1);
        }
        
        String[] nameClients = {"Alfredo", "Sara", "Almudena", "Francisco", "Pablo", "Diana", "Aki", "Gonzalo", "Alvaro"};
        for(String nameClient : nameClients) {
            new Client(nameClient).start();
        }
    }
    static class Client extends Thread {
        //Attributes
        private String nameClient;

        //Constructor
        public Client(String nameC) {
            nameClient = nameC;
        }

        //Methods
        @Override
        public void run() {
            try {
                for(int j = 0; j < totalVehicles; j++) {
                    int vehicleNumber = j;
                    boolean tested = false;
                    while(!tested) {
                        if(vehiclesNotInTest[vehicleNumber].tryAcquire()) {
                            try {
                                System.out.println(nameClient + " is trying the vehicle " + (vehicleNumber + 1));
                                Thread.sleep(2500);
                                System.out.println(nameClient + " has ended trying the vehicle " + (vehicleNumber + 1));
                                tested = true;
                            } finally {
                                vehiclesNotInTest[vehicleNumber].release();
                            }
                        } else {
                            vehicleNumber = (vehicleNumber + 1) % totalVehicles;
                            Thread.sleep(1000);
                        }
                    }
                }

            } catch (InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();
            }
        }
    }
}    
