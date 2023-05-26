import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.thehowtotutorial.splashscreen.JSplash;


public class Splash {
	
	private static DefaultTableModel dtm;

	public static void main(String[] args) throws InterruptedException {
		JSplash splash = new JSplash(Splash.class.getResource("image/bookStore.png"),
				true,true,false,"V1",null, Color.YELLOW, Color.BLUE);
		splash.splashOn();
		//================================
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();			
			
			String sql = "select * from bookTBL";
			ResultSet rs = stmt.executeQuery(sql);
			int totalNumber = 0;
			while(rs.next()) {
				totalNumber++;  // bookTBL에 있는 전체 레코드 수를 알아낸다.
			}
			System.out.println("전체 레코드 수: " + totalNumber);
			
			sql = "select * from bookTBL order by title asc";
			rs = stmt.executeQuery(sql);
			String columnNames[] = {"ISBN","제목","저자","출판사","이미지URL","출판일","가격","책 소개","권수"};
			dtm = new DefaultTableModel(columnNames, 0);
			
			int num=0;
			while(rs.next()) {
				num++;
				splash.setProgress(100*num/totalNumber, rs.getString("title"));
				Thread.sleep(10);
				
				Vector <String> vector = new Vector<>();
				for(int i=0;i<columnNames.length;i++)
					vector.add(rs.getString(i+1));
				dtm.addRow(vector);
			}
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//=========================
		
		splash.splashOff();
		
		WinMain winMain = new WinMain(dtm);
		winMain.setModal(true);
		winMain.setVisible(true);
	}

}
