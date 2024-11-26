package model;

import java.util.List;

public class WordleGameSession {
    private WordleGame player1Game;
    private WordleGame player2Game;
    private GameState gameState;

    public enum GameState {
        WAITING_FOR_PLAYERS,
        IN_PROGRESS,
        PLAYER1_WON,
        PLAYER2_WON,
        DRAW
    }

    public WordleGameSession(List<String> words, int maxTries) {
        player1Game = new WordleGame(words, maxTries);
        player2Game = new WordleGame(words, maxTries);
        gameState = GameState.WAITING_FOR_PLAYERS;
    }

    // Métodos para gerenciar o estado do jogo
    public void startGame() {
        if (gameState == GameState.WAITING_FOR_PLAYERS) {
            gameState = GameState.IN_PROGRESS;
        }
    }

    public MoveResult processPlayerMove(int playerNumber, String guess) throws NotInWordListException {
        if (gameState != GameState.IN_PROGRESS) {
            throw new IllegalStateException("O jogo não está em andamento");
        }

        WordleGame playerGame = (playerNumber == 1) ? player1Game : player2Game;
        MoveResult result = playerGame.checkWord(guess);

        // Verifica condição de término do jogo
        updateGameState(result, playerNumber);

        return result;
    }

    private void updateGameState(MoveResult result, int playerNumber) {
        switch (result.getResultType()) {
            case WIN:
                gameState = (playerNumber == 1) 
                    ? GameState.PLAYER1_WON 
                    : GameState.PLAYER2_WON;
                break;
            case LOSE:
                // Se um jogador perder todas as tentativas, o outro vence
                gameState = (playerNumber == 1) 
                    ? GameState.PLAYER2_WON 
                    : GameState.PLAYER1_WON;
                break;
            default:
                // Jogo continua
                break;
        }
    }

    // Outros métodos de gerenciamento da sessão
    public GameState getGameState() {
        return gameState;
    }

    // Métodos para obter informações dos jogos
    public WordleGame getPlayer1Game() {
        return player1Game;
    }

    public WordleGame getPlayer2Game() {
        return player2Game;
    }
}