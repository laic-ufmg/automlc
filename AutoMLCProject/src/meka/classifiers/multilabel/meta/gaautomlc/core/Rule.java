/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meka.classifiers.multilabel.meta.gaautomlc.core;

/**
 * Rules from the grammar
 * @author alexgcsa
 */
public class Rule {
    
    private String metricName;
    private String componentName;
    private String completeRule;
    private double minValue;
    private double maxValue;

    public Rule(String metricName, String componentName, double minValue, double maxValue, String completeRule) {
        this.metricName = metricName;
        this.componentName = componentName;
        this.completeRule = completeRule;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public String getCompleteRule() {
        return completeRule;
    }

    public String getMetricName() {
        return metricName;
    }

    public String getComponentName() {
        return componentName;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }
    
    
    
}
