/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meka.classifiers.multilabel.meta.gaautomlc.core.xmlparser;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author luiz
 */
public class Allele {
    private String m_comment;
    private String m_parameter; 
    private String m_counting;
    private int m_totalCount;
    private LinkedHashMap<String, String> intevalosAlg;
    private String m_type;
    protected int m_step;
    
    public ArrayList<Allele> m_alleleList;

    public Allele(String comment, String parameter, String counting, String type) {
        this.m_comment = comment;
        this.m_parameter = parameter;
        this.m_counting = counting;
        this.m_alleleList = new ArrayList<Allele>();
        this.m_totalCount = 0;
        this.intevalosAlg = new LinkedHashMap<String,String>();
        this.m_type = type;
    }

    public int getM_step() {
        return m_step;
    }

    
    public String getM_type() {
        return m_type;
    }

    
    public String getComment() {
        return m_comment;
    }

    public String getParameter() {
        return m_parameter;
    }
    
    public void setParameter(String parameter){
        m_parameter = parameter;        
    }

    public String getM_counting() {
        return m_counting;
    }

    public void setM_totalCount(int m_totalCount) {
        this.m_totalCount = m_totalCount;
    }

    public int getM_totalCount() {
        return m_totalCount;
    }

    public LinkedHashMap<String, String> getIntevalosAlg() {
        return intevalosAlg;
    }

    public void setIntevalosAlg(LinkedHashMap<String, String> intevalosAlg) {
        this.intevalosAlg = intevalosAlg;
    }
    
    
    
    public void addAllele(Allele allele){
        m_alleleList.add(allele);
    }
    
    

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("\"" + m_comment  + "\"" + " \"" + m_parameter + "\"\n");
        for(Allele allele : m_alleleList){
            strBuilder.append(allele.toString("\t"));
        }
        return strBuilder.toString();
    }
    
    public String toString(String prefix) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(prefix + "\"" + m_comment  + "\"" + " \"" + m_parameter + "\"\n");
        for(Allele allele : m_alleleList){
            strBuilder.append(allele.toString(prefix + "\t"));
        }
        return strBuilder.toString();
    }
    
    public Allele get(int i){
        return m_alleleList.get(i);
    }
    
    public int size(){
        return m_alleleList.size();
    }
    
    public boolean isEmpty(){
        if(m_alleleList == null){
            return true;
        }
        return false;
    }
    
    public boolean isOption(){
        return false;
    }
}
