package one.kose.sequenceset;

@SuppressWarnings("serial")
public class SequenceSetException extends Exception {
    private final int position;
    private final char character;

    public SequenceSetException(int position, char character) {
        super("invalid character in sequence set");

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
