package dk.dtu.sb.GUI.view;

import java.awt.Font;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class LoadSBMLPanel extends JPanel {
    
    public JButton btnLoadFromSbml, btnLoadLibrary;
        
    public JList list;
    public SBGateDetailsPanel sbGateDetailsPanel;
    
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
                RowSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_ROWSPEC,}));
        
        btnLoadFromSbml = new JButton("Load from SBML");
        add(btnLoadFromSbml, "2, 2");
        
        JLabel lblLibrary = new JLabel("Library");
        lblLibrary.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        add(lblLibrary, "2, 4");
        
        list = new JList();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(list, "2, 6, fill, fill");
        
        btnLoadLibrary = new JButton("Load Library Part");
        btnLoadLibrary.setEnabled(false);
        add(btnLoadLibrary, "2, 8");
        
        sbGateDetailsPanel = new SBGateDetailsPanel();
        add(sbGateDetailsPanel, "2, 10, fill, fill");
    }   
    
    public void populateLibrary(List<String> parts) {
        DefaultListModel model = new DefaultListModel();
        for (String part : parts) {
            model.addElement(part);
        }
        list.setModel(model);
    }

}
