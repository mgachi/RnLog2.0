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
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class SetSlopeOffset extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfADC1Slope;
	private JTextField tfADC1Off;
	private JTextField tfADC2Off;
	private JTextField tfADC2Slope;
	private JTextField tfADC3Slope;
	private JTextField tfADC3Off;
	private JTextField tfTemp1Slope;
	private JTextField tfTemp1Off;
	private JTextField tfTemp2Slope;
	private JTextField tfTemp2Off;
	private JTextField tfTemp3Slope;
	private JTextField tfTemp3Off;
	private JTextField tfCounter1Slope;
	private JTextField tfCounter1Off;
	private JTextField tfCounter2Slope;
	private JTextField tfCounter2Off;
	private JTextField tfResi3;
	private JTextField tfResi1;
	private JTextField tfResi2;
	public String ADC1Slope="NA";
	public String ADC1Off="NA";
	public String ADC2Off="NA";
	public String ADC2Slope="NA";
	public String ADC3Slope="NA";
	public String ADC3Off="NA";
	public String Temp1Slope="NA";
	public String Temp1Off="NA";
	public String Temp2Slope="NA";
	public String Temp2Off="NA";
	public String Temp3Slope="NA";
	public String Temp3Off="NA";
	public String Counter1Slope="NA";
	public String Counter1Off="NA";
	public String Counter2Slope="NA";
	public String Counter2Off="NA";
	public String Resi3="NA";
	public String Resi1="NA";
	public String Resi2="NA";
    public iniFile ini;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SetSlopeOffset dialog = new SetSlopeOffset();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SetSlopeOffset() {
		//load ini file for the IP
		iniFile ini = new iniFile();
		//load slopes+offsets from monitor
		getAllSettings(ini.IP);

		setBounds(100, 100, 792, 675);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(1, 0, 0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new GridLayout(0, 1, 0, 0));
			{
				JLabel lblNewLabel = new JLabel("ADC1:");
				lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblNewLabel);
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel lblNewLabel_1 = new JLabel("Slope");
					panel_1.add(lblNewLabel_1);
				}
				{
					tfADC1Slope = new JTextField();
					tfADC1Slope.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							//TODO: pressing enter pushes change button
						}
					});
					tfADC1Slope.setText(ADC1Slope);
					panel_1.add(tfADC1Slope);
					tfADC1Slope.setColumns(10);
				}
				{
					JButton btnADC1Slope = new JButton("change");
					btnADC1Slope.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							ADC1Slope = tfADC1Slope.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sts", "l", "1", ADC1Slope);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}						
						}
					});
					panel_1.add(btnADC1Slope);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel lblNewLabel_2 = new JLabel("Offset");
					panel_1.add(lblNewLabel_2);
				}
				{
					tfADC1Off = new JTextField();
					tfADC1Off.setText(ADC1Off);
					panel_1.add(tfADC1Off);
					tfADC1Off.setColumns(10);
				}
				{
					JButton btnADC1Off = new JButton("change");
					btnADC1Off.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							ADC1Off = tfADC1Off.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sto", "l", "1", ADC1Off);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}
					});
					panel_1.add(btnADC1Off);
				}
			}
			{
				JLabel lblNewLabel_4 = new JLabel("ADC2:");
				lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblNewLabel_4);
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel lblSlope = new JLabel("Slope");
					panel_1.add(lblSlope);
				}
				{
					tfADC2Slope = new JTextField();
					tfADC2Slope.setText(ADC2Slope);
					tfADC2Slope.setColumns(10);
					panel_1.add(tfADC2Slope);
				}
				{
					JButton btnADC2Slope = new JButton("change");
					btnADC2Slope.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							ADC2Slope = tfADC2Slope.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sts", "l", "2", ADC2Slope);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}
					});
					panel_1.add(btnADC2Slope);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel lblNewLabel_3 = new JLabel("Offset");
					panel_1.add(lblNewLabel_3);
				}
				{
					tfADC2Off = new JTextField();
					tfADC2Off.setText(ADC2Off);
					panel_1.add(tfADC2Off);
					tfADC2Off.setColumns(10);
				}
				{
					JButton btnADC2Off = new JButton("change");
					btnADC2Off.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							ADC2Off = tfADC2Off.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sto", "l", "2", ADC2Off);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}
					});
					panel_1.add(btnADC2Off);
				}
			}
			{
				JLabel lblAdc = new JLabel("ADC3:");
				lblAdc.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblAdc);
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel label = new JLabel("Slope");
					panel_1.add(label);
				}
				{
					tfADC3Slope = new JTextField();
					tfADC3Slope.setText(ADC3Slope);
					tfADC3Slope.setColumns(10);
					panel_1.add(tfADC3Slope);
				}
				{
					JButton btnADC3Slope = new JButton("change");
					btnADC3Slope.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							ADC3Slope = tfADC3Slope.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sts", "l", "3", ADC1Slope);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}
					});
					panel_1.add(btnADC3Slope);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel label = new JLabel("Offset");
					panel_1.add(label);
				}
				{
					tfADC3Off = new JTextField();
					tfADC3Off.setText(ADC3Off);
					tfADC3Off.setColumns(10);
					panel_1.add(tfADC3Off);
				}
				{
					JButton btnADC3Off = new JButton("change");
					btnADC3Off.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							ADC3Off = tfADC3Off.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sto", "l", "3", ADC3Off);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}
					});
					panel_1.add(btnADC3Off);
				}
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new GridLayout(0, 1, 0, 0));
			{
				JLabel lblTemp = new JLabel("Temp1:");
				lblTemp.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblTemp);
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel label = new JLabel("Slope");
					panel_1.add(label);
				}
				{
					tfTemp1Slope = new JTextField();
					tfTemp1Slope.setText(Temp1Slope);
					tfTemp1Slope.setColumns(10);
					panel_1.add(tfTemp1Slope);
				}
				{
					JButton btnTemp1Slope = new JButton("change");
					btnTemp1Slope.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Temp1Slope = tfTemp1Slope.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sts", "t", "1", Temp1Slope);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}
					});
					panel_1.add(btnTemp1Slope);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel label = new JLabel("Offset");
					panel_1.add(label);
				}
				{
					tfTemp1Off = new JTextField();
					tfTemp1Off.setText(Temp1Off);
					tfTemp1Off.setColumns(10);
					panel_1.add(tfTemp1Off);
				}
				{
					JButton btnTemp1Off = new JButton("change");
					btnTemp1Off.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Temp1Off = tfTemp1Off.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sto", "t", "1", Temp1Off);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}
					});
					panel_1.add(btnTemp1Off);
				}
			}
			{
				JLabel lblTemp_1 = new JLabel("Temp2:");
				lblTemp_1.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblTemp_1);
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel label = new JLabel("Slope");
					panel_1.add(label);
				}
				{
					tfTemp2Slope = new JTextField();
					tfTemp2Slope.setText(Temp2Slope);
					tfTemp2Slope.setColumns(10);
					panel_1.add(tfTemp2Slope);
				}
				{
					JButton btnTemp2Slope = new JButton("change");
					btnTemp2Slope.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Temp2Slope = tfTemp2Slope.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sts", "t", "2", Temp2Slope);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}
					});
					panel_1.add(btnTemp2Slope);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel label = new JLabel("Offset");
					panel_1.add(label);
				}
				{
					tfTemp2Off = new JTextField();
					tfTemp2Off.setText(Temp2Off);
					tfTemp2Off.setColumns(10);
					panel_1.add(tfTemp2Off);
				}
				{
					JButton btnTemp2Off = new JButton("change");
					btnTemp2Off.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Temp2Off = tfTemp2Off.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sto", "t", "2", Temp2Off);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}	
					});
					panel_1.add(btnTemp2Off);
				}
			}
			{
				JLabel lblTemp_2 = new JLabel("Temp3:");
				lblTemp_2.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblTemp_2);
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel label = new JLabel("Slope");
					panel_1.add(label);
				}
				{
					tfTemp3Slope = new JTextField();
					tfTemp3Slope.setText(Temp3Slope);
					tfTemp3Slope.setColumns(10);
					panel_1.add(tfTemp3Slope);
				}
				{
					JButton btnTemp3Slope = new JButton("change");
					btnTemp3Slope.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Temp3Slope = tfTemp3Slope.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sts", "t", "3", Temp3Slope);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}
					});
					panel_1.add(btnTemp3Slope);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel label = new JLabel("Offset");
					panel_1.add(label);
				}
				{
					tfTemp3Off = new JTextField();
					tfTemp3Off.setText(Temp3Off);
					tfTemp3Off.setColumns(10);
					panel_1.add(tfTemp3Off);
				}
				{
					JButton btnTemp3Off = new JButton("change");
					btnTemp3Off.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Temp3Off = tfTemp3Off.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sto", "t", "3", Temp3Off);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}
					});
					panel_1.add(btnTemp3Off);
				}
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new GridLayout(0, 1, 0, 0));
			{
				JLabel lblCounter = new JLabel("Counter1:");
				lblCounter.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblCounter);
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel label = new JLabel("Slope");
					panel_1.add(label);
				}
				{
					tfCounter1Slope = new JTextField();
					tfCounter1Slope.setText(Counter1Slope);
					tfCounter1Slope.setColumns(10);
					panel_1.add(tfCounter1Slope);
				}
				{
					JButton btnCounter1Slope = new JButton("change");
					btnCounter1Slope.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Counter1Slope = tfCounter1Slope.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sts", "c", "1", Counter1Slope);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}
					});
					panel_1.add(btnCounter1Slope);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel label = new JLabel("Offset");
					panel_1.add(label);
				}
				{
					tfCounter1Off = new JTextField();
					tfCounter1Off.setText(Counter1Off);
					tfCounter1Off.setColumns(10);
					panel_1.add(tfCounter1Off);
				}
				{
					JButton btnCounter1Off = new JButton("change");
					btnCounter1Off.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Counter1Off = tfCounter1Off.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sto", "c", "1", Counter1Off);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}
					});
					panel_1.add(btnCounter1Off);
				}
			}
			{
				JLabel lblCounter_1 = new JLabel("Counter2:");
				lblCounter_1.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(lblCounter_1);
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel label = new JLabel("Slope");
					panel_1.add(label);
				}
				{
					tfCounter2Slope = new JTextField();
					tfCounter2Slope.setText(Counter2Slope);
					tfCounter2Slope.setColumns(10);
					panel_1.add(tfCounter2Slope);
				}
				{
					JButton btnCounter2Slope = new JButton("change");
					btnCounter2Slope.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Counter2Slope = tfCounter2Slope.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sts", "c", "2", Counter2Slope);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}
					});
					panel_1.add(btnCounter2Slope);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				{
					JLabel label = new JLabel("Offset");
					panel_1.add(label);
				}
				{
					tfCounter2Off = new JTextField();
					tfCounter2Off.setText(Counter2Off);
					tfCounter2Off.setColumns(10);
					panel_1.add(tfCounter2Off);
				}
				{
					JButton btnCounter2Off = new JButton("change");
					btnCounter2Off.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Counter2Off = tfCounter2Off.getText();
							telnetTunnel con = new telnetTunnel(ini.IP, 23);
							try {
								con.set("sto", "c", "2", Counter2Off);
							} catch (IOException | InterruptedException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Could not set the value. Maybe " + ini.IP + " is the wrong IP?", "Set value", JOptionPane.WARNING_MESSAGE);
								e1.printStackTrace();
							}
						}
					});
					panel_1.add(btnCounter2Off);
				}
			}
			{
				JPanel panel_1_1 = new JPanel();
				panel.add(panel_1_1);
				panel_1_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				{
					JLabel lblPtResistance = new JLabel("PT 100 Resistance");
					panel_1_1.add(lblPtResistance);
					lblPtResistance.setHorizontalAlignment(SwingConstants.CENTER);
				}
				{
					JButton btnResistance = new JButton("load");
					btnResistance.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							loadResistance();
						}
					});
					panel_1_1.add(btnResistance);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				panel_1.setLayout(new GridLayout(0, 1, 0, 0));
				{
					JPanel panel_2 = new JPanel();
					panel_1.add(panel_2);
					{
						JLabel lblResistanceTemp = new JLabel("Resistance Temp 1");
						panel_2.add(lblResistanceTemp);
					}
					{
						tfResi1 = new JTextField();
						tfResi1.setEditable(false);
						tfResi1.setText("0.00");
						tfResi1.setColumns(10);
						panel_2.add(tfResi1);
					}
				}
				{
					JPanel panel_1_1 = new JPanel();
					panel_1.add(panel_1_1);
					{
						JLabel lblResistanceTemp_1 = new JLabel("Resistance Temp 2");
						panel_1_1.add(lblResistanceTemp_1);
					}
					{
						tfResi2 = new JTextField();
						tfResi2.setText("0.00");
						tfResi2.setEditable(false);
						tfResi2.setColumns(10);
						panel_1_1.add(tfResi2);
					}
				}
				{
					JPanel panel_2 = new JPanel();
					panel_1.add(panel_2);
					{
						JLabel lblResistanceTemp_2 = new JLabel("Resistance Temp 3");
						panel_2.add(lblResistanceTemp_2);
					}
					{
						tfResi3 = new JTextField();
						tfResi3.setText("0.00");
						tfResi3.setEditable(false);
						tfResi3.setColumns(10);
						panel_2.add(tfResi3);
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public void getAllSettings(String IP) {
		//get all hardwaresettings when starting up this window
		//open tunnel
		System.out.println("Starting getAllSettings with IP " + IP);

		telnetTunnel con = new telnetTunnel(IP, 23);
		//get slopes and offsets via the commands "sof" (offsets) and "ssl" (slopes)
		//returns e.g. "23.12345 0.00000 0.00000 0.00000 0.00000 0.00000 0.00000 0.00000"
		//for ADC1Slope, ADC2Slope, ADC3Slope, Temp1Slope, Temp2Slope, Temp3Slope, Counter1Slope, Counter2Slope
		//same for Offsets
		//TODO: check if the order is correct
		
		String[] slopes = new String[8];
		slopes = null;
		String[] offsets = new String[8];
		offsets = null;
		System.out.println("connect");
		try {
			offsets = con.get("sof").split(" ");
			slopes = con.get("ssl").split(" ");
			//TODO: connect: Address is invalid on local machine, or port is not valid on remote machine
		} catch (IOException | InterruptedException e1) {
			JOptionPane.showMessageDialog(null, "Could not connect to the Monitor. Maybe " + IP + " is the wrong IP?", "Loading Settings", JOptionPane.WARNING_MESSAGE);
			e1.printStackTrace();
			dispose();
		} 
		//check again if you got a correct answer
		if(slopes == null || offsets == null) {
			JOptionPane.showMessageDialog(null, "Could not connect to the Monitor. Maybe " + ini.IP + " is the wrong IP?", "Loading Settings", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		try {
			ADC1Slope = slopes[0]; ADC2Slope = slopes[1]; ADC3Slope = slopes[2]; Temp1Slope = slopes[3]; Temp2Slope = slopes[4]; Temp3Slope = slopes[5]; Counter1Slope = slopes[6]; Counter2Slope = slopes[7];
			ADC1Off = offsets[0]; ADC2Off = offsets[1]; ADC3Off = offsets[2]; Temp1Off = offsets[3]; Temp2Off = offsets[4]; Temp3Off = offsets[5]; Counter1Off = offsets[6]; Counter2Off = offsets[7];
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "The recieved data by the monitor is wrong. Maybe the connection got interrupted.", "Loading Settings", JOptionPane.WARNING_MESSAGE);
			e1.printStackTrace();
			dispose();
		}
	}
	
	public void loadResistance() {
		//get the PT100 resistance factors
		//TODO: implement function		
	}
	
	public void set(String value, String IP) {

	}

}
