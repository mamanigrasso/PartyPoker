package at.aau.pokerfox.partypoker;

public class Card {
    int color;
    int number;

    public Card(int color, int number) {
        this.color = color;
        this.number = number;
    }

    public String toString() {
        return "*** CARD " + color + " -> " + number + " ***";
    }
}
