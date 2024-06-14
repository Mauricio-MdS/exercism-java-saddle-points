import java.util.*;
import java.util.stream.Collectors;

class Matrix {

    private record Coordinate(int row, int col) {

        public MatrixCoordinate toMatrixCoordinate() {
                return new MatrixCoordinate(row, col);
            }
        }

    private final List<List<Integer>> matrix;

    Matrix(List<List<Integer>> values) {
        this.matrix = values;
    }

    Set<MatrixCoordinate> getSaddlePoints() {
        if (matrix.isEmpty()) return Collections.emptySet();

        var rowMaxes = generateRowMaxes();
        var colMins = generateColMins();

        List<Coordinate> saddlePoints = new ArrayList<>();
        for (int r = 0; r < rowMaxes.length; r++) {
            final int row = r + 1;
            for (int c = 0; c < colMins.length; c++) {
                final int col = c + 1;
                int value = matrix.get(r).get(c);
                if (value > rowMaxes[r]) {
                    rowMaxes[r] = value;
                    saddlePoints.removeIf(p -> p.row() == row);
                }
                if (value < colMins[c]) {
                    colMins[c] = value;
                    saddlePoints.removeIf(p -> p.col() == col);
                }
                if (value == rowMaxes[r] && value == colMins[c])
                    saddlePoints.add(new Coordinate(row, col));
            }
        }

        return saddlePoints
                .parallelStream()
                .map(Coordinate::toMatrixCoordinate)
                .collect(Collectors.toSet());
    }

    private int[] generateColMins() {
        int[] colMins = new int[matrix.get(0).size()];
        Arrays.fill(colMins, Integer.MAX_VALUE);
        return colMins;
    }

    private int[] generateRowMaxes() {
        int[] rowMaxes = new int[matrix.size()];
        Arrays.fill(rowMaxes, Integer.MIN_VALUE);
        return rowMaxes;
    }
}