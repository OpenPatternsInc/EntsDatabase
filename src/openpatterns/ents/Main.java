/*
The Ents database library provides access to a Taxonomical Hierarchy.
Copyright (C) 2016  Jason Stockwell
www.openpatterns.net/#ents

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package openpatterns.ents;

/**
 * This class is run at the start of the program, it's the main class.
 * It calls the rest of the program.
 */

public class Main {

    public static void main(String[] args) {

        Data data = new Data();

        CLI cli = new CLI(data);

    }
}
