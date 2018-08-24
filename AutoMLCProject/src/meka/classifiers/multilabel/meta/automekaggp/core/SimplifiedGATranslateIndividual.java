/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meka.classifiers.multilabel.meta.automekaggp.core;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author alexgcsa
 */
public final class SimplifiedGATranslateIndividual extends AbstractTranslateIndividual {
    private final String indFromGrammar;
//    private final long seed;
    
    HashMap<String,String> mappingSLC;
    HashMap<String,String> mappingMetaSLC_One;    
    HashMap<String,String> mappingMetaSLC_Various;
    HashMap<String,String> mappingMLCPT;
    HashMap<String,String> mappingMLCAA;
    HashMap<String,String> mappingMetaMLC;
    
    
    
    public SimplifiedGATranslateIndividual(String indFromGrammar, long seed) {
        this.indFromGrammar = indFromGrammar;
//        this.seed = seed;
        /** Mapping methods for SLC. */
        this.mappingSLC = new LinkedHashMap<>();
        this.mappingSLC();
        /** Mapping meta-methods for SLC with just one algorithm. */
        this.mappingMetaSLC_One = new LinkedHashMap<>();
        this.mappingMetaSLC_One();   
        /** Mapping meta-methods for SLC with just one algorithm. */
        this.mappingMetaSLC_Various = new LinkedHashMap<>();
        this.mappingMetaSLC_Various();             
        /** Mapping problem transformation methods for MLC. */
        this.mappingMLCPT = new LinkedHashMap<>();
        this.mappingMLCPT();
        /** Mapping algorithm adaptation methods for MLC. */
        this.mappingMLCAA = new LinkedHashMap<>();
        this.mappingMLCAA();
        /** Mapping meta-algorithm for MLC. */
        this.mappingMetaMLC = new LinkedHashMap<>();
        this.mappingMetaMLC();  
        
    }
    
    /**
     * Mapping all SLC algorithms from grammar string to code string.
     */
    public void mappingSLC(){
        /** SLC: Trees. */
        mappingSLC.put("J48", "weka.classifiers.trees.J48");
        mappingSLC.put("DecisionStump", "weka.classifiers.trees.DecisionStump");
        mappingSLC.put("RandomForest", "weka.classifiers.trees.RandomForest");
        mappingSLC.put("RandomTree","weka.classifiers.trees.RandomTree");
        mappingSLC.put("REPTree", "weka.classifiers.trees.REPTree");        
        /** SLC: Rules. */
        mappingSLC.put("DT","weka.classifiers.rules.DecisionTable");
        mappingSLC.put("JRip","weka.classifiers.rules.JRip");
        mappingSLC.put("OneR","weka.classifiers.rules.OneR");
        mappingSLC.put("PART","weka.classifiers.rules.PART");
        mappingSLC.put("ZeroR", "weka.classifiers.rules.ZeroR");        
        /** SLC: Lazy. */
        mappingSLC.put("KNN", "weka.classifiers.lazy.IBk");
        mappingSLC.put("KStar", "weka.classifiers.lazy.KStar");         
        /** SLC: Functions. */
        mappingSLC.put("VotedPerceptron", "weka.classifiers.functions.VotedPerceptron");    
        mappingSLC.put("MultiLayerPerc", "weka.classifiers.functions.MultilayerPerceptron");
        mappingSLC.put("StocGradDescent", "weka.classifiers.functions.SGD");        
        mappingSLC.put("LogisticRegression", "weka.classifiers.functions.Logistic");
        mappingSLC.put("SeqMinOptimization", "weka.classifiers.functions.SMO");  
        mappingSLC.put("LibSVM", "weka.classifiers.functions.LibSVM");
        
        /** SLC: Bayes. */
        mappingSLC.put("NaiveBayes", "weka.classifiers.bayes.NaiveBayes");
        mappingSLC.put("NaiveBayesMultinomial", "weka.classifiers.bayes.NaiveBayesMultinomial");
        mappingSLC.put("TAN", "weka.classifiers.bayes.BayesNet");
        mappingSLC.put("K2", "weka.classifiers.bayes.BayesNet");
        mappingSLC.put("HillClimber", "weka.classifiers.bayes.BayesNet");
        mappingSLC.put("LAGDHillClimber", "weka.classifiers.bayes.BayesNet");        
        mappingSLC.put("TabuSearch", "weka.classifiers.bayes.BayesNet");    
        mappingSLC.put("SimulatedAnnealing", "weka.classifiers.bayes.BayesNet");
        /** SLC: Exceptions. */
        mappingSLC.put("SimpleLogistic", "weka.classifiers.functions.SimpleLogistic");
        mappingSLC.put("LogisticModelTrees", "weka.classifiers.trees.LMT");
    }
    
