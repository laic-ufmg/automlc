/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meka.classifiers.multilabel.meta.gaautomlc.core.xmlparser;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author luiz
 */
public class XMLAlgorithmHandler {

    public static String getPath(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private File m_xmlPath;
    private final String str_algorithms = "algorithms";
    private final String str_path = "path";
    
    protected ArrayList<String> m_algorithmsFiles;

    
    public XMLAlgorithmHandler(File xmlPath) throws Exception{
        m_xmlPath = xmlPath;
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
//            String depth = doc.getDocumentElement().getAttribute("maxDepth");
//            m_depth = Integer.parseInt(depth);
            // ===================================================== PROVISORIO ======================================================================================
            m_algorithmsFiles = getPaths(doc.getDocumentElement(), str_algorithms);
        
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

    private ArrayList<String> getPaths(Element documentElement, String type) {
        ArrayList<Node> nodeList = getChildrenByTagName(documentElement, type);
        ArrayList<String> paths = new ArrayList<String>();
        for(Node node : nodeList){
            ArrayList<Node> pathNodes = getChildrenByTagName((Element)node, str_path);
            for(Node pathNode : pathNodes){
                String path = pathNode.getChildNodes().item(0).getNodeValue();
                paths.add(getFullPath(path));
            }
        }
        return paths;
    }

    public ArrayList<String> getAlgorithmsFiles() {
        return m_algorithmsFiles;
    }
    


    private String getFullPath(String path) {
        if(path.startsWith("~")){
            path = path.replaceFirst("~", System.getProperty("user.home"));
        }
        return path;
    }
}
