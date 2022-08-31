package battleship;

import java.util.Scanner;

public class Game {
    private final String p1;
    private final String p2;
    private final Board b1;
    private final Board b2;
    private final Scanner scanner;
    private boolean over = false;


    public Game(String player1, String player2, Scanner scanner) {
        p1 = player1;
        p2 = player2;
        b1 = new Board(p1);
        b2 = new Board(p2);
        this.scanner = scanner;
    }
    public boolean isOver() {return over;}
    public void setOver(Boolean over) {
        this.over = over;
    }
    public boolean p1Over() {
        return b1.gameOver();
    }
    public boolean p2Over() {
        return b2.gameOver();
    }

    public void turn(String p) {
        Board board;
        if (p == p1) {
            board = b1;
        } else {
            board = b2;
        }
        askShot(p);
    }
    
    private void askShot(String p) {
        Board enemy = b2;
        Board ours = b1;

        if (p == p2) {
            enemy = b1;
            ours = b2;
        }
        boolean validShot = false;
        System.out.println();
        System.out.println(enemy.warFogMap());
        String str = "-";
        System.out.println(str.repeat(Board.getRow() * 2 + 1));
        System.out.println(ours);

        while (!validShot) {
            System.out.println();
            System.out.printf("%s, it's your turn:\n", p);
            System.out.println();

            String shot = scanner.next();
            Position target = Position.toPoint(shot);

            try {
                validShot = Board.checkValidShot(target);
                State place = enemy.positionState(target);
                switch (place) {
                    case FOG: {
                        enemy.field[target.getRow()][target.getCOL()] = State.MISS;
                        System.out.println();
                        System.out.println(enemy.warFogMap()); // show fog of war with M: missed place
                        System.out.println();
                        System.out.println("You missed!");
                        System.out.println("Press Enter and pass the move to another player");
                        try {
                            System.in.read();
                        } catch (Exception e) {
                        }
                        break;
                    }
                    case SHIP: {
                        enemy.field[target.getRow()][target.getCOL()] = State.HIT;
                        System.out.println();
                        System.out.println(enemy.warFogMap()); // show fog of war with X: hit!
                        System.out.println();
                        if (enemy.ifAllShipSunk()) {
                            System.out.println("You sank the last ship. You won. Congratulations!");
                            over = true;
                            break;
                        }
                        if (enemy.ifShipSunk()) {
                            System.out.println("You sank a ship!");
                        } else {
                            System.out.println("You hit a ship!");
                        }

                        System.out.println("Press Enter and pass the move to another player");
                        try {
                            System.in.read();
                        } catch (Exception e) {
                        }
                        break;
                    }
                }
            } catch (wrongLocationException e) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
            }
        }
    }

    public void placeShips(String p) {
        System.out.printf("%s, place your ships on the game field\n", p);
        Board b = null;
        if (p == p1) {
            b = b1;
        }
        if (p == p2) {
            b = b2;
        }
        System.out.println(b);
        b.askSetShip(5, "Aircraft Carrier", scanner);
        b.askSetShip(4, "Battleship", scanner);
        b.askSetShip(3, "Submarine", scanner);
        b.askSetShip(3, "Cruiser", scanner);
        b.askSetShip(2, "Destroyer", scanner);
        System.out.println();
    }
}
