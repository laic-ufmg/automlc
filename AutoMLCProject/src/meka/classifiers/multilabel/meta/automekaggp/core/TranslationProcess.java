/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meka.classifiers.multilabel.meta.automekaggp.core;

/**
 *
 * @author alexgcsa
 */
public class TranslationProcess {
    private int pos;
    private String commandString;
    private boolean isHOMER;

    public TranslationProcess(String commandString, int pos) {
        this.pos = pos;
        this.commandString = commandString;
        this.isHOMER = false;
    }

    public int getPos() {
        return this.pos;
    }

    public String getGeneratedCommand() {
        return this.commandString;
    }
    
    public void setIsHOMER(boolean isHOMER){
        this.isHOMER = isHOMER;
    }
    
    public boolean getIsHOMER(){
        return this.isHOMER;
    }

    public void setCommand(String commandString) {
        this.commandString = commandString;
    }
    
    
    
    
    
    
    
    
    
    
}
