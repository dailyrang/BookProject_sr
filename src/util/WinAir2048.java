package util;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Dialog.ModalExclusionType;

public class WinAir2048 extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinAir2048 dialog = new WinAir2048();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JPanel panelAir2048;
	private int [][]Matrix;
	private int jumsu=0;
	private JLabel lblScore;
	private int number;
	private JComboBox cbNumber;
	/**
	 * Create the dialog.
	 */
	public WinAir2048() {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		getContentPane().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_LEFT) {
					moveLeft();
					pickOne();
					showMatrix();
				}else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
					moveRight();
					pickOne();
					showMatrix();
				}else if(e.getKeyCode() == KeyEvent.VK_UP) {
					moveUp();
					pickOne();
					showMatrix();
				}else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					moveDown();
					pickOne();
					showMatrix();
				}
			}
		});
		
		setTitle("air2048 Game");
		setBounds(100, 100, 442, 416);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		
		cbNumber = new JComboBox();
		cbNumber.setModel(new DefaultComboBoxModel(new String[] {"4", "5", "6"}));
		cbNumber.setFont(new Font("굴림", Font.BOLD, 20));
		panel.add(cbNumber);
		
		lblScore = new JLabel("0점");
		lblScore.setFont(new Font("굴림", Font.BOLD, 20));
		panel.add(lblScore);
		
		JButton btnStart = new JButton("Start");
		btnStart.setFont(new Font("굴림", Font.BOLD, 20));
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				init();
				pickOne();
				showMatrix();
				getContentPane().requestFocus();
			}
		});
		panel.add(btnStart);
		
		panelAir2048 = new JPanel();		
		getContentPane().add(panelAir2048, BorderLayout.CENTER);
		panelAir2048.setLayout(new GridLayout(0, 4, 10, 10));

	}

	protected void moveDown() {
		System.out.println("아래쪽 화살표");
		for(int i=0;i<number;i++)
			for(int j=0;j<number;j++)
				for(int k=i+1;k<number;k++) {
					if(Matrix[number-1-i][j]==Matrix[number-1-k][j]) {
						Matrix[number-1-i][j] = Matrix[number-1-k][j]*2;
						jumsu = jumsu + Matrix[number-1-k][j]*2;
						Matrix[number-1-k][j] = 0;
						break;
					}
					else if(Matrix[number-1-k][j]==0)
						continue;
					else
						break;
				}
		// 아래로 이동
		for(int i=0;i<number;i++)
			for(int j=0;j<number;j++)
				for(int k=i+1;k<number;k++) {
					if(Matrix[number-1-i][j]==0 && Matrix[number-1-k][j]!=0) {
						Matrix[number-1-i][j] = Matrix[number-1-k][j];
						Matrix[number-1-k][j] = 0;
						break;
					}
				}
	}

	protected void moveUp() {
		System.out.println("위쪽 화살표");
		for(int i=0;i<number;i++)
			for(int j=0;j<number;j++)
				for(int k=i+1;k<number;k++) {
					if(Matrix[i][j]==Matrix[k][j]) {
						Matrix[i][j] = Matrix[k][j]*2;
						jumsu = jumsu + Matrix[k][j]*2;
						Matrix[k][j] = 0;
						break;
					}
					else if(Matrix[k][j]==0)
						continue;
					else
						break;
				}
		// 위로 이동
		for(int i=0;i<number;i++)
			for(int j=0;j<number;j++)
				for(int k=i+1;k<number;k++) {
					if(Matrix[i][j]==0 && Matrix[k][j]!=0) {
						Matrix[i][j] = Matrix[k][j];
						Matrix[k][j] = 0;
						break;
					}
				}
	}

	protected void moveRight() {
		System.out.println("오른쪽 화살표");
		for(int i=0;i<number;i++)
			for(int j=0;j<number;j++)
				for(int k=j+1;k<number;k++) {
					if(Matrix[i][number-1-j] == Matrix[i][number-1-k]) {
						Matrix[i][number-1-j] = Matrix[i][number-1-k]*2;
						jumsu = jumsu + Matrix[i][number-1-k]*2;
						Matrix[i][number-1-k] = 0;
						break;
					}
					else if(Matrix[i][number-1-k] == 0)
						continue;
					else
						break;
				}
		// 오른쪽으로 이동	
		for(int i=0;i<number;i++)
			for(int j=0;j<number;j++)
				for(int k=j+1;k<number;k++) {
					if(Matrix[i][number-1-j]==0 && Matrix[i][number-1-k]!=0) {
						Matrix[i][number-1-j] = Matrix[i][number-1-k];
						Matrix[i][number-1-k] = 0;
						break;
					}
				}
	}

	protected void moveLeft() {
		System.out.println("왼쪽 화살표");
		for(int i=0;i<number;i++)
			for(int j=0;j<number;j++)
				for(int k=j+1;k<number;k++) {
					if(Matrix[i][j] == Matrix[i][k]) {
						Matrix[i][j] = Matrix[i][k]*2;
						jumsu = jumsu + Matrix[i][k]*2;
						Matrix[i][k] = 0;
						break;
					}
					else if(Matrix[i][k]==0)
						continue;
					else
						break;
				}
		// 왼쪽으로 이동		
		for(int i=0;i<number;i++)
			for(int j=0;j<number;j++)
				for(int k=j+1;k<number;k++) {
					if(Matrix[i][j]==0 && Matrix[i][k]!=0) {
						Matrix[i][j] = Matrix[i][k];
						Matrix[i][k] = 0;
						break;
					}
				}
	}

	protected void init() {
		number = Integer.parseInt(cbNumber.getSelectedItem().toString());
		panelAir2048.setLayout(new GridLayout(0, number, 10, 10));
		Matrix = new int[number][number];
		
		for(int i=0;i<number;i++) {
			for(int j=0;j<number;j++) {
				Matrix[i][j] = 0;
			}
		}		
	}

	protected void pickOne() {
		int row, col;
		do {
			row = (int)(Math.random()*number);
			col = (int)(Math.random()*number);
		}while(Matrix[row][col] != 0);
		
		Matrix[row][col] = (int)(Math.random()*number)==0 ? 4 : 2;
	}

	protected void showMatrix() {
		Component []componentList = panelAir2048.getComponents();
		for(Component c: componentList) {
			if(c instanceof JButton )
				panelAir2048.remove(c);
		}
		panelAir2048.revalidate();
		panelAir2048.repaint();
		
		int count = 0;
		for(int i=0; i<number; i++) {
			for(int j=0;j<number;j++) {
				int num = Matrix[i][j];
				JButton btn;
				if(num==0)
					btn = new JButton();
				else {
					btn = new JButton(num + "");
					count++;
				}
				btn.setFont(new Font("굴림", Font.BOLD, 20));
				panelAir2048.add(btn);
				panelAir2048.revalidate();
			}
		}		
		if(count == number*number) {
			JOptionPane.showMessageDialog(null, "게임이 종료되었습니다");
			init();
			pickOne();
			showMatrix();
		}
		
		for(int i=0; i<number; i++) {
			for(int j=0;j<number;j++) {
				System.out.print(Matrix[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		lblScore.setText(jumsu + "점");
		
	}

}
