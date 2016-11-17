/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Random;

import java.util.Random;

/**
 *
 * @author korenciak.marek
 */
public class ExponentialDistribution implements DistributionInterface{

    private Random randomInstance = null;
    private double lambda = 0;
    
    public ExponentialDistribution(double paLambda, SeedGenerator paGenerator) {
        randomInstance = new Random();
        lambda = paLambda;
    }
    
    @Override
    public int nextInt() {
        return (int)(-lambda * Math.log(randomInstance.nextDouble()));
    }

    @Override
    public double nextDouble() {
        return -lambda * Math.log(randomInstance.nextDouble());
        //return -(Math.log(nahodnaHodnota.nextDouble())/lambda);
        //return Math.log(1-nahodnaHodnota.nextDouble())/(-lambda);
    }
    
}
