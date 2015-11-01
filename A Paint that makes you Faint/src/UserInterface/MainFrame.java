package UserInterface;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import Geometry.SketchComponent;
import SaveAndLoad.SaveAndLoad;


public class MainFrame extends JFrame {

	private static final long serialVersionUID = 9599887731257885L;
	public static enum ShapePick {RECTANGLE  , OVAL , TRIANGLE , LINE , NOTH , GRPSLCT , CSTMCLS , CIRCLE, SQUARE , PCKCOLOR};
	public static ShapePick choose = ShapePick.NOTH ;
	public static Font TextFont;
	public static JFrame ref;
	public static String DrawString;
	public static Color ChoosenColor;
	public static String url=new String();
	public static String name =new String();
	public JToggleButton LastToggled;
	public static JPanel contentPane,SelectedColorPanel;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("A Paint That Makes You Faint");
		setBounds(100, 100, 972, 600);
		contentPane = (new JPanel());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(744, 46, 55, 228);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JToolBar ShapesToDraw = new JToolBar();
		ShapesToDraw.setBounds(6, 11, 43, 206);
		panel.add(ShapesToDraw);
		ShapesToDraw.setFloatable(false);
		ShapesToDraw.setBorder(UIManager.getBorder("OptionPane.border"));
		ShapesToDraw.setOrientation(SwingConstants.VERTICAL);
		
		final JToggleButton tglbtnLine = new JToggleButton("");
		tglbtnLine.setIcon(new ImageIcon("Icons\\Line.png"));
		ShapesToDraw.add(tglbtnLine);
		tglbtnLine.addItemListener(new ItemListener() {
			   public void itemStateChanged(ItemEvent ev) {
			      if(ev.getStateChange()==ItemEvent.SELECTED){
			    	  if(LastToggled != null && LastToggled!= tglbtnLine)
			    		  LastToggled.doClick();
			    	  LastToggled = tglbtnLine;
			    	  choose = ShapePick.LINE;
			      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
			    	  choose = ShapePick.NOTH;
			    	  LastToggled = null;
			      }
			      Sketch.SelectionNotify();
			   }
		});
		
		final JToggleButton tglbtnSquare = new JToggleButton("");
		tglbtnSquare.setIcon(new ImageIcon("Icons\\Square.png"));
		ShapesToDraw.add(tglbtnSquare);
		tglbtnSquare.addItemListener(new ItemListener() {
			   public void itemStateChanged(ItemEvent ev) {
			      if(ev.getStateChange()==ItemEvent.SELECTED){
			    	  if(LastToggled != null && LastToggled!= tglbtnSquare)
			    		  LastToggled.doClick();
			    	  LastToggled = tglbtnSquare;
			    	  choose = ShapePick.SQUARE;
			      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
			    	  choose = ShapePick.NOTH;
			    	  LastToggled = null;
			      }
			      Sketch.SelectionNotify();
			   }
		});
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(5, 5, 424, 34);
		toolBar.setBorder(new LineBorder(new Color(0, 0, 0)));
		toolBar.setFloatable(false);
		contentPane.add(toolBar);
		
