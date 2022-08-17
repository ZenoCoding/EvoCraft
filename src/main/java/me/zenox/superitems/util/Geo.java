package me.zenox.superitems.util;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Geo {
    /// <summary>
    /// Generates a list of vertices (in arbitrary order) for a tetrahedron centered on the origin.
    /// </summary>
    /// <param name="r">The distance of each vertex from origin.</param>
    /// <returns></returns>
    public static List<Vector> MakeDodecahedron(Vector vec, double r)
    {
        // Calculate constants that will be used to generate vertices
        float phi = (float)(Math.sqrt(5) - 1) / 2; // The golden ratio

        double a = 1 / Math.sqrt(3);
        double b = a / phi;
        double c = a * phi;

        // Generate each vertex
        List<Vector> vertices = new ArrayList<>();
        for (int i : new Integer[]{ -1, 1 })
        {
            for (int j : new Integer[] { -1, 1 })
            {
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

                for (int k : new Integer[] { -1, 1 })
                vertices.add(new Vector(
                        i * a * r,
                        j * a * r,
                        k * a * r));
            }
        }

        for(Vector vector : vertices){
            vector.add(vec);
        }
        return vertices;
    }
}

