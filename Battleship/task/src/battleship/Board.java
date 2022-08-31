package battleship;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Board {
    public final State[][] field;
    private static final int ROW = 10;
    private static final int COL = 10;
    private String name;
    private List<Ship> ships = new LinkedList<>();


    static public int getRow() {
        return ROW;
    }

    public boolean ifAllShipSunk() {
        return ships.size() == 0;
    }
    public boolean ifShipSunk() {
        for (Ship s : ships) {
            if (s.sunk()) {
                ships.remove(s);
                return true;
            }
        }
        return false;
    }

    public class coordinate {
        int row;
        int col;
        coordinate(int a, int b) {
            row = a;
            col = b;
        }
    }

    public class Ship {
        List<coordinate> coordinates;

        Ship() {
            coordinates = new LinkedList<>();
        }
        void addPosition(coordinate p) {
            coordinates.add(p);
        }
        boolean sunk() {
            for (coordinate p: coordinates) {
                if (field[p.row][p.col] == State.SHIP) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(' ');
        for (int i = 1; i <= COL; i++) {
            sb.append(' ').append(i);
        }
        sb.append('\n');
        for (int i = 0; i < ROW; i++) {
            char ch = (char) ('A' + i);
            sb.append(ch);
            for (int j = 0; j < COL; j++) {
                State state = field[i][j];
                char position = 0;
                switch (state) {
                    case FOG -> position = '~';
                    case SHIP -> position = 'O';
                    case HIT -> position = 'X';
                    case MISS -> position = 'M';
                }
                sb.append(' ').append(position);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public Board(String name) {
        this.name = name;
        field = new State[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                field[i][j] = State.FOG;
            }
        }
    }

    /**
     * Check if game is over
     * @return game over
     */
    public boolean gameOver() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (field[i][j] == State.SHIP) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Suppose A and B and requested length are both checked by {@link #validateShip(Position, Position, int)};
     * Helper function of {@link #askSetShip(int, String, Scanner)}
     * @param A first position
     * @param B second position
     */
    private void setShip(Position A, Position B) {
        if (A.getCOL() == B.getCOL()) {
            int maxRow = Math.max(A.getRow(), B.getRow());
            int minRow = Math.min(A.getRow(), B.getRow());
            Ship ship = new Ship();
            for (int i = minRow; i <= maxRow; i++) {
                field[i][A.getCOL()] = State.SHIP;
                ship.addPosition(new coordinate(i, A.getCOL()));
            }
            ships.add(ship);
        }
        if (A.getRow() == B.getRow()) {
            int maxCol = Math.max(A.getCOL(), B.getCOL());
            int minCol = Math.min(A.getCOL(), B.getCOL());
            Ship ship = new Ship();
            for (int i = minCol; i <= maxCol; i++) {
                field[A.getRow()][i] = State.SHIP;
                ship.addPosition(new coordinate(A.getRow(), i));
            }
        }

    }
    public State positionState(Position target) {
        return field[target.getRow()][target.getCOL()];
    }

    String warFogMap() {
        StringBuilder sb = new StringBuilder();
        sb.append(' ');
        for (int i = 1; i <= COL; i++) {
            sb.append(' ').append(i);
        }
        sb.append('\n');

        for (int i = 0; i < ROW; i++) {
            char ch = (char) ('A' + i);
            sb.append(ch);
            for (int j = 0; j < COL; j++) {
                State state = field[i][j];
                char position = 0;
                switch (state) {
                    case FOG, SHIP -> position = '~';
                    case HIT -> position = 'X';
                    case MISS -> position = 'M';
                }
                sb.append(' ').append(position);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    static boolean checkValidShot(Position A) {
        if (A.getCOL() < 0 || A.getCOL() > COL - 1 || A.getRow() < 0 || A.getRow() > ROW - 1) {
            throw new wrongLocationException();
        }
        return true;
    }

    public void askSetShip(int length, String shipType, Scanner scanner) {
        boolean validShip = false;
        while (!validShip) {
            System.out.println();
            System.out.printf("Enter the coordinates of the " + shipType + " (%d cells):", length);
            System.out.println();
            System.out.println();

            String position1 = scanner.next();
            String position2 = scanner.next();
            Position a = Position.toPoint(position1);
            Position b = Position.toPoint(position2);

            try {
                validShip = validateShip(a, b, length);
                setShip(a, b);
                System.out.println();
                System.out.println(this);
            } catch (wrongLocationException e) {
                System.out.println("Error! Wrong ship location! Try again:");
            } catch (wrongLengthException e) {
                System.out.printf("Error! Wrong length of the %s! Try again:", shipType);
                System.out.println();
            } catch (tooCloseException e) {
                System.out.println("Error! You placed it too close to another one. Try again:");
            }
        }
    }

    /**
     * Helper function to check if setShip's arguments are valid
     * @param A A point
     * @param B B point
     * @param length Requested length of ship
     * @return  if arguments are valid, return true
     * @throws wrongLocationException if location of ship is wrong
     * @throws wrongLengthException if length is wrong
     * @throws tooCloseException if location of ship is too close with other ships
     *
     */
    private boolean validateShip(Position A, Position B, int length) {
        if (A.getCOL() != B.getCOL() && A.getRow() != B.getRow()) {
            throw new wrongLocationException();
        }
        if (A.getCOL() < 0 || A.getCOL() > COL - 1 || A.getRow() < 0 || A.getRow() > ROW - 1
                ||B.getCOL() < 0 || B.getCOL() > COL - 1 || B.getRow() < 0 || B.getRow() > ROW - 1) {
            throw new wrongLocationException();
        }

        if (A.getCOL() == B.getCOL()) {
            if (Math.abs(A.getRow() - B.getRow()) + 1 != length) {
                throw new wrongLengthException();
            }
            int col = A.getCOL();
            int row1 = Math.max(A.getRow(), B.getRow());
            int row2 = Math.min(A.getRow(), B.getRow());
            for (int i = row2; i <= row1; i++) {
                if (field[i][col] == State.SHIP) {
                    throw new wrongLocationException();
                }
            }
            if (row2 > 0) {
                if (field[row2 - 1][col] == State.SHIP) {
                    throw new tooCloseException();
                }
            }
            if (row1 < ROW - 1) {
                if (field[row1 + 1][col] == State.SHIP) {
                    throw new tooCloseException();
                }
            }
            if (col > 0) {
                for (int i = row2; i <= row1; i++) {
                    if (field[i][col - 1] == State.SHIP) {
                        throw new tooCloseException();
                    }
                }
            }
            if (col < COL - 1) {
                for (int i = row2; i <= row1; i++) {
                    if (field[i][col + 1] == State.SHIP) {
                        throw new tooCloseException();
                    }
                }
            }
        } else {
            if (Math.abs(A.getCOL() - B.getCOL() ) + 1!= length) {
                throw new wrongLengthException();
            }
            int row = A.getRow();
            int col1 = Math.max(A.getCOL(), B.getCOL());
            int col2 = Math.min(A.getCOL(), B.getCOL());
            for (int i = col2; i <= col1; i++) {
                if (field[row][i] == State.SHIP) {
                    throw new wrongLocationException();
                }
            }
            if (col2 > 0) {
                if (field[row][col2 - 1] == State.SHIP) {
                    throw new tooCloseException();
                }
            }
            if (col1 < COL - 1) {
                if (field[row][col1 + 1] == State.SHIP) {
                    throw new tooCloseException();
                }
            }
            if (row > 0) {
                for (int i = col2; i <= col1; i++) {
                    if (field[row - 1][i] == State.SHIP) {
                        throw new tooCloseException();
                    }
                }
            }
            if (row < ROW - 1) {
                for (int i = col2; i <= col1; i++) {
                    if (field[row + 1][i] == State.SHIP) {
                        throw new tooCloseException();
                    }
                }
            }
        }
        return true;
    }
}

class wrongLocationException extends IllegalArgumentException {
    public wrongLocationException() {
        super();
    }
}

class wrongLengthException extends IllegalArgumentException {
    public wrongLengthException() {
        super();
    }
}

class tooCloseException extends IllegalArgumentException {
    public tooCloseException() {
        super();
    }
}

enum State {
    FOG,
    SHIP,
    HIT,
    MISS
}
