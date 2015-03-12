package lcp_app;

import java.util.logging.Level;

public class LCP_App 
{
    public static void main(String[] args) 
    {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        
        Worker myWorker = new Worker();
        myWorker.createAndShowGUI();
    }
}
