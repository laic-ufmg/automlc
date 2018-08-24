/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meka.classifiers.multilabel.meta.automekaggp.core;

/**
 * Class that has the structure to save the results -- all multi-label classification metrics in all sets.
 * @author alexgcsa
 */
public class Results {

    private String algorithm;
    private String [] command;

    private double accuracy_FullTraining;
    private double accuracy_Test;
    private double accuracy_Training;
    private double accuracy_Validation;

    private double hammingScore_FullTraining;
    private double hammingScore_Test;
    private double hammingScore_Training;
    private double hammingScore_Validation;

    private double exactMatch_FullTraining;
    private double exactMatch_Test;
    private double exactMatch_Training;
    private double exactMatch_Validation;

    private double jaccardDistance_FullTraining;
    private double jaccardDistance_Test;
    private double jaccardDistance_Training;
    private double jaccardDistance_Validation;

    private double hammingLoss_FullTraining;
    private double hammingLoss_Test;
    private double hammingLoss_Training;
    private double hammingLoss_Validation;

    private double zeroOneLoss_FullTraining;
    private double zeroOneLoss_Test;
    private double zeroOneLoss_Training;
    private double zeroOneLoss_Validation;

    private double harmonicScore_FullTraining;
    private double harmonicScore_Test;
    private double harmonicScore_Training;
    private double harmonicScore_Validation;

    private double oneError_FullTraining;
    private double oneError_Test;
    private double oneError_Training;
    private double oneError_Validation;

    private double rankLoss_FullTraining;
    private double rankLoss_Test;
    private double rankLoss_Training;
    private double rankLoss_Validation;

    private double avgPrecision_FullTraining;
    private double avgPrecision_Test;
    private double avgPrecision_Training;
    private double avgPrecision_Validation;

    private double microPrecision_FullTraining;
    private double microPrecision_Test;
    private double microPrecision_Training;
    private double microPrecision_Validation;

    private double microRecall_FullTraining;
    private double microRecall_Test;
    private double microRecall_Training;
    private double microRecall_Validation;

    private double macroPrecision_FullTraining;
    private double macroPrecision_Test;
    private double macroPrecision_Training;
    private double macroPrecision_Validation;

    private double macroRecall_FullTraining;
    private double macroRecall_Test;
    private double macroRecall_Training;
    private double macroRecall_Validation;

    private double f1MicroAveraged_FullTraining;
    private double f1MicroAveraged_Test;
    private double f1MicroAveraged_Training;
    private double f1MicroAveraged_Validation;

    private double f1MacroAveragedExample_FullTraining;
    private double f1MacroAveragedExample_Test;
    private double f1MacroAveragedExample_Training;
    private double f1MacroAveragedExample_Validation;

    private double f1MacroAveragedLabel_FullTraining;
    private double f1MacroAveragedLabel_Test;
    private double f1MacroAveragedLabel_Training;
    private double f1MacroAveragedLabel_Validation;

    private double aurcMacroAveraged_FullTraining;
    private double aurcMacroAveraged_Test;
    private double aurcMacroAveraged_Training;
    private double aurcMacroAveraged_Validation;

    private double aurocMacroAveraged_FullTraining;
    private double aurocMacroAveraged_Test;
    private double aurocMacroAveraged_Training;
    private double aurocMacroAveraged_Validation;

    private double emptyLabelvectorsPredicted_FullTraining;
    private double emptyLabelvectorsPredicted_Test;
    private double emptyLabelvectorsPredicted_Training;
    private double emptyLabelvectorsPredicted_Validation;

    private double labelCardinalityPredicted_FullTraining;
    private double labelCardinalityPredicted_Test;
    private double labelCardinalityPredicted_Training;
    private double labelCardinalityPredicted_Validation;

    private double levenshteinDistance_FullTraining;
    private double levenshteinDistance_Test;
    private double levenshteinDistance_Training;
    private double levenshteinDistance_Validation;

    private double labelCardinalityDifference_FullTraining;
    private double labelCardinalityDifference_Test;
    private double labelCardinalityDifference_Training;
    private double labelCardinalityDifference_Validation; 

