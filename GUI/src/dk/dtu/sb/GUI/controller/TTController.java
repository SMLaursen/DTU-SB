package dk.dtu.sb.GUI.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.github.qtstc.Formula;

import dk.dtu.ls.library.SBGate;
import dk.dtu.sb.Util;
import dk.dtu.sb.GUI.Model;
import dk.dtu.sb.GUI.view.TruthTablePanel;
import dk.dtu.techmap.AIG;
import dk.dtu.techmap.TechnologyMapper;

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
                        new JLabel("Inputs (seperate by comma)"), input,
                        new JLabel("Output"), output, };
                JOptionPane.showMessageDialog(null, inputs, "Add",
                        JOptionPane.PLAIN_MESSAGE);
                createNewTT(input.getText(), output.getText());
            }
        });

        view.btnMinimise.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                minimiseTT();
            }
        });

        view.btnFindFromTT.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String mini = minimiseTT();
                // view.populateResultsList(findDesigns(mini));
                findDesigns(mini);
            }
        });

        view.btnFindFromSop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // view.populateResultsList(findDesigns(view.textAreaMinimised
                // .getText()));
                findDesigns(view.textAreaMinimised.getText());
            }
        });

        view.btnLoadSelectedDesign.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int index = view.resultsList.getSelectedIndex();
                if (index != -1) {
                    model.loadNewSBGate(model.newDesignsFromTT.get(index));
                }
            }
        });

        view.resultsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent arg0) {
                int index = view.resultsList.getSelectedIndex();
                if (index != -1) {
                    view.sbGateDetailsPanel.setDetails(model.newDesignsFromTT
                            .get(index));
                    view.btnLoadSelectedDesign.setEnabled(true);
                }
            }
        });
    }

    private void findDesigns(final String SOP) {        
        (new SwingWorker<Boolean, Boolean>() {
            protected Boolean doInBackground() throws Exception {
                Util.log.info("Finding designs based on SOP: " + SOP);
                TechnologyMapper techMap = new TechnologyMapper(new AIG(SOP));
                techMap.start();
                Util.log.info(techMap.getSolutions().size() + " designs found.");
                ArrayList<SBGate> result = new ArrayList<SBGate>();
                Util.log.info("Loading designs.");
                String path;
                if (TTController.class.getResource("TTController.class")
                        .toString().startsWith("jar")) {
                    path = "";
                } else {
                    path = "../logic-synthesis/";
                }
                for (HashSet<SBGate> solution : techMap.getSolutions()) {
                    for (SBGate gate : solution) {
                        gate.sbmlFile = path + gate.sbmlFile;
                    }
                }

                for (HashSet<SBGate> solution : techMap.getSolutions()) {
                    SBGate newGate = SBGate.compose(solution);
                    newGate.SOP = SOP;
                    result.add(newGate);
                }
                //Sort list
                Collections.sort(result);

                model.newDesignsFromTT = result;
                return true;
            }

            protected void done() {
                if (model.newDesignsFromTT.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "No designs could be found.");
                } else {
                    view.populateResultsList(model.newDesignsFromTT);
                }
            }
        }).execute();
        /*
         * TechnologyMapper techMap = new TechnologyMapper(new AIG(SOP));
         * techMap.start(); ArrayList<SBGate> result = new ArrayList<SBGate>();
         * 
         * String path; if
         * (TTController.class.getResource("TTController.class").toString()
         * .startsWith("jar")) { path = ""; } else { path =
         * "../logic-synthesis/"; } for (HashSet<SBGate> solution :
         * techMap.getSolutions()) { for (SBGate gate : solution) {
         * gate.sbmlFile = path + gate.sbmlFile; } }
         * 
         * if (!techMap.getSolutions().isEmpty()) { for (HashSet<SBGate>
         * solution : techMap.getSolutions()) { SBGate newGate =
         * SBGate.compose(solution); newGate.SOP = SOP; result.add(newGate); } }
         * else { JOptionPane.showMessageDialog(null,
         * "No designs could be found."); }
         * 
         * model.newDesignsFromTT = result; return result;
         */
    }

    private String minimiseTT() {
        String tt = view.truthTableRaw.getText(), mini = "";
        try {
            Formula f = Formula.readCompleteTT(new StringReader(tt));
            f.reduceToPrimeImplicants();
            f.reducePrimeImplicantsToSubset();
            mini = f.toString();
            view.textAreaMinimised.setText(mini);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return mini;
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
