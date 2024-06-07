import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);

            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter your username: ");
            String username = userInput.readLine();
            out.println(username);

            new Thread(() -> {
                String message;
                try {
                    while ((message = serverIn.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            String userInputMessage;
            while ((userInputMessage = userInput.readLine()) != null) {
                out.println(userInputMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
