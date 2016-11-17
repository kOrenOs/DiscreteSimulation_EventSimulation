/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simCoreAbstraction;

/**
 *
 * @author korenciak.marek
 */
public class RefreshEvent extends AbstrEvent {

    private int delay;
    private AbstrSimCore core;

    public RefreshEvent(double paEventDurationTime, AbstrSimCore paCore, int paDelay) {
        super(paEventDurationTime);
        delay = paDelay;
        core = paCore;
    }

    @Override
    public void execute() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }
        if(core.getObserver()){
            core.addEvent(new RefreshEvent(core.getRepeteTime(), core, core.getDelayTime()));
        }
        core.simulationDelayRun();
    }
}
