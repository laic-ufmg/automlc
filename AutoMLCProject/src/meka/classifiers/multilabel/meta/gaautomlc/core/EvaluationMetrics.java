/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meka.classifiers.multilabel.meta.gaautomlc.core;

import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 *
 * @author luiz
 */
public class EvaluationMetrics {
    protected double m_FWeighted;
    protected double m_precisionWeighted;
    protected double m_recallWeighted;
    protected double m_accuracy;
    protected double m_kappa;
    protected String m_datasetName;
    protected double m_fitGisele;

    public EvaluationMetrics(double m_FWeighted, double m_precisionWeighted, double m_recallWeighted, double m_accuracy, double m_kappa, String m_datasetName, Instances training, Instances validation) {
        this.m_FWeighted = m_FWeighted;
        this.m_precisionWeighted = m_precisionWeighted;
        this.m_recallWeighted = m_recallWeighted;
        this.m_accuracy = m_accuracy;
        this.m_kappa = m_kappa;
        this.m_datasetName = m_datasetName;
        setFitGisele(training, validation);
    }
    
    public EvaluationMetrics(Evaluation eval, Instances training, Instances validation){
        this(eval.weightedFMeasure(), eval.weightedPrecision(), eval.weightedRecall(), eval.pctCorrect(), eval.kappa(), eval.toString(), training, validation);
    }
    
    public EvaluationMetrics() {
        this.m_FWeighted = 0;
        this.m_precisionWeighted = 0;
        this.m_recallWeighted = 0;
        this.m_accuracy = 0;
        this.m_kappa = 0;
        this.m_datasetName = "";
        this.m_fitGisele = 0;
    }
    
    private void setFitGisele(Instances training, Instances validation){
        int[] intancesPerClass = training.attributeStats(training.classIndex()).nominalCounts;
        int majorityClass = 0;
        for(int i = 1; i < intancesPerClass.length; i++){
            if(intancesPerClass[i] > intancesPerClass[majorityClass]){
                majorityClass = i;
            } 
        }
        int tp = 0;
        for(int i = 0; i < validation.numInstances(); i++){
            if(validation.instance(i).toDoubleArray()[validation.classIndex()] == majorityClass){
                tp++;
            }
        }
        double defAccuracy = tp/(double)validation.numInstances();
        if(m_accuracy > defAccuracy){
            m_fitGisele = (double)(m_accuracy - defAccuracy)/(1-defAccuracy);
        }
        else{
            m_fitGisele = (double)(m_accuracy - defAccuracy)/defAccuracy;
        }
    }
      
    public void addEvaluationMetrics(EvaluationMetrics eval){
        this.m_FWeighted += eval.m_FWeighted;
        this.m_accuracy += eval.m_accuracy;
        this.m_precisionWeighted += eval.m_precisionWeighted;
        this.m_recallWeighted += eval.m_recallWeighted;
        this.m_kappa += eval.m_kappa;
        this.m_fitGisele += eval.m_fitGisele;
    }
    
    public void setThisAsMean(int numberOfDatasets){
        this.m_FWeighted /= numberOfDatasets;
        this.m_accuracy /= numberOfDatasets;
        this.m_precisionWeighted /= numberOfDatasets;
        this.m_recallWeighted /= numberOfDatasets;
        this.m_kappa /= numberOfDatasets;
        this.m_fitGisele /= numberOfDatasets;
    }

    public double getFWeighted() {
        return m_FWeighted;
    }

    public void setM_FWeighted(double m_FWeighted) {
        this.m_FWeighted = m_FWeighted;
    }

    public double getPrecisionWeighted() {
        return m_precisionWeighted;
    }

    public void setM_precisionWeighted(double m_precisionWeighted) {
        this.m_precisionWeighted = m_precisionWeighted;
    }

    public double getRecallWeighted() {
        return m_recallWeighted;
    }

    public void setM_recallWeighted(double m_recallWeighted) {
        this.m_recallWeighted = m_recallWeighted;
    }

    public double getAccuracy() {
        return m_accuracy;
    }

    public void setM_accuracy(double m_accuracy) {
        this.m_accuracy = m_accuracy;
    }

    public double getKappa() {
        return m_kappa;
    }

    public void setM_kappa(double m_kappa) {
        this.m_kappa = m_kappa;
    }

    public String getM_datasetName() {
        return m_datasetName;
    }

    public void setM_datasetName(String m_datasetName) {
        this.m_datasetName = m_datasetName;
    }

    public double getFitGisele() {
        return m_fitGisele;
    }

    public void setM_fitGisele(double m_fitGisele) {
        this.m_fitGisele = m_fitGisele;
    }
    
    
}
