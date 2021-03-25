import java.util.ArrayList;
import java.util.Random;

public class CPUPlayer extends Player {

  private final Random rand = new Random();

  public CPUPlayer(String name) {
    super(name);
  }

  // gets target based off cpu position among players and randomness - favoring choosing previous
  // player
  @Override
  Player getTargetPlayer(ArrayList<Player> players) {
    int randPlayer = rand.nextInt(players.size());
    int randNum = rand.nextInt(10) + 1;

    for (int i = 0; i < players.size(); i++) {
      if (this.getPlayerName().equals(players.get(i).getPlayerName())) {
        // if cpu is first player then it will target last player
        if (i == 0) {
          return players.get(players.size() - 1);
          // last player asks previous, if randNum = player then ask previous - can't ask themselves
        } else if (randNum < 7 || i == players.size() - 1 || randNum == i) {
          return players.get(i - 1);
          // 40% will ask a random player
        } else {

          return players.get(randPlayer);
        }
      }
    }
    return null;
  }

  // if cpu goes first then it will ask for a random card
  // if not then it will prefer to ask for the last card that was asked for (70%)
  // 30% it will ask for a random card
  @Override
  Card requestCard() {
    int randNum = rand.nextInt(10) + 1; // random number 1 - 10

    if (lastCards.size() == 0) {
      return this.playerHand.getPlayerCards().getRandomCard();
    } else if (randNum < 8) {
      if (this.playerHand.hasRankInHand(lastCards.get(lastCards.size() - 1))) {
        return lastCards.get(lastCards.size() - 1);
      }
    }
    return this.playerHand.getPlayerCards().getRandomCard();
  }
}
