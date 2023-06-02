package util;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WinMp3Player extends JDialog {
	private JTextField tfFilePath;
	private Player player;
	private JList listMp3;
	private String dirPath;
	
	private Thread playThread; 
	private Thread resumeThread;
	private long totalLength;
	private long pauseLength;
	protected FileInputStream fileInputStream;
	protected BufferedInputStream bufferedInputStream;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinMp3Player dialog = new WinMp3Player();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public WinMp3Player() {
		setTitle("Mp3 Player");
		setBounds(100, 100, 739, 443);
		getContentPane().setLayout(null);
		
		JButton btnPlay = new JButton("재생");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				playThread = new Thread(runnablePlay);
				if(player != null) { // 재생되고 있다
					player.close();
					playThread.start();
				}
				else
					playThread.start();
			}
		});
		btnPlay.setBounds(515, 54, 97, 23);
		getContentPane().add(btnPlay);
		
		tfFilePath = new JTextField();
		tfFilePath.setText("C:/FileIO/mp3/");
		tfFilePath.setBounds(12, 21, 496, 21);
		getContentPane().add(tfFilePath);
		tfFilePath.setColumns(10);
		
		JButton btnStop = new JButton("정지");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.close();
			}
		});
		btnStop.setBounds(614, 54, 97, 23);
		getContentPane().add(btnStop);
		
		JButton btnSearch = new JButton("찾기...");
		btnSearch.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser("C:/FileIO/mp3/");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("음악파일", "mp3");
				fileChooser.setFileFilter(filter);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);				
				if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					dirPath = fileChooser.getSelectedFile().getParent();
					File paths = new File(dirPath);
					String mp3Files[] = paths.list();
					
					Vector vector = new Vector();
					for(int i=0; i<mp3Files.length;i++)
						vector.add(mp3Files[i]);
					
					listMp3.setListData(vector);
				}
			}
		});
		btnSearch.setBounds(515, 20, 97, 23);
		getContentPane().add(btnSearch);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 54, 496, 340);
		getContentPane().add(scrollPane);
		
		listMp3 = new JList();
		listMp3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tfFilePath.setText(dirPath + "/" + listMp3.getSelectedValue().toString());
			}
		});
		scrollPane.setViewportView(listMp3);
		
		JButton btnPause = new JButton("일시정지");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resumeThread = new Thread(runnableResume);
				if(player != null) {
					try {
						pauseLength = fileInputStream.available();
						player.close();
						player = null;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else {
					resumeThread.start();					
				}
			}
		});
		btnPause.setBounds(515, 104, 97, 23);
		getContentPane().add(btnPause);
	}
	
	Runnable runnablePlay = new Runnable() {
		@Override
		public void run() {
			String fileName = tfFilePath.getText();
			try {
				fileInputStream = new FileInputStream(fileName);
				bufferedInputStream = new BufferedInputStream(fileInputStream);
				player = new Player(bufferedInputStream);
				
				totalLength = fileInputStream.available();
				
				player.play();  // 얼어 붙는다. 해당 곡이 끝날때까지 아무것도 할 수 없다.
			} catch (IOException | JavaLayerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}				
		}		
	};
	Runnable runnableResume = new Runnable() {
		@Override
		public void run() {
			String fileName = tfFilePath.getText();
			try {
				fileInputStream = new FileInputStream(fileName);
				bufferedInputStream = new BufferedInputStream(fileInputStream);
				player = new Player(bufferedInputStream);
				
				fileInputStream.skip(totalLength - pauseLength);
				
				player.play();  // 얼어 붙는다. 해당 곡이 끝날때까지 아무것도 할 수 없다.
			} catch (IOException | JavaLayerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
		
	};
			
}
