package hometask2.searcher;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.*;
import java.util.*;


public class Searcher {
    public static void main(String[] args) throws IOException {
        HashMap<String, HashMap<Integer, ArrayList<Integer>>> index = getIndex(args[0]);
        ArrayList<String> nameFiles = getNameFiles();
        LuceneMorphology luceneMorph = new RussianLuceneMorphology();
        BufferedReader bf = new BufferedReader(new InputStreamReader((System.in)));
        String s = "";

        while ((s = bf.readLine()) != null) {
            String[] query = s.split(" +");
            HashSet<Integer> result = new HashSet<>();
            List<String> allForms = getAllForms(luceneMorph.getMorphInfo(query[0]));
            for (String form : allForms) {
                if (index.containsKey(form)) {
                    for (int i : index.get(form).keySet()) {
                        ArrayList<Integer> cur = new ArrayList<>(index.get(query[0]).get(i));
                        for (int j = 1; j < query.length; j += 2) {
                            String normalForm = getAllForms(luceneMorph.getMorphInfo(query[j + 1])).get(0);
                            if (query[j].charAt(1) == '+' || query[j].charAt(1) == '-') {
                                int N = Integer.parseInt(query[j].substring(1));
                                if (index.get(normalForm).containsKey(i)) {
                                    HashSet<Integer> c = new HashSet<>(index.get(normalForm).get(i));
                                    cur = getForward(cur, c, N);
                                } else {
                                    cur = new ArrayList<>();
                                }
                            } else {
                                int N = Integer.parseInt(query[j].substring(1));
                                if (index.get(normalForm).containsKey(i)) {
                                    cur = getNearPosition(cur, index.get(normalForm).get(i), N);
                                } else {
                                    cur = new ArrayList<>();
                                }
                            }
                        }
                        if (cur.size() > 0) {
                            result.add(i);
                        }

                    }
                }
            }
            if (result.size() > 0) {
                for (int i : result) {
                    System.out.print(nameFiles.get(i) + " ");
                }
                System.out.println();
            } else {
                System.out.println("not found");
            }



        }
    }
    private static ArrayList<Integer> getNearPosition(ArrayList<Integer> first, ArrayList<Integer> second, int N) {
        ArrayList<Integer> result = new ArrayList<>();
        int pFirst = 0;
        int pSecond = 0;
        while (pFirst < first.size() && pSecond < second.size()) {
            while (pSecond < second.size() &&(first.get(pFirst) + N >= second.get(pSecond) && first.get(pFirst) - N  >= second.get(pSecond))) {
                result.add(second.get(pSecond));
                pSecond++;
            }
            pFirst++;
        }
        return result;
    }
    private static ArrayList<String> getAllForms(List<String> all) {
        ArrayList<String> result = new ArrayList<>();
        for (String i : all) {
            String[] cur = i.split("\\|");
            result.add(cur[0]);
        }
        return result;
    }
    private static ArrayList getForward(ArrayList<Integer> first, HashSet<Integer> second, int N) {
        ArrayList<Integer> answer = new ArrayList<>();
        for (int i : first) {
            if (second.contains(i + N)) {
                answer.add(i + N);
            }
        }
        return  answer;
    }
    private static HashMap<String, HashMap<Integer, ArrayList<Integer>>> getIndex(String s) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(s));
        HashMap<String, HashMap<Integer, ArrayList<Integer>>> result = new HashMap<>();
        int n = Integer.parseInt(bf.readLine());
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(bf.readLine());
            String word = st.nextToken();
            result.put(word,new HashMap<Integer, ArrayList<Integer>>());
            int countDocuments = Integer.parseInt(st.nextToken());
            for (int j = 0; j < countDocuments; j++) {
                st = new StringTokenizer(bf.readLine());
                int numberDocument = Integer.parseInt(st.nextToken());
                ArrayList<Integer> pos = new ArrayList<>();
                result.get(word).put(numberDocument,pos);
                int countPositionInDocument = Integer.parseInt(st.nextToken());
                for (int k = 0; k < countPositionInDocument; k++) {
                    int position = Integer.parseInt(st.nextToken());
                    pos.add(position);
                }
            }
        }
        return result;
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
