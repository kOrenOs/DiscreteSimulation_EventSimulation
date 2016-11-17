/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import java.util.ArrayList;

/**
 *
 * @author korenciak.marek
 */
public interface ISimGraphics {
    public void setSimTime(double simTime);
    public void setProgressA(double actual, double max, String vehicleName, Double actionEnd);
    public void setProgressB(double actual, double max, String vehicleName, Double actionEnd);
    public void setCarInformation(String name, double waitingATotal, double waitingBTotal,
            double waitingAAvg, double waitingBAvg, String status, Double timeOfAction);
    public void setGraphDataset(ArrayList<Double> dataSet);
    
    public void setReplicationNumber(int replicationNumber);
    public void setAvgSimulationTime(double simTime);
    public void setConfidenceIntervalOfSimulationTime(double lowBoundOfInterval, double highBoundOfInterval);
    public void setSumWaitingTimeInRowA(double time);
    public void setSumWaitingTimeInRowB(double time);
    public void setAvgWaitingTimeRowA(double time);
    public void setAvgWaitingTimeRowB(double time);
    public void setAvgRowASize(double rowSize);
    public void setAvgRowBSize(double rowSize);
    public void setMoveVehicles(double moveSize);
}
