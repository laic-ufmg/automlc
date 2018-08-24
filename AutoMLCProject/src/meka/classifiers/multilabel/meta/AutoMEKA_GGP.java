/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package meka.classifiers.multilabel.meta;
/**
 * Java imports:
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.*;
/**
 * MEKA imports:
 */
import meka.classifiers.multilabel.AbstractMultiLabelClassifier;
import meka.classifiers.multilabel.CC;
import meka.classifiers.multilabel.MultiLabelClassifier;
import meka.classifiers.multilabel.meta.automekaggp.core.MetaIndividual;
import meka.classifiers.multilabel.meta.automekaggp.core.Results;
import meka.classifiers.multilabel.meta.automekaggp.core.GrammarDefinition;
import meka.core.MLUtils;
import meka.core.OptionUtils;
/**
 * Mulan imports:
 */
import mulan.data.InvalidDataFormatException;
import mulan.data.IterativeStratification;
import mulan.data.MultiLabelInstances;
/**
 * WEKA imports:
 */
import weka.core.*;
import weka.classifiers.*;
import weka.filters.*;
import weka.filters.unsupervised.instance.RemoveFolds;

/**
 * EpochX imports:
 */
import org.epochx.tools.grammar.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.MersenneTwisterFast;
import org.epochx.gr.op.crossover.WhighamCrossover;
import org.epochx.gr.op.init.GrowInitialiser;
import org.epochx.gr.op.mutation.WhighamMutation;
import org.epochx.gr.representation.GRCandidateProgram;


/**
 * AutoMEKA_GGP.java - A method for selecting and configuring multi-label 
 classification (MLC) algorithm in the MEKA software.
 * 
 * Auto-MEKA uses a grammar-based genetic programming approach (from EpochX)
 * aiming to find the most suitable MLC algorithm for a given dataset of 
 * interest
 *
 * @author Alex G. C. de Sa (alexgcsa@dcc.ufmg.br)
 */
public class AutoMEKA_GGP extends AbstractMultiLabelClassifier implements MultiLabelClassifier {
    
    /** For serialization. */
    private static final long serialVersionUID = -1875298821884012336L;
    
    /** The selected MLC algorithm. */
    protected Classifier bestMLCalgorithm;
   
   /** Crossover rate. */
    protected double m_crossoverRate = 0.90;
    
    /** Mutation rate. */
    protected double m_mutationRate = 0.10;
    
    /** Number of generations. */
    protected int m_numberOfGenerations = 2;
    
    /** Number of generations to resample the data. */
    protected int m_resample = 5;
    
    /** Size of the population. */
    protected int m_populationSize = 15;
    
    /** Size of the tournament. */
    protected int m_tournamentSize = 2;  
    
    /** Size of the elitism. */
    protected int m_elitismSize = 1;   
    
    /** Init of the number of threads. */
    protected int m_numberOfThreads = 1;
    
    /** seed for random number. */
    protected long m_seed = 11321;
    
    /** The directory to save the results. */
    protected String m_savingDirectory = "~/";    
    
    /** Training directory. */
    protected String m_trainingDirectory = "training.arff";
    /** Testing directory. */
    protected String m_testingDirectory = "testing.arff";
    
    /** Init of the fold. */
    protected int m_foldInit = 0;    
    
    /** A Template for Problem Transformations. */
    protected Instances m_InstancesTemplate;
    
    /** Timeout limit in seconds for each algorithm. **/
    protected int m_algorithmTimeLimit = 10;
    
    /** The name of the experiment, which is useful to define the name of the folders and files. **/
    protected String m_experimentName = "experimentABC";
    
    /** To decide if the process will be guided by generations or time**/
    protected boolean m_anytime = false;
    
    /** The time limit for running Auto-MEKA_GGP (in minutes). */
    protected int m_generaltimeLimit = 1;
    
    protected String m_grammarMode = "Full";
    
    
//    
//      /** Whether the classifier is run in debug mode. */
//  protected boolean m_Debug = false;

    public AutoMEKA_GGP(String [] argv) {
        
        this.bestMLCalgorithm = new CC();
        
        for (int i = 0; i < argv.length; i++) {
            if (argv[i].equals("-t")) {
                i++;
                m_trainingDirectory = argv[i];
            } else if (argv[i].equals("-T")) {
                i++;
                m_testingDirectory = argv[i];
            }
        }
    }
  
 //  ########################################################################################################################## 
 //  ###########################################CLASSIFIER OPTIONS#############################################################
 //  ########################################################################################################################## 
   
    /**
     * The name of the method.
     * @return the name of the method.
     */
    @Override
    public String toString() {
		return "Auto-MEKA";
    }
    
    /**
     * Parses a given list of options. 
     * @param options to be parsed.
     * @throws Exception 
     */ 
    @Override
    public void setOptions (String[] options)  throws Exception {
        super.setOptions(options);
        setTournamentSize(OptionUtils.parse(options, "K", 2 ));
        setElitismSize(OptionUtils.parse(options, "V", 1 ));
        setPopulationSize(OptionUtils.parse(options, "P", 10));
        setNumberOfGenerations(OptionUtils.parse(options, "G", 2 ));
        setResample(OptionUtils.parse(options, "R", -1));
        setMutationRate(OptionUtils.parse(options, "M", 0.10));
        setCrossoverRate(OptionUtils.parse(options, "X", 0.90));
        setNumberOfThreads(OptionUtils.parse(options, "N", 1));
        setSeed(OptionUtils.parse(options, "H", 11321));
//        setGrammarDirectory(OptionUtils.parse(options, "A", "Auto-MEKA_Grammar-equal.bnf"));        
        setFoldInit(OptionUtils.parse(options, "Y", 0));
        setAlgorithmTimeLimit(OptionUtils.parse(options, "L", 60));
        setExperimentName(OptionUtils.parse(options, "W", "ExperimentABC"));
        
        setAnytime(Utils.getFlag("C", options));        
        setGeneraltimeLimit(OptionUtils.parse(options, "B", 1));
        setSavingDirectory(OptionUtils.parse(options, "D", "~/"));
        setGrammarMode(OptionUtils.parse(options, "O", "Full"));
    }
    
   /**
    * Gets the current settings of Auto-MEKA.
    * @return an array of strings suitable for passing to setOptions()
    */
    @Override
    public String[] getOptions(){
	List<String> result = new ArrayList<String>();
        OptionUtils.add(result, super.getOptions());
        OptionUtils.add(result, "K", this.getTournamentSize());
        OptionUtils.add(result, "V", this.getElitismSize());
        OptionUtils.add(result, "P", this.getPopulationSize());
        OptionUtils.add(result, "G", this.getNumberGenerations());
        OptionUtils.add(result, "R", this.getResample());
        OptionUtils.add(result, "M", this.getMutationRate());
        OptionUtils.add(result, "X", this.getCrossoverRate());
        OptionUtils.add(result, "N", this.getNumberOfThreads());
        OptionUtils.add(result, "H", this.getSeed());
//        OptionUtils.add(result, "A", this.getGrammarDirectory());
        OptionUtils.add(result, "Y", this.getFoldInit());
        OptionUtils.add(result, "L", this.getAlgorithmTimeLimit()); 
        OptionUtils.add(result, "W", this.getExperimentName());
        OptionUtils.add(result, "B", this.getGeneraltimeLimit()); 
        OptionUtils.add(result, "D", this.getSavingDirectory());
        OptionUtils.add(result, "O", this.getGrammarMode());
        
        if(this.getAnytime()){
            result.add("-C");
        }      
	
	return OptionUtils.toArray(result);
    }

