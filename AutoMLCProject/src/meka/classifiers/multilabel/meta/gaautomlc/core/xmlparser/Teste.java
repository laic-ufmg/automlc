/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meka.classifiers.multilabel.meta.gaautomlc.core.xmlparser;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author luiz
 */
public class Teste {
    public static void main(String args[]) throws Exception{
        XMLGeneHandler ch = new XMLGeneHandler(new File("/home/luiz/Dados/Trabalho/Pesquisa/Doutorado/CoMet/CoMet/template.xml"));
        Allele comp = ch.getGenes();
        for(Allele al :  comp.m_alleleList){
            System.out.println(al.toString());
        }
        int x = 0;
    }
}
