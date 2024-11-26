package model;

import java.util.Arrays;

public class MoveResult {
    private WordleResultType resultType;
    private String secretWord;
    private int[] letterStates;

    public MoveResult(WordleResultType resultType, String secretWord, int[] letterStates) {
        this.resultType = resultType;
        this.secretWord = secretWord;
        this.letterStates = letterStates;
    }

    // Getters
    public WordleResultType getResultType() {
        return resultType;
    }

    public String getSecretWord() {
        return secretWord;
    }

    public int[] getLetterStates() {
        return letterStates;
    }

    // Método toString para facilitar depuração
    @Override
    public String toString() {
        return "MoveResult{" +
                "resultType=" + resultType +
                ", secretWord='" + secretWord + '\'' +
                ", letterStates=" + Arrays.toString(letterStates) +
                '}';
    }
}