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
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.Color;
import javax.swing.JRadioButton;
import java.awt.SystemColor;
import java.awt.Window;

import javax.swing.JProgressBar;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
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
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.event.ChangeListener;
import javax.xml.ws.Service;
import javax.swing.event.ChangeEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Window.Type;

public class RnLog extends JFrame {

	/**
	 * 
	 */
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
	private JTable table;
	public String SoftwareVersion = "RnLog 2.0";
	Thread TLiveMode;
	//for continue evaluation mode
	public ArrayList<Spectra> flagged = new ArrayList<Spectra>();
	public ArrayList<Integer> flaggedIdx = new ArrayList<Integer>();
	public int currentflaggedSpec = 0;
	public File extract = null;
	public File activity = null;
	
	//load *.ini file
	public iniFile ini = new iniFile();
	
	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
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
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Extract Logfile");
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Make Activity File");
		mnNewMenu.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_8 = new JMenuItem("Make Activity File from multiple Log (txt) Files");
		mnNewMenu.add(mntmNewMenuItem_8);
		
		JMenuItem mntmNewMenuItem_7 = new JMenuItem("Continue Evaluation");
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
		
		JLabel lblAdc = new JLabel("ADC 1:");
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
		
		
		//TODO: Gridlines auf schwarz setzen
		//CategoryPlot plot = chart.getCategoryPlot();
		//plot.setDomainGridlinePaint(Color.BLACK);
	    //plot.setRangeGridlinePaint(Color.BLACK);
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
		
