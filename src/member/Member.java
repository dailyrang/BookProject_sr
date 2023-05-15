package member;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.WinCalendar;
import util.WinSearchDoro;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPasswordField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Member extends JPanel {
	private JTextField tfId;
	private JTextField tfName;
	private JTextField tfEmail;
	private JTextField tfMobile;
	private JTextField tfBirth;
	private JTextField tfAddress;
	private JTextField tfAddressDetail;
	private JTextField tfSolarBirth;
	private JPasswordField tfPassword;
	private JPasswordField tfPassword_1;
	private JButton btnMemberChoice;
	
	private int number;  // type 값 : 1(등록) 2(탈퇴) 3(변경) 4 (검색)
	private JComboBox cbEmail;
	private JCheckBox chkSolarLunar;
	private String path;
	private JLabel lblPic;
	
	/**
	 * Create the dialog.
	 */
	public Member() {
		setBackground(new Color(255, 218, 185));
		setBounds(100, 100, 525, 380);
		setLayout(null);
		
		lblPic = new JLabel("");
		lblPic.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					JFileChooser chooser = new JFileChooser("c:/FileIO/kakaoImage/");
					FileNameExtensionFilter filter = new FileNameExtensionFilter("이미지파일","gif","jpg","png","bmp");
					chooser.setFileFilter(filter);
					if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						path = chooser.getSelectedFile().getPath();
						ImageIcon icon = new ImageIcon(path);
						Image image = icon.getImage();
						image = image.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
						icon = new ImageIcon(image);
						lblPic.setIcon(icon);
						path = path.replaceAll("\\\\","\\\\\\\\\\\\\\\\" );
					}
				}
			}
		});
		lblPic.setBackground(new Color(255, 255, 0));
		lblPic.setOpaque(true);
		lblPic.setBounds(12, 10, 150, 200);
		add(lblPic);
		
		JLabel lblId = new JLabel("Id");
		lblId.setBounds(197, 21, 57, 15);
		add(lblId);
		
		tfId = new JTextField();
		tfId.setBackground(new Color(192, 192, 192));
		tfId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					tfPassword.requestFocus();
				}
			}
		});
		tfId.setBounds(278, 18, 116, 21);
		add(tfId);
		tfId.setColumns(10);
		
		JButton btnDup = new JButton("중복확인");
		btnDup.setBounds(419, 17, 97, 23);
		add(btnDup);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(197, 60, 88, 15);
		add(lblPassword);
		
		JLabel lblPassword_1 = new JLabel("Password 확인");
		lblPassword_1.setBounds(197, 88, 97, 15);
		add(lblPassword_1);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(197, 125, 57, 15);
		add(lblName);
		
		tfName = new JTextField();
		tfName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					tfEmail.requestFocus();
				}
			}
		});
		tfName.setColumns(10);
		tfName.setBounds(266, 122, 116, 21);
		add(tfName);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(197, 162, 57, 15);
		add(lblEmail);
		
		tfEmail = new JTextField();
		tfEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					String sEmail = tfEmail.getText() + "@" + cbEmail.getSelectedItem();
					boolean bOne = false;
					int zero=1;
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
						Statement stmt = con.createStatement();
						
						String sql = "select count(*) from memberTBL where email='" + sEmail + "'";
						ResultSet rs = stmt.executeQuery(sql);
						
						while(rs.next()) {
							if(rs.getInt(1) == 1)
								bOne=true;
							else if(rs.getInt(1) > 1)
								bOne=false;
							else
								zero = 0;
						}						
						
					} catch (ClassNotFoundException | SQLException e1) {
						e1.printStackTrace();
					}
					if(zero==1 && bOne)
						showRecord(sEmail);
					
				}
			}
		});
		tfEmail.setBackground(new Color(192, 192, 192));
		tfEmail.setColumns(10);
		tfEmail.setBounds(266, 159, 116, 21);
		add(tfEmail);
		
		JLabel lblMobile = new JLabel("Mobile");
		lblMobile.setBounds(197, 204, 57, 15);
		add(lblMobile);
		
		tfMobile = new JTextField();
		tfMobile.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {					 
					if(isCorrect(tfMobile.getText()))// 하이픈 다 없애고 전화번호의 갯수가 11개인지 확인
						tfBirth.requestFocus();
					else {
						JOptionPane.showMessageDialog(null, "전화번호에 문제가 있습니다");		
						tfMobile.setSelectionStart(0);
						tfMobile.setSelectionEnd(tfMobile.getText().length());
					}
						
				}
			}
		});
		tfMobile.setColumns(10);
		tfMobile.setBounds(266, 201, 177, 21);
		add(tfMobile);
		
		JLabel lblBirth = new JLabel("Birth");
		lblBirth.setBounds(88, 233, 36, 15);
		add(lblBirth);
		
		tfBirth = new JTextField();
		tfBirth.setColumns(10);
		tfBirth.setBounds(129, 229, 116, 21);
		add(tfBirth);
		
		chkSolarLunar = new JCheckBox("양력");
		chkSolarLunar.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(chkSolarLunar.isSelected()) {
					chkSolarLunar.setText("음력");
					tfSolarBirth.setText("나중에...");
				}else {
					chkSolarLunar.setText("양력");
					tfSolarBirth.setText(tfBirth.getText());
				}
				tfAddress.requestFocus();
			}
		});
		chkSolarLunar.setBounds(12, 228, 68, 23);
		add(chkSolarLunar);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setBounds(12, 270, 57, 15);
		add(lblAddress);
		
		tfAddress = new JTextField();
		tfAddress.setColumns(10);
		tfAddress.setBounds(81, 267, 301, 21);
		add(tfAddress);
		
		JButton btnSearchAddress = new JButton("주소 찾기...");
		btnSearchAddress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinSearchDoro winSearchDoro = new WinSearchDoro();
				winSearchDoro.setModal(true);
				winSearchDoro.setVisible(true);
				
				tfAddress.setText(winSearchDoro.getAddress());
				tfAddressDetail.requestFocus();
			}
		});
		btnSearchAddress.setBounds(407, 266, 109, 53);
		add(btnSearchAddress);
		
		tfAddressDetail = new JTextField();
		tfAddressDetail.setColumns(10);
		tfAddressDetail.setBounds(81, 298, 301, 21);
		add(tfAddressDetail);
		
		JButton btnCalendar = new JButton("달력 선택...");
		btnCalendar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinCalendar winCalendar = new WinCalendar();
				winCalendar.setModal(true);
				winCalendar.setVisible(true);
				tfBirth.setText(winCalendar.getDate());
				tfSolarBirth.setText(winCalendar.getDate());
				
				tfAddress.requestFocus();
			}
		});
		btnCalendar.setBounds(257, 229, 97, 23);
		add(btnCalendar);
		
		JLabel lblAt = new JLabel("@");
		lblAt.setBounds(393, 162, 17, 15);
		add(lblAt);
		
		cbEmail = new JComboBox();
		cbEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!cbEmail.getSelectedItem().toString().equals("직접 입력"))
					tfMobile.requestFocus();
				else {
					cbEmail.setSelectedItem("");
					cbEmail.requestFocus();
				}
					
			}
		});
		cbEmail.setModel(new DefaultComboBoxModel(new String[] {"naver.com", "google.com", "daum.net", "korea.com", "paran.com", "ici.re.kr", "sorry.com", "happy.net", "직접 입력"}));
		cbEmail.setEditable(true);
		cbEmail.setBounds(419, 158, 97, 23);
		add(cbEmail);
		
		tfSolarBirth = new JTextField();
		tfSolarBirth.setColumns(10);
		tfSolarBirth.setBounds(400, 230, 116, 21);
		add(tfSolarBirth);
		
		JLabel lblAddress_1 = new JLabel("상세주소");
		lblAddress_1.setBounds(12, 304, 57, 15);
		add(lblAddress_1);
		
		tfPassword = new JPasswordField();
		tfPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					String pw2 = new String(tfPassword_1.getPassword());
					tfPassword_1.setSelectionStart(0);
					tfPassword_1.setSelectionEnd(pw2.length());
					tfPassword_1.requestFocus();
				}
			}
		});
		tfPassword.setBounds(297, 49, 97, 21);
		add(tfPassword);
		
		tfPassword_1 = new JPasswordField();
		tfPassword_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					String pw1 = new String(tfPassword.getPassword());
					String pw2 = new String(tfPassword_1.getPassword());
					if(pw1.equals(pw2))
						tfName.requestFocus();
					else {
						JOptionPane.showMessageDialog(null, "암호가 일치하지 않습니다");
						tfPassword.setSelectionStart(0);
						tfPassword.setSelectionEnd(pw1.length());
						tfPassword.requestFocus();
					}
						
				}
			}
		});
		
		tfPassword_1.setBounds(297, 85, 97, 21);
		add(tfPassword_1);
		
		JLabel lblBirth_1 = new JLabel("올해");
		lblBirth_1.setBounds(366, 233, 36, 15);
		add(lblBirth_1);
		
		btnMemberChoice = new JButton("회원 등록");  // "회원 등록/변경/탈퇴/검색"으로 변경된다.
		btnMemberChoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(number == 1) {
					insertRecord();
				}else if(number == 2) {
					deleteRecord();
				}
			}
		});
		btnMemberChoice.setBounds(197, 329, 116, 37);
		add(btnMemberChoice);

	}

	protected void showRecord(String sEmail) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();
			
			String sql = "select * from memberTBL where email='" + sEmail + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				tfId.setText(rs.getString("id"));
				//tfPassword는 삭제를 결정했을 때 인증 수단으로 써 보자.
				tfName.setText(rs.getString("name"));
				tfMobile.setText(rs.getString("mobile"));
				tfBirth.setText(rs.getString("birth"));
				tfAddress.setText(rs.getString("address"));
				if(rs.getInt("lsType")==0)
					chkSolarLunar.setSelected(false);
				else
					chkSolarLunar.setSelected(true);
				
				
				path = rs.getString("pic");
				ImageIcon icon = new ImageIcon(path);
				Image image = icon.getImage();
				image = image.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
				icon = new ImageIcon(image);
				lblPic.setIcon(icon);
				path = path.replaceAll("\\\\","\\\\\\\\\\\\\\\\" );
			}			
			
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}	
		
	}

	protected void deleteRecord() {
		// TODO Auto-generated method stub
		
	}

	protected void insertRecord() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();
			
			String sql = "insert into memberTBL values('" + tfId.getText() + "','";
			sql = sql + new String(tfPassword.getPassword()) + "','" + tfName.getText() + "','";
			sql = sql + tfEmail.getText() + "@" + cbEmail.getSelectedItem() + "','";
			sql = sql + tfMobile.getText() + "','" + tfBirth.getText() + "'," ;
			if(chkSolarLunar.isSelected()) // 양력(0), 음력(1)
				sql = sql + "1,'";
			else
				sql = sql + "0,'";
			sql = sql + tfAddress.getText() + " " + tfAddressDetail.getText() + "','";
			sql = sql + path + "')";
			
			stmt.executeUpdate(sql);  // 레코드 삽입
			
			
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}	
		
	}

	public Member(int type) {
		this();
		if(type == 1)
			btnMemberChoice.setText("회원 가입");
		else if(type == 2)
			btnMemberChoice.setText("회원 탈퇴");
		else if(type == 3)
			btnMemberChoice.setText("회원 변경");
		else if(type == 4)
			btnMemberChoice.setText("회원 검색");
		
		number = type;
	}

	protected boolean isCorrect(String text) {
		text = text.replace("-", "");
		if(text.length() == 8)
			text = "010" + text;
		
		text = text.substring(0,3) + "-" + text.substring(3,7) + "-" + text.substring(7);
		
		if(text.length()==13) {
			tfMobile.setText(text);
			return true;
		}else {			
			return false;
		}
	}

	protected int getCountHyphen(String text) {
		int count = 0;
		for(int i=0; i<text.length();i++)
			if(text.charAt(i) == '-')
				count++;
		
		return count;
	}

	
}
