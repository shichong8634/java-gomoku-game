package Gobang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class NetworkManager {
    private Socket socket;
    private ServerSocket serverSocket;
    private BufferedReader in;
    private PrintWriter out;
    public boolean isHost;
    private Gomokulogic logic;
    private Runnable updateUI;

    public NetworkManager(Gomokulogic logic, Runnable updateUI) {
        this.logic = logic;
        this.updateUI = updateUI;
    }

    private void setupStreams() throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    // Host runs in background so UI stays responsive
    public boolean hostGame(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Thread hostThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Waiting on port: " + port);
                    socket = serverSocket.accept();
                    serverSocket.close();
                    isHost = true;
                    setupStreams();
                    startListenerThread();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        hostThread.start();
        return true;
    }

    public boolean joinGame(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            isHost = false;
            setupStreams();
            startListenerThread();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendMove(int r, int c, int color) {
        if (out != null) {
            out.println("MOVE," + r + "," + c + "," + color);
        }
    }

    private void startListenerThread() {
        Thread listener = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        final String m = msg;
                        // UI updates must run on the Swing thread
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                handleMessage(m);
                            }
                        });
                    }
                } catch (IOException e) {
                    System.out.println("Network disconnected.");
                }
            }
        });
        listener.start();
    }

    private void handleMessage(String msg) {
        String[] parts = msg.split(",");
        if (parts[0].equals("MOVE") && parts.length == 4) {
            int r = Integer.parseInt(parts[1]);
            int c = Integer.parseInt(parts[2]);
            int color = Integer.parseInt(parts[3]);
            logic.placePiece(r, c, color);
            if (logic.checkWin(r, c, color)) {
                String winner = (color == 1) ? "Black" : "White";
                JOptionPane.showMessageDialog(null, winner + " side wins!");
            }
        }
        if (updateUI != null) {
            updateUI.run();
        }
    }
}