/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meka.classifiers.multilabel.meta.automekaggp.core;

/**
 * Class that has the structure to save part of the results -- multi-label classification metrics..
 * @author alexgcsa
 */
public class ResultsEval {
    
    String [] command;

    private double accuracy_Training;
    private double accuracy_Evaluation;

    private double hammingScore_Training;
    private double hammingScore_Evaluation;

    private double exactMatch_Training;
    private double exactMatch_Evaluation;

    private double jaccardDistance_Training;
    private double jaccardDistance_Evaluation;

    private double hammingLoss_Training;
    private double hammingLoss_Evaluation;

    private double zeroOneLoss_Training;
    private double zeroOneLoss_Evaluation;

    private double harmonicScore_Training;
    private double harmonicScore_Evaluation;

    private double oneError_Training;
    private double oneError_Evaluation;

    private double rankLoss_Training;
    private double rankLoss_Evaluation;

    private double avgPrecision_Training;
    private double avgPrecision_Evaluation;

    private double microPrecision_Training;
    private double microPrecision_Evaluation;

    private double microRecall_Training;
    private double microRecall_Evaluation;

    private double macroPrecision_Training;
    private double macroPrecision_Evaluation;

    private double macroRecall_Training;
    private double macroRecall_Evaluation;

    private double f1MicroAveraged_Training;
    private double f1MicroAveraged_Evaluation;

    private double f1MacroAveragedExample_Training;
    private double f1MacroAveragedExample_Evaluation;

    private double f1MacroAveragedLabel_Training;
    private double f1MacroAveragedLabel_Evaluation;

    private double aurcMacroAveraged_Training;
    private double aurcMacroAveraged_Evaluation;

    private double aurocMacroAveraged_Training;
    private double aurocMacroAveraged_Evaluation;

    private double emptyLabelvectorsPredicted_Training;
    private double emptyLabelvectorsPredicted_Evaluation;

    private double labelCardinalityPredicted_Training;
    private double labelCardinalityPredicted_Evaluation;

    private double levenshteinDistance_Training;
    private double levenshteinDistance_Evaluation;

    private double labelCardinalityDifference_Training;
    private double labelCardinalityDifference_Evaluation;

    public ResultsEval() {
        accuracy_Training = 0.0;
        accuracy_Evaluation = 0.0;

        hammingScore_Training = 0.0;
        hammingScore_Evaluation = 0.0;

        exactMatch_Training = 0.0;
        exactMatch_Evaluation = 0.0;

        jaccardDistance_Training = 0.0;
        jaccardDistance_Evaluation = 0.0;

        hammingLoss_Training = 0.0;
        hammingLoss_Evaluation = 0.0;

        zeroOneLoss_Training = 0.0;
        zeroOneLoss_Evaluation = 0.0;

        harmonicScore_Training = 0.0;
        harmonicScore_Evaluation = 0.0;

        oneError_Training = 0.0;
        oneError_Evaluation = 0.0;

        rankLoss_Training = 0.0;
        rankLoss_Evaluation = 0.0;

        avgPrecision_Training = 0.0;
        avgPrecision_Evaluation = 0.0;

        microPrecision_Training = 0.0;
        microPrecision_Evaluation = 0.0;

        microRecall_Training = 0.0;
        microRecall_Evaluation = 0.0;

        macroPrecision_Training = 0.0;
        macroPrecision_Evaluation = 0.0;

        macroRecall_Training = 0.0;
        macroRecall_Evaluation = 0.0;

        f1MicroAveraged_Training = 0.0;
        f1MicroAveraged_Evaluation = 0.0;

        f1MacroAveragedExample_Training = 0.0;
        f1MacroAveragedExample_Evaluation = 0.0;

        f1MacroAveragedLabel_Training = 0.0;
        f1MacroAveragedLabel_Evaluation = 0.0;

        aurcMacroAveraged_Training = 0.0;
        aurcMacroAveraged_Evaluation = 0.0;

        aurocMacroAveraged_Training = 0.0;
        aurocMacroAveraged_Evaluation = 0.0;

        emptyLabelvectorsPredicted_Training = 0.0;
        emptyLabelvectorsPredicted_Evaluation = 0.0;

        labelCardinalityPredicted_Training = 0.0;
        labelCardinalityPredicted_Evaluation = 0.0;

        levenshteinDistance_Training = 0.0;
        levenshteinDistance_Evaluation = 0.0;

        labelCardinalityDifference_Training = 0.0;
        labelCardinalityDifference_Evaluation = 0.0;

    }

