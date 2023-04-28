import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WinPopup extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinPopup dialog = new WinPopup();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinPopup() {
		getContentPane().setBackground(new Color(0, 128, 0));
		setTitle("팝업 메뉴 연습");
		setBounds(100, 100, 450, 300);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(getContentPane(), popupMenu);
		
		JMenuItem mnured = new JMenuItem("빨강");
		popupMenu.add(mnured);
		
		JMenuItem mnugreen = new JMenuItem("초록");
		popupMenu.add(mnugreen);
		
		JMenuItem mnuBule = new JMenuItem("파랑");
		popupMenu.add(mnuBule);
		getContentPane().setLayout(new BorderLayout());
		
			{
		JButton btnRed = new JButton("Red");
			btnRed.setBackground(new Color(255, 255, 255));
			getContentPane().add(btnRed, BorderLayout.NORTH);
			
			JPopupMenu popupMenu_1 = new JPopupMenu();
			addPopup(btnRed, popupMenu_1);
			
			JMenuItem mnuYellow = new JMenuItem("노랑");
			mnuYellow.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getContentPane().setBackground(Color.yellow);
				}
			});
			popupMenu_1.add(mnuYellow);
			
			JMenuItem mnuOrange = new JMenuItem("오렌지");
			popupMenu_1.add(mnuOrange);

			
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
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
