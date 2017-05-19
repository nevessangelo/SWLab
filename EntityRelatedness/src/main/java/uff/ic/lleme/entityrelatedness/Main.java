package uff.ic.lleme.entityrelatedness;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        MusicEntityPairs musicEntityPairs = new MusicEntityPairs();
        MovieEntityPairs movieEntityPairs = new MovieEntityPairs();
        MovieClassMapping movieClassMapping = new MovieClassMapping();
        MusicClassMapping musicClassMapping = new MusicClassMapping();

        MovieEntityMappings movieEntityMapping = new MovieEntityMappings();
        MoviePropertyRelevanceScore moviePropertyRelevanceScore = new MoviePropertyRelevanceScore();
        MovieRankedPaths movieRankedPaths = new MovieRankedPaths();
        MovieScores movieScores = new MovieScores();

        MusicEntityMappings musicEntityMapping = new MusicEntityMappings();
        MusicPropertyRelevanceScore musicPropertyRelevanceScore = new MusicPropertyRelevanceScore();
        MusicRankedPaths musicRankedPaths = new MusicRankedPaths();
        MusicScores musicScores = new MusicScores();

        System.out.println("Fim.");

    }
}