    public ResultsEval(String [] command, double accuracy_Training, double accuracy_Evaluation, double hammingScore_Training, double hammingScore_Evaluation, double exactMatch_Training, double exactMatch_Evaluation, double jaccardDistance_Training, double jaccardDistance_Evaluation, double hammingLoss_Training, double hammingLoss_Evaluation, double zeroOneLoss_Training, double zeroOneLoss_Evaluation, double harmonicScore_Training, double harmonicScore_Evaluation, double oneError_Training, double oneError_Evaluation, double rankLoss_Training, double rankLoss_Evaluation, double avgPrecision_Training, double avgPrecision_Evaluation, double microPrecision_Training, double microPrecision_Evaluation, double microRecall_Training, double microRecall_Evaluation, double macroPrecision_Training, double macroPrecision_Evaluation, double macroRecall_Training, double macroRecall_Evaluation, double f1MicroAveraged_Training, double f1MicroAveraged_Evaluation, double f1MacroAveragedExample_Training, double f1MacroAveragedExample_Evaluation, double f1MacroAveragedLabel_Training, double f1MacroAveragedLabel_Evaluation, double aurcMacroAveraged_Training, double aurcMacroAveraged_Evaluation, double aurocMacroAveraged_Training, double aurocMacroAveraged_Evaluation, double emptyLabelvectorsPredicted_Training, double emptyLabelvectorsPredicted_Evaluation, double labelCardinalityPredicted_Training, double labelCardinalityPredicted_Evaluation, double levenshteinDistance_Training, double levenshteinDistance_Evaluation, double labelCardinalityDifference_Training, double labelCardinalityDifference_Evaluation) {
        this.command = command;
        this.accuracy_Training = accuracy_Training;
        this.accuracy_Evaluation = accuracy_Evaluation;
        this.hammingScore_Training = hammingScore_Training;
        this.hammingScore_Evaluation = hammingScore_Evaluation;
        this.exactMatch_Training = exactMatch_Training;
        this.exactMatch_Evaluation = exactMatch_Evaluation;
        this.jaccardDistance_Training = jaccardDistance_Training;
        this.jaccardDistance_Evaluation = jaccardDistance_Evaluation;
        this.hammingLoss_Training = hammingLoss_Training;
        this.hammingLoss_Evaluation = hammingLoss_Evaluation;
        this.zeroOneLoss_Training = zeroOneLoss_Training;
        this.zeroOneLoss_Evaluation = zeroOneLoss_Evaluation;
        this.harmonicScore_Training = harmonicScore_Training;
        this.harmonicScore_Evaluation = harmonicScore_Evaluation;
        this.oneError_Training = oneError_Training;
        this.oneError_Evaluation = oneError_Evaluation;
        this.rankLoss_Training = rankLoss_Training;
        this.rankLoss_Evaluation = rankLoss_Evaluation;
        this.avgPrecision_Training = avgPrecision_Training;
        this.avgPrecision_Evaluation = avgPrecision_Evaluation;
        this.microPrecision_Training = microPrecision_Training;
        this.microPrecision_Evaluation = microPrecision_Evaluation;
        this.microRecall_Training = microRecall_Training;
        this.microRecall_Evaluation = microRecall_Evaluation;
        this.macroPrecision_Training = macroPrecision_Training;
        this.macroPrecision_Evaluation = macroPrecision_Evaluation;
        this.macroRecall_Training = macroRecall_Training;
        this.macroRecall_Evaluation = macroRecall_Evaluation;
        this.f1MicroAveraged_Training = f1MicroAveraged_Training;
        this.f1MicroAveraged_Evaluation = f1MicroAveraged_Evaluation;
        this.f1MacroAveragedExample_Training = f1MacroAveragedExample_Training;
        this.f1MacroAveragedExample_Evaluation = f1MacroAveragedExample_Evaluation;
        this.f1MacroAveragedLabel_Training = f1MacroAveragedLabel_Training;
        this.f1MacroAveragedLabel_Evaluation = f1MacroAveragedLabel_Evaluation;
        this.aurcMacroAveraged_Training = aurcMacroAveraged_Training;
        this.aurcMacroAveraged_Evaluation = aurcMacroAveraged_Evaluation;
        this.aurocMacroAveraged_Training = aurocMacroAveraged_Training;
        this.aurocMacroAveraged_Evaluation = aurocMacroAveraged_Evaluation;
        this.emptyLabelvectorsPredicted_Training = emptyLabelvectorsPredicted_Training;
        this.emptyLabelvectorsPredicted_Evaluation = emptyLabelvectorsPredicted_Evaluation;
        this.labelCardinalityPredicted_Training = labelCardinalityPredicted_Training;
        this.labelCardinalityPredicted_Evaluation = labelCardinalityPredicted_Evaluation;
        this.levenshteinDistance_Training = levenshteinDistance_Training;
        this.levenshteinDistance_Evaluation = levenshteinDistance_Evaluation;
        this.labelCardinalityDifference_Training = labelCardinalityDifference_Training;
        this.labelCardinalityDifference_Evaluation = labelCardinalityDifference_Evaluation;
    }



