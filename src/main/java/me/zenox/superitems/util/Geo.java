package me.zenox.superitems.util;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Geo {

    /// Generates a list of vertices (in arbitrary order) for a tetrahedron centered on the origin.
    public static List<Vector> makeDodecahedron(Vector vec, double r) {
        // Calculate constants that will be used to generate vertices
        float phi = (float) (Math.sqrt(5) - 1) / 2; // The golden ratio

        double a = 1 / Math.sqrt(3);
        double b = a / phi;
        double c = a * phi;

        // Generate each vertex
        List<Vector> vertices = new ArrayList<>();
        for (int i : new Integer[]{-1, 1}) {
            for (int j : new Integer[]{-1, 1}) {
                vertices.add(new Vector(
                        0,
                        i * c * r,
                        j * b * r));
                vertices.add(new Vector(
                        i * c * r,
                        j * b * r,
                        0));
                vertices.add(new Vector(
                        i * b * r,
                        0,
                        j * c * r));

                for (int k : new Integer[]{-1, 1})
                    vertices.add(new Vector(
                            i * a * r,
                            j * a * r,
                            k * a * r));
            }
        }

        for (Vector vector : vertices) {
            vector.add(vec);
        }
        return vertices;
    }

    public static List<Vector> lerpEdges(List<Vector> vertices, int steps) {
        List<Vector> result = new ArrayList<>();

        double minLengthSquared = Double.MAX_VALUE;
        for (Vector vertex : vertices) {
            if (vertex == vertices.get(0)) continue;
            double distSqr = vertices.get(0).distanceSquared(vertex);
            if (distSqr < minLengthSquared) minLengthSquared = Util.round(distSqr, 2);
        }


        List<Vector> connected = new ArrayList<>();

        for (Vector vertex : vertices) {
            List<Vector> lConnected = new ArrayList<>();
            for (Vector v : vertices) {
                if (lConnected.size() == 3) break;
                if (v.equals(vertex) || connected.contains(v)) continue;
                if (Math.round(vertex.distanceSquared(v) * 100.0) / 100.0 == minLengthSquared) {
                    lConnected.add(v);
                    for (int i = 0; i < steps; i++) {
                        Vector diff = vertex.clone().subtract(v);
                        result.add(vertex.clone().subtract(diff.multiply(1d / steps * (i + 1))));
                    }
                }
            }
            //connected.add(vertex);
        }

        return result;
    }
}