    public Results(String algorithm, String [] command,
            double accuracy_FullTraining, double accuracy_Test, double accuracy_Training, double accuracy_Validation, 
            double hammingScore_FullTraining, double hammingScore_Test, double hammingScore_Training, double hammingScore_Validation, 
            double exactMatch_FullTraining, double exactMatch_Test, double exactMatch_Training, double exactMatch_Validation, 
            double jaccardDistance_FullTraining, double jaccardDistance_Test, double jaccardDistance_Training, double jaccardDistance_Validation, 
            double hammingLoss_FullTraining, double hammingLoss_Test, double hammingLoss_Training, double hammingLoss_Validation, 
            double zeroOneLoss_FullTraining, double zeroOneLoss_Test, double zeroOneLoss_Training, double zeroOneLoss_Validation, 
            double harmonicScore_FullTraining, double harmonicScore_Test, double harmonicScore_Training, double harmonicScore_Validation, 
            double oneError_FullTraining, double oneError_Test, double oneError_Training, double oneError_Validation, 
            double rankLoss_FullTraining,double rankLoss_Test, double rankLoss_Training, double rankLoss_Validation, 
            double avgPrecision_FullTraining, double avgPrecision_Test, double avgPrecision_Training, double avgPrecision_Validation, 
            double microPrecision_FullTraining, double microPrecision_Test, double microPrecision_Training, double microPrecision_Validation, 
            double microRecall_FullTraining, double microRecall_Test, double microRecall_Training, double microRecall_Validation, 
            double macroPrecision_FullTraining, double macroPrecision_Test, double macroPrecision_Training, double macroPrecision_Validation, 
            double macroRecall_FullTraining, double macroRecall_Test, double macroRecall_Training, double macroRecall_Validation, 
            double f1MicroAveraged_FullTraining, double f1MicroAveraged_Test, double f1MicroAveraged_Training, double f1MicroAveraged_Validation, 
            double f1MacroAveragedExample_FullTraining, double f1MacroAveragedExample_Test, double f1MacroAveragedExample_Training, double f1MacroAveragedExample_Validation, 
            double f1MacroAveragedLabel_FullTraining, double f1MacroAveragedLabel_Test, double f1MacroAveragedLabel_Training, double f1MacroAveragedLabel_Validation, 
            double aurcMacroAveraged_FullTraining, double aurcMacroAveraged_Test, double aurcMacroAveraged_Training, double aurcMacroAveraged_Validation, 
            double aurocMacroAveraged_FullTraining, double aurocMacroAveraged_Test, double aurocMacroAveraged_Training, double aurocMacroAveraged_Validation, 
            double emptyLabelvectorsPredicted_FullTraining, double emptyLabelvectorsPredicted_Test, double emptyLabelvectorsPredicted_Training, double emptyLabelvectorsPredicted_Validation, 
            double labelCardinalityPredicted_FullTraining, double labelCardinalityPredicted_Test, double labelCardinalityPredicted_Training, double labelCardinalityPredicted_Validation, 
            double levenshteinDistance_FullTraining, double levenshteinDistance_Test, double levenshteinDistance_Training, double levenshteinDistance_Validation, 
            double labelCardinalityDifference_FullTraining, double labelCardinalityDifference_Test, double labelCardinalityDifference_Training, double labelCardinalityDifference_Validation) {
        
        this.algorithm = algorithm;
        this.command = command;
        this.accuracy_FullTraining = accuracy_FullTraining;
        this.accuracy_Test = accuracy_Test;
        this.accuracy_Training = accuracy_Training;
        this.accuracy_Validation = accuracy_Validation;
        
        this.hammingScore_FullTraining = hammingScore_FullTraining;
        this.hammingScore_Test = hammingScore_Test;
        this.hammingScore_Training = hammingScore_Training;
        this.hammingScore_Validation = hammingScore_Validation;
        
        this.exactMatch_FullTraining = exactMatch_FullTraining;
        this.exactMatch_Test = exactMatch_Test;
        this.exactMatch_Training = exactMatch_Training;
        this.exactMatch_Validation = exactMatch_Validation;
        
        this.jaccardDistance_FullTraining = jaccardDistance_FullTraining;
        this.jaccardDistance_Test = jaccardDistance_Test;
        this.jaccardDistance_Training = jaccardDistance_Training;
        this.jaccardDistance_Validation = jaccardDistance_Validation;
        
        this.hammingLoss_FullTraining = hammingLoss_FullTraining;
        this.hammingLoss_Test = hammingLoss_Test;
        this.hammingLoss_Training = hammingLoss_Training;
        this.hammingLoss_Validation = hammingLoss_Validation;
        
        this.zeroOneLoss_FullTraining = zeroOneLoss_FullTraining;
        this.zeroOneLoss_Test = zeroOneLoss_Test;
        this.zeroOneLoss_Training = zeroOneLoss_Training;
        this.zeroOneLoss_Validation = zeroOneLoss_Validation;
        
        this.harmonicScore_FullTraining = harmonicScore_FullTraining;
        this.harmonicScore_Test = harmonicScore_Test;
        this.harmonicScore_Training = harmonicScore_Training;
        this.harmonicScore_Validation = harmonicScore_Validation;
        
        this.oneError_FullTraining = oneError_FullTraining;
        this.oneError_Test = oneError_Test;
        this.oneError_Training = oneError_Training;
        this.oneError_Validation = oneError_Validation;
        
        this.rankLoss_FullTraining = rankLoss_FullTraining;
        this.rankLoss_Test = rankLoss_Test;
        this.rankLoss_Training = rankLoss_Training;
        this.rankLoss_Validation = rankLoss_Validation;
        
        this.avgPrecision_FullTraining = avgPrecision_FullTraining;
        this.avgPrecision_Test = avgPrecision_Test;
        this.avgPrecision_Training = avgPrecision_Training;
        this.avgPrecision_Validation = avgPrecision_Validation;
        
        this.microPrecision_FullTraining = microPrecision_FullTraining;
        this.microPrecision_Test = microPrecision_Test;
        this.microPrecision_Training = microPrecision_Training;
        this.microPrecision_Validation = microPrecision_Validation;
        
        this.microRecall_FullTraining = microRecall_FullTraining;
        this.microRecall_Test = microRecall_Test;
        this.microRecall_Training = microRecall_Training;
        this.microRecall_Validation = microRecall_Validation;
        
        this.macroPrecision_FullTraining = macroPrecision_FullTraining;
        this.macroPrecision_Test = macroPrecision_Test;
        this.macroPrecision_Training = macroPrecision_Training;
        this.macroPrecision_Validation = macroPrecision_Validation;
        
        this.macroRecall_FullTraining = macroRecall_FullTraining;
        this.macroRecall_Test = macroRecall_Test;
        this.macroRecall_Training = macroRecall_Training;
        this.macroRecall_Validation = macroRecall_Validation;
        
        this.f1MicroAveraged_FullTraining = f1MicroAveraged_FullTraining;
        this.f1MicroAveraged_Test = f1MicroAveraged_Test;
        this.f1MicroAveraged_Training = f1MicroAveraged_Training;
        this.f1MicroAveraged_Validation = f1MicroAveraged_Validation;
        
        this.f1MacroAveragedExample_FullTraining = f1MacroAveragedExample_FullTraining;
        this.f1MacroAveragedExample_Test = f1MacroAveragedExample_Test;
        this.f1MacroAveragedExample_Training = f1MacroAveragedExample_Training;
        this.f1MacroAveragedExample_Validation = f1MacroAveragedExample_Validation;
        
        this.f1MacroAveragedLabel_FullTraining = f1MacroAveragedLabel_FullTraining;
        this.f1MacroAveragedLabel_Test = f1MacroAveragedLabel_Test;
        this.f1MacroAveragedLabel_Training = f1MacroAveragedLabel_Training;
        this.f1MacroAveragedLabel_Validation = f1MacroAveragedLabel_Validation;
        
        this.aurcMacroAveraged_FullTraining = aurcMacroAveraged_FullTraining;
        this.aurcMacroAveraged_Test = aurcMacroAveraged_Test;
        this.aurcMacroAveraged_Training = aurcMacroAveraged_Training;
        this.aurcMacroAveraged_Validation = aurcMacroAveraged_Validation;
        
        this.aurocMacroAveraged_FullTraining = aurocMacroAveraged_FullTraining;
        this.aurocMacroAveraged_Test = aurocMacroAveraged_Test;
        this.aurocMacroAveraged_Training = aurocMacroAveraged_Training;
        this.aurocMacroAveraged_Validation = aurocMacroAveraged_Validation;
        
        this.emptyLabelvectorsPredicted_FullTraining = emptyLabelvectorsPredicted_FullTraining;
        this.emptyLabelvectorsPredicted_Test = emptyLabelvectorsPredicted_Test;
        this.emptyLabelvectorsPredicted_Training = emptyLabelvectorsPredicted_Training;
        this.emptyLabelvectorsPredicted_Validation = emptyLabelvectorsPredicted_Validation;
        
        this.labelCardinalityPredicted_FullTraining = labelCardinalityPredicted_FullTraining;
        this.labelCardinalityPredicted_Test = labelCardinalityPredicted_Test;
        this.labelCardinalityPredicted_Training = labelCardinalityPredicted_Training;
        this.labelCardinalityPredicted_Validation = labelCardinalityPredicted_Validation;
        
        this.levenshteinDistance_FullTraining = levenshteinDistance_FullTraining;
        this.levenshteinDistance_Test = levenshteinDistance_Test;
        this.levenshteinDistance_Training = levenshteinDistance_Training;
        this.levenshteinDistance_Validation = levenshteinDistance_Validation;
        
        this.labelCardinalityDifference_FullTraining = labelCardinalityDifference_FullTraining;
        this.labelCardinalityDifference_Test = labelCardinalityDifference_Test;
        this.labelCardinalityDifference_Training = labelCardinalityDifference_Training;
        this.labelCardinalityDifference_Validation = labelCardinalityDifference_Validation;
    }
    
    
    

