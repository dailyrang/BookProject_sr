package Book;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Image;

import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.WinCalendar;

import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.im.InputContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Book extends JPanel {
	private JTextField tfISBN;
	private JTextField tfTitle;
	private JTextField tfAuthor;
	private JTextField tfPdate;
	private JTextField tfDiscount;
	private JTextArea taDescription;
	private JLabel lblImage;
	private JButton btnType;
	private JButton btnDup;
	private JButton btnCalendar;
	private JComboBox cbPublisher;
	private String filePath;
	private String strImage;
	
	public Book() {
		setBackground(new Color(192, 192, 192));
		setLayout(null);
		
		lblImage = new JLabel("");
		lblImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) { // 더블클릭하여 그림 선택
					strImage = JOptionPane.showInputDialog("책의 URL을 입력하시오");
					lblImage.setText("<html><body><img src='" + strImage + "' width=200 height=250></body></html>");
				}
			}
		});
		lblImage.setToolTipText("더블클릭해서 그림 선택하기");
		lblImage.setOpaque(true);
		lblImage.setBackground(Color.YELLOW);
		lblImage.setBounds(29, 35, 200, 250);
		add(lblImage);
		
		JLabel lblISBN = new JLabel("ISBN : ");
		lblISBN.setBounds(243, 35, 57, 15);
		add(lblISBN);
		
		tfISBN = new JTextField();
		tfISBN.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					checkDup();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(tfISBN.getText().length() == 13 && isDigit(tfISBN.getText()))
					changeEnComps();
				else
					changeDisComps();
			}
		});
		tfISBN.setColumns(10);
		tfISBN.setBounds(311, 32, 160, 21);
		add(tfISBN);
		
		JLabel lblTitle = new JLabel("책제목 : ");
		lblTitle.setBounds(241, 63, 57, 15);
		add(lblTitle);
		
		tfTitle = new JTextField();
		tfTitle.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				try {  
                    InputContext inCtx = tfTitle.getInputContext();  
                    Character.Subset[] subset = { Character.UnicodeBlock.HANGUL_SYLLABLES }; 
                     inCtx.setCharacterSubsets( subset ); 
             }catch (Exception x) { } 
			}
		});
		tfTitle.setColumns(10);
		tfTitle.setBounds(311, 60, 318, 21);
		add(tfTitle);
		
		JLabel lblAuthor = new JLabel("저자 : ");
		lblAuthor.setBounds(241, 94, 57, 15);
		add(lblAuthor);
		
		tfAuthor = new JTextField();
		tfAuthor.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				try {  
                    InputContext inCtx = tfAuthor.getInputContext();  
                    Character.Subset[] subset = { Character.UnicodeBlock.HANGUL_SYLLABLES }; 
                     inCtx.setCharacterSubsets( subset ); 
             }catch (Exception x) { } 
			}
		});
		tfAuthor.setColumns(10);
		tfAuthor.setBounds(311, 91, 318, 21);
		add(tfAuthor);
		
		JLabel lblPublisher = new JLabel("출판사 : ");
		lblPublisher.setBounds(241, 122, 57, 15);
		add(lblPublisher);
		
		JLabel lblPdate = new JLabel("출판일 : ");
		lblPdate.setBounds(241, 150, 57, 15);
		add(lblPdate);
		
		tfPdate = new JTextField();
		tfPdate.setColumns(10);
		tfPdate.setBounds(311, 147, 160, 21);
		add(tfPdate);
		
		JLabel lblDiscount = new JLabel("가격 : ");
		lblDiscount.setBounds(241, 178, 57, 15);
		add(lblDiscount);
		
		tfDiscount = new JTextField();
		tfDiscount.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {  // F2:0000, F3:000, F4:00
				String amount =  tfDiscount.getText();
				tfDiscount.setText(amount.replaceAll(",", ""));
				
				if(e.getKeyCode() == KeyEvent.VK_F2)
					tfDiscount.setText(tfDiscount.getText() + "0000");
				else if(e.getKeyCode() == KeyEvent.VK_F3)
					tfDiscount.setText(tfDiscount.getText() + "000");
				else if(e.getKeyCode() == KeyEvent.VK_F4)
					tfDiscount.setText(tfDiscount.getText() + "00");
				
				
				amount =  tfDiscount.getText();
				amount = amount.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
				tfDiscount.setText(amount);
				
			}
		});
		tfDiscount.setHorizontalAlignment(SwingConstants.RIGHT);
		tfDiscount.setColumns(10);
		tfDiscount.setBounds(311, 175, 160, 21);
		add(tfDiscount);
		
		JLabel lblDescription = new JLabel("책 소개 : ");
		lblDescription.setBounds(29, 295, 57, 15);
		add(lblDescription);
		
		JScrollPane pane = new JScrollPane();
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setBounds(29, 320, 612, 181);
		add(pane);
		
		taDescription = new JTextArea();
		taDescription.setLineWrap(true);
		pane.setViewportView(taDescription);
		
		btnType = new JButton("도서 추가");
		btnType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnType.getText().equals("도서 추가")) {
					insertBook();
				}
				else if(btnType.getText().equals("도서 삭제")) {
					if(JOptionPane.showConfirmDialog(null, "정말 삭제할까요?") == JOptionPane.YES_OPTION)
						deleteBook();
				}
				else {
					if(JOptionPane.showConfirmDialog(null, "정말 변경할까요?") == JOptionPane.YES_OPTION)
						updateBook();
				}
			}
		});
		btnType.setBounds(369, 219, 115, 66);
		add(btnType);
		
		btnDup = new JButton("중복확인...");
		btnDup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkDup();
			}
		});
		btnDup.setEnabled(false);
		btnDup.setBounds(482, 31, 97, 23);
		add(btnDup);
		
		btnCalendar = new JButton("달력...");
		btnCalendar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinCalendar winCalendar = new WinCalendar();
				winCalendar.setModal(true);
				winCalendar.setVisible(true);
				tfPdate.setText(winCalendar.getDate());
			}
		});
		btnCalendar.setBounds(483, 146, 97, 23);
		add(btnCalendar);
		
		cbPublisher = new JComboBox();
		cbPublisher.setEditable(true);
		cbPublisher.setBounds(311, 116, 316, 21);
		add(cbPublisher);
		
		
		changeDisComps();
	}
	protected void updateBook() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			
