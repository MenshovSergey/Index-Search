package searcher;


import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class Search {
    public static final int OR = 0, AND = 1;


    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader((System.in)));
        String s = "";
        HashMap<String, ArrayList<Integer>> index = getIndex(args[0]);
        ArrayList<String> nameFiles = getNameFiles();
        LuceneMorphology luceneMorph = new RussianLuceneMorphology();

        int flag = OR;
        while ((s = bf.readLine()) != null) {
            String[] words = s.split(" +");
            ArrayList<Integer> ans = new ArrayList<>();
            int checker = -1;
            for (int i = 0; i < words.length; i++) {
                if (words[i].contains("AND")) {
                    if (checker == 0) {
                        System.out.println("incorrect query");
                        checker = 3;
                        break;
                    } else {
                        checker = 1;
                    }

                }
                if (words[i].contains("OR")) {
                    if (checker == 1) {
                        System.out.println("incorrect query");
                        checker = 3;
                        break;
                    } else {
                        checker = 0;
                    }
                }

            }
            if (checker != 3) {
                for (int i = 0; i < words.length; i++) {
                    if (!words[i].equals("AND") && !words[i].equals("OR")) {
                        List<String> wordBaseForms = luceneMorph.getMorphInfo(words[i]);
                        ArrayList<String> allForms = getAllForms(wordBaseForms);
                        for (String j : allForms) {

                            ans = mergeLists(ans, index.get(j), flag);
                        }
                    } else {

                        flag = words[i].length() - 2;
                    }
                }
                if (ans.size() == 0) {
                    System.out.println("not found");
                } else
                    for (int i = 0; i < ans.size(); i++) {
                        System.out.println(nameFiles.get(ans.get(i)));
                    }
            }



        }

    }
    private static ArrayList<String> getAllForms(List<String> all) {
        ArrayList<String> result = new ArrayList<>();
        for (String i : all) {
            String[] cur = i.split("\\|");
            result.add(cur[0]);
        }
        return result;
    }
    private static ArrayList<Integer> mergeLists(ArrayList<Integer> first, ArrayList<Integer> second, int flag) {
        ArrayList<Integer> result = new ArrayList<>();

        if (flag == OR) {
            int u1 = 0;
            int u2 = 0;
            if (second == null) {
                return first;
            }
            if (first.size() == 0) {
                return second;
            }
            while (u1 < first.size() && u2 < second.size()) {
                while (first.get(u1) < second.get(u2) && u1 < first.size()) {
                    result.add(first.get(u1));
                    u1++;
                }
                if (u1 == first.size()) break;
                while (first.get(u1) > second.get(u2) && u2 < second.size()) {
                    result.add(second.get(u2));
                    u2++;
                }
                if (u2 == second.size()) break;
                if (second.get(u2).equals(first.get(u1))) {
                    result.add(second.get(u2));
                    u1++;
                    u2++;
                }
            }

            if (u1 < first.size()) {
                while (u1 < first.size()) {
                    result.add(first.get(u1));
                    u1++;
                }
            }
            if (u2 < second.size()) {
                while (u2 < second.size()) {
                    result.add(first.get(u2));
                    u2++;
                }
            }


            return result;

        } else {
            int u1 = 0;
            int u2 = 0;
            if (second == null) {
                return result;
            }
            while (u1 < first.size() && u2 < second.size()) {
                while (first.get(u1) < second.get(u2) && u1 < first.size()) {
                    u1++;
                }
                if (u1 == first.size()) break;
                while (first.get(u1) > second.get(u2) && u2 < second.size()) {
                    u2++;
                }
                if (u2 == second.size()) break;
                if (second.get(u2) == first.get(u1)) {
                    result.add(second.get(u2));
                    u1++;
                    u2++;
                }
            }

            return result;

        }
    }
    private static HashMap<String, ArrayList<Integer>> getIndex(String name) throws IOException {
        HashMap<String, ArrayList<Integer>> index = new HashMap<>();
        String s = "";
        BufferedReader bf = new BufferedReader(new FileReader(name));
        while((s = bf.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(s);
            String key = st.nextToken();
            ArrayList<Integer> tek = new ArrayList<>();
            while (st.hasMoreTokens()) {
                tek.add(Integer.parseInt(st.nextToken()));
            }
            index.put(key, tek);

        }
        return index;
    }
    private static ArrayList<String> getNameFiles() throws IOException {
        ArrayList<String> ans = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new FileReader("name.out"));
        String s = "";
        while ((s = bf.readLine()) != null) {
            ans.add(s);
        }
        return ans;
    }

}