package service;

import graph.DiGraph;
import graph.GraphADT;
import javafx.geometry.Point2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphService {

    public static  GraphADT<String, Double> importGraph() {
        final GraphADT<String, Double> graph = new DiGraph<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("src/graph.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("Vertices")) {
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    if (line.startsWith("Edges"))
                        break;
                    graph.addVertex(line);
                }

            } else {
                do {
                    final String[] tmp = line.split(":");
                    var vertex = graph.getVertex( tmp[0]);
                    final String regex = "(\\w+)(\\[(\\w+=\\d+\\.\\d+)[,](\\w+=\\w+)])";
                    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                    final Matcher matcher = pattern.matcher(tmp[1]);
                    while (matcher.find()) {
                        System.out.println("Full match: " + matcher.group(0));
                        var destVertex =graph.getVertex( matcher.group(1));
                        var weight =  Double.parseDouble(matcher.group(3).split("=")[1]);
                        var reversed = Boolean.parseBoolean(matcher.group(4).split("=")[1]);
                        vertex.addDistance(destVertex, weight);
                        if (reversed)
                            vertex.addReversedVertex(destVertex, reversed);
                        else
                            destVertex.setVacancy(weight);
                    }
                    line = scanner.nextLine();
                }
                while (scanner.hasNextLine());
            }
        }
        return graph;
    }

    public static <K,V> Map<String, Point2D> importVertexCoordinates() {
        Map<String, Point2D> vertexCoordinates = new HashMap<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("src/vertex_cartesian_coordinates.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tmp = line.split(":");
            String vertex = tmp[0];
            String[] coordinates = tmp[1].split(",");
            double x = Double.parseDouble(coordinates[0]);
            double y = Double.parseDouble(coordinates[1]);
            vertexCoordinates.put(vertex, new Point2D(x, y));
        }

        return vertexCoordinates;
    }

}
