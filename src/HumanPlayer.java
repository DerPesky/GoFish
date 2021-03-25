import java.util.ArrayList;

public class HumanPlayer extends Player {

  public HumanPlayer(String name) {
    super(name);
  }

  // makes sure targeted player is not themselves or an invalid name - not case sensitive
  @Override
  Player getTargetPlayer(ArrayList<Player> players) {
    String targetPlayer;
    Player target = null;
    boolean notMatching = true;

    do {
      System.out.println("Enter the name of the player you want to target: ");
      targetPlayer = sc.nextLine();
      for (Player player : players) {
        // checks if input matches any active players
        if (targetPlayer.equalsIgnoreCase(player.getPlayerName())) {
          target = player;
          notMatching = false;
          break;
        } else {
          notMatching = true;
        }
      }
      // runs while they entered their own name or entered a name that didn't match any players
    } while (targetPlayer.equalsIgnoreCase(this.getPlayerName()) || notMatching);
    return target;
  }

  // gets a card/rank to request - makes sure it is in players hand and target players hand
  @Override
  Card requestCard() {
    boolean properInput = false;

    do {
      System.out.println("Please enter a card's rank to request");
      String input = sc.nextLine();
      for (Card.Rank rank : Card.Rank.values()) {
        // checks if ranks match a rank in enum - not case sensitive
        if (rank.toString().equalsIgnoreCase(input)) {
          properInput = true;
          // iterates through players hand
          for (Card card : this.playerHand.getPlayerCards().getAllCards()) {
            Card.Rank cardRank = card.getRank();
            // if it is present than it is a valid request
            if (cardRank.toString().equalsIgnoreCase(input)) {
              return card;
            }
          }
          // for invalid request - prints error message
        } else if (!rank.toString().equalsIgnoreCase(input) && rank.toString().equals("KING")) {
          System.out.println("Please enter a valid rank");
          properInput = false;
        }
      }
    } while (!properInput);
    return null;
  }
}
