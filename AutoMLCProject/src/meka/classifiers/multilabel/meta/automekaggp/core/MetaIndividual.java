package meka.classifiers.multilabel.meta.automekaggp.core;
/**
 * Java imports:
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
/**
 * EpochX imports:
 */
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.representation.CandidateProgram;
/**
 * Sun imports:
 */
import sun.tools.jar.CommandLine;


/**
 * MetaIndividual.java -- A class for evaluating the individuals (MLC algorithms)
 * from MEKA.
 * @author alexgcsa
 */
public class MetaIndividual{
   
    /**
     * Evaluate a set of individuals over a set of datasets
     * @param individuals a set of individuals (algorithms) to be evaluated
     * @param learningData the learning data directory, i.e., part of the training data to construct the model.
     * @param validationData the validation data directory, i.e., part of the training data to validate the produced model.
     * @param numberOfThreads the maximum number of threads to run the evaluation of the individuals.
     * @param seed the random seed.
     * @param saveCompTime a map to reduce computational time.
     * @param timeoutLimit a timeout limit for each algorithm.
     * @param experimentName the name of the experiment.
     * @param grammarMode the grammar type.
     * @return an array of evaluated individuals.
     * @throws InterruptedException 
     * @throws Exception 
     */
    public static ArrayList<CandidateProgram> evaluateIndividuals(ArrayList<CandidateProgram> individuals, String learningData, String validationData, int numberOfThreads, long seed, HashMap<String,Double> saveCompTime, int timeoutLimit, String experimentName, String grammarMode) throws InterruptedException, Exception {
        /** List of individuals to be evaluated. Duplicated individuals are evaluated only once. **/
        ArrayList<CandidateProgram> evaluatedIndividuals = new ArrayList<CandidateProgram>();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        /** List of callables. **/
        Collection<Callable<String>> list = new LinkedList<Callable<String>>();
        
        /** It checks for unique individuals. **/
        ArrayList<String> uniqueIndividuals = new ArrayList<String>();        
        for (CandidateProgram individual : individuals) {
            GRCandidateProgram realIndividual = (GRCandidateProgram) individual;
            String realIndividual_grammar = realIndividual.toString();
            if(!uniqueIndividuals.contains(realIndividual_grammar)){
                evaluatedIndividuals.add(individual);
                uniqueIndividuals.add(realIndividual_grammar);
            }
        }       

        /** It initializes the lists lists **/
        for (CandidateProgram individual : evaluatedIndividuals) {
            GRCandidateProgram realIndividual = (GRCandidateProgram) individual;
            /** It adds a new individual to be processed, i.e., to be evaluated by a thread. **/
            list.add(new ProcessedIndividual(realIndividual, learningData, validationData, seed, saveCompTime, timeoutLimit, experimentName, grammarMode));
            System.gc();
        }
        /** It calls all the individuals -- ie, it runs all the individuals. **/
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
        /** As it does not evaluate duplicated individuals, it set the fitness of
         *  the duplicated by the fitness of evaluated individuals.
         **/
        for (CandidateProgram evaluatedInd : evaluatedIndividuals) {  
            GRCandidateProgram realEvalInd = (GRCandidateProgram) evaluatedInd;
            String realEvalInd_grammar = realEvalInd.toString();
            for (CandidateProgram individual : individuals) {
                GRCandidateProgram realInd = (GRCandidateProgram) individual;
                String realInd_grammar = realInd.toString();
                if(realInd_grammar.equals(realEvalInd_grammar)){
                    /** It sets the fitness. **/
                    realInd.setFitnessValue(realEvalInd.getFitnessValue());
                }
            }
        }
        System.gc();
        /** It terminates the execution (executorservice). **/
        executor.shutdownNow();
        executor.shutdown();
        
        /** Return the evaluated individuals. **/
        return individuals;
    }
    
