/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meka.classifiers.multilabel.meta.gaautomlc.core;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.tools.jar.CommandLine;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import meka.classifiers.multilabel.meta.gaautomlc.core.xmlparser.Allele;
import weka.core.Instances;
import weka.core.Utils;



/**
 *
 * @author alex
 */
public class MetaIndividual implements Comparable<MetaIndividual>{
    
    protected static StringBuffer m_statistics;
    
    protected double[] m_individual;    
    protected Allele m_genome;
    protected int m_genomeSize;
    private double m_fitness;
    protected String m_individualInString;
    protected String [] m_individualNameVector;
    
//    public static ArrayList<Results> generatedResutls = null;
    protected Double m_validationEvaluation;

        
    protected static MersenneTwisterFast rnd = null;
    

    public MetaIndividual(double[] individual, Allele genome, double fitness) {
        this.m_individual = individual;
        this.m_genome = genome;
        this.m_genomeSize = individual.length;
        this.m_fitness = fitness;
        try {
            this.m_individualNameVector = this.getClassifierName();
            this.m_individualInString = Arrays.toString(this.m_individualNameVector);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }


    /**
     * Returns a copy of the genetic array of the individual
     * @return genetic array (copy)
     */
    public double[] getGeneticCode() {
        return Arrays.copyOf(m_individual, m_individual.length);
    }
    

    private int doubleToIntMapping(double d, Allele gene){
        int idMethod = -1;
        
        int optionSize = gene.size() - 1;
            idMethod = (int)Math.round(d * optionSize);
      
            return idMethod;
    }
    

        
           
    public double intToDoubleMapping(int j, Allele gene){
        int optionSize = gene.size() - 1;
        double prob = 0.0;
        
//        if(gene.getParameter().equals("-D -Q")){
//            prob = i/4960000 ; //5,547,000 or 4.960.000
//        }else{
            prob = j/(double) optionSize;
//        }
        
        return prob;        
    }

    public int getM_genomeSize() {
        return m_genomeSize;
    }
    
    
        
  /**
     * Specific mapping for search methods:
     */
    private int doubleToIntMappingSearch(double d, Allele gene) {
        int idMethod = -1;

        int maxValue = gene.getM_totalCount();
       
        int value = (int) Math.round(d * maxValue);
        idMethod = this.findCorrectIntervalID(value);

        return idMethod;
    }
   
    /**
     * Find the correct int that maps the real value.
     * @param value value of the interval - in that gene
     * @return the correct value of the mapped option - in that gene
     */
    private int findCorrectIntervalID(long value){
        LinkedHashMap<String, String> intervals = this.m_genome.getIntevalosAlg();
        
        String between = "";
        int i = 0;
        long begin = -1;
        long end = -1;
        int index = 0;

        Iterator<String> it = intervals.values().iterator();

        while (it.hasNext()) {
           between = it.next();
           
            String[] split = between.split(";");
            begin = Long.parseLong(split[0]);
            end = Long.parseLong(split[1]);
            
            if((value >= begin) && (value < end)){
                index = i;
            }
            i++;
        }        
        return index;
    }
    
    
    /**
     * Returns an evaluation metric about the individual
     * @param fitnessType type of evaluation metric
     * @return avalation metric (fitness)
     * @throws Exception 
     */
    public  double setEvaluation() throws Exception{
        
        this.m_fitness = 0.0;
        
        if(this.m_validationEvaluation == null){
            this.m_fitness = 0.0;
            return this.m_fitness;
        }
        
        else{
//            for(int i=0; i< m_validationsEvaluations.size(); i++){
             if(m_validationEvaluation != null){

                double validationEval = m_validationEvaluation;
                   this.m_fitness += validationEval;

            }else{
                 this.m_fitness = 0.0;
             }

        }
        return this.m_fitness;
    }  

    public void setM_validationEvaluation(Double m_validationEvaluation) {
        this.m_validationEvaluation = m_validationEvaluation;
    }

    public Double getM_validationEvaluation() {
        return m_validationEvaluation;
    }
    
    
  
    
        /**
     * Evaluate a set of individuals over a set of datasets
     * @param individuals set of individuals to be evaluated
     * @param data training sets
     * @param validation
     * @param validationPercentage percentage used for validation
     * @param additionalInfo any aditional information to be appended to the statistics
     * @throws Exception 
     */
    public static void evaluateIndividuals(ArrayList<MetaIndividual> individuals, String data,  String validation, int numberOfThreads, int timeout, HashMap<String,Double> saveCompTime) throws InterruptedException, Exception {

           
            ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);            
            Collection<Callable<String>> list = new LinkedList<Callable<String>>();
            
            ArrayList<MetaIndividual> evaluateIndividuals = new ArrayList<MetaIndividual>();
            ArrayList<String> uniqueIndividuals = new ArrayList<String>();        
                for (MetaIndividual individual : individuals) {
                    String individualName = individual.getM_individualInString();//Arrays.toString(individual.getClassifierName());   
//                    System.out.println("teste:"+individualName );
                    if(!uniqueIndividuals.contains(individualName)){
                        evaluateIndividuals.add(individual);
                        uniqueIndividuals.add(individualName);
                    }       
                }  
            
            
            
            
            
            // Initialize all lists
            for (MetaIndividual individual : evaluateIndividuals) {
                individual.reset();
                list.add(new ProcessedIndividual(individual, data, validation, timeout, saveCompTime));        
            }
            
            
        try {
            List<Future<String>> futures = executor.invokeAll(list, 999999999, TimeUnit.SECONDS);
            for (Future<?> fut : futures) {
                try {
                    fut.get();
                } catch (ExecutionException | CancellationException ex) {
                    ex.printStackTrace();
                    System.out.println("matando por: " + ex);
                    fut.cancel(true);
                    list.clear();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
      
        
            System.gc();
            
        System.gc();
        for (MetaIndividual evaluatedInd : evaluateIndividuals) {  
            String realEvalInd = evaluatedInd.getM_individualInString();//Arrays.toString(evaluatedInd.getClassifierName());
            for (MetaIndividual individual : individuals) {
                String realInd = individual.getM_individualInString();//Arrays.toString(individual.getClassifierName());
                if(realInd.equals(realEvalInd)){
                    individual.setM_validationEvaluation(evaluatedInd.getM_validationEvaluation());
//                     individual.m_validationsEvaluations = evaluatedInd.m_validationsEvaluations;
//                    individual.setToValueEvaluation(evaluatedInd.getEvaluation());
//                    individual.m_fitness = evaluatedInd.m_fitness;
                }
            }

        }
               
            
            System.gc();
            
            for(MetaIndividual individual : individuals){
                    try {
                        individual.setEvaluation();
                    } catch (Exception ex) {
                        Logger.getLogger(MetaIndividual.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
          
            
            executor.shutdownNow();
            executor.shutdown(); 


  
        
        

 
}
    
    
    public synchronized Object [] invertFunctionalities(String [] individualName){
         String [] classifierName = individualName;
         String classifierNameInString = "";
         for(String s : classifierName){
             classifierNameInString += s + " ";
         }
        
        if (classifierNameInString.contains("meka.classifiers.multilabel.meta")) {
            ArrayList<String> part1 = new ArrayList<String>();
            ArrayList<String> part2 = new ArrayList<String>();
            ArrayList<String> part3 = new ArrayList<String>();
            boolean isOK = false;
            int i = 0;
            
            for (int j = i; j < classifierName.length; j++) {
                if (( !classifierName[j+1].startsWith("meka.classifiers.multilabel."))) {
                    part3.add(classifierName[j]);
                    i++;
                } else if ((classifierName[j].equals("-W")  && classifierName[j+1].startsWith("meka.classifiers.multilabel."))) {
                    break;
                }
            }

            for (int j = i ; j < classifierName.length; j++) {
                if (!classifierName[j].startsWith("meka.classifiers.multilabel.meta")) {
                    part2.add(classifierName[j]);
                    i++;
                } else {
                    break;
                }
            }

            for (int j = i; j < classifierName.length; j++) {
                part1.add(classifierName[j]);
            }        
            
            part1.addAll(part2);
            part1.addAll(part3);

            return part1.toArray();
        }else{
            
            ArrayList<String> part1 = new ArrayList<String>();
            ArrayList<String> part2 = new ArrayList<String>();

            int i = 0;

            for (String s : classifierName) {
                if (!s.startsWith("meka.classifiers.multilabel.")) {
                    part2.add(s);
                    i++;
                } else {
                    break;
                }
            }

            for (int j = i; j < classifierName.length; j++) {
                part1.add(classifierName[j]);

            }

            part1.addAll(part2);
            return part1.toArray();         
            
        }
         

        
    }
    
    
     /**
     * Evaluates the algorithm
     * @param trainingSet training set (train + validation)
     * @param validationPercentage percentage utilised to validation
     * @param additionalInfo additional information to be appended to the statistics.
     * @throws Exception 
     */
    
    public synchronized void EvaluateAlgorithm(MetaIndividual individual, String [] individualName, String trainingSet, String validationSet, int timeout) throws IOException, Exception{
        String[] commandLine = null;
        Object [] classifierName = invertFunctionalities(individualName);
        String command = "";   
        boolean isFirstW = false;
        
        
        command  = "timeout " +timeout+"s java -Xmx2g -cp weka.jar:meka.jar";
        
        for(Object o : classifierName){
            String s = (String) o;
            if(s.equals("-W") && (!isFirstW)){
                command  +=  " -t " + trainingSet +" -T " +validationSet + " -verbosity 7 " + s;                            
                isFirstW = true;
            }else{
                command  += " " + s;
            }
        }
                    
        if(!command .contains("-W")){
            command  +=  " -t " + trainingSet +" -T " +validationSet + " -verbosity 7";                                              
        }
                    
//        System.out.println("COMANDO:" + command );
        
//        command = "#" + command;

//        command = "timeout 300s java -Xmx1g";

        commandLine = CommandLine.parse(command.split(" "));
        ProcessBuilder pb = new ProcessBuilder(commandLine);
        pb.redirectErrorStream(true);
//
//        /* Start the process */
        Process proc = pb.start();
//      System.out.println("Process started !");

        /* Read the process's output */
        String line;             
        BufferedReader in = new BufferedReader(new InputStreamReader(
        proc.getInputStream())); 

//        String outTraining = "";
//        double outValidAcc = 0.0;
        double outValidF1 = 0.0;
        double outValidEM = 0.0;
        
        double outValidHL = 1.0;
        double outValidOE = 1.0;
        double outValidRL = 1.0;
        
//        boolean validAcc = false;
        boolean validF1 = false;        
        boolean validEM = false;
        boolean validHL = false;
        
//        boolean validOE = false;
        boolean validRL = false;  

        while ((line = in.readLine()) != null) { 
//            System.out.println(line);
           if(line.startsWith("Exact-match-test")){
                outValidEM = Double.parseDouble(line.split("=")[1]);
                validEM = true;
//                System.out.println("EM:"+outValidEM);
            }else if(line.startsWith("Hamming-loss-test")){
                outValidHL = Double.parseDouble(line.split("=")[1]);
                validHL = true;
//                System.out.println("HL:"+outValidHL);
            }else if(line.startsWith("Rank-loss-test")){
                outValidRL= Double.parseDouble(line.split("=")[1]);
                validRL = true;
//                System.out.println("RL:"+outValidRL);
            }else if(line.startsWith("F1-macro-averaged-by-label-test")){
                outValidF1= Double.parseDouble(line.split("=")[1]);
                validF1 = true;
//                System.out.println("F1:"+outValidF1);
            }        
        }
        

        
//        
//        
//        m_validationsEvaluations = new LinkedList<Double>();
        this.m_validationEvaluation = 0.0;
        double fitness = 0.0;
        if(validEM && validHL && validRL && validF1){
            fitness = outValidEM + (1.0 - outValidHL) + (1.0 - outValidRL) + outValidF1;
            fitness = fitness/4.0;
//            System.out.println("Fitness: "+fitness);
            this.m_validationEvaluation = fitness;        
        }else{
            this.m_validationEvaluation = 0.0;  
        }
//        System.out.println("fitness: "+fitness);

        
        
        in.close();
        /* Clean-up */
        proc.destroy();
        System.gc();

    }

    public String getM_individualInString() {
        return this.m_individualInString;
    }

    public String[] getM_individualNameVector() {
        return this.m_individualNameVector;
    }
    
    
    
    
    
     /**
     * Evaluates the algorithm
     * @param algorithmName
     * @param individual
     * @param trainingSet training set (train + validation)
     * @param evaluationSet percentage utilised to validation
     * @param additionalInfo additional information to be appended to the statistics.
     * @return 
     * @throws java.io.IOException 
     * @throws Exception 
     */
    
    public synchronized ResultsEval EvaluateAlgorithmOnTest(String [] algorithmName, 
                                                        String trainingSet, String evaluationSet) throws IOException, Exception{
        double accuracy_Training= 0.0;
        double hammingScore_Training= 0.0;
        double exactMatch_Training= 0.0;
        double jaccardDistance_Training= 0.0;
        double hammingLoss_Training= 0.0;
        double zeroOneLoss_Training= 0.0;
        double harmonicScore_Training= 0.0;
        double oneError_Training= 0.0;
        double rankLoss_Training= 0.0;
        double avgPrecision_Training= 0.0;
        double microPrecision_Training= 0.0;
        double microRecall_Training= 0.0;
        double macroPrecision_Training= 0.0;
        double macroRecall_Training= 0.0;
        double f1MicroAveraged_Training= 0.0;
        double f1MacroAveragedExample_Training= 0.0;
        double f1MacroAveragedLabel_Training= 0.0;
        double aurcMacroAveraged_Training= 0.0;
        double aurocMacroAveraged_Training= 0.0;
        double emptyLabelvectorsPredicted_Training= 0.0;
        double labelCardinalityPredicted_Training= 0.0;
        double levenshteinDistance_Training= 0.0;
        double labelCardinalityDifference_Training= 0.0;
        
        double accuracy_Evaluation= 0.0;        
        double hammingScore_Evaluation= 0.0;     
        double exactMatch_Evaluation= 0.0;        
        double jaccardDistance_Evaluation= 0.0;
        double hammingLoss_Evaluation= 0.0;        
        double zeroOneLoss_Evaluation= 0.0;        
        double harmonicScore_Evaluation= 0.0;        
        double oneError_Evaluation= 0.0;        
        double rankLoss_Evaluation= 0.0;        
        double avgPrecision_Evaluation= 0.0;        
        double microPrecision_Evaluation= 0.0;        
        double microRecall_Evaluation= 0.0;        
        double macroPrecision_Evaluation= 0.0;        
        double macroRecall_Evaluation= 0.0;        
        double f1MicroAveraged_Evaluation= 0.0;        
        double f1MacroAveragedExample_Evaluation= 0.0;        
        double f1MacroAveragedLabel_Evaluation= 0.0;        
        double aurcMacroAveraged_Evaluation= 0.0;        
        double aurocMacroAveraged_Evaluation= 0.0;      
        double emptyLabelvectorsPredicted_Evaluation= 0.0;        
        double labelCardinalityPredicted_Evaluation= 0.0;        
        double levenshteinDistance_Evaluation= 0.0;        
        double labelCardinalityDifference_Evaluation= 0.0;    
        
        
        String[] commandLine = null;
        Object [] classifierName = invertFunctionalities(algorithmName);
        String command = "";   
        boolean isFirstW = false;      
        
        command  = "java -Xmx10g -cp weka.jar:meka.jar";
        
        for(Object o : classifierName){
            String s = (String) o;
            if(s.equals("-W") && (!isFirstW)){
                command  +=  " -t " + trainingSet +" -T " + evaluationSet + " -verbosity 6 " + s;                            
                isFirstW = true;
            }else{
                command  += " " + s;
            }
        }
                    
        if(!command .contains("-W")){
            command  +=  " -t " + trainingSet +" -T " + evaluationSet + " -verbosity 6";                                              
        }
                    
        System.out.println("TEST COMMAND:" + command );
        commandLine = CommandLine.parse(command.split(" "));
        ProcessBuilder pb = new ProcessBuilder(commandLine);
        pb.redirectErrorStream(true);
//        /* Start the process */
        Process proc = pb.start();

        /* Read the process's output */
        String line;             
        BufferedReader in = new BufferedReader(new InputStreamReader(
        proc.getInputStream())); 

                 
        while ((line = in.readLine()) != null) {   
//            System.out.println(line);
                if (line.startsWith("Accuracy-training")) {
                    accuracy_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Hamming-score-training")) {
                    hammingScore_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Exact-match-training")) {
                    exactMatch_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Jaccard-distance-training")) {
                    jaccardDistance_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Hamming-loss-training")) {
                    hammingLoss_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("ZeroOne-loss-training")) {
                    zeroOneLoss_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Harmonic-score-training")) {
                    harmonicScore_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("One-error-training")) {
                    oneError_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Rank-loss-training")) {
                    rankLoss_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Avg-precision-training")) {
                    avgPrecision_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Micro-Precision-training")) {
                    microPrecision_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Micro-Recall-training")) {
                    microRecall_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Macro-Precision-training")) {
                    macroPrecision_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Macro-Recall-training")) {
                    macroRecall_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("F1-micro-averaged-training")) {
                    f1MicroAveraged_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("F1-macro-averaged-by-example-training")) {
                    f1MacroAveragedExample_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("F1-macro-averaged-by-label-training")) {
                    f1MacroAveragedLabel_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("AUPRC-macro-averaged-training")) {
                    aurcMacroAveraged_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("AUROC-macro-averaged-training")) {
                    aurocMacroAveraged_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Empty-labelvectors-predicted-training")) {
                    emptyLabelvectorsPredicted_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Label-cardinality-predicted-training")) {
                    labelCardinalityPredicted_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Levenshtein-distance-training")) {
                    levenshteinDistance_Training = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Label-cardinality-difference-training")) {
                    labelCardinalityDifference_Training = Double.parseDouble(line.split("=")[1]);

                } else if (line.startsWith("Accuracy-test")) {
                    accuracy_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Hamming-score-test")) {
                    hammingScore_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Exact-match-test")) {
                    exactMatch_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Jaccard-distance-test")) {
                    jaccardDistance_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Hamming-loss-test")) {
                    hammingLoss_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("ZeroOne-loss-test")) {
                    zeroOneLoss_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Harmonic-score-test")) {
                    harmonicScore_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("One-error-test")) {
                    oneError_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Rank-loss-test")) {
                    rankLoss_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Avg-precision-test")) {
                    avgPrecision_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Micro-Precision-test")) {
                    microPrecision_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Micro-Recall-test")) {
                    microRecall_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Macro-Precision-test")) {
                    macroPrecision_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Macro-Recall-test")) {
                    macroRecall_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("F1-micro-averaged-test")) {
                    f1MicroAveraged_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("F1-macro-averaged-by-example-test")) {
                    f1MacroAveragedExample_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("F1-macro-averaged-by-label-test")) {
                    f1MacroAveragedLabel_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("AUPRC-macro-averaged-test")) {
                    aurcMacroAveraged_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("AUROC-macro-averaged-test")) {
                    aurocMacroAveraged_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Empty-labelvectors-predicted-test")) {
                    emptyLabelvectorsPredicted_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Label-cardinality-predicted-test")) {
                    labelCardinalityPredicted_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Levenshtein-distance-test")) {
                    levenshteinDistance_Evaluation = Double.parseDouble(line.split("=")[1]);
                } else if (line.startsWith("Label-cardinality-difference-test")) {
                    labelCardinalityDifference_Evaluation = Double.parseDouble(line.split("=")[1]);
                }
            
            
        }
        
//accuracy_Training,accuracy_Evaluation, 
//hammingScore_Training,hammingScore_Evaluation,
//exactMatch_Training,exactMatch_Evaluation, 
//jaccardDistance_Training,jaccardDistance_Evaluation,
//hammingLoss_Training,hammingLoss_Evaluation,  
//zeroOneLoss_Training,zeroOneLoss_Evaluation, 
//harmonicScore_Training,harmonicScore_Evaluation,   
//oneError_Training,oneError_Evaluation,   
//rankLoss_Training,rankLoss_Evaluation, 
//avgPrecision_Training,avgPrecision_Evaluation,
//microPrecision_Training,microPrecision_Evaluation, 
//microRecall_Training,microRecall_Evaluation,
//macroPrecision_Training,macroPrecision_Evaluation, 
//macroRecall_Training,macroRecall_Evaluation,  
//f1MicroAveraged_Training,f1MicroAveraged_Evaluation, 
//f1MacroAveragedExample_Training,f1MacroAveragedExample_Evaluation,
//f1MacroAveragedLabel_Training,f1MacroAveragedLabel_Evaluation,  
//aurcMacroAveraged_Training,aurcMacroAveraged_Evaluation,  
//aurocMacroAveraged_Training,aurocMacroAveraged_Evaluation, 
//emptyLabelvectorsPredicted_Training,emptyLabelvectorsPredicted_Evaluation,
//labelCardinalityPredicted_Training,labelCardinalityPredicted_Evaluation,  
//levenshteinDistance_Training,levenshteinDistance_Evaluation,     
//labelCardinalityDifference_Training,labelCardinalityDifference_Evaluation,   




        ResultsEval rtest = new ResultsEval(
                accuracy_Training, accuracy_Evaluation,
                hammingScore_Training, hammingScore_Evaluation,
                exactMatch_Training, exactMatch_Evaluation,
                jaccardDistance_Training, jaccardDistance_Evaluation,
                hammingLoss_Training, hammingLoss_Evaluation,
                zeroOneLoss_Training, zeroOneLoss_Evaluation,
                harmonicScore_Training, harmonicScore_Evaluation,
                oneError_Training, oneError_Evaluation,
                rankLoss_Training, rankLoss_Evaluation,
                avgPrecision_Training, avgPrecision_Evaluation,
                microPrecision_Training, microPrecision_Evaluation,
                microRecall_Training, microRecall_Evaluation,
                macroPrecision_Training, macroPrecision_Evaluation,
                macroRecall_Training, macroRecall_Evaluation,
                f1MicroAveraged_Training, f1MicroAveraged_Evaluation,
                f1MacroAveragedExample_Training, f1MacroAveragedExample_Evaluation,
                f1MacroAveragedLabel_Training, f1MacroAveragedLabel_Evaluation,
                aurcMacroAveraged_Training, aurcMacroAveraged_Evaluation,
                aurocMacroAveraged_Training, aurocMacroAveraged_Evaluation,
                emptyLabelvectorsPredicted_Training, emptyLabelvectorsPredicted_Evaluation,
                labelCardinalityPredicted_Training, labelCardinalityPredicted_Evaluation,
                levenshteinDistance_Training, levenshteinDistance_Evaluation,
                labelCardinalityDifference_Training, labelCardinalityDifference_Evaluation
        );

        
        
        in.close();
        /* Clean-up */
        proc.destroy();
        System.gc();
        
        return rtest;

    }    
    
    
    
   
            /**
     * Evaluates the algorithm by testing
     * @param trainingSet training set (train + validation)
     * @param validationPercentage percentage utilised to validation
     * @param additionalInfo additional information to be appended to the statistics.
     * @throws Exception 
     */
    public static Results testAlgorithm(MetaIndividual algorithm, 
                                        String fullTrainingSet, String testSet,
                                        String trainingSet, String validationSet) throws Exception{
        String [] classifierName = algorithm.getM_individualNameVector();
        //Results on Training (part of the Full-training) and Validation:
        ResultsEval rValid = algorithm.EvaluateAlgorithmOnTest(classifierName, trainingSet, validationSet);
        //Results on Full-training and Test:
        ResultsEval rTest = algorithm.EvaluateAlgorithmOnTest(classifierName, fullTrainingSet, testSet);        
        
        double accuracy_FullTraining = rTest.getAccuracy_Training();
        double accuracy_Test = rTest.getAccuracy_Evaluation();
        double accuracy_Training =  rValid.getAccuracy_Training();
        double accuracy_Validation = rValid.getAccuracy_Evaluation();
        
        double hammingScore_FullTraining = rTest.getHammingScore_Training();
        double hammingScore_Test = rTest.getHammingScore_Evaluation();
        double hammingScore_Training = rValid.getHammingScore_Training();
        double hammingScore_Validation = rValid.getHammingScore_Evaluation();
        
        double exactMatch_FullTraining = rTest.getExactMatch_Training();
        double exactMatch_Test = rTest.getExactMatch_Evaluation();        
        double exactMatch_Training = rValid.getExactMatch_Training();
        double exactMatch_Validation = rValid.getExactMatch_Evaluation();
        
        double jaccardDistance_FullTraining = rTest.getJaccardDistance_Training();
        double jaccardDistance_Test = rTest.getJaccardDistance_Evaluation();        
        double jaccardDistance_Training = rValid.getJaccardDistance_Training();
        double jaccardDistance_Validation = rValid.getJaccardDistance_Evaluation();   
        
        double hammingLoss_FullTraining = rTest.getHammingLoss_Training();
        double hammingLoss_Test = rTest.getHammingLoss_Evaluation();       
        double hammingLoss_Training = rValid.getHammingLoss_Training();
        double hammingLoss_Validation = rValid.getHammingLoss_Evaluation();  

        double zeroOneLoss_FullTraining = rTest.getZeroOneLoss_Training();
        double zeroOneLoss_Test = rTest.getZeroOneLoss_Evaluation();        
        double zeroOneLoss_Training = rValid.getZeroOneLoss_Training();
        double zeroOneLoss_Validation = rValid.getZeroOneLoss_Evaluation();
        
        double harmonicScore_FullTraining = rTest.getHarmonicScore_Training();
        double harmonicScore_Test = rTest.getHarmonicScore_Evaluation();
        double harmonicScore_Training = rValid.getHarmonicScore_Training();
        double harmonicScore_Validation = rValid.getHarmonicScore_Evaluation();   
        
        double oneError_FullTraining = rTest.getOneError_Training();
        double oneError_Test = rTest.getOneError_Evaluation();        
        double oneError_Training = rValid.getOneError_Training();
        double oneError_Validation = rValid.getOneError_Evaluation();   
        
        double rankLoss_FullTraining = rTest.getRankLoss_Training();
        double rankLoss_Test = rTest.getRankLoss_Evaluation();        
        double rankLoss_Training = rValid.getRankLoss_Training();
        double rankLoss_Validation = rValid.getRankLoss_Evaluation();   
        
        double avgPrecision_FullTraining = rTest.getAvgPrecision_Training();
        double avgPrecision_Test = rTest.getAvgPrecision_Evaluation();        
        double avgPrecision_Training = rValid.getAvgPrecision_Training();
        double avgPrecision_Validation = rValid.getAvgPrecision_Evaluation();        
        
        double microPrecision_FullTraining = rTest.getMicroPrecision_Training();
        double microPrecision_Test = rTest.getMicroPrecision_Evaluation();        
        double microPrecision_Training = rValid.getMicroPrecision_Training();
        double microPrecision_Validation = rValid.getMicroPrecision_Evaluation(); 
        
        double microRecall_FullTraining = rTest.getMicroRecall_Training();
        double microRecall_Test = rTest.getMicroRecall_Evaluation();        
        double microRecall_Training = rValid.getMicroRecall_Training();
        double microRecall_Validation = rValid.getMicroRecall_Evaluation(); 

        double macroPrecision_FullTraining = rTest.getMacroPrecision_Training();
        double macroPrecision_Test = rTest.getMacroPrecision_Evaluation();        
        double macroPrecision_Training = rValid.getMacroPrecision_Training();
        double macroPrecision_Validation = rValid.getMacroPrecision_Evaluation(); 

        double macroRecall_FullTraining = rTest.getMacroRecall_Training();
        double macroRecall_Test = rTest.getMacroRecall_Evaluation();        
        double macroRecall_Training = rValid.getMacroRecall_Training();
        double macroRecall_Validation = rValid.getMacroRecall_Evaluation(); 

        double f1MicroAveraged_FullTraining = rTest.getF1MicroAveraged_Training();
        double f1MicroAveraged_Test = rTest.getF1MicroAveraged_Evaluation();        
        double f1MicroAveraged_Training = rValid.getF1MicroAveraged_Training();
        double f1MicroAveraged_Validation = rValid.getF1MicroAveraged_Evaluation(); 

        double f1MacroAveragedExample_FullTraining = rTest.getF1MacroAveragedExample_Training();
        double f1MacroAveragedExample_Test = rTest.getF1MacroAveragedExample_Evaluation();        
        double f1MacroAveragedExample_Training = rValid.getF1MacroAveragedExample_Training();
        double f1MacroAveragedExample_Validation = rValid.getF1MacroAveragedExample_Evaluation(); 

        double f1MacroAveragedLabel_FullTraining = rTest.getF1MacroAveragedLabel_Training();
        double f1MacroAveragedLabel_Test = rTest.getF1MacroAveragedLabel_Evaluation();        
        double f1MacroAveragedLabel_Training = rValid.getF1MacroAveragedLabel_Training();
        double f1MacroAveragedLabel_Validation = rValid.getF1MacroAveragedLabel_Evaluation(); 

        double aurcMacroAveraged_FullTraining = rTest.getAurcMacroAveraged_Training();
        double aurcMacroAveraged_Test = rTest.getAurcMacroAveraged_Evaluation();        
        double aurcMacroAveraged_Training = rValid.getAurcMacroAveraged_Training();
        double aurcMacroAveraged_Validation = rValid.getAurcMacroAveraged_Evaluation(); 

        double aurocMacroAveraged_FullTraining = rTest.getAurocMacroAveraged_Training();
        double aurocMacroAveraged_Test = rTest.getAurocMacroAveraged_Evaluation();        
        double aurocMacroAveraged_Training = rValid.getAurocMacroAveraged_Training();
        double aurocMacroAveraged_Validation = rValid.getAurocMacroAveraged_Evaluation();    
        
        double emptyLabelvectorsPredicted_FullTraining = rTest.getEmptyLabelvectorsPredicted_Training();
        double emptyLabelvectorsPredicted_Test = rTest.getEmptyLabelvectorsPredicted_Evaluation();        
        double emptyLabelvectorsPredicted_Training = rValid.getEmptyLabelvectorsPredicted_Training();
        double emptyLabelvectorsPredicted_Validation = rValid.getEmptyLabelvectorsPredicted_Evaluation(); 

        double labelCardinalityPredicted_FullTraining = rTest.getLabelCardinalityPredicted_Training();
        double labelCardinalityPredicted_Test = rTest.getLabelCardinalityPredicted_Evaluation();        
        double labelCardinalityPredicted_Training = rValid.getLabelCardinalityPredicted_Training();
        double labelCardinalityPredicted_Validation = rValid.getLabelCardinalityPredicted_Evaluation(); 

        double levenshteinDistance_FullTraining =  rTest.getLevenshteinDistance_Training();
        double levenshteinDistance_Test = rTest.getLevenshteinDistance_Evaluation();        
        double levenshteinDistance_Training = rValid.getLevenshteinDistance_Training();
        double levenshteinDistance_Validation = rValid.getLevenshteinDistance_Evaluation(); 

        double labelCardinalityDifference_FullTraining = rTest.getLabelCardinalityDifference_Training();
        double labelCardinalityDifference_Test = rTest.getLabelCardinalityDifference_Evaluation();        
        double labelCardinalityDifference_Training = rValid.getLabelCardinalityDifference_Training();
        double labelCardinalityDifference_Validation = rValid.getLabelCardinalityDifference_Evaluation();
        
        Results results = new Results(Arrays.toString(classifierName),
                accuracy_FullTraining,accuracy_Test,accuracy_Training,accuracy_Validation,
                hammingScore_FullTraining, hammingScore_Test, hammingScore_Training, hammingScore_Validation,
                exactMatch_FullTraining, exactMatch_Test, exactMatch_Training, exactMatch_Validation,
                jaccardDistance_FullTraining, jaccardDistance_Test, jaccardDistance_Training, jaccardDistance_Validation,
                hammingLoss_FullTraining, hammingLoss_Test, hammingLoss_Training, hammingLoss_Validation,
                zeroOneLoss_FullTraining, zeroOneLoss_Test, zeroOneLoss_Training, zeroOneLoss_Validation,
                harmonicScore_FullTraining, harmonicScore_Test, harmonicScore_Training, harmonicScore_Validation,
                oneError_FullTraining, oneError_Test, oneError_Training, oneError_Validation,
                rankLoss_FullTraining, rankLoss_Test, rankLoss_Training, rankLoss_Validation,
                avgPrecision_FullTraining, avgPrecision_Test, avgPrecision_Training, avgPrecision_Validation,
                microPrecision_FullTraining, microPrecision_Test, microPrecision_Training, microPrecision_Validation,
                microRecall_FullTraining, microRecall_Test, microRecall_Training, microRecall_Validation,
                macroPrecision_FullTraining, macroPrecision_Test, macroPrecision_Training, macroPrecision_Validation,
                macroRecall_FullTraining, macroRecall_Test, macroRecall_Training, macroRecall_Validation,
                f1MicroAveraged_FullTraining, f1MicroAveraged_Test, f1MicroAveraged_Training, f1MicroAveraged_Validation,
                f1MacroAveragedExample_FullTraining, f1MacroAveragedExample_Test, f1MacroAveragedExample_Training, f1MacroAveragedExample_Validation,
                f1MacroAveragedLabel_FullTraining, f1MacroAveragedLabel_Test, f1MacroAveragedLabel_Training, f1MacroAveragedLabel_Validation,
                aurcMacroAveraged_FullTraining, aurcMacroAveraged_Test, aurcMacroAveraged_Training, aurcMacroAveraged_Validation,
                aurocMacroAveraged_FullTraining, aurocMacroAveraged_Test, aurocMacroAveraged_Training, aurocMacroAveraged_Validation,
                emptyLabelvectorsPredicted_FullTraining, emptyLabelvectorsPredicted_Test, emptyLabelvectorsPredicted_Training, emptyLabelvectorsPredicted_Validation,
                labelCardinalityPredicted_FullTraining, labelCardinalityPredicted_Test, labelCardinalityPredicted_Training, labelCardinalityPredicted_Validation,
                levenshteinDistance_FullTraining, levenshteinDistance_Test, levenshteinDistance_Training, levenshteinDistance_Validation,
                labelCardinalityDifference_FullTraining, labelCardinalityDifference_Test, labelCardinalityDifference_Training, labelCardinalityDifference_Validation
        );

//        results = new Results(algorithm, 0, accuracyTraining, accuracyValidation, accuracyTest, precisionTraining, precisionValidation, precisionTest,
//                                    recallTraining, recallValidation, recallTest, fMeasureTraining, fMeasureValidation, fMeasureTest,
//                                    aucTraining, aucValidation, aucTest, tpTraining, tpValidation, tpTest, tnTraining, tnValidation, tnTest,
//                                    fpTraining, fpValidation, fpTest, fnTraining, fnValidation, fnTest);   
//        System.out.println("DONE-TESTE:" + algorithm.toString());
       

        System.gc();


        return results;
    }
    
    
    public static Instances merge(Instances data1, Instances data2){
        
        Instances data = new Instances(data1);
        
        for(int i=0; i < data2.numInstances(); i++){
            data.add(data2.instance(i));
        }
        
        return data;
        
    }
    
    
    private  static int getNumberOfEdges(Classifier classifier, Instances trainingSet) throws Exception{
        BayesNet bn = (BayesNet) classifier;
        bn.buildClassifier(trainingSet);
        
        int edges = 0;

         for (int nds = 0; nds < bn.getNrOfNodes(); nds++) {
               edges += bn.getParentSet(nds).getNrOfParents();
         }
         
         return edges;
    }
    

    
    /**
     * Returns an evaluation metric about the individual
     * @param fitnessType type of evaluation metric
     * @return avalation metric (fitness)
     * @throws Exception 
     */
    public double getEvaluation() throws Exception{
        return this.m_fitness;
    }
    
    public void setToZeroEvaluation() throws Exception{
        this.m_fitness = 0.0;
    }
    
    public void setToValueEvaluation(double value) throws Exception{
        this.m_fitness = value;
    }
    

   
    public final String [] getClassifierName() throws Exception{        
        String [] classifier = generateClassifier(1);
        
        return classifier;
    }
    
    
    /**
     * Mapping of chromosome array (double) to an array of options (integer)
     * @return array of options
     */
    public int[] ChromosomeMapping(){       
        return ChromosomeMapping(m_individual);
    }
    
    /**
     * Mapping of chromosome array (double) to an array of options (integer)
     * @param individual individual chromosome array
     * @return array of options
     */
    public int[] ChromosomeMapping(double[] individual){
        
        int[] mappedIndividual = new int[individual.length];
        LinkedList<Allele> queue = null;
        LinkedList<Allele> mlLearning = new LinkedList<Allele>();
        
        for (int k=0; k < mappedIndividual.length; k++) {
            mappedIndividual[k] = -1;
        }
        
        for(int s=0; s < m_genome.m_alleleList.get(0).m_alleleList.size(); s++){
            mlLearning.addLast(m_genome.m_alleleList.get(0).m_alleleList.get(s));
        }
        
        int i = 1;
        
        for(Allele g : mlLearning){
            int k = 0;
            queue = new LinkedList<Allele>();
            queue.add(g);
            
            while (!queue.isEmpty()) {
                Allele gene = queue.pop();
//                System.out.println(gene.getParameter() + " -> " + i);
//                if(gene.getParameter().equals("#")){
//                    mappedIndividual[i] = doubleToIntMapping(individual[i], gene);
//                    Allele allele = gene.get(mappedIndividual[i]);
//                    i++;
//                    if (!allele.isEmpty()) {
//                            for (Allele al : allele.m_alleleList) {
//                                queue.add(al);
//                            }
//                        }
//                    continue;
//                }
                

                if (!gene.isEmpty()) {
                    mappedIndividual[i] = doubleToIntMapping(individual[i], gene);  
                    
                    

                    if (!gene.isOption()) {
                        Allele allele = gene.get(mappedIndividual[i]);
                        if (!allele.isEmpty()) {
                            for (Allele al : allele.m_alleleList) {
                                queue.add(al);
                            }
                        }
                    }
                   
                    i++;
                }
            }        
        }
        
//        System.out.println("i: "+ i);
        

        
//        Allele gene = queue.pop();
//        System.out.println(gene.getParameter());
        
//        queue.add(m_genome);
////        System.out.println(m_genome.m_alleleList.get(0).m_alleleList.size());
//


        
        return mappedIndividual;
    }    
    
//    public String getClassifierNameCommon() throws Exception {
//        int[] mappedIndividual = ChromosomeMapping();
//        
//        ArrayList<String> options = new ArrayList<String>();
//        LinkedList<Allele> queue = new LinkedList<Allele>();
//        LinkedList<Allele> mlLearning = new LinkedList<Allele>();
//        
//        for(int s=0; s < m_genome.m_alleleList.get(0).m_alleleList.size(); s++){
//            mlLearning.addLast(m_genome.m_alleleList.get(0).m_alleleList.get(s));
//        }
//        int i = 1;
//        
//       for(Allele g : mlLearning){
//            
//            queue = new LinkedList<Allele>();
//            queue.add(g);
//            
//           while (!queue.isEmpty()) {
//               Allele gene = queue.pop();
////                if(gene.getParameter().equals("#")){
////                    Allele allele = gene.get(mappedIndividual[i]);
////                    i++;
////                    if (!allele.isEmpty()) {
////                            for (Allele al : allele.m_alleleList) {
////                                queue.add(al);
////                            }
////                        }
////                    continue;
////                }               
//
//               if (!gene.getParameter().isEmpty()) {
//                   options.add(gene.getParameter());
//               }
//               if (!gene.isEmpty()) {
//                   options.add(gene.get(mappedIndividual[i]).getParameter());
//                   if (!gene.isOption()) {
//                       Allele allele = gene.get(mappedIndividual[i]);
//                       if (!allele.isEmpty()) {
//                           for (Allele al : allele.m_alleleList) {
//                               queue.add(al);
//                           }
//                       }
//                   }
//                   i++;
//               }
//           }         
//       
//       
//       
//       }
//        StringBuilder strBuilder = new StringBuilder();
//        String aux = "";
//
//        for (int j = 0; j < options.size(); j++) {
////            if (options.get(j).equals("-E weka.classifiers.bayes.net.estimate.SimpleEstimator -- -A")) {
////                aux = options.get(j);
////
////                aux += " " + options.get(j + 1);
////                j = j + 1;
////            } else {
//                if (options.get(j).equals(" ")) {
//                    strBuilder.append(options.get(j)).append("");
//                } else {
//                    strBuilder.append(options.get(j)).append(" ");
//                }
//
////            }
//        }
//        strBuilder.append(aux);
//
//        
//    return strBuilder.toString();
//    }

    private void reset() {
        m_validationEvaluation = 0.0;
    }
    
    public String[] generateClassifier(int name) throws Exception{
        int[] mappedIndividual = ChromosomeMapping();        
       
        ArrayList<String> options = new ArrayList<String>();
        LinkedList<Allele> queue = new LinkedList<Allele>();
        LinkedList<Allele> mlLearning = new LinkedList<Allele>();
        
        for(int s=0; s < m_genome.m_alleleList.get(0).m_alleleList.size(); s++){
            mlLearning.addLast(m_genome.m_alleleList.get(0).m_alleleList.get(s));
        }
        int i = 1;
        
       for(Allele g : mlLearning){
            
            queue = new LinkedList<Allele>();
            queue.add(g);
            
           while (!queue.isEmpty()) {
               Allele gene = queue.pop();
//                if(gene.getParameter().equals("#")){
//                    Allele allele = gene.get(mappedIndividual[i]);
//                    i++;
//                    
//                    options.add(allele.getParameter());
//                    if (!allele.isEmpty()) {
//                            for (Allele al : allele.m_alleleList) {
//                                queue.add(al);
//                            }
//                        }
//                    continue;
//                }               

               if (!gene.getParameter().isEmpty()) {
                   options.add(gene.getParameter());
               }
               if (!gene.isEmpty()) {
                   options.add(gene.get(mappedIndividual[i]).getParameter());
                   if (!gene.isOption()) {
                       Allele allele = gene.get(mappedIndividual[i]);
                       if (!allele.isEmpty()) {
                           for (Allele al : allele.m_alleleList) {
                               queue.add(al);
                           }
                       }
                   }
                   i++;
               }
           }         
       
       
       
       }        



        StringBuilder strBuilder = new StringBuilder();
        String aux = "";
        
        //It starts in 2 because the first position is the global parameter of the Weka BayesNet
        //the second is the grammar which does not need to be mapped in the individual:
        for(int j = 0; j < options.size(); j++){
                    strBuilder.append(options.get(j)).append(" ");


            }
//        }

        strBuilder.append(aux);

        return Utils.splitOptions(strBuilder.toString());
    }

    public Allele getM_genome() {
        return m_genome;
    }
    
    


//    
//    public LinkedList<Double> getTrainingEvaluations(){        
//        return m_trainingEvaluations;
//    }
//    
//    public LinkedList<Double> getValidationsEvaluations(){        
//        return m_validationsEvaluations;
//    }
    
//    private void reset() {
//        m_trainingEvaluations = new LinkedList<Double>();
//        m_validationsEvaluations = new LinkedList<Double>();
//    }

    public static void setRnd(MersenneTwisterFast rnd) {
        MetaIndividual.rnd = rnd;
    }   
    
    public static void resetStatistics(boolean debug){
        if(debug){
            m_statistics = new StringBuffer("Classifier,Dataset,F measure,Recall,Precision,Kappa,Accuracy,Generation\n");
        }
        else{
            m_statistics = new StringBuffer();
        }
    }


        
    public static StringBuffer getStatistics(){
        return m_statistics;
    }
    
    public static void appendToStatistics(String str){
        m_statistics.append(str);
    }
    
    /**
     * Implemented from comparable. Used to sort in descending order.
     * @param other
     * @return 
     */
    @Override
    public int compareTo(MetaIndividual other) {
        if (this.m_fitness < other.m_fitness) {
            return -1;
        }
        if (this.m_fitness > other.m_fitness) {
            return 1;
        }
        return 0;
    }  
    
    
    private static class ProcessedIndividual extends Thread implements Callable<String> {
        protected MetaIndividual individual;
        protected String training;
        protected String validation;
        protected int datasetIndex;
        protected String dir;
        protected int timeout;
        protected HashMap<String,Double> saveCompTime;
        
        
        public ProcessedIndividual(MetaIndividual individual, String training, String validation, int timeout, HashMap<String,Double> saveCompTime) {
            try {
                this.individual = individual;
                this.training =  training;
                this.validation = validation;
                this.timeout = timeout;
                this.saveCompTime = saveCompTime;
                
                
              
            } catch (Exception ex) {
                Logger.getLogger(MetaIndividual.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        

        
              
       
        public String call() throws Exception {
            if(!this.saveCompTime.containsKey(individual.getM_individualInString())){
                individual.EvaluateAlgorithm(individual, individual.getM_individualNameVector(), training, validation, timeout);
            }else{
//                System.out.println("chega aqui para: "+individual.getM_individualInString());
                double measuredFitness = this.saveCompTime.get(individual.getM_individualInString());
                individual.m_validationEvaluation = measuredFitness;
//                System.out.println("fitness salvando o tempo: "+measuredFitness);
//                individual.m_fitness = measuredFitness;
            }              
            //return the thread name executing this callable task
            return individual.getM_individualInString();
        }  
    }
    
    
    

    
    
}
