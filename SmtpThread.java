package projekat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.ireasoning.protocol.TimeoutException;
import com.ireasoning.protocol.snmp.SnmpConst;
import com.ireasoning.protocol.snmp.SnmpDataType;
import com.ireasoning.protocol.snmp.SnmpSession;
import com.ireasoning.protocol.snmp.SnmpTableModel;
import com.ireasoning.protocol.snmp.SnmpTarget;

public class SmtpThread extends Thread {
	private String ipAddress;
	private JTable scrtable;
	private JScrollPane pane;
	private MainProgram frame;
	
	static {
		try {
			SnmpSession.loadMib("/home/korisnik/Desktop/IETF - BGP4-MIB.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SnmpSession.loadMib2();
	}
	
	public SmtpThread(MainProgram frame) {
		this.frame=frame;
	}
	@Override
	public void run() {
		
	
		while (!Thread.interrupted()) {
			
		SnmpTarget target=new SnmpTarget(ipAddress, 161, "si2019", "si2019", SnmpConst.SNMPV2);
		
		try {
			//kreiranje sesije
			SnmpSession session=new SnmpSession(target);

			session.setTimeout(10000);
			SnmpTableModel table=session.snmpGetTable("bgp4PathAttrTable");
			String[][]data=new String[table.getRowCount()][];
			boolean []bestRoute=new boolean[table.getRowCount()];
			
			for (int i=0;i<table.getRowCount();i++) {
				int cntrBind=0;
				data[i]=new String[9];
				for (int j=0;j<table.getColumnCount();j++) {
					if ((j>=2 && j<11) || j==12) {
						
						SnmpDataType val=table.get(i, j).getValue();
						if (val.getType()==SnmpDataType.INTEGER) {
							if (j==12) {
								if (val.toString().equals("2")) {
									bestRoute[i]=true;
									
								}
							}
							
							else if (table.getColumnName(j).equals("bgp4PathAttrOrigin")) {
								if (val.toString().equals("1"))
									data[i][cntrBind++]="igp";
								else if (val.toString().equals("2"))
									data[i][cntrBind++]="egp";
								else 
									data[i][cntrBind++]="incomplete";
							}
							
							else {
								data[i][cntrBind++]=val.toString();
							}
						}
						else if (val.getType()==SnmpDataType.IPADDRESS) {
							data[i][cntrBind++]=val.toString();
						}
						else if (val.getType()==SnmpDataType.OCTETSTRING) {
							
						String strOct=val.toString();
						byte[] bytes=strOct.getBytes();
						int asPathNumbers[]=new int[bytes.length/4];
						int cntrAsPath=0;
						for (int k=10;k+3<bytes.length;k+=5) {
							int first=bytes[k+2]-'0';
							first<<=8;
							int second=bytes[k+3]-'0';
							int number=first+second;
							asPathNumbers[cntrAsPath++]=number;							
						}
						strOct="";
						for (int k=0;k<cntrAsPath;k++) {
							if (asPathNumbers[k]!=0) {
								if (k!=0) {
									strOct=strOct+" ";
								}
								strOct=strOct+asPathNumbers[k];
							}
							
						}
						data[i][cntrBind++]=strOct;
						
						}
						else {
							data[i][cntrBind++]=val.toString();
						}
					}
				}
				
			}
			String[]header= {"Mreza","Origin","AS-Path","Next Hop","MED","Local Preference","Atomic aggregate","Aggregator AS","Aggregator Address"};
			scrtable=new JTable(data,header);
			
			scrtable.setDefaultRenderer(Object.class,new DefaultTableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					final Component c=super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					c.setBackground(bestRoute[row]==true ? new Color(153, 255, 214,255):Color.WHITE);
					return c;
				}
				
			});
			session.close();
			pane=new JScrollPane(scrtable);
			frame.add(pane,BorderLayout.CENTER);
			frame.requestFocus();
			frame.revalidate();
			synchronized (this) {
			wait(10000);	
			}
			
		}
		catch (TimeoutException e) {
		System.out.println("Greska u uspostavljanju veze!");	
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		}
	
	
	}
	
	public void setIpAddres(String ipAddress) {
		this.ipAddress=ipAddress;
	}
	public MainProgram getMain() {
		return frame;
	}
}
