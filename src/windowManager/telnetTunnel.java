package windowManager;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.SystemColor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class telnetTunnel {
	
	/*opening connection to the monitor via an telent socket
	 * unfortunantely the connection has to be redone for every function
	 * or the connection will be lost.
	 */
	
	public Socket soc = null;
	public BufferedReader readBuffer = null;
	public BufferedWriter writeBuffer = null;
	public String IP = null;
	public int port = 23;
	
	telnetTunnel(String IP, int port) {
		this.IP = IP;
		this.port = port;
	}
	
	public String get_dir(JLabel lblStatus) throws  InterruptedException, IOException {
		//open socket connection to monitor
		System.out.println("opening socket");
		System.out.println("1 ");
		soc = new Socket(IP,port);	
		System.out.println("2 ");
		//create buffered writer
		BufferedReader readBuffer = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		BufferedWriter writeBuffer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
		//wait and enter password, standard pw is 4118
		TimeUnit.MILLISECONDS.sleep(100);
		writeBuffer.write("4118\r\n");
		writeBuffer.flush(); 
		TimeUnit.MILLISECONDS.sleep(100);
		
		//send 'dir' to get a list of all files on the SD card
		writeBuffer.write("dir");
		writeBuffer.newLine();
		writeBuffer.flush();
		TimeUnit.MILLISECONDS.sleep(100);

		// getting the first 4 lines, password etc
		String directory = ""; 
		readBuffer.readLine();
		readBuffer.readLine();
		readBuffer.readLine();
		readBuffer.readLine();
		
		//check every 500ms if a new name arrived
		String tmp = null;
		while(true){
			TimeUnit.MILLISECONDS.sleep(5);
			tmp = readBuffer.readLine();
			System.out.println("tmp " + tmp);
			try {
				//tmp is null sometimes, connection error
				if(tmp.isEmpty()) {
					//no new files read
					break;
				} else {
					directory = directory+ ";" + tmp.trim();
					try {
						if(!tmp.trim().isEmpty())
							lblStatus.setText("Read " +tmp.trim());
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					tmp = null;
				}
			} catch (Exception e) {
				directory = null;
				break;
			}
		}
		soc.close();
		return directory;
	}
	
	public String get(String telnet_command) throws UnknownHostException, IOException, InterruptedException {
		
		/* Returns the Answer of the monitor regarding a telnet command, but only for one line commands without parameters;
		 * ver reports the current firmware version and ID of the monitor
		 * clk reports the current date and time of the monitor if no parameter is given, else one can adjust the date and time using the format dd.mm.yyyy hh:mm:ss
		 * act displays the current sensor data
		 * dir lists all data files stored on the SD card
		 * sof displays the current offsets
		 * ssl displays the current slopes
		 * ivl displays the interval length if no parameter is given, else the interval length can get set by giving a parameter in seconds, e.g. ivl 1800
		 * clr clears the current spectrum
		 * era deletes everything on the SD card
		 * raw displays the PT100 resistances
		 * ver Displays the current version of the monitor
		 * spc 8 displays the current data/channel values
		 */
		
		//exception for "dir" because it returns several lines of code
		if(telnet_command.equals("dir")) {
			return "dir";
		}
		
		//open socket connection to monitor
		System.out.println("opening socket");
		soc = new Socket(IP,port);	
		//create buffered writer
		BufferedReader readBuffer = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		BufferedWriter writeBuffer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
		//wait and enter password, standard pw is 4118
		TimeUnit.MILLISECONDS.sleep(100);
		writeBuffer.write("4118\r\n");
		writeBuffer.flush(); 
		TimeUnit.MILLISECONDS.sleep(100);
		writeBuffer.write(telnet_command);
		writeBuffer.newLine();
		writeBuffer.flush();
		TimeUnit.MILLISECONDS.sleep(500);
		readBuffer.readLine(); readBuffer.readLine(); readBuffer.readLine(); readBuffer.readLine(); 
		String result = readBuffer.readLine();
		writeBuffer.close();
		readBuffer.close();
		this.soc.close();
		return result;
	}	
	
	public String set(String telnet_command, String param) throws IOException, InterruptedException {
		
		/* for telnet commands with one parameter setting values:
		 * clk adjust the date and time using the format dd.mm.yyyy hh:mm:ss
		 * ivl interval length can get set by giving a parameter in seconds, e.g. ivl 1800
		 * snd sends a specific file to the PC, e.g. snd C2418001.R1
		 */
		
		//check for wrong inputs
		boolean correct = false;
		if(telnet_command.equals("clk")) {
			correct = true;
			if(param.length()!=19) {
				return "wrong parameter given";
			}
		}
		if(telnet_command.equals("ivl")) {
			correct = true;
			try {
				Integer.parseInt(param);
			} catch (Exception e){
				return "wrong parameter given";
			}
		}
		if(telnet_command.equals("snd")) {
			correct = true;
			if(param.split(".")[0].length() != 8) {
				return  "wrong parameter given";
			}
		}
		if (!correct) return "wrong command given";
		
		//open socket connection to monitor
		System.out.println("opening socket");
		soc = new Socket(IP,port);	
		//create buffered writer
		BufferedReader readBuffer = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		BufferedWriter writeBuffer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
		//wait and enter password, standard pw is 4118
		TimeUnit.MILLISECONDS.sleep(100);
		writeBuffer.write("4118\r\n");
		writeBuffer.flush(); 
		TimeUnit.MILLISECONDS.sleep(100);
		writeBuffer.write(telnet_command + " " + param);
		writeBuffer.newLine();
		writeBuffer.flush();
		TimeUnit.MILLISECONDS.sleep(500);
		readBuffer.readLine(); readBuffer.readLine(); readBuffer.readLine(); readBuffer.readLine(); 
		String result = readBuffer.readLine();
		writeBuffer.close();
		readBuffer.close();
		this.soc.close();
		return result;
	}
	
	public String set(String telnet_command, String sensor, String number, String offset) throws IOException, InterruptedException {
		/* for telnet commands with two parameters:
		 * sto sets the offset of the sensors. To define the sensor that is to be set there are 2 address parameters. 
		 * The letters [l,t,c] stand for the [ADW, PT100, pulse counter] connections and adding a number 1,2 or 3 (only 1 or 2 for the pulse counters) 
		 * makes the address unique. Conclude the command with the offset value you want to assign, e.g. sto t 1 4.3
		 * sts sets the slope of the sensors, parameter syntax analog to ‘sto’
		 */
		//TODO: catch input errors
		
		//open socket connection to monitor
		System.out.println("opening socket");
		soc = new Socket(IP,port);	
		//create buffered writer
		BufferedReader readBuffer = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		BufferedWriter writeBuffer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
		//wait and enter password, standard pw is 4118
		TimeUnit.MILLISECONDS.sleep(100);
		writeBuffer.write("4118\r\n");
		writeBuffer.flush(); 
		TimeUnit.MILLISECONDS.sleep(100);
		writeBuffer.write(telnet_command + " " + sensor + " " + number + " " + offset);
		writeBuffer.newLine();
		writeBuffer.flush();
		TimeUnit.MILLISECONDS.sleep(500);
		readBuffer.readLine(); readBuffer.readLine(); readBuffer.readLine(); readBuffer.readLine(); 
		String version = readBuffer.readLine();
		writeBuffer.close();
		readBuffer.close();
		this.soc.close();
		return version;
	}
	
	public Spectra getLiveSpectrum() throws UnknownHostException, IOException, InterruptedException {
		//this starts a new thread
		//returns the actual spectrum of
		Spectra actSpec = null;
		String [] actSpecLines = new String[131];
		//open socket connection to monitor
		System.out.println("opening socket");
		soc = new Socket(IP,port);	
		//create buffered writer
		BufferedReader readBuffer = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		BufferedWriter writeBuffer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
		//wait and enter password, standard pw is 4118
		TimeUnit.MILLISECONDS.sleep(100);
		writeBuffer.write("4118\r\n");
		writeBuffer.flush(); 
		TimeUnit.MILLISECONDS.sleep(100);
		writeBuffer.write("spc 8");
		writeBuffer.newLine();
		writeBuffer.flush();
		TimeUnit.MILLISECONDS.sleep(500);
		readBuffer.readLine(); readBuffer.readLine(); readBuffer.readLine(); readBuffer.readLine(); 
		String[] values =  new String[128];
		for(int i = 0; i< 128; i++) {
			values[i] = readBuffer.readLine();
	        System.out.print("value " + i + " " + values[i]);
		}
		
		writeBuffer.close();
		readBuffer.close();
		this.soc.close();
		
		actSpecLines[0] = this.get("act");
		TimeUnit.MILLISECONDS.sleep(100);
		for (int i = 0; i<129; i++) {
			actSpecLines[i+1] = values[i];
		}
		actSpecLines[131] = this.get("ver").split(" ")[2];
		TimeUnit.MILLISECONDS.sleep(100);

		for (int i = 0; i<actSpecLines.length; i++) {
			System.out.print("actSpecLine " + actSpecLines[i]);
		}
		return actSpec;
	}

	public ArrayList<String> snd(ArrayList<String> FileList, JProgressBar progressBar, JLabel lblProgress2) throws IOException, InterruptedException {
		//gets an array of filenames and returns an array of Strings, consisting of the spectra
		//the strings needs to be saved as spectra
		
		ArrayList<String> Files = new ArrayList<String>();
		double progress = 100/FileList.size();
		
		//open socket connection to monitor
		System.out.println("opening socket");
		soc = new Socket(IP,port);	
		//soc2 = new TelnetClient();
		//soc2.connect(IP);
		
		//create buffered writer
		BufferedReader readBuffer = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		BufferedWriter writeBuffer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
		//wait and enter password, standard pw is 4118
		TimeUnit.MILLISECONDS.sleep(100);
		writeBuffer.write("4118\r\n");
		writeBuffer.flush(); 
		TimeUnit.MILLISECONDS.sleep(100);
		
		////////////////////////////////////
		//get the spectra for every filename
		////////////////////////////////////
		for(int j = 0; j< FileList.size(); j++) {
			progressBar.setValue((int) progress * (j+1));
			lblProgress2.setText("Getting " + FileList.get(j));
			System.out.println("Getting " + FileList.get(j));
			TimeUnit.MILLISECONDS.sleep(50);
			writeBuffer.write("snd" + " " + FileList.get(j));
			writeBuffer.newLine();
			writeBuffer.flush();
			TimeUnit.MILLISECONDS.sleep(500);

			//////////////////////////////////////////////////////////
			//gather the data
			///////////////////////////////////////////////////////////
			TimeUnit.MILLISECONDS.sleep(50);
			int tmp;
			String result="";
			TimeUnit.MILLISECONDS.sleep(50);
			
			//read telnet buffer byte by byte and convert it to char
			//if buffer is not ready, wait and try again, if its still not ready, end of file is reached
			//checking if tmp == -1 does not work
			
			while(true) {
				tmp = readBuffer.read();
				result += Character.toString ((char) tmp);
				if(!readBuffer.ready()) {
					TimeUnit.MILLISECONDS.sleep(70);
					System.out.println("waiting for telnet buffer");
					if(!readBuffer.ready())
						break;
				}
			}
			
			//////////////////////////////////////////////////////////
			//compute the correct output string out of the telnet data
			//by removing linebreaks ("\n") and spaces
			///////////////////////////////////////////////////////////
			String cleanResult = "";
			for(int i = 0; i<result.split("\n").length; i++) {
				if(!result.split("\n")[i].isEmpty() && !result.split("\n")[i].contains("Password")) {
					cleanResult+=result.split("\n")[i].trim() + System.lineSeparator();
				}
			}
			Files.add(cleanResult);
		}
		writeBuffer.close();
		readBuffer.close();
		this.soc.close();
		return Files;
	}

	public void getLive(String LIP, ChartPanel chartPanel, String id) throws UnknownHostException, IOException, InterruptedException {
		//open socket connection to monitor
				System.out.println("opening live socket");
				soc = new Socket(IP,port);	
				//create buffered writer
				BufferedReader readBuffer = new BufferedReader(new InputStreamReader(soc.getInputStream()));
				BufferedWriter writeBuffer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
				//wait and enter password, standard pw is 4118
				TimeUnit.MILLISECONDS.sleep(100);
				writeBuffer.write("4118\r\n");
				writeBuffer.flush(); 
				TimeUnit.MILLISECONDS.sleep(100);
				//first three lines are empty
				System.out.println("act");
				writeBuffer.write("act");
				writeBuffer.newLine();
				writeBuffer.flush();
				TimeUnit.MILLISECONDS.sleep(100);
				String result="";
				//first 4 lines are empty
				readBuffer.readLine();
				readBuffer.readLine();
				readBuffer.readLine();
				readBuffer.readLine();
				result = readBuffer.readLine();
				readBuffer.readLine();
				String act = result.trim().split(" ")[0] + " " + result.trim().split(" ")[1];
				System.out.println("act ############ " + act);
				TimeUnit.MILLISECONDS.sleep(100);
				writeBuffer.write("spc 8");
				writeBuffer.newLine();
				writeBuffer.flush();
				TimeUnit.MILLISECONDS.sleep(100);
				int i = 0;
				int[] values = new int[128];
				while(true) {
					if(i==256)
						break;
					if(i%2 == 0) {
						values[i/2] = Integer.valueOf(readBuffer.readLine().trim());
					} else {
						readBuffer.readLine();
					}
					i++;
				}
				System.out.println(act);
				for(int j = 0; j< values.length; j++) {
					System.out.println(values[j]);
				}
				//update GUI
				 XYSeries showSpectrum = new XYSeries("");
				XYSeriesCollection counts = new XYSeriesCollection(showSpectrum);
				int counter = 0;
			    for(int i1=0; i1< 128; i1++) {
			    	showSpectrum.add(i1, values[i1]);
			    	counter+=values[i1];
			    }
				JFreeChart chart = ChartFactory.createXYLineChart("", "" /*x-axis label*/, "" /*y-axis label*/, counts);
			    chart.removeLegend();
				chart.setTitle("");
				chart.getPlot().setBackgroundPaint( Color.WHITE );
				chartPanel.setChart(chart);
				//only Update datetime
				RnLog.textField_20.setText(String.valueOf(counter));
				RnLog.textField_4.setText(id);
				RnLog.textField_2.setText(act);
				//draw edge
				XYPlot plot = (XYPlot) chart.getPlot();
				plot.setDomainGridlinesVisible(true);
				plot.setRangeGridlinesVisible(true);
				plot.setRangeGridlinePaint(Color.gray);
				plot.setDomainGridlinePaint(Color.gray);
				XYItemRenderer renderer = plot.getRenderer();
				Shape shape  = new Rectangle();
				renderer.setShape(shape);
			    renderer.setBasePaint(Color.red);
				System.out.println("Updated chart"); 
				
				writeBuffer.close();
				readBuffer.close();
				
				soc.close();
				//dont get the data too fast
				TimeUnit.MILLISECONDS.sleep(1000);
	}
}