    /**
     * Mapping all meta SLC algorithms with just one algorithm at the SLC level
     * from grammar string to code string.
     */
    public void mappingMetaSLC_One(){
        this.mappingMetaSLC_One.put("LWL", "weka.classifiers.lazy.LWL");
        this.mappingMetaSLC_One.put("ASC", "weka.classifiers.meta.AttributeSelectedClassifier");    
        this.mappingMetaSLC_One.put("RandomSubspace", "weka.classifiers.meta.RandomSubSpace");
        this.mappingMetaSLC_One.put("RandomCommittee", "weka.classifiers.meta.RandomCommittee");
        this.mappingMetaSLC_One.put("AdaM1", "weka.classifiers.meta.AdaBoostM1");
        this.mappingMetaSLC_One.put("BaggingSLC", "weka.classifiers.meta.Bagging");
    }
    
    /**
     * Mapping all meta SLC algorithms with various algorithms at the SLC level
     * from grammar string to code string.
     */
    public void mappingMetaSLC_Various(){
        this.mappingMetaSLC_Various.put("Vote", "weka.classifiers.meta.Vote");
        this.mappingMetaSLC_Various.put("Stacking", "weka.classifiers.meta.Stacking");    
    }    
    
    
    /**
     * Mapping all MLC PT algorithms from grammar strign to code string.
     */
    public void mappingMLCPT(){      
        mappingMLCPT.put("BR", "meka.classifiers.multilabel.BR");
        mappingMLCPT.put("CC", "meka.classifiers.multilabel.CC");
        mappingMLCPT.put("LP", "meka.classifiers.multilabel.LC");        
        mappingMLCPT.put("BRq", "meka.classifiers.multilabel.BRq");
        mappingMLCPT.put("CCq", "meka.classifiers.multilabel.CCq");
        mappingMLCPT.put("PCC", "meka.classifiers.multilabel.PCC");
        mappingMLCPT.put("MCC", "meka.classifiers.multilabel.MCC");
        mappingMLCPT.put("CT",  "meka.classifiers.multilabel.CT");
        mappingMLCPT.put("CDN", "meka.classifiers.multilabel.CDN");     
        mappingMLCPT.put("CDT", "meka.classifiers.multilabel.CDT"); 
        mappingMLCPT.put("FW",  "meka.classifiers.multilabel.FW");
        mappingMLCPT.put("RT",  "meka.classifiers.multilabel.RT");
        mappingMLCPT.put("PS",  "meka.classifiers.multilabel.PS");
        mappingMLCPT.put("PSt", "meka.classifiers.multilabel.PSt");
        mappingMLCPT.put("RAkEL", "meka.classifiers.multilabel.RAkEL");
        mappingMLCPT.put("RAkELd", "meka.classifiers.multilabel.RAkELd");  
        mappingMLCPT.put("HASEL", "meka.classifiers.multilabel.HASEL"); 
        mappingMLCPT.put("BCC", "meka.classifiers.multilabel.BCC");
        mappingMLCPT.put("PMCC", "meka.classifiers.multilabel.PMCC");        
    }
    
        /**
     * Mapping all MLC AA algorithms from grammar strign to code string.
     */
    public void mappingMLCAA(){        
       mappingMLCAA.put("ML-BPNN", "meka.classifiers.multilabel.BPNN"); 
       mappingMLCAA.put("ML-DBPNN", "meka.classifiers.multilabel.DBPNN");  
    }
    
    /**
     * Mapping all Meta MLC algorithms from grammar strign to code string.
     */
    public void mappingMetaMLC(){
        mappingMetaMLC.put("SM", "meka.classifiers.multilabel.meta.SubsetMapper");
        mappingMetaMLC.put("RSML","meka.classifiers.multilabel.meta.RandomSubspaceML");
        mappingMetaMLC.put("MLC-BMaD","meka.classifiers.multilabel.MLCBMaD");
        
        mappingMetaMLC.put("BaggingML","meka.classifiers.multilabel.meta.BaggingML");
        mappingMetaMLC.put("BaggingMLDup","meka.classifiers.multilabel.meta.BaggingMLdup");
        mappingMetaMLC.put("EnsembleML","meka.classifiers.multilabel.meta.EnsembleML");        
        
        mappingMetaMLC.put("EM","meka.classifiers.multilabel.meta.EM");
        mappingMetaMLC.put("CM","meka.classifiers.multilabel.meta.CM");
        mappingMetaMLC.put("DeepML","meka.classifiers.multilabel.meta.DeepML");
        mappingMetaMLC.put("MBR","meka.classifiers.multilabel.meta.MBR");
        mappingMetaMLC.put("HOMER","meka.classifiers.multilabel.MULAN -S HOMER");
    }
    


    
    
