package projekat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;



import com.ireasoning.protocol.TimeoutException;
import com.ireasoning.protocol.snmp.SnmpConst;
import com.ireasoning.protocol.snmp.SnmpDataType;
import com.ireasoning.protocol.snmp.SnmpSession;
import com.ireasoning.protocol.snmp.SnmpTableModel;
import com.ireasoning.protocol.snmp.SnmpTarget;

public class MainProgram extends JFrame{

	private JButton btnStart;
	private JTextField lblIpAddress;
	private JTable table;
	private JScrollPane pane;
	private SmtpThread threadTable=null;

	public MainProgram() {
		this.setSize(new Dimension(1024,768));
		this.setLayout(new BorderLayout());
		btnStart=new JButton("Prikazi");
		threadTable=new SmtpThread(this);
		lblIpAddress=new JTextField();
		lblIpAddress.setPreferredSize(new Dimension(130,20));
		this.setLayout(new BorderLayout());
		JPanel panel=new JPanel();
		panel.add(lblIpAddress);
		panel.add(btnStart);
		this.add(panel,BorderLayout.NORTH);
		this.setVisible(true);

		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String ipAddress=lblIpAddress.getText();		
				synchronized (threadTable) {
				threadTable.setIpAddres(ipAddress);
				threadTable.notify();
				}
				if (threadTable.isAlive()==false)
					threadTable.start();
			}
		});

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}
	public static void main (String[]args) {
		new MainProgram();
	}
}
/*

/home/korisnik/Downloads/ireasoning/mibbrowser/browser.sh


*/