    public String getAlgorithm() {
        return algorithm;
    }

    public double getAccuracy_FullTraining() {
        return accuracy_FullTraining;
    }

    public double getAccuracy_Test() {
        return accuracy_Test;
    }

    public double getAccuracy_Training() {
        return accuracy_Training;
    }

    public double getAccuracy_Validation() {
        return accuracy_Validation;
    }

    public double getHammingScore_FullTraining() {
        return hammingScore_FullTraining;
    }

    public double getHammingScore_Test() {
        return hammingScore_Test;
    }

    public double getHammingScore_Training() {
        return hammingScore_Training;
    }

    public double getHammingScore_Validation() {
        return hammingScore_Validation;
    }

    public double getExactMatch_FullTraining() {
        return exactMatch_FullTraining;
    }

    public double getExactMatch_Test() {
        return exactMatch_Test;
    }

    public double getExactMatch_Training() {
        return exactMatch_Training;
    }

    public double getExactMatch_Validation() {
        return exactMatch_Validation;
    }

    public double getJaccardDistance_FullTraining() {
        return jaccardDistance_FullTraining;
    }

    public double getJaccardDistance_Test() {
        return jaccardDistance_Test;
    }

    public double getJaccardDistance_Training() {
        return jaccardDistance_Training;
    }

