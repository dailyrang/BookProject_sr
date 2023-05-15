package member;

import java.awt.EventQueue;

import javax.swing.JDialog;

public class WinMemberRemove extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinMemberRemove dialog = new WinMemberRemove();
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
	public WinMemberRemove() {
		setBounds(100, 100, 584, 431);
		setTitle("회원 탈퇴창");
		
	}

	public WinMemberRemove(int type) {
		this();
		Member member = new Member(type);
		getContentPane().add(member);
	}

}