    /**
    * Returns an enumeration describing the available options.
    * @return an enumeration of all the available options.
    *
    **/    
    @Override
    @SuppressWarnings("unchecked")
    public Enumeration<Option> listOptions () {        
        Vector<Option> new_options = new Vector<Option>();  
        
        new_options.addElement(new Option("\n" + crossoverRateTipText(), "X", 1, "-X <value>"));
        new_options.addElement(new Option("\t" + elitismSizeTipText(), "V", 1, "-V <value>"));  
        new_options.addElement(new Option("\t" + foldInitTipText(), "Y", 1, "-Y <value>"));          
//        new_options.addElement(new Option("\t" + grammarDirectoryTipText(), "A", 1, "-A <value>"));
        new_options.addElement(new Option("\t" + mutationRateTipText(), "M", 1, "-M <value>"));       
        new_options.addElement(new Option("\t" + numberOfGenerationsTipText(), "G", 1, "-G <value>"));           
        new_options.addElement(new Option("\t" + numberOfThreadsTipText(), "N", 1, "-N <value>"));  
        new_options.addElement(new Option("\t" + populationSizeTipText(), "P", 1, "-P <value>"));
        new_options.addElement(new Option("\t" + resampleTipText() ,"R", 1, "-R <value>")); 
        new_options.addElement(new Option("\t" + seedTipText(), "H", 1, "-H <value>"));
        new_options.addElement(new Option("\t" + timeoutLimitTipText(), "L", 1, "-L <value>"));            
        new_options.addElement(new Option("\t" + tournamentSizeTipText(), "K", 1, "-K <value>"));         
        new_options.addElement(new Option("\t" + experimentNameTipText() , "W", 1, "-W <value>"));  
        new_options.addElement(new Option("\t" + anytimeTipText() , "C", 0, "-Z"));
        new_options.addElement(new Option("\t" + generalTimeLimitTipText() , "B", 1, "-B <value>"));
        new_options.addElement(new Option("\t" + savingDirectoryTipText(), "D", 1, "-D <value>"));
        new_options.addElement(new Option("\t" + grammarModeTipText(), "O", 1, "-O <value>"));
        OptionUtils.add(new_options, super.listOptions());      
        
        return OptionUtils.toEnumeration(new_options);
    } 
    
 /*  ########################################################################################################################## 
     ###########################################TIP TEXTS - GUI################################################################
     ##########################################################################################################################  */ 

    /**
     * Description of Auto-MEKA to display in the GUI.
     * @return	the description of Auto-MEKA in the GUI.
     */
    @Override
    public String globalInfo() {
        return "Auto-MEKA: A method for selecting and configuring multi-label "
                + "classification algorithm in the MEKA software";
    }    
    /**
     * Tip text for the crossover rate.
     * @return the tip text for the crossover rate.
     */
    public String crossoverRateTipText() {
        return "The crossover rate to be used in the evolutionary process.";
    }
    /**
     * Tip text for the elistism size.
     * @return the tip text for the elistism size.
     */   
    public String elitismSizeTipText() {
        return "The size of the elitism to be used in the evolutionary process.";
    }
    /**
     * Tip text for the fold init.
     * @return the tip text for the fold init.
     */     
    public String foldInitTipText(){
        return "The fold to init the evolutionary process, ie, the part of the dataset to search for the best algorithm.";
    }
    /**
     * Tip text for the grammar directory.
     * @return the tip text for the grammar directory.
     */      
    public String savingDirectoryTipText(){
        return "The directory to save the results of the search process.";
    }    
//    /**
//     * Tip text for the grammar directory.
//     * @return the tip text for the grammar directory.
//     */      
//    public String grammarDirectoryTipText(){
//        return "The directory containing the grammar file, ie, the multi-label classification search space.";
//    }
    /**
     * Tip text for the mutation rate.
     * @return the tip text for the mutation rate.
     */     
    public String mutationRateTipText() {
        return "The mutation rate to be used in the evolutionary process.";
    }
    /**
     * Tip text for the number of generations.
     * @return the tip text for the number of generations.
     */     
    public String numberOfGenerationsTipText() {
        return "The number of generations to be used in the evolutionary process.";
    }
    /**
     * Tip text for the number of threads.
     * @return the tip text for the number of threads.
     */      
    public String numberOfThreadsTipText() {
        return "The number of threads to evaluate the individuals (algorithms).";
    }
    /**
     * Tip text for the population size.
     * @return the tip text for the population size.
     */      
    public String populationSizeTipText() {
        return "The size of the population (number of individuals) in the evolutionary process.";
    } 
    /**
     * Tip text for the resample value.
     * @return the tip text for the resample value.
     */     
    public String resampleTipText() {
        return "It resamples learning and validation sets in each R iterations (-1 means that no resampling will be performed).";
    }    
    /**
     * Tip text for the random seed.
     * @return the tip text for the random seed.
     */      
    public String seedTipText() {
        return "The seed for random number generator.";
    }  
    /**
     * Tip text for the testing directory.
     * @return the tip text for the testing directory.
     */       
    public String testingDirectoryTipText() {
        return "The directory for the testing data.";
    }   
    /**
     * Tip text for the timeout limit.
     * @return the tip text for the timeout limit (in seconds).
     */     
    public String timeoutLimitTipText() {
        return "The time budget in seconds for each individual (algorithm) to be executed.";
    }   
    /**
     * Tip text for the tournament size.
     * @return the tip text for the tournament size.
     */     
    public String tournamentSizeTipText() {
        return "The size of the tournament in the evolutionary process.";
    } 
    /**
     * Tip text for the training directory.
     * @return the tip text for the training directory.
     */     
    public String trainingDirectoryTipText() {
        return "The directory for the training data.";
    }  
    
