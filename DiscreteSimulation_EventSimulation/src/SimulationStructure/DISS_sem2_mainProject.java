/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimulationStructure;

import SimulationEvents.StartSimulationEvent;

/**
 *
 * @author korenciak.marek
 */
public class DISS_sem2_mainProject {

    /**
     * @param args the command line arguments
     */
    public DISS_sem2_mainProject(String[] args) {
        SimulationStarter starter = new SimulationStarter();
        starter.setDelayTime(1);
        starter.setRepeteTime(10);
        starter.simulationTurnOnObserverMode();
        Thread vlakno = new Thread(new Runnable() {

            @Override
            public void run() {
                starter.simulationRun(Double.MAX_VALUE, 100, 0);
            }
        });
        vlakno.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        starter.simulationPause();
        System.out.println("pauznute");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        starter.simulationUnpause();
        System.out.println("odpauzovane");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        starter.simulationStop();
        System.out.println("stopnute");
    }
}