    @Override
    public String translate2Command(String trainingSet, String validationSet, int timeoutLimit, boolean completeCommand){
        TranslationProcess translProc = null;
        String [] classifierName = this.indFromGrammar.split(" ");
        String command = "";  
        String threshold = "";
        int i=0;
        boolean meta = false;
        boolean metaSLC_various = false;
        
        if(completeCommand){
            command  = "timeout " + timeoutLimit+"s java -Xmx2g -cp weka.jar:meka.jar ";
        }
        
        
        
        while(i < classifierName.length){
            
            if(classifierName[i].equals("threshold")){
                threshold += " -" + classifierName[i];
                i++;
                threshold += " "+ classifierName[i];
                i++;
//                System.out.println(threshold);
            }else if(this.mappingMetaMLC.containsKey(classifierName[i])){
                meta = true;
                translProc = this.conditionals4MetaMLC(classifierName, i, trainingSet, validationSet, threshold);
                command  += translProc.getGeneratedCommand(); 
                i = translProc.getPos();
                if(!translProc.getIsHOMER())
                    command += threshold + " -t " + trainingSet +" -T " +validationSet + " -verbosity 6 -W ";    
           
            } else if(this.mappingMLCPT.containsKey(classifierName[i])){  
                translProc = this.conditionals4MLCPTMethods(classifierName, i, meta);
                command  += translProc.getGeneratedCommand(); 
                i = translProc.getPos();
                if(!meta)
                    command  +=  threshold + " -t " + trainingSet +" -T " +validationSet + " -verbosity 6 ";

            }else if(this.mappingMLCAA.containsKey(classifierName[i])){
                translProc = this.conditionals4AAMethods(classifierName, i, meta);
                command  += translProc.getGeneratedCommand(); 
                i = translProc.getPos();     
                if(!meta)
                    command  +=  " -t " + trainingSet +" -T " +validationSet + " -verbosity 6 ";
            
            }else if(this.mappingSLC.containsKey(classifierName[i])){ 
                translProc = this.conditionals4SLCMethods(classifierName, i, metaSLC_various);
                command  += translProc.getGeneratedCommand(); 
                i = translProc.getPos();            
            }else{
                System.err.println("######### Problems: not read:"+classifierName[i] + " in the position "+i);
                i++;
//                System.err.println("##################Not suitable parsing of the grammar!##################");
            }            
            
        }   
        
        return command;
    }
    
    
    public TranslationProcess conditionals4MetaMLC(String [] classifierName, int i, String trainingSet, String validationSet, String threshold){    
        int pos = i;
        String id = classifierName[pos];
        TranslationProcess translProc = null;
        String transl = this.mappingMetaMLC.get(id);
        pos++;
        
        if(id.equals("SM")){
            translProc = new TranslationProcess(transl, pos);
        }else if(id.equals("RSML")){
            translProc = translate_RSML(classifierName, transl, pos, trainingSet, validationSet);
        }else if((id.equals("BaggingML"))){
            translProc = translate_Bagging(classifierName, transl, pos, trainingSet, validationSet);
        }else if((id.equals("BaggingMLDup")) || (id.equals("EnsembleML"))){
            translProc = translate_BaggingDup_EnsembleML(classifierName, transl, pos, trainingSet, validationSet);
        }else if((id.equals("EM")) || (id.equals("CM"))){
            translProc = translate_EM_CM(classifierName, transl, pos, trainingSet, validationSet);

        }else if((id.equals("MBR")) ){
            translProc = new TranslationProcess(transl, pos);
        }else if((id.equals("HOMER")) ){
            translProc = translate_HOMER(classifierName, transl, pos, trainingSet, validationSet, threshold);

        }else {
            translProc = new TranslationProcess("",pos);
            System.err.println("##################Not found Meta-MLC classifier!##################");
        }     
        
        return translProc;
    }
    
