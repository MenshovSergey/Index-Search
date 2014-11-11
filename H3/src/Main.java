import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.StringTokenizer;


public class Main {
    public static final double PBreak = 0.15;
    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader("test.in"));
        int n = Integer.parseInt(bf.readLine());
        ArrayList<Integer> scores = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            scores.add(0);
        }
        StringTokenizer st = new StringTokenizer(bf.readLine());
        int i = 0;
        int sum = 0;
        while (st.hasMoreTokens()) {
            scores.set(i, Integer.parseInt(st.nextToken()));
            sum += scores.get(i);
            i++;
        }
        double DCG = getDCG(scores);
        Collections.sort(scores, Collections.reverseOrder());
        double IDCG = getDCG(scores);
        double NDCG = DCG / IDCG;
        System.out.println("DCG = "+ DCG + "    " + "NDCG = "+ NDCG);
        ArrayList<Double> rel = new ArrayList<Double>();
        for (int j = 0; j < scores.size(); j++) {
            rel.add((double)(scores.get(j)) / sum);
        }
        double pFound = getPFound(rel);
        System.out.println("pFound = " + pFound);

    }
    public static double getDCG(ArrayList<Integer> scores) {
        double result = scores.get(0);
        for (int i = 1; i < scores.size(); i++) {
            result += scores.get(i)/(Math.log(i + 1)/Math.log(2));
        }
        return result;
    }
    public static double getPFound(ArrayList<Double> rel) {
        double pLook = 0.75;
        double result = 0;
        for (int i = 0; i < rel.size(); i++) {
            if (i > 0) {
                pLook = pLook * (1 - rel.get(i - 1)) * (1 - PBreak);
            }
            result += pLook * rel.get(i);
        }
        return result;
    }

}