    public double getJaccardDistance_Validation() {
        return jaccardDistance_Validation;
    }

    public double getHammingLoss_FullTraining() {
        return hammingLoss_FullTraining;
    }

    public double getHammingLoss_Test() {
        return hammingLoss_Test;
    }

    public double getHammingLoss_Training() {
        return hammingLoss_Training;
    }

    public double getHammingLoss_Validation() {
        return hammingLoss_Validation;
    }

    public double getZeroOneLoss_FullTraining() {
        return zeroOneLoss_FullTraining;
    }

    public double getZeroOneLoss_Test() {
        return zeroOneLoss_Test;
    }

    public double getZeroOneLoss_Training() {
        return zeroOneLoss_Training;
    }

    public double getZeroOneLoss_Validation() {
        return zeroOneLoss_Validation;
    }

    public double getHarmonicScore_FullTraining() {
        return harmonicScore_FullTraining;
    }

    public double getHarmonicScore_Test() {
        return harmonicScore_Test;
    }

    public double getHarmonicScore_Training() {
        return harmonicScore_Training;
    }

    public double getHarmonicScore_Validation() {
        return harmonicScore_Validation;
    }

    public double getOneError_FullTraining() {
        return oneError_FullTraining;
    }

    public double getOneError_Test() {
        return oneError_Test;
    }

    public double getOneError_Training() {
        return oneError_Training;
    }

    public double getOneError_Validation() {
        return oneError_Validation;
    }

    public double getRankLoss_FullTraining() {
        return rankLoss_FullTraining;
    }

    public double getRankLoss_Test() {
        return rankLoss_Test;
    }

    public double getRankLoss_Training() {
        return rankLoss_Training;
    }

    public double getRankLoss_Validation() {
        return rankLoss_Validation;
    }

    public double getAvgPrecision_FullTraining() {
        return avgPrecision_FullTraining;
    }

    public double getAvgPrecision_Test() {
        return avgPrecision_Test;
    }

    public double getAvgPrecision_Training() {
        return avgPrecision_Training;
    }

    public double getAvgPrecision_Validation() {
        return avgPrecision_Validation;
    }

    public double getMicroPrecision_FullTraining() {
        return microPrecision_FullTraining;
    }

    public double getMicroPrecision_Test() {
        return microPrecision_Test;
    }

    public double getMicroPrecision_Training() {
        return microPrecision_Training;
    }

    public double getMicroPrecision_Validation() {
        return microPrecision_Validation;
    }

    public double getMicroRecall_FullTraining() {
        return microRecall_FullTraining;
    }

    public double getMicroRecall_Test() {
        return microRecall_Test;
    }

    public double getMicroRecall_Training() {
        return microRecall_Training;
    }

    public double getMicroRecall_Validation() {
        return microRecall_Validation;
    }

    public double getMacroPrecision_FullTraining() {
        return macroPrecision_FullTraining;
    }

    public double getMacroPrecision_Test() {
        return macroPrecision_Test;
    }

    public double getMacroPrecision_Training() {
        return macroPrecision_Training;
    }

    public double getMacroPrecision_Validation() {
        return macroPrecision_Validation;
    }

    public double getMacroRecall_FullTraining() {
        return macroRecall_FullTraining;
    }

    public double getMacroRecall_Test() {
        return macroRecall_Test;
    }

    public double getMacroRecall_Training() {
        return macroRecall_Training;
    }

