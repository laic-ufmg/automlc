/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meka.classifiers.multilabel.meta.gaautomlc.core.xmlparser;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author luiz
 */
public class XMLGeneHandler {
    private Allele m_genome;
    private File m_xmlPath;
    private int m_depth;
    
    private final String str_gene = "gene";
    private final String str_allele = "allele";
    private final String str_parameter = "parameter";
    private final String str_comment = "comment";
    private final String str_type = "type";
    private final String str_step = "step";
    private final String str_counting = "counting";
    
   int totalCount = 0;
   LinkedHashMap<String, String> intevalosAlg = null;
    
    public XMLGeneHandler(File xmlPath) throws Exception{
        m_xmlPath = xmlPath;
        this.intevalosAlg = new LinkedHashMap<String, String>();
        parsing();
    }    
    /**
     * @param args the command line arguments
     */
    private void parsing() throws Exception{
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(m_xmlPath);            
            
            // ===================================================== PROVISORIO ======================================================================================
            String depth = doc.getDocumentElement().getAttribute("maxDepth");
            m_depth = Integer.parseInt(depth);
            // ===================================================== PROVISORIO ======================================================================================
            
            ArrayList<Node> nodes = getChildrenByTagName(doc.getDocumentElement(), str_gene);
            
            m_genome = getGene(nodes.get(0));
            m_genome.setM_totalCount(this.totalCount);
            m_genome.setIntevalosAlg(intevalosAlg);

        }
        catch(Exception e){
            throw new Exception("XML parser error", e);
        }
    }
    
    private ArrayList<Node> getChildrenByTagName(Element documentElement, String str_component) {
        ArrayList<Node> nodeList = new ArrayList<Node>();
        for (Node child = documentElement.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE && str_component.equals(child.getNodeName())) {
                nodeList.add((Element) child);
            }
        }
        return nodeList;
    }
    
    /**
     * Recover a component from the node
     * @param node input node
     * @return recovered component
     * @throws Exception error on parsing
     */
    private Allele getGene(Node node) throws Exception{
        try{
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                
                String parameter = getAttribute(element, str_parameter);
                String comment = getAttribute(element, str_comment);
                String type = getAttribute(element, str_type);
                String step = getAttribute(element, str_step);
                String counting = getAttribute(element, str_counting);                
                
                if(type.equals("")){
                    ArrayList<Node> nodes = getChildrenByTagName(element, str_allele);
                    
                    Allele gene = new Gene(comment, parameter, counting, "");
                    
                    for(int i = 0; i < nodes.size(); i++){
                        Node comp_node = nodes.get(i);
                        gene.addAllele(getAllele(comp_node));
                    }
                    
                    return gene;
                }
                // Else, the gene has alleles (values).
                else{
                    GeneOption gene = new GeneOption(comment, parameter, type, step, counting);
                    
                    ArrayList<Node> nodes = getChildrenByTagName(element, str_allele);
                    
                    for(int i = 0; i < nodes.size(); i++){
                        Element comp_node = (Element) nodes.get(i);
                        String value;
                        if(comp_node.getChildNodes().getLength() == 0){
                            value = "";
                        }
                        else{
                            value = comp_node.getChildNodes().item(0).getNodeValue();

                        }  
                        gene.addAllele(value, getAllele(comp_node).getComment());
                        
                    }
                    return gene;
                }
            }
        }
        catch (Exception ex) {
            throw new Exception("XML: Gene parser error", ex);
        }
        return null;
    }
    
    private Allele getAllele(Node node) throws Exception{
        Allele allele;
        
        try{
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                
                String parameter = getAttribute(element, str_parameter);
                String comment = getAttribute(element, str_comment);
                String counting = getAttribute(element, str_counting);
                
                if(!counting.equals("")){
                    String intervalo = this.totalCount +";";
                    this.totalCount += Integer.parseInt(counting);
                    intervalo += this.totalCount +"";
                    this.intevalosAlg.put(parameter, intervalo);
                }
                 
                allele = new Allele(comment, parameter, counting,"set");
                
                ArrayList<Node> nodes = getChildrenByTagName(element, str_gene);
                for(int i = 0; i < nodes.size(); i++){
                    allele.addAllele(getGene(nodes.get(i)));
                }
                return allele;
            }
        }
        catch (Exception ex) {
            throw new Exception("XML: Allele  parser error", ex);
        }
        return null;
    }
    
    private String getAttribute(Element element, String attibuteName){
        String str = element.getAttribute(attibuteName);
        if(str == null) return "";
        else return str;
    }
    
    public Allele getGenes() {
        return m_genome;
    }

    public int getDepth() {
        return m_depth;
    }
}