/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimulationEvents;

import SimulationStructure.Vehicle;
import simCoreAbstraction.AbstrEvent;

/**
 *
 * @author korenciak.marek
 */
public class StartSimulationEvent extends AbstrEvent{
    
    private SimulationStructure.SimCore core;
    
    public StartSimulationEvent(double paEventDurationTime, SimulationStructure.SimCore paCore) {
        super(paEventDurationTime);
        core = paCore;
    }

    @Override
    public void execute() {
        core.initialize();
    }
}
