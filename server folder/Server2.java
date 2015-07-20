
import java.io.*;
import java.util.*;
import java.math.BigInteger;
import java.net.*;

public class Server2 {
	
	 private static final int NULL = 0;
	 private static double p;
	// public static int[] seqno=new int[2];
	 static int Prev_seqnum=0,Seqnum;
	 public static int k=0;
	 public int MSS=500;
	 public static int server_port; 
	 public static String file_name; 
	    
	 public static void main(String args[]) throws IOException
	    {
		 System.out.println("Enter command in format p2mpserver#port#file-name#p");
			Scanner input=new Scanner(System.in);
			String server=input.nextLine();
			
			
			String[] result1 = server.split("#");
		    server_port = Integer.parseInt(result1[1]);
		    file_name=result1[2];
		    
		    p=Double.parseDouble(result1[3]);
		    
		    
		
		//System.out.println("Enter the probabiltiy for packet to be lost : ");
		//Scanner in = new Scanner(System.in);
		//p=in.nextDouble();
		 Server2 s1=new Server2();
		 s1.rcv_data();
	    }
	 
	 public void send_ack(int s) throws IOException
	 {
		 
		    ByteArrayOutputStream baos;
	    	DataOutputStream daos;
	    	byte[] send_ack=new byte[8];
	    	baos = new ByteArrayOutputStream();
	        daos = new DataOutputStream(baos);
	        
	     //   Short Ack_Zero=0;
	      // uint16_t PackType=43690;
	        
	        daos.writeInt(s);
	        daos.writeShort(0);
	    	daos.writeShort(43690);
	    	send_ack= baos.toByteArray();
	    	InetAddress IpAddress = InetAddress.getByName("10.139.57.169");
	    	System.out.println("Inside send ack"); 
	    	DatagramSocket clientSocket = new DatagramSocket();
	    	DatagramPacket sendack = new DatagramPacket(send_ack, send_ack.length, IpAddress, 9880);
	    	clientSocket.send(sendack);
	    	clientSocket.close();
	    	System.out.println("chekcing"); 
	 }
	 
	 void rcv_data() throws IOException
	 {
		 DatagramSocket serverSocket = new DatagramSocket(7735);
		 
		 Random rand=new Random();
		 
		 int checksum=0;
	     byte[] recData = new byte[MSS+8];
	     byte[] recData1=new byte[MSS];
	     byte[] recData2=new byte[MSS];
	     int i =0;
	     //p=0;
	     ByteArrayInputStream bais;
	     DataInputStream dais;
	     FileWriter file = new FileWriter(file_name);
	     int chksum,PackType;
         while(true)
	     {
        	
	         DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
	         serverSocket.receive(recPacket);
	         System.out.println("\n Packet length: " + recPacket.getLength());
	         recData1= recPacket.getData();
	            
	         bais = new ByteArrayInputStream(recData1);
		     dais = new DataInputStream(bais);
		    
	         // file.write(line);
	         // System.out.println("\nPacket" + ++i + " written to file\n");
	          double r=rand.nextDouble();
	          System.out.println("Random number    "+r);
	         // chksum=checksum;
	         // if(seqno[0]==seqno[1])
	         // {
	         if(r>p)
	          {
	        	 
	        	 Seqnum = dais.readInt();
			     System.out.println("Sequence number = " + Seqnum); 
			     //seqno[0]=Seqnum;
			     //seqno[1]=++k;
				     
			     chksum = dais.readShort();
			     PackType = dais.readShort();
			     int count = dais.read(recData2);
			    // while(dais.read(recData2)!=NULL)
			     // {
			    	//  checksum +=dais.read(recData2);
			    	  
			     // }
			      //System.out.println("Checksum of " + ++i + "th packet  = " + checksum); 
				  //System.out.println("number of data bytes read = " + count); 
			     int length = (recPacket.getLength()) - 8;
			      String line = new String(recData2,0,length);
		          System.out.println("\n Data: " + line);
	        	         	 
	        	  	 
	        	 
	        	 
	        	 
	        	 if(Seqnum != Prev_seqnum){
	        	  file.write(line);
		          System.out.println("\nPacket" + Seqnum+ " written to file\n" );
	           
	            System.out.println("\nPacket" + Seqnum + " reception successful and ack is being sent");
	            
	            
	            }
	        	 send_ack(Seqnum);
	        	 
	        	 
	         }
	         
	         else
	         {
	        	// System.out.println("\nPacket" +Seqnum + " is lost with sequence num    "+Seqnum);
	        	 
	        	 System.out.println("\nPacket Loss : Sequence number  =  " + Seqnum );
	        	 
	         }
	         // }
	          //else 
	          //{send_ack(Seqnum);
	        	  //System.out.println("\nPacket" +Seqnum + " is discarded with   "+Seqnum);
	            
	          //}
	          file.flush();
	         Prev_seqnum=Seqnum;
        	 
        	 
	     }// while 
	 
	 }
	 
}

	        

	 
	    
	