    public double getMacroRecall_Validation() {
        return macroRecall_Validation;
    }

    public double getF1MicroAveraged_FullTraining() {
        return f1MicroAveraged_FullTraining;
    }

    public double getF1MicroAveraged_Test() {
        return f1MicroAveraged_Test;
    }

    public double getF1MicroAveraged_Training() {
        return f1MicroAveraged_Training;
    }

    public double getF1MicroAveraged_Validation() {
        return f1MicroAveraged_Validation;
    }

    public double getF1MacroAveragedExample_FullTraining() {
        return f1MacroAveragedExample_FullTraining;
    }

    public double getF1MacroAveragedExample_Test() {
        return f1MacroAveragedExample_Test;
    }

    public double getF1MacroAveragedExample_Training() {
        return f1MacroAveragedExample_Training;
    }

    public double getF1MacroAveragedExample_Validation() {
        return f1MacroAveragedExample_Validation;
    }

    public double getF1MacroAveragedLabel_FullTraining() {
        return f1MacroAveragedLabel_FullTraining;
    }

    public double getF1MacroAveragedLabel_Test() {
        return f1MacroAveragedLabel_Test;
    }

    public double getF1MacroAveragedLabel_Training() {
        return f1MacroAveragedLabel_Training;
    }

    public double getF1MacroAveragedLabel_Validation() {
        return f1MacroAveragedLabel_Validation;
    }

    public double getAurcMacroAveraged_FullTraining() {
        return aurcMacroAveraged_FullTraining;
    }

    public double getAurcMacroAveraged_Test() {
        return aurcMacroAveraged_Test;
    }

    public double getAurcMacroAveraged_Training() {
        return aurcMacroAveraged_Training;
    }

    public double getAurcMacroAveraged_Validation() {
        return aurcMacroAveraged_Validation;
    }

    public double getAurocMacroAveraged_FullTraining() {
        return aurocMacroAveraged_FullTraining;
    }

    public double getAurocMacroAveraged_Test() {
        return aurocMacroAveraged_Test;
    }

    public double getAurocMacroAveraged_Training() {
        return aurocMacroAveraged_Training;
    }

    public double getAurocMacroAveraged_Validation() {
        return aurocMacroAveraged_Validation;
    }

    public double getEmptyLabelvectorsPredicted_FullTraining() {
        return emptyLabelvectorsPredicted_FullTraining;
    }

    public double getEmptyLabelvectorsPredicted_Test() {
        return emptyLabelvectorsPredicted_Test;
    }

    public double getEmptyLabelvectorsPredicted_Training() {
        return emptyLabelvectorsPredicted_Training;
    }

    public double getEmptyLabelvectorsPredicted_Validation() {
        return emptyLabelvectorsPredicted_Validation;
    }

    public double getLabelCardinalityPredicted_FullTraining() {
        return labelCardinalityPredicted_FullTraining;
    }

    public double getLabelCardinalityPredicted_Test() {
        return labelCardinalityPredicted_Test;
    }

    public double getLabelCardinalityPredicted_Training() {
        return labelCardinalityPredicted_Training;
    }

    public double getLabelCardinalityPredicted_Validation() {
        return labelCardinalityPredicted_Validation;
    }

    public double getLevenshteinDistance_FullTraining() {
        return levenshteinDistance_FullTraining;
    }

    public double getLevenshteinDistance_Test() {
        return levenshteinDistance_Test;
    }

    public double getLevenshteinDistance_Training() {
        return levenshteinDistance_Training;
    }

    public double getLevenshteinDistance_Validation() {
        return levenshteinDistance_Validation;
    }

    public double getLabelCardinalityDifference_FullTraining() {
        return labelCardinalityDifference_FullTraining;
    }

    public double getLabelCardinalityDifference_Test() {
        return labelCardinalityDifference_Test;
    }

    public double getLabelCardinalityDifference_Training() {
        return labelCardinalityDifference_Training;
    }

