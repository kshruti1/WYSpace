package WYSpace;



import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ResultFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ResultFrame frame = new ResultFrame(null);
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
	public ResultFrame(StringBuilder stringResult) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSatellitePassesWith = new JLabel("Satellite passes with maximum bandwidth");
		lblSatellitePassesWith.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblSatellitePassesWith.setHorizontalAlignment(SwingConstants.CENTER);
		lblSatellitePassesWith.setBounds(36, 21, 332, 31);
		contentPane.add(lblSatellitePassesWith);
		
		JTextArea textResult = new JTextArea();
		textResult.setEditable(false);
		textResult.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textResult.setBounds(25, 63, 399, 162);
		textResult.setText(stringResult.toString());
		contentPane.add(textResult);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnClose.setBounds(160, 238, 89, 23);
		contentPane.add(btnClose);
		
		
			}
}
