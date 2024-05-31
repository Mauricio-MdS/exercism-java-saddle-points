import java.util.*;
import java.util.stream.Collectors;

class Matrix {

    private record pointsOfInterest (int value, Set<MatrixCoordinate> elements) {}

    private final List<List<Integer>> matrix;
    private final Map<Integer, pointsOfInterest> rowMax = new HashMap<>();
    private final Map<Integer, pointsOfInterest> colMin = new HashMap<>();


    Matrix(List<List<Integer>> values) {
        this.matrix = values;
    }

    Set<MatrixCoordinate> getSaddlePoints() {
        getRowMaxAndColMin();
        var maximumPoints = rowMax.values().stream().flatMap(p -> p.elements.stream()).collect(Collectors.toSet());
        var minimumPoints = colMin.values().stream().flatMap(p -> p.elements.stream()).collect(Collectors.toSet());
        return maximumPoints.stream().filter(minimumPoints::contains).collect(Collectors.toSet());
    }

    private void getRowMaxAndColMin() {
        for (int rowIndex = 0; rowIndex < matrix.size(); rowIndex++) {
            var row = matrix.get(rowIndex);
            for (int colIndex = 0; colIndex < row.size(); colIndex++) {
                int value = row.get(colIndex);
                var coordinate = new MatrixCoordinate(rowIndex + 1, colIndex + 1);
                checkRowMax(rowIndex, value, coordinate);
                checkColMin(colIndex, value, coordinate);
            }
        }
    }

    private void checkColMin(int colIndex, Integer value, MatrixCoordinate coordinate) {
        var min = colMin.getOrDefault(colIndex, new pointsOfInterest(value, new HashSet<>()));
        if (value < min.value) {
            min = new pointsOfInterest(value, new HashSet<>(List.of(coordinate)));
        } else if (value == min.value) {
            min.elements.add(coordinate);
        }
        colMin.put(colIndex, min);
    }

    private void checkRowMax(int rowIndex, Integer value, MatrixCoordinate coordinate) {
        var max = rowMax.getOrDefault(rowIndex, new pointsOfInterest(value, new HashSet<>()));
        if (value > max.value) {
            max = new pointsOfInterest(value, new HashSet<>(List.of(coordinate)));
        } else if (value == max.value) {
            max.elements.add(coordinate);
        }
        rowMax.put(rowIndex, max);
    }


}
