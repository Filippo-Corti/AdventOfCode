import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day13bis {

    public static boolean isPalindrome(String s) {
        for (int i = 0; i < s.length() / 2; i++) {
            if (s.charAt(i) != s.charAt(s.length() - 1 - i))
                return false;
        }
        return true;
    }

    static class Pattern {
        ArrayList<String> rows;
        ArrayList<String> cols;

        public Pattern(ArrayList<String> rows, ArrayList<String> cols) {
            this.rows = new ArrayList<>(rows);
            this.cols = new ArrayList<>(cols);
        }

        @Override
        public String toString() {
            return "Pattern [rows=" + rows + ", cols=" + cols + "]";
        }

        public int getLineOfReflectionWithSmudge() {
            int oldLoR = getLineOfReflection(rows, cols, -1);
            for (int i = 0; i < rows.size(); i++) {
                String currentRow = rows.get(i);
                for (int j = 0; j < currentRow.length(); j++) {
                    String currentCol = cols.get(j);
                    ArrayList<String> copyRows = new ArrayList<>(rows);
                    ArrayList<String> copyCols = new ArrayList<>(cols);
                    // Modifico carattere in righe e colonne
                    copyRows.set(i, currentRow.substring(0, j) + ((currentRow.charAt(j) == '.') ? '#' : '.')
                            + currentRow.substring(j + 1));
                    copyCols.set(j, currentCol.substring(0, i) + ((currentCol.charAt(i) == '.') ? '#' : '.')
                            + currentCol.substring(i + 1));
                    // Testo la nuova lineOfReflection

                    int newLoR = getLineOfReflection(copyRows, copyCols, oldLoR);
                    if (newLoR != -1) {
                        return newLoR;
                    }
                }
            }
            return -1;
        }

        public int getLineOfReflection(ArrayList<String> newRows, ArrayList<String> newCols, int toIgnore) {
            // Provo tra le colonne
            String firstRow = newRows.get(0);
            for (int i = 1; i <= firstRow.length() - 1; i++) {
                String before = firstRow.substring(0, i);
                String after = firstRow.substring(i);
                int min = (before.length() < after.length()) ? before.length() : after.length();
                if (isPalindrome(firstRow.substring(i - min, i + min))) {
                    if (checkIfCorrect(newRows, i, min) && i != toIgnore) {
                        return i;
                    }
                }
            }
            String firstCol = newCols.get(0);
            for (int i = 1; i <= firstCol.length() - 1; i++) {
                String before = firstCol.substring(0, i);
                String after = firstCol.substring(i);
                int min = (before.length() < after.length()) ? before.length() : after.length();
                if (isPalindrome(firstCol.substring(i - min, i + min))) {
                    if (checkIfCorrect(newCols, i, min) && 100 * i != toIgnore) {
                        return 100 * i;
                    }
                }
            }
            return -1;
        }

        private boolean checkIfCorrect(ArrayList<String> rowsOrCols, int i, int min) {
            for (int j = 1; j < rowsOrCols.size(); j++) {
                if (!isPalindrome(rowsOrCols.get(j).substring(i - min, i + min)))
                    return false;
            }
            return true;
        }

    }

    public static ArrayList<Pattern> parseInput() {

        File f = new File("input.txt");
        Scanner s = null;
        try {
            s = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        ArrayList<Pattern> patterns = new ArrayList<>();
        ArrayList<String> rows = new ArrayList<>();
        ArrayList<String> cols = new ArrayList<>();

        while (s.hasNextLine()) {

            String in = s.nextLine();

            if (in.equals("")) {
                patterns.add(new Pattern(rows, cols));
                rows.clear();
                cols.clear();
                continue;
            }

            rows.add(in);
            for (int i = 0; i < in.length(); i++) {
                if (cols.size() < i + 1) {
                    cols.add("");
                }
                cols.set(i, cols.get(i) + in.charAt(i));
            }

        }

        patterns.add(new Pattern(rows, cols));
        return patterns;
    }

    public static void main(String[] args) {
        ArrayList<Pattern> input = parseInput();
        System.out.println(input.stream().mapToInt((p) -> p.getLineOfReflectionWithSmudge()).sum());
    }

}