//			String sql = "update bookTBL set title=?, author=?, publisher=?, image=?, pDate=?, discount=?, description=? where isbn=?";	
//						
//			PreparedStatement pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, tfTitle.getText());
//			pstmt.setString(2, tfAuthor.getText());
//			pstmt.setString(3, cbPublisher.getSelectedItem().toString());
//			pstmt.setString(4, "http://");
//			pstmt.setString(5, tfPdate.getText());
//			pstmt.setString(6, tfDiscount.getText());
//			pstmt.setString(7, taDescription.getText());
//			pstmt.setString(8, tfISBN.getText());
//			pstmt.executeUpdate();
			
			String sql = "update bookTBL set title='" + tfTitle.getText() + "', author='" + tfAuthor.getText();
			sql = sql + "', publisher='" + cbPublisher.getSelectedItem().toString() + "', image='" + strImage;
			sql = sql + "', pDate='" + tfPdate.getText() + "', discount=" + tfDiscount.getText();
			sql = sql + ", description='" + taDescription.getText() +  "' where isbn='";
			sql = sql + tfISBN.getText() + "'";
			
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			
			
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	protected void deleteBook() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			
			String sql = "delete from bookTBL where isbn='" + tfISBN.getText() + "'";		
			Statement stmt = con.createStatement();
			
			stmt.executeUpdate(sql);			
			
			
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	protected void insertBook() {
		// PreparedStatement 이용	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			
//			String sql = "insert into bookTBL values('" + tfISBN.getText() + "','";
//			sql = sql + tfTitle.getText() + "','" + tfAuthor.getText() + "','" ;
//			sql = sql + cbPublisher.getSelectedItem() + "','" + filePath + "','";
//			sql = sql + tfPdate.getText() + "'," + tfDiscount.getText().replaceAll(",", "") + ",'";
//			sql = sql + taDescription.getText() + "')";			
//			Statement stmt = con.createStatement();
			
			String sql = "insert into bookTBL values(?,?,?,?,?,?,?,?)";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, tfISBN.getText());
			pstmt.setString(2, tfTitle.getText());
			pstmt.setString(3, tfAuthor.getText());
			pstmt.setString(4, cbPublisher.getSelectedItem().toString());
			pstmt.setString(5, filePath);
			pstmt.setString(6, tfPdate.getText());
			pstmt.setInt(7, Integer.parseInt(tfDiscount.getText().replaceAll(",", "")));
			pstmt.setString(8, taDescription.getText());
			pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
	protected void checkDup() {
		//DB 연동
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();
			
			String sql = "select count(*) from bookTBL where isbn = '" + tfISBN.getText() + "'";
			ResultSet rs = stmt.executeQuery(sql);
					
			while(rs.next()) {
				if(rs.getInt(1) == 0)
					tfTitle.requestFocus();
				else {
					tfISBN.setSelectionStart(0);  // 첫번째 글짜부터
					tfISBN.setSelectionEnd(tfISBN.getText().length()); // 마지막 글짜까지 블록으로 선택
					tfISBN.requestFocus();
				}
			}
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	protected boolean isDigit(String strISBN) {
		 return strISBN.matches("\\d*");
		 
//		for(int i=0; i<strISBN.length(); i++)
//			if(strISBN.charAt(i) >= '0' && strISBN.charAt(i) <= '9')
//				continue;
//			else
//				return false;
//		return true;
	}
	protected void changeDisComps() {
		btnDup.setEnabled(false);
		tfTitle.setEnabled(false);
		tfAuthor.setEnabled(false);
		cbPublisher.setEnabled(false);
		tfPdate.setEnabled(false);
		tfDiscount.setEnabled(false);
		taDescription.setEnabled(false);
		btnCalendar.setEnabled(false);
		btnType.setEnabled(false);
	}
	protected void changeEnComps() {
		btnDup.setEnabled(true);
		tfTitle.setEnabled(true);
		tfAuthor.setEnabled(true);		
		cbPublisher.setEnabled(true);
		tfPdate.setEnabled(true);
		tfDiscount.setEnabled(true);
		taDescription.setEnabled(true);
		btnCalendar.setEnabled(true);
		btnType.setEnabled(true);
		
		showPublishers();
	}
	
	private void showPublishers() {
		//DB 연동
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();
			
			String sql = "select distinct publisher from bookTBL";
			ResultSet rs = stmt.executeQuery(sql);
			cbPublisher.removeAllItems();		
			while(rs.next()) {
				cbPublisher.addItem(rs.getString("publisher"));
			}
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
	}
	public Book(String sISBN, String sTitle, String sAuthor, String sPublisher, String sImage, 
			String sPdate, String sDiscount, String sDescription) {
		this();		
		tfISBN.setText(sISBN);
		tfTitle.setText(sTitle);
		tfAuthor.setText(sAuthor);
		cbPublisher.setSelectedItem(sPublisher);
		tfPdate.setText(sPdate);
		tfDiscount.setText(sDiscount);
		taDescription.setText(sDescription);
		lblImage.setText("<html><body><img src='" + sImage + "' width=200 height=250></body></html>");
	}
	public Book(String[] arrBook) {
		this();
		changeEnComps();
		tfISBN.setText(arrBook[0]);
		tfTitle.setText(arrBook[1]);
		tfAuthor.setText(arrBook[2]);
		//tfPublisher.setText(arrBook[3]);
		cbPublisher.setSelectedItem(arrBook[3]);
		tfPdate.setText(arrBook[5]);
		tfDiscount.setText(arrBook[6]);
		taDescription.setText(arrBook[7]);
		lblImage.setText("<html><body><img src='" + arrBook[4] + "' width=200 height=250></body></html>");
	}
	public Book(int number) {
		this();
		if(number==1) { //삽입
			btnType.setText("도서 추가");
		}else if(number==2) { //삭제
			btnType.setText("도서 삭제");
			btnDup.setEnabled(false);
		}else if(number==3) { //변경
			btnType.setText("도서 변경");
			btnDup.setEnabled(false);
		}
	}
	public Book(int number, String sISBN) {
		this(number);
		tfISBN.setText(sISBN);
		tfISBN.setEnabled(false); // 삭제, 변경시는 활성화할 필요가 없기 때문에.
		btnDup.setVisible(false);
		tfTitle.setEnabled(true);
		tfAuthor.setEnabled(true);		
		cbPublisher.setEnabled(true);
		tfPdate.setEnabled(true);
		tfDiscount.setEnabled(true);
		taDescription.setEnabled(true);
		
		if(number==2)
			btnCalendar.setVisible(false);
		else if(number==3) {
			btnCalendar.setVisible(true);
			btnCalendar.setEnabled(true);
		}
		
		btnType.setEnabled(true);
		
		showRecord(sISBN);
	}
	private void showRecord(String sISBN) {
		//DB 연동
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();
			
			String sql = "select * from bookTBL where isbn='" + sISBN + "'";
			ResultSet rs = stmt.executeQuery(sql);
				
			while(rs.next()) {
				tfTitle.setText(rs.getString("title"));
				tfAuthor.setText(rs.getString("author"));
				cbPublisher.setSelectedItem(rs.getString("publisher"));
				strImage = rs.getString("image");
				lblImage.setText("<html><body><img src='" + strImage + "' width=200 height=250></body></html>");
				tfPdate.setText(rs.getString("pDate"));
				tfDiscount.setText(rs.getString("discount"));
				taDescription.setText(rs.getString("description"));
			}
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