    public TranslationProcess conditionals4AAMethods(String [] classifierName, int i, boolean meta){    
        int pos = i;
        String id = classifierName[pos];
        TranslationProcess translProc = null;
        String transl = ""; 
        transl += this.mappingMLCAA.get(id);
        pos++;
        
        if(id.equals("ML-BPNN")){
            translProc = translate_MLBPNN(classifierName, transl, pos, meta);
        }else if(id.equals("ML-DBPNN")){
            translProc = translate_MLDBPNN(classifierName, transl, pos, meta);
        }else {
            translProc = new TranslationProcess("",pos);
            System.err.println("##################Not found MLC AA classifier!##################");
        }       
        
        return translProc;
    }
    
    
    public TranslationProcess conditionals4MLCPTMethods(String [] classifierName, int i, boolean meta){
        int pos = i;
        String id = classifierName[pos];
        TranslationProcess translProc = null;
        String transl = ""; 
        transl += this.mappingMLCPT.get(id);
        pos++;        
        if((id.equals("BR")) || (id.equals("CC")) || (id.equals("LP")) || 
           (id.equals("FW")) || (id.equals("RT")) || (id.equals("PCC"))){
            translProc = translate_ManyMethods(classifierName, transl, pos, meta);
        }else if ((id.equals("BRq")) || (id.equals("CCq"))){ 
            translProc = translate_BRq_CCq(classifierName, transl, pos, meta);
        }else if(id.equals("MCC")){
            translProc = translate_MCC(classifierName, transl, pos, meta);
        }else if(id.equals("CT")){
            translProc = translate_CT(classifierName, transl, pos, meta);
        }else if(id.equals("CDN")){
            translProc = translate_CDN(classifierName, transl, pos, meta);
        }else if(id.equals("CDT")){
            translProc = translate_CDT(classifierName, transl, pos, meta);
        }else if( (id.startsWith("RAkEL")) || (id.equals("HASEL"))  ){
            translProc = translate_RAkEL(classifierName, transl, pos, meta);
        }else if( (id.equals("PS")) || (id.equals("PSt")) ){
            translProc = translate_PS(classifierName, transl, pos, meta);
        }else if (id.equals("BCC")){
            translProc = translate_BCC(classifierName, transl, pos, meta);
        }else if (id.equals("PMCC")){
            translProc = translate_PMCC(classifierName, transl, pos, meta);
        }else {
            translProc = new TranslationProcess("",pos);
            System.err.println("##################Not found MLC PT classifier!##################");
        }       
        return translProc;       
    }
    
    
    public TranslationProcess conditionals4SLCMethods(String [] classifierName, int i, boolean metaSLC_various){
        int pos = i;
        String id = classifierName[pos];
        TranslationProcess translProc = null;  
        String sep = " ";
        String transl = ""; 
        if(!metaSLC_various){
            transl =  " -W ";
            transl += this.mappingSLC.get(id);
            transl += " --";  
        }else{
            sep = "@";
            transl =  " -B ";
            transl += this.mappingSLC.get(id);  
        }
        pos++; 
        
        if(id.equals("J48")){
            translProc = translate_J48(classifierName, transl, pos, sep);
        }else if(id.equals("DecisionStump")){
            translProc = new TranslationProcess(transl, pos);
        }else if(id.equals("RandomForest")){
            translProc = translate_RandomForest(classifierName, transl, pos, sep);
        }else if(id.equals("OneR")){
            translProc = translate_OneR(classifierName, transl, pos, sep);
        }else if(id.equals("ZeroR")){
            translProc = new TranslationProcess(transl, pos);
        }else if(id.equals("PART")){
            translProc = translate_PART(classifierName, transl, pos, sep);
        }else if(id.equals("KNN")){
            translProc = translate_KNN(classifierName, transl, pos, sep);

        }else if(id.equals("MultiLayerPerc")){
            translProc = translate_MultiLayerPerc(classifierName, transl, pos, sep);   
        }else if(id.equals("LogisticRegression")){
            translProc = translate_LogisticReg(classifierName, transl, pos, sep);            
        }else if(id.equals("LibSVM")){
            translProc = translate_LibSVM(classifierName, transl, pos, sep);          
     
        }else if(id.equals("NaiveBayes")){
            translProc = translate_NaiveBayes(classifierName, transl, pos, sep);
        }else if(id.equals("NaiveBayesMultinomial")){
            translProc = new TranslationProcess(transl, pos);
        }else if (id.equals("TAN")){
            translProc = translate_TAN(classifierName, transl, pos, sep);

        }else if (id.equals("HillClimber")){
            translProc = translate_HC(classifierName, transl, pos, sep);


        }else{
            translProc = new TranslationProcess("",pos);
            System.err.println("##################Not found SLC classifier!##################");
        }       
        
        translProc.setCommand(translProc.getGeneratedCommand());
        return translProc;    
    }
 

    

    
/** ################################################################################################################################## **/       
/** ################################################################################################################################## **/   
/** ################################################################################################################################## **/   
      
