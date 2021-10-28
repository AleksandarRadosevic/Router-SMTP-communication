/*
 * Copyright (c) 2002 iReasoning Networks. All Rights Reserved.
 * 
 * This SOURCE CODE FILE, which has been provided by iReasoning Networks as part
 * of an iReasoning Software product for use ONLY by licensed users of the product,
 * includes CONFIDENTIAL and PROPRIETARY information of iReasoning Networks.  
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


import com.ireasoning.util.ParseArguments;
import com.ireasoning.protocol.snmp.*;
import com.ireasoning.protocol.TimeoutException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class demonstrates SNMP broadcast discovery
 */
public class snmpbroadcast extends snmp
{

    public static void main(String[] args)
    {
        snmpbroadcast s = new snmpbroadcast();
        s.parseOptions(args, "snmpbroadcast");
        s.broadcast();
    }
    
    private void broadcast()
    {
        try
        {
            SnmpSession.setToUseJCE(true);
            SnmpSession session = new SnmpSession(_host, _port, _community,
                    _community, _version);
            session.setTimeout(5000); // sets timeout to be 5 seconds

            if(_isSnmpV3)
            {
                session.setV3Params(_user, _authProtocol, _authPassword, _privProtocol, _privPassword, _context, null);
            }
 
            ArrayList ret = session.broadcastDiscovery(new SnmpOID(_oids[0]));

            for (int i = 0; i < ret.size() ; i++) 
            {
                HostValue v = (HostValue) ret.get(i);
                System.out.println( v);
            }
            session.close();
        }
        catch(TimeoutException timeout)
        {
            System.out.println( "time out");
        }
        catch(IOException e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------------------
    // Parsing command line options
    // ----------------------------------------------------------------------
    
    /**
     * Prints out the example usage
     */
    protected void printExample(String programName)
    {
        System.out.println( "java " + programName + " 192.168.1.255 .1.3.6.1.2.1.1.2.0");
        System.out.println( "java " + programName + " 192.168.1.255 -v 3 -u newUser -A abc12345 -X abc12345 .1.3.6.1.2.1.1.2.0");
    }
    
    /**
     * Creates a new instance of ParseArguments
     */
    protected ParseArguments newParseArgumentsInstance(String[] args)
    {
        return new ParseArguments(args, "?ho", "utvaAXxcpmnrw");
    }
}// end of class 

