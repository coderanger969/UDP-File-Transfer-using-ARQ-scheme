****************************************Instructions for Executing Point to MultiPoint File Transfer Protocol (P2MP FTP)***************************************
We have two folders:
1.Client Folder
2.Server Folder


*******************************************************Client Folder*************************************************************
Client Folder: This folder has the following .java files.
1. Client2.java
2. recieveAckThread.java.

***************************************************Server Folder******************************************************************
This folder has the following .java files

1.Server2.java 
In Server2.java go to line 99 and change the IP address to IP address of client.
 

****************************************************PROCESS OF EXECUTION*********************************************************

1.Open the java files in Eclipse software.
2.Place client and server on different machines
3. First go to Server machine and add server2.java to a project file in eclipse and run the file.
4. In the console, the program would ask for input. Input should be given in the format p2mpserver#port#file-name#p. For example in this project, server port is 7735, file_name is path
of where you want your file to be saved and p is loss probability. so input will be as follows: p2mpserver#7735#out.txt#0.05.
5.Then go to Client machine and add all the java files in Client folder to a project file in eclipse and run Client2.java file. 
6.In the console, the program will ask you for input. Input should be given in the format p2mpclient#ip1#1p2#...#<serverportno>#file_name#MSS.
Here ip1, ip2.... are IP Address of the servers to which client is transferring the file. 
Serverportno is the listening port of the server, which is 7735 in our project.
file_name is the path from where you are reading the file that you want to transfer.
MSS is the maximum segment size.

You can check in the console , the status of the file transmission.




















