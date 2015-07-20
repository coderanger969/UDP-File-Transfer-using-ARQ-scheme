


import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

public class Client2 {
	public static volatile boolean  Tflag = true;
	public static int[] PortNo ; //= {7735,7735,7735};
	public static String[] IPaddress;// = {"10.139.57.169","10.139.61.228","10.139.60.112"};
	
	public static volatile int[] AckIndicator;//= new int[3];
	public static int Unum; // numberof users
	
	public static int SEQUENCE_NUMBER = 0;
	
	 //String filePath = "C:/Users/Silpa/Desktop/IP_Project/IP_project2/RFC7256.txt";
	String filePath;
	 
	 Client2(int n,String cmd ){
		 Unum = n;
		 PortNo= new int[n];
		 IPaddress=new String[n];
		 AckIndicator= new int[n];
		 
		 String[] result = cmd.split("#");
			
			
			for (int z=0;z<n;z++){
				IPaddress[z]=result[z+1];
				PortNo[z]=7735;
							
			}
			
			filePath= result[n+2];
		 
		 
	 }
		
	
	
	public void rdt_send(){
		try{	
			int count=0;
	        int MAX_SIZE =500;

	        DatagramSocket clientSocket = new DatagramSocket();
	        DatagramSocket clientSocket1 = new DatagramSocket();
	      //  InetAddress IpAddress = InetAddress.getByName("192.168.1.42");
	       // InetAddress IpAddress1 = InetAddress.getByName("192.168.1.42");
	        ByteArrayOutputStream baos;
	    	DataOutputStream daos;
	      // clientSocket.connect(IpAddress,9870);
	       //clientSocket1.connect(IpAddress,9890);
	       
	        byte[] sendData = new byte[MAX_SIZE];
	        
	       byte[] sendData1 = new byte[508]; // MSS+8
	        int k;
	    
	        
	        

	       
	        File file = new File(filePath);
	        FileInputStream fis = new FileInputStream(file);
	        int totLength = 0; 
	 int i=1;
	        while((count = fis.read(sendData)) != -1)    //calculate total length of file
	        {
	        	System.out.println(" for round     " + i);
	            totLength += count;
	            i++;
	        }

	        System.out.println("Total Length :" + totLength);

	        int noOfPackets = totLength/MAX_SIZE;
	        System.out.println("No of packets : " + noOfPackets);
	        
	        int off = noOfPackets * MAX_SIZE;  

	        int lastPackLen = totLength - off;
	        System.out.println("\nLast packet Length : " + lastPackLen);

	        byte[] lastPack = new byte[lastPackLen-1];  
	        
	        
	        FileInputStream fis1 = new FileInputStream(file);
	        int Checksum=123,PackType=21845;
	        
	        //while((count = fis1.read(sendData)) != -1 && (noOfPackets!=0))
	        while((count = fis1.read(sendData)) != -1 )
	        { 
	        	 for(int l=0;l<Unum;l++){
	        		 AckIndicator[l]=1;
	        		 
	        	 }
	        	
	        	
	        	//AckIndicator[0]=1;
	        	//AckIndicator[1]=1;
	        	//AckIndicator[2]=1;
	        	SEQUENCE_NUMBER = SEQUENCE_NUMBER +1;
	        	baos = new ByteArrayOutputStream();
		        daos = new DataOutputStream(baos);
		        daos.writeInt(SEQUENCE_NUMBER);
		        daos.writeShort(Checksum);
		        daos.writeShort(PackType);
		        daos.write(sendData,0,sendData.length);
		        sendData1= baos.toByteArray();
		        
	        	
	        /*	
	        	for (int j= 8; j < sendData1.length; j++) {
	        		sendData1[j] = sendData[j-8];
	            }*/
	        	
	            if(noOfPackets<=0)
	                break;
	            System.out.println(sendData1.length);
	            System.out.println(new String(sendData1));
	            
	            for(int t=0;t<Unum;t++){
	            	  InetAddress IpAddress9 = InetAddress.getByName(IPaddress[t]);
	            	DatagramPacket sendPacket_c2 = new DatagramPacket(sendData1, sendData1.length,IpAddress9,PortNo[t]);
	            	clientSocket.send(sendPacket_c2);
	            	System.out.println("sent for time   "+ t);
	            }
	            
	            
	          //  DatagramPacket sendPacket_c2 = new DatagramPacket(sendData1, sendData1.length, IpAddress1, 9890);
	            //clientSocket.send(sendPacket_c2);
	           
	          //  DatagramPacket sendPacket_c1 = new DatagramPacket(sendData1, sendData1.length, IpAddress, 9870);	            
	            
	         //clientSocket.send(sendPacket_c1);
	           
	           
	           
	           
	           // System.out.println(sendPacket_c1.getLength());
	         //   System.out.println(sendPacket_c2.getLength());
	      //      System.out.println(sendPacket_c1);
	           // System.out.println(sendPacket_c2);
	            System.out.println("sent to 2nd Server");
	            MyTimerTask1 Task;
	        	Timer myTimer = new Timer();
	        	Task = new MyTimerTask1(SEQUENCE_NUMBER);
	        	myTimer.schedule(Task,10000);
	        	
	           String Fate_rcv= recieveAck(SEQUENCE_NUMBER);
	           System.out.println("fate  " + Fate_rcv);
	            if (Fate_rcv.equals("1")){
	            myTimer.cancel();
				myTimer.purge(); 
	            }
	            boolean flag;
	            do{
	             flag = false;
	             System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");

	             for(int j=0;j<Unum;j++){
	            	 
	            	 System.out.println("######");
	            	 System.out.println(" ack indicator :    " +j+"     "+ AckIndicator[j]);
	            	 if (AckIndicator[j]==1) {
	            		 
	            		 System.out.println("*************************************************************************************************************&");
	            		 
	            		 flag=true;
	            		 int portnum = PortNo[j];
	            		 InetAddress IpAddress8 = InetAddress.getByName(IPaddress[j]);
	            		 
	            		 DatagramPacket sendPacket_c3 = new DatagramPacket(sendData1, sendData1.length, IpAddress8,portnum);
	            		 clientSocket.send(sendPacket_c3);
	            		 
	            	 }
	            	 
	            	
	            	 
	             } // for
	             
	             MyTimerTask1 Task1;
	 	        	Timer myTimer1 = new Timer();
	 	        	Task1 = new MyTimerTask1(SEQUENCE_NUMBER);
	            	 
	            	 myTimer1.schedule(Task1,10000);
	            		            	 
	            	 String Fate_rcv1= recieveAck(SEQUENCE_NUMBER);
	            	 
	  	           System.out.println("fate  " + Fate_rcv1);
	  	            if (Fate_rcv1.equals("1")){
	  	            myTimer1.cancel();
	  				myTimer1.purge(); 
	  	            }// if
	             
	            	
	            }while(flag);  // do while
	            
	            
	            
	         /*   int t=1;
	            while(flag){
	            clientSocket.send(sendPacket);
	            
	            System.out.println("sending same packet for " + t + "    time");
	            recieveAck();
	            t++;
	            
	            }
	            */
	            
	            System.out.println("========");
	            /*   System.out.println("last pack sent" + sendPacket_c1);*/
	        noOfPackets--;
	        } // while
	       // System.out.println("\nlast packet\n");
	        System.out.println(new String(sendData1));

	        lastPack = Arrays.copyOf(sendData1,lastPackLen+8);

	        System.out.println("\nActual last packet\n");
	        System.out.println(new String(lastPack));
	                
	        //DatagramPacket sendPacket1 = new DatagramPacket(lastPack, lastPack.length, IpAddress,9876);
	        for(int t=0;t<Unum;t++){
          	  InetAddress IpAddress7 = InetAddress.getByName(IPaddress[t]);
          	DatagramPacket sendPacket_c3 = new DatagramPacket(lastPack, lastPack.length,IpAddress7,PortNo[t]);
          	clientSocket.send(sendPacket_c3);
          	System.out.println("sent for time   "+ t);
          }
	        System.out.println("sent to 2nd Server");
            MyTimerTask1 Task;
        	Timer myTimer = new Timer();
        	Task = new MyTimerTask1(SEQUENCE_NUMBER);
        	myTimer.schedule(Task,10000);
        	
           String Fate_rcv= recieveAck(SEQUENCE_NUMBER);
           System.out.println("fate  " + Fate_rcv);
            if (Fate_rcv.equals("1")){
            myTimer.cancel();
			myTimer.purge(); 
            }
            boolean flag;
            do{
             flag = false;
             System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");

             for(int j=0;j<Unum;j++){
            	 
            	 System.out.println("######");
            	 System.out.println(" ack indicator :    " +j+"     "+ AckIndicator[j]);
            	 if (AckIndicator[j]==1) {
            		 
            		 System.out.println("*************************************************************************************************************&");
            		 
            		 flag=true;
            		 int portnum = PortNo[j];
            		 InetAddress IpAddress8 = InetAddress.getByName(IPaddress[j]);
            		 
            		 DatagramPacket sendPacket_c3 = new DatagramPacket(sendData1, sendData1.length, IpAddress8,portnum);
            		 clientSocket.send(sendPacket_c3);
            		 
            	 }
            	 
            	
            	 
             } // for
             
             MyTimerTask1 Task1;
 	        	Timer myTimer1 = new Timer();
 	        	Task1 = new MyTimerTask1(SEQUENCE_NUMBER);
            	 
            	 myTimer1.schedule(Task1,10000);
            		            	 
            	 String Fate_rcv1= recieveAck(SEQUENCE_NUMBER);
            	 
  	           System.out.println("fate  " + Fate_rcv1);
  	            if (Fate_rcv1.equals("1")){
  	            myTimer1.cancel();
  				myTimer1.purge(); 
  	            }// if
             
            	
            }while(flag);  // do while
            
	        
	       // clientSocket.send(sendPacket1);
	        //System.out.println("last pack sent" + sendPacket_c3);
	        
	        
		}// try
		catch(IOException e){
			e.printStackTrace();
			
		} 
		
		
		
		

		
		
	}  // rdt_send method
	
	
String recieveAck(int SEQNUM)throws IOException{
		
	System.out.println("inside recieveAck");
	
	
	Tflag = true;
	String Fate= "0";
	
	while(Client2.Tflag)
	{
		//System.out.println("inside while of RecieveAck");
		 int count=0;
		 for(int j=0;j<Unum;j++)
		 { 
			 if (AckIndicator[j]== 0){
				 count= count +1;}
			 
			 
		 }
		 if(count==Unum) { Fate = "1";
			  
			  break;
		}
 	
		 
 	// FileWriter file = new FileWriter("C:\\Users\\manasa\\Desktop\\out.txt");
    //PrintWriter out = new PrintWriter(file);
      
		}
	
	return Fate;
	
	}
public static long getChecksum(InputStream is)
{
    CheckedInputStream cis = null;        
    long checksum = 0;
    try 
    {
        cis = new CheckedInputStream(is, new Adler32());
        byte[] tempBuf = new byte[128];
        while (cis.read(tempBuf) >= 0) 
        {
        }
        checksum = cis.getChecksum().getValue();
    } 
    catch (IOException e) 
    {
        checksum = 0;
    }
    finally
    {
        if (cis != null)
        {
            try
            {
                cis.close();
            }
            catch (IOException ioe)
            {                    
            }
        }
    }
    return checksum;
}
	