    public TranslationProcess translate_MultiLayerPerc(String[] classifierName, String transl_partial, int pos_partial, String sep) {
        String transl = transl_partial;
        int pos = pos_partial;
        
        transl += sep + "-L" + sep + classifierName[pos];
        pos++;
        transl += sep + "-M" + sep + classifierName[pos];
        pos++;      
        transl += sep + "-H" + sep + classifierName[pos];
        pos++;     
        transl += sep + "-N" + sep + classifierName[pos];
        pos++;   
        
//        transl += sep + "-S" + sep + this.seed;
        
        return new TranslationProcess(transl, pos);
    }    
        
    
    public TranslationProcess translate_LibSVM(String[] classifierName, String transl_partial, int pos_partial, String sep) {
        String transl = transl_partial;
        int pos = pos_partial;
        
        transl += sep + "-C" + sep + classifierName[pos];
        pos++;
        transl += sep + "-G" + sep + classifierName[pos];
        pos++;      

//        transl += sep + "-seed" + sep + this.seed;
        
        return new TranslationProcess(transl, pos);
    }   
    
    public TranslationProcess translate_KNN(String[] classifierName, String transl_partial, int pos_partial, String sep) {
        String transl = transl_partial;
        int pos = pos_partial;
        
        transl += sep + "-K" + sep + classifierName[pos];
        pos++;  
        
        return new TranslationProcess(transl, pos);
    }        
    
    
    public TranslationProcess translate_LogisticReg(String[] classifierName, String transl_partial, int pos_partial, String sep) {
        String transl = transl_partial;
        int pos = pos_partial;
        
        transl += sep + "-R" + sep + classifierName[pos];
        pos++;
        
        return new TranslationProcess(transl, pos);
    }
    
    public TranslationProcess translate_OneR(String[] classifierName, String transl_partial, int pos_partial, String sep) {
        String transl = transl_partial;
        int pos = pos_partial;
        
        transl += sep + "-B" + sep + classifierName[pos];
        pos++;
        
        return new TranslationProcess(transl, pos);
    }     
    
    public TranslationProcess translate_PART(String[] classifierName, String transl_partial, int pos_partial, String sep) {
        String transl = transl_partial;
        int pos = pos_partial;
        
        transl += sep + "-C" + sep + classifierName[pos];
        pos++;
        transl += sep + "-M" + sep + classifierName[pos];
        pos++;        

        
//        transl += sep + "-Q" + sep + this.seed;   
        
        return new TranslationProcess(transl, pos);
    }       
    
    
    public TranslationProcess translate_RandomForest(String[] classifierName, String transl_partial, int pos_partial, String sep) {
        String transl = transl_partial;
        int pos = pos_partial;
        
        transl += sep + "-I" + sep + classifierName[pos];
        pos++;

//        transl += sep + "-S" + sep + this.seed;
        
        return new TranslationProcess(transl, pos);
    }    
    
    public TranslationProcess translate_J48(String[] classifierName, String transl_partial, int pos_partial, String sep) {
        String transl = transl_partial;
        int pos = pos_partial;
        
        transl += sep + "-C" + sep + classifierName[pos];
        pos++;     
        transl += sep + "-M" + sep + classifierName[pos];
        pos++;        

//        transl += sep + "-Q" + sep + this.seed;

        return new TranslationProcess(transl, pos);
    }     
    
    
    public TranslationProcess translate_NaiveBayes(String[] classifierName, String transl_partial, int pos_partial, String sep) {
        String transl = transl_partial;
        int pos = pos_partial;
        
        return new TranslationProcess(transl, pos);
    }    
    
    
    public TranslationProcess translate_TAN(String[] classifierName, String transl_partial, int pos_partial, String sep) {
        String transl = transl_partial;
        int pos = pos_partial;

        transl += sep + "-D" + sep + "-Q";
        transl += sep + "weka.classifiers.bayes.net.search.local.TAN  --";
        
        transl += sep + "-S" + sep + classifierName[pos];
         pos++; 
        transl += sep + "-E weka.classifiers.bayes.net.estimate.SimpleEstimator --";
        transl += sep + "-A" + sep + classifierName[pos];
         pos++; 
        
        
        return new TranslationProcess(transl, pos);
    } 
    
    
    public TranslationProcess translate_HC(String[] classifierName, String transl_partial, int pos_partial, String sep) {
        String transl = transl_partial;
        int pos = pos_partial;

        transl += sep + "-D" + sep + "-Q";
        transl += sep + "weka.classifiers.bayes.net.search.local.HillClimber  --";

        transl += sep + "-S" + sep + classifierName[pos];
        pos++;
        transl += sep + "-P" + sep + classifierName[pos];
        pos++;

        transl += sep + "-E weka.classifiers.bayes.net.estimate.SimpleEstimator --";
        transl += sep + "-A" + sep + classifierName[pos];
        pos++;
        
        return new TranslationProcess(transl, pos);
    }    
    

/** ################################################################################################################################## **/       
/** ################################################################################################################################## **/   
/** ################################################################################################################################## **/   
        
    
    /**
     * The translation process for various algorithms (e.g., CC, PCC, BR, FW, LC).
     * @param classifierName the array having the classifier name;
     * @param transl_partial the partial translation to a command;
     * @param pos_partial the current position in the array;
     * @param meta if the complete algorithm is a meta MLC algorithm or not.
     * @return TranslationProcess containing the command for various
     *         algorithms and its current position in the array.
     */
    public TranslationProcess translate_ManyMethods(String[] classifierName, String transl_partial, int pos_partial, boolean meta) {
        String transl = transl_partial;
        int pos = pos_partial;
//        String id = classifierName[pos-1];   
        if(meta){
            transl += " -- ";        
        }

//        if (id.equals("CC") || (id.equals("PCC"))) {
//            transl += " -S " + this.seed;
//        }

        return new TranslationProcess(transl, pos);
    }      
    
