import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {
	public static Scanner scan =  new Scanner(System.in);
    static Map<String, String> users = new HashMap<>(); // Store registered users and their passwords
    static Map<String, Boolean> loginStatus = new HashMap<>(); // Store login status of users

    public static void main(String args[]) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        while (true) {
            Socket socket = serverSocket.accept();

            InputStream in = socket.getInputStream();
            DataInputStream dataIn = new DataInputStream(in);

            OutputStream out = socket.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(out);

            String request = dataIn.readUTF();
            System.out.println("Client requested: " + request);

            String[] splitRequest = request.split(":");
            String action = splitRequest[0];
            String username = splitRequest[1];
            String password = splitRequest[2];

            String response;
            switch (action) {
                case "register":
                    if (users.containsKey(username)) {
                        response = "Username already exists. Please try another.";
                    } else {
                        users.put(username, password);
                        loginStatus.put(username, false);
                        response = "Registration successful!";
                    }
                    break;
                case "login":
                    if (!users.containsKey(username)) {
                        response = "Username not found. Please register first.";
                    } else if (!users.get(username).equals(password)) {
                        response = "Invalid password. Please try again.";
                    } else if (loginStatus.get(username)) {
                        response = "User already logged in.";
                    } else {
                        loginStatus.put(username, true);
                        response = "Login successful!";
                    }
                    break;
                default:
                    response = "Invalid action. Please send a valid request.";
            }

            dataOut.writeUTF(response);
            dataOut.flush();

            dataOut.close();
            dataIn.close();
            socket.close();

            if (response.equals("Login successful!") && action.equals("login")) {
                handleLoggedInUser(username);
            }
        }
    }

    private static void handleLoggedInUser(String username) {
        boolean quit = false;
        int opcao ;

        while (!quit) {
        	
            
            System.out.println("Logged-in user: " + username);
            System.out.println("1) New game");
            System.out.println("2) Quit");
            
            
            opcao = scan.nextInt();


			switch (opcao) {
			case 1:
				

			case 2:
				quit = true;
				
				
			default:
				System.out.println();
				System.out.println("------------------------");
				System.out.println();
				System.out.println("Escolha invalida, Por favor repita: ");
				break;
			}
        }

        // Reset login status when the user chooses to quit
        loginStatus.put(username, false);
    }
}