
package meka.classifiers.multilabel.meta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import meka.classifiers.multilabel.meta.gaautomlc.core.MetaIndividual;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import meka.classifiers.multilabel.AbstractMultiLabelClassifier;
import meka.classifiers.multilabel.CC;
import meka.classifiers.multilabel.meta.gaautomlc.core.MersenneTwisterFast;
import meka.core.MLUtils;
import mulan.data.InvalidDataFormatException;
import mulan.data.IterativeStratification;
import mulan.data.MultiLabelInstances;
import weka.classifiers.AbstractClassifier;
import static weka.classifiers.AbstractClassifier.runClassifier;
import meka.classifiers.multilabel.meta.gaautomlc.core.Results;
import meka.classifiers.multilabel.meta.gaautomlc.core.xmlparser.Allele;
import meka.classifiers.multilabel.meta.gaautomlc.core.xmlparser.XMLAlgorithmHandler;
import meka.classifiers.multilabel.meta.gaautomlc.core.xmlparser.XMLGeneHandler;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Utils;


/**
 * GAAutoMLC.java - A method for selecting and configuring multi-label 
 classification (MLC) algorithm in the MEKA software.
 * 
 * GA-Auto-MLC. uses a real-coded genetic algorithm
 * aiming to find the most suitable MLC algorithm for a given dataset of 
 * interest
 *
 * @author Alex G. C. de Sa (alexgcsa@dcc.ufmg.br)
 */
public class GAAutoMLC extends AbstractMultiLabelClassifier{

    private static final long serialVersionUID = -427074159411195910L;

    /** a XML file containing the algorithms search space to build a new algorithm.  */
    protected File m_XMLAlgorithmsFile = new File(System.getProperty("user.dir"));   
    
    /** seed for random number. */
    protected long m_seed; 
    
    /** crossover rate. */
    protected double m_crossoverRate;
    
    /** number of generations. */
    protected int m_numberOfGenerations;
    
    /** number of generations to resample the data*/
    protected int m_resample;
    
    /** size of the population. */
    protected int m_populationSize;
    
    /** size of the tournament. */
    protected int m_tournamentSize;
   
    /** Size of the elitism. */
    protected int m_elitismSize = 1;  
    
    /** mutation rate. */
    protected double m_mutationRate;
    
    /** Init of the fold: */
    protected int m_foldInit;
    
    /** Init of the number of threads: */
    protected int m_numberOfThreads;
    
    /** Training directory. */
    protected String m_trainingDirectory;
    /** Testing directory. */
    protected String m_testingDirectory;    

    /** Timeout limit in seconds for each algorithm. **/
    protected int m_timeoutLimit = 60;
    
    /** The name of the experiment, which is useful to define the name of the folders and files. **/
    protected String m_experimentName = "experimentABC";    
    
    /** To decide if the process will be guided by generations or time**/
    protected boolean m_anytime = false;
    
    /** The time limit for running GA-Auto-MLC (in minutes). */
    protected int m_generaltimeLimit = 1;        
    
    /** The directory to save the results. */
    protected String m_savingDirectory = "~/"; 
    
    /** The selected MLC algorithm. */
    protected Classifier bestMLCalgorithm;    
    
