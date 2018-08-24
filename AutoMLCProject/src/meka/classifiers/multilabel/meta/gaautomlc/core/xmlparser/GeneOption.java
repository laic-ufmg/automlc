/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meka.classifiers.multilabel.meta.gaautomlc.core.xmlparser;

import java.util.ArrayList;

/**
 *
 * @author luiz
 */
public class GeneOption extends Allele{
    enum e_types{
        INT, FLOAT, SET
    }
    
    private e_types m_type;
    private double m_leftLimit, m_rightLimit;
    private boolean useGrammar = false;

//    private ArrayList<String> m_alleleList;

    public GeneOption(String comment, String parameter, String type) throws Exception {
        this(comment, parameter, type, "", "");
    }

    public GeneOption(String comment, String parameter, String type, String step, String counting) throws Exception {
        super(comment, parameter, counting, type);
        setType(type, step);
    }
    
    

    private void setType(String type, String step) throws Exception {
        if(type.equalsIgnoreCase("int")){
            if(step.isEmpty()){
                m_step = 1;
            }
            else{
                m_step = Integer.parseInt(step);
            }
            m_type = e_types.INT;
        }
        else if(type.equalsIgnoreCase("float")){
            if(step.isEmpty()){
                m_step = 10;
            }
            else{
                m_step = Integer.parseInt(step);
            }
            m_type = e_types.FLOAT;
        }
        else if(type.equalsIgnoreCase("set")){
            m_type = e_types.SET;
        }
        else{
            throw new Exception("XML parser error: Option type unknown.");
        }
    }
    
//    public int size(){
//        switch(m_type){
//            case SET:
//                return m_alleleList.size();
//            case INT:
//                return getIntIntervalSize();
//            case FLOAT:
//                return getFloatIntervalSize();
//        }
//        return 0;
//    }

    @Override
    public boolean isEmpty() {
        if(size() == 0) return true;
        return false;
    }    
    
    private int getIntIntervalSize() {
        return (int)((m_rightLimit - m_leftLimit)/m_step)+1;
    }

    private int getFloatIntervalSize() {
        return m_step;
    }
    
    public void addAllele(String input, String input2) throws Exception{
    	if(input == null){
            input = "";
        }
        switch(m_type){
            case SET:
                addElement(input, input2);
                break;
            case INT:
                setInterval(input);
                break;
            case FLOAT:
                setInterval(input);
                break;
        }  
    }

    private void addElement(String input, String input2){
        if(m_alleleList == null){
            m_alleleList = new ArrayList<Allele>();
        }
        m_alleleList.add(new Allele(input2, input,"","set"));
    }
    
    private void setInterval(String input) throws Exception{
        String[] strSplit = input.split(",");
        try{
            // Store the right and left limit
            m_leftLimit = Double.parseDouble(strSplit[0]);
            m_rightLimit = Double.parseDouble(strSplit[1]);
            
            
            buildIntervalAlleles();
        }        
        catch(NumberFormatException e){
            throw new Exception("XML parser error: Float/int interval conversion", e);
        }
    }
    
    private void buildIntervalAlleles() {
        m_alleleList = new ArrayList<Allele>();
        if(m_type == e_types.FLOAT){
            double increment = (m_rightLimit - m_leftLimit)/(double)(m_step-1);
            double start = m_leftLimit;
            for(int i = 0; i < m_step; i++){
                m_alleleList.add(new Allele("value", start + "","","float"));
                start += increment;
            }
        }
        else{
            for(int start = (int)m_leftLimit; start <= m_rightLimit; start += m_step){
                m_alleleList.add(new Allele("value", start + "","","int"));
            }
        }
    }
    
    
    

//    @Override
//    public Allele get(int i) {
//        if(m_type == e_types.FLOAT || m_type == e_types.INT){
//            
//        }
//        return super.get(i); //To change body of generated methods, choose Tools | Templates.
//    }
    
    
    
    @Override
    public String toString(String prefix) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(prefix + getComment()  + " " + getParameter() + "\n");
        if(m_type == e_types.SET){
            for(Allele allele : m_alleleList){
                strBuilder.append(prefix + "\t" + allele.getParameter() + "\n");
            }
        }
        else if(m_type == e_types.INT){
            for(int value = (int)m_leftLimit; value <= m_rightLimit; value += m_step){
                strBuilder.append(prefix + "\t" + value + "\n");
            }
        }
        else if(m_type == e_types.FLOAT){
            double stepSize = (m_rightLimit - m_leftLimit)/(m_step - 1);
            for(double value = m_leftLimit; value <= m_rightLimit; value += stepSize){
                strBuilder.append(prefix + "\t" + value + "\n");
            }
        }
        return strBuilder.toString();
    }

    @Override
    public boolean isOption() {
        return true;
    }
}
