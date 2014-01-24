package dk.dtu.sb.GUI.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.AbstractListModel;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Font;

public class LoadSBMLPanel extends JPanel {
    
    public JButton btnLoadFromSbml;
    
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
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,}));
        
        btnLoadFromSbml = new JButton("Load from SBML");
        add(btnLoadFromSbml, "2, 2");
        
        JLabel lblLibrary = new JLabel("Library");
        lblLibrary.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        add(lblLibrary, "2, 4");
        
        JList list = new JList();
        add(list, "2, 6, fill, fill");
        
        JButton btnLoadLibrary = new JButton("Load Library Part");
        btnLoadLibrary.setEnabled(false);
        add(btnLoadLibrary, "2, 8");
        
    }   

}
