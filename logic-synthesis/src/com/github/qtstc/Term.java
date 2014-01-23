package com.github.qtstc;

/* Copyright (c) 2013 the authors listed at the following URL, and/or
 the authors of referenced articles or incorporated external code:
 http://en.literateprograms.org/Quine-McCluskey_algorithm_(Java)?action=history&offset=20110925122251

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 Retrieved from: http://en.literateprograms.org/Quine-McCluskey_algorithm_(Java)?oldid=17357
 */

import java.util.*;
import java.io.*;

public class Term {
	private byte[] varVals;
	
    public static final byte False = 0;
    public static final byte True = 1;
    public static final byte DontCare = 2;
    
    private List<String> names = new ArrayList<String>();

    public Term(byte[] varVals) {
        this.varVals = varVals;
    }
    public Term(byte[] varVals, List<String> names) {
        this(varVals);
        this.addNames(names);
    }
    
    public void addNames(List<String> names) {
        this.names.addAll(names);
    }

    public int getNumVars() {
        return varVals.length;
    }

    public String toString() {
        String result = "(";
        for (int i = 0; i < varVals.length; i++) {
            /*if (varVals[i] == DontCare)
                result += "X";
            else
                result += varVals[i];*/
            result += getVarResult(i);
        }
        result += ")";
        return result;
    }
    
    private String getVarResult(int i) {
        String result = "";
        if (varVals[i] != DontCare) {
//          result = Character.toString((char)(i + 65)) + (varVals[i] == False ? "'" : "");
        	result = names.get(i) + (varVals[i] == False ? "' " : " ");
        }
        return result;
    }

    public Term combine(Term term) {
        int diffVarNum = -1; // The position where they differ
        for (int i = 0; i < varVals.length; i++) {
            if (this.varVals[i] != term.varVals[i]) {
                if (diffVarNum == -1) {
                    diffVarNum = i;
                } else {
                    // They're different in at least two places
                    return null;
                }
            }
        }
        if (diffVarNum == -1) {
            // They're identical
            return null;
        }
        byte[] resultVars = varVals.clone();
        resultVars[diffVarNum] = DontCare;
        return new Term(resultVars, names);
    }

    public int countValues(byte value) {
        int result = 0;
        for (int i = 0; i < varVals.length; i++) {
            if (varVals[i] == value) {
                result++;
            }
        }
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null || !getClass().equals(o.getClass())) {
            return false;
        } else {
            Term rhs = (Term) o;
            return Arrays.equals(this.varVals, rhs.varVals);
        }
    }

    public int hashCode() {
        return varVals.hashCode();
    }

    boolean implies(Term term) {
        for (int i = 0; i < varVals.length; i++) {
            if (this.varVals[i] != DontCare
                    && this.varVals[i] != term.varVals[i]) {
                return false;
            }
        }
        return true;
    }

    public static Term read(Reader reader) throws IOException {
        int c = '\0';
        ArrayList<Byte> t = new ArrayList<Byte>();
        while (c != '\n' && c != -1) {
            c = reader.read();
            if (c == '0') {
                t.add((byte) False);
            } else if (c == '1') {
                t.add((byte) True);
            }
        }
        if (t.size() > 0) {
            byte[] resultBytes = new byte[t.size()];
            for (int i = 0; i < t.size(); i++) {
                resultBytes[i] = (byte) t.get(i);
            }
            return new Term(resultBytes);
        } else {
            return null;
        }
    }
    
    public static Term readCompleteTT(Reader reader) throws IOException {
        int c = reader.read(), prevC = c;
        if (c == -1) {
            return null;
        }
        ArrayList<Byte> t = new ArrayList<Byte>();
        while (c != '\n' && c != -1) {
            if (c == '0') {
                t.add((byte) False);
            } else if (c == '1') {
                t.add((byte) True);
            }
            prevC = c;
            c = reader.read();
        }
        if (prevC == '0') {
            // don't create term if output is false
            t.clear();
        } else if (t.size() > 0) {
            // remove output column
            t.remove(t.size()-1);
        }
        byte[] resultBytes = new byte[t.size()];
        for (int i = 0; i < t.size(); i++) {
            resultBytes[i] = (byte) t.get(i);
        }
        return new Term(resultBytes);
    }
    
    public byte[] getVals() {
        return varVals;
    }
}
