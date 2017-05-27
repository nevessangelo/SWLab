package uff.ic.lleme.entityrelatednesstestdata.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MovieClassMapping;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MovieEntityMappings;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MovieEntityPairs;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MoviePropertyRelevanceScore;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MovieRankedPaths;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MovieScores;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicClassMapping;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicEntityMappings;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicEntityPairs;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicPropertyRelevanceScore;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicRankedPaths;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicScores;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.Pair;

public class Main {

    public static void main(String[] args) {

        {
            MovieEntityMappings MOVIE_ENTITY_MAPPINGS = new MovieEntityMappings();
            MusicEntityMappings MUSIC_ENTITY_MAPPINGS = new MusicEntityMappings();

            List<Set<Map.Entry<String, ArrayList<Pair>>>> sets = new ArrayList<>();
            sets.add(MOVIE_ENTITY_MAPPINGS.entrySet());
            sets.add(MUSIC_ENTITY_MAPPINGS.entrySet());
            MOVIE_ENTITY_MAPPINGS = null;
            MUSIC_ENTITY_MAPPINGS = null;

        }

        MusicScores MUSIC_SCORES = new MusicScores();
        MusicRankedPaths MUSIC_RANKED_SCORES = new MusicRankedPaths();
        MusicPropertyRelevanceScore MUSIC_PROPERTY_RELEVANCE_SCORE = new MusicPropertyRelevanceScore();
        MusicClassMapping MUSIC_CLASS_MAPPING = new MusicClassMapping();
        MusicEntityPairs MUSIC_ENTITY_PAIRS = new MusicEntityPairs();

        MovieScores MOVIE_SCORES = new MovieScores();
        MovieRankedPaths MOVIE_RANKED_PATHS = new MovieRankedPaths();
        MoviePropertyRelevanceScore MOVIE_PROPERTY_RELEVANCE_SCORE = new MoviePropertyRelevanceScore();
        MovieClassMapping MOVIE_CLASS_MAPPING = new MovieClassMapping();
        MovieEntityPairs MOVIE_ENTITY_PAIRS = new MovieEntityPairs();

    }
}
