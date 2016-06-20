/*******************************************************************************
 * Utility resources not specific to an Ents sub-project.
 * Copyright (c) 2016. Jason Stockwell and OpenPatterns Inc.
 * www.openpatterns.net/#ents
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package net.openpatterns.ents.Util;

/**
 * <p>A preliminary class that would perform prime number calculations.</p>
 * <p>Prime numbers could perhaps be used to efficiently organize and analyze an Ent's location
 * within the hierarchy by associating each node with a prime number and having all children be
 * divisible by it.</p>
 * <p>For instance, to find the place where two Ent's ancestors differ, we could calculate the
 * greatest common factor instead of going through a linked list and performing logic tests.</p>
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
