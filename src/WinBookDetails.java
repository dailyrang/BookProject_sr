import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

public class WinBookDetails extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinBookDetails dialog = new WinBookDetails();
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
	public WinBookDetails() {
		setTitle("모든 책 자세히 보기");
		setBounds(100, 100, 766, 601);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		//DB 연동
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();
			
			String sql = "select * from bookTBL order by title asc";
			ResultSet rs = stmt.executeQuery(sql);
			
			String arrBook[] = new String[8];			
			while(rs.next()) {
				for(int i=0;i<8;i++)
					arrBook[i] = new String(rs.getString(i+1));
				String title = arrBook[1];
				if(title.length() > 13)
					tabbedPane.addTab(title.substring(0,10)+"...", new Book(arrBook));
				else
					tabbedPane.addTab(title, new Book(arrBook));
				
//				String sISBN = rs.getString("ISBN");
//				String sTitle = rs.getString("title");
//				String sAuthor = rs.getString("author");
//				String sPublisher = rs.getString("publisher");
//				String sImage = rs.getString("image");
//				String sPdate = rs.getString("pDate");
//				String sDiscount = rs.getString("discount");
//				String sDescription = rs.getString("description");	
//				tabbedPane.addTab(sTitle, new Book(sISBN, sTitle, sAuthor, sPublisher,
//						sImage, sPdate, sDiscount, sDescription));
			}
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