    /**
     * The translation process for the BRq and CCq algorithms.
     * @param classifierName the array having the classifier name;
     * @param transl_partial the partial translation to a command;
     * @param pos_partial the current position in the array;
     * @param meta if the complete algorithm is a meta MLC algorithm or not.
     * @return TranslationProcess containing the command for the BRq and CCq 
     *         algorithms and its current position in the array.
     */   
    public TranslationProcess translate_BRq_CCq(String[] classifierName, String transl_partial, int pos_partial, boolean meta) {
        String transl = transl_partial;
        int pos = pos_partial;
        if(meta){
            transl += " --";        
        }
        transl += " -P " + classifierName[pos];
        pos++;
//        transl += " -S " + this.seed;

        return new TranslationProcess(transl, pos);
    }    
    
    
    /**
     * The translation process for the CDN algorithm.
     * @param classifierName the array having the classifier name;
     * @param transl_partial the partial translation to a command;
     * @param pos_partial the current position in the array;
     * @param meta if the complete algorithm is a meta MLC algorithm or not.
     * @return TranslationProcess containing the command for the CDN algorithm
     *         and its current position in the array.
     */   
    public TranslationProcess translate_CDN(String[] classifierName, String transl_partial, int pos_partial, boolean meta) {
        String transl = transl_partial;
        int pos = pos_partial;
        if(meta){
            transl += " --";        
        }
        transl += " -I " + classifierName[pos];
        pos++;
        transl += " -Ic " + classifierName[pos];
        pos++;
//        transl += " -S " + this.seed;

        return new TranslationProcess(transl, pos);
    }    
    
    
    /**
     * The translation process for the CDT algorithm.
     * @param classifierName the array having the classifier name;
     * @param transl_partial the partial translation to a command;
     * @param pos_partial the current position in the array;
     * @param meta if the complete algorithm is a meta MLC algorithm or not.
     * @return TranslationProcess containing the command for the CDT algorithm
     *         and its current position in the array.
     */   
    public TranslationProcess translate_CDT(String[] classifierName, String transl_partial, int pos_partial, boolean meta) {
        String transl = transl_partial;
        int pos = pos_partial;
        if(meta){
            transl += " --";        
        }
        transl += " -H " + classifierName[pos];
        pos++;
        transl += " -L " + classifierName[pos];
        pos++;
        transl += " -X " + classifierName[pos];
        pos++;
        transl += " -I " + classifierName[pos];
        pos++;
        transl += " -Ic " + classifierName[pos];
        pos++;
//        transl += " -S " + this.seed;
        
        return new TranslationProcess(transl, pos);
    }    
    
    
    
        /**
     * The translation process for the CT algorithm.
     * @param classifierName the array having the classifier name;
     * @param transl_partial the partial translation to a command;
     * @param pos_partial the current position in the array;
     * @param meta if the complete algorithm is a meta MLC algorithm or not.
     * @return TranslationProcess containing the command for the CT algorithm
     *         and its current position in the array.
     */ 
    public TranslationProcess translate_CT(String[] classifierName, String transl_partial, int pos_partial, boolean meta) {
        String transl = transl_partial;
        int pos = pos_partial;
        if(meta){
            transl += " --";        
        }
        transl += " -Is " + classifierName[pos];
        pos++;
        transl += " -H " + classifierName[pos];
        pos++;
        transl += " -L " + classifierName[pos];
        pos++;
        transl += " -X " + classifierName[pos];
        pos++;
        transl += " -Iy " + classifierName[pos];
        pos++;
//        transl += " -S " + this.seed;

        return new TranslationProcess(transl, pos);
    }
    
