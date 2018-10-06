package labAssignment1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Httpc {
	private static Scanner scanner;

	// Main Method
	public static void main(String[] args) {

		System.out.println("Please enter http command");
		scanner = new Scanner(System.in);
		String inputStr = scanner.nextLine();
		String[] inputStrArray = inputStr.split(" ");
		Httpc httpc = new Httpc();

		if (inputStrArray == null) {
			return;
		}

		for (int i = 0; i < inputStrArray.length; i++) {
			if (inputStrArray[i].equals("help")) {
				httpc.httpcHelp(inputStrArray);
			}
		}

		if (inputStrArray[0].equals("httpc") & inputStrArray[1].equals("get")) {
			try {
				httpc.httpcGet(inputStrArray);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (inputStrArray[0].equals("httpc") & inputStrArray[1].equals("post")) {
			try {
				httpc.httpcPost(inputStrArray);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// Get Method
	public void httpcGet(String[] str) throws IOException, FileNotFoundException {
		String host = "";
		String strGetUrl = "";
		String[] strGetHostArray;
		String strBody = "";

		// Get with query parameters
		if (str.length == 3) {
			strGetUrl = str[2];
			strGetUrl = strGetUrl.replace("'", "");

			// Host
			strGetHostArray = strGetUrl.split("/");
			host = strGetHostArray[2];

			// Body and Parameters
			strBody = strGetHostArray[3];

			@SuppressWarnings("resource")
			Socket mySocket = new Socket(host, 80);
			PrintWriter wtr = new PrintWriter(mySocket.getOutputStream());
			wtr.println("GET " + strBody + " HTTP/1.1");
			wtr.println("Host: " + host);
			wtr.println("");
			wtr.flush();

			BufferedReader bufRead = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			String outStr;
			String totalOutStr = "";

			// Prints each line of the response
			while ((outStr = bufRead.readLine()) != null) {
				totalOutStr = totalOutStr + outStr + System.lineSeparator();
			}
			int splitat = totalOutStr.indexOf("{");
			String body = totalOutStr.substring(splitat + 1);
			System.out.println(body);

			// Closes out buffer and writer
			bufRead.close();
			wtr.close();
		}

		// Get with verbose option and -o
		if (str[2].equals("-v")) {
			strGetUrl = str[3];
			strGetUrl = strGetUrl.replace("'", "");

			// Host
			strGetHostArray = strGetUrl.split("/");
			host = strGetHostArray[2];

			// Body and Parameters
			strBody = strGetHostArray[3];

			@SuppressWarnings("resource")
			Socket mySocket = new Socket(host, 80);
			PrintWriter wtr = new PrintWriter(mySocket.getOutputStream());
			wtr.println("GET " + strBody + " HTTP/1.1");
			wtr.println("Host: " + host);
			wtr.println("");
			wtr.flush();

			BufferedReader bufRead = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			String outStr = "";
			String totalOutStr = "";
			String fileName = "";

			// Prints each line of the response
			while ((outStr = bufRead.readLine()) != null) {
				totalOutStr = totalOutStr + outStr + System.lineSeparator();
			}
			for (int i = 0; i < str.length; i++) {
				if (str[i].equals("-o")) {
					fileName = str[i + 1];
					BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
					writer.write(totalOutStr);
					writer.close();

				}
			}
			if (fileName.isEmpty()) {
				System.out.println(totalOutStr);
			}

			// Closes out buffer and writer
			bufRead.close();
			wtr.close();

		}

	}

	// Post Method
	public void httpcPost(String[] str) throws IOException {
		String strUrl = str[str.length - 1];
		String content_Type = "";
		String content_Data = "";
		String[] strUrlArray;
		String[] contentTypeArray;
		String[] contentDataArray;
		String host = "";
		String content_Type_Info = "";
		String content_Data_Info = "";
		int dataLen = 0;
		String fileName = "";

		// post with query parameters
		if (str[2].equals("-h")) {
			for (int i = 0; i < str.length; i++) {
				if (str[i].equals("-h")) {
					content_Type = str[i + 1];
				} else if (str[i].equals("-d")) {
					content_Data = str[i + 1];
				} else if (str[i].equals("-f")) {
					fileName = str[i + 1];
					content_Data = fileName;
				}

			}

			// host
			strUrlArray = strUrl.split("/");
			host = strUrlArray[2];

			// Content_Type
			contentTypeArray = content_Type.split(":");
			content_Type_Info = contentTypeArray[0] + ":" + contentTypeArray[1];

			// Content_Data
			if (content_Data == fileName) {
				BufferedReader reader = new BufferedReader(new FileReader(fileName));
				while ((content_Data = reader.readLine()) != null) {
					contentDataArray = content_Data.split(":");
					content_Data_Info = contentDataArray[0] + ":" + contentDataArray[1];
					dataLen = content_Data_Info.length();
				}

				reader.close();

			} else {
				content_Data = content_Data.replace("'", "");
				contentDataArray = content_Data.split(":");
				content_Data_Info = contentDataArray[0] + ":" + contentDataArray[1];
				dataLen = content_Data_Info.length();

			}

			@SuppressWarnings("resource")
			Socket mySocket = new Socket(host, 80);
			PrintWriter wtr = new PrintWriter(mySocket.getOutputStream());
			wtr.println("POST /post HTTP/1.1");
			wtr.println("Host: " + host);
			wtr.println(content_Type_Info);
			wtr.println("Content-Length: " + dataLen);
			wtr.println("");
			wtr.print(content_Data_Info);
			wtr.flush();

			BufferedReader bufRead = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			String outStr;

			// Prints each line of the response
			String totalOutStr = "";
			while ((outStr = bufRead.readLine()) != null) {
				totalOutStr = totalOutStr + outStr + System.lineSeparator();
			}
			int splitat = totalOutStr.indexOf("{");
			String body = totalOutStr.substring(splitat + 1);
			System.out.println(body);

			// Closes out buffer and writer
			bufRead.close();
			wtr.close();

		}

		// verbose option
		if (str[2].equals("-v")) {
			for (int i = 0; i < str.length; i++) {
				if (str[i].equals("-v")) {
					content_Type = str[i + 1];
				} else if (str[i].equals("-d")) {
					content_Data = str[i + 1];
				} else if (str[i].equals("-f")) {
					fileName = str[i + 1];
					content_Data = fileName;
				}

			}

			// host
			strUrlArray = strUrl.split("/");
			host = strUrlArray[2];

			// Content_Type
			contentTypeArray = content_Type.split(":");
			content_Type_Info = contentTypeArray[0] + ":" + contentTypeArray[1];

			// Content_Data
			if (content_Data == fileName) {
				BufferedReader reader = new BufferedReader(new FileReader(fileName));
				while ((content_Data = reader.readLine()) != null) {
					contentDataArray = content_Data.split(":");
					content_Data_Info = contentDataArray[0] + ":" + contentDataArray[1];
					dataLen = content_Data_Info.length();
				}

				reader.close();

			} else {
				content_Data = content_Data.replace("'", "");
				contentDataArray = content_Data.split(":");
				content_Data_Info = contentDataArray[0] + ":" + contentDataArray[1];
				dataLen = content_Data_Info.length();

			}

			@SuppressWarnings("resource")
			Socket mySocket = new Socket(host, 80);
			PrintWriter wtr = new PrintWriter(mySocket.getOutputStream());
			wtr.println("POST /post HTTP/1.1");
			wtr.println("Host: " + host);
			wtr.println(content_Type_Info);
			wtr.println("Content-Length: " + dataLen);
			wtr.println("");
			wtr.print(content_Data_Info);
			wtr.flush();

			BufferedReader bufRead = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			String outStr;

			// Prints each line of the response
			while ((outStr = bufRead.readLine()) != null) {
				System.out.println(outStr);
			}

			// Closes out buffer and writer
			bufRead.close();
			wtr.close();
		}

	}

	// Help Method

	public void httpcHelp(String[] str) {
		if (str.length == 2 & str[0].equals("httpc") & str[1].equals("help")) {
			System.out.println("httpc is a curl-like application but supports HTTP protocol only.\n" + "Usage: \n"
					+ "   httpc command [arguments]\n " + "The commands are:\n"
					+ "   get executes a HTTP GET request and prints the response.\n"
					+ "   post executes a HTTP POST request and prints the response. \n"
					+ "   help prints this screen. \n"
					+ "Use \"httpc help [command]\" for more information about a command.\n");

		} else if (str.length == 3 & str[0].equals("httpc") & str[1].equals("help") & str[2].equals("get")) {
			System.out.println(
					"usage: httpc get [-v] [-h key:value] URL\n" + "Get executes a HTTP GET request for a given URL.\n"
							+ "   -v            Prints the detail of the response such as protocol, status,\n"
							+ "and headers.\n" + "   -h key:value  Associates headers to HTTP Request with the format\n"
							+ "'key:value'.");

		} else if (str.length == 3 & str[0].equals("httpc") & str[1].equals("help") & str[2].equals("post")) {
			System.out.println("usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL\n"
					+ "Post executes a HTTP POST request for a given URL with inline data or from\n" + "file.\n"
					+ "   -v             Prints the detail of the response such as protocol, status,\n"
					+ "and headers.\n" + "   -h key:value   Associates headers to HTTP Request with the format\n"
					+ "'key:value'.\n" + "   -d string      Associates an inline data to the body HTTP POST request.\n"
					+ "   -f file        Associates the content of a file to the body HTTP POST\n" + "request.\n"
					+ "Either [-d] or [-f] can be used but not both.");
		}

	}
}
