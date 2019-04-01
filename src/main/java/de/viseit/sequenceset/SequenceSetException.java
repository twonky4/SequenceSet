package de.viseit.sequenceset;

public class SequenceSetException extends RuntimeException {
    private static final long serialVersionUID = 8238618101835876578L;

    private final int position;
    private final char character;

    public SequenceSetException(int position, char character) {
        super("invalid character(" + character + ") in sequence set at position " + position);

        this.position = position;
        this.character = character;
    }

    public int getPosition() {
        return position;
    }

    public char getCharacter() {
        return character;
    }
}
