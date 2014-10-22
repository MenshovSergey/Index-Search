package hometask2.indexator;


import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.*;
import java.text.BreakIterator;
import java.util.*;

public class Index {
    public static void main(String[] args) throws IOException {
        String path = args[0];
        File directory = new File(path);
        File[] allFiles = getAlFiles(directory);
        createIndex(allFiles,args[1]);
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
    private static void createIndex(File[] allFiles,String nameIndex) throws IOException
    {
        HashMap<String, HashMap<Integer,ArrayList<Integer>>> index = new HashMap<>();
        ArrayList<String> name = new ArrayList<>();
        LuceneMorphology luceneMorph = new RussianLuceneMorphology();
        BreakIterator bi = BreakIterator.getWordInstance(new Locale("RU"));
        for (int i = 0; i < allFiles.length; i++) {
            BufferedReader bf = new BufferedReader(new FileReader(allFiles[i]));
            name.add(allFiles[i].getName());
            String s = "";
            int positionInDocument = 0;
            while ((s = bf.readLine()) != null) {

                bi.setText(s);
                int lastIndex = bi.first();
                boolean firstEnter = true;
                while (lastIndex != BreakIterator.DONE) {
                    int firstIndex = lastIndex;
                    lastIndex = bi.next();
                    if (lastIndex != BreakIterator.DONE && Character.isLetter(s.charAt(firstIndex))) {
                        String word = s.substring(firstIndex, lastIndex);
                        positionInDocument++;
                        word = word.toLowerCase();
                        if (!check(word)) continue;
                        List<String> wordBaseForms = luceneMorph.getMorphInfo(word);
                        for (String s1 : wordBaseForms) {
                            String[] cur = s1.split("\\|");
                            if (index.containsKey(cur[0])) {
                                ArrayList<Integer> tek = null;
                                if (!index.get(cur[0]).containsKey(i)) {
                                     index.get(cur[0]).put(i,new ArrayList<Integer>());
                                }
                                tek = index.get(cur[0]).get(i);

                                tek.add(positionInDocument);
                            } else {
                                index.put(cur[0], new HashMap<Integer, ArrayList<Integer>>());
                                ArrayList<Integer> tek = new ArrayList<>();
                                tek.add(positionInDocument);
                                index.get(cur[0]).put(i, tek);


                            }
                        }
                    }
                }
            }


        }
        PrintWriter out = new PrintWriter(nameIndex);
        out.println(index.keySet().size());
        for (String i : index.keySet()) {
            out.println(i + " " + index.get(i).size());
            for (int j : index.get(i).keySet()) {
                out.print(j + " " + index.get(i).get(j).size() + " ");
                for (int k : index.get(i).get(j)) {
                    out.print(k + " ");
                }
                out.println();
            }

        }
        out.close();
        out = new PrintWriter("nameNew.out");
        for (String i:name) {
            out.println(i);
        }
        out.close();
    }
    private static boolean check(String word) {
        for (int i = 0; i < word.length();i++) {
            if (!(word.charAt(i) >='А' && word.charAt(i) <='Я' || word.charAt(i) >='а' && word.charAt(i) <='я' || word.charAt(i) =='ё')) {
                return false;
            }
        }
        return true;
    }
}