		//TODO: change edge/spektrum with keys
        class MyDispatcher implements KeyEventDispatcher {
			@Override
		    public boolean dispatchKeyEvent(KeyEvent e) {
				System.out.println(e.getKeyChar());
				if(spectraList.size() == 0) return false;
				//down key was pressed
		        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					try {
						tfEdge.setText(spectraList.get(selectedSpecIdx - 1).showSpectra(chartPanel));
						selectedSpecIdx--;
					} catch (Exception e2) {
			             System.out.println("no previous spectrum found -> out of bounds"); 
						//no previous spectrum found -> out of bounds
					}
		         }
		        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
					try {
						tfEdge.setText(spectraList.get(selectedSpecIdx).changeEdge(chartPanel, chart, true));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        }
		        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
					try {
						tfEdge.setText(spectraList.get(selectedSpecIdx).changeEdge(chartPanel, chart, false));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        }
		        if(e.getKeyCode() == KeyEvent.VK_UP) {
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
		 }
		
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
					// TODO Auto-generated catch block
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
					RefSpec = new Spectra(spectraList);
					spectraList.clear();
					spectraList.add(RefSpec);
					tfEdge.setText(RefSpec.showSpectra(chartPanel));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
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
					iniFile ini = new iniFile();
					} 
					EvalIniDialog dialog = new EvalIniDialog(ini);
					dialog.setVisible(true);
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_3);
		
		
		//Extract Spectra
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//PopUp to enter Name of *.ext file
				//Select Spectra to extract
				String extFilename = (String) JOptionPane.showInputDialog(null,"Enter a filename (*.txt):",
                        "Creating Logfile",
                        JOptionPane.PLAIN_MESSAGE, null, null, "extract.txt");
				//if no filename was given or canceled:
				if(extFilename == null || (extFilename != null && ("".equals(extFilename))))   {
				    return;
				}
				System.out.println(extFilename);
				spectraList.clear();
				//select spectra
				chooseSpectraDialog(chartPanel);
				if(spectraList.isEmpty()) {
					//user cancelled operation
					return;
				}
				//extract spectra from spectra list and write it into extFile
				File extFile = null;
				System.out.println(spectraList.get(0).path.getParent() + "\\" + extFilename);
				extFile = new File(spectraList.get(0).path.getParent() + "\\" + extFilename);
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
			        int[] flaggedIdx = new int[spectraList.size()];
			        for(int i=0; i<spectraList.size(); i++) {
			        	//set edge of spectrum according to reference (if no edge is set yet)
			        	spectraList.get(i).calcEdge(RefSpec, ini.thres3, ini.thres4, ini.Edgeoffset);
			        	progress = (((double) i+1.0)/(spectraList.size())*100.0);
			        	System.out.println( (progress));
			        	progressBar.setValue((int) (progress));
			        	//flag spectra
			        	if(spectraList.get(i).edge > ini.Edgeoffset+ini.UpperFlagThres || spectraList.get(i).edge < ini.Edgeoffset-ini.LowerFlagThres) {
			        		//save Spectrum in new subfolder \flagged
			        		File tmpFlagged = new File( spectraList.get(i).path.getParent()+ "\\flagged\\" + spectraList.get(i).name);
			        		copyFile(spectraList.get(i).path, tmpFlagged);
			        		flaggedIdx[i]=1;
			        		System.out.println("copied " + spectraList.get(i).path.getPath() + " to " + tmpFlagged.getPath() );
			        		continue;
			        	}
			        	//TODO: berechnung der slopes und RNs
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
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		
		//make activity file from a single file
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		        final JFileChooser actDialog = new JFileChooser();
		        
		        //only show not hidden files
		        actDialog.setFileHidingEnabled(true);
		        //to select single file
		        actDialog.setMultiSelectionEnabled(false);
		        //In response to a button click:
		        int option = actDialog.showOpenDialog(null);
		        File extFile=null;
		        if(option == JFileChooser.APPROVE_OPTION) {
		        	extFile = actDialog.getSelectedFile();
		        } else if (option == JFileChooser.CANCEL_OPTION){
		        	System.out.println("User cancelled operation. No ACT file was chosen.");
		        	return;
		        }
		        String actFilename = (String) JOptionPane.showInputDialog(null,"Filename (*.act):",
                        "Creating ACT File",
                        JOptionPane.PLAIN_MESSAGE, null, null, extFile.getName().split("\\.")[0]+".act");
				if(actFilename == null || (actFilename != null && ("".equals(actFilename)))) {
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
		        //open ext and act file
				FileReader fileReader;
				FileOutputStream fileOut;
				File actFile = new File(extFile.getParent() + "\\" + actFilename);
				try {
					//read in extract file
					fileReader = new FileReader(extFile);
			        BufferedReader bufferedReader = new BufferedReader(fileReader);
			        ArrayList<String> extlines = new ArrayList<String>();
			        ArrayList<String> actlines = new ArrayList<String>();
			        String line = null;
			        while ((line = bufferedReader.readLine()) != null) {
			            extlines.add(line);
			        }
			        bufferedReader.close();
			        System.out.println("successfully loaded extract file");
			        if(extlines.size()<2) {
			        	JOptionPane.showMessageDialog(null, "Extract file is empty or has no values."/*, JOptionPane.INFORMATION_MESSAGE*/);
			        	return;
			        }
			        //open act File
					fileOut = new FileOutputStream(actFile);
			    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOut));
			        bw.write("222-Radon activities calculated with " + SoftwareVersion + "\r\n"+ "\r\n");
			        //DateTimeFormatter.ofPattern("d M y");
			       // System.out.println(java.time.LocalDate.now().format());
			        //TODO: das anpassen
			        bw.write("Evaluated by: " + evaluator + " on "); 
			        bw.write(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))); 
			        bw.write("\r\n");
			        bw.write("Used parameters \r\n");
			        String Method="Stockburger";
			        bw.write("Method	    : " + Method + "\r\n");
			        bw.write("Source File : " + extFile.getPath() + "\r\n");
			        bw.write("Solid Angle : " + String.valueOf(ini.solidangle) + "\r\n");
			        bw.write("Disequil.   : " + String.valueOf(ini.disequilibrium) + "\r\n");
			        bw.write("Flux Offset : " + String.valueOf(ini.fluxoffset) + "\r\n");
			        bw.write("Flux Slope  : " + String.valueOf(ini.fluxslope) + "\r\n"+"\r\n");	
			        bw.write("Format: \r\n");
			        bw.write("Stoptime,Activity [Bq/m3], Ac[dps],Ac/dt,Total, Window, Edge, temp1[C], temp2[C], temp3[C], Pressure[mbar], LifeTime[sec], Flux[m3/s], ID \r\n");
			      
			        //split extlines to get rid of duplicates or  missing values
			        ArrayList<ArrayList<String>> splittedExtlines = new ArrayList<ArrayList<String>>();
			        ArrayList<ArrayList<String>> splittedActlines = new ArrayList<ArrayList<String>>();
			        ArrayList<String> tmpList = new ArrayList<String>();

			        //save positions where to split
			        // 0-> dont split; 1-> split; 2-> delete 
			        int[] flag = new int[extlines.size()];
			        flag[1] = 0;
			        tmpList.add(extlines.get(1));
			        int j = 0;
			        DateFormat formatter = new SimpleDateFormat("dd.MM.yyy HH:mm:ss");
			        
			        for (int i = 2; i< extlines.size(); i++) {
			        	
			        	long last = formatter.parse(extlines.get(i-1).split(";")[0]).getTime();
			        	long actual =  formatter.parse(extlines.get(i).split(";")[0]).getTime();
			        	if((actual - last) > 1800000 ) {
			        		//if (Datetime_last - Datetime_current) > 1800s
			        		flag[i] = 1; //split here
			        		System.out.println("split " + (actual - last));
			        		splittedExtlines.add((ArrayList<String>) tmpList.clone());
			        		tmpList.clear();
			        		j++;
			        		tmpList.add(extlines.get(i));
			        		System.out.println("new Array of extLines " + extlines.get(i));
			        	}
				        	else if((actual - last) < 60000 ) {
				        		//if (Datetime_last - Datetime_current) < 600s
				        		flag[i] = 2; //remove this
				        		System.out.println("remove " + (actual - last));
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
				        		System.out.println( i + " "+ k + " " + splittedActlines.get(i).get(k));
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
				} catch (Exception e3) {
					e3.printStackTrace();
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
			        final JFileChooser actDialog = new JFileChooser();
			        
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
			            			            		
			            		FileReader fileReader;
			    				fileReader = new FileReader(extFiles[x]);
			    		        BufferedReader bufferedReader = new BufferedReader(fileReader);	    		        
			    		        String line = null;
			    		        line = bufferedReader.readLine();
			    		        while ((line = bufferedReader.readLine()) != null) {
			    		            extlines.add(line);
			    		        }
			    		        bufferedReader.close();
			    		        System.out.println("successfully loaded extract file number " + (x + 1));
			            		
			                }
			            }
				        
			            //sorting entries in the final extract file by the measurement time
			            DateFormat formatter = new SimpleDateFormat("dd.MM.yyy HH:mm:ss");
			            int minIndex;
				        for (int i = 0; i<extlines.size()-1; i++) {
				        	String minTimeExtLine = extlines.get(i);
				        	minIndex = i;
				        	for (int j = i+1; j< extlines.size(); j++) {
					        	long currentMin = formatter.parse(minTimeExtLine.split(";")[0]).getTime();
					        	long currentLine =  formatter.parse(extlines.get(j).split(";")[0]).getTime();
					        	if(currentMin > currentLine) {
					        		minTimeExtLine = extlines.get(j);
					        		minIndex = j;
					        	}
					        }
				        	
				        	//changing places of current i element with the minimum of the remaining
				        	extlines.set(minIndex, extlines.get(i));
				        	extlines.set(i, minTimeExtLine);
		
				        }
				        
				        int dialogButton = JOptionPane.YES_NO_OPTION;
			        	int dialogResult = JOptionPane.showConfirmDialog (null, "Would you like to save combined and sorted Log file?" ,"Save new Log file?", dialogButton);
			        	if(dialogResult == JOptionPane.YES_OPTION){
			        		JFileChooser fileChooser = new JFileChooser();
			        		
			        		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			        		  File newExtFile = fileChooser.getSelectedFile();
			        		  try {
			        			  String newFilePath = newExtFile.getPath();
			        			  if (!(newFilePath.substring(newFilePath.length() - 3) == ".txt")) {
			        				 newExtFile = new File(newFilePath+".txt");
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
				      		      for (int i=0;i<extlines.size();i++) {
				      		    	  bw.write(extlines.get(i) + "\r\n");
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
				        
				        String actFilename = (String) JOptionPane.showInputDialog(null,"Filename (*.act):",
		                        "Creating ACT File in " +ini.lvl2,
		                        JOptionPane.PLAIN_MESSAGE, null, null, "activity.act");
						if(actFilename == null || (actFilename != null && ("".equals(actFilename)))) {
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
						      
				        //creating act file
						FileOutputStream fileOut;
						File actFile = new File(ini.lvl2 + "\\" + actFilename);
						System.out.println("saving activity file at " + actFile.getPath());
						
						ArrayList<String> actlines = new ArrayList<String>();
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
				        bw.write("Stoptime,Activity [Bq/m3], Ac[dps],Ac/dt,Total, Window, Edge, temp1[C], temp2[C], temp3[C], Pressure[mbar], LifeTime[sec], Flux[m3/s], ID \r\n");
				      
				        //split extlines to get rid of duplicates or  missing values
				        ArrayList<ArrayList<String>> splittedExtlines = new ArrayList<ArrayList<String>>();
				        ArrayList<ArrayList<String>> splittedActlines = new ArrayList<ArrayList<String>>();
				        ArrayList<String> tmpList = new ArrayList<String>();
		
				        //save positions where to split
				        // 0-> dont split; 1-> split; 2-> delete 
				        int[] flag = new int[extlines.size()];
				        flag[1] = 0;
				        tmpList.add(extlines.get(1));
				        int j = 0;
				        
				        for (int i = 2; i< extlines.size(); i++) {
				        	
				        	long last = formatter.parse(extlines.get(i-1).split(";")[0]).getTime();
				        	long actual =  formatter.parse(extlines.get(i).split(";")[0]).getTime();
				        	if((actual - last) > 1800000 ) {
				        		//if (Datetime_last - Datetime_current) > 1800s
				        		flag[i] = 1; //split here
				        		System.out.println("split " + (actual - last));
				        		splittedExtlines.add((ArrayList<String>) tmpList.clone());
				        		tmpList.clear();
				        		j++;
				        		tmpList.add(extlines.get(i));
				        		System.out.println("new Array of extLines " + extlines.get(i));
				        	}
					        	else if((actual - last) < 60000 ) {
					        		//if (Datetime_last - Datetime_current) < 600s
					        		flag[i] = 2; //remove this
					        		System.out.println("remove " + (actual - last));
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
					        		System.out.println( i + " "+ k + " " + splittedActlines.get(i).get(k));
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
				        JOptionPane.showMessageDialog(null, "Successfully created " + actFile.getPath() , "Make Activity File from multiple sources", JOptionPane.INFORMATION_MESSAGE);
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
		JButton btnContinueFlagging = new JButton("continue evaluation");
		btnContinueFlagging.setBackground(new Color(50, 205, 50));
		btnContinueFlagging.setToolTipText("click here to continue the automatic evaluation after setting the edge for the flagged spectra");
		btnContinueFlagging.setBounds(10, 435, 145, 44);
		panel.add(btnContinueFlagging);
		btnContinueFlagging.setVisible(false);

		//continue evaluation
		mntmNewMenuItem_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				continueEvaluation(progressBar, chartPanel, panel, btnContinueFlagging);			
			}
		});
		
		btnContinueFlagging.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				continueEvaluation2(btnContinueFlagging);
				
				
				// get the next spectra where the edge needs to be set
				/*
				if(currentflaggedSpec==flagged.size() || flagged.isEmpty()) {
					//continue the evaluation
					continueEvaluation2(btnContinueFlagging);
				}
				flagged.get(currentflaggedSpec).showSpectra(chartPanel);
				*/
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
        final JFileChooser fileDialog = new JFileChooser();
        
        //only show not hidden files
        fileDialog.setFileHidingEnabled(true);
        
        //TODO: home directory als start ausw#hlen
        //fileDialog.setCurrentDirectory(System.getProperty("user.dir"));
        
        String filenames = "";
        //to select mulitple files
        fileDialog.setMultiSelectionEnabled(true);
        //In response to a button click:
        int option = fileDialog.showOpenDialog(null);
        //create list of spectra
        //ArrayList<Spectra> spectraList = new ArrayList<Spectra>();
        //defined globally
        spectraList.clear();
        if(option == JFileChooser.APPROVE_OPTION) {
        	//  File[] files = getSelectedFiles(fileDialog);
            /*final*/ File[] files = fileDialog.getSelectedFiles();
        	System.out.println("get selected files");
            if (files != null && files.length > 0){
            	for (int x = 0; x < files.length; x++) { 
                	filenames = filenames + "\n" + files[x].getName();
               		System.out.println("You have selected this file:\n" +files[x]);
               		if(!checkFilename(files[x].getName()))
               			continue;	
               		Spectra actualSpectrum;
               		System.out.println("add actual spectrum");
               		try {
               			actualSpectrum = new Spectra(files[x].getName(), files[x]);
               			//add actual spectrum to list
               			spectraList.add(actualSpectrum);
               		} catch (Exception e) {// iwo hier ist der fehler...kp
               			// TODO Auto-generated catch block
               			System.out.println("Something went wrong, maybe the file is already opened?");
               			e.printStackTrace();
               		}
                }
            }
            //zeige erstes Spectrum der auswahl
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
		System.out.println("Calculating these lines : ");
		for ( int i =0; i< extLines.size(); i++) {
			System.out.println(extLines.get(i));
		}
		
		//load *.ini file
		if (ini._pathToIniFile == null) {
			iniFile ini = new iniFile();
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
		if(extLines.size()<4) {
			System.out.println("Extractfile had less than 4 lines.");
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
		
		
		for(int i = 1; i < extLines.size(); i++) {
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
			
			//calculate flux in m³/s
			fluxs[i] = Double.valueOf(extLines.get(i).split(";")[2].replaceAll("\\s+",""));
			//multiply by slope, add offset and convert to m³/s
			fluxs[i] = ((ini.fluxslope * fluxs[i]) + ini.fluxoffset) / 3600000; 
			
			//calculate act_p
			act_ps[i] = ini.disequilibrium / (ini.solidangle * fluxs[i] * 4302);
		}
		
		for( int i = 1; i < extLines.size(); i++) {
			//use intermediates to calculate the derivation (dAc/dt)
			//kick the first #points out, because there are not enough values before to calculate the derivation
			if((i - 1 - points) < 0) {
				//not enough points to calculate derivation
				continue;
			}
			
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
			System.out.println("adding " + actline);
		}
		return actlines;
	}

	public ArrayList<String> getDateTimeBetween ( String DT1, String DT2) throws ParseException{

		//helper function which gets the Date Time of a String (split it at ";" and convert to timeformat
		//and returns a list of Strings with timestamps between
		ArrayList<String> results = new ArrayList<String>();
		
		//time format used in the spactra
		DateFormat formatter = new SimpleDateFormat("dd.MM.yyy HH:mm:ss");
		//extracting the time information from the first entry in the line
        Date first = formatter.parse(DT1.split(";")[0]);
        Date last = formatter.parse(DT2.split(";")[0]);
        long diff = last.getTime() - first.getTime();
        
        //failsafe for the time difference
		if (diff < 1800000) {
			return results;
		}
		//number of entries to fill
		int fillingpoints = 0;
		fillingpoints = (int) (diff/1800000);
		for(int i = 1; i<fillingpoints; i++) {
			Date newDate = new Date(first.getTime() + 1800000*i);
			results.add(formatter.format(newDate));
			 System.out.println("fill this :" + formatter.format(newDate));
		}
		return results;
	}

	//guides the user through the evaluation
	//this needed to be splitted into two functions, the first one gets the new spectra, copies them
	//and tries to set the edge, afterwards, if there are flagged spectra, the user can to flag them manually
	//which will then start part2 of the evaluation
	public void continueEvaluation(JProgressBar progressBar, ChartPanel chartPanel, JPanel panel, JButton btnContinue) {
		//Search where the last evaluation ended and start a new one
		//expects lvl0, lvl2 and lvl1 directories
		if (ini._pathToIniFile == null) {
			iniFile ini = new iniFile();
			} 
		System.out.println("setting the progress bar");
		progressBar.setStringPainted(true);
		progressBar.setString("gathering spectra from lvl0 and lvl2");
		progressBar.setValue(0);
		String lvl0=  ini.lvl0; //"C:\\Users\\Alessandro\\eclipse-workspace\\AutoRnLog\\lvl0\\";
		String lvl1=  ini.lvl1; //"C:\\Users\\Alessandro\\eclipse-workspace\\AutoRnLog\\lvl1\\";
		String lvl2=  ini.lvl2; //"C:\\Users\\Alessandro\\eclipse-workspace\\AutoRnLog\\lvl1\\";
		//compare the files from lvl0 and lvl2, if a file is not in lvl2 but its after the last file in lvl0 -> continue evaluation
		//otherwise return, because there is no new file to evaluate
		File lvl0Dir = new File(lvl0);
		File lvl2Dir = new File(lvl2);
		ArrayList<File> rawFiles = new ArrayList<File>();// = lvl0Dir.listFiles();
		ArrayList<File> evFiles = new ArrayList<File>();// = lvl2Dir.listFiles();
		System.out.println(lvl0Dir + " exists? " + lvl0Dir.exists());
		System.out.println(lvl2Dir + " exists? " + lvl2Dir.exists());

		try {
			for (int i = 0; i < lvl0Dir.listFiles().length; i++) {
				//check if the file is a spectra
				if (lvl0Dir.listFiles()[i].isFile() && checkFilename(lvl0Dir.listFiles()[i].getName())) {
					rawFiles.add(lvl0Dir.listFiles()[i]);
				}
				//search for reference spectrum
				if (lvl0Dir.listFiles()[i].isFile() && lvl0Dir.listFiles()[i].getName().contains("temp_ref_spec.ref")) {
					try {
						System.out.println("Reference spectrum found: " + lvl0Dir.listFiles()[i] + lvl0Dir.listFiles()[i].getName());
						RefSpec = new Spectra(lvl0Dir.listFiles()[i].getName(), lvl0Dir.listFiles()[i]);
					} catch (Exception e) {
						System.out.println("could not load reference spectrum");
						JOptionPane.showMessageDialog(null, "No reference spectrum found. Please create one first." , "Continue evaluation", JOptionPane.INFORMATION_MESSAGE);
						progressBar.setString("");
						progressBar.setValue(0);
						e.printStackTrace();
						return;
					}
				}
			}
			for (int i = 0; i < lvl2Dir.listFiles().length; i++) {
				//check if the file is a spectra
				if (lvl2Dir.listFiles()[i].isFile() && checkFilename(lvl2Dir.listFiles()[i].getName())) {
					evFiles.add(lvl2Dir.listFiles()[i]);
				}
				
				//search for activity and extract file
				if (lvl2Dir.listFiles()[i].isFile() && lvl2Dir.listFiles()[i].getName().endsWith(".txt")) {
					extract = lvl2Dir.listFiles()[i];
				}
				if (lvl2Dir.listFiles()[i].isFile() && lvl2Dir.listFiles()[i].getName().endsWith(".act")) {
					activity = lvl2Dir.listFiles()[i];
				}
			}
		} catch (Exception e) {
			//could not load the spectra
			JOptionPane.showMessageDialog(null, "Could not read the spectra from lvl0 and lvl2 directories, please specify a correct path in the *.ini file." , "Continue evaluation", JOptionPane.INFORMATION_MESSAGE);
			progressBar.setString("");
			progressBar.setValue(0);
			e.printStackTrace();
			return;
		}
		
		//double check if the reference spectrum was found
		if (RefSpec == null) {
			//no reference spectrum found
			JOptionPane.showMessageDialog(null, "No reference spectrum found. Please create one first." , "Continue evaluation", JOptionPane.INFORMATION_MESSAGE);
			progressBar.setString("");
			progressBar.setValue(0);
			return;
		}
		
		//compare the two lists, create a new list which contains the new spectra from lvl0 that need to be evaluated
		//add them to a Map<String, int <amount of added items>
		int max = rawFiles.size();
		if(evFiles.size()>max)
			max = evFiles.size();
		//get the latest Filenames that are not in lvl0
		ArrayList<File> toEvaluate = new ArrayList<File>();
		Collections.sort(rawFiles, Collections.reverseOrder());
		Collections.sort(evFiles, Collections.reverseOrder());
		//check if there are no files in evFiles (lvl2), otherwise u get a nullPointer exception
		if(!evFiles.isEmpty()) {
			if(rawFiles.get(0) == evFiles.get(0)){
				//no new file
				JOptionPane.showMessageDialog(null, "The latest spectrum found is " + rawFiles.get(0).getName() + " and it is already evaluated." , "Continue evaluation", JOptionPane.INFORMATION_MESSAGE);
				progressBar.setString("");
				progressBar.setValue(0);
				return;
			}
		}
		//copy all, or just the new ones
		if(evFiles.isEmpty()) {
			System.out.println("no files in lvl2 found -> copy all");
			while(!rawFiles.isEmpty()) {
				System.out.println("remembering " + rawFiles.get(0));
				toEvaluate.add(rawFiles.get(0));
				rawFiles.remove(0);
			}
		} else {
			System.out.println("copy only new files from lvl0");
			while(!rawFiles.isEmpty()) {
				if(rawFiles.get(0).getName().equals(evFiles.get(0).getName())) {
					break;
				} else {
					toEvaluate.add(rawFiles.get(0));
					rawFiles.remove(0);
				}
			}
		}
		if(toEvaluate.isEmpty()) {
			JOptionPane.showMessageDialog(null, "The latest spectrum found is " + rawFiles.get(0).getName() + " and it is already evaluated." , "Continue evaluation", JOptionPane.INFORMATION_MESSAGE);
			progressBar.setString("");
			progressBar.setValue(0);
			return;
		}
		progressBar.setString("extracting new spectra");
		progressBar.setValue(0);
	
		
		//////////////////////////////////////////////////////
		//extract these files and add it to the extract file
		//////////////////////////////////////////////////////

		if(extract == null) {
			System.out.println("No extract file found, creating a new one at " + lvl2Dir.getPath()+"\\extract.txt");
			File e = new File(lvl2Dir.getPath()+"\\extract.txt");
			try {
				e.createNewFile();
				FileOutputStream fileOut = new FileOutputStream(e);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOut));
		    	double progress = 0;
		    	progressBar.setValue((int) (progress));
		    	progressBar.setStringPainted(true);
		    	//write first line
		        bw.write("Date Time; Lifetime;ADC1; StdADC1; T1; StdT1;T2; StdT2;T3; StdT3;Rn1;Rn2;Rn3;Rn4;ADC2; StdADC2; ADC3; StdADC3; Counter1;Counter2;FluxSlope;FluxOffset;ADC2Slope;ADC2Offset;ADC3Slope;ADC3Offset;Temp1Slope;Temp1Offset;Temp2Slope;Temp2Offset;Temp3Slope;Temp3Offset;Counter1Slope;Counter1Offset;Counter2Slope;Counter2Offset;ID \r\n");
		        bw.close();
		        fileOut.close();
		        extract = e;
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Could not create the extract file, maybe you have no writing permissions?" , "Continue evaluation", JOptionPane.INFORMATION_MESSAGE);
				e1.printStackTrace();
				return;
			}
		}

		if (activity == null) {
			System.out.println("No activity file found, creating a new one at " + lvl2Dir.getPath()+"\\activity.txt");
			File a = new File(lvl2Dir.getPath()+"\\activity.act");
			try {
				a.createNewFile();
				activity = a;
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Could not create the activity file, maybe you have no writing permissions?" , "Continue evaluation", JOptionPane.INFORMATION_MESSAGE);
				e1.printStackTrace();
				return;
			}
		}
		String extFilename = extract.getName();
		
		//copy the files that need to be evaluated into lvl2	
		for(int i=0; i<toEvaluate.size(); i++) {
			try {
				System.out.println("copy " + toEvaluate.get(i) + " to " +  lvl2Dir + "\\" + toEvaluate.get(i).getName());
				copyFile(toEvaluate.get(i), new File(lvl2Dir + "\\" + toEvaluate.get(i).getName()));
				//dont flag the lvl0 spectra
				System.out.print("davor :" +toEvaluate.get(i));
				toEvaluate.set(i, new File(lvl2Dir + "\\" + toEvaluate.get(i).getName()));
				System.out.print("danach :" +toEvaluate.get(i));
			} catch (IOException e) {
				System.out.println("Could not copy file into lvl2");
				JOptionPane.showMessageDialog(null, "Could not copy the evaluated file into the lvl2 folder. Maybe you have no writing permissions?"/*, JOptionPane.INFORMATION_MESSAGE*/);
				e.printStackTrace();
				break;
			}
		}
		
		spectraList.clear();
		//select spectra
		for(int i=0; i< toEvaluate.size(); i++) {
			try {
				spectraList.add(new Spectra(toEvaluate.get(i).getName(), toEvaluate.get(i)));
			} catch (Exception e) {
				System.out.print(toEvaluate.get(i).getName() + " is broken");
				e.printStackTrace();
			}
		}
		
		//extract spectra from spectra list and write it into extFile
		File extFile = extract;
		System.out.println(spectraList.get(0).path.getParent() + "\\" + extFilename);
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(extFile, true);
			//--------------------------------------^^^^ means append new line, don't override old data
	    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOut));
	    	double progress = 0;
	    	progressBar.setValue((int) (progress));
	    	progressBar.setStringPainted(true);
	    	
	    	//loop though the selected spectra, see if they need to be flagged	       
	        for(int i=0; i<spectraList.size(); i++) {
	        	//set edge of spectrum according to reference (if no edge is set yet)
	        	spectraList.get(i).showSpectra(chartPanel);
	        	spectraList.get(i).calcEdge(RefSpec, ini.thres3, ini.thres4, ini.Edgeoffset);
	        	spectraList.get(i).showSpectra(chartPanel);
	        	progress = (((double) i+1.0)/(spectraList.size())*100.0);
	        	progressBar.setValue((int) (progress));
	        	//flag spectra
	        	if(spectraList.get(i).edge > ini.Edgeoffset+ini.UpperFlagThres || spectraList.get(i).edge < ini.Edgeoffset-ini.LowerFlagThres) {
	        		//for continue evaluation: remember these spectra and ask user to set edge manually
	        		
	        		//add actual spectrum to list
	        		flagged.add(spectraList.get(i));  
	        		//add index of the flagged file to array
	        		flaggedIdx.add(i);
	        	}
	        }
	        
	      //removing flagged spectra from the spectra list
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
	        		bw.close();
	        		
	        		//moving  Spectra to the new flagged subfolder
	        		new File(flagged.get(0).path.getParent()+ "\\flagged").mkdirs();
	        		
	        		for(int i=0; i<flagged.size(); i++) {
	        			//removing the generated edge if the user currently don't want to set the edge manually
	        			flagged.get(i).removeEdge();
	        			
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
	        	} else {
	        		//user wants to flag spectra himself
	        		btnContinue.setVisible(true);
	        		tmpList = (ArrayList<Spectra>) spectraList.clone();
	        		spectraList.clear();
	        		spectraList = (ArrayList<Spectra>) flagged.clone();
	        		spectraList.get(0).showSpectra(chartPanel);
	        		bw.close();
	        		return;
	        	}
	        } 
	        
	        if (flagged.size()==0) {
	        	continueEvaluation2(btnContinue);
	        }
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void continueEvaluation2(JButton btnContinue) {
		FileOutputStream fileOut;
		try {
			//user set the edge of some spectra manually
			if(!tmpList.isEmpty()) {

				//adding corrected spectra to the proper place in the array
				for (int i = 0; i < flaggedIdx.size(); i++) {
					tmpList.add(flaggedIdx.get(i)+i, spectraList.get(i));
	    		}
				
				spectraList = (ArrayList<Spectra>) tmpList.clone();
				tmpList.clear();
				flaggedIdx.clear();
				flagged.clear();
			}
			
			//spectraList needs to be reversed in order
			for (int i=0; i<spectraList.size(); i++) {
				tmpList.add(spectraList.get(spectraList.size()-i-1));
			}
			spectraList = (ArrayList<Spectra>) tmpList.clone();
			
			//writing the extract file
			fileOut = new FileOutputStream(extract, true);
			//--------------------------------------^^^^ means append new line, don't override old data
	    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOut));
	        for (int i=0; i<spectraList.size(); i++) {
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
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	/////////////////////////////////////////////
	//create activity file
	////////////////////////////////////////////
	//TODO: don't completely redo activtiy file
	
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
	//select extract file
	//Create a file chooser

    String actFilename = activity.getName();
	String points = "1";	
    //open ext and act file
	FileReader fileReader;
	File actFile =activity;
	try {
		//read in extract file
		fileReader = new FileReader(extract);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        ArrayList<String> extlines = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            extlines.add(line);
        }
       
        bufferedReader.close();
        System.out.println("successfully loaded extract file");
        if(extlines.size()<2) {
        	JOptionPane.showMessageDialog(null, "Extract file is empty or has no values."/*, JOptionPane.INFORMATION_MESSAGE*/);
        	return;
        }
        //open act File
		fileOut = new FileOutputStream(actFile);
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOut));
        bw.write("222-Radon activities calculated with " + SoftwareVersion + "\r\n"+ "\r\n");
        //DateTimeFormatter.ofPattern("d M y");
       // System.out.println(java.time.LocalDate.now().format());
        //TODO: das anpassen
        bw.write("Evaluated by: " + evaluator + " on "); 
        bw.write(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))); 
        bw.write("\r\n");
        bw.write("Used parameters \r\n");
        String Method="Stockburger";
        bw.write("Method	    : " + Method + "\r\n");
        bw.write("Source File : " + extract.getPath() + "\r\n");
        bw.write("Solid Angle : " + String.valueOf(ini.solidangle) + "\r\n");
        bw.write("Disequil.   : " + String.valueOf(ini.disequilibrium) + "\r\n");
        bw.write("Flux Offset : " + String.valueOf(ini.fluxoffset) + "\r\n");
        bw.write("Flux Slope  : " + String.valueOf(ini.fluxslope) + "\r\n"+"\r\n");	
        bw.write("Format: \r\n");
        bw.write("Stoptime,Activity [Bq/m3], Ac[dps],Ac/dt,Total, Window, Edge, temp1[C], temp2[C], temp3[C], Pressure[mbar], LifeTime[sec], Flux[m3/s], ID \r\n");
        
        //split extlines to get rid of duplicates or  missing values
        ArrayList<ArrayList<String>> splittedExtlines = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> splittedActlines = new ArrayList<ArrayList<String>>();
        ArrayList<String> tmpList = new ArrayList<String>();

      //save positions where to split
        // 0-> dont split; 1-> split; 2-> delete 
        int[] flag = new int[extlines.size()];
        flag[1] = 0;
        tmpList.add(extlines.get(1));
        int j = 0;
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyy HH:mm:ss");
        
        for (int i = 2; i< extlines.size(); i++) {
        	
        	long last = formatter.parse(extlines.get(i-1).split(";")[0]).getTime();
        	long actual =  formatter.parse(extlines.get(i).split(";")[0]).getTime();
        	if((actual - last) > 1800000 ) {
        		//if (Datetime_last - Datetime_current) > 1800s
        		flag[i] = 1; //split here
        		System.out.println("split " + (actual - last));
        		splittedExtlines.add((ArrayList<String>) tmpList.clone());
        		tmpList.clear();
        		j++;
        		tmpList.add(extlines.get(i));
        		System.out.println("new Array of extLines " + extlines.get(i));
        	}
	        	else if((actual - last) < 60000 ) {
	        		//if (Datetime_last - Datetime_current) < 600s
	        		flag[i] = 2; //remove this
	        		System.out.println("remove " + (actual - last));
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
		
        //berechnung der Werte mit Stockburger
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
        	System.out.println("splittedActlines.size() is " + splittedActlines.size());
        	//just write the results into the file if no filler is given or only one block was created
	        for(int i=0; i<splittedActlines.size(); i++) {
	        	for(int k = 0; k < splittedActlines.get(i).size() ; k++) {
	        		System.out.println( i + " "+ k + " " + splittedActlines.get(i).get(k));
	        		bw.write(splittedActlines.get(i).get(k) + "\r\n");
	        	}
	        }
        } else {
        	//write the results into the file but every time a new block starts, fill it with the correct date and the filler
        	System.out.println("Fill Up with " + ini.filler);
        	System.out.println("splittedActlines.size() is " + splittedActlines.size());
        	for(int i = 0; i < splittedActlines.size() ; i++) {
        		for(int k = 0; k < splittedActlines.get(i).size(); k++) {
        			System.out.println( i + " "+ k + " " + splittedActlines.get(i).get(k));
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
    	btnContinue.setVisible(false);
    	JOptionPane.showMessageDialog(null, "Successfully created " + activity.getName() , "Continue evaluation", JOptionPane.INFORMATION_MESSAGE);
    	progressBar.setString("");
    	progressBar.setValue(0);
    	
	} catch (Exception e3) {
    	progressBar.setString("");
    	progressBar.setValue(0);
		e3.printStackTrace();
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