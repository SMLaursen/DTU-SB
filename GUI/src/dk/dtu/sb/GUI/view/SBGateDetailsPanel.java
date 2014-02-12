package dk.dtu.sb.GUI.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import dk.dtu.ls.library.SBGate;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class SBGateDetailsPanel extends JPanel {

    private JLabel lblSoptext, lblCosttext, lblInputtext,
            lblInttext, lblOuttext;
    private JLabel lblSop;
    private JLabel lblName;
    private JLabel lblComposedOfPart;
    private JPanel panelComposedOf;
    private JTextArea textAreaIdDescText;

    /**
     * Create the panel.
     */
    public SBGateDetailsPanel() {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("min(50dlu;default):grow"),},
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
                RowSpec.decode("default:grow"),}));

        lblName = new JLabel("ID and description");
        lblName.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        add(lblName, "2, 2");
        
        textAreaIdDescText = new JTextArea();
        textAreaIdDescText.setMinimumSize(new Dimension(0, 10));
        textAreaIdDescText.setBackground(UIManager.getColor("Label.background"));
        textAreaIdDescText.setEditable(false);
        textAreaIdDescText.setLineWrap(true);
        add(textAreaIdDescText, "2, 4, fill, top");

        lblSop = new JLabel("SoP");
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

        JLabel lblInputProteins = new JLabel("Input species");
        lblInputProteins.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        add(lblInputProteins, "2, 14");

        lblInputtext = new JLabel("input-text");
        lblInputtext.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        add(lblInputtext, "2, 16");

        JLabel lblIntermediateProteins = new JLabel("Intermediate species");
        lblIntermediateProteins
                .setFont(new Font("Lucida Grande", Font.BOLD, 12));
        add(lblIntermediateProteins, "2, 18");

        lblInttext = new JLabel("int-text");
        lblInttext.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        add(lblInttext, "2, 20");

        JLabel lblOutputProteins = new JLabel("Output species");
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
        
        lblComposedOfPart = new JLabel("Composed of Part IDs");
        lblComposedOfPart.setVisible(false);
        lblComposedOfPart.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        add(lblComposedOfPart, "2, 26");
        
        panelComposedOf = new JPanel();
        panelComposedOf.setVisible(false);
        add(panelComposedOf, "2, 28, left, top");
        
        ToolTipManager.sharedInstance().setInitialDelay(0);
    }

    public void setDetails(SBGate gate) {
        lblCosttext.setText("" + gate.getCost());
        lblInputtext.setText(gate.inputProteins.toString());
        lblInttext.setText(gate.intermediateProteins.toString());
        lblOuttext.setText(gate.outputProtein);
        if (gate.isLibraryPart) {
            lblSoptext.setText(gate.SOP);
            textAreaIdDescText.setText(gate.id + ": " + gate.description);
        } else {
            lblName.setVisible(false);
            textAreaIdDescText.setVisible(false);
            lblSop.setVisible(false);
            lblSoptext.setVisible(false);
            if (gate.composedOf != null) {
                panelComposedOf.removeAll();
                for (SBGate gatePart : gate.composedOf) {
                    JLabel part = new JLabel("Part " + gatePart.id + ";");
                    part.setForeground(Color.BLUE);
                    part.setToolTipText("SoP: " + gatePart.SOP + ", Cost: " + gatePart.getCost());
                    panelComposedOf.add(part);
                }
                lblComposedOfPart.setVisible(true);
                panelComposedOf.setVisible(true);
            }
        }
    }

}
