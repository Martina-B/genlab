/*
 * Copyright 2006 - 2015
 *     Stefan Balev     <stefan.balev@graphstream-project.org>
 *     Julien Baudry    <julien.baudry@graphstream-project.org>
 *     Antoine Dutot    <antoine.dutot@graphstream-project.org>
 *     Yoann Pigné      <yoann.pigne@graphstream-project.org>
 *     Guilhelm Savin   <guilhelm.savin@graphstream-project.org>
 * 
 * This file is part of GraphStream <http://graphstream-project.org>.
 * 
 * GraphStream is a library whose purpose is to handle static or dynamic
 * graph, create them from scratch, file or any source and display them.
 * 
 * This program is free software distributed under the terms of two licenses, the
 * CeCILL-C license that fits European law, and the GNU Lesser General Public
 * License. You can  use, modify and/ or redistribute the software under the terms
 * of the CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
 * URL <http://www.cecill.info> or under the terms of the GNU LGPL as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C and LGPL licenses and that you accept their terms.
 */
package org.graphstream.stream.file.pajek;

/**
 * Token literal values and constants. Generated by
 * org.javacc.parser.OtherFilesGen#start()
 */
public interface PajekParserConstants {

	/** End of File. */
	int EOF = 0;
	/** RegularExpression Id. */
	int EOL = 3;
	/** RegularExpression Id. */
	int DIGIT = 4;
	/** RegularExpression Id. */
	int HEXDIGIT = 5;
	/** RegularExpression Id. */
	int INT = 6;
	/** RegularExpression Id. */
	int REAL = 7;
	/** RegularExpression Id. */
	int STRING = 8;
	/** RegularExpression Id. */
	int NETWORK = 9;
	/** RegularExpression Id. */
	int VERTICES = 10;
	/** RegularExpression Id. */
	int ARCS = 11;
	/** RegularExpression Id. */
	int EDGES = 12;
	/** RegularExpression Id. */
	int EDGESLIST = 13;
	/** RegularExpression Id. */
	int ARCSLIST = 14;
	/** RegularExpression Id. */
	int MATRIX = 15;
	/** RegularExpression Id. */
	int COMMENT = 16;
	/** RegularExpression Id. */
	int ELLIPSE = 17;
	/** RegularExpression Id. */
	int DIAMOND = 18;
	/** RegularExpression Id. */
	int CROSS = 19;
	/** RegularExpression Id. */
	int BOX = 20;
	/** RegularExpression Id. */
	int TRIANGLE = 21;
	/** RegularExpression Id. */
	int EMPTY = 22;
	/** RegularExpression Id. */
	int SIZE = 23;
	/** RegularExpression Id. */
	int XFACT = 24;
	/** RegularExpression Id. */
	int YFACT = 25;
	/** RegularExpression Id. */
	int PHI = 26;
	/** RegularExpression Id. */
	int R = 27;
	/** RegularExpression Id. */
	int Q = 28;
	/** RegularExpression Id. */
	int IC = 29;
	/** RegularExpression Id. */
	int BC = 30;
	/** RegularExpression Id. */
	int BW = 31;
	/** RegularExpression Id. */
	int LC = 32;
	/** RegularExpression Id. */
	int LA = 33;
	/** RegularExpression Id. */
	int LR = 34;
	/** RegularExpression Id. */
	int LPHI = 35;
	/** RegularExpression Id. */
	int FOS = 36;
	/** RegularExpression Id. */
	int FONT = 37;
	/** RegularExpression Id. */
	int C = 38;
	/** RegularExpression Id. */
	int P = 39;
	/** RegularExpression Id. */
	int W = 40;
	/** RegularExpression Id. */
	int S = 41;
	/** RegularExpression Id. */
	int A = 42;
	/** RegularExpression Id. */
	int AP = 43;
	/** RegularExpression Id. */
	int L = 44;
	/** RegularExpression Id. */
	int LP = 45;
	/** RegularExpression Id. */
	int H1 = 46;
	/** RegularExpression Id. */
	int H2 = 47;
	/** RegularExpression Id. */
	int K1 = 48;
	/** RegularExpression Id. */
	int K2 = 49;
	/** RegularExpression Id. */
	int A1 = 50;
	/** RegularExpression Id. */
	int A2 = 51;
	/** RegularExpression Id. */
	int B = 52;
	/** RegularExpression Id. */
	int KEY = 53;

	/** Lexical state. */
	int DEFAULT = 0;

	/** Literal token values. */
	String[] tokenImage = { "<EOF>", "\" \"", "\"\\t\"", "<EOL>", "<DIGIT>",
			"<HEXDIGIT>", "<INT>", "<REAL>", "<STRING>", "\"*network\"",
			"\"*vertices\"", "\"*arcs\"", "\"*edges\"", "\"*edgeslist\"",
			"\"*arcslist\"", "\"*matrix\"", "<COMMENT>", "\"ellipse\"",
			"\"diamond\"", "\"cross\"", "\"box\"", "\"triangle\"", "\"empty\"",
			"<SIZE>", "\"x_fact\"", "\"y_fact\"", "\"phi\"", "\"r\"", "\"q\"",
			"\"ic\"", "\"bc\"", "\"bw\"", "\"lc\"", "\"la\"", "\"lr\"",
			"\"lphi\"", "\"fos\"", "\"font\"", "\"c\"", "\"p\"", "\"w\"",
			"\"s\"", "\"a\"", "\"ap\"", "\"l\"", "\"lp\"", "\"h1\"", "\"h2\"",
			"\"k1\"", "\"k2\"", "\"a1\"", "\"a2\"", "\"b\"", "<KEY>", };

}
