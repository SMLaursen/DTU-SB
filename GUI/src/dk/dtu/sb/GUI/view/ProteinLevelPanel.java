package dk.dtu.sb.GUI.view;

import javax.swing.JPanel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import dk.dtu.ls.library.SBGate;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JSpinner;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class ProteinLevelPanel extends JPanel {

    private JSpinner levelSpinner;
    private JRadioButton radioHigh;
    private JRadioButton radioLow;
    
    private String proteinName;

    /**
     * Create the panel.
     */
    public ProteinLevelPanel(String name, int initLevel) {
        this.proteinName = name;
        setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("left:max(32dlu;default):grow"),
                FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,},
            new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,}));
        
        JLabel lblProtein = new JLabel(name);
        lblProtein.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        add(lblProtein, "2, 2");
        
        levelSpinner = new JSpinner();
        levelSpinner.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        add(levelSpinner, "2, 4, fill, default");
        
        radioHigh = new JRadioButton("H");
        radioHigh.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        add(radioHigh, "4, 4");
        
        radioLow = new JRadioButton("L");
        radioLow.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        add(radioLow, "6, 4");
        
        setupInitLevels(initLevel);
    }
    
    private void setupInitLevels(int level) {
        changeRadioLevel(level);
        changeSpinnerLevel(level);
        radioHigh.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                if (radioHigh.isSelected()) {
                    changeSpinnerLevel(SBGate.HIGH);
                }
            }
        });
        radioLow.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                if (radioLow.isSelected()) {
                    changeSpinnerLevel(SBGate.LOW);
                }
            }
        });
        levelSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                changeRadioLevel((Integer)levelSpinner.getValue());
            }
        });
    }
    
    private void changeRadioLevel(int level) {
        if (level == SBGate.HIGH) {
            radioHigh.setSelected(true);
            radioLow.setSelected(false);
        } else if (level == SBGate.LOW) {
            radioHigh.setSelected(false);
            radioLow.setSelected(true);
        } else {
            radioHigh.setSelected(false);
            radioLow.setSelected(false);
        }
    }
    
    private void changeSpinnerLevel(int level) {
        levelSpinner.setValue(level);
    }
    
    public int getLevel() {
        return (Integer)levelSpinner.getValue();
    }
    
    public String getProteinName() {
        return this.proteinName;
    }

}