    /**
     * It cleans unnecessary/special characters from the generated command.
     * @param command to be processed.
     * @return an Array containing the processed command.
     */
    public static String[] cleanCommand(String command) throws IOException{
        String[] commandLine = null;
        
        boolean isSMO = false;
        boolean isMetaSLCVarious = false;
        /** The special characters to be processed. **/
        if(command.contains("#")){
            isSMO = true;
        }
        if(command.contains("@")){
            isMetaSLCVarious = true;
        }      
  
        commandLine = CommandLine.parse(command.split(" "));        
        /** It removes the special characters. **/
        if(isMetaSLCVarious){
            for(int s=0; s < commandLine.length; s++){
                if(commandLine[s].contains("@")){
                    commandLine[s] = commandLine[s].replace("@", " ");
                }            
            }                
        }        
        if(isSMO){
            for(int s=0; s < commandLine.length; s++){
                if(commandLine[s].contains("#")){
                    commandLine[s] = commandLine[s].replaceAll("#", " ");
                }            
            }    
        }
        
        /** It returns a clean command line into an array. **/
        return commandLine;
    }

    
    /**
     * It evaluates the algorithm, represented by an indivividual.
     * @param individual the individual/algorithm to be evaluated.
     * @param learningSet the learning set to construct the model on it.
     * @param validationSet the validation set to evaluate the produced model.
     * @param seed the random seed.
     * @param timeoutLimit timeout limit for each evaluated algorithm.
     * @param experimentName the name of the experiment.
     * @param grammarMode the grammar mode.
     * @return the fitness value.
     * @throws IOException
     * @throws Exception 
     */    
    public double evaluateAlgorithm(GRCandidateProgram individual, String learningSet, String validationSet, long seed, int timeoutLimit, String experimentName, String grammarMode) throws IOException, Exception{
        /** The command line to run. **/
        String[] commandLine = null;
        /** It gets the string from grammar. **/
        String grammarInd = individual.toString();
        /** It calls a class to handle the string from the grammar. **/
        AbstractTranslateIndividual translInd = null;
        if(grammarMode.equals("Full") || grammarMode.equals("SimpBO")){
            translInd = new TranslateIndividual(grammarInd, seed);
        }else if(grammarMode.equals("SimpGA")){
            translInd = new SimplifiedGATranslateIndividual(grammarInd, seed);
        }else{
            translInd = new TranslateIndividual(grammarInd, seed);
        } 
        /** It returns the raw command given the string from grammar. **/
        String command = translInd.translate2Command(learningSet, validationSet, timeoutLimit, true);   
        double fitnessValue = 0.0;
        /** It cleans the command. **/
        commandLine = MetaIndividual.cleanCommand(command);
//        System.out.println("command in array: "+Arrays.toString(commandLine));
        
        /** It creates the process. **/
        ProcessBuilder pb = new ProcessBuilder(commandLine);
        pb.redirectErrorStream(true);

        /** It starts the process. **/
        Process proc = pb.start();
        
        /** It reads the process's output. **/
        String line;             
        BufferedReader in = new BufferedReader(new InputStreamReader(
        proc.getInputStream())); 
        StringBuilder sf = new StringBuilder();
        
        /** The metrics of the fitness formula. **/
        double outValidF1 = 0.0;
        double outValidEM = 0.0;        
        double outValidHL = 1.0;
        double outValidRL = 1.0;
        
        /** Boolean variables to control the generated output for the algorithm.. **/
        boolean validF1 = false;        
        boolean validEM = false;
        boolean validHL = false;        
        boolean validRL = false;  
        
        /** It sets the values of the metrics, if possible. **/
        while ((line = in.readLine()) != null) {
             sf.append(line).append("\n");
           if(line.startsWith("Exact-match-test")){
                outValidEM = Double.parseDouble(line.split("=")[1]);
                validEM = true;
            }else if(line.startsWith("Hamming-loss-test")){
                outValidHL = Double.parseDouble(line.split("=")[1]);
                validHL = true;
            }else if(line.startsWith("Rank-loss-test")){
                outValidRL= Double.parseDouble(line.split("=")[1]);
                validRL = true;
            }else if(line.startsWith("F1-macro-averaged-by-label-test")){
                outValidF1= Double.parseDouble(line.split("=")[1]);
                validF1 = true;
            }
        }
        
        /** The fitness is only defined if all the metrics were calculated. **/
        if(validEM && validHL && validRL && validF1){
            double fitness = outValidEM + (1.0 - outValidHL) + (1.0 - outValidRL) + outValidF1;
            fitness = fitness/4.0;
            fitnessValue = fitness;        
        }else{
           /** Otherwise, a file showing the issue is created or appended**/
           String buffer = sf.toString();
           BufferedWriter bfw = null;
           new File("./results-"+ experimentName).mkdir();
           fitnessValue = 0.0;
           if(buffer.isEmpty()){ 
                /** File for complexity issues, ie, the algorithm did not finish with the given timeout limit. **/
                bfw = new BufferedWriter(new FileWriter("results-"+ experimentName +"/TimeoutIssues-"+experimentName+"-"+seed+".csv", true));
                bfw.write("Complexity issues in the algorithm: "+grammarInd + "\n");         
           }else{   
                /** File for general issues with the executed algorithm. **/
                bfw = new BufferedWriter(new FileWriter("results-"+ experimentName +"/GeneralIssues-"+experimentName+"-"+seed+".csv", true));
                bfw.write("Complexity issues in the algorithm: "+grammarInd + "\n"); 
                bfw.write(buffer + "\n");
           }
           bfw.write("=====================================================\n");
           sf = null; 
           bfw.close(); 
        }       
        
        in.close();
        /** Clean-up the process. **/
        proc.destroy();
        System.gc();
        
        /** It returns the fitness value. **/
        return fitnessValue;
    }
    
    
    
