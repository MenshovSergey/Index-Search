package indexator;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.morphology.analyzer.MorphologyFilter;
import org.apache.lucene.morphology.russian.RussianAnalyzer;
import org.apache.lucene.morphology.LuceneMorphology;
import  org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import sun.misc.Lock;

import java.io.*;
import java.util.*;

import java.text.*;


public class Indexator {
    public static void main(String[] args) throws IOException {
        String path = args[0];
        File directory = new File(path);
        File[] allFiles = getAlFiles(directory);
        CreateIndex createIndex = new CreateIndex(allFiles,args[1]);



    }
    private static File[] getAlFiles(File directory) {
        ArrayList<File> result = new ArrayList<File>();
        Queue<File> current = new ArrayDeque<File>();
        current.add(directory);
        while (!current.isEmpty()) {
            File cur = current.poll();
            if (cur.isFile()) {
                result.add(cur);
            } else {
                for (File i : cur.listFiles()) {
                    current.add(i);
                }
            }
        }
        File[] f = new File[result.size()];
        result.toArray(f);
        return f;
    }
}
