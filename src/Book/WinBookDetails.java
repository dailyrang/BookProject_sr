package Book;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;

public class WinBookDetails extends JDialog implements PropertyChangeListener{
	
	private ProgressMonitor progressMonitor;   
    private Task task;
	private JTabbedPane tabbedPane;
    
    class Task extends SwingWorker<Void, Void> {
        @Override
        public Void doInBackground() throws Exception {           
            int progress = 0;
            setProgress(0);
            
            Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sqlDB","root","1234");						
			Statement stmt = con.createStatement();		
			
			String sql1 = "select count(*) from bookTBL";
			ResultSet rs1 = stmt.executeQuery(sql1);
			int total=0;
			while(rs1.next())
				total = rs1.getInt(1);
			
			String sql = "select * from bookTBL";
			ResultSet rs = stmt.executeQuery(sql);
			int count=0;
			
			String arrBook[] = new String[8];			
			while(rs.next() && progress < 100 && !isCancelled()) {
				// 레코드를 가져오는 부분
				Thread.sleep(1);				
				setProgress(100 * ++count / total);				
				// 각 레코드로부터 정보를 읽어와 탭에 전달
				for(int i=0;i<8;i++)
					arrBook[i] = new String(rs.getString(i+1));
				String title = arrBook[1];
				if(title.length() > 13)
					tabbedPane.addTab(title.substring(0,10)+"...", new Book(arrBook));
				else
					tabbedPane.addTab(title, new Book(arrBook));
				tabbedPane.setSelectedIndex(count-1);
			}    
			tabbedPane.setSelectedIndex(0);  // 맨 앞 탭으로 이동
            return null;
        }
    }
	
	/**
	 * Create the dialog.
	 */
	public WinBookDetails() {
		setTitle("모든 책 자세히 보기");
		setBounds(100, 100, 766, 601);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		
		progressMonitor = new ProgressMonitor(WinBookDetails.this,
                "도서 정보를 읽어오는 중",
                "", 0, 100);
		progressMonitor.setProgress(0);
		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {		
		if ("progress" == evt.getPropertyName() ) {
            int progress = (Integer) evt.getNewValue();
            progressMonitor.setProgress(progress);
            String message =
                String.format("%d%% 진행중\n", progress);
            progressMonitor.setNote(message);
            if (progressMonitor.isCanceled() || task.isDone()) {
                Toolkit.getDefaultToolkit().beep();                
            }
        }
        
	}

}
