/*
 * Copyright (c) 2002-2003 iReasoning Inc. All Rights Reserved.
 * 
 * This SOURCE CODE FILE, which has been provided by iReasoning Inc. as part
 * of an iReasoning Software product for use ONLY by licensed users of the product,
 * includes CONFIDENTIAL and PROPRIETARY information of iReasoning Inc.  
 *
 * USE OF THIS SOFTWARE IS GOVERNED BY THE TERMS AND CONDITIONS 
 * OF THE LICENSE STATEMENT AND LIMITED WARRANTY FURNISHED WITH
 * THE PRODUCT.
 *
 * IN PARTICULAR, YOU WILL INDEMNIFY AND HOLD IREASONING SOFTWARE, ITS
 * RELATED COMPANIES AND ITS SUPPLIERS, HARMLESS FROM AND AGAINST ANY
 * CLAIMS OR LIABILITIES ARISING OUT OF THE USE, REPRODUCTION, OR
 * DISTRIBUTION OF YOUR PROGRAMS, INCLUDING ANY CLAIMS OR LIABILITIES
 * ARISING OUT OF OR RESULTING FROM THE USE, MODIFICATION, OR
 * DISTRIBUTION OF PROGRAMS OR FILES CREATED FROM, BASED ON, AND/OR
 * DERIVED FROM THIS SOURCE CODE FILE.
 */


import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import com.ireasoning.protocol.snmp.*;
import com.ireasoning.protocol.*;

public class SnmpApplet extends Applet implements ActionListener, Listener
{
    TextArea _textarea;
    TextField _subtreeOid;

    public void init()
    {
        setLayout(new BorderLayout());
        Panel p = new Panel();
        p.setLayout(new BorderLayout());
        p.add(new Label("Subtree OID:"), BorderLayout.WEST);
        _subtreeOid = new TextField(".1.3");
        p.add(_subtreeOid, BorderLayout.CENTER);
        add(p, BorderLayout.NORTH);
        _textarea = new TextArea("", 200, 120);
        add(_textarea, BorderLayout.CENTER);
        Button btn = new Button("Start");
        add(btn, BorderLayout.SOUTH);
        btn.addActionListener(this);
    }
    
    public void handleMsg(Object msgSender, Msg msg)
    {
        SnmpPdu pdu = (SnmpPdu)msg;
        if(pdu.hasMore())
        {
            SnmpVarBind[] varbinds = pdu.getVarBinds();
            for (int i = 0; i < varbinds.length ; i++) 
            {
                _textarea.append("" + varbinds[i] + "\n\n");
            }
        }
    }
    
    public void actionPerformed(ActionEvent e)
    {
        try
        {
            _textarea.setText("");
            String ip = getCodeBase().getHost();
            System.out.println( "ip:" + ip);
            SnmpSession session = new SnmpSession(ip, 161, "public", "public", 1);
            session.snmpGetSubtree(_subtreeOid.getText().trim(), this);
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }
}
