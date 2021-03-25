public class Card {
  // enum used as rank and suit constants instead of having separate classes for each, also easily
  // allows the use of words for ranks without making separate variables for each one
  public enum Rank {
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING
  }

  public enum Suit {
    CLUBS,
    DIAMONDS,
    HEARTS,
    SPADES
  }

  private final Rank rank;
  private final Suit suit;

  public Card(Rank rank, Suit suit) {
    this.rank = rank;
    this.suit = suit;
  }

  public Rank getRank() {
    return this.rank;
  }

  public Suit getSuit() {
    return this.suit;
  }

  @Override
  public String toString() {
    return "[" + rank + ":" + suit + "]";
  }
}
