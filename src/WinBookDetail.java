import java.awt.EventQueue;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class WinBookDetail extends JDialog {
	private JTextField tfISBN;
	private JTextField tfTitle;
	private JTextField tfAuthor;
	private JTextField tfPublisher;
	private JTextField tfPdate;
	private JTextField tfDiscount;
	private JTextArea taDescription;
	private JLabel lblImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinBookDetail dialog = new WinBookDetail("9791137234451");
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
	public WinBookDetail() {
		setTitle("책 정보");
		setBounds(100, 100, 614, 610);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		lblImage = new JLabel("");
		lblImage.setOpaque(true);
		lblImage.setBackground(new Color(255, 255, 0));
		lblImage.setBounds(12, 10, 200, 250);
		panel.add(lblImage);
		
		JLabel lblISBN = new JLabel("ISBN:");
		lblISBN.setBounds(224, 10, 57, 15);
		panel.add(lblISBN);
		
		tfISBN = new JTextField();
		tfISBN.setBounds(293, 7, 124, 21);
		panel.add(tfISBN);
		tfISBN.setColumns(10);
		
		tfTitle = new JTextField();
		tfTitle.setColumns(10);
		tfTitle.setBounds(293, 52, 293, 21);
		panel.add(tfTitle);
		
		JLabel lblTitle = new JLabel("책제목:");
		lblTitle.setBounds(224, 55, 57, 15);
		panel.add(lblTitle);
		
		tfAuthor = new JTextField();
		tfAuthor.setColumns(10);
		tfAuthor.setBounds(293, 95, 293, 21);
		panel.add(tfAuthor);
		
		JLabel lblAuthor = new JLabel("저자:");
		lblAuthor.setBounds(224, 98, 57, 15);
		panel.add(lblAuthor);
		
		tfPublisher = new JTextField();
		tfPublisher.setColumns(10);
		tfPublisher.setBounds(293, 142, 293, 21);
		panel.add(tfPublisher);
		
		JLabel lblPublisher = new JLabel("출판사:");
		lblPublisher.setBounds(224, 145, 57, 15);
		panel.add(lblPublisher);
		
		tfPdate = new JTextField();
		tfPdate.setColumns(10);
		tfPdate.setBounds(293, 191, 293, 21);
		panel.add(tfPdate);
		
		JLabel lblPdate = new JLabel("출판일:");
		lblPdate.setBounds(224, 194, 57, 15);
		panel.add(lblPdate);
		
		tfDiscount = new JTextField();
		tfDiscount.setColumns(10);
		tfDiscount.setBounds(293, 239, 293, 21);
		panel.add(tfDiscount);
		
		JLabel lblDiscount = new JLabel("가격:");
		lblDiscount.setBounds(224, 242, 57, 15);
		panel.add(lblDiscount);
		
		JLabel lblDescription = new JLabel("책 소개:");
		lblDescription.setBounds(12, 279, 57, 15);
		panel.add(lblDescription);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(12, 304, 574, 257);
		panel.add(scrollPane);
		
		taDescription = new JTextArea();
		taDescription.setLineWrap(true);
		scrollPane.setViewportView(taDescription);

	}

	public WinBookDetail(String sISBN) throws IOException {
		this(); // 매개변수 0개 짜리 생성자 호출
		showRecord(sISBN);
	}

	private void showRecord(String sISBN) throws IOException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();
			
			String sql = "select * from bookTBL where isbn='" + sISBN + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				tfISBN.setText(sISBN);
				tfTitle.setText(rs.getString("title"));
				tfAuthor.setText(rs.getString("author"));
				tfPublisher.setText(rs.getString("publisher"));
				tfPdate.setText(rs.getString("pDate"));
				tfDiscount.setText(rs.getString("discount"));
				taDescription.setText(rs.getString("description"));
				
				String imgUrl = rs.getString("image");
				URL url = new URL(imgUrl);
				Image image = ImageIO.read(url);
				image = image.getScaledInstance(200, 250, Image.SCALE_SMOOTH);
				lblImage.setIcon(new ImageIcon(image));
			}
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}		
	}
}
