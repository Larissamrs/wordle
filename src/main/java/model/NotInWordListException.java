package model;

public class NotInWordListException extends Exception {
    public NotInWordListException() {
        super("A palavra não está na lista de palavras válidas.");
    }

    public NotInWordListException(String message) {
        super(message);
    }
}