    /**
     * Tip text for the experiment name.
     * @return the tip text for the name of the experiment.
     */     
    public String experimentNameTipText() {
        return "The name of the experiment, which is useful to define the name of the folders and files.";
    } 
    /**
     * Tip text for the anytime behavior.
     * @return the tip text for the anytime behavior.
     */     
    public String anytimeTipText() {
        return "If true, it defines the anytime behavior, instead of the generational.";
    }  
    /**
     * Tip text for the general time limit.
     * @return the tip text for the general time limit.
     */     
    public String generalTimeLimitTipText() {
        return "It defines the general time limit (used only if anytime is set to true).";
    }    
    /**
     * Tip text for the general time limit.
     * @return the tip text for the general time limit.
     */     
    public String grammarModeTipText() {
        return "It defines the grammar mode, i.e., which grammar the GGP will use to guide its search.";
    }     
    
    
 /*  ########################################################################################################################## 
     ###########################################GETTERS######################################################################## 
     ########################################################################################################################## 
 */  

    
    /**
     * Getter for the fold init.
     * @return the fold init.
     */
    public int getFoldInit() {
        return m_foldInit;
    }
    /**
     * Getter for the crossover rate.
     * @return the crossover rate.
     */
    public double getCrossoverRate() {
        return m_crossoverRate;
    }    
    /**
     * Getter for the mutation rate.
     * @return the mutation rate.
     */    
    public double getMutationRate() {
        return m_mutationRate;
    }
    /**
     * Getter for the number of generations.
     * @return the number of generations.
     */    
    public int getNumberGenerations() {
        return m_numberOfGenerations;
    }
    /**
     * Getter for resampling in R iterations.
     * @return the value of resampling, ie, the number of generations to wait
     *         to apply the resample.
     */ 
    public int getResample() {
        return m_resample;
    }
    /**
     * Getter for the population size.
     * @return the population size.
     */ 
    public int getPopulationSize() {
        return m_populationSize;
    }
    /**
     * Getter for the tournament size.
     * @return the tournament size.
     */ 
    public int getTournamentSize() {
        return m_tournamentSize;
    }
    /**
     * Getter for the elitism size.
     * @return the elitism size.
     */ 
    public int getElitismSize() {
        return m_elitismSize;
    }    
    /**
     * Getter for the number of threads.
     * @return the number of threads.
     */ 
    public int getNumberOfThreads() {
        return m_numberOfThreads;
    }
    /**
     * Getter for the random seed.
     * @return the number of threads.
     */ 
    public long getSeed() {
        return m_seed;
    }
    /**
     * Getter for the saving directory.
     * @return the saving directory.
     */ 
    public String getSavingDirectory() {
        return m_savingDirectory;
    }    
//    /**
//     * Getter for the grammar directory.
//     * @return the grammar directory.
//     */ 
//    public String getGrammarDirectory() {
//        return m_grammarDirectory;
//    }
    /**
     * Getter for the training directory.
     * @return the training directory.
     */ 
    public String getTrainingDirectory() {
        return m_trainingDirectory;
    }
    /**
     * Getter for the testing directory.
     * @return the testing directory.
     */ 
    public String getTestingDirectory() {
        return m_testingDirectory;
    }
    /**
     * Getter for the timeout limit (in seconds) for each individual.
     * @return the timeout limit (in seconds) for each individual.
     */ 
    public int getAlgorithmTimeLimit() {
        return m_algorithmTimeLimit;
    }    
    /**
     * Getter for the name of the experiment.
     * @return the experiment name.
     */
    public String getExperimentName(){
        return m_experimentName;
    }    
    /**
     * Getter for the anytime behavior.
     * @return the type of behavior:anytime or generational.
     */
    public boolean getAnytime() {
        return m_anytime;
    }
    /**
     * Getter for the general timeout (in minutes).
     * @return the general timeout (in minutes).
     */
    public int getGeneraltimeLimit() {
        return m_generaltimeLimit;
    }
    /**
     * Getter for the grammar mode.
     * @return the grammar mode.
     */
    public String getGrammarMode() {
        return m_grammarMode;
    }
    
    
    
 /*  ########################################################################################################################## 
     ###########################################SETTERS######################################################################## 
     ########################################################################################################################## 
 */  
    
    /**
     * Setter for the name of the experiment..
     * @param experimentName the actual value for the experiment name.
     */
    public void setExperimentName(String experimentName){
        this.m_experimentName = experimentName;
    }
    /**
     * Setter for the fold init.
     * @param foldInit the actual value for the fold init.
     */
    public void setFoldInit(int foldInit) {
        this.m_foldInit = foldInit;
    }
    /**
     * Setter for the crossover rate.
     * @param crossoverRate the actual value for the crossover rate.
     */
    public void setCrossoverRate(double crossoverRate) {
        this.m_crossoverRate = crossoverRate;
    }
    /**
     * Setter for the mutation rate.
     * @param mutationRate the actual value for the mutation rate.
     */
    public void setMutationRate(double mutationRate) {
        this.m_mutationRate = mutationRate;
    }
    /**
     * Setter for the number of generations.
     * @param numberOfGenerations the actual value for the number of generations.
     */
    public void setNumberOfGenerations(int numberOfGenerations) {
        this.m_numberOfGenerations = numberOfGenerations;
    }
    /**
     * Setter for the resample value.
     * @param resample the actual value for the resample.
     */
    public void setResample(int resample) {
        this.m_resample = resample;
    }
    /**
     * Setter for the population size.
     * @param populationSize the actual value for the population size.
     */
    public void setPopulationSize(int populationSize) {
        this.m_populationSize = populationSize;
    }
    /**
     * Setter for the tournament size.
     * @param tournamentSize the actual value for the tournament size.
     */
    public void setTournamentSize(int tournamentSize) {
        this.m_tournamentSize = tournamentSize;
    }
    /**
     * Setter for the elitism size.
     * @param elitismSize the actual value for the elistism size.
     */
    public void setElitismSize(int elitismSize) {
        this.m_elitismSize = elitismSize;
    }
    /**
     * Setter for the number of threads.
     * @param numberOfThreads the actual value for the number of threads.
     */
    public void setNumberOfThreads(int numberOfThreads) {
        this.m_numberOfThreads = numberOfThreads;
    }
    /**
     * Setter for random seed.
     * @param seed the actual value for the random seed.
     */
    public void setSeed(long seed) {
        if(seed >= 0){
            this.m_seed = seed;
        }else{
            this.m_seed = 123;
        }        
    }
    /**
     * Setter for directory to save the results.
     * @param savingDirectory the actual value for the saving directory.
     */
    public void setSavingDirectory(String savingDirectory) {
        this.m_savingDirectory = savingDirectory;
    }    
//    /**
//     * Setter for the grammar directory containing the multi-label search space..
//     * @param grammarDirectory the actual value for the grammar directory.
//     */
//    public void setGrammarDirectory(String grammarDirectory) {
//        this.m_grammarDirectory = grammarDirectory;
//    }

    /**
     * Setter for the timeout limit (in seconds).
     * @param algorithmTimeLimit the actual value timeout limit (in seconds) for each individual.
     */
    public void setAlgorithmTimeLimit(int algorithmTimeLimit) {
        this.m_algorithmTimeLimit = algorithmTimeLimit;
    }  
    /**
     * Setter for the anytime behavior.
     * @param anytime it decides if the anytime behavior will be performed instead of the generational..
     */
    public void setAnytime(boolean anytime) {
        this.m_anytime = anytime;
    }  
    /**
     * Setter for the general timeout limit (in minutes).
     * @param generaltimeLimit the actual value timeout limit (in seconds) for the whole evolutionary process.
     */
    public void setGeneraltimeLimit(int generaltimeLimit) {
        this.m_generaltimeLimit = generaltimeLimit;
    }

