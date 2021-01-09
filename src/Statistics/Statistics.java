package Statistics;

import Elements.Genotype;

import java.util.HashMap;

public class Statistics {
    protected int grassCounter;
    protected int animalCounter;
    protected Genotype dominantGenotype;
    protected double avgChildrenNumber;
    protected double avgEnergyNumber;
    protected double avgLifeSpan;
    protected int deathsCounter;
    protected int days;
    protected final HashMap<Genotype, Integer> gens;

    public Statistics() {
        grassCounter = 0;
        animalCounter = 0;
        dominantGenotype = null;
        avgChildrenNumber = 0;
        avgEnergyNumber = 0;
        avgLifeSpan = 0;
        deathsCounter = 0;
        days = 0;
        gens = new HashMap<>();
    }

    // Add gen to statistics
    protected void addGen(Genotype gen) {   // gene
        int count = 1;
        if (gens.containsKey(gen))
            count += gens.get(gen);
        gens.put(gen, count);
    }


    // Remove gen from statistics
    protected void removeGen(Genotype gen) {
        int count = gens.get(gen);
        if (count > 1) {
            count--;
            gens.put(gen, count);
        } else gens.remove(gen);
    }

    // Find dominant genotype among living animals
    protected Genotype getDominantGen() {
        int max = 0;
        Genotype dominant = null;
        for (Genotype gen : gens.keySet())
            if (gens.get(gen) > max) {
                max = gens.get(gen);
                dominant = gen;
            }
        return dominant;
    }

    protected String getStatisticsString(Statistics that) {
        return String.format("Day %d\n\nCurrent grass number: %d \n" +
                        "Current animal number: %d\n" +
                        "Dominant genotype(gen:count):\n%s\n" +
                        "Average children number: %.2f\n" +
                        "Average energy number: %.2f\n" +
                        "Average lifespan: %.2f\n" +
                        "Death counter:  %d", that.days, that.grassCounter, (that.animalCounter - that.deathsCounter),
                that.dominantGenotype, that.avgChildrenNumber, that.avgEnergyNumber,
                that.avgLifeSpan, that.deathsCounter);
    }
}
