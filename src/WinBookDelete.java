import java.awt.EventQueue;

import javax.swing.JDialog;

public class WinBookDelete extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinBookDelete dialog = new WinBookDelete();
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
	public WinBookDelete() {
		this("");
	}

	public WinBookDelete(String sISBN) {
		setTitle("도서 삭제");
		setBounds(100, 100, 763, 591);

		Book book = new Book(2, sISBN);
		getContentPane().add(book);
	}

}
