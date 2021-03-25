// class for specifically handling players hand directly instead of manipulating it through deck
public class Hand {
  private final Deck playerCards;
  private int numOfBooks;

  public Hand() {
    this.numOfBooks = 0;
    this.playerCards = new Deck(0);
  }

  public int getHandCardCount() {
    return this.playerCards.getCardCount();
  }

  public int getNumBooks() {
    return this.numOfBooks;
  }

  public void incrementNumBooks() {
    this.numOfBooks++;
  }

  public void addCardToHand(Card card) {
    this.playerCards.addCard(card);
  }

  public void removeCardFromHand(Card card) {
    this.playerCards.removeCard(card);
  }

  public Deck getPlayerCards() {
    return this.playerCards;
  }

  // returns string representation of all cards in hand
  public String getHand() {
    StringBuilder sb = new StringBuilder();

    for (Card card : this.playerCards.getAllCards()) {
      sb.append(card.toString()).append(" ");
    }
    return sb.toString();
  }

  // returns first card that occurs 4 times, user never has two books at a time, so it is ok to only
  // check for one book in the player's hand
  public Card getFirstBook() {
    for (Card a : this.getPlayerCards().getAllCards()) {
      int num = 0;
      for (Card b : this.getPlayerCards().getAllCards()) {
        if (a.getRank() == b.getRank()) {
          num++;
        }
      }
      if (num == 4) {
        return a;
      }
    }
    return null;
  }

  // verifies whether a certain player has a card
  public boolean hasRankInHand(Card cardRank) {
    if (this.playerCards.getCardCount() == 0) {
      return false;
    }
    for (Card card : this.playerCards.getAllCards()) {
      if (card.getRank() == cardRank.getRank()) {
        return true;
      }
    }
    return false;
  }
}
