package hw3.hash;

import java.util.HashSet;
import java.util.Set;

public class HashTableVisualizer {

    public static void main(String[] args) {
        /* scale: StdDraw scale
           N:     number of items
           M:     number of buckets */

        double scale = 0.5;
        int N = 100;
        int M = 12;

        HashTableDrawingUtility.setScale(scale);
        Set<Oomage> oomies = new HashSet<Oomage>();
        for (int i = 0; i < N; i += 1) {
            oomies.add(SimpleOomage.randomSimpleOomage());
        }
        visualize(oomies, M, scale);
    }

    public static void visualize(Set<Oomage> set, int M, double scale) {
        HashTableDrawingUtility.drawLabels(M);
        int[] bucketOccupants = new int[M];
        for (int i = 0; i < M; ++i) {
          bucketOccupants[i] = 0;
        }

        for (Oomage oom: set) {
          int bucketNum = (oom.hashCode() & 0x7FFFFFFF) % M;
          int position = bucketOccupants[bucketNum];

          oom.draw(HashTableDrawingUtility.xCoord(position), HashTableDrawingUtility.yCoord(bucketNum, M));
          ++bucketOccupants[bucketNum];
        }

        /* TODO: Create a visualization of the given hash table. Use
           du.xCoord and du.yCoord to figure out where to draw
           Oomages.
         */

        /* When done with visualizer, be sure to try 
           scale = 0.5, N = 2000, M = 100. */

    }
} 
