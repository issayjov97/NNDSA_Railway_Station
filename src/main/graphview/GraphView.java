package main.graphview;

import main.graph.DiGraph;
import main.graph.GraphADT;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import main.service.DijkstraSPAlgorithm;
import main.service.GraphService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GraphView {
    private Map<String, StackPane> vertices;
    private DijkstraSPAlgorithm dijkstraSPAlgorithm;
    private GraphADT<String, Double> graph;
    private Map <DiGraph<String, Double>.Vertex, DiGraph<String, Double>.Vertex> shortestPath;

    @FXML
    private Pane pane;

    @FXML
    void initialize() {
        this.vertices = new HashMap<>();
        importGraphADT();
        this.dijkstraSPAlgorithm = new DijkstraSPAlgorithm(graph);
        importGraphVerticesCoordinates();
        setEdgesView();
        setVerticesView();
    }

    @FXML
    void onAddNodeClicked(ActionEvent event) {
        final Dialog addNodeDialog = new Dialog();
        addNodeDialog.setTitle("Add Node");
        addNodeDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField vertexNameField = new TextField();
        vertexNameField.setPromptText("Vertex name");
        TextField xValueField = new TextField();
        xValueField.setPromptText("X");
        TextField yValueField = new TextField();
        yValueField.setPromptText("Y");

        grid.add(new Label("Vertex name:"), 0, 0);
        grid.add(vertexNameField, 1, 0);
        grid.add(new Label("X:"), 0, 1);
        grid.add(xValueField, 1, 1);
        grid.add(new Label("Y:"), 0, 2);
        grid.add(yValueField, 1, 2);
        addNodeDialog.getDialogPane().setContent(grid);
            final Optional result = addNodeDialog.showAndWait();
        if (result.isPresent()) {
            graph.addVertex(vertexNameField.getText());
            final var xCoordinate = Double.parseDouble(xValueField.getText());
            final var yCoordinate = Double.parseDouble(yValueField.getText());
            final var vertexName = vertexNameField.getText();
            final var vertexView = addVertexView(xCoordinate, yCoordinate, vertexName);
            vertices.put(vertexName, vertexView);
            clearView();
            setEdgesView();
            setVerticesView();
        }
    }

    @FXML
    void onDeleteNodeClicked(ActionEvent event) {
        final ChoiceDialog<String> dialog = new ChoiceDialog<>();
        dialog.getItems().addAll(vertices.keySet());
        dialog.setTitle("Choice Dialog");
        dialog.setHeaderText("Look, a Choice Dialog");
        dialog.setContentText("Choose your letter:");

        final Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            graph.removeVertex(result.get());
            vertices.remove(result.get());
            clearView();
            setEdgesView();
            setVerticesView();
        }
    }

    @FXML
    void onAddEdgeClicked(ActionEvent event) {
        final Dialog addNodeDialog = new Dialog();
        addNodeDialog.setTitle("Add Edge");
        addNodeDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> sourceVertices = new ComboBox<>();
        sourceVertices.getItems().addAll(vertices.keySet());
        ComboBox<String> destVertices = new ComboBox<>();
        destVertices.getItems().addAll(vertices.keySet());
        TextField valueField = new TextField();
        valueField.setPromptText("Weight:");

        grid.add(new Label("Source node:"), 0, 0);
        grid.add(sourceVertices, 1, 0);
        grid.add(new Label("Destination node:"), 0, 1);
        grid.add(destVertices, 1, 1);
        grid.add(new Label("Weight:"), 0, 2);
        grid.add(valueField, 1, 2);
        addNodeDialog.getDialogPane().setContent(grid);
        Optional result = addNodeDialog.showAndWait();
        if (result.isPresent()) {
            final var weight = Double.parseDouble(valueField.getText());
            final var from = sourceVertices.getValue();
            final var to = destVertices.getValue();
            graph.addEdge(weight, from, to);
            clearView();
            setEdgesView();
            setVerticesView();
        }
    }

    @FXML
    void onDeleteEdgeClicked(ActionEvent event) {
        final Dialog addNodeDialog = new Dialog();
        addNodeDialog.setTitle("Delete Edge");
        addNodeDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> sourceVertices = new ComboBox<>();
        sourceVertices.getItems().addAll(vertices.keySet());
        ComboBox<String> destVertices = new ComboBox<>();
        destVertices.getItems().addAll(vertices.keySet());

        grid.add(new Label("Source node:"), 0, 0);
        grid.add(sourceVertices, 1, 0);
        grid.add(new Label("Destination node:"), 0, 1);
        grid.add(destVertices, 1, 1);
        addNodeDialog.getDialogPane().setContent(grid);
        Optional result = addNodeDialog.showAndWait();
        if (result.isPresent()) {
            final var from = sourceVertices.getValue();
            final var to = destVertices.getValue();
            graph.removeEdge(from, to);
            clearView();
            setEdgesView();
            setVerticesView();
        }
    }

    @FXML
    void onFindTheShortestPathClicked(ActionEvent event) {
        final Dialog dialog = new Dialog();
        dialog.setTitle("Dijkstra SHPath");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> sourceVertices = new ComboBox<>();
        sourceVertices.getItems().addAll(vertices.keySet());
        ComboBox<String> destVertices = new ComboBox<>();
        destVertices.getItems().addAll(vertices.keySet());
        TextField trainLength = new TextField();
        trainLength.setPromptText("Train length");
        Button shortestPath = new Button("Calculate");
        TextField pathCost = new TextField();
        Label path = new Label();
        path.setPrefWidth(450.0);
        pathCost.setDisable(true);

        grid.add(new Label("Source node:"), 0, 0);
        grid.add(sourceVertices, 0, 1);
        grid.add(new Label("Destination node:"), 0, 2);
        grid.add(destVertices, 0, 3);
        grid.add(new Label("Train length:"), 0, 4);
        grid.add(trainLength, 0, 5);
        grid.add(new Label("Path cost:"), 0, 6);
        grid.add(pathCost, 0, 7);
        grid.add(shortestPath, 0, 8);
        grid.add(path, 0, 9);

        pathCost.setText("0.0");
        dialog.getDialogPane().setContent(grid);
        shortestPath.setOnAction(e -> {
            final var trainLengthValue = Double.parseDouble(trainLength.getText());
            findTheShortestPath(sourceVertices.getValue(), destVertices.getValue(), trainLengthValue);
            pathCost.setText(dijkstraSPAlgorithm.getShortestPathCost().toString());
            path.setText(dijkstraSPAlgorithm.getPath());
        });
        dialog.showAndWait();
    }

    @FXML
    void onSetVacancyClicked(ActionEvent event) {
        Dialog addNodeDialog = new Dialog();
        addNodeDialog.setTitle("Set Vacancy");
        addNodeDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> sourceEdges = new ComboBox<>();
        sourceEdges.getItems().addAll(vertices.keySet());
        TextField vertexVacancyActualValue = new TextField();
        vertexVacancyActualValue.setDisable(true);

        TextField vertexVacancyField = new TextField();
        vertexVacancyField.setPromptText("Vacancy");

        sourceEdges.valueProperty().addListener((observableValue, s, t1) -> {
            vertexVacancyActualValue.setText(String.valueOf(graph.getVertex(t1).getVacancy()));
        });

        grid.add(new Label("Vertex:"), 0, 0);
        grid.add(sourceEdges, 1, 0);
        grid.add(new Label("Actual value:"), 0, 1);
        grid.add(vertexVacancyActualValue, 1, 1);
        grid.add(new Label("New value:"), 0, 2);
        grid.add(vertexVacancyField, 1, 2);
        addNodeDialog.getDialogPane().setContent(grid);
        Optional result = addNodeDialog.showAndWait();
        if (result.isPresent()) {
            final var vertex = graph.getVertex(sourceEdges.getValue());
            vertex.setVacancy(Double.parseDouble(vertexVacancyField.getText()));
        }
    }

    @FXML
    void onResetClicked(ActionEvent event) {

    }

    private void clearView() {
        pane.getChildren().clear();
    }

    private void importGraphADT() {
        this.graph = GraphService.importGraph();
    }

    private void importGraphVerticesCoordinates() {
        final var vertexCartesianCoordinates = GraphService.importVertexCoordinates();
        for (Map.Entry<String, DiGraph<String, Double>.Vertex> entry : graph.getAdjVertices().entrySet()) {
            final Point2D vertexCoordinates = vertexCartesianCoordinates.get(entry.getKey());
            final var stack = addVertexView(vertexCoordinates.getX(), vertexCoordinates.getY(), entry.getKey());
            vertices.putIfAbsent(entry.getKey(), stack);
        }
    }

    private void setEdgesView() {
        for (Map.Entry<String, StackPane> entry : vertices.entrySet()) {
            var source = graph.getVertex(entry.getKey());
            for (Map.Entry<DiGraph<String, Double>.Vertex, Double> node : source.getVertices().entrySet()) {
                var dest = vertices.get(node.getKey().getKey());
                addUnidirectionalEdge(source, node.getKey(), entry.getValue(), dest);
            }
        }
    }

    private void setVerticesView() {
        for (Map.Entry<String, StackPane> entry : vertices.entrySet()) {
            pane.getChildren().add(entry.getValue());
        }
    }

    private void setReversedVerticesLength(Double length) {
        for (Map.Entry<String, DiGraph<String, Double>.Vertex> entry : graph.getAdjVertices().entrySet()) {
            entry.getValue().setReversedVerticesLength(length);
        }
    }

    private void findTheShortestPath(String source, String dest, Double trainLength) {
        var startFromVertex = graph.getVertex(source);
        var startToVertex = startFromVertex.getEndVertex();

        var endVertex = graph.getVertex(dest);
        setReversedVerticesLength(trainLength);
        this.shortestPath = dijkstraSPAlgorithm.DijkstraShortestPath(startToVertex, endVertex, trainLength);
        dijkstraSPAlgorithm.reset();
        clearView();
        setEdgesView();
        setVerticesView();
    }


    private void addUnidirectionalEdge(DiGraph<String, Double>.Vertex source, DiGraph<String, Double>.Vertex dest, StackPane start, StackPane end) {
        final Line startLine = new Line();
        Color color = Color.BLACK;
        if (source.getReversed().containsKey(dest))
            startLine.getStrokeDashArray().addAll(2d);
        if (shortestPath != null && shortestPath.containsKey(dest) && shortestPath.get(dest) == source)
            color = Color.RED;
        startLine.setStroke(color);
        startLine.setStrokeWidth(1);

        startLine.startXProperty().bind(start.layoutXProperty().add(start.translateXProperty()).add(start.widthProperty().divide(2.0)));
        startLine.startYProperty().bind(start.layoutYProperty().add(start.translateYProperty()).add(start.heightProperty().divide(2.0)));
        startLine.endXProperty().bind(end.layoutXProperty().add(end.translateXProperty()).add(end.widthProperty().divide(2.0)));
        startLine.endYProperty().bind(end.layoutYProperty().add(end.translateYProperty()).add(end.heightProperty().divide(2.0)));

        var arrow = getArrow(startLine, start, end, color);

        pane.getChildren().addAll(arrow, startLine);
    }

    private StackPane addVertexView(Double x, Double y, String text) {
        StackPane stack = new StackPane();
        stack.setLayoutX(x);
        stack.setLayoutY(y);
        final Circle vertex = new Circle(15);
        vertex.setFill(Color.RED);
        final Text vertexLabel = new Text(text);
        vertexLabel.setFill(Color.WHITE);
        stack.getChildren().addAll(vertex, vertexLabel);
        return stack;
    }

    private StackPane getArrow(Line line, StackPane startDot, StackPane endDot, Color color) {
        double size = 6; // Arrow size
        StackPane arrow = new StackPane();
        arrow.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        BorderStroke stroke = new BorderStroke(color, BorderStrokeStyle.SOLID, new CornerRadii(1), new BorderWidths(1));
        Border border = new Border(stroke);
        arrow.setBorder(border);
        arrow.setStyle("-fx-shape: \"M0,-4L4,0L0,4Z\"");//
        arrow.setPrefSize(size, size);
        arrow.setMaxSize(size, size);
        arrow.setMinSize(size, size);

        DoubleBinding tX = Bindings.createDoubleBinding(() -> {
            double xDiffSqu = (line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX());
            double yDiffSqu = (line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY());
            double lineLength = Math.sqrt(xDiffSqu + yDiffSqu);
            double dt = lineLength - (endDot.getWidth() / 2) - (arrow.getWidth() / 2);

            double t = dt / lineLength;
            double dx = ((1 - t) * line.getStartX()) + (t * line.getEndX());
            return dx;
        }, line.startXProperty(), line.endXProperty(), line.startYProperty(), line.endYProperty());

        DoubleBinding tY = Bindings.createDoubleBinding(() -> {
            double xDiffSqu = (line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX());
            double yDiffSqu = (line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY());
            double lineLength = Math.sqrt(xDiffSqu + yDiffSqu);
            double dt = lineLength - (endDot.getHeight() / 2) - (arrow.getHeight() / 2);
            double t = dt / lineLength;
            double dy = ((1 - t) * line.getStartY()) + (t * line.getEndY());
            return dy;
        }, line.startXProperty(), line.endXProperty(), line.startYProperty(), line.endYProperty());

        arrow.layoutXProperty().bind(tX.subtract(arrow.widthProperty().divide(2)));
        arrow.layoutYProperty().bind(tY.subtract(arrow.heightProperty().divide(2)));

        DoubleBinding endArrowAngle = Bindings.createDoubleBinding(() -> {
            double stX = line.getStartX();
            double stY = line.getStartY();
            double enX = line.getEndX();
            double enY = line.getEndY();
            double angle = Math.toDegrees(Math.atan2(enY - stY, enX - stX));
            if (angle < 0) {
                angle += 360;
            }
            return angle;
        }, line.startXProperty(), line.endXProperty(), line.startYProperty(), line.endYProperty());
        arrow.rotateProperty().bind(endArrowAngle);

        return arrow;
    }
}
