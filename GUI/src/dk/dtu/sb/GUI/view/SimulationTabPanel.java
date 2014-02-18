package dk.dtu.sb.GUI.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import dk.dtu.ls.library.SBGate;
import dk.dtu.sb.Parameters;
import dk.dtu.sb.Util;
import dk.dtu.sb.data.SimulationResult;
import dk.dtu.sb.outputformatter.CSV;
import dk.dtu.sb.outputformatter.GraphPanel;

import javax.swing.JLabel;

import java.awt.Font;

@SuppressWarnings("serial")
public class SimulationTabPanel extends JPanel {
    
    private final SimulationResult simData;
    private final JFileChooser fileChooser = new JFileChooser();

    /**
     * Create the panel.
     */
    public SimulationTabPanel(SimulationResult data, String outputProtein, SBGate gate) {
        this.simData = data;
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);
        
        String title = "";
        if (gate != null) {
            title = gate.SOP;
            if (gate.isLibraryPart) {
                title += " | ID: " + gate.id;
            }
        }
        JLabel lblGate = new JLabel(title);
        lblGate.setFont(new Font("Lucida Grande", Font.BOLD, 14));
        GridBagConstraints gbc_lblGate = new GridBagConstraints();
        gbc_lblGate.anchor = GridBagConstraints.NORTH;
        gbc_lblGate.insets = new Insets(0, 0, 5, 0);
        gbc_lblGate.gridx = 0;
        gbc_lblGate.gridy = 0;
        add(lblGate, gbc_lblGate);

        final JPanel panel = new GraphPanel(data, outputProtein);
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.insets = new Insets(0, 0, 5, 0);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 1;
        add(panel, gbc_panel);

        JButton btnExport = new JButton("Export to CSV");
        GridBagConstraints gbc_btnExport = new GridBagConstraints();
        gbc_btnExport.anchor = GridBagConstraints.EAST;
        gbc_btnExport.gridx = 0;
        gbc_btnExport.gridy = 2;
        add(btnExport, gbc_btnExport);
        
        btnExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showSaveDialog(panel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    Util.log.info("Exporting to: " + file.getAbsolutePath());
                    Parameters params = new Parameters();
                    params.setOutputFilename(file.getAbsolutePath());
                    CSV csv = new CSV();
                    csv.process(simData, params);
                    Util.log.info("Export done...");
                }
            }
        });
    }

}
