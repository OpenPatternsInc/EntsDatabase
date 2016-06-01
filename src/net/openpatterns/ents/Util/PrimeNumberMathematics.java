package net.openpatterns.ents.Util;

/**
 * <p></p>
 */
public class PrimeNumberMathematics {


    static int greatestCommonFactor(int a1, int a2) {

        /* Both are non zero and positive, otherwise return 0. */
        if (a1 <= 0 || a1 * a2 <= 0)
            return 0;

        while (a1 * a2 != 0) {

            /* Make sure n1 > n2, otherwise swap them. */
            if (a1 > a2) {
                a1 = a1 - a2;
            } else if (a1 < a2) {
                a2 = a2 - a1;
            } else {
                return a1;
            }
            /* Better way? */

        }

        return a1 + a2;

    }

    public static void main(String[] args) {

        //pr(greatestCommonFactor(, ));

    }

    public static void pr(Object text) {
        System.out.println(text);
    }


}
