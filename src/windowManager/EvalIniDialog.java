package windowManager;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartPanel;

import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class EvalIniDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField tfMonitorID;
	private JTextField tfNoiseThreshold;
	private JTextField tfWindowThreshold;
	private JTextField tfLowerFitThreshold;
	private JTextField tfUpperFitThreshold;
	private JTextField tfLowerFlagThreshold;
	private JTextField tfUpperFlagThreshold;
	private JTextField tfFluxslope;
	private JTextField tfFluxoffset;
	private JTextField tfSolidangle;
	private JTextField tfDisequilibriumfactor;
	private JTextField tfHoentzsch;
	private JTextField tfInterval;
	private JTextField tfEdgeoffset;
	private JTextField tfIPAdress;
	private JTextField tfFluxchannel;
	private JTextField tfFiller;
	private JCheckBox chckbxfillup;
	private JLabel tfIniFilePath;
	private JTextField tfFluxthreshold;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			
			iniFile ini = new iniFile();
			
			EvalIniDialog dialog = new EvalIniDialog(ini);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EvalIniDialog(iniFile ini) {
		
		setBounds(100, 100, 819, 477);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNoiseThreshold = new JLabel("noise threshold 1:");
		lblNoiseThreshold.setBounds(10, 39, 127, 14);
		contentPanel.add(lblNoiseThreshold);
		
		
		JLabel lblMonitorID = new JLabel("Monitor ID:");
		lblMonitorID.setBounds(10, 14, 127, 14);
		contentPanel.add(lblMonitorID);
		
		JLabel lblWindowThreshold = new JLabel("window threshold 2:");
		lblWindowThreshold.setBounds(10, 64, 127, 14);
		contentPanel.add(lblWindowThreshold);
		
		JLabel lblLowerFitThreshold = new JLabel("lower fit threshold 3:");
		lblLowerFitThreshold.setBounds(10, 89, 127, 14);
		contentPanel.add(lblLowerFitThreshold);
		
		JLabel lblUpperFitThreshold = new JLabel("upper fit threshold 4:");
		lblUpperFitThreshold.setBounds(10, 114, 127, 14);
		contentPanel.add(lblUpperFitThreshold);
		
		JLabel lblLowerFlagThreshold = new JLabel("lower flag threshold:");
		lblLowerFlagThreshold.setBounds(10, 139, 127, 14);
		contentPanel.add(lblLowerFlagThreshold);
		
		JLabel lblUpperFlagThreshold = new JLabel("upper flag threshold:");
		lblUpperFlagThreshold.setBounds(10, 164, 127, 14);
		contentPanel.add(lblUpperFlagThreshold);
		
		tfMonitorID = new JTextField();
		tfMonitorID.setBounds(133, 11, 86, 20);
		tfMonitorID.setText(ini.id);
		contentPanel.add(tfMonitorID);
		tfMonitorID.setColumns(10);
		
		tfNoiseThreshold = new JTextField();
		tfNoiseThreshold.setColumns(10);
		tfNoiseThreshold.setBounds(133, 36, 86, 20);
		tfNoiseThreshold.setText(Integer.toString(ini.thres1));
		contentPanel.add(tfNoiseThreshold);
		
		tfWindowThreshold = new JTextField();
		tfWindowThreshold.setColumns(10);
		tfWindowThreshold.setBounds(133, 61, 86, 20);
		tfWindowThreshold.setText(Integer.toString(ini.thres2));
		contentPanel.add(tfWindowThreshold);
		
		tfLowerFitThreshold = new JTextField();
		tfLowerFitThreshold.setColumns(10);
		tfLowerFitThreshold.setBounds(133, 86, 86, 20);
		tfLowerFitThreshold.setText(Integer.toString(ini.thres3));
		contentPanel.add(tfLowerFitThreshold);
		
		tfUpperFitThreshold = new JTextField();
		tfUpperFitThreshold.setColumns(10);
		tfUpperFitThreshold.setBounds(133, 111, 86, 20);
		tfUpperFitThreshold.setText(Integer.toString(ini.thres4));
		contentPanel.add(tfUpperFitThreshold);
		
		tfLowerFlagThreshold = new JTextField();
		tfLowerFlagThreshold.setColumns(10);
		tfLowerFlagThreshold.setBounds(133, 136, 86, 20);
		tfLowerFlagThreshold.setText(Integer.toString(ini.LowerFlagThres));
		contentPanel.add(tfLowerFlagThreshold);
		
		tfUpperFlagThreshold = new JTextField();
		tfUpperFlagThreshold.setColumns(10);
		tfUpperFlagThreshold.setBounds(133, 161, 86, 20);
		tfUpperFlagThreshold.setText(Integer.toString(ini.UpperFlagThres));
		contentPanel.add(tfUpperFlagThreshold);
		
		tfFluxslope = new JTextField();
		tfFluxslope.setColumns(10);
		tfFluxslope.setBounds(372, 33, 86, 20);
		tfFluxslope.setText(Double.toString(ini.fluxslope));
		contentPanel.add(tfFluxslope);
		
		JLabel lblFluxslope = new JLabel("Fluxslope:");
		lblFluxslope.setBounds(249, 36, 113, 14);
		contentPanel.add(lblFluxslope);
		
		JLabel lblFluxoffset = new JLabel("Fluxoffset:");
		lblFluxoffset.setBounds(249, 61, 113, 14);
		contentPanel.add(lblFluxoffset);
		
		tfFluxoffset = new JTextField();
		tfFluxoffset.setColumns(10);
		tfFluxoffset.setBounds(372, 58, 86, 20);
		tfFluxoffset.setText(Double.toString(ini.fluxoffset));
		contentPanel.add(tfFluxoffset);
		
		JLabel lblSolidangle = new JLabel("Solidangle: ");
		lblSolidangle.setBounds(249, 86, 113, 14);
		contentPanel.add(lblSolidangle);
		
		tfSolidangle = new JTextField();
		tfSolidangle.setColumns(10);
		tfSolidangle.setBounds(372, 83, 86, 20);
		tfSolidangle.setText(Double.toString(ini.solidangle));
		contentPanel.add(tfSolidangle);
		
		JLabel lblDisequilibriumFactor = new JLabel("Disequilibriumfactor:");
		lblDisequilibriumFactor.setBounds(249, 111, 113, 14);
		contentPanel.add(lblDisequilibriumFactor);
		
		tfDisequilibriumfactor = new JTextField();
		tfDisequilibriumfactor.setColumns(10);
		tfDisequilibriumfactor.setBounds(372, 108, 86, 20);
		tfDisequilibriumfactor.setText(Double.toString(ini.disequilibrium));
		contentPanel.add(tfDisequilibriumfactor);
		
		tfHoentzsch = new JTextField();
		tfHoentzsch.setColumns(10);
		tfHoentzsch.setBounds(612, 36, 86, 20);
		tfHoentzsch.setText(Integer.toString(ini.Hoen));
		contentPanel.add(tfHoentzsch);
		
		JLabel lblHoentzsch = new JLabel("Hoentzsch:");
		lblHoentzsch.setBounds(489, 39, 113, 14);
		contentPanel.add(lblHoentzsch);
		
		JLabel lblIntervals = new JLabel("Interval[s]:");
		lblIntervals.setBounds(489, 64, 113, 14);
		contentPanel.add(lblIntervals);
		
		tfInterval = new JTextField();
		tfInterval.setColumns(10);
		tfInterval.setBounds(612, 61, 86, 20);
		tfInterval.setText(Integer.toString(ini.invl));
		contentPanel.add(tfInterval);
		
		JLabel lblEdgeoffset = new JLabel("Edgeoffset:");
		lblEdgeoffset.setBounds(489, 89, 113, 14);
		contentPanel.add(lblEdgeoffset);
		
		tfEdgeoffset = new JTextField();
		tfEdgeoffset.setColumns(10);
		tfEdgeoffset.setBounds(612, 86, 86, 20);
		tfEdgeoffset.setText(Integer.toString(ini.Edgeoffset));
		contentPanel.add(tfEdgeoffset);
		
		JLabel lblIpaddress = new JLabel("IP-Adress:");
		lblIpaddress.setBounds(489, 114, 113, 14);
		contentPanel.add(lblIpaddress);
		
		tfIPAdress = new JTextField();
		tfIPAdress.setColumns(10);
		tfIPAdress.setBounds(612, 111, 86, 20);
		tfIPAdress.setText(ini.IP);
		contentPanel.add(tfIPAdress);
		
		JLabel lblFluxchannel = new JLabel("Fluxchannel:");
		lblFluxchannel.setBounds(489, 139, 113, 14);
		contentPanel.add(lblFluxchannel);
		
		tfFluxchannel = new JTextField();
		tfFluxchannel.setColumns(10);
		tfFluxchannel.setBounds(612, 136, 86, 20);
		tfFluxchannel.setText(Integer.toString(ini.Fluxchannel));
		contentPanel.add(tfFluxchannel);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(tfFluxchannel, popupMenu);
		
		JLabel lblFillMissingValues = new JLabel("Fill missing values in activity file?");
		lblFillMissingValues.setBounds(249, 139, 171, 14);
		contentPanel.add(lblFillMissingValues);
		
		JCheckBox chckbxfillup = new JCheckBox("");
		chckbxfillup.setBounds(437, 136, 21, 23);
		chckbxfillup.setSelected(ini.fill != 0);
		contentPanel.add(chckbxfillup);
		
		JLabel lblWith = new JLabel("with");
		lblWith.setBounds(249, 164, 209, 14);
		contentPanel.add(lblWith);
		
		tfFiller = new JTextField();
		tfFiller.setText("NaN");
		tfFiller.setColumns(10);
		tfFiller.setBounds(372, 161, 86, 20);
		tfFiller.setText(ini.filler);
		contentPanel.add(tfFiller);
		
		JLabel lblUsedIniFile = new JLabel("used ini file:");
		lblUsedIniFile.setBounds(10, 198, 75, 14);
		contentPanel.add(lblUsedIniFile);
		
		tfIniFilePath = new JLabel();
		tfIniFilePath.setBounds(79, 195, 368, 20);
		contentPanel.add(tfIniFilePath);
		if (ini._pathToIniFile == null) {tfIniFilePath.setText("no ini file was found or it cannot be loaded");
		} else {tfIniFilePath.setText(""+ini._pathToIniFile);			
		}
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(456, 194, 89, 23);
		contentPanel.add(btnBrowse);
				
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//home use
		        final JFileChooser fileDialog = new JFileChooser("C:\\Users\\Max\\Desktop\\zu Auswerten\\Ini Files\\");
				//Create a file chooser
		        //final JFileChooser fileDialog = new JFileChooser();
		        //name of the filechooser window
		        fileDialog.setDialogTitle("Choose ini file (.ini):");
		        //only show not hidden files
		        fileDialog.setFileHidingEnabled(true);		        
		       
		        //to select single file
		        fileDialog.setMultiSelectionEnabled(false);
		        //In response to a button click:
		        int option = fileDialog.showOpenDialog(null);
		        
		        //defined globally
		        if(option == JFileChooser.APPROVE_OPTION) {
		        	
		            /*final*/ File file = fileDialog.getSelectedFile();
		        	System.out.println("get selected files");
		            if (file != null){
	               		//passing file to the ini method
	               		try {
	               			ini.loadIniFile(file); //= new iniFile(file);
	               			dispose();
	               			EvalIniDialog dialog = new EvalIniDialog(ini);
	               			dialog.setVisible(true);
	               			System.out.println("Trying to refresh the window");
	               			
	               		} catch (Exception e) {// somewhere here is an error, I don't know where
	               			// TODO Auto-generated catch block
	               			System.out.println("Something went wrong, maybe the file is already opened?");
	               			e.printStackTrace();
	               		}
		            }
		            //here should we some how refresh the values of the eval setting 
		            //but we cannot use this method directly, because it initiates the iniFile()
		            //Ideas?
		         } else if (option == JFileChooser.CANCEL_OPTION){
		             System.out.println("User cancelled operation. No file was chosen.");  
		         }
			
				
			}
		});
		
		JLabel lblRawDataFolder = new JLabel("Folder with raw data (lvl0):");
		lblRawDataFolder.setBounds(10, 223, 171, 16);
		contentPanel.add(lblRawDataFolder);
		
		JTextField tfRawDataPath = new JTextField();
		tfRawDataPath.setBounds(10, 240, 368, 16);
		tfRawDataPath.setText(ini.lvl0);
		contentPanel.add(tfRawDataPath);	
		
		JLabel lblAutomaticDataFolder = new JLabel("Folder with automatically browsed data (lvl1):");
		lblAutomaticDataFolder.setBounds(10, 257, 301, 16);
		contentPanel.add(lblAutomaticDataFolder);	
		
		JTextField tfAtomaticDataPath = new JTextField();
		tfAtomaticDataPath.setBounds(10, 274, 368, 16);
		tfAtomaticDataPath.setText(ini.lvl1);
		contentPanel.add(tfAtomaticDataPath);
		
		JLabel lblBrowsedDataFolder = new JLabel("Folder with manually browsed data (lvl2):");
		lblBrowsedDataFolder.setBounds(10, 292, 301, 16);
		contentPanel.add(lblBrowsedDataFolder);	
		
		JTextField tfBrowsedDataPath = new JTextField();
		tfBrowsedDataPath.setBounds(10, 308, 368, 16);
		tfBrowsedDataPath.setText(ini.lvl2);
		contentPanel.add(tfBrowsedDataPath);
		
		JLabel lblextractFileFolder = new JLabel("Folder to save extract (.txt) file:");
		lblextractFileFolder.setBounds(10, 327, 301, 16);
		contentPanel.add(lblextractFileFolder);	
		
		JTextField tfextractFileFolder = new JTextField();
		tfextractFileFolder.setBounds(10, 342, 368, 16);
		tfextractFileFolder.setText(ini.extractFileFolder);
		contentPanel.add(tfextractFileFolder);

		JLabel lblactivityFileFolder = new JLabel("Folder to save activity (.act) file:");
		lblactivityFileFolder.setBounds(10, 362, 301, 16);
		contentPanel.add(lblactivityFileFolder);	
		
		JTextField tfactivityFileFolder = new JTextField();
		tfactivityFileFolder.setBounds(10, 376, 368, 16);
		tfactivityFileFolder.setText(ini.activityFileFolder);
		contentPanel.add(tfactivityFileFolder);
		
		JLabel lblFluxthreshold = new JLabel("Fluxthreshold:");
		lblFluxthreshold.setBounds(249, 11, 113, 14);
		contentPanel.add(lblFluxthreshold);
		
		tfFluxthreshold = new JTextField();
		tfFluxthreshold.setBounds(372, 8, 86, 20);
		contentPanel.add(tfFluxthreshold);
		tfFluxthreshold.setColumns(10);
		tfFluxthreshold.setText(Double.toString(ini.fluxthreshold));
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Save");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//Save Evaluation Settings and close Dialog
						try {
							ini.overwriteIniFile(ini._pathToIniFile, tfMonitorID, tfNoiseThreshold, tfWindowThreshold, tfLowerFitThreshold, tfUpperFitThreshold, tfLowerFlagThreshold, 
									tfUpperFlagThreshold, tfFluxslope, tfFluxoffset, tfFluxthreshold, tfSolidangle, tfDisequilibriumfactor, tfHoentzsch, tfInterval, tfEdgeoffset, tfIPAdress, tfFluxchannel, tfFiller, chckbxfillup,
									tfRawDataPath, tfAtomaticDataPath, tfBrowsedDataPath, tfextractFileFolder, tfactivityFileFolder
									);
							dispose();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, "Settings could not be saved. Maybe the file is already opened?", "RSave settings", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				});
				okButton.setActionCommand("Save");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}