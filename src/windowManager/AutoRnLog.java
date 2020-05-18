package windowManager;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
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

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AutoRnLog extends JPanel {

	
	//Creating the panel	
	private static JTextArea taskOutput;
	public static String SoftwareVersion = "AutoRnLog 0.1";
	
	
	public AutoRnLog() {
		
		taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
        
        JPanel panel = new JPanel();
        
        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
	}
	
	//getting all files in the folder, including all subfolders; recursive
	public static void listOfAllFiles(String directoryName, ArrayList<File> files, ArrayList<String> fileNames) {
	    File directory = new File(directoryName);

	    // Get all files from a directory.
	    File[] fList = directory.listFiles();
	    if(fList != null)
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
	
	//check if a given string has a proper filename for a spectra
	public static boolean checkFilename(String Filename) {		
		if(Filename.length()==12) {
			if(Filename.contains(".R")) {
				return true;
			}
		}
		return false;
	}
	
	//helper function which gets the Date Time of a String (split it at ";" and convert to timeformat
	//and returns a list of Strings with timestamps between
	public static ArrayList<String> getDateTimeBetween ( String DT1, String DT2) throws ParseException{		
		ArrayList<String> results = new ArrayList<String>();
		
		//time format used in the spectra
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
	
	//calculation of the activity using the Stockburger method
	public static ArrayList<String> calcStockburger(ArrayList<String> extLines,  int points, iniFile ini) {		
				
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

	//Function to write a log file and close the frame
    private static void writingLogAndClosing(JFrame frame, iniFile ini) {//creating log file for the current run
        //log file is saved in the lvl1 dir
    	File lvl1Dir = new File(ini.lvl1);
        File logFile = new File(ini.lvl1 + "\\LogFile_" + lvl1Dir.getName() + ".txt");
        FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(logFile, true);
			//--------------------------------------^^^^ means append new line, don't override old data
			BufferedWriter extractWriter = new BufferedWriter(new OutputStreamWriter(fileOut));				
			extractWriter.write(taskOutput.getText());
			extractWriter.close();
		    fileOut.close();
		    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		} catch (FileNotFoundException e) {
			System.out.println("Something went wrong!\n\n");
			System.out.println("\nExiting evaluation\n");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Something went wrong!\n\n");
			System.out.println("\nExiting evaluation\n");
			e.printStackTrace();
		}		
	    return;
    }
 
    public static void main(String[] args) {
            	
    	//initialize a gui window with log text
    	//Create and set up the window.
        JFrame frame = new JFrame("AutoRnLog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        JComponent newContentPane = new AutoRnLog();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        //time format used in the spectra
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss");
		
		taskOutput.append(LocalDateTime.now().format(timeFormat).toString() + ": Starting the evaluation\n");
		
        taskOutput.append("Loading ini file...");
        
        //creating log file for the case if no ini file can be loaded
        //it is created in the folder with .jar file
        iniFile ini;
        try {
        	ini = new iniFile(new File(args[0]));
        } catch (Exception e) {
        	taskOutput.append("Ini file cannot be loaded\n");
			taskOutput.append("Exiting evaluation\n");
			File logFile = new File("LogFile_NO_INI_FILE.txt");
	        FileOutputStream fileOut;
			try {
				fileOut = new FileOutputStream(logFile, true);
				//--------------------------------------^^^^ means append new line, don't override old data
				BufferedWriter extractWriter = new BufferedWriter(new OutputStreamWriter(fileOut));	
				extractWriter.write(taskOutput.getText());
				extractWriter.close();
			    fileOut.close();
			    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			} catch (FileNotFoundException e2) {
				System.out.println("Something went wrong!\n\n");
				System.out.println("\nExiting evaluation\n");
				e2.printStackTrace();
				
			} catch (IOException e2) {
				System.out.println("Something went wrong!\n\n");
				System.out.println("\nExiting evaluation\n");
				e2.printStackTrace();
			}	
			return;                	
        }
        
        taskOutput.append("done\n");
        
        taskOutput.append("Getting list of files to evaluate...");
        
        //checking if the lvl1 folder exists and creating if not
        File lvl1Dir = new File(ini.lvl1);                
		if (lvl1Dir.exists()) {
			System.out.println(lvl1Dir + " exists");
		} else {
			boolean folderCreated = lvl1Dir.mkdirs();
			if (folderCreated) {
				
				//new folder meaning new month meaning have to use new ref spec
				//cycling over all files in the ini file folder to find current ref spec
				File iniFileFolder = new File(ini._pathToIniFile.getParent()); 
                File[] iniFolderList = iniFileFolder.listFiles();
                
                for (int i = 0; i < iniFolderList.length; i++) {
                	//file found -> move it to the current lvl0 folder
					if (iniFolderList[i].getName().endsWith(".ref")) {	
						try {
							copyFile(iniFolderList[i], new File(ini.lvl0+"\\" + iniFolderList[i].getName()));
						} catch (IOException e) {
							taskOutput.append("\nSomething went wrong!\n\n");
		    				taskOutput.append(e.toString());
	        				continue;
						}
						iniFolderList[i].delete();
						break;    							
					} 
				}  				
			} else {
				//major point, cannot continue without, exiting application
				taskOutput.append(lvl1Dir + " cannot be created\n");
				taskOutput.append("Exiting evaluation\n");
				writingLogAndClosing(frame, ini);
				return;
			}
		}        		
		//loading lists of ALL files and filenames in the lvl1 dir
        ArrayList<File> lvl1Files = new ArrayList<File>();
        ArrayList<String> lvl1FileNames = new ArrayList<String>();
        listOfAllFiles(ini.lvl1, lvl1Files, lvl1FileNames);
        
        //loading lists of ALL files and filenames in the lvl0 dir
        File lvl0Dir = new File(ini.lvl0);
        ArrayList<File> lvl0Files = new ArrayList<File>();
        ArrayList<String> lvl0FileNames = new ArrayList<String>();
        listOfAllFiles(ini.lvl0, lvl0Files, lvl0FileNames);
        
        //removing all entries from the lvl0 filename list that already exist in lvl1 dir
        //it is assumed that these files are already evaluated
        lvl0FileNames.removeAll(lvl1FileNames);
        
        //exiting if no new files are found (1 file should be refSpec and cannot be evaluated by the routine anyway)
        if (lvl0FileNames.size() < 2) {
        	taskOutput.append("done\nNo new files found!\n\n");
        	taskOutput.append(args[0]);
        	writingLogAndClosing(frame, ini);
        	return;
        }
        
        taskOutput.append("done.\n");
        
        taskOutput.append("Loading and copying spectra...");
        //initialization of the RefSPec using the hard coded data
        Spectra RefSpec = new Spectra();
        
        //creating the spectraList to evaluate
        ArrayList<Spectra> spectraList = new ArrayList<Spectra>();
        
        //counters for final log
        int deleteCount = 0;
        int brokenCount = 0;
        int lowFlowCount = 0;
        int lowLTCount = 0;
        
        for (int i = 0; i < lvl0FileNames.size(); i++) {
			
        	//check if the file is a reference spectrum
        	//loading it
			if (lvl0FileNames.get(i).endsWith(".ref")) {
				try {
					System.out.println("Reference spectrum found: " + ini.lvl0 + lvl0FileNames.get(i));
					File refSpecFile = new File(ini.lvl0 + "\\" + lvl0FileNames.get(i)); 
					RefSpec = new Spectra(refSpecFile.getName(), refSpecFile);								
				} catch (Exception e) {
					//major point, cannot continue without, exiting application
					taskOutput.append("could not load " + lvl0FileNames.get(i) + " as reference spectrum\n");
					taskOutput.append(e.toString());
					taskOutput.append("\nExiting evaluation\n");
					writingLogAndClosing(frame, ini);
                	return;
				}
			//check if the file is a spectra
			//if not delete from the list
			} else if (!checkFilename(lvl0FileNames.get(i))) {						
				lvl0FileNames.remove(i);
				i--;
				
			} else {
				try {
					//moving file to lvl1 dir and recognizing it as spectrum
					File currentSpectrum = new File(ini.lvl0 + "\\" + lvl0FileNames.get(i));
					File destinationSpectrum = new File(ini.lvl1 + "\\" + lvl0FileNames.get(i));
    				copyFile(currentSpectrum, destinationSpectrum);
    				spectraList.add(new Spectra(destinationSpectrum.getName(), destinationSpectrum));
    				
    				//TODO delete empties completely
    				//moving empty spectra file  to delete folder
    				if (spectraList.get(i).linesCount < 2) {
    					System.out.print(destinationSpectrum.getName() + " is empty. Trying to remove.");
    					new File(ini.lvl1+ "\\delete").mkdirs();
    					copyFile(destinationSpectrum, new File(ini.lvl1 + "\\delete\\"+ destinationSpectrum.getName()));			
    					destinationSpectrum.delete();
    					lvl0FileNames.remove(i);
	    				spectraList.remove(i);
	    				i--;
	    				deleteCount++;
	    				continue;
    				}
    				
    				//moving broken spectra to broken folder
    				if (spectraList.get(i).linesCount < 135) {
    					System.out.print(destinationSpectrum.getName() + " is broken. Trying to remove.");
    					new File(ini.lvl1+ "\\broken").mkdirs();
    					copyFile(destinationSpectrum, new File(ini.lvl1 + "\\broken\\"+ destinationSpectrum.getName()));			
    					destinationSpectrum.delete();
    					lvl0FileNames.remove(i);
	    				spectraList.remove(i);
	    				i--;
	    				brokenCount++;
	    				continue;
    				}
    				
					//checking if the flux is higher than the flux threshold and moving them to lowFlux subfolder
					if (spectraList.get(i).ADC1 < ini.fluxthreshold) {
						System.out.println("Flux is too low: " + spectraList.get(i).ADC1 + ". Trying to remove.");
    					new File(ini.lvl1+ "\\lowFlux").mkdirs();
    					copyFile(destinationSpectrum, new File(ini.lvl1 + "\\lowFlux\\"+ destinationSpectrum.getName()));			
    					destinationSpectrum.delete();
    					lvl0FileNames.remove(i);
	    				spectraList.remove(i);
	    				i--;
	    				lowFlowCount++;
	    				continue;
					}
					
					//checking if the LT is higher than 60s and moving them to lowFlux subfolder
					if (spectraList.get(i).LT < 60) {
						System.out.println("LT is too low: " + spectraList.get(i).LT + ". Trying to remove.");
    					new File(ini.lvl1+ "\\lowLT").mkdirs();
    					copyFile(destinationSpectrum, new File(ini.lvl1 + "\\lowLT\\"+ destinationSpectrum.getName()));			
    					destinationSpectrum.delete();
    					lvl0FileNames.remove(i);
	    				spectraList.remove(i);
	    				i--;
	    				lowLTCount++;
	    				continue;
					}
    				
    			} catch (IOException e) {
    				//maybe error concerns only this file, continue evaluation
    				taskOutput.append("Something went wrong!\n\n");
    				taskOutput.append(e.toString());
    				continue;		    				
    			} catch (Exception e2) {
    				//maybe error concerns only this file, continue evaluation
    				taskOutput.append("Something went wrong!\n\n");
    				taskOutput.append(e2.toString());
    				continue;	
				} 						
			}
		}
        
        taskOutput.append("done.\n");
        
        taskOutput.append("Calculating Po edge and flagging spectra...");	            
		//ArrayList for the future extract file
		ArrayList<String> extlines = new ArrayList<String>();
		//counters for final log
		int flaggedCount = 0;
		int goodSpecCount = 0;
		
		//calculating Po edge
		for(int i=0; i<spectraList.size(); i++) {	
			
        	//set edge of spectrum according to reference (if no edge is set yet)
        	try {
				spectraList.get(i).calcEdge(RefSpec, ini.thres3, ini.thres4, ini.Edgeoffset);
			} catch (IOException e) {
				//maybe error concerns only this file, continue evaluation
				taskOutput.append("Something went wrong!\n\n");
				taskOutput.append(e.toString());
				continue;
			}
        	
        	//flag spectra if new edge is outside of the parameters defined in the ini file
        	if(spectraList.get(i).edge > ini.Edgeoffset+ini.UpperFlagThres || spectraList.get(i).edge < ini.Edgeoffset-ini.LowerFlagThres) {
        		
        		//setting edge to the value of the refSpec
        		try {
					spectraList.get(i).setEdge(ini.Edgeoffset);
					File flaggedSpectrum = spectraList.get(i).path;
					spectraList.remove(i);
					i--;
					//moving  Spectra to the new flagged subfolder
	        		new File(flaggedSpectrum.getParent()+ "\\flagged").mkdirs();   
	        		File tmpFlagged = new File(flaggedSpectrum.getParent()+ "\\flagged\\" + flaggedSpectrum.getName());
	        		copyFile(flaggedSpectrum, tmpFlagged);
	        		
	        		//deleting file from the lvl1 directory if flagged
	        		flaggedSpectrum.delete();
	        		flaggedCount++;
					
				} catch (IOException e) {
					//maybe error concerns only this file, continue evaluation
					taskOutput.append("Something went wrong!\n\n");
    				taskOutput.append(e.toString());
    				continue;
				}
        	} else {
        		//saving all related info for the extract file
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
        }	    		
		
		//saving the number of good spectra
		goodSpecCount = extlines.size();
		//sorting entries in the final extract file by the measurement time using insertion algorithm 
        for (int i = 0; i<extlines.size()-1; i++) { 
            String current = extlines.get(i);
			LocalDateTime currentTime =  LocalDateTime.parse(current.split(";")[0], DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss"));				
			int j = i - 1;
			while(j >= 0 &&  currentTime.isBefore(LocalDateTime.parse(extlines.get(j).split(";")[0], DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss"))) ) {
				extlines.set(j+1,extlines.get(j));
			    j--;
			}
			extlines.set(j+1, current);	
        }
        taskOutput.append("done.\n");
        
        taskOutput.append("Writing to the extract file...");
        //creating directories for the extract and activity files
		new File(ini.extractFileFolder).mkdirs();
		new File(ini.activityFileFolder).mkdirs();
		
		//name convention for extract and activity files
		String prefixExtract = lvl0Dir.getName();
		String prefixActivity = lvl0Dir.getName();
		
		//creating new activity and extract files
		File extract = new File(ini.extractFileFolder + "\\" + prefixExtract + ".txt");
		File activity = new File(ini.activityFileFolder + "\\" + prefixActivity + ".act");
		
		//check if extract file is already exist to either add header to new file or not to the existing
        boolean extractExist = extract.exists();
        FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(extract, true);
			//--------------------------------------^^^^ means append new line, don't override old data
			BufferedWriter extractWriter = new BufferedWriter(new OutputStreamWriter(fileOut));
			
			//adding header to the new extract file
			if (!extractExist) {
				extractWriter.write("Date Time; Lifetime;ADC1; StdADC1; T1; StdT1;T2; StdT2;T3; StdT3;Rn1;Rn2;Rn3;Rn4;ADC2; StdADC2; ADC3; StdADC3; Counter1;Counter2;FluxSlope;FluxOffset;ADC2Slope;ADC2Offset;ADC3Slope;ADC3Offset;Temp1Slope;Temp1Offset;Temp2Slope;Temp2Offset;Temp3Slope;Temp3Offset;Counter1Slope;Counter1Offset;Counter2Slope;Counter2Offset;ID \r\n");
			}
			//adding new lines
			for (int i=0; i<extlines.size(); i++) {
				extractWriter.write(extlines.get(i));				
			}
			extractWriter.close();
		    fileOut.close();
		} catch (FileNotFoundException e) {
			//major point, cannot continue without, exiting application
			taskOutput.append("Something went wrong!\n\n");
			taskOutput.append(e.toString());
			taskOutput.append("\nExiting evaluation\n");
			writingLogAndClosing(frame, ini);
			return;
		} catch (IOException e) {
			//major point, cannot continue without, exiting application
			taskOutput.append("Something went wrong!\n\n");
			taskOutput.append(e.toString());
			taskOutput.append("\nExiting evaluation\n");
			writingLogAndClosing(frame, ini);
			return;
		}
		taskOutput.append("done.\n");
		
		taskOutput.append("Calculating activity and writing it to file...");
		
		//clearing extlines to fill with complete month-extract file
		extlines.clear();
		try {
			//reading complete extract file
			FileReader fileReader = new FileReader(extract);
			BufferedReader bufferedReader = new BufferedReader(fileReader);	    		        
	        String line = null;
	        //skipping first (header) line
	        line = bufferedReader.readLine();
	        while ((line = bufferedReader.readLine()) != null) {
	            extlines.add(line);
	            System.out.println(line);
	        }
	        bufferedReader.close();
	        fileReader.close();
		} catch (FileNotFoundException e) {
			//major point, cannot continue without, exiting application
			taskOutput.append("Something went wrong!\n\n");
			taskOutput.append(e.toString());
			taskOutput.append("\nExiting evaluation\n");
			writingLogAndClosing(frame, ini);
			return;
		} catch (IOException e) {
			//major point, cannot continue without, exiting application
			taskOutput.append("Something went wrong!\n\n");
			taskOutput.append(e.toString());
			taskOutput.append("\nExiting evaluation\n");
			writingLogAndClosing(frame, ini);
			return;
		}
        
		
		//split extlines to get rid of duplicates or fill missing values with filler from the ini file
        ArrayList<ArrayList<String>> splittedExtlines = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> splittedActlines = new ArrayList<ArrayList<String>>();
        ArrayList<String> tmpStringList = new ArrayList<String>();
        //getting first line
        tmpStringList.add(extlines.get(0));
        
        for (int i = 1; i< extlines.size(); i++) {
        	
        	LocalDateTime current =  LocalDateTime.parse(extlines.get(i).split(";")[0], DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss"));
            LocalDateTime last = LocalDateTime.parse(extlines.get(i-1).split(";")[0], DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm:ss"));
            long difference = Duration.between(last,current).toMinutes();
            
        	if(difference > 31 ) {
        		//split if the time difference > 31min
        		System.out.println("split, time difference is " + difference + " min");
        		splittedExtlines.add((ArrayList<String>) tmpStringList.clone());
        		tmpStringList.clear();
        		tmpStringList.add(extlines.get(i));
        	}
	        	else if(difference < 1 ) {
	        		//remove if time difference < 1min
	        		System.out.println("remove, time difference is " + difference + " min");
	        		continue;
	        	}
	        	else {
	        		//normal time difference, do nothing
	        		tmpStringList.add(extlines.get(i));
	        	}		        	
        }
        //getting the last section
        splittedExtlines.add((ArrayList<String>) tmpStringList.clone());
        tmpStringList.clear();
		
        //calculating the activity values with Stockburger
        //point to evaluate for the time derivative (?)
        int points = 1;
        for(int x = 0; x < splittedExtlines.size(); x++) {	
        	System.out.println(points);
        	tmpStringList = (ArrayList<String>) calcStockburger(splittedExtlines.get(x), points, ini).clone();
        	if (tmpStringList.get(0) == "") {
        		continue;
        	}
        	//adding section for the future activity file
        	splittedActlines.add((ArrayList<String>) tmpStringList.clone());
        }
        tmpStringList.clear();
        
        //creating the activity file
		try {
			fileOut = new FileOutputStream(activity);
			BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(fileOut));
			//activity header
	        bw1.write("222-Radon activities calculated with " + SoftwareVersion + "\r\n"+ "\r\n");
	        bw1.write("Evaluated by: AutoRnLog on "); 
	        bw1.write(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))); 
	        bw1.write("\r\n");
	        bw1.write("Used parameters \r\n");
	        //used method
	        String Method="Stockburger";
	        bw1.write("Method	    : " + Method + "\r\n");
	        bw1.write("Source File : " + extract.getPath() + "\r\n");
	        bw1.write("Solid Angle : " + String.valueOf(ini.solidangle) + "\r\n");
	        bw1.write("Disequil.   : " + String.valueOf(ini.disequilibrium) + "\r\n");
	        bw1.write("Flux Offset : " + String.valueOf(ini.fluxoffset) + "\r\n");
	        bw1.write("Flux Slope  : " + String.valueOf(ini.fluxslope) + "\r\n"+"\r\n");	
	        bw1.write("Format: \r\n");
	        bw1.write("Stoptime; Activity [Bq/m3]; Ac[dps]; Ac/dt; Total; Window; Edge; temp1[C]; temp2[C]; temp3[C]; Pressure[mbar]; LifeTime[sec]; Flux[m3/s]; ID \r\n");
	        
	        //gather, fuse and fill if variable "fill"  == true
	        Boolean fill = false;
	        if(ini.fill == 1) fill = true;
	        
	        //just write the results into the file if no fill == false or only one block was created
	        if(!fill || splittedActlines.size() == 1) {
		        for(int i=0; i<splittedActlines.size(); i++) {
		        	for(int k = 0; k < splittedActlines.get(i).size() ; k++) {
		        		bw1.write(splittedActlines.get(i).get(k) + "\r\n");
		        	}
		        }
	        } else {
	        	//write the results into the file but every time a new block starts, fill it with the correct date and the filler
	        	for(int i = 0; i < splittedActlines.size() ; i++) {
	        		for(int k = 0; k < splittedActlines.get(i).size(); k++) {
	        			bw1.write(splittedActlines.get(i).get(k) + "\r\n");
	        		}
	        		try {
	        			//taking last line form the current section and first line from the next section
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
	        
	        //creating reference spectrum for the next runs
	        //clearing lists from previous use and loading lists of ALL files and filenames in the lvl1 dir
	        spectraList.clear();
            File lvl1dirNew = new File(ini.lvl1); 
            File[] lvl1FileList = lvl1dirNew.listFiles();
            
            //cycling over all 'healthy' spectra in the current lvl1 folder
            for (int i = 0; i < lvl1FileList.length; i++) {						
				//check if the file is a spectra
				//if not delete from the lists
				if (!checkFilename(lvl1FileList[i].getName())) {						
					continue;
					
				} else {
					//adding file to the spectraList								
    				try {
						spectraList.add(new Spectra(lvl1FileList[i].getName(), lvl1FileList[i]));
					} catch (Exception e) {
						//major point, cannot continue without, exiting application
						taskOutput.append("Something went wrong!\n\n");
	    				taskOutput.append(e.toString());
	    				taskOutput.append("\nExiting evaluation\n");
	    				writingLogAndClosing(frame, ini);
	    				return;
					} 						
				}
			}
            
            //new ref Spec is saved in the folder with ini file 
            //name convention: 'RefSpec_(name of the lvl1 folder, usually month).ref'
            String pathToNewRefSpec = ini._pathToIniFile.getParent() + "\\RefSpec_" + lvl1dirNew.getName()+ ".ref";
	        new Spectra(spectraList, ini, pathToNewRefSpec);
	        
	        taskOutput.append("All done!\n");
	        taskOutput.append("Today statistic:\n");
	        taskOutput.append("Evaluated spectra: " + goodSpecCount + "; flagged: " + flaggedCount + "; low flow: " + lowFlowCount + "; low LT: " + lowLTCount + "; broken: " + brokenCount + "; empty/deleted: " + deleteCount + ".\n\n");
	        
	        writingLogAndClosing(frame, ini);
		    return;
		    
		} catch (FileNotFoundException e) {
			taskOutput.append("Something went wrong!\n\n");
			taskOutput.append(e.toString());
			taskOutput.append("\nExiting evaluation\n");
		} catch (IOException e) {
			taskOutput.append("Something went wrong!\n\n");
			taskOutput.append(e.toString());
			taskOutput.append("\nExiting evaluation\n");
		}
            
    }
	
}
