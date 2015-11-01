package UserInterface;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
 
/* ColorPicker.java requires no other files. */
public class ColorPicker extends JPanel
                              implements ChangeListener {

	private static final long serialVersionUID = -1881331961261599689L;
	
	protected JColorChooser tcc;
    protected JLabel banner;
    public static JPanel choosed;
    public ColorPicker() {
        super(new BorderLayout());
 
        //Set up the banner at the top of the window
        banner = new JLabel("Choose the Color",
                            JLabel.LEFT);
        banner.setForeground(Color.yellow);
        banner.setBackground(Color.blue);
        banner.setOpaque(true);
        banner.setFont(new Font("SansSerif", Font.BOLD, 24));
        banner.setPreferredSize(new Dimension(100, 65));
 
        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.add(banner, BorderLayout.CENTER);
        bannerPanel.setBorder(BorderFactory.createTitledBorder("Banner"));
 
        //Set up color chooser for setting text color
        tcc = new JColorChooser(banner.getForeground());
        tcc.getSelectionModel().addChangeListener(this);
        tcc.setBorder(BorderFactory.createTitledBorder(
                                             "Choose Color"));
 
        add(bannerPanel, BorderLayout.CENTER);
        add(tcc, BorderLayout.PAGE_END);
    }
 
    public  void stateChanged(ChangeEvent e) {
        Color newColor = tcc.getColor();
        banner.setForeground(newColor);
        choosed.setBackground(newColor);
        MainFrame.ChoosenColor = newColor;
    }
    public static void createAndShowGUI(JPanel chooser) {
        JFrame frame = new JFrame("ColorPicker");
        choosed = chooser;
        JComponent newContentPane = new ColorPicker();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        frame.pack();
        frame.setVisible(true);
    }
 
}
