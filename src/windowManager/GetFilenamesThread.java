package windowManager;

import java.awt.Color;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class GetFilenamesThread implements Runnable{
	public JLabel lblStatus;
	//add the filenames to this list:
	public DefaultListModel<String> listModel1;
	public iniFile ini = new iniFile();
	public JButton btnAll = new JButton();
	public JButton btnCopySelection = new JButton();
	public JButton btnStopTransfer = new JButton();
	public JButton btnDelete = new JButton();
	public JButton btnSelect = new JButton();
	public JProgressBar progressBar = new JProgressBar();
	public String IP;

	public GetFilenamesThread(String _IP, JLabel _lblStatus, DefaultListModel<String> _listModel1, JButton _btnAll, 
			JButton _btnCopySelection, JButton _btnStopTransfer, JButton _btnDelete, JButton _btnSelect, JProgressBar _progressBar) {
		IP = _IP;
		lblStatus = _lblStatus;
		listModel1 = _listModel1;
		btnAll = _btnAll;
		btnCopySelection = _btnCopySelection;
		btnStopTransfer = _btnStopTransfer;
		btnDelete = _btnDelete;
		btnSelect = _btnSelect;
		progressBar = _progressBar;
		System.out.println("opening thread to gather filenames");
	}

	
	public void run(){
		//connect to the monitor via telnetTunnel
		//get the filenames on the Card
		lblStatus.setText("Gathering filenames from " + ini.id +"'s SD Card.");
		try {
			getDirectory();
		} catch (InterruptedException e) {
			lblStatus.setText("Conenction closed");
			e.printStackTrace();
		} catch (IOException e) {

			lblStatus.setText("Could not connect to " + IP);
			lblStatus.setForeground(Color.RED);
			e.printStackTrace();
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			//close thread
			progressBar.setValue(100);
			return;
		}
		if(listModel1.isEmpty()) {
			//could not retrieve files from card
			lblStatus.setText("Could not connect to " + IP);
			lblStatus.setForeground(Color.RED);
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			//close thread
			progressBar.setValue(100);
			return;
		}
		//TODO:enable all buttons again
		/*
		btnAll.setEnabled(true);
		btnCopySelection.setEnabled(true);
		btnStopTransfer.setEnabled(true);
		btnDelete.setEnabled(true);
		btnSelect.setEnabled(true);
		progressBar.setValue(100);*/
	}
	
	
	public void getDirectory() throws InterruptedException, IOException {
		//get directory
		System.out.println("Gathering filenames...");
		telnetTunnel con = new telnetTunnel(IP, 23);
		TimeUnit.MILLISECONDS.sleep(100);
		String directory = con.get_dir(lblStatus);
		TimeUnit.MILLISECONDS.sleep(100);
		for(int i = 0; i< directory.replaceAll("\\s","").split(";").length; i++) {
			try {
				if(!directory.replaceAll("\\s","").split(";")[i].isEmpty()) {
					listModel1.addElement(directory.replaceAll("\\s","").split(";")[i]);
					System.out.print("added " + directory.replaceAll("\\s","").split(";")[i]);
				}
			} catch (NullPointerException ne) {
				//could not retrieve files
				lblStatus.setText("Could not connect to " + IP);
				lblStatus.setForeground(Color.RED);
				try {
					TimeUnit.MILLISECONDS.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				//close thread
				progressBar.setValue(100);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		lblStatus.setText("Loaded Filenames from " + ini.id);
	}
	
}
