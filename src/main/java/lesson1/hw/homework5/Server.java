package lesson1.hw.homework5;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server {

    public static final int PORT = 8181;

    public static void main(String[] args) {

        final Map<String, ClientHandler> clients = new HashMap<>();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    Scanner clientIn = new Scanner(clientSocket.getInputStream());
                    String clientId = clientIn.nextLine();
                    ClientHandler clientHandler = new ClientHandler(clientSocket, clientId, clients);
                    clients.put(clientId, clientHandler);
                    new Thread(clientHandler).start();
                } catch (IOException e) {
                    System.err.println("Произошла ошибка при взаимодействии с клиентом: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось начать прослушивать порт " + PORT, e);
        }
    }
}

class ClientHandler implements Runnable {

    private final Socket clientSocket;

    private final String clientId;
    private final PrintWriter out;
    private final Map<String, ClientHandler> clients;

    public ClientHandler(Socket clientSocket, String clientId, Map<String, ClientHandler> clients) throws IOException {
        this.clientSocket = clientSocket;
        this.clientId = clientId;
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.clients = clients;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        // Оповестить о подключении
        System.out.println("Подключился новый клиент: " + clientId);
        clients.values().stream()
                .filter(it -> !it.equals(this))
                .forEach(it -> it.send("Подключился новый клиент: " + clientId));
        // Основной алгоритм
        try (Scanner in = new Scanner(clientSocket.getInputStream())) {
            boolean isRunning = true;
            while (isRunning) {
                String input = in.nextLine();
                if (input.startsWith("@")) {
                    String toClientId = null;
                    String[] parts = input.split("\\s+");
                    if (parts.length > 0) {
                        toClientId = parts[0].substring(1);
                    }
                    if (toClientId != null) {
                        ClientHandler toClient = null;
                        for (String id: clients.keySet()) {
                            if (id.startsWith(toClientId)) {
                                toClient = clients.get(id);
                            }
                        }
                        if (toClient != null) {
                            toClient.send(clientId + ": " + input.replace("@" + toClientId + " ", ""));
                        } else {
                            this.send("Не найден клиент: " + toClientId);
                        }
                    }
                } else if (input.startsWith("/")) {
                    switch (input.substring(1)) {
                        case "exit":
                            isRunning = false;
                            break;
                        case "all":
                            this.send(clients
                                    .entrySet()
                                    .stream()
                                    .filter(it -> !it.getKey().equals(clientId))
                                    .map(it -> it.getKey())
                                    .collect(Collectors.joining("\n"))
                            );
                            break;
                    }
                } else {
                    System.out.println(clientId + ": " + input);
                    clients
                            .values()
                            .stream().filter(it -> !it.equals(this))
                            .forEach(it -> it.send(clientId + ": " + input));
                }
                if (clientSocket.isClosed()) {
                    isRunning = false;
                }
            }
        } catch (IOException e) {
            System.err.println("Произошла ошибка при взаимодействии с клиентом " + clientSocket + ": " + e.getMessage());
        }
        // Отключение клиента
        try {
            clientSocket.close();
            System.out.println("Клиент " + clientId + " отключился");
            clients.values().stream()
                    .filter(it -> !it.equals(this))
                    .forEach(it -> it.send("Клиент отключился: " + clientId));
            clients.remove(clientId);
        } catch (IOException e) {
            System.err.println("Ошибка при отключении клиента " + clientSocket + ": " + e.getMessage());
        }
    }

    public void send(String msg) {
        out.println(msg);
    }

}
