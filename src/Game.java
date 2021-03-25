import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Game {

  private int numTurns; // not needed but used to see how long cpu only games last
  private final Deck gameDeck;
  private final Scanner sc;
  private final ArrayList<Player> players;
  private final ArrayList<Player> removedPlayers;

  public Game() {
    this.numTurns = 0;
    this.gameDeck = new Deck();
    this.players = new ArrayList<>();
    this.removedPlayers = new ArrayList<>();
    this.sc = new Scanner(System.in);
  }

  // The main menu before starting the game - enables the user to manually start/exit the game or to
  // look at the instructions of this implementation of Go Fish.
  public void mainMenu() {
    int input;
    goFishLogo();

    do {
      System.out.println("Press 1 to play, 2 for the rules, or another number to quit");
      input = getIntInput();

      if (input == 1) {
        playGame();
      } else if (input == 2) {
        printRules();
      }
      // allows user to start/exit while they haven't started/exited the game already
    } while (input == 2);
  }

  // Method includes all necessary instructions for the game to end properly
  private void playGame() {
    gameDeck.shuffle();
    addPlayers();

    // drawing 5 cards for <2 players and 7 otherwise
    if (players.size() == 3) {
      dealCardsToAllPlayers(7);
    } else if (players.size() > 3) {
      dealCardsToAllPlayers(5);
    } else if (players.size() == 0) {
      System.exit(0);
    }

    // first player added gets first turn
    players.get(0).setMyTurn(true);
    while (!isGameOver()) {
      // prints fish character as border
      System.out.println("\uD83D\uDC1F ".repeat(30));
      // gets all necessary user actions to complete a turn for the player with myTurn() = true;
      completeTurn((Objects.requireNonNull(seeWhichPlayersTurn())));
      numTurns++;
    }

    // combines two lists to get to all players
    players.addAll(removedPlayers);
    Player winner = findWinner();

    System.out.println("\nThe total number of turns taken was: " + numTurns);
    System.out.println(
        "The winner is: "
            + winner.getPlayerName()
            + " with a total of "
            + winner.playerHand.getNumBooks()
            + " Books");
  }

  // Method completes all actions for one turn
  private void completeTurn(Player playing) {
    playing.putDownBooks();

    // if user has an empty hand then will follow actions for that condition - will exit if true
    doIfEmptyHand(playing);
    if (playing.outOfGame()) {
      return;
    }

    // prints hand and possible target information to player
    System.out.println(
        "Your Hand: "
            + playing.playerHand.getHand()
            + "\nPlayers you can target: "
            + getPossibleTargetNames(playing));

    Player targetPlayer = playing.getTargetPlayer(players);
    Card askedFor = playing.requestCard();
    System.out.println(
        playing.getPlayerName() + " asked for " + askedFor + " to " + targetPlayer.getPlayerName());

    // completes the fishing for a player
    doFishing(targetPlayer, playing, askedFor);
  }

  private void doFishing(Player targetPlayer, Player playing, Card askedFor) {
    // checks whether or not player can continue or will end turn
    if (targetPlayer.playerHand.hasRankInHand(askedFor)) {
      targetPlayer.giveCardTo(playing, askedFor);
    } else {
      playing.addPreviousCard(askedFor); // used for CPU Player to "remember" previous card
      System.out.println("Go Fish!");
      drawCards(playing, 1);
      playing.setMyTurn(false);
      players.get(getNextPlayerPosition(playing)).setMyTurn(true);
    }
  }

  private void doIfEmptyHand(Player playing) {
    if (playing.hasEmptyHand()) {
      // checks if deck has 0 cards given that player has 0 cards
      if (gameDeck.getCardCount() == 0) {
        System.out.println("You have 0 cards and the deck is empty! You are out of the game!");
        // sets next players turn to be true
        players.get(getNextPlayerPosition(playing)).setMyTurn(true);
        // adds player to removedPlayers arraylist
        playing.setOutOfGame();
        removedPlayers.add(playing);
        // removes player from active players
        players.remove(playing);
      }
      // if they have cards or the deck has cards then they can draw 3 cards
      drawCards(playing, 3);
    }
  }

  // sets next Active Player myTurn to true. Last player >> 1st Player >> 2nd Player etc.
  private int getNextPlayerPosition(Player player) {
    int currentPosition = findPlayerIndex(player);

    // last players "next player" is the first player - circular turn order
    if (currentPosition == players.size() - 1) {
      return 0;
    } else if (currentPosition != -1) {
      return currentPosition + 1;
    }
    return -1;
  }

  // returns the player with the highest number of books
  private Player findWinner() {
    Player hasMostBooks = players.get(0);
    int mostBooks = hasMostBooks.playerHand.getNumBooks();

    // iterates through players, if there books is > mostBooks, that becomes mostBooks and so on
    for (Player player : players) {
      int numBooks = player.playerHand.getNumBooks();
      if (numBooks > mostBooks) {
        hasMostBooks = player;
        mostBooks = numBooks;
      }
    }
    return hasMostBooks;
  }

  // returns index of given player
  private int findPlayerIndex(Player player) {
    for (int i = 0; i < players.size(); i++) {
      if (players.get(i) == player) {
        return i;
      }
    }
    return -1;
  }

  // returns a string of all possible players that can be target by whoever is playing
  private String getPossibleTargetNames(Player playing) {
    StringBuilder sb = new StringBuilder();

    for (Player player : players) {
      // only appends name if it isn't the current players'
      if (!player.equals(playing)) {
        sb.append(player.getPlayerName());
      }
    }
    return sb.toString();
  }

  // checks each players myTurn(), return the player with value of true
  private Player seeWhichPlayersTurn() {
    for (Player player : players) {
      if (player.isMyTurn()) {
        System.out.println(player.getPlayerName() + "'s turn.");
        return player;
      }
    }
    return null;
  }

  // if game deck has cards then gives (given N) cards to player from game deck
  private void drawCards(Player player, int numCards) {
    if (gameDeck.getCardCount() == 0) {
      System.out.println("There are no more cards in the deck!");
      return;
    }
    for (int i = 0; i < numCards; i++) {
      Card randCard = gameDeck.getRandomCard();
      player.playerHand.addCardToHand(randCard);
      gameDeck.removeCard(randCard);
    }
  }

  // gives all players cards - instead of individually
  private void dealCardsToAllPlayers(int numCards) {
    for (Player player : players) {
      drawCards(player, numCards);
    }
  }

  // checks if either only one player is left or if 13 books have been collected
  private boolean isGameOver() {
    int numBooksCollected = 0;
    for (Player player : players) {
      numBooksCollected += player.playerHand.getNumBooks();
    }
    return numBooksCollected == 13 || players.size() == 1;
  }

  // allows user to either add CPU or Human players, player limit: 2 <= players <= 5
  private void addPlayers() {
    int cpuCount = 0; // tally for cpu players
    int humanCount = 0; // tally for cpu players
    int input;

    do {
      if (humanCount + cpuCount > 5) {
        System.out.println("You've reached the max amount of players!");
        break;
      }
      System.out.println("Enter 1 for a human and 2 for a CPU, -1 to quit");
      input = getIntInput();
      sc.nextLine();
      String name;
      // for human players - user can enter a name, cpu name is CPU #
      if (input == 1) {
        humanCount++;
        System.out.println("Please enter this players name");
        name = sc.nextLine();
        players.add(new HumanPlayer(name));
        System.out.println("Added " + name + " to the game");
      } else if (input == 2) {
        cpuCount++;
        name = "CPU " + cpuCount;
        players.add(new CPUPlayer(name));
        System.out.println("Added " + name + " to the game");
      }
      // user can enter -1 to stop adding players
    } while (input != -1 || (humanCount + cpuCount) < 3);
  }

  // makes sure user enters an integer to avoid InputMismatchException
  private int getIntInput() {
    while (!sc.hasNextInt()) {
      System.out.println("Please enter a number");
      sc.next();
    }
    return sc.nextInt();
  }

  // prints rules for Go Fish
  private void printRules() {
    System.out.println(
        "One player is \"fishing\", asks for certain card (rank only, suit doesn't matter) to a certain player.\n"
            + "\"Fishing\" player must have at least one card with the same rank that they asked for. The player\n"
            + "that is asked, must hand over all the cards that were asked for. If they don't have any then they\n"
            + "say \"go fish\", the person who asked must draw a card from the top of the deck and adds that card to\n"
            + "their hand. If the asked player has the requested card then the player gets to continue asking,\n"
            + "they can ask for the same or different card. As long as they keep on receiving card \"catch\" they can\n"
            + "continue. A player is eliminated if they don't have any cards and the deck is empty. If they don't have\n"
            + "any cards and they can draw a card from the deck then they are still in the game, but lose that turn.\n"
            + "Once you get a book (4 cards of the same rank), the cards are taken out of your hand and tallied.\n"
            + "Whoever gets the most books wins.\n"
            + "3 players - 7 cards each\n"
            + "4+ Player - 5 cards each");
  }

  // prints go fish logo ascii art - used tutorial from https://www.baeldung.com/ascii-art-in-java
  private void goFishLogo() {
    int height = 30;
    int width = 250;

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics graph = image.getGraphics();
    graph.setFont(new Font("Calibri", Font.PLAIN, 16));

    Graphics2D graphics = (Graphics2D) graph;
    graphics.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    graphics.drawString("Go Fish", 12, 10);

    for (int y = 0; y < height; y++) {
      StringBuilder sb = new StringBuilder();
      for (int x = 0; x < width; x++) {
        sb.append(image.getRGB(x, y) == -16777216 ? " " : "0");
      }
      if (sb.toString().trim().isEmpty()) {
        continue;
      }
      System.out.println(sb);
    }
    // prints fish logo
    System.out.println("\uD83D\uDC1F  ".repeat(20));
  }
}
