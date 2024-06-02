import java.util.*;
import java.util.stream.Collectors;

class Matrix {

    private record PointsOfInterest(int value, Set<MatrixCoordinate> elements) {}

    private final List<List<Integer>> matrix;
    private final Map<Integer, PointsOfInterest> rowMax = new HashMap<>();
    private final Map<Integer, PointsOfInterest> colMin = new HashMap<>();


    Matrix(List<List<Integer>> values) {
        this.matrix = values;
    }

    Set<MatrixCoordinate> getSaddlePoints() {
        computeRowMaxAndColMin();
        return getPointsOfInterest(rowMax).stream()
                .filter(getPointsOfInterest(colMin)::contains)
                .collect(Collectors.toSet());
    }

    private void computeRowMaxAndColMin() {
        for (int rowIndex = 0; rowIndex < matrix.size(); rowIndex++) {
            var row = matrix.get(rowIndex);
            for (int colIndex = 0; colIndex < row.size(); colIndex++) {
                updateColAndRow(row, colIndex, rowIndex);
            }
        }
    }

    private Set<MatrixCoordinate> getPointsOfInterest(Map<Integer, PointsOfInterest> rowMax) {
        return rowMax.values()
                .stream()
                .flatMap(p -> p.elements.stream())
                .collect(Collectors.toSet());
    }

    private void updateColAndRow(List<Integer> row, int colIndex, int rowIndex) {
        int value = row.get(colIndex);
        var coordinate = new MatrixCoordinate(rowIndex + 1, colIndex + 1);
        updateColMin(colIndex, value, coordinate);
        updateRowMax(rowIndex, value, coordinate);
    }

    private void updateColMin(int colIndex, int value, MatrixCoordinate coordinate) {
        var min = colMin.get(colIndex);
        if (min == null || value < min.value) min = new PointsOfInterest(value, new HashSet<>());
        if (value == min.value) min.elements.add(coordinate);
        colMin.put(colIndex, min);
    }

    private void updateRowMax(int rowIndex, int value, MatrixCoordinate coordinate) {
        var max = rowMax.get(rowIndex);
        if (max == null || value > max.value) max = new PointsOfInterest(value, new HashSet<>());
        if (value == max.value) max.elements.add(coordinate);
        rowMax.put(rowIndex, max);
    }




}
