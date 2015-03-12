package lcp_app;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Worker extends JPanel implements ActionListener 
{
  JButton openButton;
  JFileChooser fc;

  public Worker() 
  {
    super(new BorderLayout());
    
    //Create a file chooser
    fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

    openButton = new JButton("Upload File");
    openButton.addActionListener(this);

    //For layout purposes, put the buttons in a separate panel
    JPanel buttonPanel = new JPanel(); //use FlowLayout
    buttonPanel.add(openButton);
    
    //Add the buttons and the log to this panel.
    add(buttonPanel, BorderLayout.PAGE_START);
  }

  public void actionPerformed(ActionEvent e) 
  {
    File file;
    Crawler myC = new Crawler();
    if(e.getSource() == openButton) 
    {
      int returnVal = fc.showOpenDialog(Worker.this);

      if(returnVal == JFileChooser.APPROVE_OPTION) 
      {
        file = fc.getSelectedFile();
        myC.goCrawl(file);
      }
    } 
  }

  public void createAndShowGUI() 
  {
    //Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(true);
    JDialog.setDefaultLookAndFeelDecorated(true);

    //Create and set up the window.
    JFrame frame = new JFrame("SwingFileChooserDemo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Create and set up the content pane.
    JComponent newContentPane = new Worker();
    newContentPane.setOpaque(true); //content panes must be opaque
    frame.setContentPane(newContentPane);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }
}