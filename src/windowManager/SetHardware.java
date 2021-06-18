package windowManager;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.SystemColor;

public class SetHardware extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SetHardware dialog = new SetHardware();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SetHardware() {
		
		iniFile ini = new iniFile();
		
		setBounds(100, 100, 283, 346);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		{
			JButton btnSetName = new JButton("Monitor Tag & Name");
			btnSetName.setBackground(SystemColor.menu);
			btnSetName.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String input = JOptionPane.showInputDialog(null,"Example '1_Heidelberg'",
                            "Set Monitor & Tag Name",
                            JOptionPane.PLAIN_MESSAGE);
					telnetTunnel con = new telnetTunnel(ini.IP, 0);
					//TODO: welcher befehl nicht gegeben! con.set(telnet_command, param)
					//.setDefaultButton(submitButton);
				}
			});
			btnSetName.setToolTipText("Change Monitor ID and Name");
			getContentPane().add(btnSetName);
		}
		{
			JButton btnSetClock = new JButton("Adjust Monitor Clock");
			btnSetClock.setBackground(SystemColor.menu);
			btnSetClock.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String input = JOptionPane.showInputDialog(null,"dd.mm.yyyy hh:mm:ss",
                            "Adjust Monitor Clock",
                            JOptionPane.PLAIN_MESSAGE);
					//Check for wrong input
					DateFormat formatter=new SimpleDateFormat("dd.MM.yyy HH:mm:ss");
					try {
						Date date=formatter.parse(input);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Please enter a valid time format of dd.MM.yyy HH:mm:ss", "Adjust Monitor Clock", JOptionPane.WARNING_MESSAGE);
						return;
					}
					telnetTunnel con = new telnetTunnel(ini.IP, 0);
					try {
						con.set("clk", input );
					} catch (IOException | InterruptedException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Could not connect to the Monitor. Maybe " + ini.IP + " is the wrong IP?", "Adjust Monitor Clock", JOptionPane.WARNING_MESSAGE);
						e1.printStackTrace();
					}
				}
			});
			getContentPane().add(btnSetClock);
		}
		{
			JButton btnSetIntegration = new JButton("Set Integration Time");
			btnSetIntegration.setBackground(SystemColor.menu);
			btnSetIntegration.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String input = JOptionPane.showInputDialog(null,"60s-43200s",
                            "Adjust Monitor Clock",
                            JOptionPane.PLAIN_MESSAGE);
					//dialog got cancelled
					if(input==null)
						return;
					//check for correct input
					try {
						if(Integer.parseInt(input)>43200 || Integer.parseInt(input)<60) {
							JOptionPane.showMessageDialog(null, "Please enter an interval between 60s and 43200s", "Set Integration Time", JOptionPane.WARNING_MESSAGE);
							return;
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Please enter an interval between 60s and 43200s", "Set Integration Time", JOptionPane.WARNING_MESSAGE);
						return;
					}
					telnetTunnel con = new telnetTunnel(ini.IP, 0);
					try {
						con.set("ivl", input);
					} catch (IOException | InterruptedException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Could not connect to the Monitor. Maybe " + ini.IP + " is the wrong IP?", "Set Integration Time", JOptionPane.WARNING_MESSAGE);
						e1.printStackTrace();
					}
					
				}
			});
			getContentPane().add(btnSetIntegration);
		}
		{
			JButton btnClear = new JButton("Clear Memory Card");
			btnClear.setBackground(SystemColor.menu);
			btnClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//input = 0 -> yes; input = 1 -> no
					int input = JOptionPane.showConfirmDialog(null,
                            "Delete all data on memory card?",
                            "Clear Memory Card",
                            JOptionPane.YES_NO_OPTION);

					if(input == 0) {
						telnetTunnel con = new telnetTunnel(ini.IP, 23);
						try {
							con.get("era");
						} catch (IOException | InterruptedException e1) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, "Could not connect to the Monitor. Maybe " + ini.IP + " is the wrong IP?", "Clear Memory Card", JOptionPane.WARNING_MESSAGE);
							e1.printStackTrace();
						}
					}
				}
			});
			getContentPane().add(btnClear);
		}
		{
			JButton btnSetSlope = new JButton("Change Slope/Offset");
			btnSetSlope.setBackground(SystemColor.menu);
			btnSetSlope.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//open new window for the slopes and offsets
					SetSlopeOffset dialog = new SetSlopeOffset();
					dialog.setVisible(true);
				}
			});
			getContentPane().add(btnSetSlope);
		}
		{
			JButton cancelButton = new JButton("Close");
			cancelButton.setBackground(SystemColor.menu);
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			getContentPane().add(cancelButton);
			cancelButton.setActionCommand("Cancel");
		}
	}

}