        /**
     * The translation process for the MCC algorithm.
     * @param classifierName the array having the classifier name;
     * @param transl_partial the partial translation to a command;
     * @param pos_partial the current position in the array;
     * @param meta if the complete algorithm is a meta MLC algorithm or not.
     * @return TranslationProcess containing the command for the MCC algorithm
     *         and its current position in the array.
     */   
    public TranslationProcess translate_MCC(String[] classifierName, String transl_partial, int pos_partial, boolean meta) {
        String transl = transl_partial;
        int pos = pos_partial;
        if(meta){
            transl += " --";        
        }
        transl += " -Is " + classifierName[pos];
        pos++;
        transl += " -Iy " + classifierName[pos];
        pos++;
//        transl += " -S " + this.seed;

        return new TranslationProcess(transl, pos);
    }
    
    
    /**
     * The translation process for the PS algorithm.
     * @param classifierName the array having the classifier name;
     * @param transl_partial the partial translation to a command;
     * @param pos_partial the current position in the array;
     * @param meta if the complete algorithm is a meta MLC algorithm or not.
     * @return TranslationProcess containing the command for the PS algorithm
     *         and its current position in the array.
     */
    public TranslationProcess translate_PS(String[] classifierName, String transl_partial, int pos_partial, boolean meta) {
        String transl = transl_partial;
        int pos = pos_partial;
        if(meta){
            transl += " --";        
        }
        transl += " -N " + classifierName[pos];
        pos++;
        transl += " -P " + classifierName[pos];
        pos++;
//        transl += " -S " + this.seed;
        
        return new TranslationProcess(transl, pos);
    }    
    
    /**
     * The translation process for the RAkEL and RAkELd algorithms to a command.
     * @param classifierName the array having the classifier name and parameters;
     * @param transl_partial the partial translation to a command;
     * @param pos_partial the current position in the array;
     * @param meta if the complete algorithm is a meta MLC algorithm or not.
     * @return TranslationProcess containing the command for the RAkEL and RAkELd
     *         algorithms and their current positions in the array.
     */
    public TranslationProcess translate_RAkEL(String[] classifierName, String transl_partial, int pos_partial, boolean meta) {
        String transl = transl_partial;
        int pos = pos_partial;
        String id = classifierName[pos-1];
        if(meta){
            transl += " --";        
        }        
        if (id.equals("RAkEL")) {
            transl += " -M " + classifierName[pos];
            pos++;
        }
        transl += " -k " + classifierName[pos];
        pos++;
        transl += " -N " + classifierName[pos];
        pos++;
        transl += " -P " + classifierName[pos];
        pos++;
//        transl += " -S " + this.seed;
        
        return new TranslationProcess(transl, pos);
    }    
    
    
    
    /**
     * The translation process for the PMCC algorithm to a command.
     * @param classifierName the array having the classifier name and parameters;;
     * @param transl_partial the partial translation to a command;
     * @param pos_partial the current position in the array;
     * @param meta if the complete algorithm is a meta MLC algorithm or not.
     * @return TranslationProcess containing the command for the PMCC algorithm
     *         and its current position in the array.
     */
    public TranslationProcess translate_PMCC(String[] classifierName, String transl_partial, int pos_partial, boolean meta) {
        String transl = transl_partial;
        int pos = pos_partial;
        if(meta){
            transl += " --";        
        }        
        transl += " -B " + classifierName[pos];
        pos++;
        transl += " -O " + classifierName[pos];
        pos++;
        transl += " -Iy " + classifierName[pos];
        pos++;
        transl += " -Is " + classifierName[pos];
        pos++;
        transl += " -M " + classifierName[pos];
        pos++;
//        transl += " -S " + this.seed;   
        
        return new TranslationProcess(transl, pos);
    }    
    
    
    /**
     * The translation process for the BCC algorithm to a command.
     * @param classifierName the array having the classifier name and parameters;
     * @param transl_partial the partial translation to a command;
     * @param pos_partial the current position in the array;
     * @param meta if the complete algorithm is a meta MLC algorithm or not.
     * @return TranslationProcess containing the command for the BCC algorithm
     *         and its current position in the array.
     */
    public TranslationProcess translate_BCC(String[] classifierName, String transl_partial, int pos_partial, boolean meta) {
        String transl = transl_partial;
        int pos = pos_partial;
        if(meta){
            transl += " --";        
        }
        transl += " -X "+ classifierName[pos];
        pos++;
//        transl += " -S " + this.seed;
        
        return new TranslationProcess(transl, pos);
    }    
    
    

/** ################################################################################################################################## **/       
/** ################################################################################################################################## **/   
/** ################################################################################################################################## **/   
    
