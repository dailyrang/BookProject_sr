package rental;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class WinRentalList extends JDialog {
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinRentalList dialog = new WinRentalList();
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
	public WinRentalList() {
		setTitle("대여된 책들");
		setBounds(100, 100, 757, 540);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		String columnNames[] = {"번호","제목","이름","빌려간 날짜", "반환"};//번호:rental, 책제목:book, 이름:member, 빌려:rental
		//String columnNames[] = {"IDX","ISBN","ID","RentDate", "ReturnDate"};
		DefaultTableModel dtm = new DefaultTableModel(columnNames,0);
		table = new JTable(dtm);	
		scrollPane.setViewportView(table);
		showRecords();
	}
	private void showRecords() {
		DefaultTableModel dtm = (DefaultTableModel)table.getModel();		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();			
			String sql = "select R.idx, B.title, M.name, R.rentDate, R.bRental from rentalTBL R";
			sql = sql + " INNER JOIN bookTBL B ON R.isbn=B.isbn";
			sql = sql + " INNER JOIN memberTBL M ON R.id=M.id";
			sql = sql + " where R.bRental=0";
			
			ResultSet rs = stmt.executeQuery(sql);			
			while(rs.next()) {
				Vector<String> vector = new Vector<>();
				for(int i=1; i<=5; i++)
					vector.add(rs.getString(i));
				dtm.addRow(vector);
			}
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
