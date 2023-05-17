import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String args[]) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;
        boolean quit = false;

        while (!quit) {
            Socket socket = new Socket("localhost", 1234);

            InputStream in = socket.getInputStream();
            DataInputStream dataIn = new DataInputStream(in);

            OutputStream out = socket.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(out);

            String choice = "";
            while (!loggedIn && !(choice.equalsIgnoreCase("L") || choice.equalsIgnoreCase("R"))) {
                System.out.println("Enter 'L' for login or 'R' for register:");
                choice = scanner.nextLine();
            }

            if (loggedIn) {
                System.out.println("1) New game");
                System.out.println("2) Quit");

                while (true) {
                    System.out.print("Enter your choice: ");
                    String menuChoice = scanner.nextLine();

                    if (menuChoice.equals("1")) {
                        // TO-DO: Implement new game logic
                        System.out.println("Starting a new game...");
                        break;
                    } else if (menuChoice.equals("2")) {
                        loggedIn = false;
                        break;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                }
            } else {
                System.out.println("Enter username:");
                String username = scanner.nextLine();

                System.out.println("Enter password:");
                String password = scanner.nextLine();

                String action;
                if (choice.equalsIgnoreCase("L")) {
                    action = "login";
                } else {
                    action = "register";
                }

                String input = action + ":" + username + ":" + password;
                dataOut.writeUTF(input);
                dataOut.flush();

                String serverResponse = dataIn.readUTF();
                System.out.println("The server replied: " + serverResponse);

                if (serverResponse.equals("Login successful!")) {
                    loggedIn = true;
                }
            }

            dataOut.close();
            dataIn.close();
            socket.close();
        }
    }
}
