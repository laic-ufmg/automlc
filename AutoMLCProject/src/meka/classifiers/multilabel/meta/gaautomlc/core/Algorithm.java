/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meka.classifiers.multilabel.meta.gaautomlc.core;

/**
 *
 * @author alexgcsa
 */
public class Algorithm {
    
    private double fitness;
    private String algorithm;

    public Algorithm(double fitness, String algorithm) {
        this.fitness = fitness;
        this.algorithm = algorithm;
    }

    public double getFitness() {
        return fitness;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
    
    
    
    
}