    public void setGrammarMode(String grammarMode) {
        if(grammarMode.equals("Full") || grammarMode.equals("SimpGA") || grammarMode.equals("SimpBO") ){
            this.m_grammarMode = grammarMode;
        }else{
            this.m_grammarMode = "Full";
        }
        
    }
    
    
    
    
 /*  ########################################################################################################################## 
     ###########################################CLASSIFIERS METHODS############################################################ 
     ########################################################################################################################## 
 */        
    
    /**
     * It builds Auto-WEKA, by selecting and configuring the MLC algorithms
     * for this data.
     * 
     * @param data - The instances to be used.
     * @throws Exception - To inform any exception in the code.
     */
    @Override
    public void buildClassifier(Instances data) throws Exception {        
        train(data);        
    }

    /**
     * It runs the training (evolution) of Auto-MEKA and produces the final classification
     * algorithm to be evaluated
     *
     * @param data - The instances to be used.
     * @throws Exception  - To inform any exception in the code.
     */
    private void train(Instances data) throws Exception {
        //Starting the process...
        long startTime = System.currentTimeMillis();        
        //For logging the actual generation and the search time.        
        int actualGeneration = -1;
        long searchTime = -1;
        //Number of evaluations.
        int numbOfEval = 0;
        //General time limit in miliseconds to be more precise.
        long timeLimitMilSec = this.getGeneraltimeLimit() * 60 * 1000;
        //Used to calculate de diff time.        
        long currentAnyTime = 0;
        long diffAnyTime = 0;  

        //It defines the random number generator.
        MersenneTwisterFast rng = new MersenneTwisterFast(this.getSeed());
        
        //String Buffer to save the results.
        StringBuilder strB = new StringBuilder();
        //Training and validation directories.
        String[] learningANDvalidationDataDir = new String[2];
        
        //Variables to measure the worst, the best and the average fitness values.
        double worstFitness = 0.0;
        double bestFitness = 0.0;
        double avgFitness = 0.0; 
        
        //Map to save computational time.
        HashMap<String,Double> saveCompTime = new HashMap<String,Double>();
        
        /** Number of attributes and labels. */
        String[] dataOpts = MLUtils.getDatasetOptions(data);        
        int n_labels = Integer.parseInt(dataOpts[dataOpts.length - 1]);
        if(n_labels < 0){
            n_labels *= -1;
        }         
        int n_attributes = data.numAttributes() - data.classIndex();  
        
        
        //logging...
        strB.append(0).append(";");         
        
        //It is used to change the seed, when resampling is performed.
        long usedSeed = 0;        
        //It is used to save the best of the generations.
        ArrayList<CandidateProgram> bestOfTheGenerations = new ArrayList<CandidateProgram>();
            
        
        GrammarDefinition grammarDef = new GrammarDefinition(this.getGrammarMode());
        //It defines the grammar.
        Grammar grammar = new Grammar(grammarDef.getGrammar());
        //For initializing the population.
        GrowInitialiser initPop = new GrowInitialiser(rng, grammar, this.getPopulationSize(), 50, false, n_labels, n_attributes);
        //It creates the initial population (zero) in accordance to the grammar. 
        ArrayList<CandidateProgram> population = new ArrayList<CandidateProgram>(initPop.getInitialPopulation());       
        
        //It creates the learning and the validation sets. **/
        learningANDvalidationDataDir = splitDataInAStratifiedWay(usedSeed, this.getFoldInit(), n_labels);
        usedSeed++;
        
        //Generation iterator.
        int generation = 0;
        //If it is using anytime, the number of generations is infinitive.
        if(this.getAnytime()){            
            this.setNumberOfGenerations(Integer.MAX_VALUE);
        }
        //It executes for a number of generations. 
        for (generation = 0; generation <= this.getNumberGenerations(); generation++) {     
            System.out.println("Generation: "+generation);
            System.out.println("GGP:" + this.getGrammarMode());
            //Resampling data every m_resample generations. 
            if ((this.getResample() > 0) && (generation % this.getResample() == 0) && (generation > 0)) {
                learningANDvalidationDataDir = splitDataInAStratifiedWay(usedSeed, this.getFoldInit(), n_labels);
                saveCompTime = new HashMap<String,Double>();
                usedSeed++;
            }  
            
            //Evaluate the population to get the fitness curve for each generation. 
            ArrayList<CandidateProgram> populationAux = new ArrayList<CandidateProgram>(population);            
            
            System.out.println("Evaluating...");  
            //It evaluates the individuals.
            MetaIndividual.evaluateIndividuals(populationAux, learningANDvalidationDataDir[0], learningANDvalidationDataDir[1], this.getNumberOfThreads(), this.getSeed(), saveCompTime, this.getAlgorithmTimeLimit(), this.getExperimentName(), this.getGrammarMode());
            //It sorts the individuals given the fitness value. 
            Collections.sort(populationAux);
            
            //It updates the current time to verify if the timeout was achieved.
            currentAnyTime = System.currentTimeMillis();
            diffAnyTime =  currentAnyTime - startTime;
            
            //It tests if the timeout was reached or if we can continue with the generational process. 
            //We run at least the first generation
            if( ( ((diffAnyTime <= timeLimitMilSec) || (generation==0)) && (this.getAnytime()) ) || (!this.getAnytime()) ){
                //For logging the statistics.
                numbOfEval+= this.getPopulationSize();
                searchTime = diffAnyTime;
                actualGeneration = generation;                              
                bestFitness = ((GRCandidateProgram) populationAux.get(populationAux.size()-1)).getFitnessValue();
                worstFitness = ((GRCandidateProgram) populationAux.get(0)).getFitnessValue();
                avgFitness = this.getAvgFitness(populationAux);        
                strB.append(worstFitness).append(";").append(avgFitness).append(";").append(bestFitness).append("\n");
                strB.append(generation+1).append(";").append(";");              
                //Saving the best of the generations.
                bestOfTheGenerations.add(populationAux.get(populationAux.size()-1));   
                //Printing the population and the individuals' fitness values.
                System.out.println("#Population#");
                for(CandidateProgram cp : populationAux){
                    GRCandidateProgram gcp = (GRCandidateProgram) cp;
                    String gcp_grammar = gcp.toString();
                    if(!saveCompTime.containsKey(gcp_grammar)){
                        saveCompTime.put(gcp_grammar, gcp.getFitnessValue());
                    }                 
                    System.out.println(gcp_grammar +"=>"+ gcp.getFitnessValue());     
                }            
            }
            if(diffAnyTime > timeLimitMilSec){
                break;
            }
          
            System.out.println("=============================================="); 
            //It defines the new population, by adding firstly the best individuals from the size of the elitism. 
             population = new ArrayList<CandidateProgram>();
             for(int e=0; e < this.getElitismSize(); e++){
                   population.add(populationAux.get(populationAux.size() - 1 - e));
             }
             
             int pop = this.getElitismSize();
             
             //It fulfills the population considering the individuals from elitism.
             while(pop < this.getPopulationSize()){
                //Tournament.
                CandidateProgram parent1 = getParentFromTournament(populationAux, rng);
                CandidateProgram parent2 = getParentFromTournament(populationAux, rng);
                
                //Probabilistic crossover.
                double randomVar = rng.nextDouble();
                CandidateProgram child1 = parent1.clone();
                CandidateProgram child2 = parent2.clone();                
                
                if (randomVar < this.getCrossoverRate()) {
                    WhighamCrossover xover = new WhighamCrossover(rng);
                    CandidateProgram[] xoverprograms = xover.crossover(parent1.clone(), parent2.clone());
                    
                    if (xoverprograms != null) {
                        child1 = xoverprograms[0];
                        child2 = xoverprograms[1];    
                    }               
                }
                //And probabilistic mutation.
                CandidateProgram mchild1 = this.mutate(child1, rng, n_labels, n_attributes);
                CandidateProgram mchild2 = this.mutate(child2, rng, n_labels, n_attributes);
                
                population.add(mchild1);
                pop++;
                if (pop < this.getPopulationSize()) {
                    population.add(mchild2);
                    pop++;
                }      
            }
            //Reset the individuals.
            for(CandidateProgram cp : population){               
                ((GRCandidateProgram) cp).reset();
            }	
            
        } //The end of the evolutionary process.
        
        //It resamples again to check the best individual in the whole evolutionary process.
        usedSeed++;
        learningANDvalidationDataDir = splitDataInAStratifiedWay(usedSeed, this.getFoldInit(), n_labels);
        //It chooses among the best individuals of all generations.
        CandidateProgram m_bestAlgorithm = this.chooseAmongBestAlgorithms(bestOfTheGenerations, usedSeed, learningANDvalidationDataDir);
        numbOfEval+=bestOfTheGenerations.size();
        
        //Measuring the elapsed time to run the GGP.
        long endTime = System.currentTimeMillis();
        long differenceTime = endTime - startTime; 
        System.gc();
        //Saving the results...
        this.savingMLCResults(m_bestAlgorithm, searchTime, differenceTime, actualGeneration, learningANDvalidationDataDir, numbOfEval);    
        //And the log of each generation.
        this.savingLog(strB);
        System.gc();       

            
            
//            System.out.println("teste: "+((GRCandidateProgram)m_bestAlgorithm).toString());
//            TranslateIndividual translInd = new TranslateIndividual(((GRCandidateProgram)m_bestAlgorithm).toString(), this.getSeed());
//            String command = translInd.translate2Command("", "", 0, false); 
//            command = command.replace("-t ", " ");
//            command = command.replace("-T ", " ");
//            command = command.replace("   ", " ");
//            command = command.replace("  ", " ");
//            command = command.replace("  ", " ");
//            
//            String[] commandLine = null;
//            boolean isSMO = false;
//            boolean isMetaSLCVarious = false;
//        
//            if(command.contains("#")){
//                isSMO = true;
//            }
//            if(command.contains("@")){
//                isMetaSLCVarious = true;
//            }      
//      
//            commandLine = CommandLine.parse(command.split(" "));
//        
//        
//            if(isMetaSLCVarious){
//                for(int s=0; s < commandLine.length; s++){
//                    if(commandLine[s].contains("@")){
//                        commandLine[s] = commandLine[s].replace("@", " ");
//                    }            
//                }                
//            }
//        
//            if(isSMO){
//                for(int s=0; s < commandLine.length; s++){
//                    if(commandLine[s].contains("#")){
//                        commandLine[s] = commandLine[s].replaceAll("#", " ");
//                    }            
//                }    
//            }
//            System.out.println("###############comandLineFinal: "+Arrays.toString(commandLine));
//            String[] options = Arrays.copyOfRange(commandLine, 1, commandLine.length);
//            
//            
//            this.bestMLCalgorithm = (MultiLabelClassifier) AbstractMultiLabelClassifier.forName(commandLine[0], new String[]{""});
//            this.bestMLCalgorithm.buildClassifier(data);
            
            
            
            System.exit(1);
        
    }
    
