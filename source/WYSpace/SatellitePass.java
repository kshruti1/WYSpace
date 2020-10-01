package WYSpace;


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalTime;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.ComponentOrientation;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SatellitePass {

	static File filePath = null;
	static final int slotDuration = 30; // 30 min per slot
	static final int maxSlots = 24 * 60 / slotDuration; // 24 hours divided in 30 min slots
	private final JFileChooser openFileChooser;

	private JFrame frame;
	private JTextField txtInputBandwidth;
	private JTextField txtFilePath;

	public SatellitePass() {

		initialize();
		this.openFileChooser = new JFileChooser();
		openFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		openFileChooser.setFileFilter(new FileNameExtensionFilter("Text File", "txt"));
	}

	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int maxValue = 100;
		frame = new JFrame();
		JFrame messageFrame = new JFrame();
		frame.getContentPane().setMaximumSize(new Dimension(1000, 1000));
		frame.getContentPane().setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		frame.setMaximumSize(new Dimension(800, 800));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblBaseStationBandwidth = new JLabel("Base Station Bandwidth:");
		lblBaseStationBandwidth.setFont(new Font("Arial Black", Font.PLAIN, 12));
		lblBaseStationBandwidth.setBounds(10, 76, 169, 33);
		frame.getContentPane().add(lblBaseStationBandwidth);

		JLabel lblWySpaceExcercise = new JLabel("WY Space Excercise");
		lblWySpaceExcercise.setHorizontalAlignment(SwingConstants.CENTER);
		lblWySpaceExcercise.setFont(new Font("Arial Black", Font.BOLD, 20));
		lblWySpaceExcercise.setBounds(39, 11, 356, 27);
		frame.getContentPane().add(lblWySpaceExcercise);

		// accepting only integer input
		NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(maxValue);
		formatter.setAllowsInvalid(false);
		
		// If you want the value to be committed on each keystroke instead of focus lost
		//formatter.setCommitsOnValidEdit(true);

		txtInputBandwidth = new JFormattedTextField(formatter);
		txtInputBandwidth.addKeyListener(new KeyAdapter() {
			
			
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == e.VK_BACK_SPACE) {
					
					txtInputBandwidth.setText("");
				}
			}
		});
		txtInputBandwidth.setToolTipText("Enter bandwidth in integer.");
		txtInputBandwidth.setMaximumSize(new Dimension(50, 50));
		txtInputBandwidth.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtInputBandwidth.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		txtInputBandwidth.setBounds(194, 83, 102, 20);
		txtInputBandwidth.setColumns(12);
		frame.getContentPane().add(txtInputBandwidth);


		JLabel lblSourceFile = new JLabel("Source File:");
		lblSourceFile.setFont(new Font("Arial Black", Font.PLAIN, 12));
		lblSourceFile.setBounds(10, 134, 169, 33);
		frame.getContentPane().add(lblSourceFile);

		txtFilePath = new JTextField();
		txtFilePath.setEditable(false);
		txtFilePath.setHorizontalAlignment(SwingConstants.LEFT);
		txtFilePath.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		txtFilePath.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtFilePath.setBounds(194, 141, 102, 20);
		frame.getContentPane().add(txtFilePath);
		txtFilePath.setColumns(10);

		JButton btnSearch = new JButton("Open File..");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnValue = openFileChooser.showOpenDialog(btnSearch);

				if (returnValue == JFileChooser.APPROVE_OPTION) {

					filePath = openFileChooser.getSelectedFile();

					txtFilePath.setText(filePath.getPath());
					// JOptionPane.showMessageDialog(messageFrame,"Failed to load selected text
					// file!!","Alert",JOptionPane.WARNING_MESSAGE);
					// \\

				} else {

					JOptionPane.showMessageDialog(messageFrame, "Please select a source text file!!", "Alert",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		btnSearch.setBounds(306, 140, 100, 23);
		frame.getContentPane().add(btnSearch);

		JButton btnCalculate = new JButton("Calculate");
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringBuilder stringResult = new StringBuilder();
				if (txtInputBandwidth.getText().trim().isEmpty() || txtFilePath.getText().trim().isEmpty()) {

					JOptionPane.showMessageDialog(messageFrame, "Please enter all the inputs!!", "Alert",
							JOptionPane.WARNING_MESSAGE);
				} else {
					
					 try {
						 int baseStationBandwidth = Integer.parseInt(txtInputBandwidth.getText().trim());
						Schedule[] schedule = readData();
						optimalTime[] bestTime = createAndMatchSlots(schedule);
						char flag = 0; 
						optimalTime[] bestPass = findBestPass(bestTime,baseStationBandwidth, flag); 
						stringResult.append("Best passes with maximum bandwidth are:");
						
						stringResult = appendResult(bestPass,stringResult );
						stringResult.append("\nGiven base station bandwidth is "+ baseStationBandwidth);
						if (baseStationBandwidth < bestPass[0].maxBandwidth) {
							
							stringResult.append("\nIt seems difficult for base station to support max bandwidth.");
							stringResult.append("\nNew best pass for your base station:");
							  // calculating best pass for base station
							 
							 flag = 1;
							  optimalTime[] baseStationBestPass = findBestPass(bestTime,baseStationBandwidth, flag);
							  
							  
							  if (baseStationBestPass[0].startPass == null) {
								  stringResult.append("\nSorry. There is no matching pass for this bandwidth."); }
							  else 
								  appendResult(baseStationBestPass,stringResult);
							  
							  } else { stringResult.append("\nIt is appropriate for max bandwidth pass.");
					}
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 
					 ResultFrame displayResult = new ResultFrame(stringResult);
					 displayResult.setVisible(true);
					frame.dispose();

				}

			}
		});
		btnCalculate.setBounds(250, 201, 89, 23);
		frame.getContentPane().add(btnCalculate);

		JButton btnExit = new JButton("Close");
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(112, 201, 89, 23);
		frame.getContentPane().add(btnExit);
	}

	static StringBuilder appendResult(optimalTime[] bestPass, StringBuilder stringResult) {

		if (bestPass.length != 0) {

			for (int i = 0; i < bestPass.length; i++) {
				if (bestPass[i].maxBandwidth != 0) {
					stringResult.append("\nBest pass = " + bestPass[i].startPass + " - "
							+ bestPass[i].startPass.plusMinutes(slotDuration));
				
					stringResult.append("  Maximum Bandwidth = " + bestPass[i].maxBandwidth);
				} else
					break;
			}
		}
		return stringResult;

	}

	static Schedule[] readData() throws IOException {
		int i = 0;
		String line;
		String[] parts = null;
		Schedule[] schedule = new Schedule[26];
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			// check for empty file or end of the file
			while ((line = reader.readLine()) != null) {
				parts = line.split(",");
				// assuming all inputs from the given file are in correct format and sequence.
				schedule[i] = new Schedule(parts[0], Integer.parseInt(parts[1]), LocalTime.parse(parts[2]),
						LocalTime.parse(parts[3]));
				i++;

			}

			reader.close();
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			System.out.println("Input file " + filePath + " not found.");
			System.exit(0);
		}
		return schedule;
	}

	static optimalTime[] findBestPass(optimalTime[] bestTime, int baseStationBandwidth, char flag) {
		LocalTime startBestTime = null;
		int max = bestTime[0].maxBandwidth;
		optimalTime bestPass[] = new optimalTime[2];
		// calculate maxBandwidth
		if (flag == 0) {
			for (int i = 0; i < bestTime.length - 1; i++) {
				if (bestTime[i + 1].maxBandwidth > max) {
					max = bestTime[i + 1].maxBandwidth;
					startBestTime = bestTime[i + 1].startPass;
				}
			}
			bestPass[0] = new optimalTime(max, startBestTime);
		} else if (flag == 1) {

			max = 0;

			for (int i = 0; i < bestTime.length - 1; i++) {
				if (bestTime[i + 1].maxBandwidth > max) {
					if (!(bestTime[i + 1].maxBandwidth > baseStationBandwidth)) {
						max = bestTime[i + 1].maxBandwidth;
						startBestTime = bestTime[i + 1].startPass;
					}
				}
			}
			bestPass[0] = new optimalTime(max, startBestTime);

		}

		// ckeck for one more slot with same bandwidth if exists
		for (int i = 0; i < bestTime.length - 1; i++) {
			if (bestTime[i].maxBandwidth == max && !(bestPass[0].startPass.equals(bestTime[i].startPass))) {

				bestPass[1] = new optimalTime(bestTime[i].maxBandwidth, bestTime[i].startPass);
				break;
			} else
				bestPass[1] = new optimalTime(0, null);
			;
		}
		return bestPass;

	}

	static optimalTime[] createAndMatchSlots(Schedule[] schedule) {
		// initialize start time and bandwidth for every 30 min slot
		optimalTime[] bestTime = new optimalTime[maxSlots];
		bestTime[0] = new optimalTime(0, LocalTime.MIDNIGHT);
		for (int i = 0; i < maxSlots - 1; i++) {
			bestTime[i + 1] = new optimalTime(0, bestTime[i].startPass.plusMinutes(slotDuration));
		}

		// divide all given passes into 30 min slots
		int count;
		LocalTime[][] slots = new LocalTime[schedule.length][];
		for (int i = 0; i < schedule.length; i++) {
			slots[i] = new LocalTime[maxSlots];
			count = 0;
			LocalTime j = schedule[i].startPass;
			while (j.isBefore(schedule[i].endPass)) {
				slots[i][count] = j;
				j = j.plusMinutes(slotDuration);
				count++;
			}

		}

		for (int j = 0; j < schedule.length; j++) {
			int k = 0;
			while (slots[j][k] != null) {
				for (int i = 0; i < maxSlots; i++) {
					if (slots[j][k].equals(bestTime[i].startPass)) {
						bestTime[i].maxBandwidth += schedule[j].satBandwidth;
						break;
					}
				}
				k++;
			}
		}

//		// print max bandwidth for each slot
//		for (int i = 0; i < bestTime.length - 1; i++) {
//			System.out.println("\n");
//			System.out.println(bestTime[i].startPass + "\t" + bestTime[i].maxBandwidth);
//		}

		return bestTime;
	}

	public static void main(String[] args) throws Exception {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SatellitePass window = new SatellitePass();
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public static boolean isValidNumber(String input) {
		try {
			int in = Integer.parseInt(input);
			if (Integer.signum(in) != 1)
				return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

class optimalTime {
	int maxBandwidth;
	LocalTime startPass;

	public optimalTime(int bandwidth, LocalTime startTime) {
		this.maxBandwidth = bandwidth;
		this.startPass = startTime;
	}

}

class Schedule {
	String satName;
	int satBandwidth;
	LocalTime startPass;
	LocalTime endPass;

	public Schedule(String name, int bandwidth, LocalTime startTime, LocalTime endTime) {
		this.satName = name;
		this.satBandwidth = bandwidth;
		this.startPass = startTime;
		this.endPass = endTime;
	}
}
