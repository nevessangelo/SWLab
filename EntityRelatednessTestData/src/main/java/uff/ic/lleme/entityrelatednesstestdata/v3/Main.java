package uff.ic.lleme.entityrelatednesstestdata.v3;

import java.util.ArrayList;
import java.util.Map;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.Category;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.Entity;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.Resource;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.SetOfCategories;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.SetOfEntities;
import uff.ic.lleme.entityrelatednesstestdata.v3.model.SetOfResources;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MovieClassMapping;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MovieEntityMappings;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MovieScores;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicClassMapping;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicEntityMappings;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.MusicScores;
import uff.ic.lleme.entityrelatednesstestdata.v3.util.Pair;

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
                    Category category = SetOfCategories.getInstance().addCategory(mapping.label);
                    try {
                        Resource resource = SetOfResources.getInstance().addResource(mapping.entity1);
                        category.addSameAs(resource);
                    } catch (Exception e) {
                        System.out.println(String.format("Error: invalid sameAs resource. (category -> %1s, resource -> %1s)", mapping.label, mapping.entity1));
                    }
                    try {
                        Resource resource = SetOfResources.getInstance().addResource(mapping.entity2);
                        category.addSameAs(resource);
                    } catch (Exception e) {
                        System.out.println(String.format("Error: invalid sameAs resource. (category -> %1s, resource -> %1s)", mapping.label, mapping.entity2));
                    }
                } catch (Exception e) {
                    System.out.println(String.format("Error: invalid label. (property -> %1s)", mapping.label));
                }

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
                        Entity entity = SetOfEntities.getInstance().addEntity(mapping.label, mapping.type);
                        try {
                            Resource resource = SetOfResources.getInstance().addResource(mapping.entity1);
                            entity.addSameAs(resource);
                        } catch (Exception e) {
                            System.out.println(String.format("Error: invalid sameAs resource. (file -> %1s, entity -> %1s, resource -> %1s)", subset.getKey(), mapping.label, mapping.entity1));
                        }
                        try {
                            Resource resource = SetOfResources.getInstance().addResource(mapping.entity1);
                            entity.addSameAs(resource);
                        } catch (Exception e) {
                            System.out.println(String.format("Error: invalid sameAs resource. (file -> %1s, entity -> %1s, resource -> %1s)", subset.getKey(), mapping.label, mapping.entity2));
                        }
                    } catch (Exception e) {
                        System.out.println(String.format("Error: invalid label. (file -> %1s, entity -> %1s)", subset.getKey(), mapping.label));
                    }
        }

        {
        }

//        MusicRankedPaths MUSIC_RANKED_SCORES = new MusicRankedPaths();
//        MusicPropertyRelevanceScore MUSIC_PROPERTY_RELEVANCE_SCORE = new MusicPropertyRelevanceScore();
//        MusicEntityPairs MUSIC_ENTITY_PAIRS = new MusicEntityPairs();
//
//        MovieRankedPaths MOVIE_RANKED_PATHS = new MovieRankedPaths();
//        MoviePropertyRelevanceScore MOVIE_PROPERTY_RELEVANCE_SCORE = new MoviePropertyRelevanceScore();
//        MovieEntityPairs MOVIE_ENTITY_PAIRS = new MovieEntityPairs();
    }
}
