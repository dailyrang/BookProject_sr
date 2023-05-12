import java.awt.EventQueue;

import javax.swing.JDialog;

public class WinBookUpdate extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinBookUpdate dialog = new WinBookUpdate();
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
	public WinBookUpdate() {
		this("");
	}

	public WinBookUpdate(String sISBN) {
		setTitle("도서 변경");
		setBounds(100, 100, 763, 591);

		Book book = new Book(3, sISBN);
		getContentPane().add(book);
	}

}
