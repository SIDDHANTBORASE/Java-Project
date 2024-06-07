import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final List<ClientHandler> clients;
    private PrintWriter out;

    public ClientHandler(Socket clientSocket, List<ClientHandler> clients) {
        this.clientSocket = clientSocket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String username = in.readLine();
            broadcast(username + " has joined the chat.");

            String message;
            while ((message = in.readLine()) != null) {
                broadcast(username + ": " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeClient();
        }
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            if (client != this) {
                client.sendMessage(message);
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void closeClient() {
        try {
            clients.remove(this);
            clientSocket.close();
            broadcast("A user has left the chat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
