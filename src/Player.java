import java.util.ArrayList;
import java.util.Scanner;

abstract class Player {

  protected static Scanner sc = new Scanner(System.in);
  protected Hand playerHand = new Hand();
  protected ArrayList<Card> lastCards = new ArrayList<>();
  protected String playerName;
  protected boolean myTurn;
  protected boolean outOfGame;

  abstract Card requestCard();

  abstract Player getTargetPlayer(ArrayList<Player> players);

  protected void addPreviousCard(Card askedFor) {
    lastCards.add(askedFor);
  }

  public Player(String name) {
    this.myTurn = false;
    this.playerName = name;
    this.outOfGame = false;
  }

  protected boolean hasEmptyHand() {
    return this.playerHand.getHandCardCount() == 0;
  }

  // removes books from players hand automatically and increments numBooks
  protected void putDownBooks() {
    ArrayList<Card> cardsToRemove = new ArrayList<>();
    Card removeCard = this.playerHand.getFirstBook();

    if (removeCard != null) {
      for (Card card : this.playerHand.getPlayerCards().getAllCards()) {
        if (card.getRank() == removeCard.getRank()) {
          cardsToRemove.add(card);
        }
      }
      this.playerHand.incrementNumBooks();
      String cards = cardsToRemove.toString();

      System.out.println("Putting down " + cards.substring(1, cards.length()-1));

      this.playerHand.getPlayerCards().getAllCards().removeAll(cardsToRemove);
      this.playerHand.getPlayerCards().decrementCardCount(4);
    }

    System.out.println(
        "Number of books for " + getPlayerName() + ": " + this.playerHand.getNumBooks());
  }

  protected String getPlayerName() {
    return this.playerName;
  }

  // removes card from targeted players hand and adds it to asking player if they both have the card
  // will remove all cards with asked for rank
  protected void giveCardTo(Player receiving, Card card) {
    for (int i = 0; i < this.playerHand.getHandCardCount(); i++) {
      Card cardAtPosInHand = this.playerHand.getPlayerCards().getCardAtIndex(i);

      if (cardAtPosInHand.getRank() == card.getRank()) {
        System.out.println("RECEIVING THIS CARD " + cardAtPosInHand);

        this.playerHand.removeCardFromHand(cardAtPosInHand);
        receiving.playerHand.addCardToHand(cardAtPosInHand);
      }
    }
  }

  protected boolean outOfGame() {
    return this.outOfGame;
  }

  protected void setOutOfGame() {
    this.outOfGame = true;
  }

  protected boolean isMyTurn() {
    return this.myTurn;
  }

  protected void setMyTurn(boolean turn) {
    this.myTurn = turn;
  }
}
