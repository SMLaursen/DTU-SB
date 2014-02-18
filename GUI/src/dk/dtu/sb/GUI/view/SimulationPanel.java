package dk.dtu.sb.GUI.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

import dk.dtu.ls.library.SBGate;
import dk.dtu.sb.data.SimulationResult;

@SuppressWarnings("serial")
public class SimulationPanel extends JPanel {

    public JTabbedPane tabs;

    /**
     * Create the panel.
     */
    public SimulationPanel() {
        setLayout(new BorderLayout(0, 0));

        tabs = new JTabbedPane(JTabbedPane.TOP);
        add(tabs);
    }

    public void addTab(String title, Component panel) {
        tabs.addTab(title, panel);
    }

    public void updateTab(String title, SimulationResult data, String outputProtein, SBGate gate) {
        int index = tabs.indexOfTab(title);

        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        TabButton btnClose = new TabButton();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        pnlTab.add(lblTitle, gbc);

        gbc.gridx++;
        gbc.weightx = 0;
        pnlTab.add(btnClose, gbc);

        tabs.setTabComponentAt(index, pnlTab);
        tabs.setComponentAt(index, new SimulationTabPanel(data, outputProtein, gate));

        btnClose.addActionListener(new MyCloseActionHandler(title));
    }

    public void selectTab(String title) {
        tabs.setSelectedIndex(tabs.indexOfTab(title));
    }

    private class TabButton extends JButton {
        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("Close this tab");
            // Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            // Make it transparent
            setContentAreaFilled(false);
            // No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            // Making nice rollover effect
            // we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
        }

        // we don't want to update UI for this button
        public void updateUI() {
        }

        // paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            // shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight()
                    - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight()
                    - delta - 1);
            g2.dispose();
        }
    }

    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };

    public void removeTab(String title) {
        int index = tabs.indexOfTab(title);
        if (index >= 0) {
            tabs.removeTabAt(index);
        }
    }

    private class MyCloseActionHandler implements ActionListener {

        private String tabName;

        public MyCloseActionHandler(String tabName) {
            this.tabName = tabName;
        }

        public void actionPerformed(ActionEvent evt) {
            removeTab(tabName);
        }

    }
}
