package dk.dtu.sb.GUI.view;

import java.awt.Font;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import dk.dtu.ls.library.models.SBGate;

import javax.swing.ListSelectionModel;

public class LoadSBMLPanel extends JPanel {
    
    public JButton btnLoadFromSbml, btnLoadLibrary;
    
    private JLabel lblIdtext, lblSoptext, lblCosttext, lblInputtext, lblInttext, lblOuttext;
    
    public JList list;
    
    public LoadSBMLPanel() {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("top:default"),
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,}));
        
        btnLoadFromSbml = new JButton("Load from SBML");
        add(btnLoadFromSbml, "2, 2");
        
        JLabel lblLibrary = new JLabel("Library");
        lblLibrary.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        add(lblLibrary, "2, 4");
        
        list = new JList();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(list, "2, 6, fill, fill");
        
        JPanel panel = new JPanel();
        add(panel, "2, 10, fill, fill");
        panel.setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,},
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
                FormFactory.DEFAULT_ROWSPEC,}));
        
        JLabel lblName = new JLabel("ID");
        lblName.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        panel.add(lblName, "2, 2");
        
        lblIdtext = new JLabel("name-text");
        lblIdtext.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        panel.add(lblIdtext, "2, 4");
        
        JLabel lblSop = new JLabel("SoP");
        lblSop.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        panel.add(lblSop, "2, 6");
        
        lblSoptext = new JLabel("SoP-text");
        lblSoptext.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        panel.add(lblSoptext, "2, 8");
        
        JLabel lblCost = new JLabel("Cost");
        lblCost.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        panel.add(lblCost, "2, 10");
        
        lblCosttext = new JLabel("cost-text");
        lblCosttext.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        panel.add(lblCosttext, "2, 12");
        
        JLabel lblInputProteins = new JLabel("Input proteins");
        lblInputProteins.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        panel.add(lblInputProteins, "2, 14");
        
        lblInputtext = new JLabel("input-text");
        lblInputtext.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        panel.add(lblInputtext, "2, 16");
        
        JLabel lblIntermediateProteins = new JLabel("Intermediate proteins");
        lblIntermediateProteins.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        panel.add(lblIntermediateProteins, "2, 18");
        
        lblInttext = new JLabel("int-text");
        lblInttext.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        panel.add(lblInttext, "2, 20");
        
        JLabel lblOutputProteins = new JLabel("Output protein");
        lblOutputProteins.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        panel.add(lblOutputProteins, "2, 22");
        
        lblOuttext = new JLabel("out-text");
        lblOuttext.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        panel.add(lblOuttext, "2, 24");
        
        btnLoadLibrary = new JButton("Load Library Part");
        btnLoadLibrary.setEnabled(false);
        add(btnLoadLibrary, "2, 8");
        
        lblCosttext.setText("");
        lblInputtext.setText("");
        lblInttext.setText("");
        lblOuttext.setText("");
        lblSoptext.setText("");
        lblIdtext.setText("");
    }   
    
    public void populateLibrary(List<String> parts) {
        DefaultListModel model = new DefaultListModel();
        for (String part : parts) {
            model.addElement(part);
        }
        list.setModel(model);
    }
    
    public void setDetails(SBGate gate) {
        lblCosttext.setText(""+gate.cost);
        lblInputtext.setText(gate.inputProteins.toString());
        lblInttext.setText(gate.intermediateProteins.toString());
        lblOuttext.setText(gate.outputProtein);
        lblSoptext.setText(gate.SOP);
        lblIdtext.setText(""+gate.id);
        btnLoadLibrary.setEnabled(true);
    }

}
