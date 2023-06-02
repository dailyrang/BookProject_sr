import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import Book.WinBookDelete;
import Book.WinBookDetail;
import Book.WinBookDetails;
import Book.WinBookInsert;
import Book.WinBookUpdate;
import Book.WinCondition;
import member.WinMemberAdd;
import member.WinMemberRemove;
import member.WinMemberSelect;
import member.WinMemberUpdate;
import rental.WinRentalList;
import rental.WinReturnList;
import util.WinMp3Player;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;
import javax.swing.JSeparator;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JProgressBar;

public class WinMain extends JDialog {
	private JTable table;
	private JTextField tfMobile;
	private static JMenuItem mnuReturn;
	private static JMenuItem mnuRent;


	/**
	 * Create the dialog.
	 */
	public WinMain() {
		addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
				String columns[] = {"ISBN","제목","저자","출판사","이미지URL","출판일","가격","책 소개","권수"};
				DefaultTableModel dtm = (DefaultTableModel)table.getModel();
				dtm.setColumnIdentifiers(columns);
				showRecords(dtm);
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		setTitle("도서 관리 프로그램");
		setBounds(100, 100, 1178, 634);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnuFile = new JMenu("File");
		mnuFile.setMnemonic('F');
		menuBar.add(mnuFile);
		
		JMenuItem mnuExit = new JMenuItem("Exit");
		mnuFile.add(mnuExit);
		
		JMenu mnuBookManger = new JMenu("도서관리");
		menuBar.add(mnuBookManger);
		
		JMenuItem mnuBookAdd = new JMenuItem("도서 등록...");
		mnuBookAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinBookInsert winBookInsert = new WinBookInsert();
				winBookInsert.setModal(true);
				winBookInsert.setVisible(true);
			}
		});
		mnuBookManger.add(mnuBookAdd);
		
		JMenuItem mnuBookRemove = new JMenuItem("도서 삭제...");
		mnuBookRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if(row == -1) {
					WinCondition winCondition = new WinCondition(2);
					winCondition.setModal(true);
					winCondition.setVisible(true);					
				}else {
					String sISBN = table.getValueAt(row, 0).toString();
					WinBookDelete winBookDelete = new WinBookDelete(sISBN);
					winBookDelete.setModal(true);
					winBookDelete.setVisible(true);
				}
			}
		});
		mnuBookManger.add(mnuBookRemove);
		
		JMenuItem mnuBookUpdate = new JMenuItem("도서 변경...");
		mnuBookUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if(row == -1) {
					WinCondition winCondition = new WinCondition(3);
					winCondition.setModal(true);
					winCondition.setVisible(true);	
				}else {
					String sISBN = table.getValueAt(row, 0).toString();
					WinBookUpdate winBookUpdate = new WinBookUpdate(sISBN);
					winBookUpdate.setModal(true);
					winBookUpdate.setVisible(true);
				}
				
			}
		});
		mnuBookManger.add(mnuBookUpdate);
		
		JMenuItem mnuBookSelect = new JMenuItem("도서 조회...");
		mnuBookManger.add(mnuBookSelect);
		
		mnuBookManger.addSeparator();
		
		JMenuItem mnuAllShow = new JMenuItem("모든 책 보기...");
		mnuAllShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinBookDetails winBookDetails = new WinBookDetails();
				winBookDetails.setModal(true);
				winBookDetails.setVisible(true);
			}
		});
		mnuBookManger.add(mnuAllShow);
		
		JMenu mnuMemberManager = new JMenu("회원관리");
		menuBar.add(mnuMemberManager);
		
		JMenuItem mnuMemberAdd = new JMenuItem("회원 가입...");
		mnuMemberAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinMemberAdd winMemberAdd = new WinMemberAdd(1);
				winMemberAdd.setModal(true);
				winMemberAdd.setVisible(true);
			}
		});
		mnuMemberManager.add(mnuMemberAdd);
		
		JMenuItem mnuMemberRemove = new JMenuItem("회원 삭제...");
		mnuMemberRemove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				WinMemberRemove winMemberRemove = new WinMemberRemove(2);
				winMemberRemove.setModal(true);
				winMemberRemove.setVisible(true);				
			}
		});
		mnuMemberManager.add(mnuMemberRemove);
		
		JMenuItem mnuMemberUpdate = new JMenuItem("회원 변경...");
		mnuMemberUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				WinMemberUpdate winMemberUpdate = new WinMemberUpdate(3);
				winMemberUpdate.setModal(true);
				winMemberUpdate.setVisible(true);	
				
			}
		});
		mnuMemberManager.add(mnuMemberUpdate);
		
		JMenuItem mnuMemberSelect = new JMenuItem("회원 조회...");
		mnuMemberSelect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				WinMemberSelect winMemberSelect = new WinMemberSelect(4);
				winMemberSelect.setModal(true);
				winMemberSelect.setVisible(true);	
				
			}
		});
		mnuMemberManager.add(mnuMemberSelect);
		
		JSeparator separator = new JSeparator();
		mnuMemberManager.add(separator);
		
		JMenuItem mnuMemberAllShow = new JMenuItem("모든 회원 보기...");
		mnuMemberManager.add(mnuMemberAllShow);
		
		JMenu mnuRentalReturn = new JMenu("대여/반납");
		menuBar.add(mnuRentalReturn);
		
		JMenuItem mnuRentalList = new JMenuItem("대여리스트...");
		mnuRentalList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinRentalList wrl = new WinRentalList();
				wrl.setModal(true);
				wrl.setVisible(true);
			}
		});
		mnuRentalReturn.add(mnuRentalList);
		
		JMenuItem mnuReturnList = new JMenuItem("반납리스트...");
		mnuReturnList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinReturnList wrl = new WinReturnList();
				wrl.setModal(true);
				wrl.setVisible(true);
			}
		});
		mnuRentalReturn.add(mnuReturnList);
		
		JMenu mnuMusic = new JMenu("음악");
		menuBar.add(mnuMusic);
		
		JMenuItem mnuMp3Player = new JMenuItem("MP3플레이어...");
		mnuMp3Player.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinMp3Player winMp3Player = new WinMp3Player();
				//winMp3Player.setModal(true);
				winMp3Player.setVisible(true);
			}
		});
		mnuMusic.add(mnuMp3Player);
		
		JMenu mnNewMenu = new JMenu("Help");
		mnNewMenu.setMnemonic('H');
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("회사 정보...");
		mnNewMenu.add(mntmNewMenuItem_4);
		
		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnBookAdd = new JButton("");
		btnBookAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinBookInsert winBookInsert = new WinBookInsert();
				winBookInsert.setModal(true);
				winBookInsert.setVisible(true);
			}
		});
		btnBookAdd.setIcon(new ImageIcon(WinMain.class.getResource("/image/bookAdd.png")));
		toolBar.add(btnBookAdd);
		
		JButton btnBookRemove = new JButton("");
		btnBookRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if(row == -1) {
					WinCondition winCondition = new WinCondition(2);
					winCondition.setModal(true);
					winCondition.setVisible(true);					
				}else {
					String sISBN = table.getValueAt(row, 0).toString();
					WinBookDelete winBookDelete = new WinBookDelete(sISBN);
					winBookDelete.setModal(true);
					winBookDelete.setVisible(true);
				}
			}
		});
		btnBookRemove.setIcon(new ImageIcon(WinMain.class.getResource("/image/bookRemove.png")));
		toolBar.add(btnBookRemove);
		
		JButton btnBookUpdate = new JButton("");
		btnBookUpdate.setIcon(new ImageIcon(WinMain.class.getResource("/image/bookUpdate.png")));
		toolBar.add(btnBookUpdate);
		
		JButton btnBookSearch = new JButton("");
		btnBookSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if(row == -1) {
					WinCondition winCondition = new WinCondition(3);
					winCondition.setModal(true);
					winCondition.setVisible(true);	
				}else {
					String sISBN = table.getValueAt(row, 0).toString();
					WinBookUpdate winBookUpdate = new WinBookUpdate(sISBN);
					winBookUpdate.setModal(true);
					winBookUpdate.setVisible(true);
				}
			}
		});
		btnBookSearch.setIcon(new ImageIcon(WinMain.class.getResource("/image/bookSearch.png")));
		toolBar.add(btnBookSearch);
		
		toolBar.addSeparator();
		
		tfMobile = new JTextField();
		tfMobile.setFont(new Font("굴림", Font.BOLD, 24));
		tfMobile.setHorizontalAlignment(SwingConstants.CENTER);
		tfMobile.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					showMembersByMobile();
			}
		});
		toolBar.add(tfMobile);
		tfMobile.setColumns(10);
		
		toolBar.addSeparator();
		JButton btnMemberAdd = new JButton("");
		btnMemberAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinMemberAdd winMemberAdd = new WinMemberAdd(1);
				winMemberAdd.setModal(true);
				winMemberAdd.setVisible(true);
			}
		});
		btnMemberAdd.setIcon(new ImageIcon(WinMain.class.getResource("/image/addUser.png")));
		toolBar.add(btnMemberAdd);
		
		JButton btnMemberRemove = new JButton("");
		btnMemberRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinMemberRemove winMemberRemove = new WinMemberRemove(2);
				winMemberRemove.setModal(true);
				winMemberRemove.setVisible(true);	
			}
		});
		btnMemberRemove.setIcon(new ImageIcon(WinMain.class.getResource("/image/delUser.png")));
		toolBar.add(btnMemberRemove);
		
		JButton btnMemberUpdate = new JButton("");
		btnMemberUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinMemberUpdate winMemberUpdate = new WinMemberUpdate(3);
				winMemberUpdate.setModal(true);
				winMemberUpdate.setVisible(true);	
			}
		});
		btnMemberUpdate.setIcon(new ImageIcon(WinMain.class.getResource("/image/showUser.png")));
		toolBar.add(btnMemberUpdate);
		
		JButton btnMemberSearch = new JButton("");
		btnMemberSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinMemberSelect winMemberSelect = new WinMemberSelect(4);
				winMemberSelect.setModal(true);
				winMemberSelect.setVisible(true);	
			}
		});
		btnMemberSearch.setIcon(new ImageIcon(WinMain.class.getResource("/image/search.png")));
		toolBar.add(btnMemberSearch);
				
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		//String columns[] = {"ISBN","제목","저자","출판사","이미지URL","출판일","가격","책 소개"};
		//DefaultTableModel dtm = new DefaultTableModel(columns,0);
		//table = new JTable(dtm);		
		table = new JTable();
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(table, popupMenu);
		
		JMenuItem mnuDetail = new JMenuItem("자세히...");
		mnuDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				String sISBN = table.getValueAt(row, 0).toString();
				
				WinBookDetail winBookDetail;
				try {
					winBookDetail = new WinBookDetail(sISBN);
					winBookDetail.setModal(true);
					winBookDetail.setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		popupMenu.add(mnuDetail);
		
		JMenuItem mnuDelete = new JMenuItem("삭제...");
		mnuDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				String sISBN = table.getValueAt(row, 0).toString();
				//System.out.println(sISBN);
				WinBookDelete winBookDelete = new WinBookDelete(sISBN);
				winBookDelete.setModal(true);
				winBookDelete.setVisible(true);
				
			}
		});
		popupMenu.add(mnuDelete);
		
		JMenuItem mnuUpdate = new JMenuItem("변경...");
		mnuUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				String sISBN = table.getValueAt(row, 0).toString();
				//System.out.println(sISBN);
				WinBookUpdate winBookUpdate = new WinBookUpdate(sISBN);
				winBookUpdate.setModal(true);
				winBookUpdate.setVisible(true);
			}
		});
		popupMenu.add(mnuUpdate);
		
		JSeparator separator_1 = new JSeparator();
		popupMenu.add(separator_1);
		
		mnuRent = new JMenuItem("대여...");
		mnuRent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String strISBN = "";
				int row = table.getSelectedRow();				
				if(row != -1) {
					strISBN = table.getValueAt(row, 0).toString();
				}				
				
				WinRentalSearch winRentalSearch = new WinRentalSearch(1, strISBN); //대여
				winRentalSearch.setModal(true);
				winRentalSearch.setVisible(true);
				
				// id가 날라오면 rentalTBL에 isbn, XXXdate, 추가하기.				
				String strID = winRentalSearch.getID();
				
				insertRecord(strISBN, strID);
			}
		});
		popupMenu.add(mnuRent);
		
		mnuReturn = new JMenuItem("반납...");
		mnuReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String strISBN = "";
				int row = table.getSelectedRow();				
				if(row != -1) {
					strISBN = table.getValueAt(row, 0).toString();
				}				
				
				WinRentalSearch winRentalSearch = new WinRentalSearch(2, strISBN); //반납
				winRentalSearch.setModal(true);
				winRentalSearch.setVisible(true);
				
				// id가 날라오면 rentalTBL에 bRental 값을 1로 바꾸기			
				String strID = winRentalSearch.getID();
				if(!strID.equals(""))
					updateRecord(strISBN, strID);
			}
		});
		popupMenu.add(mnuReturn);
		
		scrollPane.setViewportView(table);
			
		
	}

	protected void updateRecord(String strISBN, String strID) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();			
			String sql = "update rentalTBL set bRental = 1 ";
			sql = sql + "where isbn='" + strISBN + "' and id='" + strID + "'";
			stmt.executeUpdate(sql);			
			
			sql = "update bookTBL set count = count + 1 where isbn ='" + strISBN + "'";
			stmt.executeUpdate(sql);
			
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}

	protected void insertRecord(String strISBN, String strID) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();			
			String sql = "insert into rentalTBL values(null,'" + strISBN;
			sql = sql + "','" + strID + "',curdate(),adddate(curdate(), interval 15 day),0)";
			stmt.executeUpdate(sql);
			
			sql = "update bookTBL set count = count - 1 where isbn ='" + strISBN + "'";
			stmt.executeUpdate(sql);
			
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	protected void showMembersByMobile() {
		String columnNames[] = {"ID","이름","이메일","전화번호","주소"};
		DefaultTableModel dtm = (DefaultTableModel) table.getModel();
		dtm.setColumnIdentifiers(columnNames);
		dtm.setRowCount(0);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();			
			String sql = "select * from memberTBL where mobile like '%" + tfMobile.getText() + "%'";
			ResultSet rs = stmt.executeQuery(sql);			
			while(rs.next()) {
				Vector<String> vector = new Vector<>();
				vector.add(rs.getString("id"));
				vector.add(rs.getString("name"));
				vector.add(rs.getString("email"));
				vector.add(rs.getString("mobile"));
				vector.add(rs.getString("address"));
				dtm.addRow(vector);
			}
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	public WinMain(DefaultTableModel dtm) {
		// TODO Auto-generated constructor stub
		this();
		table.setModel(dtm);
		//showRecords(dtm); // 화면 구성한 후, showRecords()함수를 이용하여 테이블에 출력
	}

	private void showRecords(DefaultTableModel dtm) {
		dtm.setRowCount(0);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();
			
			String sql = "select * from bookTBL order by title asc";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				Vector<String> vector = new Vector<>();
				for(int i=1; i<=9; i++)
					vector.add(rs.getString(i));
				dtm.addRow(vector);
			}
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					JTable source = (JTable)e.getSource();
					int row = source.rowAtPoint(e.getPoint());
					int column = source.columnAtPoint(e.getPoint());
					
					if(!source.isRowSelected(row))  // 행이 선택되지 않았다면 그 행을 선택한다.
						source.changeSelection(row, column, false, false);
					
					String sNum = source.getValueAt(row, 8).toString();
					if(Integer.parseInt(sNum) > 0  && Integer.parseInt(sNum) < 2) {
						mnuRent.setEnabled(true);
						mnuReturn.setEnabled(true);
					}else if(Integer.parseInt(sNum) == 0 ){
						mnuRent.setEnabled(false);
						mnuReturn.setEnabled(true);
					}else {
						mnuRent.setEnabled(true);
						mnuReturn.setEnabled(false);
					}
					
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
}