	public static void main (String args[]){
	
		System.out.println("Enter the nuber of users : ");
		Scanner in = new Scanner(System.in);
		
		int n = Integer.parseInt(in.nextLine());
		System.out.println("Enter the command p2mpclient#ip1#1p2#...#<serverportno>#file_name#MSS : ");
		String CMD = in.nextLine();
		
	Client2 c = new Client2(n,CMD);
	
	new recieveAckThread(9880,Unum);
	//new recieveAckThread(9860,Unum);
	//new recieveAckThread(9800,Unum);
	
	
	
	
	
	
	long startTime = System.currentTimeMillis();
	c.rdt_send();

	long elapsedTime = System.currentTimeMillis()-startTime;
	System.out.println("it took   " + elapsedTime + "    milliseconds to transfer file");
	System.out.println("File transfer coplete");
	
	//c.recieveAck();
	
	
	

       /* int off = noOfPackets * MAX_SIZE;  //calculate offset. it total length of file is 1048 and array size is 1000 den starting position of last packet is 1001. this value is stored in off.

        int lastPackLen = totLength - off;
        System.out.println("\nLast packet Length : " + lastPackLen);

        byte[] lastPack = new byte[lastPackLen-1];  //create new array without redundant information

		*/
		
		
		
	}
	
		

}

class MyTimerTask1 extends TimerTask{
	int seq_num;
	MyTimerTask1(int seq_no){
		seq_num= seq_no;
		
	}
	
	
	
	public void run (){
				
		System.out.println("Time OUT !!! " + seq_num );
		
		Client2.Tflag=false;
		
return;
	
	}
	
	
}







