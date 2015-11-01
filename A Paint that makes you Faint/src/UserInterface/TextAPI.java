package UserInterface;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Geometry.SketchComponent;
import Geometry.TextComponent;

public class TextAPI extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtEnterYourText;
    private GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private Font[] fonts = e.getAllFonts();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TextAPI dialog = new TextAPI();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TextAPI() {
		setTitle("Select Font");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			txtEnterYourText = new JTextField();
			txtEnterYourText.setText("Enter Your Text Here");
			txtEnterYourText.setBounds(93, 11, 243, 61);
			contentPanel.add(txtEnterYourText);
			txtEnterYourText.setColumns(10);
		}
		JLabel lblEnterText = new JLabel("Enter Text");
		lblEnterText.setBounds(22, 23, 71, 37);
		contentPanel.add(lblEnterText);
		
		JLabel lblFont = new JLabel("Font");
		lblFont.setBounds(22, 102, 46, 14);
		contentPanel.add(lblFont);
		
		 final Choice choice = new Choice();
		choice.setBounds(81, 102, 132, 22);
		contentPanel.add(choice);
	    for (Font f : fonts) {
	      choice.add(f.getFontName());
	    }
		
		final JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(15, 1, 80, 1));
		spinner.setBounds(319, 102, 36, 20);
		contentPanel.add(spinner);
		
		JLabel lblFontSize = new JLabel("Font Size");
		lblFontSize.setBounds(248, 102, 64, 14);
		contentPanel.add(lblFontSize);
		
		final JToggleButton Bold = new JToggleButton("");
		Bold.setIcon(new ImageIcon("Icons\\Bold.png"));
		Bold.setBounds(286, 134, 36, 22);
		contentPanel.add(Bold);
		
		final JToggleButton Italic = new JToggleButton("");
		Italic.setIcon(new ImageIcon("Icons\\Italic.png"));
		Italic.setBounds(329, 134, 36, 22);
		contentPanel.add(Italic);
		
		JPanel Preview = new JPanel();
		Preview.setBorder(new TitledBorder(null, "Preview", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		Preview.setBounds(8, 157, 309, 68);
		contentPanel.add(Preview);
		Preview.setLayout(null);
		
		final JLabel Prev = new JLabel("Enter Your Text Here");
		Prev.setBounds(12, 12, 292, 54);
		Prev.setFont(Prev.getFont().deriveFont(Font.PLAIN));
		Prev.setFont(Prev.getFont().deriveFont(15.0f));
		Preview.add(Prev);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				final JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						SketchComponent T = new TextComponent(Prev.getText(),Prev.getFont());
						Sketch.drawingSHape = T;
						Sketch.isDrawingText = true;
						setVisible(false);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						setVisible(false);
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		txtEnterYourText.addCaretListener(new CaretListener() {
			   @Override
			public void caretUpdate(CaretEvent e){
				   Prev.setText(txtEnterYourText.getText());
			}
		});
		spinner.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				int v = (int)spinner.getValue();
				float sz = v;
				Prev.setFont(Prev.getFont().deriveFont(sz));
			}
		});
		choice.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				Prev.setFont(fonts[choice.getSelectedIndex()]);
				int v = (int)spinner.getValue();
				float sz = v;
				Prev.setFont(Prev.getFont().deriveFont(sz));
			}
			
		});
		Bold.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(Bold.isSelected() && !Italic.isSelected())
					Prev.setFont(Prev.getFont().deriveFont(Font.BOLD));
				else if(!Bold.isSelected() && Italic.isSelected()){
					Prev.setFont(Prev.getFont().deriveFont(Font.ITALIC));
				}
				else if(Bold.isSelected() && Italic.isSelected()){
					Prev.setFont(Prev.getFont().deriveFont(Font.ITALIC + Font.BOLD));
				}
				else{
					Prev.setFont(Prev.getFont().deriveFont(Font.PLAIN));
				}
			}
			
		});
		Italic.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(Bold.isSelected() && !Italic.isSelected())
					Prev.setFont(Prev.getFont().deriveFont(Font.BOLD));
				else if(!Bold.isSelected() && Italic.isSelected()){
					Prev.setFont(Prev.getFont().deriveFont(Font.ITALIC));
				}
				else if(Bold.isSelected() && Italic.isSelected()){
					Prev.setFont(Prev.getFont().deriveFont(Font.ITALIC + Font.BOLD));
				}
				else{
					Prev.setFont(Prev.getFont().deriveFont(Font.PLAIN));
				}
			}
		});
		setVisible(true);
	}
}
