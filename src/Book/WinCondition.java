package Book;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WinCondition extends JDialog {
	private JTextField tfPrice1;
	private JTextField tfPrice2;
	private JTable table;
	private int type;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinCondition dialog = new WinCondition();
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
	public WinCondition() {
		setTitle("검색할 조건 입력");
		setBounds(100, 100, 396, 494);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("가격이");
		lblNewLabel.setBounds(22, 20, 57, 15);
		getContentPane().add(lblNewLabel);
		
		tfPrice1 = new JTextField();
		tfPrice1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					tfPrice2.requestFocus();
				}
			}
		});
		tfPrice1.setBounds(100, 17, 116, 21);
		getContentPane().add(tfPrice1);
		tfPrice1.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("이상이고");
		lblNewLabel_1.setBounds(235, 20, 57, 15);
		getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("미만인 것 검색");
		lblNewLabel_1_1.setBounds(235, 65, 123, 15);
		getContentPane().add(lblNewLabel_1_1);
		
		tfPrice2 = new JTextField();
		tfPrice2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					SearchISBN();
				}
			}
		});
		tfPrice2.setColumns(10);
		tfPrice2.setBounds(100, 62, 116, 21);
		getContentPane().add(tfPrice2);
		
		JButton btnSearch = new JButton("검색");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchISBN();
			}
		});
		btnSearch.setBounds(100, 107, 116, 23);
		getContentPane().add(btnSearch);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 156, 346, 289);
		getContentPane().add(scrollPane);
		
		String columnNames[] = { "ISBN", "Title", "Price" };
		DefaultTableModel dtm = new DefaultTableModel(columnNames, 0);
		table = new JTable(dtm);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if(row==-1)
					;
				else {
					if(JOptionPane.showConfirmDialog(null, "이동할까요?")==JOptionPane.YES_OPTION) {
						String sISBN = table.getValueAt(row, 0).toString();
						dispose();
						
						if(type==2) {
							WinBookDelete winBookDelete = new WinBookDelete(sISBN);
							winBookDelete.setModal(true);
							winBookDelete.setVisible(true);
						}else if(type == 3) {
							WinBookUpdate winBookUpdate = new WinBookUpdate(sISBN);
							winBookUpdate.setModal(true);
							winBookUpdate.setVisible(true);
						}
						
					}
				}
			}
		});
		scrollPane.setViewportView(table);

	}

	public WinCondition(int number) {
		this();
		type = number;
	}

	protected void SearchISBN() {
		// 조건에 맞는 레코드들을 찾아서 테이블에 표시한다.
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();
			
			String sql = "select isbn, title, discount from bookTBL where discount >= " + tfPrice1.getText();
			sql = sql + " and discount < " + tfPrice2.getText() + " order by discount desc";
			
			ResultSet rs = stmt.executeQuery(sql);
			DefaultTableModel dtm = (DefaultTableModel) table.getModel();
			dtm.setRowCount(0); // 테이블 내용 모두 삭제
			
			while(rs.next()) {
				Vector<String> vector = new Vector<>();
				vector.add(rs.getString("isbn"));
				vector.add(rs.getString("title"));
				vector.add(rs.getString("discount"));
				dtm.addRow(vector);
			}
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
