package dk.dtu.sb.GUI.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.github.qtstc.Formula;

import dk.dtu.ls.library.Library;
import dk.dtu.ls.library.models.SBGate;
import dk.dtu.sb.GUI.Model;
import dk.dtu.sb.GUI.view.LoadSBMLPanel;
import dk.dtu.sb.GUI.view.TruthTablePanel;

public class TTController implements PropertyChangeListener {
    
    private TruthTablePanel view;
    private Model model;

    public TTController(TruthTablePanel view, Model model) {
        this.model = model;
        this.model.addListener(this);
        
        this.view = view;
        
        setUpViewEvents();
    }
    
    private void setUpViewEvents() {
        view.btnAddTruthTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JTextField input = new JTextField();
                JTextField output = new JTextField();
                final JComponent[] inputs = new JComponent[] {
                        new JLabel("Input proteins (seperate by comma)"),
                        input, new JLabel("Output protein"), output, };
                JOptionPane.showMessageDialog(null, inputs, "Add proteins",
                        JOptionPane.PLAIN_MESSAGE);
                createNewTT(input.getText(), output.getText());
            }
        });
        
        view.btnMinimise.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String tt = view.truthTableRaw.getText();
                try {
                    Formula f = Formula.readCompleteTT(new StringReader(tt));
                    f.reduceToPrimeImplicants();
                    f.reducePrimeImplicantsToSubset();
                    view.textAreaMinimised.setText(f.toString());
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
    }
    
    private void createNewTT(String input, String output) {
        String[] inputs = input.trim().replace(" ", "").split(",");
        String tt = "";
        for (int i = 0; i < inputs.length; i++) {
            tt += inputs[i] + " ";
        }
        tt += output + "\n";
        tt += TTEntries(inputs.length);

        view.truthTableRaw.setText(tt);
    }

    private String TTEntries(int n) {
        int rows = (int) Math.pow(2, n);
        String entries = "";
        for (int i = 0; i < rows; i++) {
            for (int j = n - 1; j >= 0; j--) {
                entries += (i / (int) Math.pow(2, j)) % 2;
            }
            entries += "0\n";
        }

        return entries;
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        
    }

}
