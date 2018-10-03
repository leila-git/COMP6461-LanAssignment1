package labAssignment1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Httpc {
	private static Scanner scanner;
	
	public static void main(String[] args) {
		
		System.out.println("Please enter http command");
		scanner = new Scanner(System.in);
		String inputStr=scanner.nextLine();
		String [] inputStrArray=inputStr.split(" ");
		Httpc httpc=new Httpc();
		
		
		if ( inputStrArray == null ) {
			return;
		}
		
		for (int i = 0; i < inputStrArray.length; i++) {
			if (inputStrArray[i].equals("help")){
				httpc.httpcHelp(inputStrArray);
			}
		}
		
		if(inputStrArray[0].equals("httpc") & inputStrArray[1].equals("get")) {
			try {
				httpc.httpcGet(inputStrArray);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if (inputStrArray[0].equals("httpc") & inputStrArray[1].equals("post")) {
			httpc.httpcPost(inputStrArray);
		}
		
	}
	
	
	
	
	public void httpcGet(String[] str) throws IOException {
		String host="";
		String strGetUrl="";
		String[] strGetHostArray;
		String[] strGetBodyArray;
		String[] strKeyValues;
		String strParam="";
		String strBody="";
		Map<String,String> urlMap;
		String[] strKeyValuesMap;
	
//get without any option
		if(str.length==3) {
		strGetUrl=str[2];
		strGetUrl=strGetUrl.replace("'", "");
		
		//Host
		strGetHostArray= strGetUrl.split("/");
		host=strGetHostArray[2];
		
		//Body and Parameters
		strBody=strGetHostArray[3];
//		strGetBodyArray=strBody.split("\\?");
//		
//		strParam=strGetBodyArray[1];
//		strKeyValues=strParam.split("&");
//		
//		//Key & Value
//		for (int i = 0; i < strKeyValues.length; i++) {
//			strKeyValuesMap=strKeyValues[i].split("=");	
//			urlMap=new HashMap<>();
//			urlMap.put(strKeyValuesMap[0], strKeyValuesMap[1]);
//		}
		
		@SuppressWarnings("resource")
		Socket mySocket=new Socket(host,80);
		 PrintWriter wtr = new PrintWriter(mySocket.getOutputStream());
		 wtr.println("GET "+strBody+" HTTP/1.1");
	     wtr.println("Host: "+host);
	     wtr.println("");
	     wtr.flush();
	     
		BufferedReader bufRead  = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
		String outStr;
		String totalOutStr="";

        //Prints each line of the response 
        while((outStr = bufRead.readLine()) != null){
        	totalOutStr = totalOutStr + outStr  +System.lineSeparator(); 
        }
        int splitat=totalOutStr.indexOf("{");
        String verbosePart = totalOutStr.substring(0, splitat) ;
  //      String body = totalOutStr.substring(splitat +1);
//        System.out.println(totalOutStr);
//        System.out.println("split at" +splitat);
        System.out.println(verbosePart);
        
        //Closes out buffer and writer
        bufRead.close();
        wtr.close();
		}
	
//get with -v
		if(str.length==4 & str[2].equals("-v")) {
			strGetUrl=str[3];
			strGetUrl=strGetUrl.replace("'", "");
			
			//Host
			strGetHostArray= strGetUrl.split("/");
			host=strGetHostArray[2];
			
			//Body and Parameters
			strBody=strGetHostArray[3];
			
			@SuppressWarnings("resource")
			Socket mySocket=new Socket(host,80);
			 PrintWriter wtr = new PrintWriter(mySocket.getOutputStream());
			 wtr.println("GET "+strBody+" HTTP/1.1");
		     wtr.println("Host: "+host);
		     wtr.println("");
		     wtr.flush();
		     
			BufferedReader bufRead  = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			String outStr;

	        //Prints each line of the response 
	        while((outStr = bufRead.readLine()) != null){
	            System.out.println(outStr);
	        }
	        
	        //Closes out buffer and writer
	        bufRead.close();
	        wtr.close();
			
			
		}
		
	}
	
	
	
	
	
	
	
	public void httpcPost(String[] str) {
		
	}
	
	
	
	
	
	public void httpcHelp(String[] str) {
		if(str.length==2 & str[0].equals("httpc") & str[1].equals("help")) {
			System.out.println("httpc is a curl-like application but supports HTTP protocol only.\n"
					+ "Usage: \n"
					+ "   httpc command [arguments]\n "
					+ "The commands are:\n"
					+ "   get executes a HTTP GET request and prints the response.\n"
					+ "   post executes a HTTP POST request and prints the response. \n"
					+ "   help prints this screen. \n"
					+ "Use \"httpc help [command]\" for more information about a command.\n");
		
		}else if (str.length==3 & str[0].equals("httpc") & str[1].equals("help") & str[2].equals("get")) {
			System.out.println("usage: httpc get [-v] [-h key:value] URL\n"
					+"Get executes a HTTP GET request for a given URL.\n"
					+"   -v            Prints the detail of the response such as protocol, status,\n"
					+"and headers.\n"
					+"   -h key:value  Associates headers to HTTP Request with the format\n"
					+"'key:value'.");
			
		}else if (str.length==3 & str[0].equals("httpc") & str[1].equals("help") & str[2].equals("post")) {
			System.out.println("usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL\n"
					+ "Post executes a HTTP POST request for a given URL with inline data or from\n"
					+ "file.\n"
					+ "   -v             Prints the detail of the response such as protocol, status,\n"
					+ "and headers.\n"
					+ "   -h key:value   Associates headers to HTTP Request with the format\n"
					+ "'key:value'.\n"
					+ "   -d string      Associates an inline data to the body HTTP POST request.\n"
					+ "   -f file        Associates the content of a file to the body HTTP POST\n"
					+ "request.\n"
					+ "Either [-d] or [-f] can be used but not both.");
		}
		
		
	}
}
