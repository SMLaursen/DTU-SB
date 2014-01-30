package dk.dtu.sb.GUI.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ParametersPanel extends JPanel {
    
    public JSpinner simulations;
    public JSpinner stoptime;
    public JSpinner timeout;
    public JSpinner outSteps;
    public JSpinner threshold;
    public JSpinner cores;
    
    public JButton simButton;
    public JButton stopSimButton;

    /**
     * Create the panel.
     */
    public ParametersPanel() {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.GLUE_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,}));
        
        String simTooltip = "Number of iterations";
        JLabel lblSimulations = new JLabel("Iterations");
        lblSimulations.setToolTipText(simTooltip);
        add(lblSimulations, "2, 2");
        
        simulations = new JSpinner();
        simulations.setModel(new SpinnerNumberModel(10, 1, 1000, 1));
        simulations.setToolTipText(simTooltip);
        add(simulations, "2, 4");
        
        String stopTooltip = "The simulation interval";
        JLabel lblStoptime = new JLabel("Stoptime");
        lblStoptime.setToolTipText(stopTooltip);
        add(lblStoptime, "2, 6");
        
        stoptime = new JSpinner();
        stoptime.setModel(new SpinnerNumberModel(new Integer(10000), new Integer(0), null, new Integer(1000)));
        stoptime.setToolTipText(stopTooltip);
        add(stoptime, "2, 8");
        
        String timeoutTooltip = "Timeout before stopping simulation";
        JLabel lblTimeout = new JLabel("Timeout");
        lblTimeout.setToolTipText(timeoutTooltip);
        add(lblTimeout, "2, 10");
        
        timeout = new JSpinner();
        timeout.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(10)));
        timeout.setToolTipText(timeoutTooltip);
        add(timeout, "2, 12");
        
        String outTooltip = "The amount of samples to output. Reduces memory consumption.";
        JLabel lblOutputSteps = new JLabel("Output steps");
        lblOutputSteps.setToolTipText(outTooltip);
        add(lblOutputSteps, "2, 14");
        
        outSteps = new JSpinner();
        outSteps.setModel(new SpinnerNumberModel(new Integer(1000), new Integer(0), null, new Integer(100)));
        outSteps.setToolTipText(outTooltip);
        add(outSteps, "2, 16");
        
        String thresTooltip = "The minimum time elapsed between successive samples. Reduces memory consumption.";
        JLabel lblThreshold = new JLabel("Threshold");
        lblThreshold.setToolTipText(thresTooltip);
        add(lblThreshold, "2, 18");
        
        threshold = new JSpinner();
        threshold.setModel(new SpinnerNumberModel(new Double(0), new Double(0), null, new Double(0)));
        threshold.setToolTipText(thresTooltip);
        add(threshold, "2, 20");
       
        String coresTooltip = "Usually this should be set to the number of CPU cores";
        JLabel lblCpuCores = new JLabel("Max threads");
        lblCpuCores.setToolTipText(coresTooltip);
        add(lblCpuCores, "2, 22");
        
        cores = new JSpinner();
        cores.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
        cores.setToolTipText(coresTooltip);
        add(cores, "2, 24");
        
        JPanel panel = new JPanel();
        add(panel, "2, 28, fill, fill");
        
        simButton = new JButton("Simulate");
        simButton.setEnabled(false);
        panel.add(simButton);
        
        stopSimButton = new JButton("Stop");
        stopSimButton.setEnabled(false);
        panel.add(stopSimButton);

    }

}