    /**
     * The translation process for the MLBPNN algorithm.
     * @param classifierName the array having the classifier name;
     * @param transl_partial the partial translation to a command;
     * @param pos_partial the current position in the array;
     * @param meta if the complete algorithm is a meta MLC algorithm or not.
     * @return TranslationProcess containing the command for the MLBPNN algorithm
     *         and its current position in the array.
     */  
    public TranslationProcess translate_MLBPNN(String[] classifierName, String transl_partial, int pos_partial, boolean meta) {
        String transl = transl_partial;
        int pos = pos_partial;
        
        if(meta){
            transl += " --";        
        }  

        transl += " -E " + classifierName[pos];
        pos++;
        transl += " -H " + classifierName[pos];
        pos++;
        transl += " -r " + classifierName[pos];
        pos++;
        transl += " -m " + classifierName[pos];
        pos++;
//        transl += " -S " + this.seed;        
        
        return new TranslationProcess(transl, pos);
    }    
    
 
    /**
     * The translation process for the ML-DBPNN algorithm.
     * @param classifierName the array having the classifier name;
     * @param transl_partial the partial translation to a command;
     * @param pos_partial the current position in the array;
     * @return TranslationProcess containing the command for the ML-DBPNN algorithm
     *         and its current position in the array.
     */   
    public TranslationProcess translate_MLDBPNN(String[] classifierName, String transl_partial, int pos_partial, boolean meta) {
        String transl = transl_partial;
        int pos = pos_partial;
        
        if(meta){
            transl += " --";        
        }  

        transl += " -E_dbpnn " + classifierName[pos];
        pos++;
        transl += " -H_dbpnn " + classifierName[pos];
        pos++;
        transl += " -R_dbpnn " + classifierName[pos];
        pos++;
        transl += " -M_dbpnn " + classifierName[pos];
        pos++;
        transl += " -N " + classifierName[pos];
        pos++;
        pos++;
        transl += " -E " + classifierName[pos];
        pos++;
        transl += " -H " + classifierName[pos];
        pos++;
        transl += " -r " + classifierName[pos];
        pos++;
        transl += " -m " + classifierName[pos];
        pos++;
//        transl += " -S " + this.seed;

        return new TranslationProcess(transl, pos);
    }
        

/** ################################################################################################################################## **/       
/** ################################################################################################################################## **/   
/** ################################################################################################################################## **/   
        
    public TranslationProcess translate_RSML(String[] classifierName, String transl_partial, int pos_partial, String trainingSet, String validationSet) {
        String transl = transl_partial;
        int pos = pos_partial;

        transl += " -P " + classifierName[pos];
        pos++;
        transl += " -I " + classifierName[pos];
        pos++;
        transl += " -A " + classifierName[pos];
        pos++;      
        
        return new TranslationProcess(transl, pos);
    }        
    
    public TranslationProcess translate_BaggingDup_EnsembleML(String[] classifierName, String transl_partial, int pos_partial, String trainingSet, String validationSet) {
        String transl = transl_partial;
        int pos = pos_partial;    
        
        transl += " -P " + classifierName[pos];
        pos++;       
        transl += " -I " + classifierName[pos];
        pos++;
        
        return new TranslationProcess(transl, pos);
    } 
     
    public TranslationProcess translate_Bagging(String[] classifierName, String transl_partial, int pos_partial, String trainingSet, String validationSet) {
        String transl = transl_partial;
        int pos = pos_partial; 

        transl += " -I " + classifierName[pos];
        pos++;
        
        return new TranslationProcess(transl, pos);
    }     
    

    
/** ################################################################################################################################## **/       
/** ################################################################################################################################## **/   
/** ################################################################################################################################## **/   
      
    
    public TranslationProcess translate_HOMER(String[] classifierName, String transl_partial, int pos_partial, String trainingSet, String validationSet, String threshold) {
        String transl = transl_partial;
        int pos = pos_partial;     
        TranslationProcess tp = null;
        
        transl += "." + classifierName[pos];
        pos++;
        transl += "." + classifierName[pos];
        pos++;
        
        if(classifierName[pos].equals("BR")){
            transl += ".BinaryRelevance";              
        }else if(classifierName[pos].equals("CC")){
            transl += ".ClassifierChain";
        }else if(classifierName[pos].equals("LP")){
            transl += ".LabelPowerset";
        }
        pos++;  
        transl += threshold + " -t " + trainingSet +" -T " +validationSet + " -verbosity 6 "; 
        
        tp = new TranslationProcess(transl, pos);
        tp.setIsHOMER(true);
     
        return tp;
    }    
    
  
    
    public TranslationProcess translate_EM_CM(String[] classifierName, String transl_partial, int pos_partial, String trainingSet, String validationSet) {
        String transl = transl_partial;
        int pos = pos_partial;      

        transl += " -I " + classifierName[pos];        
        pos++;
        
        return new TranslationProcess(transl, pos);
    }
    
    
    
}
