package uff.ic.lleme.entityrelatednesstestdata.v3;

import java.util.ArrayList;
import java.util.Map;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.Category;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.DB;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.Entity;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.Property;
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
import uff.ic.lleme.entityrelatednesstestdata.v3.util.Score;

public class Main {

    public static void main(String[] args) {

        {
            MovieClassMapping MOVIE_CLASS_MAPPING = new MovieClassMapping();
            MusicClassMapping MUSIC_CLASS_MAPPING = new MusicClassMapping();

            MOVIE_CLASS_MAPPING.addAll(MUSIC_CLASS_MAPPING);
            MovieClassMapping categories = MOVIE_CLASS_MAPPING;

            MOVIE_CLASS_MAPPING = null;
            MUSIC_CLASS_MAPPING = null;

            for (Pair mapping : categories)
                try {
                    Category category = DB.Categories.addCategory(mapping.label);
                    try {
                        category.addSameAs(mapping.entity1);
                    } catch (Exception e) {
                        System.out.println(String.format("Error: invalid sameAs resource. (category -> %1s, resource -> %1s)", mapping.label, mapping.entity1));
                    }
                    try {
                        category.addSameAs(mapping.entity2);
                    } catch (Exception e) {
                        System.out.println(String.format("Error: invalid sameAs resource. (category -> %1s, resource -> %1s)", mapping.label, mapping.entity2));
                    }
                } catch (Exception e) {
                    System.out.println(String.format("Error: invalid label. (property -> %1s)", mapping.label));
                }
            System.out.println("");
        }

        {
            MovieEntityMappings MOVIE_ENTITY_MAPPINGS = new MovieEntityMappings();
            MusicEntityMappings MUSIC_ENTITY_MAPPINGS = new MusicEntityMappings();

            MOVIE_ENTITY_MAPPINGS.putAll(MUSIC_ENTITY_MAPPINGS);
            MovieEntityMappings mappings = MOVIE_ENTITY_MAPPINGS;

            MOVIE_ENTITY_MAPPINGS = null;
            MUSIC_ENTITY_MAPPINGS = null;

            MovieScores MOVIE_SCORES = new MovieScores();
            MusicScores MUSIC_SCORES = new MusicScores();

            MOVIE_SCORES.putAll(MUSIC_SCORES);
            MovieScores scores = MOVIE_SCORES;

            MOVIE_SCORES = null;
            MUSIC_SCORES = null;

            for (Map.Entry<String, ArrayList<Pair>> subset : mappings.entrySet())
                for (Pair mapping : subset.getValue())
                    try {
                        Entity entity = DB.Entities.addEntity(mapping.label, mapping.type);
                        try {
                            entity.addSameAs(mapping.entity1);
                        } catch (Exception e) {
                            System.out.println(String.format("Entity error: invalid sameAs resource. (file -> %1s, label -> %1s, resource -> %1s)", subset.getKey(), mapping.label, mapping.entity1));
                        }
                        try {
                            entity.addSameAs(mapping.entity2);
                        } catch (Exception e) {
                            System.out.println(String.format("Entity error: invalid sameAs resource. (file -> %1s, label -> %1s, resource -> %1s)", subset.getKey(), mapping.label, mapping.entity2));
                        }
                    } catch (Exception e) {
                        System.out.println(String.format("Entity error: invalid label or category. (file -> %1s, label -> %1s, category -> %1s)", subset.getKey(), mapping.label, mapping.type));
                    }
        }

        {
            MoviePropertyRelevanceScore MOVIE_PROPERTY_RELEVANCE_SCORE = new MoviePropertyRelevanceScore();
            MusicPropertyRelevanceScore MUSIC_PROPERTY_RELEVANCE_SCORE = new MusicPropertyRelevanceScore();

            MOVIE_PROPERTY_RELEVANCE_SCORE.putAll(MUSIC_PROPERTY_RELEVANCE_SCORE);
            MoviePropertyRelevanceScore properties = MOVIE_PROPERTY_RELEVANCE_SCORE;

            MOVIE_PROPERTY_RELEVANCE_SCORE = null;
            MUSIC_PROPERTY_RELEVANCE_SCORE = null;

            for (Map.Entry<String, Double> property : properties.entrySet())
                try {
                    Property p = DB.Properties.addProperty(property.getKey(), property.getValue());
                } catch (Exception e) {
                    System.out.println(String.format("Property error: invalid label or score. (label -> %1s, score -> %1s)", property.getKey(), property.getValue()));
                }
        }

        {
            MovieEntityPairs MOVIE_ENTITY_PAIRS = new MovieEntityPairs();
            MusicEntityPairs MUSIC_ENTITY_PAIRS = new MusicEntityPairs();

            MOVIE_ENTITY_PAIRS.addAll(MUSIC_ENTITY_PAIRS);
            MovieEntityPairs pairs = MOVIE_ENTITY_PAIRS;

            MOVIE_ENTITY_PAIRS = null;
            MUSIC_ENTITY_PAIRS = null;

            for (Pair p : pairs)
                try {
                    DB.EntityPairs.addEntityPair(p.entity1, p.entity2);
                } catch (Exception e) {
                    System.out.println(String.format("EntityPair error: invalid entity. (entity1 -> %1s, entity2 -> %1s)", p.entity1, p.entity2));
                }
        }

        {
            MovieRankedPaths MOVIE_RANKED_PATHS = new MovieRankedPaths();
            MusicRankedPaths MUSIC_RANKED_PATHS = new MusicRankedPaths();

            MOVIE_RANKED_PATHS.putAll(MUSIC_RANKED_PATHS);
            MovieRankedPaths movieRankedPaths = MOVIE_RANKED_PATHS;

            MOVIE_RANKED_PATHS = null;
            MUSIC_RANKED_PATHS = null;

            for (Map.Entry<String, ArrayList<Score>> paths : movieRankedPaths.entrySet())
                for (Score path : paths.getValue()) {

                }

        }
    }
}
