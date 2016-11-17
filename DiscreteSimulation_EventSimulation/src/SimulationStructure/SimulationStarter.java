/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimulationStructure;

import Graphics.ISimGraphics;
import java.util.ArrayList;

/**
 *
 * @author korenciak.marek
 */
public class SimulationStarter implements Runnable{

    private boolean simulationRun = false;

    private SimCore simulationInstance;
    
    private double maxSimTime;
    private int replicationCount;
    private int configurationNumber;

    public SimulationStarter() {
        simulationInstance = new SimCore();
    }

    public void addGUI(ISimGraphics GUI) {
        simulationInstance.addGUI(GUI);
    }

    public void simulationPause() {
        simulationInstance.setPause(true);
    }

    public void simulationUnpause() {
        simulationInstance.setPause(false);
    }

    public void simulationStop() {
        simulationInstance.endSimulation();
        simulationRun = false;
    }

    public void simulationTurnOnObserverMode() {
        simulationInstance.setObserver(true);
    }

    public void simulationTurnOffObserverMode() {
        simulationInstance.setObserver(false);
    }

    public final void setRepeteTime(double paRepeteTime) {
        simulationInstance.setRepeteTime(paRepeteTime);
    }

    public final void setDelayTime(int paDelay) {
        simulationInstance.setDelayTime(paDelay);
    }

    public void simulationRun(double paMaxSimTime, int paReplicationCount, int paConfigurationNumber) {
        simulationRun = true;
        simulationInstance.reset();
        simulationInstance.simulate(paMaxSimTime, paReplicationCount, paConfigurationNumber);
        simulationRun = false;
    }
    
    public boolean simulationRunning(){
        return simulationRun;
    }
    
    public void setMaxSimTime(double paMaxsimtime){
        maxSimTime = paMaxsimtime;
    }
    
    public void setReplicationCount(int paReplicationCount){
        replicationCount = paReplicationCount;
    }
    
    public void setConfigurationNumber(int paConfigurationNumber){
        configurationNumber = paConfigurationNumber;
    }
    
    public ArrayList<Double> getDataset(){
        return simulationInstance.getDataSet();
    }

    @Override
    public void run() {
        simulationRun(maxSimTime, replicationCount, configurationNumber);
    }
}
