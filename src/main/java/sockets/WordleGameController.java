package sockets;

import model.*;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@jakarta.websocket.server.ServerEndpoint(
    value = "/wordle",
    encoders = {MessageEncoder.class},
    decoders = {MessageDecoder.class}
)
public class WordleGameController {

    private static Session s1;
    private static Session s2;
    private static WordleGame game;
    private static String secret;

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        List<String> words = Wordplay.returnWords();

        // Selecionar uma palavra aleatória
        secret = words.get(new Random().nextInt(words.size()));

        if (s1 == null) {
            s1 = session;
            s1.getBasicRemote().sendObject(new GameControl(TypeState.OPEN, Player.PLAYER1, secret, null));
        } else if (s2 == null) {
            game = new WordleGame(secret);
            s2 = session;
            s2.getBasicRemote().sendObject(new GameControl(TypeState.OPEN, Player.PLAYER2, secret, null));
            sendMessage(new GameControl(TypeState.MESSAGE, game.getTurn(),"dd", null));
        } else {
            session.close();
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException, EncodeException {
        try {
            String ret = game.move(session == s1 ? Player.PLAYER1 : Player.PLAYER2, message);
            if (game.winner == Winner.NONE) {
                sendMessage(new GameControl(TypeState.MESSAGE, game.getTurn(), ret, null));
            } else {
                sendMessage(new GameControl(TypeState.ENDGAME, null, ret, game.winner));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException, EncodeException {
        switch (reason.getCloseCode().getCode()) {
            case 4000 -> {
                if (session == s1) {
                    s1 = null;
                } else {
                    s2 = null;
                }
            }
            case 1001, 4001 -> {
                if (session == s1) {
                    s2.getBasicRemote()
                            .sendObject(new GameControl(TypeState.ENDGAME, null, "fim", Winner.PLAYER2));
                    s1 = null;
                } else {
                    s1.getBasicRemote()
                            .sendObject(new GameControl(TypeState.ENDGAME, null, "fim", Winner.PLAYER1));
                    s2 = null;
                }
            }
            default -> {
                System.out.println(String.format("Close code %d incorrect", reason.getCloseCode().getCode()));
            }
        }
    }

    private void sendMessage(GameControl msg) throws EncodeException, IOException {
        s1.getBasicRemote().sendObject(msg);
        s2.getBasicRemote().sendObject(msg);
    }
}