    public void setAccuracy_Training(double accuracy_Training) {
        this.accuracy_Training = accuracy_Training;
    }

    public void setAccuracy_Evaluation(double accuracy_Evaluation) {
        this.accuracy_Evaluation = accuracy_Evaluation;
    }

    public void setHammingScore_Training(double hammingScore_Training) {
        this.hammingScore_Training = hammingScore_Training;
    }

    public void setHammingScore_Evaluation(double hammingScore_Evaluation) {
        this.hammingScore_Evaluation = hammingScore_Evaluation;
    }

    public void setExactMatch_Training(double exactMatch_Training) {
        this.exactMatch_Training = exactMatch_Training;
    }

    public void setExactMatch_Evaluation(double exactMatch_Evaluation) {
        this.exactMatch_Evaluation = exactMatch_Evaluation;
    }

    public void setJaccardDistance_Training(double jaccardDistance_Training) {
        this.jaccardDistance_Training = jaccardDistance_Training;
    }

    public void setJaccardDistance_Evaluation(double jaccardDistance_Evaluation) {
        this.jaccardDistance_Evaluation = jaccardDistance_Evaluation;
    }

    public void setHammingLoss_Training(double hammingLoss_Training) {
        this.hammingLoss_Training = hammingLoss_Training;
    }

    public void setHammingLoss_Evaluation(double hammingLoss_Evaluation) {
        this.hammingLoss_Evaluation = hammingLoss_Evaluation;
    }

    public void setZeroOneLoss_Training(double zeroOneLoss_Training) {
        this.zeroOneLoss_Training = zeroOneLoss_Training;
    }

    public void setZeroOneLoss_Evaluation(double zeroOneLoss_Evaluation) {
        this.zeroOneLoss_Evaluation = zeroOneLoss_Evaluation;
    }

    public void setHarmonicScore_Training(double harmonicScore_Training) {
        this.harmonicScore_Training = harmonicScore_Training;
    }

    public void setHarmonicScore_Evaluation(double harmonicScore_Evaluation) {
        this.harmonicScore_Evaluation = harmonicScore_Evaluation;
    }

    public void setOneError_Training(double oneError_Training) {
        this.oneError_Training = oneError_Training;
    }

    public void setOneError_Evaluation(double oneError_Evaluation) {
        this.oneError_Evaluation = oneError_Evaluation;
    }

    public void setRankLoss_Training(double rankLoss_Training) {
        this.rankLoss_Training = rankLoss_Training;
    }

    public void setRankLoss_Evaluation(double rankLoss_Evaluation) {
        this.rankLoss_Evaluation = rankLoss_Evaluation;
    }

    public void setAvgPrecision_Training(double avgPrecision_Training) {
        this.avgPrecision_Training = avgPrecision_Training;
    }

    public void setAvgPrecision_Evaluation(double avgPrecision_Evaluation) {
        this.avgPrecision_Evaluation = avgPrecision_Evaluation;
    }

    public void setMicroPrecision_Training(double microPrecision_Training) {
        this.microPrecision_Training = microPrecision_Training;
    }

    public void setMicroPrecision_Evaluation(double microPrecision_Evaluation) {
        this.microPrecision_Evaluation = microPrecision_Evaluation;
    }

    public void setMicroRecall_Training(double microRecall_Training) {
        this.microRecall_Training = microRecall_Training;
    }

    public void setMicroRecall_Evaluation(double microRecall_Evaluation) {
        this.microRecall_Evaluation = microRecall_Evaluation;
    }

