import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Matrix {

    private final List<List<Integer>> values;

    Matrix(List<List<Integer>> values) {
        this.values = Collections.unmodifiableList(values);
    }

    Set<MatrixCoordinate> getSaddlePoints() {
        Set<MatrixCoordinate> saddlePoints = new HashSet<>();
        for (int y = 0; y < values.size(); y++) {
            List<Integer> row = values.get(y);
            int highestValueInRow = row.stream().max(Integer::compare).orElseThrow();
            for (int x = 0; x < row.size(); x++) {
                var candidate = row.get(x);
                if (candidate == highestValueInRow && isLowestVertical(x, candidate))
                        saddlePoints.add(new MatrixCoordinate(y + 1, x + 1));
            }
        }
        return saddlePoints;
    }

    private boolean isLowestVertical(int col, int candidate) {
        return values.stream()
                .mapToInt(row -> row.get(col))
                .allMatch(value -> value >= candidate);
    }

}
