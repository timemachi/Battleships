package battleship;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        // Write your code here

        Scanner scanner = new Scanner(System.in);

        Game game = new Game("Player1", "Player2", scanner);

        game.placeShips("Player1");
        System.out.println("Press Enter and pass the move to another player");
        try{
            System.in.read();
        } catch (Exception e) {
        }
        game.placeShips("Player2");
        System.out.println("Press Enter and pass the move to another player");
        try{
            System.in.read();
        } catch (Exception e) {
        }

        while (!game.isOver()) {
            game.turn("Player1");
            game.setOver(game.p1Over());

            game.turn("Player2");
            game.setOver(game.p2Over());

        }

    }
}