    public void setMacroPrecision_Training(double macroPrecision_Training) {
        this.macroPrecision_Training = macroPrecision_Training;
    }

    public void setMacroPrecision_Evaluation(double macroPrecision_Evaluation) {
        this.macroPrecision_Evaluation = macroPrecision_Evaluation;
    }

    public void setMacroRecall_Training(double macroRecall_Training) {
        this.macroRecall_Training = macroRecall_Training;
    }

    public void setMacroRecall_Evaluation(double macroRecall_Evaluation) {
        this.macroRecall_Evaluation = macroRecall_Evaluation;
    }

    public void setF1MicroAveraged_Training(double f1MicroAveraged_Training) {
        this.f1MicroAveraged_Training = f1MicroAveraged_Training;
    }

    public void setF1MicroAveraged_Evaluation(double f1MicroAveraged_Evaluation) {
        this.f1MicroAveraged_Evaluation = f1MicroAveraged_Evaluation;
    }

    public void setF1MacroAveragedExample_Training(double f1MacroAveragedExample_Training) {
        this.f1MacroAveragedExample_Training = f1MacroAveragedExample_Training;
    }

    public void setF1MacroAveragedExample_Evaluation(double f1MacroAveragedExample_Evaluation) {
        this.f1MacroAveragedExample_Evaluation = f1MacroAveragedExample_Evaluation;
    }

    public void setF1MacroAveragedLabel_Training(double f1MacroAveragedLabel_Training) {
        this.f1MacroAveragedLabel_Training = f1MacroAveragedLabel_Training;
    }

    public void setF1MacroAveragedLabel_Evaluation(double f1MacroAveragedLabel_Evaluation) {
        this.f1MacroAveragedLabel_Evaluation = f1MacroAveragedLabel_Evaluation;
    }

    public void setAurcMacroAveraged_Training(double aurcMacroAveraged_Training) {
        this.aurcMacroAveraged_Training = aurcMacroAveraged_Training;
    }

    public void setAurcMacroAveraged_Evaluation(double aurcMacroAveraged_Evaluation) {
        this.aurcMacroAveraged_Evaluation = aurcMacroAveraged_Evaluation;
    }

    public void setAurocMacroAveraged_Training(double aurocMacroAveraged_Training) {
        this.aurocMacroAveraged_Training = aurocMacroAveraged_Training;
    }

    public void setAurocMacroAveraged_Evaluation(double aurocMacroAveraged_Evaluation) {
        this.aurocMacroAveraged_Evaluation = aurocMacroAveraged_Evaluation;
    }

    public void setEmptyLabelvectorsPredicted_Training(double emptyLabelvectorsPredicted_Training) {
        this.emptyLabelvectorsPredicted_Training = emptyLabelvectorsPredicted_Training;
    }

    public void setEmptyLabelvectorsPredicted_Evaluation(double emptyLabelvectorsPredicted_Evaluation) {
        this.emptyLabelvectorsPredicted_Evaluation = emptyLabelvectorsPredicted_Evaluation;
    }

    public void setLabelCardinalityPredicted_Training(double labelCardinalityPredicted_Training) {
        this.labelCardinalityPredicted_Training = labelCardinalityPredicted_Training;
    }

    public void setLabelCardinalityPredicted_Evaluation(double labelCardinalityPredicted_Evaluation) {
        this.labelCardinalityPredicted_Evaluation = labelCardinalityPredicted_Evaluation;
    }

    public void setLevenshteinDistance_Training(double levenshteinDistance_Training) {
        this.levenshteinDistance_Training = levenshteinDistance_Training;
    }

    public void setLevenshteinDistance_Evaluation(double levenshteinDistance_Evaluation) {
        this.levenshteinDistance_Evaluation = levenshteinDistance_Evaluation;
    }

    public void setLabelCardinalityDifference_Training(double labelCardinalityDifference_Training) {
        this.labelCardinalityDifference_Training = labelCardinalityDifference_Training;
    }

    public void setLabelCardinalityDifference_Evaluation(double labelCardinalityDifference_Evaluation) {
        this.labelCardinalityDifference_Evaluation = labelCardinalityDifference_Evaluation;
    }

    public double getAccuracy_Training() {
        return accuracy_Training;
    }

    public double getAccuracy_Evaluation() {
        return accuracy_Evaluation;
    }

