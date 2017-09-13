/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra
 * @version 1.4 - April 14, 2016
 *
 **/
public class RadixSort
{

    /**
     * Does Radix sort on the passed in array with the following restrictions:
     *  The array can only have ASCII Strings (sequence of 1 byte characters)
     *  The sorting is stable and non-destructive
     *  The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     **/
    public static String[] sort(String[] asciis)
    {
        String[] toSort = new String[asciis.length];
        System.arraycopy(asciis, 0, toSort, 0, asciis.length);
        sortHelper(toSort, 0, asciis.length - 1, 0);

        return toSort;
    }

    /**
     * Radix sort helper function that recursively calls itself to achieve the sorted array
     *  destructive method that changes the passed in array, asciis
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelper(String[] asciis, int start, int end, int index)
    {
        //System.out.println(start + ", " + end + " index = " + index) ;
        //TODO use if you want to
        String[] sorted = new String[asciis.length];
        int[] counts = new int[256];
        int[] positions = new int[256];

        for (int i = start; i <= end; i++) {
            String s = asciis[i];
            if (index >= s.length()) {
                swap(asciis, start, i);
                sorted[start++] = s;
                continue;
            }

            int c = (int) s.charAt(index);
            counts[c] += 1;

            //System.out.print(s + " ");
        } //System.out.println();

        /* set starting positions for keys to be inserted
           into sorted array
        */
        int mark = start;
        for (int i = 0; i < 256; i++) {
            positions[i] = mark;
            mark += counts[i];
        }

        //for (int i = 0; i < 256; i++) System.out.println((char) i + " " + positions[i]);

        // System.out.println("start = " + start);
        /* add strings to the sorted array in order */
        for (int i = start; i <= end; i++) {
            String s = asciis[i];
            int c = (int) s.charAt(index);
            //System.out.println("pos " + positions[c] + ", " + s);
            sorted[positions[c]++] = s;
        }

        System.arraycopy(sorted, start, asciis, start, end - start + 1);

        for (int i = 0; i < 256; i++) {
            if (counts[i] > 1) {
                sortHelper(asciis, positions[i] - counts[i], positions[i] - 1, index + 1);
            }
        }
    }

    public static void swap(String[] arr, int i, int j) {
        String temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}