package uff.ic.swlab.dataset.entityrelatednesstestdata.v3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import uff.ic.swlab.dataset.entityrelatednesstestdata.v3.util.MovieScores;
import uff.ic.swlab.dataset.entityrelatednesstestdata.v3.util.MusicScores;
import uff.ic.swlab.dataset.entityrelatednesstestdata.v3.util.Score;

public class NewClass {

    private static class Score2 {

        public String pair = null;
        public Double score = null;

        public Score2(String pair, Double score) {
            this.pair = pair;
            this.score = score;
        }
    }

    public static void main(String[] args) {
        MovieScores MOVIE_SCORES = new MovieScores();
        MusicScores MUSIC_SCORES = new MusicScores();

        MOVIE_SCORES.putAll(MUSIC_SCORES);
        MovieScores scores = MOVIE_SCORES;

        MOVIE_SCORES = null;
        MUSIC_SCORES = null;

        Map<String, ArrayList<Score2>> r = new HashMap<>();
        for (Map.Entry<String, ArrayList<Score>> subset : scores.entrySet())
            for (Score score : subset.getValue()) {
                String entity = score.label;
                Double s = score.score;

                ArrayList<Score2> l = r.get(entity);
                if (l == null) {
                    l = new ArrayList<>();
                    r.put(entity, l);
                }
                l.add(new Score2(subset.getKey(), s));
            }
        for (Map.Entry<String, ArrayList<Score2>> s : r.entrySet()) {
            Set<Double> n = new HashSet<>();
            for (Score2 s2 : s.getValue())
                n.add(s2.score);
            if (n.size() > 1)
                for (Score2 s2 : s.getValue())
                    System.out.println(String.format("Entity %s, score %f (pair %s).", s.getKey(), s2.score, s2.pair));
        }
    }
}
