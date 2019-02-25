package windowManager;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;

public class SDCardDialog extends JFrame {

	public static JPanel contentPane = new JPanel();
	public static JLabel lblStatus = new JLabel("Connecting to Monitor...");
	public static JProgressBar progressBar = new JProgressBar();
	public static DefaultListModel<String> listModel1 = new DefaultListModel<String>();
	public static JList list1; // = new JList(listModel1);
	public static iniFile ini = new iniFile();
	public static String IP;
	
	/////////////////////////////////////
	//TODO: currently sometimes listModel1 gets deleted after the thread runned
	//maybe a pointer problem
	////////////////////////////
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SDCardDialog frame = new SDCardDialog(IP);
					frame.setVisible(true);
				} catch (Exception e) {
					System.out.println("constructor failed");
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 */
	public SDCardDialog(String _IP) {
		IP = _IP;
		JTextPane textPane = new JTextPane();
		getContentPane().add(textPane, BorderLayout.WEST);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 510, 324);
		//contentPane = new JPanel();// nach oben oder?
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//JLabel lblStatus = new JLabel("Connecting to Monitor...");
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(0, 0, 506, 33);
		contentPane.add(lblStatus);
		
		//DefaultListModel<String> listModel1 = new DefaultListModel<String>();
 
		//JList list1 = new JList(listModel1);
		list1 = new JList(listModel1);
		list1.setBorder(null);
		list1.setBounds(44, 74, 126, 174);
		list1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list1.setLayoutOrientation(JList.VERTICAL);
		list1.setVisibleRowCount(-1);
		JScrollPane scrollPane1 = new JScrollPane(list1);
		scrollPane1.setBounds(44, 73, 126, 176);
		contentPane.add(scrollPane1); 
		DefaultListModel<String> listModel2 = new DefaultListModel<String>();
		JList list2 = new JList(listModel2);
		list2.setBorder(null);
		list2.setBounds(317, 74, 126, 174);
		
		JScrollPane scrollPane2 = new JScrollPane(list2);
		scrollPane2.setBounds(317, 73, 126, 176);
		contentPane.add(scrollPane2);
		
		JLabel lblSD = new JLabel("Files on SD card");
		lblSD.setHorizontalAlignment(SwingConstants.CENTER);
		lblSD.setBounds(44, 58, 126, 14);
		contentPane.add(lblSD);
		
		JLabel lblSelection = new JLabel("Files to be copied");
		lblSelection.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelection.setBounds(317, 58, 126, 14);
		contentPane.add(lblSelection);	
		
		JButton btnSelect = new JButton("Select Files -->");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//copy the selected items from list1 to list2
				int[] Selected = new int[list1.getSelectedIndices().length];
				Selected = list1.getSelectedIndices();
				for(int i = 0; i< Selected.length; i++) {
					//check wether item is already inside the list
					if(!listModel2.contains(listModel1.get(Selected[i])))
						listModel2.addElement(listModel1.getElementAt(Selected[i]));
				}
				//sort list by name using temporary list
				String[] list = new String[listModel2.size()];
			    for (int i = 0; i < listModel2.size(); i++) {
			    	list[i] = listModel2.get(i);
			    }
			    Arrays.sort(list);
			    listModel2.removeAllElements();
			    for (String s : list) {
			        listModel2.addElement(s);
			    }
			}
		});
		btnSelect.setBounds(180, 99, 127, 23);
		contentPane.add(btnSelect);
		
		JButton btnDelete = new JButton("Delete Selection");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listModel2.clear();
			}
		});
		btnDelete.setBounds(180, 153, 127, 23);
		contentPane.add(btnDelete);
		
		JButton btnAll = new JButton("Copy All");
		btnAll.setBounds(44, 251, 126, 23);
		contentPane.add(btnAll);
		
		JButton btnCopySelection = new JButton("Copy selection");
		btnCopySelection.setBounds(317, 251, 126, 23);
		contentPane.add(btnCopySelection);
		
		JButton btnStopTransfer = new JButton("Stop transfer");
		btnStopTransfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO: implement stop transfer
			}
		});
		btnStopTransfer.setBounds(180, 251, 127, 23);
		contentPane.add(btnStopTransfer);
		btnStopTransfer.setVisible(false);
		
		progressBar.setForeground(Color.GREEN);
		progressBar.setBounds(10, 30, 474, 14);
		contentPane.add(progressBar);
		
		btnAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listModel1.isEmpty()) {
					JOptionPane.showMessageDialog(null, "No files recieved, close window and try again."/*, JOptionPane.INFORMATION_MESSAGE*/);
					return;
				}
				//ask user where to safe them
				final JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        //only show not hidden files
		        chooser.setFileHidingEnabled(true);
		        //to select single directory
		        chooser.setMultiSelectionEnabled(false);
		        //In response to a button click:
		        int option = chooser.showOpenDialog(null);
		        File safeHere =null;
		        if(option == JFileChooser.APPROVE_OPTION) {
		        	safeHere = chooser.getSelectedFile();
		        } else if (option == JFileChooser.CANCEL_OPTION){
		        	System.out.println("User cancelled operation. No Directory file was chosen.");
		        	return;
		        }
				transfer(listModel1, safeHere.getPath());
			}
		});
		
		btnCopySelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//check wether files are selected
				if (listModel2.isEmpty()) {
					JOptionPane.showMessageDialog(null, "No files selected"/*, JOptionPane.INFORMATION_MESSAGE*/);
					return;
				}
				//ask user where to safe them
				final JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        //only show not hidden files
		        chooser.setFileHidingEnabled(true);
		        //to select single directory
		        chooser.setMultiSelectionEnabled(false);
		        //In response to a button click:
		        int option = chooser.showOpenDialog(null);
		        File safeHere =null;
		        if(option == JFileChooser.APPROVE_OPTION) {
		        	safeHere = chooser.getSelectedFile();
		        } else if (option == JFileChooser.CANCEL_OPTION){
		        	System.out.println("User cancelled operation. No Directory file was chosen.");
		        	return;
		        }
				transfer(listModel2, safeHere.getPath());
			}
		});

		//open up a thread which gets the directories
		
		//testConnection(IP, progressBar, lblStatus);
		//getDirectory(IP, progressBar,lblStatus, listModel1);
		
		//clear listModel1 just in case there might be files from a previous run/anbother monitor and reset others
		listModel1.clear();
		lblStatus.setForeground(Color.BLACK);
		progressBar.setValue(0);
		Thread TgetFilenames = new Thread(new GetFilenamesThread (IP, lblStatus, listModel1, btnAll ,btnCopySelection, btnStopTransfer, btnDelete, btnSelect, progressBar));
		//TODO:disable all buttons until thread is finished
		//currently, modelLiust1, get deleted if they get enabled again in the thread
		/* btnAll.setEnabled(false);
		btnCopySelection.setEnabled(false);
		btnStopTransfer.setEnabled(false);
		btnDelete.setEnabled(false);
		btnSelect.setEnabled(false);*/
		//thread should enable buttons again if everything worked fine
		TgetFilenames.start();

	}

	private static void transfer(DefaultListModel<String> listModel, String path) {
		//transfer the files from the listModel
		
		progressBar.setValue(0);
		lblStatus.setText("Copying files from "+ ini.id);
		ArrayList<String> returnedSpectra = new ArrayList<String>();
		ArrayList<String> filenamesToSend = new ArrayList<String>();
		for(int i=0; i<listModel.getSize(); i++) {
			filenamesToSend.add(listModel.getElementAt(i));
		}
		
		int files = listModel.size();
		try {
			telnetTunnel con = new telnetTunnel(IP, 23);
			returnedSpectra = con.snd(filenamesToSend, progressBar, lblStatus);
			
		} catch (Exception e) {
			e.printStackTrace();
			lblStatus.setText("Could not transfer files from " + IP);
			lblStatus.setForeground(Color.RED);
			progressBar.setValue(100);
		}
		//safe the spectra safed in retrunedSpectra into the directory
		for(int j=0; j<returnedSpectra.size(); j++) {
			try { 
				File newTextFile = new File(path + "\\" + ((String)filenamesToSend.get(j).trim()));
		        FileWriter fw = new FileWriter(newTextFile);
		        fw.write(returnedSpectra.get(j));
		        fw.close();
			} catch (Exception e) {
				lblStatus.setText("Could not safe " + ((String)filenamesToSend.get(j).trim()));
				lblStatus.setForeground(Color.RED);
				e.printStackTrace();
			}
		}
		lblStatus.setText("Successfully copyied file(s)");
	}
	
/*
	private static void testConnection(String _IP, JProgressBar progressBar, JLabel lblStatus) {
		//connect to the monitor, use command "ver" to check if something comes back
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			telnetTunnel con = new telnetTunnel(_IP, 23);
			String version = con.get("ver");
			if(version.length()>5) {
				lblStatus.setText("Connected to " + version.substring(1));
				progressBar.setValue(100);
				System.out.println(version);
			} else {
				//if no "real" version comes back from the monitor
				System.out.println("Connection failed");
				throw new Exception();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//try once again
			try {
				telnetTunnel con = new telnetTunnel(_IP, 23);
				String version = con.get("ver");
				if(version.length()>5) {
					lblStatus.setText("Connected to " + version.substring(1));
					progressBar.setValue(100);
					System.out.println(version);
				} else {
					//if no "real" version comes back from the monitor
					System.out.println("Connection failed second");
					throw new Exception();
				}
			} catch (Exception e2) {
				//second connection failed
				e.printStackTrace();
				lblStatus.setText("Could not connect to IP " + _IP);
				lblStatus.setForeground(Color.RED);
				progressBar.setValue(100);
			}
		}
		
	}
*/

}
