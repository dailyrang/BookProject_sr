package util;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

import com.ibm.icu.util.Calendar;

import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Encoder;
import java.awt.Window.Type;
import java.awt.Dialog.ModalExclusionType;
import javax.swing.JProgressBar;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import java.awt.Font;
import javax.swing.AbstractListModel;

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
	private JButton btnPause;
	private JProgressBar progressBar;
	private int seconds=0;
	private int total=0;
	private JSpinner spinnerHour;
	private JSpinner spinnerMinute;
	private JSpinner spinnerSecond;
	private JLabel lblSeconds;
	private JList taLyrics;
	
	private int seq = 0;
	private JLabel lblLyrics;

	
	DefaultListModel model ;
	private JList listInterval;
	/**
	 * Create the dialog.
	 */
	public WinMp3Player() {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(player != null)
					player.close();
			}
		});
		setTitle("Mp3 Player");
		setBounds(100, 100, 904, 691);
		getContentPane().setLayout(null);
		
		JButton btnPlay = new JButton("재생");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				seconds = 0;
				seq = 0;
				
				showInformation(); // 가사 보이기
				listInterval.setSelectedIndex(seq);
				
				new Timer().schedule(task, 0, 1000);
				
				new Timer().schedule(taskInterval, 0, 1000);
				
				musicQ();
			}
		});
		btnPlay.setBounds(359, 55, 97, 23);
		getContentPane().add(btnPlay);
		
		tfFilePath = new JTextField();
		tfFilePath.setText("C:/FileIO/mp3/");
		tfFilePath.setBounds(12, 21, 335, 21);
		getContentPane().add(tfFilePath);
		tfFilePath.setColumns(10);
		
		JButton btnStop = new JButton("정지");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPause.setEnabled(false);
				
				player.close();
			}
		});
		btnStop.setBounds(458, 55, 97, 23);
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
		btnSearch.setBounds(359, 21, 97, 23);
		getContentPane().add(btnSearch);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 54, 335, 340);
		getContentPane().add(scrollPane);
		
		listMp3 = new JList();
		listMp3.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				tfFilePath.setText(dirPath + "/" + listMp3.getSelectedValue().toString());
			}
		});
		listMp3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tfFilePath.setText(dirPath + "/" + listMp3.getSelectedValue().toString());
				//musicQ();
			}
		});
		scrollPane.setViewportView(listMp3);
		
		btnPause = new JButton("일시정지");
		btnPause.setEnabled(false);
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resumeThread = new Thread(runnableResume);
				if(player != null) {
					try {
						pauseLength = fileInputStream.available();
						player.close();
						player = null;
						btnPause.setText("재시작");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else {
					resumeThread.start();		
					btnPause.setText("일시정지");
				}
			}
		});
		btnPause.setBounds(359, 88, 97, 23);
		getContentPane().add(btnPause);
		
		JButton btnPrev = new JButton("<");
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int idx = listMp3.getSelectedIndex();
				listMp3.setSelectedIndex(idx-1);	
				seconds = 0;
				seq = 0;
				showInformation(); // 가사 보이기
				musicQ();
			}
		});
		btnPrev.setBounds(359, 121, 97, 23);
		getContentPane().add(btnPrev);
		
		JButton btnNext = new JButton(">");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int idx = listMp3.getSelectedIndex();
				listMp3.setSelectedIndex(idx+1);	
				seconds = 0;
				seq = 0;
				showInformation(); // 가사 보이기
				musicQ();
			}
		});
		btnNext.setBounds(458, 121, 97, 23);
		getContentPane().add(btnNext);
		
		progressBar = new JProgressBar();
		progressBar.setBackground(new Color(192, 192, 192));
		progressBar.setForeground(new Color(255, 0, 0));
		progressBar.setStringPainted(true);
		progressBar.setValue(50);
		progressBar.setBounds(359, 154, 196, 23);
		getContentPane().add(progressBar);
		
		JButton btnStart = new JButton("Alarm Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Timer timer = new Timer();
				//timer.schedule(task, 0, 1000);
				new Timer().schedule(task, 0, 1000);
			}
		});
		btnStart.setBounds(359, 187, 196, 23);
		getContentPane().add(btnStart);
		
		JLabel lblHour = new JLabel("시");
		lblHour.setBounds(408, 220, 18, 15);
		getContentPane().add(lblHour);
		
		spinnerHour = new JSpinner();
		spinnerHour.setModel(new SpinnerNumberModel(0, 0, 23, 1));
		spinnerHour.setBounds(359, 217, 47, 22);
		getContentPane().add(spinnerHour);
		
		JLabel lblMinute = new JLabel("분");
		lblMinute.setBounds(477, 220, 18, 15);
		getContentPane().add(lblMinute);
		
		spinnerMinute = new JSpinner();
		spinnerMinute.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		spinnerMinute.setBounds(431, 217, 47, 22);
		getContentPane().add(spinnerMinute);
		
		JLabel lblSecond = new JLabel("초");
		lblSecond.setBounds(537, 220, 18, 15);
		getContentPane().add(lblSecond);
		
		spinnerSecond = new JSpinner();
		spinnerSecond.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		spinnerSecond.setBounds(494, 217, 42, 22);
		getContentPane().add(spinnerSecond);
		
		JButton btnInformation = new JButton("정보 보기");
		btnInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showInformation();
			}
		});
		btnInformation.setBounds(579, 216, 97, 23);
		getContentPane().add(btnInformation);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(359, 249, 352, 333);
		getContentPane().add(scrollPane_1);
		
		taLyrics = new JList();
		scrollPane_1.setViewportView(taLyrics);
		
		lblSeconds = new JLabel("현재초/전체초");
		lblSeconds.setBounds(579, 156, 97, 15);
		getContentPane().add(lblSeconds);
		
		lblLyrics = new JLabel("New label");
		lblLyrics.setHorizontalAlignment(SwingConstants.CENTER);
		lblLyrics.setFont(new Font("굴림", Font.BOLD, 30));
		lblLyrics.setBounds(0, 592, 699, 50);
		getContentPane().add(lblLyrics);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(772, 249, 104, 333);
		getContentPane().add(scrollPane_2);
		
		model = new DefaultListModel();
		listInterval = new JList(model);
		
		scrollPane_2.setViewportView(listInterval);
		
		JButton btnAddSeconds = new JButton("시간추가");
		btnAddSeconds.addActionListener(new ActionListener() {
			private int oldSeconds;

			public void actionPerformed(ActionEvent e) {
				int nowSeconds = seconds;
				model.addElement(nowSeconds - oldSeconds);	
				oldSeconds = nowSeconds;				
			}
		});
		btnAddSeconds.setBounds(772, 216, 97, 23);
		getContentPane().add(btnAddSeconds);
		
		JButton btnInsertInterval = new JButton("테이블에 추가");
		btnInsertInterval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
					Statement stmt = con.createStatement();			
					
					String temp = "";
					for(int i=0;i<model.getSize();i++)
						temp = temp + model.get(i) + ",";
					
					String sql = "insert into lyricsTBL values('";
					sql = sql + listMp3.getSelectedValue() + "','";
					sql = sql + temp + "')";
					stmt.executeUpdate(sql);
					
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnInsertInterval.setBounds(742, 147, 127, 59);
		getContentPane().add(btnInsertInterval);
	}
	
	protected void showInformation() {
		File fs = new File(tfFilePath.getText());
		MP3File mp3 = null;
        try{
            mp3 = (MP3File) AudioFileIO.read(fs);
            AbstractID3v2Tag tag2 = mp3.getID3v2Tag();
            
            Tag tag = mp3.getTag();
            String title = tag.getFirst(FieldKey.TITLE);
            String artist = tag.getFirst(FieldKey.ARTIST);
            String album = tag.getFirst(FieldKey.ALBUM);
            String year = tag.getFirst(FieldKey.YEAR);
            String genre = tag.getFirst(FieldKey.GENRE);
            String lyrics = tag.getFirst(FieldKey.LYRICS);
     
//            System.out.println("Tag : " + tag2);
//            System.out.println("Song Name : " + title);
//            System.out.println("Artist : " + artist);
//            System.out.println("Album : " + album);
//            System.out.println("Year : " + year);
//            System.out.println("Genre : " + genre);
//            System.out.println("가사 : " + lyrics);
            
            String strLyrics[] = lyrics.split("\n");
            taLyrics.setListData(strLyrics);
            taLyrics.setSelectedIndex(0);
            
            try {
    			Class.forName("com.mysql.cj.jdbc.Driver");
    			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
    			Statement stmt = con.createStatement();			
    			
    			String sql = "select intervalTimes from lyricsTBL where title='" + listMp3.getSelectedValue() + "'";
    			System.out.println(sql);
    			ResultSet rs = stmt.executeQuery(sql);    			
    			
    			if(rs.next()) {
    				String arrLyrics[] = rs.getString("intervalTimes").split(",");
    				listInterval.setListData(arrLyrics);
    			}
    			
    		} catch (ClassNotFoundException | SQLException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
            //taLyrics.setText(lyrics);
            //taLyrics.setCaretPosition(0);
        }catch(Exception ex){
            ex.printStackTrace();
        }		
        
        // 시간 측정       
        Header h = null;
        FileInputStream file = null;
        Bitstream bitstream ;
        long tn = 0;
        try {
            file = new FileInputStream(tfFilePath.getText());
            bitstream = new Bitstream(file);
            h = bitstream.readFrame();
            tn = file.getChannel().size();
            System.out.println((int)h.total_ms((int)(tn - mp3.getMP3StartByte(fs)))/1000);
            total = (int)h.total_ms((int)(tn - mp3.getMP3StartByte(fs)))/1000;
        } catch (IOException | BitstreamException | InvalidAudioFrameException ex) {
            ex.printStackTrace();
        }    
        
	}

	protected void musicQ() {
		btnPause.setEnabled(true);
		btnPause.setText("일시정지");
		playThread = new Thread(runnablePlay);
		if(player != null) { // 재생되고 있다
			player.close();
			playThread.start();
		}
		else
			playThread.start();	
		
		
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
	
	TimerTask task = new TimerTask() {

		@Override
		public void run() {
			seconds++;
			
			lblSeconds.setText(seconds + "/" + total);
			progressBar.setValue(seconds*100/total);
			if(seconds==total) {
				seconds=0;
				seq=0;
				int idx = listMp3.getSelectedIndex();
				listMp3.setSelectedIndex(idx+1);
				showInformation();
				musicQ();
			}
				
//			Calendar current = Calendar.getInstance();
//			int hour = current.get(Calendar.HOUR);
//			int minute = current.get(Calendar.MINUTE);
//			int second = current.get(Calendar.SECOND);
//			
//			if((int)spinnerHour.getValue() == hour && (int)spinnerMinute.getValue()==minute 
//					&& (int)spinnerSecond.getValue()==second) {
//				System.out.println("알람 울림");
//				musicQ();
//			}
//			int total = (int)spinnerHour.getValue() * 3600 + 
//					(int)spinnerMinute.getValue() * 60 + 
//					(int)spinnerSecond.getValue();
//			int curTotal = hour * 3600 + minute * 60 + second;
//			progressBar.setValue(curTotal*100/total);
		}		
	};
	
	TimerTask taskInterval = new TimerTask() {

		@Override
		public void run() {			
			listInterval.setSelectedIndex(seq);
			taLyrics.setSelectedIndex(seq);
			lblLyrics.setText(taLyrics.getSelectedValue().toString());	
			int sec = Integer.parseInt(listInterval.getSelectedValue().toString());
			try {
				Thread.sleep(sec*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			seq++;
		}		
	};
}