		JButton btnNew = new JButton("New");
		btnNew.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Sketch.Shapes.clear();
				Sketch.ref.removeAll();
			}
		});
		btnNew.setIcon(new ImageIcon("Icons\\New.png"));
		toolBar.add(btnNew);
		JButton btnSave = new JButton("Save");
		btnSave.setIcon(new ImageIcon("Icons\\Save.png"));
		toolBar.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent ae) {
		          JFileChooser chooser = new JFileChooser();
		          int option = chooser.showSaveDialog(MainFrame.this);
		          if (option == JFileChooser.APPROVE_OPTION) 
		          {
		        	  if(choose == ShapePick.GRPSLCT){
		        		  LastToggled.doClick();
		        	  }
		            System.out.println(chooser.getSelectedFile().getAbsolutePath());
		            SaveAndLoad s =new  SaveAndLoad();
		            s.save(Sketch.Shapes,chooser.getSelectedFile().getAbsolutePath());
		          }
		        }
		      });
		
		JButton btnLoad = new JButton("Load");
		btnLoad.setIcon(new ImageIcon("Icons\\Load.png"));
		toolBar.add(btnLoad);
		toolBar.add(btnLoad);
		btnLoad.addActionListener(new ActionListener() {
		      @SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent ae) {
		        JFileChooser chooser = new JFileChooser();
		        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml","xml");
			    chooser.setFileFilter(filter);
		        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        int option = chooser.showOpenDialog(MainFrame.this);
		        if (option == JFileChooser.APPROVE_OPTION)
		        {
		        	SaveAndLoad s =new  SaveAndLoad();
		        	Sketch.ref.removeAll();
		        	Sketch.Shapes = (Vector<SketchComponent>)s.load(chooser.getSelectedFile().getAbsolutePath());
		        	for(int i=0;i<Sketch.Shapes.size();i++){
		        		SketchComponent C = Sketch.Shapes.elementAt(i);
		        		C.BorderIn = false;
		        	}
		        	Sketch.SelectionNotify();
		        }
		      }
		    });
		
		
		JButton btnUndo = new JButton("Undo");
		btnUndo.setIcon(new ImageIcon("Icons\\UndoResized.png"));
		toolBar.add(btnUndo);
		btnUndo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Sketch.Undo();
				Sketch.SelectionNotify();
			}
		});
		
		JButton btnRedo = new JButton("Redo");
		btnRedo.setIcon(new ImageIcon("Icons\\RedoResized.png"));
		toolBar.add(btnRedo);
		btnRedo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Sketch.Redo();
				Sketch.SelectionNotify();
			}
		});
		final JToggleButton tglbtnTriangle = new JToggleButton("");
		tglbtnTriangle.setIcon(new ImageIcon("Icons\\Triang;e.png"));
		ShapesToDraw.add(tglbtnTriangle);
		tglbtnTriangle.addItemListener(new ItemListener() {
			   public void itemStateChanged(ItemEvent ev) {
			      if(ev.getStateChange()==ItemEvent.SELECTED){
			    	  if(LastToggled != null && LastToggled!= tglbtnTriangle)
			    		  LastToggled.doClick();
			    	  LastToggled = tglbtnTriangle;
			    	  choose = ShapePick.TRIANGLE;
			      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
			    	  choose = ShapePick.NOTH;
			    	  LastToggled = null;
			      }
			      Sketch.SelectionNotify();
			   }
		});
		final JToggleButton tglbtnGroupSelect = new JToggleButton("Group Select");
		tglbtnGroupSelect.setIcon(new ImageIcon("Icons\\Group.png"));
		tglbtnGroupSelect.setBounds(744, 366, 202, 34);
		contentPane.add(tglbtnGroupSelect);
		tglbtnGroupSelect.addItemListener(new ItemListener() {
			   public void itemStateChanged(ItemEvent ev) {
			      if(ev.getStateChange()==ItemEvent.SELECTED){
			    	  if(LastToggled != null && LastToggled!= tglbtnGroupSelect)
			    		  LastToggled.doClick();
			    	  LastToggled = tglbtnGroupSelect;
			    	  choose = ShapePick.GRPSLCT;
			      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
			    	  choose = ShapePick.NOTH;
			    	  LastToggled = null;
			      }
			      Sketch.SelectionNotify();
			   }
		});
		
		final JToggleButton tglbtnCircle = new JToggleButton("");
		tglbtnCircle.setIcon(new ImageIcon("Icons\\Circle.png"));
		ShapesToDraw.add(tglbtnCircle);
		tglbtnCircle.addItemListener(new ItemListener() {
			   public void itemStateChanged(ItemEvent ev) {
			      if(ev.getStateChange()==ItemEvent.SELECTED){
			    	  if(LastToggled != null && LastToggled!= tglbtnCircle)
			    		  LastToggled.doClick();
			    	  LastToggled = tglbtnCircle;
			    	  choose = ShapePick.CIRCLE;
			      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
			    	  choose = ShapePick.NOTH;
			    	  LastToggled = null;
			      }
			      Sketch.SelectionNotify();
			   }
		});
		
		final JToggleButton tglbtnElipse = new JToggleButton("");
		tglbtnElipse.setIcon(new ImageIcon("Icons\\Elipse.png"));
		ShapesToDraw.add(tglbtnElipse);
		tglbtnElipse.addItemListener(new ItemListener() {
			   public void itemStateChanged(ItemEvent ev) {
			      if(ev.getStateChange()==ItemEvent.SELECTED){
			    	  if(LastToggled != null && LastToggled!= tglbtnElipse)
			    		  LastToggled.doClick();
			    	  LastToggled = tglbtnElipse;
			    	  choose = ShapePick.OVAL;
			      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
			    	  choose = ShapePick.NOTH;
			    	  LastToggled = null;
			      }
			      Sketch.SelectionNotify();
			   }
		});
		
		final JToggleButton tglbtnRectangle = new JToggleButton("");
		tglbtnRectangle.setIcon(new ImageIcon("Icons\\Rec.png"));
		ShapesToDraw.add(tglbtnRectangle);
		tglbtnRectangle.addItemListener(new ItemListener() {
			   public void itemStateChanged(ItemEvent ev) {
			      if(ev.getStateChange()==ItemEvent.SELECTED){
			    	  if(LastToggled != null && LastToggled!= tglbtnRectangle)
			    		  LastToggled.doClick();
			    	  LastToggled = tglbtnRectangle;
			    	  choose = ShapePick.RECTANGLE;
			      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
			    	  choose = ShapePick.NOTH;
			    	  LastToggled = null;
			      }
			      Sketch.SelectionNotify();
			   }
		});
		JButton TextButton = new JButton("");
		TextButton.setIcon(new ImageIcon("Icons\\TextBox2.png"));
		ShapesToDraw.add(TextButton);
		TextButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				new TextAPI();
			}
		});
		
		final JPanel Sketch = new Sketch();
		Sketch.setBounds(5, 47, 730, 500);
		Sketch.setName("Color Panel");
		Sketch.setBorder(new LineBorder(new Color(0, 0, 0)));
		Sketch.setBackground(Color.WHITE);
		contentPane.add(Sketch);
		Sketch.setLayout(null);
		
		JPanel ColorPanel = new JPanel();
		ColorPanel.setBounds(744, 411, 202, 136);
		ColorPanel.setName("Color Panel");
		ColorPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Color Panel", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		contentPane.add(ColorPanel);
		ColorPanel.setLayout(null);
		
		final JPanel SelectedColor = new JPanel();
		SelectedColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ColorPicker.createAndShowGUI(SelectedColor);
			}
		});
		SelectedColorPanel = SelectedColor;
		JButton btnFill = new JButton("Fill");
		btnFill.setBounds(44, 26, 116, 44);
		ColorPanel.add(btnFill);
		btnFill.setIcon(new ImageIcon("Icons\\Fill.png"));
		btnFill.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				@SuppressWarnings({ "unchecked" })
				Vector<SketchComponent> V = (Vector<SketchComponent>) UserInterface.Sketch.Copy(UserInterface.Sketch.Shapes);
				boolean Changed = false;
				for(int i = 0;i< V.size();i++){
					SketchComponent C = V.elementAt(i);
					if(C.Selected){
						Changed = true;
						if(!C.Filled){
							C.Filled = true;
							C.setColor(SelectedColorPanel.getBackground());
						}
						else
							C.Filled = false;
					}
				}
				if(Changed){
					UserInterface.Sketch.ref.removeAll();
					for(int i= UserInterface.Sketch.States.size()-1; i > UserInterface.Sketch.CurState ;i--){
						UserInterface.Sketch.States.remove(i);
					}
					UserInterface.Sketch.States.add(V);
					UserInterface.Sketch.CurState++;
					UserInterface.Sketch.removeBorders(UserInterface.Sketch.States.elementAt(UserInterface.Sketch.CurState));
					UserInterface.Sketch.Shapes = UserInterface.Sketch.States.elementAt(UserInterface.Sketch.CurState);
					UserInterface.Sketch.SelectionNotify();
				}
			}
		});
		
		SelectedColor.setBounds(44, 81, 40, 33);
		ColorPanel.add(SelectedColor);
		SelectedColor.setBackground(Color.RED);
		SelectedColor.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		final JToggleButton PickColor = new JToggleButton("");
		PickColor.setBounds(108, 81, 52, 34);
		PickColor.addItemListener(new ItemListener() {
			   @SuppressWarnings("deprecation")
			public void itemStateChanged(ItemEvent ev) {
			      if(ev.getStateChange()==ItemEvent.SELECTED){
			    	  if(LastToggled != null && LastToggled!= PickColor )
			    		  LastToggled.doClick();
			    	  LastToggled = PickColor;
			    	  Toolkit toolkit = Toolkit.getDefaultToolkit();
			    	  Image image = toolkit.getImage("Icons\\Cursor.png");
			    	  Cursor c = toolkit.createCustomCursor(image , new Point(7,20), "img");
			    	  setCursor (c);
			    	  choose = MainFrame.ShapePick.PCKCOLOR;
			      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
			    	  setCursor(Cursor.DEFAULT_CURSOR);
			    	  choose = ShapePick.NOTH;
			    	  LastToggled = null;
			      }
			   }
		});
		PickColor.setIcon(new ImageIcon("Icons\\PickColor.png"));
		ColorPanel.add(PickColor);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Pre-Defined Drawing", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(809, 40, 133, 234);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(47, 26, 74, 20);
		panel_1.add(textField);
		textField.setToolTipText("");
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(47, 63, 74, 20);
		panel_1.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(47, 105, 74, 20);
		panel_1.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(47, 149, 74, 20);
		panel_1.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("   X :");
		lblNewLabel.setBounds(10, 29, 111, 14);
		panel_1.add(lblNewLabel);
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JLabel lblY = new JLabel("   Y :");
		lblY.setBounds(10, 66, 111, 14);
		panel_1.add(lblY);
		lblY.setForeground(Color.BLUE);
		lblY.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JLabel lblWidth = new JLabel("   Width :");
		lblWidth.setBounds(-1, 108, 122, 14);
		panel_1.add(lblWidth);
		lblWidth.setForeground(Color.BLUE);
		lblWidth.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JLabel lblHeight = new JLabel("   Height :");
		lblHeight.setBounds(-1, 152, 122, 14);
		panel_1.add(lblHeight);
		lblHeight.setForeground(Color.BLUE);
		lblHeight.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JButton btnDraw = new JButton("Draw");
		btnDraw.setBounds(22, 194, 89, 23);
		btnDraw.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String x = textField.getText();
				String y = textField_1.getText();
				String Width = textField_2.getText();
				String Height = textField_3.getText();
				int X = 0,Y = 0,W = 0,H = 0;
				try{
					X = Integer.parseInt(x);
					Y = Integer.parseInt(y);
					W = Integer.parseInt(Width);
					H = Integer.parseInt(Height);
				}catch(Exception IOException){
					JOptionPane.showMessageDialog(new JFrame(), "Invalid Input !", "Dialog",
					        JOptionPane.ERROR_MESSAGE);
					return;
				}
				UserInterface.Sketch.preDefinedDraw(X,Y,W,H);
			}
		});
		panel_1.add(btnDraw);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Custom Class", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(746, 285, 200, 70);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JButton btnLoadClass = new JButton("Load Class");
		btnLoadClass.setIcon(new ImageIcon("Icons\\LoadClass.png"));
		btnLoadClass.setBounds(72, 23, 122, 34);
		panel_2.add(btnLoadClass);
		
		final JToggleButton tglbtnCustomclass = new JToggleButton("");
		tglbtnCustomclass.setIcon(new ImageIcon("Icons\\PlaceCustom.png"));
		tglbtnCustomclass.setBounds(12, 23, 48, 34);
		tglbtnCustomclass.setEnabled(false);
		panel_2.add(tglbtnCustomclass);
		tglbtnCustomclass.addItemListener(new ItemListener() {
			   public void itemStateChanged(ItemEvent ev) {
			      if(ev.getStateChange()==ItemEvent.SELECTED){
			    	  if(LastToggled != null && LastToggled!= tglbtnCustomclass )
			    		  LastToggled.doClick();
			    	  LastToggled = tglbtnCustomclass;
			    	  choose = ShapePick.CSTMCLS;
			      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
			    	  choose = ShapePick.NOTH;
			    	  LastToggled = null;
			      }
			      UserInterface.Sketch.SelectionNotify();
			   }
		});
		
		btnLoadClass.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub			
				JFileChooser chooser = new JFileChooser();
				String dir = System.getProperty("user.dir");
		         System.out.println("Dir" + dir);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("class","class");
			    chooser.setFileFilter(filter);
		        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        int option = chooser.showOpenDialog(MainFrame.this);
		        if (option == JFileChooser.APPROVE_OPTION)
		        {
		        	url=chooser.getSelectedFile().getAbsolutePath();
		        	name=chooser.getSelectedFile().getName().replace(".class","");
		        	tglbtnCustomclass.setEnabled(true);
		        }
			}
			
		});
	}
}