    public double getLabelCardinalityDifference_Validation() {
        return labelCardinalityDifference_Validation;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setAccuracy_FullTraining(double accuracy_FullTraining) {
        this.accuracy_FullTraining = accuracy_FullTraining;
    }

    public void setAccuracy_Test(double accuracy_Test) {
        this.accuracy_Test = accuracy_Test;
    }

    public void setAccuracy_Training(double accuracy_Training) {
        this.accuracy_Training = accuracy_Training;
    }

    public void setAccuracy_Validation(double accuracy_Validation) {
        this.accuracy_Validation = accuracy_Validation;
    }

    public void setHammingScore_FullTraining(double hammingScore_FullTraining) {
        this.hammingScore_FullTraining = hammingScore_FullTraining;
    }

    public void setHammingScore_Test(double hammingScore_Test) {
        this.hammingScore_Test = hammingScore_Test;
    }

    public void setHammingScore_Training(double hammingScore_Training) {
        this.hammingScore_Training = hammingScore_Training;
    }

    public void setHammingScore_Validation(double hammingScore_Validation) {
        this.hammingScore_Validation = hammingScore_Validation;
    }

    public void setExactMatch_FullTraining(double exactMatch_FullTraining) {
        this.exactMatch_FullTraining = exactMatch_FullTraining;
    }

    public void setExactMatch_Test(double exactMatch_Test) {
        this.exactMatch_Test = exactMatch_Test;
    }

    public void setExactMatch_Training(double exactMatch_Training) {
        this.exactMatch_Training = exactMatch_Training;
    }

    public void setExactMatch_Validation(double exactMatch_Validation) {
        this.exactMatch_Validation = exactMatch_Validation;
    }

    public void setJaccardDistance_FullTraining(double jaccardDistance_FullTraining) {
        this.jaccardDistance_FullTraining = jaccardDistance_FullTraining;
    }

    public void setJaccardDistance_Test(double jaccardDistance_Test) {
        this.jaccardDistance_Test = jaccardDistance_Test;
    }

    public void setJaccardDistance_Training(double jaccardDistance_Training) {
        this.jaccardDistance_Training = jaccardDistance_Training;
    }

    public void setJaccardDistance_Validation(double jaccardDistance_Validation) {
        this.jaccardDistance_Validation = jaccardDistance_Validation;
    }

    public void setHammingLoss_FullTraining(double hammingLoss_FullTraining) {
        this.hammingLoss_FullTraining = hammingLoss_FullTraining;
    }

    public void setHammingLoss_Test(double hammingLoss_Test) {
        this.hammingLoss_Test = hammingLoss_Test;
    }

    public void setHammingLoss_Training(double hammingLoss_Training) {
        this.hammingLoss_Training = hammingLoss_Training;
    }

    public void setHammingLoss_Validation(double hammingLoss_Validation) {
        this.hammingLoss_Validation = hammingLoss_Validation;
    }

    public void setZeroOneLoss_FullTraining(double zeroOneLoss_FullTraining) {
        this.zeroOneLoss_FullTraining = zeroOneLoss_FullTraining;
    }

    public void setZeroOneLoss_Test(double zeroOneLoss_Test) {
        this.zeroOneLoss_Test = zeroOneLoss_Test;
    }

    public void setZeroOneLoss_Training(double zeroOneLoss_Training) {
        this.zeroOneLoss_Training = zeroOneLoss_Training;
    }

    public void setZeroOneLoss_Validation(double zeroOneLoss_Validation) {
        this.zeroOneLoss_Validation = zeroOneLoss_Validation;
    }

    public void setHarmonicScore_FullTraining(double harmonicScore_FullTraining) {
        this.harmonicScore_FullTraining = harmonicScore_FullTraining;
    }

    public void setHarmonicScore_Test(double harmonicScore_Test) {
        this.harmonicScore_Test = harmonicScore_Test;
    }

    public void setHarmonicScore_Training(double harmonicScore_Training) {
        this.harmonicScore_Training = harmonicScore_Training;
    }

    public void setHarmonicScore_Validation(double harmonicScore_Validation) {
        this.harmonicScore_Validation = harmonicScore_Validation;
    }

    public void setOneError_FullTraining(double oneError_FullTraining) {
        this.oneError_FullTraining = oneError_FullTraining;
    }

    public void setOneError_Test(double oneError_Test) {
        this.oneError_Test = oneError_Test;
    }

    public void setOneError_Training(double oneError_Training) {
        this.oneError_Training = oneError_Training;
    }

    public void setOneError_Validation(double oneError_Validation) {
        this.oneError_Validation = oneError_Validation;
    }

    public void setRankLoss_FullTraining(double rankLoss_FullTraining) {
        this.rankLoss_FullTraining = rankLoss_FullTraining;
    }

    public void setRankLoss_Test(double rankLoss_Test) {
        this.rankLoss_Test = rankLoss_Test;
    }

    public void setRankLoss_Training(double rankLoss_Training) {
        this.rankLoss_Training = rankLoss_Training;
    }

    public void setRankLoss_Validation(double rankLoss_Validation) {
        this.rankLoss_Validation = rankLoss_Validation;
    }

    public void setAvgPrecision_FullTraining(double avgPrecision_FullTraining) {
        this.avgPrecision_FullTraining = avgPrecision_FullTraining;
    }

    public void setAvgPrecision_Test(double avgPrecision_Test) {
        this.avgPrecision_Test = avgPrecision_Test;
    }

    public void setAvgPrecision_Training(double avgPrecision_Training) {
        this.avgPrecision_Training = avgPrecision_Training;
    }

    public void setAvgPrecision_Validation(double avgPrecision_Validation) {
        this.avgPrecision_Validation = avgPrecision_Validation;
    }

    public void setMicroPrecision_FullTraining(double microPrecision_FullTraining) {
        this.microPrecision_FullTraining = microPrecision_FullTraining;
    }

    public void setMicroPrecision_Test(double microPrecision_Test) {
        this.microPrecision_Test = microPrecision_Test;
    }

    public void setMicroPrecision_Training(double microPrecision_Training) {
        this.microPrecision_Training = microPrecision_Training;
    }

    public void setMicroPrecision_Validation(double microPrecision_Validation) {
        this.microPrecision_Validation = microPrecision_Validation;
    }

    public void setMicroRecall_FullTraining(double microRecall_FullTraining) {
        this.microRecall_FullTraining = microRecall_FullTraining;
    }

    public void setMicroRecall_Test(double microRecall_Test) {
        this.microRecall_Test = microRecall_Test;
    }

    public void setMicroRecall_Training(double microRecall_Training) {
        this.microRecall_Training = microRecall_Training;
    }

    public void setMicroRecall_Validation(double microRecall_Validation) {
        this.microRecall_Validation = microRecall_Validation;
    }

    public void setMacroPrecision_FullTraining(double macroPrecision_FullTraining) {
        this.macroPrecision_FullTraining = macroPrecision_FullTraining;
    }

    public void setMacroPrecision_Test(double macroPrecision_Test) {
        this.macroPrecision_Test = macroPrecision_Test;
    }

    public void setMacroPrecision_Training(double macroPrecision_Training) {
        this.macroPrecision_Training = macroPrecision_Training;
    }

    public void setMacroPrecision_Validation(double macroPrecision_Validation) {
        this.macroPrecision_Validation = macroPrecision_Validation;
    }

    public void setMacroRecall_FullTraining(double macroRecall_FullTraining) {
        this.macroRecall_FullTraining = macroRecall_FullTraining;
    }

    public void setMacroRecall_Test(double macroRecall_Test) {
        this.macroRecall_Test = macroRecall_Test;
    }

    public void setMacroRecall_Training(double macroRecall_Training) {
        this.macroRecall_Training = macroRecall_Training;
    }

    public void setMacroRecall_Validation(double macroRecall_Validation) {
        this.macroRecall_Validation = macroRecall_Validation;
    }

    public void setF1MicroAveraged_FullTraining(double f1MicroAveraged_FullTraining) {
        this.f1MicroAveraged_FullTraining = f1MicroAveraged_FullTraining;
    }

    public void setF1MicroAveraged_Test(double f1MicroAveraged_Test) {
        this.f1MicroAveraged_Test = f1MicroAveraged_Test;
    }

    public void setF1MicroAveraged_Training(double f1MicroAveraged_Training) {
        this.f1MicroAveraged_Training = f1MicroAveraged_Training;
    }

    public void setF1MicroAveraged_Validation(double f1MicroAveraged_Validation) {
        this.f1MicroAveraged_Validation = f1MicroAveraged_Validation;
    }

    public void setF1MacroAveragedExample_FullTraining(double f1MacroAveragedExample_FullTraining) {
        this.f1MacroAveragedExample_FullTraining = f1MacroAveragedExample_FullTraining;
    }

    public void setF1MacroAveragedExample_Test(double f1MacroAveragedExample_Test) {
        this.f1MacroAveragedExample_Test = f1MacroAveragedExample_Test;
    }

    public void setF1MacroAveragedExample_Training(double f1MacroAveragedExample_Training) {
        this.f1MacroAveragedExample_Training = f1MacroAveragedExample_Training;
    }

    public void setF1MacroAveragedExample_Validation(double f1MacroAveragedExample_Validation) {
        this.f1MacroAveragedExample_Validation = f1MacroAveragedExample_Validation;
    }

    public void setF1MacroAveragedLabel_FullTraining(double f1MacroAveragedLabel_FullTraining) {
        this.f1MacroAveragedLabel_FullTraining = f1MacroAveragedLabel_FullTraining;
    }

    public void setF1MacroAveragedLabel_Test(double f1MacroAveragedLabel_Test) {
        this.f1MacroAveragedLabel_Test = f1MacroAveragedLabel_Test;
    }

    public void setF1MacroAveragedLabel_Training(double f1MacroAveragedLabel_Training) {
        this.f1MacroAveragedLabel_Training = f1MacroAveragedLabel_Training;
    }

    public void setF1MacroAveragedLabel_Validation(double f1MacroAveragedLabel_Validation) {
        this.f1MacroAveragedLabel_Validation = f1MacroAveragedLabel_Validation;
    }

    public void setAurcMacroAveraged_FullTraining(double aurcMacroAveraged_FullTraining) {
        this.aurcMacroAveraged_FullTraining = aurcMacroAveraged_FullTraining;
    }

    public void setAurcMacroAveraged_Test(double aurcMacroAveraged_Test) {
        this.aurcMacroAveraged_Test = aurcMacroAveraged_Test;
    }

    public void setAurcMacroAveraged_Training(double aurcMacroAveraged_Training) {
        this.aurcMacroAveraged_Training = aurcMacroAveraged_Training;
    }

    public void setAurcMacroAveraged_Validation(double aurcMacroAveraged_Validation) {
        this.aurcMacroAveraged_Validation = aurcMacroAveraged_Validation;
    }

    public void setAurocMacroAveraged_FullTraining(double aurocMacroAveraged_FullTraining) {
        this.aurocMacroAveraged_FullTraining = aurocMacroAveraged_FullTraining;
    }

    public void setAurocMacroAveraged_Test(double aurocMacroAveraged_Test) {
        this.aurocMacroAveraged_Test = aurocMacroAveraged_Test;
    }

    public void setAurocMacroAveraged_Training(double aurocMacroAveraged_Training) {
        this.aurocMacroAveraged_Training = aurocMacroAveraged_Training;
    }

    public void setAurocMacroAveraged_Validation(double aurocMacroAveraged_Validation) {
        this.aurocMacroAveraged_Validation = aurocMacroAveraged_Validation;
    }

    public void setEmptyLabelvectorsPredicted_FullTraining(double emptyLabelvectorsPredicted_FullTraining) {
        this.emptyLabelvectorsPredicted_FullTraining = emptyLabelvectorsPredicted_FullTraining;
    }

    public void setEmptyLabelvectorsPredicted_Test(double emptyLabelvectorsPredicted_Test) {
        this.emptyLabelvectorsPredicted_Test = emptyLabelvectorsPredicted_Test;
    }

    public void setEmptyLabelvectorsPredicted_Training(double emptyLabelvectorsPredicted_Training) {
        this.emptyLabelvectorsPredicted_Training = emptyLabelvectorsPredicted_Training;
    }

    public void setEmptyLabelvectorsPredicted_Validation(double emptyLabelvectorsPredicted_Validation) {
        this.emptyLabelvectorsPredicted_Validation = emptyLabelvectorsPredicted_Validation;
    }

    public void setLabelCardinalityPredicted_FullTraining(double labelCardinalityPredicted_FullTraining) {
        this.labelCardinalityPredicted_FullTraining = labelCardinalityPredicted_FullTraining;
    }

    public void setLabelCardinalityPredicted_Test(double labelCardinalityPredicted_Test) {
        this.labelCardinalityPredicted_Test = labelCardinalityPredicted_Test;
    }

    public void setLabelCardinalityPredicted_Training(double labelCardinalityPredicted_Training) {
        this.labelCardinalityPredicted_Training = labelCardinalityPredicted_Training;
    }

    public void setLabelCardinalityPredicted_Validation(double labelCardinalityPredicted_Validation) {
        this.labelCardinalityPredicted_Validation = labelCardinalityPredicted_Validation;
    }

    public void setLevenshteinDistance_FullTraining(double levenshteinDistance_FullTraining) {
        this.levenshteinDistance_FullTraining = levenshteinDistance_FullTraining;
    }

    public void setLevenshteinDistance_Test(double levenshteinDistance_Test) {
        this.levenshteinDistance_Test = levenshteinDistance_Test;
    }

    public void setLevenshteinDistance_Training(double levenshteinDistance_Training) {
        this.levenshteinDistance_Training = levenshteinDistance_Training;
    }

    public void setLevenshteinDistance_Validation(double levenshteinDistance_Validation) {
        this.levenshteinDistance_Validation = levenshteinDistance_Validation;
    }

    public void setLabelCardinalityDifference_FullTraining(double labelCardinalityDifference_FullTraining) {
        this.labelCardinalityDifference_FullTraining = labelCardinalityDifference_FullTraining;
    }

    public void setLabelCardinalityDifference_Test(double labelCardinalityDifference_Test) {
        this.labelCardinalityDifference_Test = labelCardinalityDifference_Test;
    }

    public void setLabelCardinalityDifference_Training(double labelCardinalityDifference_Training) {
        this.labelCardinalityDifference_Training = labelCardinalityDifference_Training;
    }

    public void setLabelCardinalityDifference_Validation(double labelCardinalityDifference_Validation) {
        this.labelCardinalityDifference_Validation = labelCardinalityDifference_Validation;
    }

    public String[] getCommand() {
        return command;
    }

    public void setCommand(String[] command) {
        this.command = command;
    }

    
    
    
    
    

 
    
    
    
}
