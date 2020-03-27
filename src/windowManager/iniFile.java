package windowManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jfree.chart.ChartPanel;

public class iniFile {
	
	//Hard coded Standardvalues if no *.ini file was found
	public String version = "Radmon 2.0";
	public int port = 1;
	public int thres1 = 15;                
	public int thres2 = 25;                 
	public int thres3 = 50;
	public int thres4 = 50;
	public int thres5 = 50;
	public int thres6 = 50;
	public int thres7 = 35;
	public int thres8 = 105;
	public int invl = 1800;
	public int Edgeoffset = 96;
	public double fluxslope = 1.0;             
	public double fluxoffset = 0;
	public double fluxthreshold = 0;
	public String id = "1_HD";
	public String decimalchr = ".";
	public double solidangle = 0.2650;          
	public double disequilibrium = 1.0;     
	public int Hoen = 0;                    
	public double HDHoenfluxoffset = 0;
	public double HDHoenfluxslope = 1;
    public double HoenTempOffset = 0;       
    public double HoenTempSlope = 1;           
    public double HoenPressOffset = 0;        
    public double HoenPressSlope = 1;          
	public double HoenTempChannel = 2;         
	public double HoenFluxChannel = 0;         
	public String IP = "192.168.0.20";
	public int Fluxchannel = 1;
	public int LowerFlagThres=1;
	public int UpperFlagThres=2;
	public File _pathToIniFile;
	//decide wether to fill up the activity file for missing values
	//0=false
	public int fill=0;
	public String filler = "NaN";
	//added default values for continue evaluation button
	public String lvl0 = "./lvl0/";
	public String lvl1 = "./lvl1/";
	public String lvl2 = "./lvl2/";
	public String extractFileFolder = lvl2;
	public String activityFileFolder = lvl2;
	
