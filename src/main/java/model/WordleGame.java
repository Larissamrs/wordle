package model;

import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class WordleGame {
    private List<String> words;
    private String secretWord;
    private int maxTries;
    private int currentTries;
    private int wordLength;

    public WordleGame(List<String> words, int maxTries) {
        this.words = words;
        this.maxTries = maxTries;
        this.secretWord = selectRandomWord();
        this.currentTries = 0;
        this.wordLength = secretWord.length();
    }

    public WordleGame(String secret) {
        //TODO Auto-generated constructor stub
    }

    private String selectRandomWord() {
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    public MoveResult checkWord(String guess) throws NotInWordListException {
        // Validações
        validateGuess(guess);

        // Calcula o estado das letras
        int[] letterStates = calculateLetterStates(guess);

        // Incrementa tentativas
        currentTries++;

        // Verifica condições de vitória/derrota
        WordleResultType resultType = determineResultType(letterStates);

        return new MoveResult(resultType, secretWord, letterStates);
    }

    private void validateGuess(String guess) throws NotInWordListException {
        // Verifica tamanho da palavra
        if (guess.length() != wordLength) {
            throw new IllegalArgumentException(
                "O comprimento da palavra deve ser " + wordLength
            );
        }

        // Verifica se está na lista de palavras permitidas
        if (!words.contains(guess.toUpperCase())) {
            throw new NotInWordListException();
        }

        // Verifica limite de tentativas
        if (currentTries >= maxTries) {
            throw new IllegalStateException("Não há mais tentativas disponíveis.");
        }
    }

    private int[] calculateLetterStates(String guess) {
        guess = guess.toUpperCase();
        String secretWordUpper = secretWord.toUpperCase();
        int[] result = new int[wordLength];
        
        // Primeiro, marca letras exatamente corretas (verde - estado 2)
        for (int i = 0; i < wordLength; i++) {
            if (guess.charAt(i) == secretWordUpper.charAt(i)) {
                result[i] = 2;
            }
        }

        // Depois, marca letras presentes (amarelo - estado 1) 
        // e ausentes (cinza - estado 0)
        for (int i = 0; i < wordLength; i++) {
            if (result[i] == 2) continue; // Pula letras já marcadas como corretas

            char currentLetter = guess.charAt(i);
            
            // Conta ocorrências da letra na palavra secreta e no palpite
            long secretOccurrences = secretWordUpper.chars()
                .filter(ch -> ch == currentLetter)
                .count();
            long guessOccurrences = guess.chars()
                .filter(ch -> ch == currentLetter)
                .count();

            // Lógica para determinar estado da letra
            if (secretWordUpper.contains(String.valueOf(currentLetter)) && 
                secretOccurrences >= guessOccurrences) {
                result[i] = 1; // Letra presente, mas em posição diferente
            } else {
                result[i] = 0; // Letra não existe na palavra
            }
        }

        return result;
    }

    private WordleResultType determineResultType(int[] letterStates) {
        // Verifica se todas as letras estão corretas (vitória)
        boolean isComplete = Arrays.stream(letterStates).allMatch(state -> state == 2);
        
        if (isComplete) {
            return WordleResultType.WIN;
        }

        // Verifica se esgotou todas as tentativas (derrota)
        if (currentTries == maxTries) {
            return WordleResultType.LOSE;
        }

        // Jogo continua
        return WordleResultType.NONE;
    }

    // Getters para informações do jogo
    public String getSecretWord() {
        return secretWord;
    }

    public int getCurrentTries() {
        return currentTries;
    }

    public int getMaxTries() {
        return maxTries;
    }
}