/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meka.classifiers.multilabel.meta.automekaggp.core;

/**
 * Abstract class for translate indiviual
 * @author alexgcsa
 */
public abstract class AbstractTranslateIndividual {
    
    public String translate2Command(String trainingSet, String validationSet, int timeoutLimit, boolean completeCommand){
        return "";
    }
    
    
}