    public CandidateProgram chooseAmongBestAlgorithms(ArrayList<CandidateProgram> bestOfTheGenerations, long usedSeed, String [] learningANDvalidationDataDir) throws Exception{
        
        HashMap<String, Double> saveCompTime = new HashMap<String, Double>(); 
        System.out.println("==============================================");
        System.out.println("Evaluating the best ones...");  
        MetaIndividual.evaluateIndividuals(bestOfTheGenerations, learningANDvalidationDataDir[0], learningANDvalidationDataDir[1], this.getNumberOfThreads(), this.getSeed(), saveCompTime, this.getAlgorithmTimeLimit(), this.getExperimentName(), this.getGrammarMode());
        //It sorts the best of the bests.
        Collections.sort(bestOfTheGenerations);
        for(CandidateProgram e : bestOfTheGenerations){
            GRCandidateProgram gcp = (GRCandidateProgram) e;
            String gcp_grammar = gcp.toString();
            System.out.println(gcp_grammar +"=>"+ gcp.getFitnessValue());     
        } 
        //The best algorithm generated during training is defined.
        CandidateProgram bestAlgorithm = bestOfTheGenerations.get(bestOfTheGenerations.size() - 1);
        
        return bestAlgorithm;
    }
    
    /**
     * It creates the files and saves the results.
     * @param m_bestAlgorithm the best found algorithm.
     * @param searchTime the time to search for the individuals.
     * @param differenceTime the total time.
     * @param actualGeneration the actual generation where the process stopped.
     * @param learningANDvalidationDataDir the directory of the learning and validation files
     * @param numbOfEval the number of evaluations
     * @throws IOException
     * @throws Exception 
     */
    public void savingMLCResults(CandidateProgram m_bestAlgorithm, long searchTime, long differenceTime, int actualGeneration, String[] learningANDvalidationDataDir, int numbOfEval) throws IOException, Exception {
           
        System.out.println("==============================================");
        System.out.println("############TESTING########");
        new File(this.getSavingDirectory()).mkdir();
        new File(this.getSavingDirectory() + File.separator + "results-"+ this.getExperimentName()).mkdir();
        
        //It tests the best individual (MLC algorithm) for the input dataset.
        Results gr = MetaIndividual.testAlgorithm(m_bestAlgorithm, 
                                                      this.getTrainingDirectory(), this.getTestingDirectory(),
                                                      learningANDvalidationDataDir[0], learningANDvalidationDataDir[1], 
                                                      this.getSeed(), Integer.MAX_VALUE, this.getGrammarMode());
        
        //It saves the results in specific files and folders.
        BufferedWriter bf0 = new BufferedWriter(new FileWriter(this.getSavingDirectory() + File.separator+"results-" + this.getExperimentName()+File.separator+this.getFoldInit() + "Statistics-" + this.getExperimentName() + ".csv", true));
        BufferedWriter bf1 = new BufferedWriter(new FileWriter(this.getSavingDirectory() +File.separator+"results-"+ this.getExperimentName()+File.separator+this.getFoldInit() + "StatisticsCompact-" + this.getExperimentName() + ".csv", true));
        
        //Deleting the training and validation files.
        File fileT = new File("./Learning-" + this.getFoldInit() + ".arff");
        fileT.delete();
        fileT = new File("./Validation-" + this.getFoldInit() + ".arff");
        fileT.delete();         
        System.gc();
        
        //Generated algorithm and performance measures on learning, validation, full-training and testing sets.
        String algorithm = gr.getAlgorithm();
        double accuracy_FullTraining = gr.getAccuracy_FullTraining();
        double accuracy_Test = gr.getAccuracy_Test();
        double accuracy_Training = gr.getAccuracy_Training();
        double accuracy_Validation = gr.getAccuracy_Validation();

        double hammingScore_FullTraining = gr.getHammingScore_FullTraining();
        double hammingScore_Test = gr.getHammingScore_Test();
        double hammingScore_Training = gr.getHammingScore_Training();
        double hammingScore_Validation = gr.getHammingScore_Validation();

        double exactMatch_FullTraining = gr.getExactMatch_FullTraining();
        double exactMatch_Test = gr.getExactMatch_Test();
        double exactMatch_Training = gr.getExactMatch_Training();
        double exactMatch_Validation = gr.getExactMatch_Validation();

        double jaccardDistance_FullTraining = gr.getJaccardDistance_FullTraining();
        double jaccardDistance_Test = gr.getJaccardDistance_Test();
        double jaccardDistance_Training = gr.getJaccardDistance_Training();
        double jaccardDistance_Validation = gr.getJaccardDistance_Validation();

        double hammingLoss_FullTraining = gr.getHammingLoss_FullTraining();
        double hammingLoss_Test = gr.getHammingLoss_Test();
        double hammingLoss_Training = gr.getHammingLoss_Training();
        double hammingLoss_Validation = gr.getHammingLoss_Validation();

        double zeroOneLoss_FullTraining = gr.getZeroOneLoss_FullTraining();
        double zeroOneLoss_Test = gr.getZeroOneLoss_Test();
        double zeroOneLoss_Training = gr.getZeroOneLoss_Training();
        double zeroOneLoss_Validation = gr.getZeroOneLoss_Validation();

        double harmonicScore_FullTraining = gr.getHarmonicScore_FullTraining();
        double harmonicScore_Test = gr.getHarmonicScore_Test();
        double harmonicScore_Training = gr.getHarmonicScore_Training();
        double harmonicScore_Validation = gr.getHarmonicScore_Validation();

        double oneError_FullTraining = gr.getOneError_FullTraining();
        double oneError_Test = gr.getOneError_Test();
        double oneError_Training = gr.getOneError_Training();
        double oneError_Validation = gr.getOneError_Validation();

        double rankLoss_FullTraining = gr.getRankLoss_FullTraining();
        double rankLoss_Test = gr.getRankLoss_Test();
        double rankLoss_Training = gr.getRankLoss_Training();
        double rankLoss_Validation = gr.getRankLoss_Validation();

        double avgPrecision_FullTraining = gr.getAvgPrecision_FullTraining();
        double avgPrecision_Test = gr.getAvgPrecision_Test();
        double avgPrecision_Training = gr.getAvgPrecision_Training();
        double avgPrecision_Validation = gr.getAvgPrecision_Validation();

        double microPrecision_FullTraining = gr.getMicroPrecision_FullTraining();
        double microPrecision_Test = gr.getMicroPrecision_Test();
        double microPrecision_Training = gr.getMicroPrecision_Training();
        double microPrecision_Validation = gr.getMicroPrecision_Validation();

        double microRecall_FullTraining = gr.getMicroRecall_FullTraining();
        double microRecall_Test = gr.getMicroRecall_Test();
        double microRecall_Training = gr.getMicroRecall_Test();
        double microRecall_Validation = gr.getMicroRecall_Validation();

        double macroPrecision_FullTraining = gr.getMacroPrecision_FullTraining();
        double macroPrecision_Test = gr.getMacroPrecision_Test();
        double macroPrecision_Training = gr.getMacroPrecision_Training();
        double macroPrecision_Validation = gr.getMacroPrecision_Validation();

        double macroRecall_FullTraining = gr.getMacroRecall_FullTraining();
        double macroRecall_Test = gr.getMacroRecall_Test();
        double macroRecall_Training = gr.getMacroRecall_Test();
        double macroRecall_Validation = gr.getMacroRecall_Validation();

        double f1MicroAveraged_FullTraining = gr.getF1MicroAveraged_FullTraining();
        double f1MicroAveraged_Test = gr.getF1MicroAveraged_Test();
        double f1MicroAveraged_Training = gr.getF1MicroAveraged_Training();
        double f1MicroAveraged_Validation = gr.getF1MicroAveraged_Validation();

        double f1MacroAveragedExample_FullTraining = gr.getF1MacroAveragedExample_FullTraining();
        double f1MacroAveragedExample_Test = gr.getF1MacroAveragedExample_Test();
        double f1MacroAveragedExample_Training = gr.getF1MacroAveragedExample_Training();
        double f1MacroAveragedExample_Validation = gr.getF1MacroAveragedExample_Validation();;

        double f1MacroAveragedLabel_FullTraining = gr.getF1MacroAveragedLabel_FullTraining();
        double f1MacroAveragedLabel_Test = gr.getF1MacroAveragedLabel_Test();
        double f1MacroAveragedLabel_Training = gr.getF1MacroAveragedLabel_Training();
        double f1MacroAveragedLabel_Validation = gr.getF1MacroAveragedLabel_Validation();

        double aurcMacroAveraged_FullTraining = gr.getAurcMacroAveraged_FullTraining();
        double aurcMacroAveraged_Test = gr.getAurcMacroAveraged_Test();
        double aurcMacroAveraged_Training = gr.getAurcMacroAveraged_Training();
        double aurcMacroAveraged_Validation = gr.getAurcMacroAveraged_Validation();

        double aurocMacroAveraged_FullTraining = gr.getAurocMacroAveraged_FullTraining();
        double aurocMacroAveraged_Test = gr.getAurocMacroAveraged_Test();
        double aurocMacroAveraged_Training = gr.getAurocMacroAveraged_Training();
        double aurocMacroAveraged_Validation = gr.getAurocMacroAveraged_Validation();

        double emptyLabelvectorsPredicted_FullTraining = gr.getEmptyLabelvectorsPredicted_FullTraining();
        double emptyLabelvectorsPredicted_Test = gr.getEmptyLabelvectorsPredicted_Test();
        double emptyLabelvectorsPredicted_Training = gr.getEmptyLabelvectorsPredicted_Training();
        double emptyLabelvectorsPredicted_Validation = gr.getEmptyLabelvectorsPredicted_Validation();

        double labelCardinalityPredicted_FullTraining = gr.getLabelCardinalityPredicted_FullTraining();
        double labelCardinalityPredicted_Test = gr.getLabelCardinalityPredicted_Test();
        double labelCardinalityPredicted_Training = gr.getLabelCardinalityPredicted_Training();
        double labelCardinalityPredicted_Validation = gr.getLabelCardinalityPredicted_Validation();

        double levenshteinDistance_FullTraining = gr.getLevenshteinDistance_FullTraining();
        double levenshteinDistance_Test = gr.getLevenshteinDistance_Test();
        double levenshteinDistance_Training = gr.getLevenshteinDistance_Training();
        double levenshteinDistance_Validation = gr.getLevenshteinDistance_Validation();

        double labelCardinalityDifference_FullTraining = gr.getLabelCardinalityDifference_FullTraining();
        double labelCardinalityDifference_Test = gr.getLabelCardinalityDifference_Test();
        double labelCardinalityDifference_Training = gr.getLabelCardinalityDifference_Training();
        double labelCardinalityDifference_Validation = gr.getLabelCardinalityDifference_Validation();
   
        //It generates a file with all results.
        try {

            bf0.write(this.getSeed() + ";" + this.getFoldInit() + ";" + actualGeneration + ";"+ numbOfEval + ";" + differenceTime + ";" + searchTime + ";" 
                    + accuracy_FullTraining + ";" + accuracy_Test + ";" + accuracy_Training + ";" + accuracy_Validation + ";"
                    + hammingScore_FullTraining + ";" + hammingScore_Test + ";" + hammingScore_Training + ";" + hammingScore_Validation + ";"
                    + exactMatch_FullTraining + ";" + exactMatch_Test + ";" + exactMatch_Training + ";" + exactMatch_Validation + ";"
                    + jaccardDistance_FullTraining + ";" + jaccardDistance_Test + ";" + jaccardDistance_Training + ";" + jaccardDistance_Validation + ";"
                    + hammingLoss_FullTraining + ";" + hammingLoss_Test + ";" + hammingLoss_Training + ";" + hammingLoss_Validation + ";"
                    + zeroOneLoss_FullTraining + ";" + zeroOneLoss_Test + ";" + zeroOneLoss_Training + ";" + zeroOneLoss_Validation + ";"
                    + harmonicScore_FullTraining + ";" + harmonicScore_Test + ";" + harmonicScore_Training + ";" + harmonicScore_Validation + ";"
                    + oneError_FullTraining + ";" + oneError_Test + ";" + oneError_Training + ";" + oneError_Validation + ";"
                    + rankLoss_FullTraining + ";" + rankLoss_Test + ";" + rankLoss_Training + ";" + rankLoss_Validation + ";"
                    + avgPrecision_FullTraining + ";" + avgPrecision_Test + ";" + avgPrecision_Training + ";" + avgPrecision_Validation + ";"
                    + microPrecision_FullTraining + ";" + microPrecision_Test + ";" + microPrecision_Training + ";" + microPrecision_Validation + ";"
                    + microRecall_FullTraining + ";" + microRecall_Test + ";" + microRecall_Training + ";" + microRecall_Validation + ";"
                    + macroPrecision_FullTraining + ";" + macroPrecision_Test + ";" + macroPrecision_Training + ";" + macroPrecision_Validation + ";"
                    + macroRecall_FullTraining + ";" + macroRecall_Test + ";" + macroRecall_Training + ";" + macroRecall_Validation + ";"
                    + f1MicroAveraged_FullTraining + ";" + f1MicroAveraged_Test + ";" + f1MicroAveraged_Training + ";" + f1MicroAveraged_Validation + ";"
                    + f1MacroAveragedExample_FullTraining + ";" + f1MacroAveragedExample_Test + ";" + f1MacroAveragedExample_Training + ";" + f1MacroAveragedExample_Validation + ";"
                    + f1MacroAveragedLabel_FullTraining + ";" + f1MacroAveragedLabel_Test + ";" + f1MacroAveragedLabel_Training + ";" + f1MacroAveragedLabel_Validation + ";"
                    + aurcMacroAveraged_FullTraining + ";" + aurcMacroAveraged_Test + ";" + aurcMacroAveraged_Training + ";" + aurcMacroAveraged_Validation + ";"
                    + aurocMacroAveraged_FullTraining + ";" + aurocMacroAveraged_Test + ";" + aurocMacroAveraged_Training + ";" + aurocMacroAveraged_Validation + ";"
                    + emptyLabelvectorsPredicted_FullTraining + ";" + emptyLabelvectorsPredicted_Test + ";" + emptyLabelvectorsPredicted_Training + ";" + emptyLabelvectorsPredicted_Validation + ";"
                    + labelCardinalityPredicted_FullTraining + ";" + labelCardinalityPredicted_Test + ";" + labelCardinalityPredicted_Training + ";" + labelCardinalityPredicted_Validation + ";"
                    + levenshteinDistance_FullTraining + ";" + levenshteinDistance_Test + ";" + levenshteinDistance_Training + ";" + levenshteinDistance_Validation + ";"
                    + labelCardinalityDifference_FullTraining + ";" + labelCardinalityDifference_Test + ";" + labelCardinalityDifference_Training + ";" + labelCardinalityDifference_Validation
                    + ";" + ";" + algorithm);
            bf0.newLine();

            //It generates a file with the compacted results results.
            bf1.write(this.getSeed() + ";" + this.getFoldInit() + ";" + actualGeneration + ";"+ numbOfEval + ";" + differenceTime + ";" + searchTime + ";"
                    + exactMatch_FullTraining + ";" + exactMatch_Test + ";" + exactMatch_Training + ";" + exactMatch_Validation + ";"
                    + hammingLoss_FullTraining + ";" + hammingLoss_Test + ";" + hammingLoss_Training + ";" + hammingLoss_Validation + ";"
                    + rankLoss_FullTraining + ";" + rankLoss_Test + ";" + rankLoss_Training + ";" + rankLoss_Validation + ";"
                    + f1MacroAveragedLabel_FullTraining + ";" + f1MacroAveragedLabel_Test + ";" + f1MacroAveragedLabel_Training + ";" + f1MacroAveragedLabel_Validation
                    + ";" + ";" + algorithm);

            bf1.newLine();
        } catch (Exception e) {
            System.out.println(e);
        }
        bf0.close();
        bf1.close();    
    }
    