    public double getHammingScore_Training() {
        return hammingScore_Training;
    }

    public double getHammingScore_Evaluation() {
        return hammingScore_Evaluation;
    }

    public double getExactMatch_Training() {
        return exactMatch_Training;
    }

    public double getExactMatch_Evaluation() {
        return exactMatch_Evaluation;
    }

    public double getJaccardDistance_Training() {
        return jaccardDistance_Training;
    }

    public double getJaccardDistance_Evaluation() {
        return jaccardDistance_Evaluation;
    }

    public double getHammingLoss_Training() {
        return hammingLoss_Training;
    }

    public double getHammingLoss_Evaluation() {
        return hammingLoss_Evaluation;
    }

    public double getZeroOneLoss_Training() {
        return zeroOneLoss_Training;
    }

    public double getZeroOneLoss_Evaluation() {
        return zeroOneLoss_Evaluation;
    }

    public double getHarmonicScore_Training() {
        return harmonicScore_Training;
    }

    public double getHarmonicScore_Evaluation() {
        return harmonicScore_Evaluation;
    }

    public double getOneError_Training() {
        return oneError_Training;
    }

    public double getOneError_Evaluation() {
        return oneError_Evaluation;
    }

    public double getRankLoss_Training() {
        return rankLoss_Training;
    }

    public double getRankLoss_Evaluation() {
        return rankLoss_Evaluation;
    }

    public double getAvgPrecision_Training() {
        return avgPrecision_Training;
    }

    public double getAvgPrecision_Evaluation() {
        return avgPrecision_Evaluation;
    }

    public double getMicroPrecision_Training() {
        return microPrecision_Training;
    }

    public double getMicroPrecision_Evaluation() {
        return microPrecision_Evaluation;
    }

    public double getMicroRecall_Training() {
        return microRecall_Training;
    }

    public double getMicroRecall_Evaluation() {
        return microRecall_Evaluation;
    }

    public double getMacroPrecision_Training() {
        return macroPrecision_Training;
    }

    public double getMacroPrecision_Evaluation() {
        return macroPrecision_Evaluation;
    }

    public double getMacroRecall_Training() {
        return macroRecall_Training;
    }

    public double getMacroRecall_Evaluation() {
        return macroRecall_Evaluation;
    }

    public double getF1MicroAveraged_Training() {
        return f1MicroAveraged_Training;
    }

    public double getF1MicroAveraged_Evaluation() {
        return f1MicroAveraged_Evaluation;
    }

    public double getF1MacroAveragedExample_Training() {
        return f1MacroAveragedExample_Training;
    }

    public double getF1MacroAveragedExample_Evaluation() {
        return f1MacroAveragedExample_Evaluation;
    }

    public double getF1MacroAveragedLabel_Training() {
        return f1MacroAveragedLabel_Training;
    }

    public double getF1MacroAveragedLabel_Evaluation() {
        return f1MacroAveragedLabel_Evaluation;
    }

    public double getAurcMacroAveraged_Training() {
        return aurcMacroAveraged_Training;
    }

    public double getAurcMacroAveraged_Evaluation() {
        return aurcMacroAveraged_Evaluation;
    }

    public double getAurocMacroAveraged_Training() {
        return aurocMacroAveraged_Training;
    }

    public double getAurocMacroAveraged_Evaluation() {
        return aurocMacroAveraged_Evaluation;
    }

    public double getEmptyLabelvectorsPredicted_Training() {
        return emptyLabelvectorsPredicted_Training;
    }

    public double getEmptyLabelvectorsPredicted_Evaluation() {
        return emptyLabelvectorsPredicted_Evaluation;
    }

    public double getLabelCardinalityPredicted_Training() {
        return labelCardinalityPredicted_Training;
    }

    public double getLabelCardinalityPredicted_Evaluation() {
        return labelCardinalityPredicted_Evaluation;
    }

    public double getLevenshteinDistance_Training() {
        return levenshteinDistance_Training;
    }

    public double getLevenshteinDistance_Evaluation() {
        return levenshteinDistance_Evaluation;
    }

    public double getLabelCardinalityDifference_Training() {
        return labelCardinalityDifference_Training;
    }

    public double getLabelCardinalityDifference_Evaluation() {
        return labelCardinalityDifference_Evaluation;
    }

    public String[] getCommand() {
        return command;
    }

    


}