package dk.dtu.sb.GUI.view;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import dk.dtu.ls.library.SBGate;

@SuppressWarnings("serial")
public class SBGateDetailsPanel extends JPanel {

    private JLabel lblIdtext, lblSoptext, lblCosttext, lblInputtext,
            lblInttext, lblOuttext;

    /**
     * Create the panel.
     */
    public SBGateDetailsPanel() {
        setLayout(new FormLayout(
                new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
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
                        FormFactory.DEFAULT_ROWSPEC, }));

        JLabel lblName = new JLabel("ID");
        lblName.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        add(lblName, "2, 2");

        lblIdtext = new JLabel("name-text");
        lblIdtext.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        add(lblIdtext, "2, 4");

        JLabel lblSop = new JLabel("SoP");
        lblSop.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        add(lblSop, "2, 6");

        lblSoptext = new JLabel("SoP-text");
        lblSoptext.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        add(lblSoptext, "2, 8");

        JLabel lblCost = new JLabel("Cost");
        lblCost.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        add(lblCost, "2, 10");

        lblCosttext = new JLabel("cost-text");
        lblCosttext.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        add(lblCosttext, "2, 12");

        JLabel lblInputProteins = new JLabel("Input proteins");
        lblInputProteins.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        add(lblInputProteins, "2, 14");

        lblInputtext = new JLabel("input-text");
        lblInputtext.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        add(lblInputtext, "2, 16");

        JLabel lblIntermediateProteins = new JLabel("Intermediate proteins");
        lblIntermediateProteins
                .setFont(new Font("Lucida Grande", Font.BOLD, 12));
        add(lblIntermediateProteins, "2, 18");

        lblInttext = new JLabel("int-text");
        lblInttext.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        add(lblInttext, "2, 20");

        JLabel lblOutputProteins = new JLabel("Output protein");
        lblOutputProteins.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        add(lblOutputProteins, "2, 22");

        lblOuttext = new JLabel("out-text");
        lblOuttext.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        add(lblOuttext, "2, 24");

        lblCosttext.setText("");
        lblInputtext.setText("");
        lblInttext.setText("");
        lblOuttext.setText("");
        lblSoptext.setText("");
        lblIdtext.setText("");
    }

    public void setDetails(SBGate gate) {
        lblCosttext.setText("" + gate.getCost());
        lblInputtext.setText(gate.inputProteins.toString());
        lblInttext.setText(gate.intermediateProteins.toString());
        lblOuttext.setText(gate.outputProtein);
        lblSoptext.setText(gate.SOP);
        lblIdtext.setText("" + gate.id);
    }

}
