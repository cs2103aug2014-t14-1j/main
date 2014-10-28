package todo_manager;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;
import javax.swing.text.StyleContext;

public class ToDoManagerGUI {
	private static Logic logic;
	private static UserInterface userInterface;
	public static Storage storage;
	private static ToDoManagerGUI toDoManagerGUI;

	private final static String MESSAGE_SPLIT_LINE = "------------------------------------------";
	private final static String MESSAGE_WELCOME = "Welcome to ToDo Manager!";

	private static ToDoManagerGUI guiWindow;
	private JFrame frame;
	private JLabel logoLabel;
	private JPanel inputBoxPanel;
	private JPanel logoPanel;
	private JScrollPane scrollPane;
	private JTextField inputBox;
	private Toolkit toolkit;
	private static JTextPane displayBox;

	public static void main(String[] args) {
		toDoManagerGUI = new ToDoManagerGUI();
		toDoManagerGUI.setup();
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				guiWindow = new ToDoManagerGUI();
				guiWindow.frame.setVisible(true);
			}
		});

	}
	
	public void setup(){
		 logic = Logic.getInstance();
		 userInterface = new UserInterface();
		 storage = new Storage();		
		
	     UserInterface.setup();
	     logic.setupGUI(this);   //creation and filling out of linked lists
  }


	public ToDoManagerGUI() {
		initialize();
		start();
	}

	private void initialize() {
		setupFrame();
		setupIcon();
		setupTextDisplayBox();
		setupTextInputBox();
		setupLayout();
	}

	private void start() {
		displayWelcomeMessage();
		guiFrameListener();
		inputBoxListener();
		displayBoxListener();
	}

	private void setupIcon() {
		/*
		 * logoLabel = new JLabel(""); logoPanel = new JPanel();
		 * logoPanel.add(logoLabel);
		 * 
		 * ImageIcon icon = new
		 * ImageIcon(getClass().getResource("ToDoLogo.png"));
		 * logoLabel.setIcon(icon);
		 */
	}

	// set up the application frame
	private void setupFrame() {
		frame = new JFrame();

		frame.setSize(400, 500);
		frame.getContentPane().setBackground(Color.white);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ToDoManager");
		frame.setResizable(false);
	}

	// text display box
	private void setupTextDisplayBox() {
		displayBox = new JTextPane();
		scrollPane = new JScrollPane(displayBox);

		displayBox.setBackground(Color.WHITE);
		displayBox.setBorder(null);
		displayBox.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		displayBox.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		displayBox.setFont(new Font("Aller", Font.PLAIN, 14));
		// displayBox.setMargin(new Insets(20,20,20,20));

		// set up the scrollable pane
		scrollPane.setBorder(null);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(400, 450));
		scrollPane.setWheelScrollingEnabled(true);
	}

	// add a text input box
	private void setupTextInputBox() {
		inputBox = new HintTextField("cmd:");
		inputBoxPanel = new JPanel();
		inputBoxPanel.add(inputBox);

		inputBoxPanel.setBackground(Color.WHITE);
		inputBoxPanel.setBorder(null);
		inputBoxPanel.setPreferredSize(new Dimension(400, 50));

		inputBox.requestFocus();
		inputBox.setBackground(SystemColor.window);
		inputBox.setBorder(null);
		inputBox.setFont(new Font("Aller", Font.PLAIN, 16));
		inputBox.setHorizontalAlignment(SwingConstants.LEFT);
		inputBox.setPreferredSize(new Dimension(400, 50));
	}

	// setup frame layout
	private void setupLayout() {
		frame.getContentPane().setLayout(
				new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		// frame.getContentPane().add(logoPanel, Component.CENTER_ALIGNMENT);
		frame.getContentPane().add(scrollPane, Component.CENTER_ALIGNMENT);
		frame.getContentPane().add(inputBoxPanel);
	}

	// clear display box
	private void clearDisplayBox() {
		displayBox.setText("");
	}

	// display a string on the display box
	private void displayMessage(String message) {
		StyleContext style = StyleContext.getDefaultStyleContext();

		System.out.println(displayBox);
		int length = displayBox.getDocument().getLength();
		System.out.println("Length:" + length);
		displayBox.setCaretPosition(length);
		displayBox.replaceSelection(message + "\n");
	}

	// read userinput
	private String readUserInput() {
		String userInput = null;
		if (!inputBox.equals(null)) {
			userInput = inputBox.getText();
		}
		return userInput;
	}

	// displays the latest messages
	private void appendToDisplayBox(String message) {
		displayBox.setText(message);
	}

	private void guiFrameListener() {
		frame.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				inputBox.requestFocusInWindow();
			}
		});

		frame.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				inputBox.requestFocusInWindow();
			}
		});
	}

	// Listener for display box
	private void displayBoxListener() {
		displayBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				inputBox.requestFocus();
			}
		});
		displayBox.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				updateScrollBar();
			}
		});
	}

	// Pushes scroll bar to display the latest
	private void updateScrollBar() {
		displayBox.setCaretPosition(displayBox.getDocument().getLength());
		DefaultCaret caret = (DefaultCaret) displayBox.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

	// display welcome meesge
	private void displayWelcomeMessage() {
		displayBox.setText(MESSAGE_WELCOME + "\nToday's Date: "
				+ getTodayDate() + "\n" + MESSAGE_SPLIT_LINE);
	}

	private String getTodayDate() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar date = Calendar.getInstance();
		return dateFormatter.format(date.getTime());
	}

	// Listener for input box
	private void inputBoxListener() {
		inputBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateScrollBar();
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!inputBox.getText().trim().equals("")) {
						clearDisplayBox();

						String userCommand = readUserInput();
						displayBox.setText(userCommand);
						LinkedList<Entry> displayList = new LinkedList<Entry>();
						displayList = toDoManagerGUI.logic.actOnUserInput(userCommand);
						displayResults(displayList);
						inputBox.setText("");
					}
				}

			}
		});
	}

	// display the display list on displaybox
	private void displayResults(LinkedList<Entry> displayList){
		int count = 1;
		String entryString;
		String displayString="";
		for (Entry e : displayList) {
			entryString = count+". "+e.getName();
			if (e.getStartingDate() != null && !e.getStartingDate().equals("")){
				entryString += " start: " + e.getStartingDate();
			}
			
			if (e.getEndingDate() != null && !e.getEndingDate().equals("")){
				entryString += " end: " + e.getEndingDate();
			}
			
			if(e.getDoneness() == true){
				entryString += " Done";
			}
		    count++;
		    entryString += "\n";
		    displayString += entryString;
		}
		if(displayList.size() == 0){
			displayBox.setText("no entry found!");
		}
		else{
			displayBox.setText(displayString);
		}
	}
	
	// setup a prompt message
	class HintTextField extends JTextField implements FocusListener {

		private final String hint;
		private boolean showingHint;

		public HintTextField(final String hint) {
			super(hint);
			this.hint = hint;
			this.showingHint = true;
			super.addFocusListener(this);
		}

		@Override
		public void focusGained(FocusEvent e) {
			if (this.getText().isEmpty()) {
				super.setText("");
				showingHint = false;
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (this.getText().isEmpty()) {
				super.setText(hint);
				showingHint = true;
			}
		}

		@Override
		public String getText() {
			return showingHint ? "" : super.getText();
		}
	}

}
