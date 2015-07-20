
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class recieveAckThread implements Runnable {

	int rec_seq,rcv_Zero,rcv_PackType;
	DatagramSocket serverSocket;
    ByteArrayInputStream bais1;
 	DataInputStream dais1;
    byte[] recData;
    int uNum;
	
	public recieveAckThread(int portno,int u_num)
	{
		try {
			serverSocket = new DatagramSocket(portno);
			uNum=u_num;
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		recData = new byte[8];
		
		Thread t= new Thread(this);
		t.start();
		
	}
	
	public void run(){
	   
	  DatagramPacket recPacket;
	     
	  while(true)
	  {
		  System.out.println("inside recieve ack while $$$$$ ");
		  recPacket = new DatagramPacket(recData, recData.length);
	     try {
			serverSocket.receive(recPacket);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	    // InetAddress rcv_address = recPacket.getAddress();
		System.out.println("recieved packet @@@ ");
	     String rcv_address1 = (recPacket.getAddress()).toString();
	     String[] result1 = rcv_address1.split("/");
		    String rcv_address = result1[1];
	     
	     System.out.println("recieved from port      " + recPacket.getPort());
	     System.out.println("recieved from IP      " + recPacket.getAddress());
	     System.out.println("\n Packet length: " + recPacket.getLength());
	     
	     
	     
	     bais1 = new ByteArrayInputStream(recPacket.getData());
	        dais1 = new DataInputStream(bais1);
	        try {
				rec_seq = dais1.readInt();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        System.out.println("Sequence number recieved = " + rec_seq); 
		     
	        try {
				rcv_Zero = dais1.readShort();
				rcv_PackType = dais1.readShort();
	        } catch (IOException e) {
				e.printStackTrace();
			}
	        
	        
	        if (rec_seq==Client2.SEQUENCE_NUMBER){ 
	        System.out.println("yes got acknowl correctly ");
	        	
	        }
	        for(int i=0;i<uNum;i++)
	        {
	        	System.out.println("inside breaking for");
	        	System.out.println(Client2.IPaddress[i]  + rcv_address );
	        	
	        	if(Client2.IPaddress[i].equals(rcv_address))
	        	{	
	        	Client2.AckIndicator[i]=0;
	        	System.out.println("indicator made 0 for position   " + i  + "    "+ rcv_address );
	        	//break;
	            }
	        }

	  }
	}
}
