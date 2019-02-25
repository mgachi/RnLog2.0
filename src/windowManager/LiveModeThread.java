package windowManager;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class LiveModeThread implements Runnable {
	
	public ChartPanel chartPanel;
	public String IP;
	public JToggleButton tglbtnLive;
	public telnetTunnel con;
	public String id;
	public Socket soc;
	
	public LiveModeThread(ChartPanel _chartPanel, String _IP, JToggleButton _tglbtnLive, String _id) {
		chartPanel = _chartPanel;
		IP=_IP;
		tglbtnLive=_tglbtnLive;
		id = _id;
		System.out.println("opening LiveModeThread");
		
	}
	
	@Override
	public void run() {		
		try {
			//open socket connection to monitor
			System.out.println("opening live socket");
			while(tglbtnLive.isSelected()) {
				try {
					soc = new Socket(IP, 23);
				} catch (ConnectException Ce) {
					JOptionPane.showMessageDialog(null, "Could not connect to the Monitor. Maybe it was refused or interrupted by the Monitor or the IP was wrong. Please try again.", "Live Mode", JOptionPane.WARNING_MESSAGE);
					Ce.printStackTrace();
					tglbtnLive.setSelected(false);
					return;
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Could not connect to the Monitor. Maybe " + IP + " is the wrong IP?", "Live Mode", JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
					tglbtnLive.setSelected(false);
					//soc does not need to be closed
					return;
				} 
				//create buffered writer
				BufferedReader readBuffer = new BufferedReader(new InputStreamReader(soc.getInputStream()));
				BufferedWriter writeBuffer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
				//wait and enter password, standard pw is 4118
				TimeUnit.MILLISECONDS.sleep(100);
				writeBuffer.write("4118\r\n");
				writeBuffer.flush(); 
				TimeUnit.MILLISECONDS.sleep(100);

				/////////////////////////////////////
				//get the monitor Time
				////////////////////////////////////
				
				System.out.println("act");
				writeBuffer.write("act");
				writeBuffer.newLine();
				writeBuffer.flush();
				TimeUnit.MILLISECONDS.sleep(100);
				int tmp = readBuffer.read();
				String result="";
				
				//read telnet buffer byte by byte and convert it to char
				//if buffer is not ready, wait and try again, if its still not ready, end of file is reached
				//checking if tmp == -1 does not work
				
				while(true) {
					tmp = readBuffer.read();
					result += Character.toString ((char) tmp);
					if(!readBuffer.ready()) {
						TimeUnit.MILLISECONDS.sleep(300);
						System.out.println("waiting for telnet buffer");
						if(!readBuffer.ready())
							break;
					}
				}
				
				String cleanResult = "";
				for(int i = 0; i<result.split("\n").length; i++) {
					if(!result.split("\n")[i].isEmpty() && !result.split("\n")[i].contains("Password")) {
						cleanResult+=result.split("\n")[i].trim() + System.lineSeparator();
					}
				}				
				
				//if its less than 10, something went wrong while receiving the data, close connection and try again
				if(cleanResult.split("\\s").length<10) {
					soc.close();
					System.out.println("close");
					System.out.println(cleanResult.split("\\s").length);
					continue;
				}
				
				//extract the time and write it into the textField
				String monitorTime = cleanResult.split("\\s")[0] + " " + cleanResult.split("\\s")[1];
				RnLog.textField_2.setText(monitorTime);
				
				//////////////////////////////////
				//get the current channel values
				///////////////////////////////
				
				System.out.println("spc 8");
				writeBuffer.write("spc 8");
				writeBuffer.newLine();
				writeBuffer.flush();
				TimeUnit.MILLISECONDS.sleep(100);
				
				result="";
				//read in everything from the read Buffer, it returns integers and -1 if its EOF
				while(true) {
					tmp = readBuffer.read();
					result += Character.toString ((char) tmp);
					if(!readBuffer.ready()) {
						TimeUnit.MILLISECONDS.sleep(300);
						System.out.println("waiting for telnet buffer");
						if(!readBuffer.ready())
							break;
					}
				}
				
				cleanResult = "";
				for(int i = 0; i<result.split("\n").length; i++) {
					if(!result.split("\n")[i].isEmpty() && !result.split("\n")[i].contains("Password")) {
						cleanResult+=result.split("\n")[i].trim() + System.lineSeparator();
					}
				}
				int[] values = new int[128];
				try {
					for(int i=0; i<cleanResult.split(System.lineSeparator()).length; i++) {
						System.out.println(cleanResult.split(System.lineSeparator())[i]);
						values[i] = Integer.parseInt( cleanResult.split(System.lineSeparator())[i] );
					}
				} catch (Exception e) {
					//the expected input from the monitor was wrong, close connection and try again from the beginning
					soc.close();
					e.printStackTrace();
					continue;
				}
				
				//update GUI with the values
				XYSeries showSpectrum = new XYSeries("");
				XYSeriesCollection counts = new XYSeriesCollection(showSpectrum);
				int counter = 0;
			    for(int i1=0; i1< 128; i1++) {
			    	showSpectrum.add(i1, values[i1]);
			    	counter+=values[i1];
			    }
				JFreeChart chart = ChartFactory.createXYLineChart("", "", "", counts);
			    chart.removeLegend();
				chart.setTitle("");
				chart.getPlot().setBackgroundPaint( Color.WHITE );
				chartPanel.setChart(chart);
				//update tf_4 = Monitor Name and tf_20 = amount of counts
				RnLog.textField_20.setText(String.valueOf(counter));
				RnLog.textField_4.setText(id);
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
				//don't get the data too fast
				soc.close();
				TimeUnit.MILLISECONDS.sleep(500);	
				//tglbtnLive.setSelected(false);
			}
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect to the Monitor. Maybe " + IP + " is the wrong IP?", "Live Mode", JOptionPane.WARNING_MESSAGE);
			tglbtnLive.setSelected(false);
			try {
				soc.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			return;
		} catch (InterruptedException intE) {
			tglbtnLive.setSelected(false);
			try {
				soc.close();
			} catch (IOException e3) {
				e3.printStackTrace();
			}
			return;
		}
	}

	
}
