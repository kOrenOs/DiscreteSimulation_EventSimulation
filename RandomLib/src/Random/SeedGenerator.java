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
public class SeedGenerator {
    
    private Random seedRandom;

    public SeedGenerator(long seed) {
        seedRandom = new Random(seed);
    }

    public SeedGenerator() {
        seedRandom = new Random();
    }
    
    public long getSeed(){
        return seedRandom.nextLong();
    }
}