    /** 
    *Constructor 
    */
    public GAAutoMLC (String [] argv) {
        super();
        resetOptions();
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

    /**
    * Returns an enumeration describing the available options.
    * @return an enumeration of all the available options.
    *
    **/
    @Override
    public Enumeration listOptions () {
        ArrayList newArray = new ArrayList(7);
        
        newArray.addAll(Arrays.asList(super.listOptions()));
        newArray.add(new Option("\tMutation rate.",
                                 "M", 1, "-M <num>"));
        newArray.add(new Option("\tCrossover rate.",
                                 "X", 1, "-X <num>"));
        newArray.add(new Option("\tTournament size.", 
                                 "K", 1, "-K <num>"));
        newArray.add(new Option("\tNumber of generations.",
                                 "G", 1, "-G <num>"));
        newArray.add(new Option("\tPopulation size.",
                                 "P", 1, "-P <num>"));
        newArray.add(new Option("\tFold init.",
                                 "Y", 1, "-Y <num>"));
        newArray.add(new Option("\tNumber of threads init.",
                                 "N", 1, "-N <num>"));
        newArray.add(new Option("\tNumber of generations to perform resampling.",
                                 "R", 1, "-R <num>"));        
        newArray.add(new Option("\tFile containing the components of the multi-label algorithms."
                                + "\n\ta new algorithm. "
                                , "A", 1, "-A <path_to_file>"));
        newArray.add(new Option("\tSeed for random numbers (default = -1).",
                                "H", 1, "-H <num>"));        
        return  Collections.enumeration(newArray);
    }


    /**
    * Parses a given list of options. <p/>
    *
    <!-- options-start -->
    * Valid options are: <p/>
    * 
    * <pre> -F &lt;num&gt;
    *  XML file containing the instructions to build
    *  a new algorithm. Defines the search space.</pre>
    * 
    * <pre> -N &lt;num&gt;
    *  Number of non-improving nodes to
    *  consider before terminating search.</pre>
    * 
    * <pre> -S &lt;num&gt; alguns
    *  Size of lookup cache for evaluated subsets.
    *  Expressed as a multiple of the number of
    *  attributes in the data set. (default = 1)</pre>
    * 
    <!-- options-end -->
    *
    * @param options the list of options as an array of strings
    * @throws Exception if an option is not supported
    *
    **/
    @Override
    public void setOptions (String[] options)
        throws Exception {
        String optionString;
 
        optionString = Utils.getOption('K', options);
        if (optionString.length() != 0) {
            setTournamentSize(Integer.parseInt(optionString));
        }        

        optionString = Utils.getOption('P', options);
        if (optionString.length() != 0) {
            setPopulationSize(Integer.parseInt(optionString));
        }
        
        optionString = Utils.getOption('G', options);
        if (optionString.length() != 0) {
            setNumberOfGenerations(Integer.parseInt(optionString));
        }
        
        optionString = Utils.getOption('R', options);
        if (optionString.length() != 0) {
            setResample(Integer.parseInt(optionString));
        }
        
        optionString = Utils.getOption('M', options);
        if (optionString.length() != 0) {
            setMutationRate(Double.parseDouble(optionString));
        }
        
        optionString = Utils.getOption('X', options);
        if (optionString.length() != 0) {
            setCrossoverRate(Double.parseDouble(optionString));
        }
        
        optionString = Utils.getOption('Y', options);
        if (optionString.length() != 0) {
            setFoldInit(Integer.parseInt(optionString));
        }
        
        optionString = Utils.getOption('N', options);
        if (optionString.length() != 0) {
            setNumberOfThreads(Integer.parseInt(optionString));
        }   
        
        optionString = Utils.getOption('V', options);
        if (optionString.length() != 0) {
            setElitismSize(Integer.parseInt(optionString));
        }
        
        optionString = Utils.getOption('L', options);
        if (optionString.length() != 0) {
            setTimeoutLimit(Integer.parseInt(optionString));
        }    
 
        optionString = Utils.getOption('W', options);
        if (optionString.length() != 0) {
            setExperimentName(optionString);
        } 

        optionString =  Utils.getOption('A', options);
        if (optionString.length() != 0) {
             setXMLAlgorithmsFile(new File(optionString));
        }
        
        optionString = Utils.getOption('H', options);
        if (optionString.length() != 0) {
            setSeed(Long.parseLong(optionString));
        }  
        
        optionString = Utils.getOption('B', options);
        if (optionString.length() != 0) {
            this.setGeneraltimeLimit(Integer.parseInt(optionString));
        }         
        
        optionString = Utils.getOption('D', options);
        if (optionString.length() != 0) {            
            setSavingDirectory(optionString);
        }    

        setAnytime(Utils.getFlag("C", options)); 
    }
    
    
    /**
    * Gets the current settings of BestFirst.
    * @return an array of strings suitable for passing to setOptions()
    */
    @Override
    public String[] getOptions () {
        ArrayList<String> options = new ArrayList<String>();

        options.addAll(Arrays.asList(super.getOptions()));

        options.add("-K");
        options.add("" + m_tournamentSize);
        
        options.add("-P");
        options.add("" + m_populationSize);   
        
        options.add("-G");
        options.add("" + m_numberOfGenerations);
        
        options.add("-R");
        options.add("" + m_resample); 
        
        options.add("-M");
        options.add("" + m_mutationRate); 
        
        options.add("-X");
        options.add("" + m_crossoverRate);
        
        options.add("-Y");
        options.add("" + this.m_foldInit); 
        
        options.add("-N");
        options.add("" + this.m_numberOfThreads); 
        
        options.add("-V");
        options.add("" + this.m_elitismSize);  

        options.add("-L");
        options.add("" + this.m_timeoutLimit);     
        
        options.add("-W");
        options.add("" + this.m_experimentName); 
        
        options.add("-A");
        options.add("" + this.m_XMLAlgorithmsFile);
        
        options.add("-H");
        options.add("" + this.m_seed);
        
        options.add("-B");
        options.add("" + this.m_generaltimeLimit);
        
        options.add("-D");
        options.add("" + this.m_savingDirectory);        
        
        if(this.m_anytime){
            options.add("-C");
        }

        return  options.toArray(new String[0]);
    }    
    
    public void setPopulationSize(int populationSize) {
        m_populationSize = populationSize;
    }
    
    public void setTournamentSize(int tournamentSize) {
        this.m_tournamentSize = tournamentSize;
    }    
   
    public void setNumberOfGenerations(int numberOfGenerations) {
        this.m_numberOfGenerations = numberOfGenerations;
    }
    
    public void setResample(int resample) {
        this.m_resample = resample;
    }
    
    public void setMutationRate(double mutationRate) {
        this.m_mutationRate = mutationRate;
    }  
    
    public void setCrossoverRate(double crossoverRate) {
        this.m_crossoverRate = crossoverRate;
    }    

    public void setElitismSize(int m_elitismSize) {
        this.m_elitismSize = m_elitismSize;
    }    

    public void setTimeoutLimit(int timeoutLimit) {
        this.m_timeoutLimit = timeoutLimit;
    }

    public void setExperimentName(String experimentName) {
        this.m_experimentName = experimentName;
    }
    
    public void setNumberOfThreads(int m_numberOfThreads) {
        this.m_numberOfThreads = m_numberOfThreads;
    }

    public void setFoldInit(int m_foldInit) {
        this.m_foldInit = m_foldInit;
    }
    
    public void setXMLAlgorithmsFile(File value) {
        if (value == null)
            value = new File(System.getProperty("user.dir"));

        m_XMLAlgorithmsFile = value;
    }    

    public void setSeed(long seed) {
        if(seed >= 0){
            this.m_seed = seed;
        }else{
            this.m_seed = 123;
        }  
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
    
    /**
     * Setter for directory to save the results.
     * @param savingDirectory the actual value for the saving directory.
     */
    public void setSavingDirectory(String savingDirectory) {
        this.m_savingDirectory = savingDirectory;
    }       
    
    public double getSeed() {
        return m_seed;
    }
    
    public File XMLAlgorithmsFile() {
        return m_XMLAlgorithmsFile;
    }    
    
    
    public String getTrainingDirectory() {
        return m_trainingDirectory;
    }

    public String getTestingDirectory() {
        return m_testingDirectory;
    }

    public int getTimeoutLimit() {
        return m_timeoutLimit;
    }

    public String getExperimentName() {
        return m_experimentName;
    } 
    

    public int getNumberOfThreads() {
        return m_numberOfThreads;
    }  
    
    public int getFoldInit() {
        return m_foldInit;
    }
    
    public double getMutationRate() {
        return m_mutationRate;
    }   

    public int getTournamentSize() {
        return m_tournamentSize;
    }
    
    public int getNumberOfGenerations() {
        return m_numberOfGenerations;
    }
    
    public int getResample() {
        return m_resample;
    }      
    
    public double getCrossoverRate() {
        return m_crossoverRate;
    }    
    
    private int getElitismSize() {
        return m_elitismSize;
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
     * Getter for the saving directory.
     * @return the saving directory.
     */ 
    public String getSavingDirectory() {
        return m_savingDirectory;
    }      




    /**
    * returns a description of the search as a String
    * @return a description of the search
    */
    @Override
    public String toString () {

        return  "GA for Auto Multi-Label Classification";
    }







    /**
    * Searches the algorithm subset space by beam search
    *
    * @param data the training instances.
    * @throws Exception if the classifier could not be built successfully
    */
    @Override
    public void buildClassifier(Instances data) throws Exception { 
        train(data);
        
        System.exit(1);
    }
    
    
 
    /**
     * Runs the training
     * @param xmlGeneHandler Xml handler to genetic mapping
     * @param xmlDatasetHandler Xml handler to datasets
     * @param rnd Seed random
     * @throws Exception 
     */
    private  void train(Instances data) throws Exception {
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
        
        
        // Test to see if the XML file path was set
        if (!m_XMLAlgorithmsFile.isFile()){
            System.out.println("No XML file");
        }
        
        /** It defines the random number generator. */
        MersenneTwisterFast rnd = new MersenneTwisterFast(this.m_seed);
   
        //It handles the search space of algorithms
        XMLAlgorithmHandler xmlAlgorithmHandler = new XMLAlgorithmHandler(m_XMLAlgorithmsFile); 
        
        //Reset the statistics and set the seed
        MetaIndividual.resetStatistics(true);
        MetaIndividual.setRnd(rnd);        
        
        //Variables to measure the worst, the best and the average fitness values.
        double worstFitness = 0.0;
        double bestFitness = 0.0;
        double avgFitness = 0.0;
        
        /** Map to save computational time. **/
        HashMap<String,Double> saveCompTime = new HashMap<String,Double>();
        
       // Number labels
        String[] dataOpts = MLUtils.getDatasetOptions(data);        
        int n_labels = Integer.parseInt(dataOpts[dataOpts.length - 1]);
        if(n_labels < 0){
            n_labels *= -1;
        }         

        
        //Original datasets:
        String[] learningANDvalidationDataDir = new String[2];
        /** It is used to change the seed, when resampling is performed . */
        int usedSeed = 0;  
        //It is used to save the best of the generations.
        ArrayList<MetaIndividual> bestOfTheGenerations = new ArrayList<MetaIndividual>();        
 
        //Training, validation e test -- array of instances
        StringBuilder strB = new StringBuilder();

        MetaIndividual[] population = new MetaIndividual[m_populationSize];

        //Initial Population
        for (int init = 0; init < population.length; init++) {
            double doubleID = rnd.nextDouble();
            int fileID = (int) ((xmlAlgorithmHandler.getAlgorithmsFiles().size()) * doubleID);
            XMLGeneHandler xmlGeneHandler = new XMLGeneHandler(new File(xmlAlgorithmHandler.getAlgorithmsFiles().get(fileID)));
            int size = 15;
            Allele genes = xmlGeneHandler.getGenes();

            double[] randomChromossome = generateRandomChromosome(size, rnd, doubleID);
            population[init] = new MetaIndividual(randomChromossome, genes, 0.0);
        }
            
        //logging...
        strB.append(0).append(";");
        
        
        /** It creates the learning and the validation sets. **/
        learningANDvalidationDataDir = splitDataInAStratifiedWay(usedSeed, this.getFoldInit(), n_labels);
        usedSeed++;
        
        /** Generation iterator. **/
        int generation = 0;
        //If it is using anytime, the number of generations is infinitive.
        if(this.getAnytime()){            
            this.setNumberOfGenerations(Integer.MAX_VALUE);
        }        
        
        /** It executes for a number of generations. */
        for (generation = 0; generation <= m_numberOfGenerations; generation++) {
            System.out.println("Generation: " + generation);
            System.out.println("GA-AutoMLC");
            /**
             * Resampling data every m_resample generations. *
             */
            if ((this.getResample() > 0) && (generation % this.getResample() == 0) && (generation > 0)) {
                learningANDvalidationDataDir = splitDataInAStratifiedWay(usedSeed, this.getFoldInit(), n_labels);
                usedSeed++;
                saveCompTime = new HashMap<String,Double>();
            }

            //Evaluate the population to get the fitness curve for each generation
            ArrayList<MetaIndividual> populationAux = new ArrayList<MetaIndividual>();
            for (int i = 0; i < population.length; i++) {
                if ((population[i] != null)) {
                    populationAux.add(population[i]);
                }
            }
            
            System.out.println("Evaluating...");
            //It evaluates the individuals.
            MetaIndividual.evaluateIndividuals(populationAux, learningANDvalidationDataDir[0], learningANDvalidationDataDir[1], this.m_numberOfThreads, this.m_timeoutLimit, saveCompTime);
            //It sorts the individuals given the fitness value. 
            Collections.sort(populationAux);
            
            //It updates the current time to verify if the timeout was achieved.
            currentAnyTime = System.currentTimeMillis();
            diffAnyTime =  currentAnyTime - startTime;
            
            
            //It tests if the timeout was reached or if we can continue with the generational process. 
            //We run at least the first generation
            if( ( ((diffAnyTime <= timeLimitMilSec) || (generation==0)) && (this.getAnytime()) ) || (!this.getAnytime()) ){
                //For logging the statistics.
                numbOfEval+= this.m_populationSize;                
                
                searchTime = diffAnyTime;
                actualGeneration = generation;          
                
                bestFitness = populationAux.get(populationAux.size()-1).getEvaluation();
                worstFitness = populationAux.get(0).getEvaluation();
                avgFitness = this.getPopulationFitness(populationAux);
                strB.append(worstFitness).append(";").append(avgFitness).append(";").append(bestFitness).append("\n");
                strB.append(generation+1).append(";").append(";");              
                //Saving the best of the generations.
                bestOfTheGenerations.add(populationAux.get(populationAux.size()-1));   
                //Printing the population and the individuals' fitness values.
                System.out.println("#Population#");
                for (int i = 0; i < populationAux.size(); i++) {
                    String classfName = populationAux.get(i).getM_individualInString();
                    if (!saveCompTime.containsKey(classfName)) {
                        saveCompTime.put(classfName, populationAux.get(i).getEvaluation());
                    }
                    System.out.println(populationAux.get(i).getM_individualInString() +"=>"+ populationAux.get(i).setEvaluation());
                }        
            }
            if(diffAnyTime > timeLimitMilSec){
                break;
            }            
            
            MetaIndividual[] newPopulation = new MetaIndividual[population.length];
            
            for(int e=0; e < this.getElitismSize(); e++){
                int p = populationAux.size() - 1 - e;
                newPopulation[e] = new MetaIndividual(populationAux.get(p).getGeneticCode(), populationAux.get(p).getM_genome(), populationAux.get(p).getEvaluation());
            }

            // Generates a new population -- starts in one because of the elitism:
            for (int j = this.getElitismSize(); j < newPopulation.length;) {

                MetaIndividual parent1 = getParentFromTournament(population, rnd);
                MetaIndividual parent2 = getParentFromTournament(population, rnd);

                double randomVar = rnd.nextDouble();

                double[] child1 = parent1.getGeneticCode();
                double[] child2 = parent2.getGeneticCode();

                // Probabilistic crossover
                if (randomVar < m_crossoverRate) {
                    //Executa cross over sobre os dois individuos:										
                    int[] mask = generateCrossoverMask(parent1.getM_genomeSize(), rnd);

                    for (int k = 0; k < mask.length; k++) {
                        if (mask[k] == 1) {
                            double aux = child1[k];
                            child1[k] = child2[k];
                            child2[k] = aux;
                        }
                    }
                }
                // Mutation                
                child1 = mutation(child1, rnd);
                child2 = mutation(child2, rnd);

                double doubleID1 = child1[0];
//                    System.out.println("Double in mutation1: "+child1[0]);
                int fileID1 = (int) ((xmlAlgorithmHandler.getAlgorithmsFiles().size()) * doubleID1);
                XMLGeneHandler xmlGeneHandler1 = new XMLGeneHandler(new File(xmlAlgorithmHandler.getAlgorithmsFiles().get(fileID1)));
                Allele genes1 = xmlGeneHandler1.getGenes();

                double doubleID2 = child2[0];
//                    System.out.println("Double in mutation2: "+child2[0]);
                int fileID2 = (int) ((xmlAlgorithmHandler.getAlgorithmsFiles().size()) * doubleID2);
                XMLGeneHandler xmlGeneHandler2 = new XMLGeneHandler(new File(xmlAlgorithmHandler.getAlgorithmsFiles().get(fileID2)));
                Allele genes2 = xmlGeneHandler2.getGenes();

                //creating the new Population:
                newPopulation[j++] = new MetaIndividual(child1, genes1, 0.0);

                if (j < newPopulation.length) {
                    newPopulation[j++] = new MetaIndividual(child2, genes2, 0.0);
                }
            }
            //The new population is the current one:
            population = newPopulation;

            System.gc();
        }/** The end of the evolutionary process. **/

        usedSeed++;
        learningANDvalidationDataDir = splitDataInAStratifiedWay(usedSeed, this.getFoldInit(), n_labels);
        //It chooses among the best individuals of all generations.
        MetaIndividual m_bestAlgorithm = this.chooseAmongBestAlgorithms(bestOfTheGenerations, usedSeed, learningANDvalidationDataDir);
        numbOfEval+=bestOfTheGenerations.size();        
        

        //Measuring the elapsed time to run the GA.
        long endTime = System.currentTimeMillis();
        long differenceTime = endTime - startTime; 
        System.gc();
        
        //Saving the results...
        this.savingMLCResults(m_bestAlgorithm, searchTime, differenceTime, actualGeneration, learningANDvalidationDataDir, numbOfEval);    
        //And the log of each generation.
        this.savingLog(strB);

    }
    
    public MetaIndividual chooseAmongBestAlgorithms(ArrayList<MetaIndividual> bestOfTheGenerations, long usedSeed, String [] learningANDvalidationDataDir) throws Exception{
        HashMap<String, Double> saveCompTime = new HashMap<String, Double>(); 
        System.out.println("==============================================");
        System.out.println("Evaluating the best ones...");  
        MetaIndividual.evaluateIndividuals(bestOfTheGenerations, learningANDvalidationDataDir[0], learningANDvalidationDataDir[1], this.getNumberOfThreads(), this.getTimeoutLimit(), saveCompTime);
        //It sorts the best of the bests.
        Collections.sort(bestOfTheGenerations);
        for(MetaIndividual me : bestOfTheGenerations){
            System.out.println(me.getM_individualInString() +"=>"+ me.setEvaluation());   
        } 
        //The best algorithm generated during training is defined.
        MetaIndividual bestAlgorithm = bestOfTheGenerations.get(bestOfTheGenerations.size() - 1);
        
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
    public void savingMLCResults(MetaIndividual m_bestAlgorithm, long searchTime, long differenceTime, int actualGeneration, String[] learningANDvalidationDataDir, int numbOfEval) throws IOException, Exception {
           
        System.out.println("==============================================");
        System.out.println("############TESTING########");
        new File(this.getSavingDirectory()).mkdir();
        new File(this.getSavingDirectory() + File.separator + "results-"+ this.getExperimentName()).mkdir();
        
        //It tests the best individual (MLC algorithm) for the input dataset.
        Results gr = MetaIndividual.testAlgorithm(m_bestAlgorithm, 
                                                      this.getTrainingDirectory(), this.getTestingDirectory(),
                                                      learningANDvalidationDataDir[0], learningANDvalidationDataDir[1]);
        
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
            
            /** It creates the validation set. **/
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
    


    
    //Get average fitness in the population:
    public  double getPopulationFitness(ArrayList<MetaIndividual> population) throws Exception {
        double average = 0.0;
        int count = 0;
        for (int i = 0; i < population.size(); i++) {
            if(population.get(i).getEvaluation() != 0.0){
                average += population.get(i).getEvaluation();
                count++;
            }

        }
        average /= count;

        return average;
    }
    
    
    //Training and test sets:
    public  Instances[] getTrainingTest(Instances trainingData, double testPercentage) {
        Instances[] trainingValidation = new Instances[2];

        int testSize = (int) Math.round(trainingData.numInstances() * testPercentage);
        int trainingSize = trainingData.numInstances() - testSize;
        // Training set
        trainingValidation[0] = new Instances(trainingData, 0, trainingSize);
        // test set
        trainingValidation[1] = new Instances(trainingData, trainingSize, testSize);

        return trainingValidation;
    }
    
    //Random chromosome to the initial population:
    protected double[] generateRandomChromosome(int size, MersenneTwisterFast rnd, double firstValue){
        double[] chromosome = new double[size+1];
        chromosome[0] = firstValue;
        for(int i = 1; i <= size; i++){
            chromosome[i] = rnd.nextDouble();
        } 
        return chromosome;
    }    
    
    private  MetaIndividual getParentFromTournament(MetaIndividual[] population, MersenneTwisterFast rnd) throws Exception {
        //Best of k candidates:
        ArrayList<MetaIndividual> candidates = new ArrayList<MetaIndividual>();
        for (int i = 0; i < m_tournamentSize; i++) {
            int index = (int) Math.round(rnd.nextDouble() * (population.length - 1));
            candidates.add(population[index]);
        }
//        MetaIndividual.evaluateIndividuals(candidates, trainingData, validationData, generation + "", false, this.m_numberOfThreads);

        Collections.sort(candidates);
        return candidates.get(candidates.size()-1);
    }
    
    //Mutation operation is executed in a probabilistic fashion:
    private double[] mutation(double[] individual, MersenneTwisterFast rnd) {
        int change = rnd.nextInt(individual.length);

        double randomVar = rnd.nextDouble();
        if (randomVar < m_mutationRate) {
            individual[change] = rnd.nextDouble();
        }
        return individual;
    }
    
    //Generates a mask to make the cross over operation:
    public  int[] generateCrossoverMask(int size, MersenneTwisterFast rnd) {
        int[] mask = new int[size];

        //Uniform distribution -- zero and ones:
        for (int i = 0; i < size; i++) {
            if (rnd.nextDouble() < 0.5) {
                mask[i] = 0;
            } else {
                mask[i] = 1;
            }
        }
        return mask;
    }
    

    /**
     * Reset options to default values
     */
    private void resetOptions() {
        this.m_seed = 1;
        this.m_tournamentSize = 2;
        this.m_populationSize = 13;
        this.m_numberOfGenerations = 3;
        this.m_resample = -1;
        this.m_mutationRate = 0.1;
        this.m_crossoverRate = 0.8;
        this.m_foldInit = 0;
        this.m_numberOfThreads = 1;
        this.m_experimentName = "Experiment-ABC";
        this.m_elitismSize = 5;
        this.m_trainingDirectory = "training.arff";
        this.m_testingDirectory = "testing.arff";
        this.m_timeoutLimit = 10;
        this.m_generaltimeLimit = 1;
        this.m_anytime = true;
        this.m_savingDirectory = "~/";
        
    }
  
 
    public static void main(String[] argv) {
        Locale.setDefault(Locale.US);
        runClassifier(new GAAutoMLC(argv), argv);
    }

    @Override
    public double[] distributionForInstance(Instance x) throws Exception {
        double p[] = new double[x.classIndex()];
        p = this.bestMLCalgorithm.distributionForInstance(x);            
        return p;
    }




}