import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Day17 {

    public static class Pos {
        int row, col;

        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + row;
            result = prime * result + col;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pos other = (Pos) obj;
            if (row != other.row)
                return false;
            if (col != other.col)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Pos [row=" + row + ", col=" + col + "]";
        }

        

    }

    public static class Direction {
        int deltaRow, deltaCol;

        public Direction(int deltaRow, int deltaCol) {
            this.deltaRow = deltaRow;
            this.deltaCol = deltaCol;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + deltaRow;
            result = prime * result + deltaCol;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Direction other = (Direction) obj;
            if (deltaRow != other.deltaRow)
                return false;
            if (deltaCol != other.deltaCol)
                return false;
            return true;
        }

        public Direction invert() {
            return new Direction(-this.deltaRow, -this.deltaCol);
        }

        @Override
        public String toString() {
            return "Direction [deltaRow=" + deltaRow + ", deltaCol=" + deltaCol + "]";
        }

        

    }

    public static class Tuple implements Comparable<Tuple> {

        Pos position;
        Direction direction;
        int straightLineCount;
        long distanceFromSource;

        public Tuple(int row, int col, int directionRow, int directionCol, int straightLineCount,
                long distanceFromSource) {
            this.position = new Pos(row, col);
            this.direction = new Direction(directionRow, directionCol);
            this.straightLineCount = straightLineCount;
            this.distanceFromSource = distanceFromSource;
        }

        @Override
        public int compareTo(Tuple o) {
            return Long.compare(distanceFromSource, o.distanceFromSource);
        }

        @Override
        public String toString() {
            return "Tuple [position=" + position + ", direction=" + direction + ", straightLineCount="
                    + straightLineCount + "]";
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, direction, straightLineCount);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Tuple other = (Tuple) obj;
            if (position == null) {
                if (other.position != null)
                    return false;
            } else if (!position.equals(other.position))
                return false;
            if (direction == null) {
                if (other.direction != null)
                    return false;
            } else if (!direction.equals(other.direction))
                return false;
            if (straightLineCount != other.straightLineCount)
                return false;
            return true;
        }

    }

    public static ArrayList<ArrayList<Integer>> parseInput() {

        File f = new File("input.txt");
        Scanner s = null;
        try {
            s = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();

        while (s.hasNextLine()) {
            ArrayList<Integer> row = new ArrayList<>();
            String in = s.nextLine();
            for (int i = 0; i < in.length(); i++) {
                row.add(Integer.parseInt(in.charAt(i) + ""));
            }
            matrix.add(row);
        }

        return matrix;

    }

    public static void main(String[] args) {

        ArrayList<ArrayList<Integer>> map = parseInput();

        long res = shortestPathToEnd(map);

        System.out.println(res);

    }

    private static long shortestPathToEnd(ArrayList<ArrayList<Integer>> map) {

        ArrayList<Direction> directions = new ArrayList<>(Arrays.asList(
                new Direction(0, 0),
                new Direction(0, 1),
                new Direction(0, -1),
                new Direction(1, 0),
                new Direction(-1, 0)));

        PriorityQueue<Tuple> pq = new PriorityQueue<>();
        ArrayList<String> visited = new ArrayList<>();

        Tuple t = new Tuple(0, 0, 0, 0, 0, 0);
        pq.add(t);

        while (!pq.isEmpty()) {

            Tuple curr = pq.remove();
            System.out.println(curr.distanceFromSource);

            if (curr.position.row == map.size() - 1 && curr.position.col == map.get(0).size() - 1)
                return curr.distanceFromSource;

            if (visited.contains(curr.toString()))
                continue;

            visited.add(curr.toString());

            // Try same direction (if you can)
            if (curr.straightLineCount < 3 && !curr.direction.equals(directions.get(0))) {
                int newRow = curr.position.row + curr.direction.deltaRow;
                int newCol = curr.position.col + curr.direction.deltaCol;
                if (newRow >= 0 && newRow < map.size() && newCol >= 0 && newCol < map.get(0).size()) {
                    pq.add(new Tuple(newRow, newCol, curr.direction.deltaRow,
                            curr.direction.deltaCol, curr.straightLineCount + 1,
                            curr.distanceFromSource + map.get(newRow).get(newCol)));
                }
            }

            // Try all other direction
            for (int i = 1; i < directions.size(); i++) {
                Direction d = directions.get(i);
                if (!curr.direction.equals(d) && !curr.direction.equals(d.invert())) {
                    int newRow = curr.position.row + d.deltaRow;
                    int newCol = curr.position.col + d.deltaCol;
                    if (newRow >= 0 && newRow < map.size() && newCol >= 0 && newCol < map.get(0).size()) {
                        pq.add(new Tuple(newRow, newCol, d.deltaRow, d.deltaCol, 1,
                                curr.distanceFromSource + map.get(newRow).get(newCol)));
                    }
                }
            }
        }

        return -1;

    }

}