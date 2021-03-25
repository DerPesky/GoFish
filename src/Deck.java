import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Deck {

  private final ArrayList<Card> cards;
  private final Random random = new Random();
  private int cardCount;

  // generates standard deck of cards
  public Deck() {
    this(0);
    fillStandardDeck();
  }

  public Deck(int card) {
    this.cardCount = card;
    this.cards = new ArrayList<>();
  }

  public int getCardCount() {
    return this.cardCount;
  }

  public Card getCardAtIndex(int index) {
    return this.cards.get(index);
  }

  // returns random card in objects own cards list
  public Card getRandomCard() {
    int randNum = random.nextInt(this.cardCount);
    return this.cards.get(randNum);
  }

  public ArrayList<Card> getAllCards() {
    return this.cards;
  }

  // fills deck with standard suits and values
  public void fillStandardDeck() {
    for (Card.Suit suit : Card.Suit.values()) {
      for (Card.Rank rank : Card.Rank.values()) {
        addCard(new Card(rank, suit));
      }
    }
  }

  public void removeCard(Card card) {
    this.cardCount--;
    this.cards.remove(card);
  }

  public void decrementCardCount(int n) {
    this.cardCount -= n;
  }

  // shuffle deck - used Fisher - Yates in last program
  public void shuffle() {
    Collections.shuffle(cards);
  }

  public void addCard(Card addedCard) {
    this.cardCount++;
    this.cards.add(addedCard);
  }
}
