package indexator;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.*;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class CreateIndex {
    public CreateIndex(File[] allFiles,String nameIndex) throws IOException {
        HashMap<String, ArrayList<Integer>> index = new HashMap<>();
        ArrayList<String> name = new ArrayList<>();
        LuceneMorphology luceneMorph = new RussianLuceneMorphology();
        BreakIterator bi = BreakIterator.getWordInstance(new Locale("RU"));
        for (int i = 0; i < allFiles.length; i++) {
            BufferedReader bf = new BufferedReader(new FileReader(allFiles[i]));
            name.add(allFiles[i].getName());
            String s = "";
            while ((s = bf.readLine()) != null) {

                bi.setText(s);
                int lastIndex = bi.first();
                while (lastIndex != BreakIterator.DONE) {
                    int firstIndex = lastIndex;
                    lastIndex = bi.next();
                    if (lastIndex != BreakIterator.DONE && Character.isLetter(s.charAt(firstIndex))) {
                        String word = s.substring(firstIndex, lastIndex);
                        word = word.toLowerCase();
                        if (!check(word)) continue;
                        List<String> wordBaseForms = luceneMorph.getMorphInfo(word);
                        for (String s1 : wordBaseForms) {
                            String[] cur = s1.split("\\|");
                            if (index.containsKey(cur[0])) {
                                ArrayList<Integer> tek = index.get(cur[0]);
                                if (tek.get(tek.size() - 1) != i) {
                                    tek.add(i);
                                }
                            } else {
                                ArrayList<Integer> tek = new ArrayList<>();
                                tek.add(i);
                                index.put(cur[0],tek);

                            }
                        }
                    }
                }
            }


        }
        PrintWriter out = new PrintWriter(nameIndex);
        for (String i : index.keySet()) {
            out.print(i + " ");
            for (Integer j : index.get(i)) {
                out.print(j.toString()+" ");
            }
            out.println();
        }
        out.close();
        out = new PrintWriter("name.out");
        for (String i:name) {
            out.println(i);
        }
        out.close();
    }
    private boolean check(String word) {
        for (int i = 0; i < word.length();i++) {
            if (!(word.charAt(i) >='А' && word.charAt(i) <='Я' || word.charAt(i) >='а' && word.charAt(i) <='я' || word.charAt(i) =='ё')) {
                return false;
            }
        }
        return true;
    }
}