	//constructor without arguments if no file was found
	public iniFile () {
		_pathToIniFile = getINIFile();
		if (_pathToIniFile == null) {
			//no ini File found -> take standard values

		} else {
			//get values of the file
			try {
				loadIniFile(_pathToIniFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Could not open " + _pathToIniFile + ". Maybe it has been deleted?");
				e.printStackTrace();
			}
		}
	}
	
	public iniFile(File selectedIniFile) {
		
   		if(selectedIniFile.getName().endsWith(".ini")) {
   			System.out.println("You have selected this ini file:\n" +selectedIniFile);
   			} else {
		        JOptionPane.showMessageDialog(null, "You have to selected ini file (.ini)!", "Error loading ini file", JOptionPane.ERROR_MESSAGE);
   			return;
   			}
   		try {
			loadIniFile(selectedIniFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not open " + selectedIniFile + ". Maybe it has been deleted?");
			e.printStackTrace();
		}
	}
	
	
	public File getINIFile () {
		//get the ini file of the working directory if possible
		File file;
		try {
			file = new File(RnLog.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		// get all the files from a directory
		////////////////////////////////////////////////////////
		//IMPORTANTIMPORTANTIMPORTANTIMPORTANTIMPORTANTIMPORTANT
		////////////////////////////////////////////////////////
		//Add .getParentFile() for exporting, otherwise .jar does not find the ini
		//File[] fList = file.listFiles();
		File[] fList = file.getParentFile().listFiles();
		
		for (int i = 0; i< fList.length; i++) {
			if(fList[i].getName().endsWith(".ini")) {
				_pathToIniFile = fList[i];
				System.out.println("iniFile loaded: " +  _pathToIniFile);
				return _pathToIniFile;
			}
		}
		System.out.println("no ini file found");
		return null;
	}
	

	public void loadIniFile (File file) throws IOException {
		//read .ini file line by line and take written values
		FileReader fileReader;
		fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
        //only get lines which are not empty or commentaries
        while ((line = bufferedReader.readLine()) != null) {
        	if(line.replace(" ", "").startsWith("rem") || line.replace(" ", "").startsWith(";") || line.replace(" ", "").isEmpty() || line.replace(" ", "").startsWith("[init]")) {
        		//line starts with commentary mark or is empty
                continue;
        	}
        	lines.add(line.split(";")[0]);
        }
    	/*for(int i=0; i< lines.size(); i++) {
        	System.out.println(lines.get(i));
    	}*/
        bufferedReader.close();
        
        this._pathToIniFile = file;
        
        //markers for the presence of folder paths
        boolean isExtFolderPresent = false;
        boolean isActFolderPresent = false;
        
        //save values in this class
        for (int i=0; i<lines.size(); i++) {
        	switch (lines.get(i).split("=")[0].trim()) {
        		case "port": port = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "thres1": thres1 = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "thres2": thres2 = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "thres3": thres3 = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "thres4": thres4 = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "thres5": thres5 = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "thres6": thres6 = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "thres7": thres7 = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "thres8": thres8 = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "invl": invl = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "Edgeoffset": Edgeoffset = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "fluxslope": fluxslope = Double.parseDouble(lines.get(i).split("=")[1]);break;
        		case "fluxoffset": fluxoffset = Double.parseDouble(lines.get(i).split("=")[1]);break;
        		case "fluxthreshold": fluxthreshold = Double.parseDouble(lines.get(i).split("=")[1]);break;
        		case "id": id = lines.get(i).split("=")[1];break; 
        		case "decimalchr": decimalchr = lines.get(i).split("=")[1];break;
        		case "solidangle": solidangle = Double.parseDouble(lines.get(i).split("=")[1]);break;
        		case "disequilibrium": disequilibrium = Double.parseDouble(lines.get(i).split("=")[1]);break;
        		case "Hoen": Hoen = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "HDHoenfluxoffset": HDHoenfluxoffset = Double.parseDouble(lines.get(i).split("=")[1]);break;
        		case "HDHoenfluxslope": HDHoenfluxslope = Double.parseDouble(lines.get(i).split("=")[1]);break;
        		case "HoenTempOffset": HoenTempOffset = Double.parseDouble(lines.get(i).split("=")[1]);break;
        		case "HoenTempSlope": HoenTempSlope = Double.parseDouble(lines.get(i).split("=")[1]);break;
        		case "HoenPressOffset": HoenPressOffset = Double.parseDouble(lines.get(i).split("=")[1]);break;
        		case "HoenPressSlope": HoenPressSlope = Double.parseDouble(lines.get(i).split("=")[1]);break;
        		case "HoenTempChannel": HoenTempChannel = Double.parseDouble(lines.get(i).split("=")[1]);break;
        		case "HoenFluxChannel": HoenFluxChannel = Double.parseDouble(lines.get(i).split("=")[1]);break;
        		case "IP": IP = lines.get(i).split("=")[1];break;
        		case "Fluxchannel": Fluxchannel = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "LowerFlagThres": LowerFlagThres = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "UpperFlagThres": UpperFlagThres = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "fill": fill = Integer.parseInt(lines.get(i).split("=")[1]);break;
        		case "filler":
        			try{
        				filler = lines.get(i).split("=")[1];break;
        			} catch(ArrayIndexOutOfBoundsException e) {
        				//user has chosen an empty string as filler parameter
        				filler = "";
        				break;
        			}
        		//added for continue evaluation
        		case "lvl0": lvl0 = lines.get(i).trim().split("=")[1];break;
        		case "lvl1": lvl1 = lines.get(i).trim().split("=")[1];break;
        		case "lvl2": lvl2 = lines.get(i).trim().split("=")[1];break;
        		case "extractFileFolder": {extractFileFolder = lines.get(i).trim().split("=")[1];
        								   isExtFolderPresent =true;break;
        		}
        		case "activityFileFolder": {activityFileFolder = lines.get(i).trim().split("=")[1];
        									isActFolderPresent= true; break;
        		}
        		default: break;
        	}
        }
        //by default if no path for the extract and activity file is given it should be saved in the lvl2 directory
        if (!isExtFolderPresent) {
        	extractFileFolder = lvl2;
        }
        if (!isActFolderPresent) {
        	activityFileFolder = lvl2;
        }
	}
	
	public void overwriteIniFile(File file, JTextField tfMonitorID, JTextField tfNoiseThreshold, JTextField tfWindowThreshold, JTextField tfLowerFitThreshold, JTextField tfUpperFitThreshold, JTextField tfLowerFlagThreshold, 
			JTextField tfUpperFlagThreshold, JTextField tfFluxslope, JTextField tfFluxoffset, JTextField tfFluxthreshold, JTextField tfSolidangle, JTextField tfDisequilibriumfactor, JTextField tfHoentzsch, JTextField tfInterval, JTextField tfEdgeoffset, JTextField tfIPAdress, 
			JTextField tfFluxchannel, JTextField tfFiller, JCheckBox chckbxfillup, JTextField tfRawDataPath, JTextField tfAtomaticDataPath, JTextField tfBrowsedDataPath, JTextField tfextractFileFolder, JTextField tfactivityFileFolder) throws Exception, IOException {
		

			
			//go through every value of the window and check if its the same as in the ini file
			try {
				
				this.id = tfMonitorID.getText();
				this.thres1 = Integer.parseInt(tfNoiseThreshold.getText());
				this.thres2 = Integer.parseInt(tfWindowThreshold.getText());
				this.thres3 = Integer.parseInt(tfLowerFitThreshold.getText());
				this.thres4 = Integer.parseInt(tfUpperFitThreshold.getText());
				this.LowerFlagThres = Integer.parseInt(tfLowerFlagThreshold.getText());
				this.UpperFlagThres = Integer.parseInt(tfUpperFlagThreshold.getText());
				
			    /* not used in the old radon program ?
			    this.thres5 = Integer.parseInt(tfLowerFlagThreshold.getText());
				this.thres6 = Integer.parseInt(tfUpperFlagThreshold.getText());
				this.thres7 = Integer.parseInt(tfFluxslope.getText());
				this.thres8 = Integer.parseInt(tfFluxoffset.getText());
			    */
			
				this.fluxslope = Double.parseDouble(tfFluxslope.getText());
				this.fluxoffset = Double.parseDouble(tfFluxoffset.getText());
				this.fluxthreshold = Double.parseDouble(tfFluxthreshold.getText());
				this.solidangle = Double.parseDouble(tfSolidangle.getText());
				this.disequilibrium = Double.parseDouble(tfDisequilibriumfactor.getText());
				this.Hoen = Integer.parseInt(tfHoentzsch.getText());
				this.invl = Integer.parseInt(tfInterval.getText());
				this.Edgeoffset = Integer.parseInt(tfEdgeoffset.getText());
				this.IP = tfIPAdress.getText();
				this.Fluxchannel = Integer.parseInt(tfFluxchannel.getText());
				this.filler = tfFiller.getText();
			
				if (chckbxfillup.isSelected()) {
					this.fill = 1;
				} else {
					this.fill = 0;
				}
				this.lvl0 = tfRawDataPath.getText();
				this.lvl1 = tfAtomaticDataPath.getText();
				this.lvl2 = tfBrowsedDataPath.getText();
				this.extractFileFolder = tfextractFileFolder.getText();
				this.activityFileFolder = tfactivityFileFolder.getText();
				
			} catch (Exception wrongInput) {
				wrongInput.printStackTrace();
				JOptionPane.showMessageDialog(null, "File NOT saved. The value entered has the wrong type."/*, JOptionPane.INFORMATION_MESSAGE*/);
			}
			
			if (_pathToIniFile==null) {
				//no file -> create new one
				System.out.println("no ini file found -> saving new one in the folder with RnLog*.*.jar");
				try {
					//creating new ini file in the current directory
					_pathToIniFile = new File("RnLog.ini");
					createNewFile(_pathToIniFile);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Could not create a new one. Maybe you have no writing permission?"/*, JOptionPane.INFORMATION_MESSAGE*/);
					e.printStackTrace();
					return;
				}
				
			} else {
				System.out.println("overwriting existing ini file");
				createNewFile(_pathToIniFile);
			}
	}
	/*
	private void findAndReplace(File file, String variable, String newValue) throws IOException {
		boolean found = false;
		//finds a variable, e.g. disequilibriumfactor, in the ini file and replaces it with a new value
		//type checking must be done before
		if(file==null) {
			System.out.println("file not found");
			return;
		}
		//read .ini file line by line
		FileReader fileReader;
		fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
        //only get lines which are not empty or commentaries
        while ((line = bufferedReader.readLine()) != null) {
        	if(line.replace(" ", "").startsWith("rem") || line.replace(" ", "").startsWith(";") || line.replace(" ", "").isEmpty() || line.replace(" ", "").startsWith("[init]")) {
        		//line starts with commentary mark or is empty
        		lines.add(line);
                continue;
        	}
        	if(line.replace(" ", "").split("=")[0].equals(variable)) {
        		//found searched line
        		lines.add(variable + "=" + newValue);
        		System.out.println(variable + " changed to " + newValue);
        		found = true;
        		continue;
        	}
        	lines.add(line);
        }
        bufferedReader.close();
        
        FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath());
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOut));
    	for (int i=0; i<lines.size(); i++) {
    		bw.write(lines.get(i)  + "\r\n");
    		System.out.println(lines.get(i)  + "\r\n");
    	}
    	if(!found) {
    		bw.write(variable + "=" + newValue);    		
    		System.out.println(variable + " saved to ini file as " + newValue);
    	}
    	bw.close();
	}
     */   
	
	//saving new edgeOffset (after the edge to the refSpectrum was set)
	public void saveNewEdgeoffsest(Integer newEdgeoffset) {
		this.Edgeoffset = newEdgeoffset;
		System.out.println("overwriting existing ini file");
		try {
			createNewFile(_pathToIniFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private void createNewFile (File pathToIniFile) throws Exception {
		//create a new ini file out of standard values
		File newIni = null;
		try {
			newIni = pathToIniFile;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//if(newIni.getAbsolutePath()==null) {
		//	throw new Exception();
		//}
        FileOutputStream fileOut = new FileOutputStream(pathToIniFile);
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOut));
        bw.write("port=" + port + "\r\n");
        bw.write("thres1=" + thres1 + "\r\n");
        bw.write("thres2=" + thres2 + "\r\n");
        bw.write("thres3=" + thres3 + "\r\n");
        bw.write("thres4=" + thres4 + "\r\n");
        bw.write("thres5=" + thres5 + "\r\n");
        bw.write("thres6=" + thres6 + "\r\n");
        bw.write("thres7=" + thres7 + "\r\n");
        bw.write("thres8=" + thres8 + "\r\n");
        bw.write("invl=" + invl + "\r\n");
        bw.write("Edgeoffset=" + Edgeoffset + "\r\n");
        bw.write("fluxslope=" + fluxslope + "\r\n");
        bw.write("fluxthreshold=" + fluxthreshold + "\r\n");
        bw.write("id=" + id + "\r\n");
        bw.write("decimalchr=" + decimalchr + "\r\n");
        bw.write("solidangle=" + solidangle + "\r\n");
        bw.write("disequilibrium=" + disequilibrium + "\r\n");
        bw.write("Hoen=" + Hoen + "\r\n");
        bw.write("HDHoenfluxoffset=" + HDHoenfluxoffset + "\r\n");
        bw.write("HDHoenfluxslope=" + HDHoenfluxslope + "\r\n");
        bw.write("HoenTempOffset=" + HoenTempOffset + "\r\n");
        bw.write("HoenTempSlope=" + HoenTempSlope + "\r\n");
        bw.write("HoenPressOffset=" + HoenPressOffset + "\r\n");
        bw.write("HoenTempChannel=" + HoenTempChannel + "\r\n");
        bw.write("HoenFluxChannel=" + HoenFluxChannel + "\r\n");
        bw.write("IP=" + IP + "\r\n");
        bw.write("Fluxchannel=" + Fluxchannel + "\r\n");
        bw.write("LowerFlagThres=" + LowerFlagThres + "\r\n");
        bw.write("UpperFlagThres=" + UpperFlagThres + "\r\n");
        bw.write("fill=" + fill + "\r\n");
        bw.write("filler=" + filler + "\r\n");
        bw.write("lvl0=" + lvl0 + "\r\n");
        bw.write("lvl1=" + lvl1 + "\r\n");
        bw.write("lvl2=" + lvl2 + "\r\n");
        bw.write("extractFileFolder=" + extractFileFolder + "\r\n");
        bw.write("activityFileFolder=" + activityFileFolder + "\r\n");
        bw.close();
        fileOut.close();
        this._pathToIniFile = newIni.getAbsoluteFile();
        }
}