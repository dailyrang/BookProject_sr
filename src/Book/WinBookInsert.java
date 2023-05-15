package Book;
import java.awt.EventQueue;

import javax.swing.JDialog;

public class WinBookInsert extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinBookInsert dialog = new WinBookInsert();
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
	public WinBookInsert() {
		setTitle("도서 추가");
		setBounds(100, 100, 731, 595);
		
		Book book = new Book(1);
		getContentPane().add(book);
	}

}
