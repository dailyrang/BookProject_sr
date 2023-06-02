package util;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.JTextField;

import member.DateUtil;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import java.awt.Font;

public class WinCalendar extends JDialog {

	private JPanel panelCalendar;
	private JComboBox cbMonth;
	private JComboBox cbYear;
	private JButton btnNextMonth;
	private JButton btnPrevMonth;
	private JButton btnNextYear;
	private JButton btnPrevYear;
	
	private int gYear;
	private int gMonth;
	private int gDay;
	
	
	private String selectedDate;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinCalendar dialog = new WinCalendar();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public String getDate() {
		return selectedDate;
	}

	/**
	 * Create the dialog.
	 */
	public WinCalendar() {
		setTitle("달력(1923년~2123년)");
		setBounds(100, 100, 743, 399);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		
		panelCalendar = new JPanel();
		getContentPane().add(panelCalendar, BorderLayout.CENTER);
		panelCalendar.setLayout(new GridLayout(0, 7, 2, 2));
		
		
		JButton btnRun = new JButton("달력보기");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCalendar();				
			}
		});
		
		cbMonth = new JComboBox();
		cbMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCalendar();
			}
		});
		
		btnPrevMonth = new JButton("<");
		btnPrevMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int year = Integer.parseInt(cbYear.getSelectedItem().toString());
				int month = Integer.parseInt(cbMonth.getSelectedItem().toString());
				month--;		
				if(month==0) {
					year--;
					month=12;
				}
				cbYear.setSelectedItem(year);
				cbMonth.setSelectedIndex(month-1);
				showCalendar();
			}
		});
		
		btnPrevYear = new JButton("<<");
		btnPrevYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int year = Integer.parseInt(cbYear.getSelectedItem().toString());
				
				year--;
				if(year == 1923)
					year=193;
				
				cbYear.setSelectedItem(year);
				
				showCalendar();
			}
		});
		panel.add(btnPrevYear);
		panel.add(btnPrevMonth);
		
		cbYear = new JComboBox();
		cbYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCalendar();
			}
		});
		panel.add(cbYear);
		cbMonth.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));
		panel.add(cbMonth);
		panel.add(btnRun);
		
		btnNextMonth = new JButton(">");
		btnNextMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int year = Integer.parseInt(cbYear.getSelectedItem().toString());
				int month = Integer.parseInt(cbMonth.getSelectedItem().toString());
				month++;		
				if(month==13) {
					year++;
					month=1;
				}
				cbYear.setSelectedItem(year);
				cbMonth.setSelectedIndex(month-1);	
				
				showCalendar();
			}
		});
		panel.add(btnNextMonth);
		
		btnNextYear = new JButton(">>");
		btnNextYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int year = Integer.parseInt(cbYear.getSelectedItem().toString());
				
				year++;
				if(year == 2123)
					year=2123;
				
				cbYear.setSelectedItem(year);
				
				showCalendar();
			}
		});
		panel.add(btnNextYear);

		for(int year=1923;year<=2123;year++)
			cbYear.addItem(year);
		
		// 현재 연도와 월을 알아오자
		Calendar today = Calendar.getInstance();		
		gYear = today.get(Calendar.YEAR);
		gMonth = today.get(Calendar.MONTH) + 1; //0~11 => 1~12
		gDay = today.get(Calendar.DAY_OF_MONTH);
		  
		cbYear.setSelectedItem(gYear);
		cbMonth.setSelectedItem(Integer.toString(gMonth));
		//cbMonth.setSelectedIndex(today.get(Calendar.MONTH));
	}

	protected void showCalendar() {
		// 버튼 싹 지우고
		Component []componentList = panelCalendar.getComponents();
		for(Component c: componentList) {
			if(c instanceof JButton )
				panelCalendar.remove(c);
		}
		panelCalendar.revalidate();
		panelCalendar.repaint();
		
		// 일요일~토요일 버튼 생성한다.
//		JButton btn1 = new JButton("일요일");	panelCalendar.add(btn1);	
//		JButton btn2 = new JButton("월요일");	panelCalendar.add(btn2);
//		JButton btn3 = new JButton("화요일");	panelCalendar.add(btn3);
//		JButton btn4 = new JButton("수요일");	panelCalendar.add(btn4);
//		JButton btn5 = new JButton("목요일");	panelCalendar.add(btn5);
//		JButton btn6 = new JButton("금요일");	panelCalendar.add(btn6);
//		JButton btn7 = new JButton("토요일");	panelCalendar.add(btn7);
		
//		String yoil[] = {"일","월","화","수","목","금","토"};
//		for(int i=0;i<yoil.length;i++) {
//			JButton btn = new JButton(yoil[i] + "요일");	
//			panelCalendar.add(btn);
//		}
		
		 String yoil = "일월화수목금토";
	      for(int i=0;i<yoil.length();i++) {
	         JButton btn = new JButton(yoil.substring(i,i+1));   
	         btn.setBackground(Color.GREEN);
	         btn.setForeground(new Color(0,0,255));
	         //btn.setEnabled(false);
	         btn.setFont(new Font("굴림", Font.BOLD, 16));
	         panelCalendar.add(btn);
	      }

	    int Month[] = {31,28,31,30,31,30,31,31,30,31,30,31};
	    int year = Integer.parseInt(cbYear.getSelectedItem().toString());
		int month = Integer.parseInt(cbMonth.getSelectedItem().toString());
		int sum = 0;
		
		// 해당하는 전연도까지의 합을 구하시오.(1923.1.1~2022.12.31)
		for(int i=1923;i<year;i++) {
			if(i%4==0 && i%100 !=0 || i%400==0)
				sum = sum + 366;
			else
				sum = sum + 365;
		}
		// 해당하는 전월까지의 날짜 수의 합을 구하시오. 
		for(int i=0;i<month-1;i++) {
			if(i==1 && (year%4==0 && year%100!=0 || year%400==0))
				sum = sum + ++Month[i];
			else
				sum = sum + Month[i];
		}		
	    
	    // 1923년도 1월 1일의 시작은 월요일(1)부터 시작이다.
	      
	    int start = 1;
	    start = (start + sum ) % 7;
	    
	    for(int i=1;i<=start;i++) {  
			JButton btn = new JButton("");
			panelCalendar.add(btn);			
			btn.setVisible(false);
		}	    
	      
		// 해당하는 달의 마지막 날짜까지 버튼을 생성한다.
		
		int last = Month[month-1];
		if(month==2 &&  (year%4==0 && year%100!=0 || year%400==0) )
			last++;
		for(int i=1;i<=last;i++) {  // 1일부터 해당월의 마지막 날짜를 출력한다.			
			//2023.5.23 추가
			String	sSlrDate = "" + year + (month<10?"0":"") + month + (i<10?"0":"")+i;
			HashMap hm = DateUtil.toLunar(sSlrDate);			 
			String  retLrrDate   = hm.get("day").toString();
			retLrrDate = Integer.parseInt(retLrrDate.substring(4,6)) + "." + Integer.parseInt(retLrrDate.substring(6));
			
			String [][]holydays = {
					{"0101","신정"},
					{"0301","삼일절"},
					{"0505","어린이날"},
					{"0606","현충일"},
					{"0815","광복절"},
					{"1003","개천절"},
					{"1009","한글날"},
					{"1225","크리스마스"},
					{"1.1","설날"},
					{"4.8","석가탄신일"},
					{"8.15","추석"}
			};
			String sMonthDay = (month<10?"0":"") + month + (i<10?"0":"") +i;
			int blueDay = 0;
			for(int j=0; j<11;j++) //양력 공휴일
				if(holydays[j][0].equals(sMonthDay)) {
					retLrrDate = holydays[j][1];
					blueDay = Integer.parseInt(holydays[j][0].substring(2));
				}
			for(int j=0; j<11;j++)//음력 공휴일
				if(holydays[j][0].equals(retLrrDate)) {
					System.out.println(retLrrDate);
					retLrrDate = holydays[j][1];
					blueDay = i;
				}
			JButton btn = new JButton(i + "(" + retLrrDate + ")");
			if(i==blueDay)
				btn.setBackground(Color.yellow);
			//2023.5.23
			
			btn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton btn1 = (JButton)e.getSource();
					// 25(4.5)
					int day = Integer.parseInt(btn1.getText().substring(0,btn1.getText().indexOf("(")));
					
					selectedDate = year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day;
					
					//System.out.println(selectedDate);
					dispose();
				}
			});
			if(year == gYear && month == gMonth	&& i == gDay)
				btn.setBackground(Color.red);
			panelCalendar.add(btn);			
			panelCalendar.revalidate();
		}
	}

}