    /**
     * It evaluates the algorithm in specific sets.
     * @param command to be run. 
     * @return the partial results.
     * @throws IOException
     */    
    public synchronized static ResultsEval EvaluateAlgorithmOnTest(String command) throws IOException{
        /** Metrics for training/validation. **/
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
        
        /** Metrics for test/validation. **/
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
        
        /** The command line in array to be run. **/
        String[] commandLine = MetaIndividual.cleanCommand(command);
        System.out.println("Comand line on the test phase: "+Arrays.toString(commandLine));    
        
        /** It creates the process. **/
        ProcessBuilder pb = new ProcessBuilder(commandLine);
        pb.redirectErrorStream(true);

        /** It starts the process. **/
        Process proc = pb.start();

        /** It reads the process's output. **/
        String line;             
        BufferedReader in = new BufferedReader(new InputStreamReader(
        proc.getInputStream())); 

        /** It saves all the metrics related to multi-label classification on training/Learning or Testing/Validation. **/         
        while ((line = in.readLine()) != null) {            
            try {
                //Training/Learning.
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
                //Testing/Validation.
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

            } catch (Exception e) {
                System.out.println("Error: " + e);
                System.out.println("Error in the the measure: " + line.split("=")[1]);
            }
            
            
        }
        /** The Results are saved in a specific structure. **/
        ResultsEval rtest = new ResultsEval(commandLine,
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
     * It evaluates the algorithm by testing the method and returning all the metrics.
     * @param bestAlgorithm
     * @param fullTrainingSet the training set to construct the model on it.
     * @param testSet the test set to evaluate the produced model on the training set.
     * @param learningSet the learning set to construct the model.
     * @param validationSet the validation set to evaluate the produced model on the learning set.
     * @param seed the random seed.
     * @param timeoutLimit the timeout limit for each individual.
     * @param grammarMode the grammar mode to define the search.
     * @return a structure containing all the results.
     * @throws Exception 
     */
    public static Results testAlgorithm(CandidateProgram bestAlgorithm, 
                                        String fullTrainingSet, String testSet,
                                        String learningSet, String validationSet, 
                                        long seed, int timeoutLimit, String grammarMode) throws Exception{
        
        String grammarName = ((GRCandidateProgram) bestAlgorithm).toString();
//        TranslateIndividual translInd = new TranslateIndividual(grammarName, seed); 
        AbstractTranslateIndividual translInd = null;
        if(grammarMode.equals("Full") || grammarMode.equals("SimpBO")){
            translInd = new TranslateIndividual(grammarName, seed);
        }else if(grammarMode.equals("SimpGA")){
            translInd = new SimplifiedGATranslateIndividual(grammarName, seed);
        }else{
            translInd = new TranslateIndividual(grammarName, seed);
        } 
        String algorithmForTraining = translInd.translate2Command(learningSet, validationSet, timeoutLimit, true);
        String algorithmForTest = translInd.translate2Command(fullTrainingSet, testSet, timeoutLimit, true);
        
        /** Results on learning (part of the Full-training) and Validation. **/
        ResultsEval rValid = MetaIndividual.EvaluateAlgorithmOnTest(algorithmForTraining);
        /** Results on Full-training and Test. **/
        ResultsEval rTest = MetaIndividual.EvaluateAlgorithmOnTest(algorithmForTest);        
        
        /** All the metrics on the sets are saved. **/
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
        
        /** And recorded in a special structured. **/
        Results results = new Results(grammarName.replace(" ", "_"), rTest.getCommand(),
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

        System.gc();

        return results;
    }
    
    

    
    /**
     * A static class to process each individual.
     */
    private static class ProcessedIndividual extends Thread implements Callable<String> {
        protected GRCandidateProgram individual;
        protected String training;
        protected String validation;
        protected long seed;
        protected HashMap<String,Double> saveCompTime;
        protected int timeoutLimit;
        protected String experimentName;
        protected String grammarMode;
        
        /**
         * 
         * @param individual individual to be evaluated.
         * @param training the training set directory
         * @param validation the validation set directory
         * @param seed the random seed.
         * @param saveCompTime the map to save computational time.
         * @param timeoutLimit the timeout limit.
         * @param experimentName  the name of the experiment.
         */
        public ProcessedIndividual(GRCandidateProgram individual, String training, String validation, long seed, HashMap<String,Double> saveCompTime, int timeoutLimit, String experimentName, String grammarMode) {
            try {
                this.individual = individual;
                this.training =  training;
                this.validation = validation;
                this.seed = seed;
                this.saveCompTime = saveCompTime;
                this.timeoutLimit = timeoutLimit;
                this.experimentName = experimentName; 
                this.grammarMode = grammarMode;
            } catch (Exception ex) {
                Logger.getLogger(MetaIndividual.class.getName()).log(Level.SEVERE, null, ex);
            }
        }     

        
        /**
         * The callable method to perform the evaluation.
         * @return the grammar string representing the individual.
         * @throws Exception 
         */
        @Override
        public String call() throws Exception {
            double fitness = 0.0;
            String grammarInd = this.individual.toString();
            /** It only runs necessary commands. **/
            if(!this.saveCompTime.containsKey(grammarInd)){
                MetaIndividual indEval = new MetaIndividual(); 
                fitness = indEval.evaluateAlgorithm(individual, training, validation, seed, timeoutLimit, experimentName, grammarMode);
            }else{
                /** If the individual is in the map, the process does not need to be started. **/
                fitness = this.saveCompTime.get(grammarInd);
            }       
            /** It sets the fitness value for the individual. **/
            individual.setFitnessValue(fitness);
            
            
            /** return the grammar string for the individual executing this callable task. **/
            return grammarInd;
        }  
    }
    
    

    
    
    
    
 
    
    
}