    /**
     * It generates a file with the fitness curve. *
     * @param strB the buffer to save.
     */
    public void savingLog(StringBuilder strB) throws IOException{
        BufferedWriter bf2 = new BufferedWriter(new FileWriter(this.getSavingDirectory() + File.separator + "results-" + this.getExperimentName() + File.separator + this.getFoldInit() + "Results-" + this.getExperimentName() + ".csv", true));
        try {
            bf2.write(strB.toString());
            bf2.newLine();
            bf2.write("############");
            bf2.newLine();
            bf2.close();
        } catch (Exception e) {
            System.out.println(e);
        }       
    }
    
    
    /**
     * It splits the training data (in a stratified way) into two subsets: learning and validation.     
     * While the former is used to learn the model, the latter is used to valid the produce model 
     * by that MLC algorithm. It uses Mulan Java library to perform the stratified sampling.
     * @param seed - The seed to sample the data.
     * @param fold - The fold to be sampled.
     * @param n_labels - The number of labels of the input dataset.
     * @return a string vector with the directories of the learning and validation sets. 
     */
    public String[] splitDataInAStratifiedWay(long seed, int fold, int n_labels){
        try {
            String arffDir = this.getTrainingDirectory();        
            MultiLabelInstances dataset = new MultiLabelInstances(arffDir, n_labels);
            
            IterativeStratification is = new IterativeStratification(seed);
            MultiLabelInstances[] folds = is.stratify(dataset, 5);
            
            //It creates the validation set.
            MultiLabelInstances validationData = new MultiLabelInstances(folds[fold].getDataSet(), dataset.getLabelsMetaData());
            validationData.getDataSet().setRelationName(dataset.getDataSet().relationName());
            
            Instances learningInst = null;
            boolean validFirstFold = true;
            
            for(int f=0; f< folds.length; f++){                
                if(f!=fold && validFirstFold){
                    learningInst = new Instances(folds[f].getDataSet());
                    validFirstFold = false;
                }else if (f!=fold){
                    learningInst.addAll(folds[f].getDataSet());
                }                
            }            
            
            MultiLabelInstances learningData = new MultiLabelInstances(learningInst, dataset.getLabelsMetaData());
            learningData.getDataSet().setRelationName(dataset.getDataSet().relationName());
            
            try (FileWriter fLearning = new FileWriter("Learning-"+fold +".arff", false)) {
                fLearning.write(learningData.getDataSet().toString());
                fLearning.close();
            }
            
            try (FileWriter fValidation = new FileWriter("Validation-"+fold+".arff", false)) {
                fValidation.write(validationData.getDataSet().toString());
                fValidation.close();
            }       
        
        } catch (InvalidDataFormatException ex) {
            System.err.println("Invalid data format exception: "+ex);
        } catch (IOException ex) {
            System.err.println("General exception: "+ex);
        }        
        
        String[] learningANDvalidationData = new String[2];
        learningANDvalidationData[0] = "Learning-"+fold +".arff";
        learningANDvalidationData[1] = "Validation-"+fold+".arff";

        return learningANDvalidationData;
    }
    
    
    /**
     * It randomly splits the training data into two subsets: learning and validation.
     * While the former is used to learn the model, the latter is used to valid the
     * produce model by that MLC algorithm. 
     * @param seed - The seed to sample the data.
     * @param fold - The fold to be sampled.
     * @param fullTrainData - The instances with the training set.
     * @return a string vector with the directories of the learning and validation sets.
     * @throws Exception 
     */
    public String[] splitData(long seed, int fold, Instances fullTrainData) throws Exception {

        Instances full = fullTrainData; // current training set.
        full.randomize(new Random(seed));

        RemoveFolds train = new RemoveFolds();
        RemoveFolds valid = new RemoveFolds();
        train.setInputFormat(full);
        train.setSeed(seed);
        train.setNumFolds(4);  // it uses 3/4 for training.
        train.setFold(1);
        train.setInvertSelection(true); // inverte = pega 3/4
        Instances training = Filter.useFilter(full, train);
        training.setRelationName(fullTrainData.relationName());

        valid.setInputFormat(full);
        valid.setSeed(seed);
        valid.setNumFolds(4); // it uses 1/4 for validating.
        valid.setFold(1);
        valid.setInvertSelection(false); // it gets 1/4 for validating.
        Instances validation = Filter.useFilter(full, valid);
        validation.setRelationName(fullTrainData.relationName());

        
        try (FileWriter fLearning = new FileWriter("Learning-"+fold +".arff", false)) {
            fLearning.write(training.toString());
            fLearning.close();
        }
        
        try (FileWriter fValidation = new FileWriter("Validation-"+fold+".arff", false)) {
            fValidation.write(validation.toString());
            fValidation.close();
        }
        
        String[] learningANDvalidationData = new String[2];
        learningANDvalidationData[0] = "Learning-"+fold +".arff";
        learningANDvalidationData[1] = "Validation-"+fold+".arff";

        return learningANDvalidationData;
    }
    
    
    /**
     * It measures the average fitness of the population.
     * @param population of individuals that represent MLC algorithms.
     * @return the average fitness of the population.
     * @throws Exception 
     */    
    public  double getAvgFitness(ArrayList<CandidateProgram> population) throws Exception {
        double average = 0.0;
        int count = 0;
        for (int i = 0; i < population.size(); i++) {
            GRCandidateProgram grcp = (GRCandidateProgram) population.get(i);
            if(grcp.getFitnessValue() != 0.0){
                average += grcp.getFitnessValue();
                count++;
            }
        }
        average /= count;

        return average;
    }
    
