package Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Genotype {

    private final ArrayList<Integer> gens = new ArrayList<>();
    private final Random generator = new Random();

    // Adam or Eve genotype | random
    public Genotype() {
        for (int i = 0; i < 32; i++)
            this.gens.add(generator.nextInt(8));
        Collections.sort(this.gens);
    }

    // Create new genotype based on parents genotypes
    public Genotype(Genotype strong, Genotype weak) {
        int firstIdx = generator.nextInt(29) + 1;
        int secondIdx = generator.nextInt((30 - firstIdx) + 1) + firstIdx;

        this.gens.addAll(strong.gens.subList(0, firstIdx));
        this.gens.addAll(weak.gens.subList(firstIdx, secondIdx));
        this.gens.addAll(strong.gens.subList(secondIdx, 32));

        this.verify();
        Collections.sort(this.gens);
    }

    // Random rotation based on genotype
    public int getGeneticRotation() {
        return gens.get(generator.nextInt(32));
    }

    // Verify if genotype is proper(it should contain all possible rotations)
    // of it's not repair it
    private void verify() {
        boolean improper;
        do {
            improper = false;
            for (int i = 0; i < 8; i++)
                if (!this.gens.contains(i)) {
                    improper = true;
                    this.gens.remove(generator.nextInt(32));
                    this.gens.add(i);
                }
        } while (improper);
    }

    // Print genotype as more compact form (Gen : count)
    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        int[] gensCount = new int[8];
        for (int i = 0; i < 32; i++) {
            gensCount[this.gens.get(i)]++;
        }
        for (int i = 0; i < 8; i++) {
            toString.append(i).append(":").append(gensCount[i]).append("  ");
        }
        return toString.toString();
    }

    // equals and hashcode overridden to create hashmap based on gens values only
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Genotype))
            return false;
        Genotype that = (Genotype) o;
        return this.gens.equals(that.gens);
    }

    @Override
    public int hashCode() {
        return this.gens.hashCode();
    }
}
