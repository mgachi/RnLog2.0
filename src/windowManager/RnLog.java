package windowManager;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.Color;
import javax.swing.JRadioButton;
import java.awt.SystemColor;

import javax.swing.JProgressBar;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RnLog extends JFrame{
				
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public static JTextField textField;
	public static JTextField textField_1;
	public static JTextField textField_2;
	public static JTextField textField_3;
	public static JTextField textField_4;
	public static JTextField tfIP;
	public static JTextField tfCOMPort;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	public static JTextField tfEdge;
	public static JTextField textField_8;
	public static JTextField textField_9;
	public static JTextField textField_10;
	public static JTextField textField_11;
	public static JTextField textField_12;
	public static JTextField textField_13;
	public static JTextField textField_14;
	public static JTextField textField_15;
	public static JTextField textField_16;
	public static JTextField textField_17;
	public static JTextField textField_18;
	public static JTextField textField_19;
	public static JTextField textField_20;
	public ArrayList<Spectra> spectraList = new ArrayList<Spectra>();
	//for continue evaluation, save the non flagged spectra for the user
	public ArrayList<Spectra> tmpList = new ArrayList<Spectra>();
	public JProgressBar progressBar;
	//the currently selected spectrum of the list
	public int selectedSpecIdx = 0;
	public Spectra RefSpec;
	public String SoftwareVersion = "RnLog 2.7";
	Thread TLiveMode;
	//for continue evaluation mode
	public ArrayList<Spectra> flagged = new ArrayList<Spectra>();
	public ArrayList<Integer> flaggedIdx = new ArrayList<Integer>();
	public int currentflaggedSpec = 0;
	public File extract = null;
	public File activity = null;
	
	//load *.ini file
	public iniFile ini = new iniFile();
	//global temporary holder for the files (need global for the SwingWorker)
	public ArrayList<File> tempFileList = new ArrayList<File>();
	//global holder for the files to evaluate (need global for the SwingWorker)
	public ArrayList<File> toEvaluate = new ArrayList<File>();
	//indicators of complicity for the SwingWorkers
	public boolean isCopyingFilesDone;
	public boolean isSettingFilesDone;
	public boolean isSortingExtractFileDone;
	public boolean isLoadingFilesDone;
	//indicator of rerunning the continueEval part due to the creation of the ref spectrum
	public boolean isRefSpecRun = false;
	public ArrayList<Spectra> suitableSpectra = new ArrayList<Spectra>();
	
	//Launch the application.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RnLog frame = new RnLog();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//creating the main frame
	public RnLog() {
		setResizable(false);
		setBackground(Color.RED);
	
		
		setTitle(SoftwareVersion);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 953, 573);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.menu);
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_1.setBounds(0, 0, 950, 21);
		panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPane.add(panel_1);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.menu);
		panel_1.add(menuBar);
		menuBar.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		
		JMenu mnNewMenu = new JMenu("Analyze");
		mnNewMenu.setMnemonic('A');
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Select Spectra");
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Make Extract File (.txt)");
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_8 = new JMenuItem("Make Activity File (.act)");
		mnNewMenu.add(mntmNewMenuItem_8);
		
		JMenuItem mntmNewMenuItem_7 = new JMenuItem("Full evaluation");
		mnNewMenu.add(mntmNewMenuItem_7);
		
		JMenu mnNewMenu_1 = new JMenu("Configure");
		mnNewMenu_1.setMnemonic('C');
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Hardware Settings");
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SetHardware dialog = new SetHardware();
				System.out.println(ini.IP);
				dialog.setVisible(true);
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_4);
		
		JMenu mnNewMenu_2 = new JMenu("Extras");
		mnNewMenu_2.setMnemonic('E');
		menuBar.add(mnNewMenu_2);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.text);
		panel.setBounds(0, 22, 950, 523);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Temperature 1:");
		lblNewLabel.setBounds(10, 21, 91, 14);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Std.Dev.:");
		lblNewLabel_1.setBounds(111, 21, 56, 14);
		panel.add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBackground(SystemColor.text);
		textField.setEditable(false);
		textField.setHorizontalAlignment(SwingConstants.RIGHT);
		textField.setText("0,0");
		textField.setBounds(111, 36, 56, 20);
		textField.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBackground(SystemColor.text);
		textField_1.setEditable(false);
		textField_1.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_1.setText("0,00");
		textField_1.setBounds(10, 36, 86, 20);
		textField_1.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Time:");
		lblNewLabel_2.setBounds(206, 21, 46, 14);
		panel.add(lblNewLabel_2);
		
		textField_2 = new JTextField();
		textField_2.setBackground(SystemColor.text);
		textField_2.setEditable(false);
		textField_2.setBounds(206, 36, 284, 20);
		textField_2.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Lifetime:");
		lblNewLabel_3.setBounds(500, 21, 86, 14);
		panel.add(lblNewLabel_3);
		
		textField_3 = new JTextField();
		textField_3.setBackground(SystemColor.text);
		textField_3.setEditable(false);
		textField_3.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_3.setText("0");
		textField_3.setBounds(500, 36, 86, 20);
		textField_3.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Name:");
		lblNewLabel_4.setBounds(596, 21, 140, 14);
		panel.add(lblNewLabel_4);
		
		textField_4 = new JTextField();
		textField_4.setBackground(SystemColor.text);
		textField_4.setEditable(false);
		textField_4.setBounds(596, 36, 179, 20);
		textField_4.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_4);
		textField_4.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(SystemColor.menu);
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_2.setBounds(789, 67, 140, 175);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblConfiguration = new JLabel("Configuration");
		lblConfiguration.setBounds(4, 2, 126, 14);
		panel_2.add(lblConfiguration);
		
		JLabel lblNewLabel_5 = new JLabel("Monitor IP:");
		lblNewLabel_5.setBounds(10, 21, 106, 14);
		panel_2.add(lblNewLabel_5);
		
		tfIP = new JTextField();
		tfIP.setBackground(SystemColor.text);
		tfIP.setHorizontalAlignment(SwingConstants.RIGHT);
		tfIP.setBounds(10, 36, 106, 20);
		tfIP.setBorder(new LineBorder(SystemColor.textText,1));
		tfIP.setText(ini.IP);
		panel_2.add(tfIP);
		tfIP.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("COM Port:");
		lblNewLabel_6.setBounds(10, 68, 106, 14);
		panel_2.add(lblNewLabel_6);
		
		tfCOMPort = new JTextField();
		tfCOMPort.setEditable(false);
		tfCOMPort.setEnabled(false);
		tfCOMPort.setBackground(SystemColor.text);
		tfCOMPort.setHorizontalAlignment(SwingConstants.RIGHT);
		tfCOMPort.setBounds(10, 83, 106, 20);
		tfCOMPort.setBorder(new LineBorder(SystemColor.textText,1));
		panel_2.add(tfCOMPort);
		tfCOMPort.setColumns(10);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("RS232");
		rdbtnNewRadioButton.setEnabled(false);
		rdbtnNewRadioButton.setForeground(SystemColor.textText);
		rdbtnNewRadioButton.setBackground(SystemColor.menu);
		buttonGroup.add(rdbtnNewRadioButton);
		rdbtnNewRadioButton.setBounds(10, 112, 109, 23);
		panel_2.add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("TCP/IP");
		rdbtnNewRadioButton_1.setForeground(SystemColor.textText);
		rdbtnNewRadioButton_1.setSelected(true);
		buttonGroup.add(rdbtnNewRadioButton_1);
		rdbtnNewRadioButton_1.setBounds(10, 138, 109, 23);
		panel_2.add(rdbtnNewRadioButton_1);
		
		progressBar = new JProgressBar();
		progressBar.setEnabled(false);
		progressBar.setBounds(10, 490, 919, 14);
		panel.add(progressBar);
		
		JToggleButton tglbtnLive = new JToggleButton("Live Mode");
	
		tglbtnLive.setBackground(SystemColor.menu);
		tglbtnLive.setForeground(SystemColor.textText);
		tglbtnLive.setToolTipText("Enable Live Mode to connect to the monitor");
		tglbtnLive.setBounds(789, 247, 140, 23);
		tglbtnLive.setFocusable(false);
		panel.add(tglbtnLive);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(SystemColor.menu);
		panel_3.setLayout(null);
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_3.setBounds(789, 304, 140, 175);
		panel.add(panel_3);
		
		JLabel lblBrowsing = new JLabel("Browsing");
		lblBrowsing.setBounds(4, 2, 126, 14);
		panel_3.add(lblBrowsing);
		
		JLabel lblMoveEdge = new JLabel("Move Edge");
		lblMoveEdge.setBounds(10, 21, 73, 14);
		panel_3.add(lblMoveEdge);
		
		tfEdge = new JTextField();
		tfEdge.setBackground(SystemColor.text);
		tfEdge.setEditable(false);
		tfEdge.setHorizontalAlignment(SwingConstants.RIGHT);
		tfEdge.setColumns(10);
		tfEdge.setBorder(new LineBorder(SystemColor.textText,1));
		tfEdge.setBounds(75, 18, 44, 20);
		panel_3.add(tfEdge);
		
		textField_8 = new JTextField();
		textField_8.setBackground(SystemColor.text);
		textField_8.setEditable(false);
		textField_8.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_8.setText("0,0");
		textField_8.setColumns(10);
		textField_8.setBounds(111, 82, 56, 20);
		textField_8.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_8);
		
		textField_9 = new JTextField();
		textField_9.setBackground(SystemColor.text);
		textField_9.setEditable(false);
		textField_9.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_9.setText("0,00");
		textField_9.setColumns(10);
		textField_9.setBounds(10, 82, 86, 20);
		textField_9.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_9);
		
		JLabel lblTemperature = new JLabel("Temperature 2:");
		lblTemperature.setBounds(10, 67, 91, 14);
		panel.add(lblTemperature);
		
		JLabel label_1 = new JLabel("Std.Dev.:");
		label_1.setBounds(111, 67, 56, 14);
		panel.add(label_1);
		
		textField_10 = new JTextField();
		textField_10.setBackground(SystemColor.text);
		textField_10.setEditable(false);
		textField_10.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_10.setText("0,0");
		textField_10.setColumns(10);
		textField_10.setBounds(111, 128, 56, 20);
		textField_10.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_10);
		
		textField_11 = new JTextField();
		textField_11.setBackground(SystemColor.text);
		textField_11.setEditable(false);
		textField_11.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_11.setText("0,00");
		textField_11.setColumns(10);
		textField_11.setBounds(10, 128, 86, 20);
		textField_11.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_11);
		
		JLabel lblTemperature_1 = new JLabel("Temperature 3:");
		lblTemperature_1.setBounds(10, 113, 91, 14);
		panel.add(lblTemperature_1);
		
		JLabel label_3 = new JLabel("Std.Dev.:");
		label_3.setBounds(111, 113, 56, 14);
		panel.add(label_3);
		
		textField_12 = new JTextField();
		textField_12.setBackground(SystemColor.text);
		textField_12.setEditable(false);
		textField_12.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_12.setText("0,0");
		textField_12.setColumns(10);
		textField_12.setBounds(111, 174, 56, 20);
		textField_12.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_12);
		
		textField_13 = new JTextField();
		textField_13.setBackground(SystemColor.text);
		textField_13.setEditable(false);
		textField_13.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_13.setText("0,00");
		textField_13.setColumns(10);
		textField_13.setBounds(10, 174, 86, 20);
		textField_13.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_13);
		
		JLabel lblAdc = new JLabel("ADC 1 [l/h]:");
		lblAdc.setBounds(10, 159, 75, 14);
		panel.add(lblAdc);
		
		JLabel label_5 = new JLabel("Std.Dev.:");
		label_5.setBounds(111, 159, 56, 14);
		panel.add(label_5);
		
		textField_14 = new JTextField();
		textField_14.setBackground(SystemColor.text);
		textField_14.setEditable(false);
		textField_14.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_14.setText("0,0");
		textField_14.setColumns(10);
		textField_14.setBounds(111, 220, 56, 20);
		textField_14.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_14);
		
		JLabel label = new JLabel("Std.Dev.:");
		label.setBounds(111, 205, 56, 14);
		panel.add(label);
		
		JLabel lblAdc_1 = new JLabel("ADC 2:");
		lblAdc_1.setBounds(10, 205, 75, 14);
		panel.add(lblAdc_1);
		
		textField_15 = new JTextField();
		textField_15.setBackground(SystemColor.text);
		textField_15.setEditable(false);
		textField_15.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_15.setText("0,00");
		textField_15.setColumns(10);
		textField_15.setBounds(10, 220, 86, 20);
		textField_15.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_15);
		
		textField_16 = new JTextField();
		textField_16.setBackground(SystemColor.text);
		textField_16.setEditable(false);
		textField_16.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_16.setText("0,0");
		textField_16.setColumns(10);
		textField_16.setBounds(111, 266, 56, 20);
		textField_16.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_16);
		
		JLabel label_4 = new JLabel("Std.Dev.:");
		label_4.setBounds(111, 251, 56, 14);
		panel.add(label_4);
		
		JLabel lblAdc_2 = new JLabel("ADC 3:");
		lblAdc_2.setBounds(10, 251, 75, 14);
		panel.add(lblAdc_2);
		
		textField_17 = new JTextField();
		textField_17.setBackground(SystemColor.text);
		textField_17.setEditable(false);
		textField_17.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_17.setText("0,00");
		textField_17.setColumns(10);
		textField_17.setBounds(10, 266, 86, 20);
		textField_17.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_17);
		
		textField_18 = new JTextField();
		textField_18.setBackground(SystemColor.text);
		textField_18.setEditable(false);
		textField_18.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_18.setText("0,00");
		textField_18.setColumns(10);
		textField_18.setBounds(10, 312, 86, 20);
		textField_18.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_18);
		
		JLabel lblCounter = new JLabel("Counter 1:");
		lblCounter.setBounds(10, 297, 75, 14);
		panel.add(lblCounter);
		
		textField_19 = new JTextField();
		textField_19.setBackground(SystemColor.text);
		textField_19.setEditable(false);
		textField_19.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_19.setText("0,00");
		textField_19.setColumns(10);
		textField_19.setBounds(10, 358, 86, 20);
		textField_19.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_19);
		
		JLabel lblCounter_1 = new JLabel("Counter 2:");
		lblCounter_1.setBounds(10, 343, 75, 14);
		panel.add(lblCounter_1);
		
		textField_20 = new JTextField();
		textField_20.setBackground(SystemColor.text);
		textField_20.setEditable(false);
		textField_20.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_20.setText("0");
		textField_20.setColumns(10);
		textField_20.setBounds(10, 404, 86, 20);
		textField_20.setBorder(new LineBorder(SystemColor.textText,1));
		panel.add(textField_20);
		
		JLabel lblRnIntegral = new JLabel("RN Integral:");
		lblRnIntegral.setBounds(10, 389, 75, 14);
		panel.add(lblRnIntegral);
		
		ChartPanel chartPanel = new ChartPanel((JFreeChart) null);
		chartPanel.setBounds(169, 65, 618, 414);
		panel.add(chartPanel);
		
		chartPanel.setBackground(Color.WHITE);
		chartPanel.setMinimumDrawHeight(0);
		chartPanel.setEnforceFileExtensions(false);
		chartPanel.setFillZoomRectangle(false);
		chartPanel.setDisplayToolTips(false);
		chartPanel.setZoomOutFactor(1.0);
		chartPanel.setZoomInFactor(1.0);
		chartPanel.setMouseZoomable(false);
		chartPanel.setMinimumDrawWidth(0);
		chartPanel.setMaximumDrawWidth(588);
		chartPanel.setLayout(null);
		
		XYSeries series = new XYSeries("");
		XYSeriesCollection dataset = new XYSeriesCollection(series);
		series.add(0.0, 0.0);
        series.add(128.0, 50.0);
        
		JFreeChart chart = ChartFactory.createXYLineChart("", "" /*x-axis label*/, "" /*y-axis label*/, dataset);
		chart.removeLegend();
		chart.setTitle("");
		chart.getPlot().setBackgroundPaint( Color.WHITE );
		chartPanel.setChart(chart);	
		
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseSpectraDialog(chartPanel);
			}
		});
		

		JButton previousSpectrum = new JButton("<<");
		previousSpectrum.setForeground(SystemColor.textText);
		previousSpectrum.setBackground(SystemColor.inactiveCaption);
		previousSpectrum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(spectraList.size() == 0) return;
				try {
					tfEdge.setText(spectraList.get(selectedSpecIdx - 1).showSpectra(chartPanel));
					selectedSpecIdx--;
				} catch (Exception e) {
		             System.out.println("no previous spectrum found -> out of bounds"); 
					//no previous spectrum found -> out of bounds
				}
			}
		});
		previousSpectrum.setFocusable(false);
		previousSpectrum.setBounds(10, 95, 60, 23);
		panel_3.add(previousSpectrum);

		JButton nextSpectrum = new JButton(">>");
		nextSpectrum.setForeground(SystemColor.textText);
		nextSpectrum.setBackground(SystemColor.inactiveCaption);
		nextSpectrum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(spectraList.size() == 0) return;
				//show next spectra of the selected ones
				try {
					tfEdge.setText(spectraList.get(selectedSpecIdx + 1).showSpectra(chartPanel));
					selectedSpecIdx++;
				} catch (Exception e) {
					System.out.println("no previous spectrum found -> out of bounds"); 
					//no next spectrum found -> out of bounds
				}
			}
		});
		nextSpectrum.setFocusable(false);
		nextSpectrum.setBounds(70, 95, 60, 23);
		panel_3.add(nextSpectrum);	
		
		JButton edgeLeft = new JButton("<<");
		edgeLeft.setForeground(SystemColor.textText);
		edgeLeft.setBackground(SystemColor.inactiveCaption);
		edgeLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(spectraList.size() == 0) return;
				try {
					tfEdge.setText(spectraList.get(selectedSpecIdx).changeEdge(chartPanel, chart, false));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		edgeLeft.setFocusable(false);
		edgeLeft.setBounds(10, 40, 60, 23);
		panel_3.add(edgeLeft);
		
		JButton edgeRight= new JButton(">>");
		edgeRight.setForeground(SystemColor.textText);
		edgeRight.setBackground(SystemColor.inactiveCaption);
		edgeRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(spectraList.size() == 0) return;
				try {
					tfEdge.setText(spectraList.get(selectedSpecIdx).changeEdge(chartPanel, chart, true));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		edgeRight.setFocusable(false);
		edgeRight.setBounds(70, 40, 60, 23);
		panel_3.add(edgeRight);
		
		JLabel lblMoveEdge_1 = new JLabel("Change Spectrum");
		lblMoveEdge_1.setBounds(10, 76, 126, 14);
		panel_3.add(lblMoveEdge_1);	
		
		JButton rejectSpectrum = new JButton("Reject");
		rejectSpectrum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(spectraList.size() == 0) return;
				try {
					
	        		//moving spectra to the new rejected subfolder
	        		new File(spectraList.get(selectedSpecIdx).path.getParent()+ "\\rejected").mkdirs();
	        		
	        		//removing edge in the rejected spectrum
	        		spectraList.get(selectedSpecIdx).removeEdge();
	        		
	        		//creating copy of the spectrum in the rejected folder
		        	File tmpRejected = new File(spectraList.get(selectedSpecIdx).path.getParent()+ "\\rejected\\" + spectraList.get(selectedSpecIdx).name);
		        	copyFile(spectraList.get(selectedSpecIdx).path, tmpRejected);
		        	System.out.println("copied " + spectraList.get(selectedSpecIdx).path.getPath() + " to " + tmpRejected.getPath() );
		        		
		        	//deleting file from the lvl2 directory if rejected
		        	File file = new File(spectraList.get(selectedSpecIdx).path.getPath());
	        		file.delete(); 
	        		System.out.println("File moved successfully"); 
	        		
	        		//removing spectrum from the spectra list and showing next
	        		spectraList.remove(selectedSpecIdx);
	        		
	        		//if the reject procedure is run during the manual edge set for the flagged spectra 
	        		//this lines will ensure that corrected information is passed on 
	        		if(!flaggedIdx.isEmpty()) {
	        			flaggedIdx.remove(selectedSpecIdx);
	    			}
	        		
	        		if (selectedSpecIdx <= spectraList.size()-1) {
	        			//showing next if there is a next spectrum
	        			tfEdge.setText(spectraList.get(selectedSpecIdx).showSpectra(chartPanel));
	        		} else {
	        			//showing previous if it is the end of the list
	        			tfEdge.setText(spectraList.get(selectedSpecIdx-1).showSpectra(chartPanel));
	        			selectedSpecIdx--;
	        			
	        		}
	        		
				} catch (Exception e1) {
					System.out.println("no spectrum found -> out of bounds");
					JOptionPane.showMessageDialog(null, "There are no more spectra to show", "List empty", JOptionPane.INFORMATION_MESSAGE);
					//e1.printStackTrace();
					
				}
				
			}
		});
		rejectSpectrum.setBackground(SystemColor.inactiveCaption);
		rejectSpectrum.setToolTipText("Remove current spectrum from the evaluation cycle and move it to the rejected folder.");
		rejectSpectrum.setBounds(10, 129, 120, 20);
		panel_3.add(rejectSpectrum);
		
		JButton btnReadMemoryCard = new JButton("Read Memory Card");
		btnReadMemoryCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tfIP.getText().isEmpty()) {
					tglbtnLive.setSelected(false);
					JOptionPane.showMessageDialog(null, "Please enter an IP address, e.g. 129.206.22.122"/*, JOptionPane.INFORMATION_MESSAGE*/);
					return;
				}
				if(tfIP.getText().split("\\.").length != 4) {
					tglbtnLive.setSelected(false);
					JOptionPane.showMessageDialog(null, "Please enter a valid IP address."/*, JOptionPane.INFORMATION_MESSAGE*/);
					return;
				}
				//start new window where you can copy files from the card
				//clear the list of files
				SDCardDialog SDdialog = new SDCardDialog(tfIP.getText());
				System.out.println(ini.IP);
				SDdialog.setVisible(true);
			}
		});
		btnReadMemoryCard.setToolTipText("Connect to the monitor and copy files from the SD card");
		btnReadMemoryCard.setForeground(Color.BLACK);
		btnReadMemoryCard.setFocusable(false);
		btnReadMemoryCard.setBackground(SystemColor.menu);
		btnReadMemoryCard.setBounds(789, 276, 140, 23);
		panel.add(btnReadMemoryCard);
		
		//Creating option to switch between spectra and change edge with arrow keys
		KeyEventDispatcher keyDispatcher = new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				//if no spectra is loaded -> ignore
				if(spectraList.size() == 0) return false;
				//if other window is opened -> ignore
				if (!RnLog.this.isActive()) return false;
				
				//down key takes previous spectrum
		        if(e.getKeyCode() == KeyEvent.VK_DOWN && e.getID() == KeyEvent.KEY_PRESSED) {
					try {
						tfEdge.setText(spectraList.get(selectedSpecIdx - 1).showSpectra(chartPanel));
						selectedSpecIdx--;
					} catch (Exception e2) {
			             System.out.println("no previous spectrum found -> out of bounds"); 
						//no previous spectrum found -> out of bounds
					}
		         }
		        
		        //right key moves Po edge to the right
		        if(e.getKeyCode() == KeyEvent.VK_RIGHT && e.getID() == KeyEvent.KEY_PRESSED) {
					try {
						tfEdge.setText(spectraList.get(selectedSpecIdx).changeEdge(chartPanel, chart, true));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
		        }
		        
		        //left key moves Po edge to the left
		        if(e.getKeyCode() == KeyEvent.VK_LEFT && e.getID() == KeyEvent.KEY_PRESSED) {
					try {
						tfEdge.setText(spectraList.get(selectedSpecIdx).changeEdge(chartPanel, chart, false));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
		        }
		        
		        //up key moves to the next spectrum
		        if(e.getKeyCode() == KeyEvent.VK_UP && e.getID() == KeyEvent.KEY_PRESSED) {
					try {
						tfEdge.setText(spectraList.get(selectedSpecIdx + 1).showSpectra(chartPanel));
						selectedSpecIdx++;
					} catch (Exception e2) {
			             System.out.println("no previous spectrum found -> out of bounds"); 
						//no previous spectrum found -> out of bounds
					}
		        }
		        return true;
			}
		};
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyDispatcher);
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("Reset Spectra");
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spectraList.clear();
				chooseSpectraDialog(chartPanel);
				if(spectraList.isEmpty()) {
					//user cancelled operation
					return;
				}
				try {
					for(int i=0; i<spectraList.size(); i++) {
						spectraList.get(i).removeEdge();
					}
					JOptionPane.showMessageDialog(null, "Reset " + spectraList.size() + " Spectra successfully", "Reset Spectra", JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Could not reset Spectra", "Reset Spectra", JOptionPane.INFORMATION_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		mnNewMenu_2.add(mntmNewMenuItem_5);

		JMenuItem mntmNewMenuItem_6 = new JMenuItem("Create temporary reference spectrum");
		mntmNewMenuItem_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		    	System.out.println("choose Spectra");
		    	spectraList.clear();
				chooseSpectraDialog(chartPanel);
				if(spectraList.isEmpty()) {
					//user cancelled operation
					return;
				}
		    	System.out.println("spectra chosen");
				try {
					RefSpec = new Spectra(spectraList, ini, spectraList.get(0).path.getParent()+"\\temp_ref_spec.ref");
					spectraList.clear();
					spectraList.add(RefSpec);
					tfEdge.setText(RefSpec.showSpectra(chartPanel));
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Could not create temporary reference specterum. Maybe you have no writing permissions?", "Create Reference Spectrum", JOptionPane.INFORMATION_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		mnNewMenu_2.add(mntmNewMenuItem_6);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Evaluation Settings");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ini._pathToIniFile == null) {
					ini = new iniFile();
					} 
					EvalIniDialog dialog = new EvalIniDialog(ini);
					dialog.setVisible(true);
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_3);
		
		
		//Extract Spectra
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//clearing global variables
				spectraList.clear();
				RefSpec = null;
				//select spectra
				chooseSpectraDialog(chartPanel);
				if(spectraList.isEmpty()) {
					//user cancelled operation
					return;
				}
				
				//if no RefSpec was loaded among other files,
				//checking if all spectra already have the edge determined
				if (RefSpec == null) {
					for (Spectra element : spectraList) {
						//if there are files with no edge user is asked to provide a RefSpec
						System.out.print(element.edge);
						if (element.edge == -1) {
							
							int dialogButton = JOptionPane.YES_NO_OPTION;
				        	int dialogResult = JOptionPane.showConfirmDialog (null, "Not all selected spectra have the Po214-edge set. To continue you have to provide a reference spectrum (.ref). Would you like to load one?" ,"Load reference spectrum", dialogButton);
				        	if(dialogResult == JOptionPane.YES_OPTION){
						    	//Create a file chooser
						        JFileChooser fileDialog = new JFileChooser(spectraList.get(0).path.getParent());
						        
						        //name of the file chooser window
						        fileDialog.setDialogTitle("Choose reference spectrum (.ref)");
						        //only show not hidden files
						        fileDialog.setFileHidingEnabled(true);			       
						        //to select single file
						        fileDialog.setMultiSelectionEnabled(false);
						        //In response to a button click:
						        int option = fileDialog.showOpenDialog(null);
						        
						        //defined globally
						        if(option == JFileChooser.APPROVE_OPTION) {
						        	int j = fileDialog.getSelectedFile().getName().lastIndexOf('.');
						        	//checking if the file extension is .ref
				        			if (!(fileDialog.getSelectedFile().getName().endsWith(".ref"))) {
						        		System.out.print(fileDialog.getSelectedFile().getName().substring(j+1));
					      				JOptionPane.showMessageDialog(null, "Provided file is not a reference file (.ref)!" , "Load reference spectrum", JOptionPane.ERROR_MESSAGE);
						        		return;
				        			}
						        	
						            try {
						            	//loading ref spectrum
										RefSpec = new Spectra(fileDialog.getSelectedFile().getName(),fileDialog.getSelectedFile());
									} catch (Exception e1) {
										e1.printStackTrace();
									}
						        }
						    	
						    } else {
						    	//user canceled
						    	JOptionPane.showMessageDialog(null, "For the automatic browsing you have to provide reference spectrum!" , "Load reference spectrum", JOptionPane.ERROR_MESSAGE);
						    	return;
						    }
							
						}
						//exiting for loop, RefSpec is loaded
						break;					
					}
				}
				
				JFileChooser fileChooser = new JFileChooser(spectraList.get(0).path.getParent()); 
				//name of the file chooser window
				fileChooser.setDialogTitle("Save extract file (.txt) as:");
        		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
        			File extFile = fileChooser.getSelectedFile();
        			try {
        				//checking if the file extension is .txt and setting it to .txt if other
	        			if (!(extFile.getName().endsWith(".txt"))) {
	        				extFile = new File(extFile+".txt");
	        			}
      				} catch (Exception e1) {
      				  JOptionPane.showMessageDialog(null, "Could not create the extract file, maybe you have no writing permissions?" , "Continue evaluation", JOptionPane.INFORMATION_MESSAGE);
      				  e1.printStackTrace();
      				}
					//extract spectra from spectra list and write it into extFile
					FileOutputStream fileOut;
					try {
						fileOut = new FileOutputStream(extFile);
				    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOut));
				    	double progress = 0;
				    	progressBar.setValue((int) (progress));
				    	progressBar.setStringPainted(true);
				    	//write first line
				        bw.write("Date Time; Lifetime;ADC1; StdADC1; T1; StdT1;T2; StdT2;T3; StdT3;Rn1;Rn2;Rn3;Rn4;ADC2; StdADC2; ADC3; StdADC3; Counter1;Counter2;FluxSlope;FluxOffset;ADC2Slope;ADC2Offset;ADC3Slope;ADC3Offset;Temp1Slope;Temp1Offset;Temp2Slope;Temp2Offset;Temp3Slope;Temp3Offset;Counter1Slope;Counter1Offset;Counter2Slope;Counter2Offset;ID \r\n");
				        //loop though the selected spectra, see if they need to be flagged
				        for(int i=0; i<spectraList.size(); i++) {
				        	
				        	//checking if the flux is higher than the flux threshold
							if (spectraList.get(i).ADC1 < ini.fluxthreshold) {
								
								//moving  Spectra to the new lowFlux subfolder
				        		new File(spectraList.get(i).path.getParent()+ "\\lowFlux").mkdirs();
				        		File lowFlux = new File(spectraList.get(i).path.getParent()+ "\\lowFlux\\" + spectraList.get(i).name);
								copyFile(spectraList.get(i).path, lowFlux);
				        		
				        		//deleting file from the lvl2 directory if flagged and removing from the current lists
				        		File file = new File(spectraList.get(i).path.getPath());
			        		    file.delete();
			        		    System.out.println("Flux is too low:" + spectraList.get(i).ADC1 + " Spectrum is removed successfully");		        		    
			        		    spectraList.remove(i);
			        		    //moving counter back to the current spectrum
			        		    i--;
			        		    continue;
							}
							
							
							//checking if the LT is higher than 1700
							if (spectraList.get(i).LT < 1700) {
								
								//moving  Spectra to the new lowLT subfolder
				        		new File(spectraList.get(i).path.getParent()+ "\\lowLT").mkdirs();
				        		File lowFlux = new File(spectraList.get(i).path.getParent()+ "\\lowLT\\" + spectraList.get(i).name);
								copyFile(spectraList.get(i).path, lowFlux);
				        		
				        		//deleting file from the lvl2 directory if flagged and removing from the current lists
				        		File file = new File(spectraList.get(i).path.getPath());
			        		    file.delete();
			        		    System.out.println("LT is too low:" + spectraList.get(i).LT + " Spectrum is removed successfully");		        		    
			        		    spectraList.remove(i);
			        		    //moving counter back to the current spectrum
			        		    i--;
			        		    continue;
							}
				        	
				        	//set edge of spectrum according to reference (if no edge is set yet)
				        	spectraList.get(i).calcEdge(RefSpec, ini.thres3, ini.thres4, ini.Edgeoffset);
				        	progress = (((double) i+1.0)/(spectraList.size())*100.0);
				        	System.out.println( (progress));
				        	progressBar.setValue((int) (progress));
				        	
				        	//flag spectra
				        	if(spectraList.get(i).edge > ini.Edgeoffset+ini.UpperFlagThres || spectraList.get(i).edge < ini.Edgeoffset-ini.LowerFlagThres) {
				        		//save Spectrum in new subfolder \flagged
				        		new File(spectraList.get(i).path.getParent()+ "\\flagged").mkdirs();
				        		File tmpFlagged = new File( spectraList.get(i).path.getParent()+ "\\flagged\\" + spectraList.get(i).name);
				        		copyFile(spectraList.get(i).path, tmpFlagged);

				        		//deleting file from the lvl2 directory if flagged and removing from the current lists
				        		File file = new File(spectraList.get(i).path.getPath());
			        		    file.delete();
			        		    System.out.println("Edge is out of the boundaries:" + spectraList.get(i).LT + " Spectrum is removed successfully");		        		    
			        		    spectraList.remove(i);
			        		    //moving counter back to the current spectrum
			        		    i--;
			        		    continue;
				        	}	
				        	
				        	
				        	bw.write(spectraList.get(i).datetime + "; " +
				        	spectraList.get(i).LT + "; " +
				        	spectraList.get(i).ADC1 + "; "+
				        	spectraList.get(i).ADC1StD + "; "+
				        	spectraList.get(i).T1 + "; "+
				        	spectraList.get(i).T1StD + "; "+
				        	spectraList.get(i).T2 + "; "+
				        	spectraList.get(i).T2StD + "; "+
				        	spectraList.get(i).T3 + "; "+
				        	spectraList.get(i).T3StD + "; "+
				        	//RN1 in the old Delphi program, counts above the noise threshold (TotalThreshold)
				        	spectraList.get(i).integrate(ini.thres1, 128) + "; "+			
				        	//RN2 in the old Delphi, counts inside threshold window (between TotalThres and WindowThreshold)
				        	spectraList.get(i).integrate(ini.thres1, ini.thres2) + "; "+		
				        	//RN3 in the old Delphi, counts above the edge
				        	spectraList.get(i).integrate(spectraList.get(i).edge, 128)+ "; "+		
				        	//RN4 in the old Delphi
				        	spectraList.get(i).edge + "; "+		
				        	spectraList.get(i).ADC2 + "; "+
				        	spectraList.get(i).ADC2StD + "; "+
				        	spectraList.get(i).ADC3 + "; "+
				        	spectraList.get(i).ADC3StD + "; "+
				        	spectraList.get(i).counter1 + "; "+
				        	spectraList.get(i).counter2 + "; "+
				        	spectraList.get(i).fluxslope+ "; "+
				        	spectraList.get(i).fluxoffset+ "; "+
				        	spectraList.get(i).ADC2Slope+ "; "+
				        	spectraList.get(i).ADC2Offset+ "; "+
				        	spectraList.get(i).ADC3Slope+ "; "+
				        	spectraList.get(i).ADC3Offset+ "; "+
				        	spectraList.get(i).Temp1Slope+ "; "+
				        	spectraList.get(i).Temp1Offset+ "; "+
				        	spectraList.get(i).Temp2Slope+ "; "+
				        	spectraList.get(i).Temp2Offset+ "; "+
				        	spectraList.get(i).Temp3Slope+ "; "+
				        	spectraList.get(i).Temp3Offset+ "; "+
				        	spectraList.get(i).Counter1Slope+ "; "+
				        	spectraList.get(i).Counter1Offset+ "; "+
				        	spectraList.get(i).Counter2Slope+ "; "+
				        	spectraList.get(i).Counter2Offset+ "; "+
				        	spectraList.get(i).monitor+ "; \r\n"
				        	);
				        }
				        bw.close();
				        fileOut.close();
				        progressBar.setString("");
				    	progressBar.setValue(0);
				        JOptionPane.showMessageDialog(null, "Extract file was successfully created!" , "Make Extract File (.txt)", JOptionPane.INFORMATION_MESSAGE);

					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					//user canceled operation
					return;				
				}
			}

		});
		
		
		//make activity file from multiple log files
		mntmNewMenuItem_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//who created the file
					String evaluator = (String) JOptionPane.showInputDialog(null,"Evaluated by:",
	                        "Creating ACT File",
	                        JOptionPane.PLAIN_MESSAGE, null, null, "Your name");
					if(evaluator == null || (evaluator != null && ("".equals(evaluator)))) {
						    return;
					}
					System.out.println("Evaluator: " + evaluator);
					//select extract file
					//Create a file chooser
			        final JFileChooser actDialog = new JFileChooser(ini.extractFileFolder);
			        
			        //name of the file chooser window
			        actDialog.setDialogTitle("Load Log file(-s) (.txt):");
			        //only show not hidden files
			        actDialog.setFileHidingEnabled(true);
			        //to select multiple files
			        actDialog.setMultiSelectionEnabled(true);
			        //In response to a button click:
			        int option = actDialog.showOpenDialog(null);
			        
			        File[] extFiles;
			        if(option == JFileChooser.APPROVE_OPTION) {
			        	extFiles = actDialog.getSelectedFiles();
			        	
			        	//creating one log file out of multiple
				        ArrayList<String> extlines = new ArrayList<String>();
			            if (extFiles != null && extFiles.length > 0){
			            	for (int x = 0; x < extFiles.length; x++) { 
			            	
			    				FileReader fileReader = new FileReader(extFiles[x]);
			    		        BufferedReader bufferedReader = new BufferedReader(fileReader);	    		        
			    		        String line = null;
			    		        //skipping first (header) line
			    		        line = bufferedReader.readLine();
			    		        while ((line = bufferedReader.readLine()) != null) {
			    		            extlines.add(line);
			    		            System.out.println(line);
			    		        }
			    		        bufferedReader.close();
			    		        System.out.println("successfully loaded extract file number " + (x + 1));			            		
			                }
			            }			            

		        		//sending first info to the progressBar
			            progressBar.setValue(0);
        	            progressBar.setString("Sorting extract file... [" + 0 + "%]");
			            
			            //sorting entries in the final extract file by the measurement time using insertion algorithm 
			            //creating SwingWorker to run the main task while GUI runs in Event Dispatch Thread and shows progress
			            class SortingExtractFile extends SwingWorker<Void, Void> {
					        
							//main task of the worker; executed in background thread		         
					        @Override
					        public Void doInBackground() {
					        	
					        	//initialize task properties
					        	isSortingExtractFileDone = false;
					            int progress;
					            
					            //sorting the combines extract file using the Insertion sorting algorithm
					            for (int i = 1; i < extlines.size(); i++) {	
					            	//stop if user canceled
					            	if (isSortingExtractFileDone) {
					            		break;
					            	}
					            	
					            	//calculating remaining progress and sending it to GUI
									progress = (int) ((((double) i+1.0)/(extlines.size()))*100.0);
									System.out.println(progress);
									setProgress(progress);
					            	
					                String current = extlines.get(i);
									LocalDateTime currentTime =  LocalDateTime.parse(current.split(";")[0], DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss"));				
									int j = i - 1;
									while(j >= 0 &&  currentTime.isBefore(LocalDateTime.parse(extlines.get(j).split(";")[0], DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss"))) ) {
										extlines.set(j+1,extlines.get(j));
									    j--;
									}
									extlines.set(j+1, current);					                
					            }
					            return null;
					        }
					        
					        //Executed in event dispatching thread after the task is done or if called upon	         
					        @Override
					        public void done() {
					        	isSortingExtractFileDone = true;
					            JOptionPane.getRootFrame().dispose();  
					        }
					    }	
						//starting the progress bar
			            progressBar.setString("Sorting extract file... [" + 0 + "%]");
						//calling new thread via SwingWorker to simultaneously change GUI and do loading
			            SortingExtractFile sortingTask = new SortingExtractFile();
			            sortingTask.addPropertyChangeListener(new PropertyChangeListener() {
			                @Override
			                public void propertyChange(PropertyChangeEvent evt) {
			        	        if ("progress".equals(evt.getPropertyName())) {
			        	            //int progress = (Integer) evt.getNewValue();
			        	            progressBar.setValue((Integer) evt.getNewValue());
			        	            progressBar.setString("Sorting extract file... [" + evt.getNewValue() + "%]");			        	            
			        	        } 
			        	    }
			            });
			            sortingTask.execute();	
						
						//user hit cancel -> aborting operations
						Object[] options = {"Cancel"};
					    int n = JOptionPane.showOptionDialog(null,
					                   "Wait until Log (extract) file being sorted by the entry date","Sorting entries...",
					                   JOptionPane.PLAIN_MESSAGE,
					                   JOptionPane.INFORMATION_MESSAGE,
					                   null,
					                   options,
					                   options[0]);
					    if (n == 0) {
					    	//stopping SwingWorker 
					    	sortingTask.done();
					    	Thread.sleep(150);
					    	progressBar.setValue(0);
					    	progressBar.setString("");
					    	return;
					    }
				        
					    //resetting progressBar
					    progressBar.setValue(0);
				    	progressBar.setString("");
					    
				        int dialogButton = JOptionPane.YES_NO_OPTION;
			        	int dialogResult = JOptionPane.showConfirmDialog (null, "Would you like to save combined and sorted Log file?" ,"Save new Log file?", dialogButton);
			        	if(dialogResult == JOptionPane.YES_OPTION){
			        		JFileChooser fileChooser = new JFileChooser(ini.extractFileFolder);
			        		//name of the file chooser window
			        		fileChooser.setDialogTitle("Save combined Log file (.txt) as:");
			        		
			        		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			        			File newExtFile = fileChooser.getSelectedFile();
			        			try {
				        			if (!(newExtFile.getName().endsWith(".txt"))) {
				        				newExtFile = new File(newExtFile+".txt");
				        			}
				        			System.out.println("" + newExtFile);
					        		newExtFile.createNewFile();
					      			FileOutputStream fileOut = new FileOutputStream(newExtFile);
					      			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOut));
					      		    double progress = 0;
					      		    progressBar.setValue((int) (progress));
					      		    progressBar.setStringPainted(true);
					      		    //write first line
					      		    bw.write("Date Time; Lifetime;ADC1; StdADC1; T1; StdT1;T2; StdT2;T3; StdT3;Rn1;Rn2;Rn3;Rn4;ADC2; StdADC2; ADC3; StdADC3; Counter1;Counter2;FluxSlope;FluxOffset;ADC2Slope;ADC2Offset;ADC3Slope;ADC3Offset;Temp1Slope;Temp1Offset;Temp2Slope;Temp2Offset;Temp3Slope;Temp3Offset;Counter1Slope;Counter1Offset;Counter2Slope;Counter2Offset;ID \r\n");
					      		    for (int j=0;j<extlines.size();j++) {
					      		    	bw.write(extlines.get(j) + "\r\n");
					      		    }
					      		        
					      		    bw.close();
					      		    fileOut.close();
			      				} catch (IOException e1) {
			      				  JOptionPane.showMessageDialog(null, "Could not create the extract file, maybe you have no writing permissions?" , "Continue evaluation", JOptionPane.INFORMATION_MESSAGE);
			      				  e1.printStackTrace();
			      				}
			        		  // save to file
			        		}
			        	}
				        
			        	JFileChooser fileChooser = new JFileChooser(ini.activityFileFolder);
		        		//name of the file chooser window
		        		fileChooser.setDialogTitle("Save activity file (.act) as:");
		        		
		        		//creating act file
						FileOutputStream fileOut;
		        		File actFile = null;
		        		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
		        			actFile = fileChooser.getSelectedFile();
		        			if (!(actFile.getName().endsWith(".act"))) {
		        				actFile = new File(actFile+".act");
		        			}
		        		} else {
		        			return;
		        		}
		        		
						String points = (String) JOptionPane.showInputDialog(null,"Choose 1-5:",
		                        "Number of points for derivative calculation",
		                        JOptionPane.PLAIN_MESSAGE, null, null, "1");
						
						if(points == null || (points != null && ("".equals(points)))) {
							    return;
						}
						try {
							if( Integer.parseInt(points)>5 || Integer.parseInt(points) < 0 || points==null) {
								JOptionPane.showMessageDialog(null, "No value between 1 and 5 selected, set to default=1"/*, JOptionPane.INFORMATION_MESSAGE*/);
								points="1";
							}
						} catch (Exception e3) {
							JOptionPane.showMessageDialog(null, "No value between 1 and 5 selected, set to default=1"/*, JOptionPane.INFORMATION_MESSAGE*/);
							points="1";
						}
						      
				        //checking if act file can be created
						if(extlines.size()<2) {
				        	JOptionPane.showMessageDialog(null, "Extract file is empty or has no values."/*, JOptionPane.INFORMATION_MESSAGE*/);
				        	return;
					    }
						
				        //open act File
						fileOut = new FileOutputStream(actFile);
				    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOut));
				        bw.write("222-Radon activities calculated with " + SoftwareVersion + "\r\n"+ "\r\n");
				        bw.write("Evaluated by: " + evaluator + " on "); 
				        bw.write(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))); 
				        bw.write("\r\n");
				        bw.write("Used parameters \r\n");
				        String Method="Stockburger";
				        bw.write("Method	    : " + Method + "\r\n");
				        bw.write("Source Files : ");
				        for (int k = 0; k<extFiles.length;k++) {
				        	bw.write(extFiles[k].getPath()+"; ");
				        }
				        bw.write("\r\n");
				        bw.write("Solid Angle : " + String.valueOf(ini.solidangle) + "\r\n");
				        bw.write("Disequil.   : " + String.valueOf(ini.disequilibrium) + "\r\n");
				        bw.write("Flux Offset : " + String.valueOf(ini.fluxoffset) + "\r\n");
				        bw.write("Flux Slope  : " + String.valueOf(ini.fluxslope) + "\r\n"+"\r\n");	
				        bw.write("Format: \r\n");
				        bw.write("Stoptime; Activity [Bq/m3]; Ac[dps]; Ac/dt; Total; Window; Edge; temp1[C]; temp2[C]; temp3[C]; Pressure[mbar]; LifeTime[sec]; Flux[m3/s]; ID \r\n");
				      
				        //split extlines to get rid of duplicates or  missing values
				        ArrayList<ArrayList<String>> splittedExtlines = new ArrayList<ArrayList<String>>();
				        ArrayList<ArrayList<String>> splittedActlines = new ArrayList<ArrayList<String>>();
				        ArrayList<String> tmpList = new ArrayList<String>();
		
				        //save positions where to split
				        // 0-> dont split; 1-> split; 2-> delete 
				        int[] flag = new int[extlines.size()];
				        flag[1] = 0;
				        tmpList.add(extlines.get(1));
				        
				        for (int i = 1; i< extlines.size(); i++) {
				        	
				        	LocalDateTime current =  LocalDateTime.parse(extlines.get(i).split(";")[0], DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss"));
				            LocalDateTime last = LocalDateTime.parse(extlines.get(i-1).split(";")[0], DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss"));
				            long difference = Duration.between(last,current).toMinutes();
				            
				            if (Integer.parseInt(extlines.get(i).split(";")[1].trim()) < 1700) {
				            	//if Spectrum LifeTime is smaller than 1700s delete ignore line				            	
				            	extlines.remove(i);
			    				i--;
			    				continue;
				            }
				            	else if(difference > 30 ) {
					        		//if (Datetime_last - Datetime_current) > 30min
					        		flag[i] = 1; //split here
					        		System.out.println("split, diff is " + difference + " min");
					        		splittedExtlines.add((ArrayList<String>) tmpList.clone());
					        		tmpList.clear();
					        		tmpList.add(extlines.get(i));
					        		System.out.println("new Array of extLines " + extlines.get(i));
					        	}
					        	else if(difference < 28.33 ) {
					        		//if (Datetime_last - Datetime_current) < 1min
					        		flag[i] = 2; //remove this
					        		System.out.println("remove " + difference);
					        		System.out.println("don't count this line (maybe duplicate) " + extlines.get(i));
					        		continue;
					        	}
					        	else {
					        		flag[i] = 0;
						        	tmpList.add(extlines.get(i));
					        		System.out.println("add " + extlines.get(i));
					        	}		        	
				        }
				        
				        splittedExtlines.add((ArrayList<String>) tmpList.clone());
		        		tmpList.clear();
		
		
				        //values calculation using the Stockburger method			        
				        System.out.println("Calculating Stockburger");
				        for(int x = 0; x < splittedExtlines.size(); x++) {	
				        	tmpList = (ArrayList<String>) calcStockburger(splittedExtlines.get(x), Integer.parseInt(points)).clone();
				        	if (tmpList.get(0) == "") {
				        		continue;
				        	}
				        	splittedActlines.add(tmpList);
				        	}
				        
				        //gather and fuse, if fill == true
						//should the values be filled up?
				        Boolean fill = false;
				        if(ini.fill == 1) fill = true;
				        if(!fill || splittedActlines.size() == 1) {
				        	System.out.println("Don't need to fill up");
				        	//just write the results into the file if no filler is given or only one block was created
					        for(int i=0; i<splittedActlines.size(); i++) {
					        	for(int j1 = 0; j1 < splittedActlines.get(i).size() ; j1++) {
					        		bw.write(splittedActlines.get(i).get(j1) + "\r\n");
					        	}
					        }
				        } else {
				        	//write the results into the file but every time a new block starts, fill it with the correct date and the filler
				        	System.out.println("Fill Up with " + ini.filler);
				        	for(int i = 0; i < splittedActlines.size() ; i++) {
				        		for(int k = 0; k < splittedActlines.get(i).size(); k++) {
					        		//System.out.println( i + " "+ k + " " + splittedActlines.get(i).get(k));
				        			bw.write(splittedActlines.get(i).get(k) + "\r\n");
				        		}
				        		try {
				        			//taking last line form the current peace and first line from the next piece
				        			//calculate time difference and number of entries to fill
				        			String last = splittedActlines.get(i).get(splittedActlines.get(i).size()-1);
				        			String next = splittedActlines.get(i+1).get(0);
				        			ArrayList<String> fillingStrings = getDateTimeBetween(last, next);
				        			for (int l = 0; l < fillingStrings.size(); l++) {
				        				bw.write(fillingStrings.get(l)+ ";" + ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + "\r\n");
				        			}
				        		} catch (Exception e2) {
				        			//could not access splittedActlines.get(i+1) -> filling done
				        			break;
				        		}
				        	}
				        }
				        bw.close();
				        JOptionPane.showMessageDialog(null, "Activity file was successfully created!" , "Make Activity File (.act)", JOptionPane.INFORMATION_MESSAGE);
			        } else if (option == JFileChooser.CANCEL_OPTION){
			        	System.out.println("User cancelled operation. No ACT file was created.");
			        	return;
			        }
				} catch (Exception e3) {
					e3.printStackTrace();
				}
				
			}
		});
		
		//create a continue button for the "continue evaluation" process
		JButton btnContinueRefSpectrum = new JButton("continue evaluation");
		btnContinueRefSpectrum.setBackground(new Color(50, 205, 50));
		btnContinueRefSpectrum.setToolTipText("click here to continue the automatic evaluation after setting the edge for the reference spectrum");
		btnContinueRefSpectrum.setBounds(10, 435, 145, 44);
		panel.add(btnContinueRefSpectrum);
		btnContinueRefSpectrum.setVisible(false);
		
		//create a continue button for the "continue evaluation" process
		JButton btnContinueFlagging = new JButton("continue evaluation");
		btnContinueFlagging.setBackground(new Color(50, 205, 50));
		btnContinueFlagging.setToolTipText("click here to continue the automatic evaluation after setting the edge for the flagged spectra");
		btnContinueFlagging.setBounds(10, 435, 145, 44);
		panel.add(btnContinueFlagging);
		btnContinueFlagging.setVisible(false);

		//continue evaluation
		mntmNewMenuItem_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RefSpec = null;
				continueEvaluation(progressBar, chartPanel, panel, btnContinueFlagging, btnContinueRefSpectrum);			
			}
		});
		
		//continue button for after manually flagging spectra
		btnContinueFlagging.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnContinueFlagging.setVisible(false);
				continueEvaluation2(btnContinueFlagging);
			}
		});
		
		//trying to pause the continue method til user have selected edge for the ref spectrum
		btnContinueRefSpectrum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnContinueRefSpectrum.setVisible(false);
				ini.saveNewEdgeoffsest(RefSpec.edge);
				continueEvaluation(progressBar, chartPanel, panel, btnContinueFlagging, btnContinueRefSpectrum);
			}
		});
		
		//get the live data from the monitor
		tglbtnLive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//start a thread that gets the data from the monitor
				//thread gets closed, when the live mode is toggled off

				if(!tglbtnLive.isSelected()) {
					//deactivate live mode
					TLiveMode.interrupt();
					tglbtnLive.setSelected(false);
					btnReadMemoryCard.setEnabled(true);
					mnNewMenu.setEnabled(true);
					mnNewMenu_1.setEnabled(true);
					mnNewMenu_2.setEnabled(true);
					edgeLeft.setEnabled(true);
					edgeRight.setEnabled(true);
					previousSpectrum.setEnabled(true);
					nextSpectrum.setEnabled(true);
					return;
				}
				
				if(tglbtnLive.isSelected()) {
					//activate live mode
					//connect to the IP entered in the textfield tfIP
					if(tfIP.getText().isEmpty()) {
						tglbtnLive.setSelected(false);
						JOptionPane.showMessageDialog(null, "Please enter an IP address, e.g. 129.206.22.122");
						return;
					}
					if(tfIP.getText().split("\\.").length != 4) {
						tglbtnLive.setSelected(false);
						JOptionPane.showMessageDialog(null, "Please enter a valid IP address.");
						return;
					}
					//disable all other buttons
					btnReadMemoryCard.setEnabled(false);
					mnNewMenu.setEnabled(false);
					mnNewMenu_1.setEnabled(false);
					mnNewMenu_2.setEnabled(false);
					edgeLeft.setEnabled(false);
					edgeRight.setEnabled(false);
					previousSpectrum.setEnabled(false);
					nextSpectrum.setEnabled(false);
					//open thread					
					TLiveMode = new Thread(new LiveModeThread(chartPanel, tfIP.getText(), tglbtnLive, ini.id));
					TLiveMode.start();	
				}
			}
		});
		
		
	}
	
	public void chooseSpectraDialog(ChartPanel chartPanel) {
		//Create a file chooser
        final JFileChooser fileDialog = new JFileChooser(ini.lvl0);
        
        //only show not hidden files
        fileDialog.setFileHidingEnabled(true);
        
        //name of the filechooser window
        fileDialog.setDialogTitle("Choose Spectra");
        
        String filenames = "";
        //to select mulitple files
        fileDialog.setMultiSelectionEnabled(true);
        //In response to a button click:
        int option = fileDialog.showOpenDialog(null);
        
        //defined globally
        spectraList.clear();
        if(option == JFileChooser.APPROVE_OPTION) {
        	
            File[] files = fileDialog.getSelectedFiles();
        	System.out.println("get selected files");
            if (files != null && files.length > 0){
            	for (int x = 0; x < files.length; x++) { 
                	filenames = filenames + "\n" + files[x].getName();
               		System.out.println("You have selected this file:\n" +files[x]);
               		//checking if the selected file is either ref spectrum or normal spectrum
               		if (!files[x].getName().contains(".ref")) {
               			if(!checkFilename(files[x].getName())) {
                   			continue;
                   		}               			
               		} else {
               			try {
							RefSpec = new Spectra(files[x].getName(),files[x]);
						} catch (Exception e) {
							e.printStackTrace();
						}
               		}
               		Spectra actualSpectrum;
               		System.out.println("add actual spectrum");
               		try {
               			actualSpectrum = new Spectra(files[x].getName(), files[x]);
               			//add actual spectrum to list
               			spectraList.add(actualSpectrum);
               		} catch (Exception e) {
               			System.out.println("Something went wrong, maybe the file is already opened?");
               			e.printStackTrace();
               		}
                }
            }
            //setting index to zero
            selectedSpecIdx = 0;
            //showing the first spectrum of the selection
        	tfEdge.setText(spectraList.get(0).showSpectra(chartPanel));
         } else if (option == JFileChooser.CANCEL_OPTION){
             System.out.println("User cancelled operation. No file was chosen.");  
         }
	}
	
	//helper function for copying files (e.g. flagged spectra into subfolder)
	private static void copyFile(File source, File dest) throws IOException {
		
		try {
			InputStream input = new FileInputStream(source);
			OutputStream output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
				output.flush();
			}
		
			input.close();
			output.close();
		} finally {
		}
	}
	
		
	public ArrayList<String> calcStockburger(ArrayList<String> extLines,  int points) {
		//calculation of the activity using the Stockburger method
		System.out.println("Calculating these lines : ");
		for ( int i =0; i< extLines.size(); i++) {
			System.out.println(extLines.get(i));
		}
		
		//load *.ini file
		if (ini._pathToIniFile == null) {
			ini = new iniFile();
			} 
		
		long[] timeDiffs = new long[extLines.size()]; //LT, total, window, Po212, timeDifference, Ac
		int[] LTs = new int[extLines.size()];
		int[] totals = new int[extLines.size()];
		int[] windows = new int[extLines.size()];
		int[] Po212s = new int[extLines.size()];
		int[] edges = new int[extLines.size()];
		
		double[] activities = new double[extLines.size()];
		double[] t1s = new double[extLines.size()];
		double[] t2s = new double[extLines.size()];
		double[] t3s = new double[extLines.size()];
		double[] pressures = new double[extLines.size()];
		double[] Acs = new double[extLines.size()];
		double[] act_ps = new double[extLines.size()];
		double[] fluxs = new double[extLines.size()];
		double[] dAcdts = new double[extLines.size()];
		double[] Acs_deriv = new double[points*2+1];
		double[] dt_deriv = new double[points*2+1];
		
		ArrayList<String> actlines = new ArrayList<String>();
		if(extLines.size()<3) {
			System.out.println("Extractfile had less than 3 lines.");
			actlines.add("");
			return actlines;
		}
		
		for(int i = 1; i < extLines.size(); i++) {
			//calculate time difference between two measurement points
			//if the difference is to small (<60) oder too large (>1800) there is a duplicate line or a missing one
			//-> delete that line or split the extLines up
			String DateTime1 = extLines.get(i-1).split(";")[0];
    		String DateTime2 =  extLines.get(i).split(";")[0];
    		DateFormat formatter = new SimpleDateFormat("dd.MM.yyy HH:mm:ss");
    		long diffInSeconds = 0;
			try {
				diffInSeconds = (formatter.parse(DateTime2).getTime() - formatter.parse(DateTime1).getTime())/1000;
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			timeDiffs[i] = diffInSeconds;
		}
		
		
		for(int i = 0; i < extLines.size(); i++) {
    		//Date Time; Lifetime;ADC1; StdADC1; T1; StdT1;T2; StdT2;T3; StdT3;Rn1;Rn2;Rn3;Rn4;ADC2; StdADC2; ADC3; StdADC3; Counter1;Counter2;FluxSlope;FluxOffset;ADC2Slope;ADC2Offset;ADC3Slope;ADC3Offset;Temp1Slope;Temp1Offset;Temp2Slope;Temp2Offset;Temp3Slope;Temp3Offset;Counter1Slope;Counter1Offset;Counter2Slope;Counter2Offset;ID
    		//get Date Time of String 04.07.2018 13:00:00
			//save every intermediate value in its own list
			
			//get LT, total, window, Po212, edge, temp1, temp2, temp3, pressure
			LTs[i] = Integer.parseInt(extLines.get(i).split(";")[1].replaceAll("\\s+",""));

			//RN1
			totals[i] = Integer.parseInt(extLines.get(i).split(";")[10].replaceAll("\\s+",""));

			//RN2
			windows[i] = Integer.parseInt(extLines.get(i).split(";")[11].replaceAll("\\s+",""));
			
			//RN3
			Po212s[i]= Integer.parseInt(extLines.get(i).split(";")[12].replaceAll("\\s+",""));
			
			//edge
			edges[i] = Integer.parseInt(extLines.get(i).split(";")[13].replaceAll("\\s+",""));
			
			//t1,t2,t3 in kelvin
			t1s[i] = Double.parseDouble(extLines.get(i).split(";")[4].replaceAll("\\s+","")) -273.2;
			t2s[i] = Double.parseDouble(extLines.get(i).split(";")[6].replaceAll("\\s+","")) -273.2;
			t3s[i] = Double.parseDouble(extLines.get(i).split(";")[8].replaceAll("\\s+","")) -273.2;
			
			//pressure -> fill with 0.0
			//not available currently
			pressures[i] = 0.0;
					
			//calculate Ac (activity on filter
			Acs[i] = 0.942 * (Double.valueOf(totals[i]) - Double.valueOf(windows[i]) - (Double.valueOf(Po212s[i]) / 0.4984646)) / Double.valueOf(LTs[i]);   //0.778 * 0.6407 = 0.4984646 
			
			//calculate flux in m^3/s
			fluxs[i] = Double.valueOf(extLines.get(i).split(";")[2].replaceAll("\\s+",""));
			//multiply by slope, add offset and convert to m^3/s
			fluxs[i] = ((ini.fluxslope * fluxs[i]) + ini.fluxoffset) / 3600000; 
			
			//calculate act_p
			act_ps[i] = ini.disequilibrium / (ini.solidangle * fluxs[i] * 4302);
		}
		
		for( int i = points; i < extLines.size(); i++) {
			//use intermediates to calculate the derivation (dAc/dt)
			//kick the first #points out, because there are not enough values before to calculate the derivation
			
			
			//create lists for saving the points, x = time Difference; y = Ac
			//and give them to LinearRegression(x[], y[])
			
			Acs_deriv = null;
			dt_deriv = null;
			Acs_deriv = new double[points*2+1];
			dt_deriv = new double[points*2+1];
			try {
				double n = 2 * points + 1; //3,5,7,...
				for (int x = 0; x < n; x++) {
					// -points, ... , 0 , ... points
					//test wether it is a spectra before, the current spectrum or one of the next spectra to avoid out of bounds exception
					//looks complicated but makes sense if you think about it
					if(x < (n/2-0.5)) {
						Acs_deriv[x] = Acs[(int) (i - ( n/2-0.5 - x))];
						dt_deriv[x] = timeDiffs[(int) (i - ( n/2-0.5 - x))];
					}
					if(x > n/2) {
						Acs_deriv[x] = Acs[(int) (x-(n/2-0.5)+i)];
						dt_deriv[x] = timeDiffs[(int) (x-(n/2-0.5)+i)];
					} 
					if (x == (n/2-0.5)){
						Acs_deriv[x] = Acs[i];
						dt_deriv[x] = timeDiffs[i];
					}
				}
			} catch (Exception OutOfBounds) {
				//not enough points to calculate derivation
				continue;
			}
			
			//until now, dt_deriv is filled with 1800, 1800, 1800,...
			//->fill it with 0, 1800, 3600, ... for the derivation
			double[] tmp = new double[Acs_deriv.length];
			for(int j = 0; j < Acs_deriv.length; j++) {
				if (j == 0) {
					tmp[j] = 0;
				} else {
					tmp[j] = tmp[j-1] + dt_deriv[j];
				}
			}
			dt_deriv = tmp;
			tmp = null;
			
			//calculate derivation
			LinearRegression LR = new LinearRegression(dt_deriv, Acs_deriv);
			dAcdts[i] = LR.slope();
			
			//calculate activity = act_p * (4302 * dAc/dt +Ac)
			activities[i] = act_ps[i] * ((4302 * dAcdts[i]) + Acs[i]);
			
			//gather results into a String line and save it in actlines[]
			//Stoptime,Activity [Bq/m3], Ac[dps],Ac/dt,Total, Window, Edge, temp1[C], temp2[C], temp3[C], Pressure[mbar], LifeTime[sec], Flux[m3/s], ID
			String actline = "";
			NumberFormat formatter = new DecimalFormat("#0.0000000000000000000");
			
			actline += extLines.get(i).split(";")[0] + "; " + activities[i] + "; " + Acs[i] + "; " 
					+ dAcdts[i] + "; " + totals[i] + "; " + windows[i] + "; " + edges[i] + "; " + t1s[i] + "; " 
					+ t2s[i] + "; " + t3s[i] + "; " + pressures[i] + "; " + LTs[i] + "; " + formatter.format(fluxs[i]).replaceAll(",", ".") + "; " 
				    +  extLines.get(i).split(";")[36] /*ID*/ ;
			actlines.add(actline);
			//System.out.println("adding " + actline);
		}
		return actlines;
	}

	public ArrayList<String> getDateTimeBetween ( String DT1, String DT2) throws ParseException{

		//helper function which gets the Date Time of a String (split it at ";" and convert to timeformat
		//and returns a list of Strings with timestamps between
		ArrayList<String> results = new ArrayList<String>();
		
		//time format used in the spactra
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss");
        LocalDateTime first =  LocalDateTime.parse(DT1.split(";")[0], timeFormat);
        LocalDateTime last = LocalDateTime.parse(DT2.split(";")[0], timeFormat);
        long diff = Duration.between(first,last).toMinutes()/30;        
        
        //failsafe for the time difference
		if (diff < 1) {
			return results;
		}
				
		for(int i = 1; i<diff; i++) {
			// i have to be casted to long type to avoid exceeding the integer range
			LocalDateTime inputDate = first.plusMinutes(i*30);
			results.add(inputDate.format(timeFormat).toString());
		}
		return results;
	}
	
	//getting all files in the folder, including all subfolders; recursive
	public void listOfAllFiles(String directoryName, ArrayList<File> files, ArrayList<String> fileNames) {
	    File directory = new File(directoryName);

	    // Get all files from a directory.
	    File[] fList = directory.listFiles();
	    if(fList != null) {
	    	for (File file : fList) { 
	        	//if file -> add to the lists
	            if (file.isFile()) {
	                files.add(file);
	                fileNames.add(file.getName());
	            //if directory use function recursively 
	            } else if (file.isDirectory()) {
	            	listOfAllFiles(file.getAbsolutePath(), files, fileNames);
	            }
	        }
	    }	        
	}
	
	//guides the user through the evaluation
	//this needed to be splitted into two functions, the first one gets the new spectra, copies them
	//and tries to set the edge, afterwards, if there are flagged spectra, the user can to flag them manually
	//which will then start part2 of the evaluation
	public void continueEvaluation(JProgressBar progressBar, ChartPanel chartPanel, JPanel panel, JButton btnContinue, JButton btnWaitRefSpectrum) {
		//Search where the last evaluation ended and start a new one
		//expects lvl0, lvl2 and lvl1 directories
		if (ini._pathToIniFile == null) {
			ini = new iniFile();
			} 
		
		System.out.println("setting the progress bar");
		progressBar.setStringPainted(true);
		progressBar.setString("gathering spectra from lvl0 and lvl2");
		progressBar.setValue(0);
		
		//extracting paths from ini file
		String lvl0=  ini.lvl0; // i.g. "C:\\Users\\Alessandro\\eclipse-workspace\\AutoRnLog\\lvl0\\";
		String lvl2=  ini.lvl2; 
		
		//creating folder variables
		File lvl0Dir = new File(lvl0);
		File lvl2Dir = new File(lvl2);

		//checking if the raw data folder exists; if no, there re no spectra to evaluate -> return
		if (lvl0Dir.exists()) {
			System.out.println(lvl0Dir + " exists");
		} else {
			JOptionPane.showMessageDialog(null, lvl0Dir + " does not exist. Please give the valid path to the raw data!", "Continue evaluation", JOptionPane.ERROR_MESSAGE);
			progressBar.setString("");
			progressBar.setValue(0);
			return;
		}
		
		//checking if the lvl2 folder exists and creating if not
		if (lvl2Dir.exists()) {
			System.out.println(lvl2Dir + " exists");
		} else {
			boolean folderCreated = lvl2Dir.mkdirs();
			if (folderCreated) {
				System.out.println(lvl2Dir + " was successfully created");
			} else {
				JOptionPane.showMessageDialog(null, lvl2Dir + " cannot be created. Do you have writing permission?", "Continue evaluation", JOptionPane.ERROR_MESSAGE);
				progressBar.setString("");
				progressBar.setValue(0);
				return;
			}
		}
		
		//array of choices for the swing worker
		Object[] options = {"Cancel"};
		
		//if it is the rerun due to the creation of the ref spec -> skip this part all together
		//comparing file names lvl0 and lvl2 folders
		//all names are compared, as files to evaluate the complement set lvl0\lvl2 is used
		if (!isRefSpecRun) {
			//reseting RefSpec
			RefSpec = null;
	        //creating SwingWorker to run the main task while GUI runs in Event Dispatch Thread and shows progress
			class LoadingFiles extends SwingWorker<Void, Void> {
		        
				//main task of the worker; executed in background thread		         
		        @Override
		        public Void doInBackground() {
		        	
		        	//initialize task properties
		        	isLoadingFilesDone = false;
		            int progress;
		            
		            //loading lists of ALL files and filenames in the lvl1 dir
			        ArrayList<File> lvl2Files = new ArrayList<File>();
			        ArrayList<String> lvl2FileNames = new ArrayList<String>();
			        listOfAllFiles(ini.lvl2, lvl2Files, lvl2FileNames);
			        
			        //loading lists of ALL files and filenames in the lvl0 dir
			        ArrayList<File> lvl0Files = new ArrayList<File>();
			        ArrayList<String> lvl0FileNames = new ArrayList<String>();
			        listOfAllFiles(ini.lvl0, lvl0Files, lvl0FileNames);
			        
			        //removing all entries from the lvl0 filename list that already exist in lvl2 dir
			        //it is assumed that these files are already evaluated
			        lvl0FileNames.removeAll(lvl2FileNames);
		            
		            //cycling over all addition (new) files in lvl0 folder
			        for (int i = 0; i < lvl0FileNames.size(); i++) {
			        	if (isLoadingFilesDone) {
		            		break;
		            	}
			        	File currentFile = new File(lvl0+lvl0FileNames.get(i));
			        	System.out.println(lvl0+lvl0FileNames.get(i));
						//check if the file is a spectra
						if (currentFile.isFile() && checkFilename(lvl0FileNames.get(i))) {
							tempFileList.add(currentFile);
						}
						
						//calculating remaining progress and sending it to GUI
						progress = (int) ((((double) i+1.0)/(lvl0FileNames.size()))*100.0);
						System.out.println(progress);
						setProgress(progress);						
						
						//search for reference spectrum
						if (currentFile.isFile() && lvl0FileNames.get(i).contains(".ref")) {
							try {
								System.out.println("Reference spectrum found: " + currentFile);
								RefSpec = new Spectra(lvl0FileNames.get(i), currentFile);
							} catch (Exception e) {
								System.out.println("could not load reference spectrum");
								JOptionPane.showMessageDialog(null, "The reference spectrum found in the lvl0 directory is brocken. Please provide a valid one." , "Continue evaluation", JOptionPane.INFORMATION_MESSAGE);
								e.printStackTrace();
							}
						}
					}
		            return null;
		        }
		        
		        //Executed in event dispatching thread after the task is done or if called upon	         
		        @Override
		        public void done() {
		            isLoadingFilesDone = true;
		            JOptionPane.getRootFrame().dispose();  
		        }
		    }	
			
			//calling new thread via SwingWorker to simultaneously change GUI and do loading
			LoadingFiles loadingTask = new LoadingFiles();
			loadingTask.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
        	        if ("progress".equals(evt.getPropertyName())) {
        	            //int progress = (Integer) evt.getNewValue();
        	            progressBar.setValue((Integer) evt.getNewValue());
        	            progressBar.setString("Loading file information... [" + evt.getNewValue() + "%]");			        	            
        	        } 
        	    }
            });
			loadingTask.execute();	
			
			//user hit cancel -> aborting operations
		    int n = JOptionPane.showOptionDialog(null,
		                   "Wait until all files are loaded","Loading files...",
		                   JOptionPane.PLAIN_MESSAGE,
		                   JOptionPane.INFORMATION_MESSAGE,
		                   null,
		                   options,
		                   options[0]);
		    if (n == 0) {
		    	//stopping SwingWorker 
		    	loadingTask.done();
		    	try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    	tempFileList.clear();
		    	progressBar.setValue(0);
		    	progressBar.setString("");
		    	return;
		    }
		    
		    //checking if the correct iniFile is used
		    if (RefSpec!=null) {
		    	boolean rightIniFile = RefSpec.monitor.equals(ini.id);	
		    	System.out.println(rightIniFile);
				if (!rightIniFile) {
					//exiting the evaluation procedure       		
	        		progressBar.setString("");
	    			progressBar.setValue(0);
	    			tempFileList.clear();
	    			JOptionPane.showMessageDialog(null, "Wrong ini file is loaded! Please provide the correct one!" , "Continue evaluation", JOptionPane.ERROR_MESSAGE);
	    			return;
				}
		    }
			
		    
			//getting the raw not yet evaluated spectra
	        toEvaluate = (ArrayList<File>) tempFileList.clone();	
			//clearing global variable for the next run
			tempFileList.clear();		
			
			//return if all files have low flow or are broken/empty spectra
			if (toEvaluate.size() == 0) {    	
				
				//no new files found -> jumping to extract file calculation
				continueEvaluation2(btnContinue);
				progressBar.setString("");
				progressBar.setValue(0);
				//reseting the refspec indicator
			    isRefSpecRun = false;
				return;    				
			}
			
			//double check if the reference spectrum was found
			if (RefSpec == null) {
				//setting the progress bar
				progressBar.setString("");
				progressBar.setValue(0);
				//no reference spectrum found
				//asking if user want to create ref spec automatically from the raw data
				int dialogButton = JOptionPane.YES_NO_OPTION;
	        	int dialogResult = JOptionPane.showConfirmDialog (null, "No reference spectrum found. Would you like to create a new reference spectrum from the raw data (lvl0 directory)?" ,"No reference spectrum was found!", dialogButton);
	        	if(dialogResult == JOptionPane.YES_OPTION){
	        		
	        		//changing the indicator for the refspec run
	    			isRefSpecRun = true;
	        	} else {     		
	        		
	        		//exiting the evaluation procedure       		
	        		progressBar.setString("");
	    			progressBar.setValue(0);
	    			JOptionPane.showMessageDialog(null, "Please create reference spectrum in the raw data folder and run the calculation again." , "Continue evaluation", JOptionPane.INFORMATION_MESSAGE);
	    			return;
	        	}
			}
			
			//creating SwingWorker to run the main task while GUI runs in Event Dispatch Thread and shows progress
			class CopyingFiles extends SwingWorker<Void, Void> {
		        
				//main task of the worker; executed in background thread		         
		        @Override
		        public Void doInBackground() {
		        	
		        	//initialize task properties
		        	isCopyingFilesDone = false;
		            int progress;
		            
		    		//copy the files that need to be evaluated into lvl2 and adding to spectra list to evaluate
		    		spectraList.clear();
		    		@SuppressWarnings("unused")
					boolean previousFlagged = false;
		    		for(int i=0; i<toEvaluate.size(); i++) {
		    			if (isCopyingFilesDone) {
		            		break;
		            	}
		    			
		    			//calculating remaining progress and sending it to GUI
						progress = (int) ((((double) i+1.0)/(toEvaluate.size()))*100.0);
						System.out.println(progress);
						setProgress(progress);
						
		    			
		    			try {
		    				System.out.println("copy " + toEvaluate.get(i) + " to " +  lvl2Dir + "\\" + toEvaluate.get(i).getName());
		    				copyFile(toEvaluate.get(i), new File(lvl2Dir + "\\" + toEvaluate.get(i).getName()));
		    				//dont flag the lvl0 spectra
		    				toEvaluate.set(i, new File(lvl2Dir + "\\" + toEvaluate.get(i).getName()));
		    				spectraList.add(new Spectra(toEvaluate.get(i).getName(), toEvaluate.get(i)));
		    				
		    				
		    				//moving broken spectra to new folder
		    				if (spectraList.get(i).linesCount < 2) {
		    					System.out.print(toEvaluate.get(i).getName() + " is empty file. Trying to remove.");
		    					//if the file is not empty -> move to the "broken Spectra" subfolder
			    				//if it is empty -> just delete it
		    					new File(spectraList.get(i).path.getParent()+ "\\empty").mkdirs();
		    					copyFile(toEvaluate.get(i), new File(lvl2Dir + "\\empty\\"+ toEvaluate.get(i).getName()));			
								toEvaluate.get(i).delete();
			    				toEvaluate.remove(i);
			    				spectraList.remove(i);
			    				i--;
			    				previousFlagged = true;
			    				continue;
		    				}
		    				
		    				//moving broken spectra to new folder
		    				if (spectraList.get(i).linesCount < 135) {
		    					System.out.print(toEvaluate.get(i).getName() + " is broken spectrum. Trying to remove.");
		    					//if the file is not empty -> move to the "broken Spectra" subfolder
			    				//if it is empty -> just delete it
		    					new File(spectraList.get(i).path.getParent()+ "\\broken").mkdirs();
		    					copyFile(toEvaluate.get(i), new File(lvl2Dir + "\\broken\\"+ toEvaluate.get(i).getName()));			
								toEvaluate.get(i).delete();
			    				toEvaluate.remove(i);
			    				spectraList.remove(i);
			    				i--;
			    				previousFlagged = true;
			    				continue;
		    				}
		    				
							//checking if the flux is higher than the flux threshold
							if (spectraList.get(i).ADC1 < ini.fluxthreshold) {
								
								//moving  Spectra to the new lowFlux subfolder
				        		new File(spectraList.get(i).path.getParent()+ "\\lowFlux").mkdirs();
				        		File lowFlux = new File(spectraList.get(i).path.getParent()+ "\\lowFlux\\" + spectraList.get(i).name);
								copyFile(spectraList.get(i).path, lowFlux);
				        		
				        		//deleting file from the lvl2 directory if flagged and removing from the current lists
				        		File file = new File(spectraList.get(i).path.getPath());
			        		    file.delete();
			        		    System.out.println("Flux is too low:" + spectraList.get(i).ADC1 + " Spectrum is removed successfully");		        		    
			        		    spectraList.remove(i);
			        		    toEvaluate.remove(i);
			        		    //moving counter back to the current spectrum
			        		    i--;
			        		    previousFlagged = true;
			        		    continue;
							}
							
							
							//checking if the LT is higher than 1700
							if (spectraList.get(i).LT < 1700) {
								
								//moving  Spectra to the new lowLT subfolder
				        		new File(spectraList.get(i).path.getParent()+ "\\lowLT").mkdirs();
				        		File lowFlux = new File(spectraList.get(i).path.getParent()+ "\\lowLT\\" + spectraList.get(i).name);
								copyFile(spectraList.get(i).path, lowFlux);
				        		
				        		//deleting file from the lvl2 directory if flagged and removing from the current lists
				        		File file = new File(spectraList.get(i).path.getPath());
			        		    file.delete();
			        		    System.out.println("LT is too low:" + spectraList.get(i).LT + " Spectrum is removed successfully");		        		    
			        		    spectraList.remove(i);
			        		    toEvaluate.remove(i);
			        		    //moving counter back to the current spectrum
			        		    i--;
			        		    previousFlagged = true;
			        		    continue;
							}
							
							previousFlagged = false;
		    				
		    			} catch (IOException e) {
		    				System.out.println("Could not copy file into lvl2");
		    				JOptionPane.showMessageDialog(null, "Could not copy the evaluated file into the lvl2 folder. Maybe you have no writing permissions?"/*, JOptionPane.INFORMATION_MESSAGE*/);
		    				e.printStackTrace();
		    				break;
		    			} catch (Exception e2) {
		    				e2.printStackTrace();
		    			}
		    		}
		            return null;
		        }
		        
		        //Executed in event dispatching thread after the task is done or if called upon	         
		        @Override
		        public void done() {
		            isCopyingFilesDone = true;
		            JOptionPane.getRootFrame().dispose();  
		        }
		    }	
			
			//calling new thread via SwingWorker to simultaneously change GUI and do loading
			CopyingFiles copyingTask = new CopyingFiles();
			copyingTask.addPropertyChangeListener(new PropertyChangeListener() {
	            @Override
	            public void propertyChange(PropertyChangeEvent evt) {
	    	        if ("progress".equals(evt.getPropertyName())) {
	    	            //int progress = (Integer) evt.getNewValue();
	    	            progressBar.setValue((Integer) evt.getNewValue());
	    	            progressBar.setString("Copying files to lvl2 folder... [" + evt.getNewValue() + "%]");			        	            
	    	        } 
	    	    }
	        });
			copyingTask.execute();		
			
			//user hit cancel -> aborting operations
		    n = JOptionPane.showOptionDialog(null,
		                   "Wait until all files are copied","copying files...",
		                   JOptionPane.PLAIN_MESSAGE,
		                   JOptionPane.INFORMATION_MESSAGE,
		                   null,
		                   options,
		                   options[0]);
		    if (n == 0) {
		    	//stopping SwingWorker 
		    	copyingTask.done();
		    	try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    	progressBar.setValue(0);
		    	progressBar.setString("");
		    	//reseting the refspec indicator
			    isRefSpecRun = false;
		    	return;
		    }
		    
		    //return if all files have low flow or are broken/empty spectra
			if (spectraList.size() == 0) {    				
				JOptionPane.showMessageDialog(null, "No suitable spectra for the reference were found." , "Reference spectrum", JOptionPane.ERROR_MESSAGE);
				progressBar.setString("");
				progressBar.setValue(0);
				//reseting the refspec indicator
			    isRefSpecRun = false;
				return;    				
			}   
			
			//creating new ref spec if none was found in the lvl0 dir
		    //using only the checked files from the copying task
			if (RefSpec == null) {			
				try {
					RefSpec = new Spectra(spectraList, ini);
					suitableSpectra = (ArrayList<Spectra>) spectraList.clone();
					spectraList.clear();
					spectraList.add(RefSpec);
					//setting index to zero
		            selectedSpecIdx = 0;
					tfEdge.setText(RefSpec.showSpectra(chartPanel));
					btnWaitRefSpectrum.setVisible(true);
					return;
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Could not create temporary reference specterum. Maybe you have no writing permissions?", "Create Reference Spectrum", JOptionPane.INFORMATION_MESSAGE);
					e1.printStackTrace();
				}
			}
		}
		
		//////////////////////////////////////////////////////
		//setting Po-edge in the radon spectra
		//////////////////////////////////////////////////////
		
		//checking if the correct iniFile is used
		System.out.println(RefSpec.monitor);
	    boolean rightIniFile = RefSpec.monitor.equals(ini.id);	
    	
		if (!rightIniFile) {
			//exiting the evaluation procedure       		
    		progressBar.setString("");
			progressBar.setValue(0);
			tempFileList.clear();
			JOptionPane.showMessageDialog(null, "Wrong ini file is loaded! Please provide the correct one!" , "Continue evaluation", JOptionPane.ERROR_MESSAGE);
			return;
		}
	    
	    
		//getting spectra from the saving container if it is ref spec run
		if (isRefSpecRun) {
			spectraList.clear();
			spectraList = (ArrayList<Spectra>) suitableSpectra.clone();
			suitableSpectra.clear();
		}
		
		
		try {	        
	        //creating SwingWorker to run the main task while GUI runs in Event Dispatch Thread and shows progress
			class SettingEdge extends SwingWorker<Void, Void> {
		        
				//main task of the worker; executed in background thread		         
		        @Override
		        public Void doInBackground() {
		        	
		        	//initialize task properties
		        	isSettingFilesDone = false;
		            int progress;
		            int refSpecEdge = RefSpec.edge;
		    		//loop though the selected spectra, see if they need to be flagged	       
			        for(int i=0; i<spectraList.size(); i++) {
			        	if (isSettingFilesDone) {
		            		break;
		            	}
			        	
			        	//calculating remaining progress and sending it to GUI
						progress = (int) ((((double) i+1.0)/(spectraList.size()))*100.0);
						setProgress(progress);						
						
			        	//set edge of spectrum according to reference (if no edge is set yet)
			        	try {
							spectraList.get(i).calcEdge(RefSpec, ini.thres3, ini.thres4, ini.Edgeoffset);
						} catch (IOException e) {
							e.printStackTrace();
						}
			        	spectraList.get(i).showSpectra(chartPanel);
			        	//flag spectra
			        	if(spectraList.get(i).edge > ini.Edgeoffset+ini.UpperFlagThres || spectraList.get(i).edge < ini.Edgeoffset-ini.LowerFlagThres) {
			        		//for continue evaluation: remember these spectra and ask user to set edge manually
			        		
			        		//setting edge to the value of the refSpec
			        		try {
								spectraList.get(i).setEdge(refSpecEdge);
							} catch (IOException e) {
								e.printStackTrace();
							}
			        		//add actual spectrum to list
			        		flagged.add(spectraList.get(i));  
			        		//add index of the flagged file to array
			        		flaggedIdx.add(i);
			        	}
			        }
		            return null;
		        }
		        
		        //Executed in event dispatching thread after the task is done or if called upon	         
		        @Override
		        public void done() {
		        	isSettingFilesDone = true;
		            JOptionPane.getRootFrame().dispose();  
		        }
		    }	
			
			//calling new thread via SwingWorker to simultaneously change GUI and do loading
			SettingEdge settingTask = new SettingEdge();
			settingTask.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
        	        if ("progress".equals(evt.getPropertyName())) {
        	            //int progress = (Integer) evt.getNewValue();
        	            progressBar.setValue((Integer) evt.getNewValue());
        	            progressBar.setString("Setting Po edge... [" + evt.getNewValue() + "%]");			        	            
        	        } 
        	    }
            });
			settingTask.execute();
			
			//user hit cancel -> aborting operations
		    int m = JOptionPane.showOptionDialog(null,
		                   "Wait until Po edge is set...","setting edge...",
		                   JOptionPane.PLAIN_MESSAGE,
		                   JOptionPane.INFORMATION_MESSAGE,
		                   null,
		                   options,
		                   options[0]);
		    if (m == 0) {
		    	//stopping SwingWorker 
		    	settingTask.done();
		    	try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    	progressBar.setValue(0);
		    	progressBar.setString("");
		    	//reseting the refspec indicator
			    isRefSpecRun = false;
		    	return;
		    }
	        
		    //reseting the refspec indicator
		    isRefSpecRun = false;
	        //removing flagged spectra from the spectra list
		    System.out.println(flaggedIdx.size() + " size of the flagged");
    		for (int i = flaggedIdx.size() - 1; i >= 0; i--) {
    			spectraList.remove(flaggedIdx.get(i).intValue());
    			System.out.println("removing flagged spectrum at index " + flaggedIdx.get(i));
    		}
	        
    		//changing flaggedIdx reference to spectraList array WITHOUT flagged spectra
    		for (int i = 0; i < flaggedIdx.size(); i++) {
    			flaggedIdx.set(i, flaggedIdx.get(i)-i);
    		}
    		
	        //ask user to set the edge on flagged spectra again manually
	        if (flagged.size()>0) {
	        	int dialogButton = JOptionPane.YES_NO_OPTION;
	        	int dialogResult = JOptionPane.showConfirmDialog (null, flagged.size() + " spectra have been flagged. Do you want to flag them manually?" ,"Continue evaluation", dialogButton);
	        	if(dialogResult == JOptionPane.NO_OPTION){
	        		
	        		//moving  Spectra to the new flagged subfolder
	        		new File(flagged.get(0).path.getParent()+ "\\flagged").mkdirs();
	        		
	        		for(int i=0; i<flagged.size(); i++) {
	        			
		        		File tmpFlagged = new File(flagged.get(i).path.getParent()+ "\\flagged\\" + flagged.get(i).name);
		        		copyFile(flagged.get(i).path, tmpFlagged);
		        		System.out.println("copied " + flagged.get(i).path.getPath() + " to " + tmpFlagged.getPath() );
		        		
		        		//deleting file from the lvl2 directory if flagged
		        		File file = new File(flagged.get(i).path.getPath());
	        		    file.delete(); 
	        		    System.out.println("File moved successfully"); 
					}
	        		
	        		progressBar.setValue(0);
	        		progressBar.setString("");
	        		continueEvaluation2(btnContinue);
	        		return;
	        	} else {
	        		//user wants to flag spectra himself
	        		btnContinue.setVisible(true);
	        		tmpList = (ArrayList<Spectra>) spectraList.clone();
	        		spectraList.clear();
	        		spectraList = (ArrayList<Spectra>) flagged.clone();
	        		spectraList.get(0).showSpectra(chartPanel);
	        		return;
	        	}
	        } 
	        
	        if (flagged.size()==0) {	        	
	        	continueEvaluation2(btnContinue);
	        	return;
	        }
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void continueEvaluation2(JButton btnContinue) {
		//hiding continue button 
		btnContinue.setVisible(false);
		//reseting the refspec indicator
	    isRefSpecRun = false;
		FileOutputStream fileOut;
		try {
			
			//clear variables from previous run
			tmpList.clear();
			spectraList.clear();
			
			//variable to check if all spectra have the edge set
			boolean spectraWithNoEdge = false;
			
			//Get all files from the lvl2 directory
			//extract file will be calculated for all files in lvl2 (except for subfolders)
			File lvl2Dir = new File(ini.lvl2);
		    File[] fList = lvl2Dir.listFiles();
		    if(fList != null) {
		    	for (File file : fList) { 
		    		
		    		//if spectrum -> add to the list
		    		if (file.isFile() && checkFilename(file.getName())) {
		    			try {
							spectraList.add(new Spectra(file.getName(), file));
							
							//checking if Po edge is set, if no -> skip this spectrum
							if (spectraList.get(spectraList.size() - 1).edge == -1) {
								System.out.print(spectraList.get(spectraList.size() - 1) + " has no edge. Trying to remove.");
								//if the file is not empty -> move to the "broken Spectra" subfolder
			    				//if it is empty -> just delete it
								new File(file.getParent()+ "\\noEdge").mkdirs();
								copyFile(file, new File(file.getParent()+ "\\noEdge\\"+ file.getName()));		
			    				spectraList.remove(spectraList.size() - 1);
								file.delete();
			    				
								spectraWithNoEdge = true;
								continue;
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
		        }
		    }	
		    
		  //message to user that there were spectra with no edge
			if (spectraWithNoEdge) {
				JOptionPane.showMessageDialog(null, "There were some spectra with no Po-edge in lvl2 folder!\n Please set Po-edge if you want them to be considered for the evaluation.", "Continue evaluation", JOptionPane.INFORMATION_MESSAGE);	
			}
			
			//if there are no suitable spectra in lvl2 folder, exit evaluation
			if (spectraList.size() == 0) {
				JOptionPane.showMessageDialog(null, "There are no suitable spectra in lvl2 folder for the further evaluation.", "Continue evaluation", JOptionPane.ERROR_MESSAGE);
				progressBar.setString("");
				progressBar.setValue(0);
				
				//clear the flagged arrays for the next iteration
				flaggedIdx.clear();
				flagged.clear();
				tmpList.clear();				
				return;
			}
		    
		    //creating directories for the extract and activity files
			new File(ini.extractFileFolder).mkdirs();
			new File(ini.activityFileFolder).mkdirs();
			
			//name convention for extract and activity files
			String prefixExtract = lvl2Dir.getName();
			String prefixActivity = lvl2Dir.getName();
			
			//creating new activity and extract files
			extract = new File(ini.extractFileFolder + "\\" + prefixExtract + ".txt");
			activity = new File(ini.activityFileFolder + "\\" + prefixActivity + ".act");
			
			//checking if such files already exist
			//cycle until free name is found
			int count = 1;
			while (extract.exists()) {
				extract = new File(ini.extractFileFolder + "\\" + prefixExtract + "_" + count + ".txt");
				count++;
			}
			
			count = 1;
			while (activity.exists()) {
				activity = new File(ini.activityFileFolder + "\\" + prefixActivity + "_" + count + ".act");
				count++;
			}
			
			//creating extract file header
			FileOutputStream fileOutHeader;
			try {
			fileOutHeader = new FileOutputStream(extract);
			BufferedWriter bwHeader = new BufferedWriter(new OutputStreamWriter(fileOutHeader));
			
			//write first line
			bwHeader.write("Date Time; Lifetime;ADC1; StdADC1; T1; StdT1;T2; StdT2;T3; StdT3;Rn1;Rn2;Rn3;Rn4;ADC2; StdADC2; ADC3; StdADC3; Counter1;Counter2;FluxSlope;FluxOffset;ADC2Slope;ADC2Offset;ADC3Slope;ADC3Offset;Temp1Slope;Temp1Offset;Temp2Slope;Temp2Offset;Temp3Slope;Temp3Offset;Counter1Slope;Counter1Offset;Counter2Slope;Counter2Offset;ID \r\n");
			bwHeader.close();
			fileOutHeader.close();
			} catch (IOException e2) {
			e2.printStackTrace();
			}
		    		    
			//extracting information from spectra and adding it to string array
			ArrayList<String> extlines = new ArrayList<String>();
			
			for (int i=0; i<spectraList.size(); i++) {
				
				extlines.add(spectraList.get(i).datetime + "; " +
				        spectraList.get(i).LT + "; " +
				        spectraList.get(i).ADC1 + "; "+
				        spectraList.get(i).ADC1StD + "; "+
				        spectraList.get(i).T1 + "; "+
				        spectraList.get(i).T1StD + "; "+
				        spectraList.get(i).T2 + "; "+
				        spectraList.get(i).T2StD + "; "+
				        spectraList.get(i).T3 + "; "+
				        spectraList.get(i).T3StD + "; "+
				       	//RN1 in the old Delphi program, counts above the noise threshold (TotalThreshold)
				       	spectraList.get(i).integrate(ini.thres1, 128) + "; "+			
				       	//RN2 in the old Delphi, counts inside threshold window (between TotalThres and WindowThreshold)
				       	spectraList.get(i).integrate(ini.thres1, ini.thres2) + "; "+		
				       	//RN3 in the old Delphi, counts above the edge
			        	spectraList.get(i).integrate(spectraList.get(i).edge, 128)+ "; "+		
				       	//RN4 in the old Delphi
				       	spectraList.get(i).edge + "; "+		
				       	spectraList.get(i).ADC2 + "; "+
				       	spectraList.get(i).ADC2StD + "; "+
				       	spectraList.get(i).ADC3 + "; "+
			        	spectraList.get(i).ADC3StD + "; "+
				       	spectraList.get(i).counter1 + "; "+
				       	spectraList.get(i).counter2 + "; "+
				       	spectraList.get(i).fluxslope+ "; "+
				        spectraList.get(i).fluxoffset+ "; "+
				        spectraList.get(i).ADC2Slope+ "; "+
				        spectraList.get(i).ADC2Offset+ "; "+
				        spectraList.get(i).ADC3Slope+ "; "+
				        spectraList.get(i).ADC3Offset+ "; "+
				        spectraList.get(i).Temp1Slope+ "; "+
				        spectraList.get(i).Temp1Offset+ "; "+
				        spectraList.get(i).Temp2Slope+ "; "+
				        spectraList.get(i).Temp2Offset+ "; "+
				        spectraList.get(i).Temp3Slope+ "; "+
				        spectraList.get(i).Temp3Offset+ "; "+
				        spectraList.get(i).Counter1Slope+ "; "+
				        spectraList.get(i).Counter1Offset+ "; "+
				        spectraList.get(i).Counter2Slope+ "; "+
				        spectraList.get(i).Counter2Offset+ "; "+
				        spectraList.get(i).monitor+ "; \r\n");
			}
			
			//sorting entries in the final extract file by the measurement time using insertion algorithm 
            for (int i = 0; i<extlines.size(); i++) { 
	            String current = extlines.get(i);
				LocalDateTime currentTime =  LocalDateTime.parse(current.split(";")[0], DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss"));	

				int j = i - 1;
				while(j >= 0 &&  currentTime.isBefore(LocalDateTime.parse(extlines.get(j).split(";")[0], DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss"))) ) {
					extlines.set(j+1,extlines.get(j));
				    j--;
				}
				extlines.set(j+1, current);	
            }
			
		    fileOut = new FileOutputStream(extract, true);
			//--------------------------------------^^^^ means append new line, don't override old data
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOut));
			for (int i=0; i<extlines.size(); i++) {
				bw.write(extlines.get(i));				
			}
			bw.close();
		    fileOut.close();
			/////////////////////////////////////////////
			//create activity file
			////////////////////////////////////////////
			
			//who created the file
			progressBar.setString("creating activity file");
			String evaluator = (String) JOptionPane.showInputDialog(null,"Last evaluated by:",
			        "Creating ACT File",
			        JOptionPane.PLAIN_MESSAGE, null, null, "Your name");
			
			if(evaluator == null || (evaluator != null && ("".equals(evaluator)))) {
				progressBar.setValue(0);
				progressBar.setString("");
				return;
			}
			System.out.println("Evaluator: " + evaluator);
			
			//number of previous and next spectra that are considered for the evaluation
			//hardcoded value
			String points = "1";			
			
			try {
			    //writing activity file
				fileOut = new FileOutputStream(activity);
				BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(fileOut));
			    bw1.write("222-Radon activities calculated with " + SoftwareVersion + "\r\n"+ "\r\n");
			    bw1.write("Evaluated by: " + evaluator + " on "); 
			    bw1.write(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))); 
			    bw1.write("\r\n");
			    bw1.write("Used parameters \r\n");
			    String Method="Stockburger";
			    bw1.write("Method	    : " + Method + "\r\n");
			    bw1.write("Source File : " + extract.getPath() + "\r\n");
			    bw1.write("Solid Angle : " + String.valueOf(ini.solidangle) + "\r\n");
			    bw1.write("Disequil.   : " + String.valueOf(ini.disequilibrium) + "\r\n");
			    bw1.write("Flux Offset : " + String.valueOf(ini.fluxoffset) + "\r\n");
			    bw1.write("Flux Slope  : " + String.valueOf(ini.fluxslope) + "\r\n"+"\r\n");	
			    bw1.write("Format: \r\n");
			    bw1.write("Stoptime; Activity [Bq/m3]; Ac[dps]; Ac/dt; Total; Window; Edge; temp1[C]; temp2[C]; temp3[C]; Pressure[mbar]; LifeTime[sec]; Flux[m3/s]; ID \r\n");
			    
			    //split extlines to get rid of duplicates or  missing values
			    ArrayList<ArrayList<String>> splittedExtlines = new ArrayList<ArrayList<String>>();
			    ArrayList<ArrayList<String>> splittedActlines = new ArrayList<ArrayList<String>>();
			    ArrayList<String> tmpStringList = new ArrayList<String>();
			
			    //save positions where to split
			    // 0-> dont split; 1-> split; 2-> delete 
			    int[] flag = new int[extlines.size()];
			    flag[0] = 0;
			    tmpStringList.add(extlines.get(0));
			    
			    for (int i = 1; i< extlines.size(); i++) {
			    	
			    	LocalDateTime current =  LocalDateTime.parse(extlines.get(i).split(";")[0], DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss"));
			        LocalDateTime last = LocalDateTime.parse(extlines.get(i-1).split(";")[0], DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss"));
			        long difference = Duration.between(last,current).toMinutes();
			        
			    	if(difference > 30 ) {
			    		//if (Datetime_last - Datetime_current) > 30min
			    		flag[i] = 1; //split here
			    		System.out.println("split, diff is " + difference + " min");
			    		splittedExtlines.add((ArrayList<String>) tmpStringList.clone());
			    		tmpStringList.clear();
			    		tmpStringList.add(extlines.get(i));
			    		System.out.println("new Array of extLines " + extlines.get(i));
			    	}
			        	else if(difference < 1 ) {
			        		//if (Datetime_last - Datetime_current) < 1min
			        		flag[i] = 2; //remove this
			        		System.out.println("remove " + difference);
			        		System.out.println("don't count this line (maybe duplicate) " + extlines.get(i));
			        		continue;
			        	}
			        	else {
			        		flag[i] = 0;
			        		tmpStringList.add(extlines.get(i));
			        		System.out.println("add " + extlines.get(i));
			        	}		        	
			    }
			    
			    splittedExtlines.add((ArrayList<String>) tmpStringList.clone());
			    tmpStringList.clear();
				
			    //calculating the values with Stockburger
			    System.out.println("Calculating Stockburger");
			    for(int x = 0; x < splittedExtlines.size(); x++) {	 
			    	tmpStringList = (ArrayList<String>) calcStockburger(splittedExtlines.get(x), Integer.parseInt(points)).clone();
			    	if (tmpStringList.get(0) == "") {
			    		continue;
			    	}
			    	
			    	splittedActlines.add((ArrayList<String>) tmpStringList.clone());
			    }
			    tmpStringList.clear();
			    
			    //gather, fuse and fill if variable "fill"  == true
			    Boolean fill = false;
			    if(ini.fill == 1) fill = true;
			    if(!fill || splittedActlines.size() == 1) {
			    	System.out.println("Don't need to fill up");
			    	//just write the results into the file if no filler is given or only one block was created
			        for(int i=0; i<splittedActlines.size(); i++) {
			        	for(int k = 0; k < splittedActlines.get(i).size() ; k++) {
			        		bw1.write(splittedActlines.get(i).get(k) + "\r\n");
			        	}
			        }
			    } else {
			    	//write the results into the file but every time a new block starts, fill it with the correct date and the filler
			    	System.out.println("Fill Up with " + ini.filler);
			    	for(int i = 0; i < splittedActlines.size() ; i++) {
			    		for(int k = 0; k < splittedActlines.get(i).size(); k++) {
			    			bw1.write(splittedActlines.get(i).get(k) + "\r\n");
			    		}
			    		try {
			    			//taking last line form the current peace and first line from the next piece
			    			//calculate time difference and number of entries to fill
			    			String last = splittedActlines.get(i).get(splittedActlines.get(i).size()-1);
			    			String next = splittedActlines.get(i+1).get(0);
			    			ArrayList<String> fillingStrings = getDateTimeBetween(last, next);
			    			for (int l = 0; l < fillingStrings.size(); l++) {
			    				bw1.write(fillingStrings.get(l)+ ";" + ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + ";"+ ini.filler + "\r\n");
			    				}
			    		} catch (Exception e2) {
			    			//could not access splittedActlines.get(i+1) -> filling done
			    			break;
			    		}
			    		
			    	}
			    }
			    bw1.close();
				JOptionPane.showMessageDialog(null, "Successfully created " + activity.getName() , "Continue evaluation", JOptionPane.INFORMATION_MESSAGE);
				progressBar.setString("");
				progressBar.setValue(0);
				
				//clear the flagged arrays for the next iteration
				flaggedIdx.clear();
				flagged.clear();
				tmpList.clear();
				flag = null;
				
				
			} catch (Exception e3) {
				progressBar.setString("");
				progressBar.setValue(0);
				e3.printStackTrace();
			}
			
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
}
	
	public boolean checkFilename(String Filename) {
		//check if a given string has a proper filename for a spectra
		if(Filename.length()==12) {
			if(Filename.contains(".R")) {
				return true;
			}
		}
		return false;
	}
}