    /**
     * It applies mutation operator on an individual
     * @param individual to be mutated.
     * @param rnd to define the random generator.
     * @param labels the number of labels of the dataset.
     * @param attributes the number of attributes of the dataset
     * @return a mutated individual, which is a candidate program.
     */
    private CandidateProgram mutate(CandidateProgram individual, MersenneTwisterFast rnd,
                                    int n_labels, int attributes){

        double randomVar = rnd.nextDouble();
        CandidateProgram mchild = individual.clone();
        if (randomVar < this.getMutationRate()) {
            WhighamMutation mutation = new WhighamMutation(rnd, n_labels, attributes);
            mchild = mutation.mutate(individual);
        }        
        
        return mchild;
    }  
    
    
    /**
     * It gets a parent from the tournament.
     * @param population the population to select the individuals.
     * @param rnd to define the random generator.
     * @return the selected candidate program (individual) from the tournament.
     * @throws Exception 
     */
    private CandidateProgram getParentFromTournament(ArrayList<CandidateProgram> population, MersenneTwisterFast rnd) throws Exception {
        
        ArrayList<CandidateProgram> candidates = new ArrayList<CandidateProgram>();
        for (int i = 0; i < this.getTournamentSize(); i++) {
            int index = (int) Math.round(rnd.nextDouble() * (population.size() - 1));
            candidates.add(population.get(index));
        }
        // The sorted k candidates.
        Collections.sort(candidates);

        return candidates.get(candidates.size() - 1);
    }    
    
   /**
    * It calculates the probability distribution for a test instance.
    * @param x the test instance to be evaluated by the best algorithm..
    * @return the distribution for the test instance.
    * @throws Exception 
    */
   @Override
   public double[] distributionForInstance(Instance x) throws Exception {                
        double p[] = new double[x.classIndex()];
        p = this.bestMLCalgorithm.distributionForInstance(x);            
        return p;
    }


    /**
     * The main method to run AutoMEKA_GGP.
     * @param args the arguments of the method.
     */
    public static void main(String args[]) {
        AbstractMultiLabelClassifier.runClassifier(new AutoMEKA_GGP(args), args);
    }
}