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
public abstract class AbstrSimCore {

    private double simTime = 0;
    private double maxSimTime = 0;
    private int configurationCount = 0;
    private int actualConfigurationNumber = 0;

    private EventList eventListInstance;
    private AbstrEvent lastExecutedEvent;

    private double repeteTime = 10;
    private int delayTime = 300;
    private boolean observerMode = false;
    private boolean paused = false;
    private boolean finished = false;

    public AbstrSimCore() {
        eventListInstance = new EventList();
    }

    public final synchronized void addEvent(AbstrEvent paEvent) {
        paEvent.setActualSimTime(simTime);
        eventListInstance.addEvent(simTime, paEvent);
    }

    public final void simulate(double paMaxSimTime, int paReplicationCount, int paConfigurationNumber) {
        simulationRun(paMaxSimTime, paReplicationCount, paConfigurationNumber, initStartSimulationEvent());
    }

    private final void simulationRun(double paMaxSimTime, int paReplicationCount, int paConfigurationNumber, AbstrEvent startEvent) {
        AbstrEvent actualEvent = null;
        maxSimTime = paMaxSimTime;
        finished = false;

        actualConfigurationNumber = paConfigurationNumber;

        preSimulationRun();
        preConfigurationChangedRun();

        for (int i = 0; i < paReplicationCount && !finished; i++) {
            simTime = 0;
            eventListInstance = new EventList();
            lastExecutedEvent = null;

            preReplicationRun();
            addStartEvent(startEvent);

            if (observerMode) {
                addEvent(new RefreshEvent(0, this, delayTime));
            }

            while (!eventListInstance.emptyList() && !finished && customFinishCondition()) {
                while (paused) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                    }
                }
                actualEvent = eventListInstance.getNextEvent();
                simTime = actualEvent.getEventActionTime();
                if (simTime > maxSimTime) {
                    simTime = maxSimTime;
                    break;
                }
                simBeforeEvent();
                actualEvent.execute();
                lastExecutedEvent = actualEvent;
                simAfterEvent();
            }
            postReplicationRun();
        }
        postConfigurationChangedRun();
        postSimulationRun();
    }

    protected final int getConfigurationCount() {
        return configurationCount;
    }

    protected final int getActualConfigurationNumber() {
        return actualConfigurationNumber;
    }

    public final double getMaxSimTime() {
        return maxSimTime;
    }

    public final double getSimTime() {
        return simTime;
    }

    public final double getRepeteTime() {
        return repeteTime;
    }

    public final int getDelayTime() {
        return delayTime;
    }

    public final void setPause(boolean paPaused) {
        paused = paPaused;
    }

    public final synchronized void setObserver(boolean paObserver) {
        observerMode = paObserver;

        if (paObserver) {
            addEvent(new RefreshEvent(repeteTime, this, delayTime));
        }
    }

    public final void setRepeteTime(double paRepeteTime) {
        if (paRepeteTime < 1) {
            paRepeteTime = 1;
        }
        repeteTime = paRepeteTime;
    }

    public final void setDelayTime(int paDelay) {
        if (paDelay < 1) {
            paDelay = 1;
        }
        delayTime = paDelay;
    }

    public final void endSimulation() {
        finished = true;
    }

    public final boolean getPaused() {
        return paused;
    }

    public final boolean getObserver() {
        return observerMode;
    }

    protected final AbstrEvent getLastExecutedEvent() {
        return lastExecutedEvent;
    }

    protected final void addStartEvent(AbstrEvent paEvent) {
        addEvent(paEvent);
    }

    protected AbstrEvent initStartSimulationEvent() {
        return null;
    }

    protected void simBeforeEvent() {
    }

    protected void simAfterEvent() {
    }

    protected void preReplicationRun() {
    }

    protected void postReplicationRun() {
    }

    protected void preSimulationRun() {
    }

    protected void postSimulationRun() {
    }

    protected boolean customFinishCondition() {
        return true;
    }

    protected void preConfigurationChangedRun() {
    }

    protected void postConfigurationChangedRun() {
    }

    public void simulationDelayRun() {
    }
}
