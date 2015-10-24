package Controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import Controller.Memento.CareTaker;
import Model.Characters.AI;
import Model.Characters.Character;
import Model.Characters.Olaf;
import Model.Characters.Scrat;

public class PickGameMode extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final ButtonGroup GMode = new ButtonGroup();
	private final ButtonGroup Player1Type = new ButtonGroup();
	private final ButtonGroup Player2Type = new ButtonGroup();
	public static GameState returnedState;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PickGameMode frame = new PickGameMode();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PickGameMode() {
		
		setTitle("New Game");
		
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Game Mode", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(102, 12, 230, 61);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JRadioButton rdbtnSinglePlayer = new JRadioButton("Single Player");
		GMode.add(rdbtnSinglePlayer);
		rdbtnSinglePlayer.setBounds(6, 16, 110, 38);
		panel.add(rdbtnSinglePlayer);
		rdbtnSinglePlayer.setActionCommand("Single Player");
		rdbtnSinglePlayer.setSelected(true);
		
		JRadioButton rdbtnVersusMode = new JRadioButton("Versus Mode");
		GMode.add(rdbtnVersusMode);
		rdbtnVersusMode.setBounds(120, 16, 102, 38);
		panel.add(rdbtnVersusMode);
		rdbtnVersusMode.setActionCommand("Versus Mode");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Player One", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(12, 84, 192, 121);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JRadioButton rdbtnHuman = new JRadioButton("Human");
		Player1Type.add(rdbtnHuman);
		rdbtnHuman.setBounds(23, 29, 65, 24);
		panel_1.add(rdbtnHuman);
		rdbtnHuman.setActionCommand("Human");
		rdbtnHuman.setSelected(true);
		
		JRadioButton rdbtnComputer = new JRadioButton("Computer");
		Player1Type.add(rdbtnComputer);
		rdbtnComputer.setBounds(96, 29, 88, 24);
		panel_1.add(rdbtnComputer);
		rdbtnComputer.setActionCommand("Computer");
		
		JLabel lblNewLabel = new JLabel("Character");
		lblNewLabel.setBounds(23, 76, 69, 16);
		panel_1.add(lblNewLabel);
		
		final JComboBox<Object> comboBox = new JComboBox<Object>();
		comboBox.setModel(new DefaultComboBoxModel<Object>(new String[] {"Scrat", "Olaf"}));
		comboBox.setBounds(92, 72, 88, 25);
		panel_1.add(comboBox);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(null, "Player Two", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(230, 84, 192, 121);
		contentPane.add(panel_2);
		
		JRadioButton radioButton = new JRadioButton("Human");
		Player2Type.add(radioButton);
		radioButton.setBounds(23, 29, 65, 24);
		panel_2.add(radioButton);
		radioButton.setActionCommand("Human");
		radioButton.setSelected(true);
		
		JRadioButton radioButton_1 = new JRadioButton("Computer");
		Player2Type.add(radioButton_1);
		radioButton_1.setBounds(96, 29, 88, 24);
		panel_2.add(radioButton_1);
		radioButton_1.setActionCommand("Computer");
		
		JLabel label = new JLabel("Character");
		label.setBounds(23, 76, 69, 16);
		panel_2.add(label);
		
		final JComboBox<Object> comboBox_1 = new JComboBox<Object>();
		comboBox_1.setModel(new DefaultComboBoxModel<Object>(new String[] {"Scrat", "Olaf"}));
		comboBox_1.setBounds(92, 72, 88, 25);
		panel_2.add(comboBox_1);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Character P1,P2;
				ButtonModel P1S =   Player1Type.getSelection();
				ButtonModel P2S =   Player2Type.getSelection();
				if(P1S.getActionCommand().equals("Human")){
					int sl = comboBox.getSelectedIndex();
					if(sl == 0)
						P1 = new Scrat(850,550);
					else
						P1 = new Olaf(850,550);
				}
				else{
					P1 = new AI(comboBox.getSelectedIndex(),850,550);
				}
				if(P2S.getActionCommand().equals("Human")){
					int sl = comboBox_1.getSelectedIndex();
					if(sl == 0)
						P2 = new Scrat(50,550);
					else
						P2 = new Olaf(50,550);
				}
				else{
					P2 = new AI(comboBox_1.getSelectedIndex(),50,550);
				}
				String Mode = GMode.getSelection().getActionCommand();
				if(Mode.equals("Single Player")){
					returnedState = new GameState(P1,null);
				}
				else{
					returnedState = new GameState(P1,P2);
				}
				Controller C = Controller.getInstance();
				CareTaker CT = CareTaker.getInstance();
				CT.reset();
				C.Pane.setInGame(returnedState);
				dispose();
				C.Pressed.clear();
			}
		});
		btnOk.setBounds(324, 224, 98, 26);
		contentPane.add(btnOk);
		this.setVisible(true);

	}
}
