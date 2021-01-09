package Elements;

import Utils.Config;
import Utils.TrackStatus;

import java.util.LinkedList;
import java.util.List;

public class Herd extends AbstractWorldMapElement {

    private final LinkedList<Animal> members = new LinkedList<>();  // może SortedSet?
    private Grass foodSupplies;

    public Herd(Animal animal, Vector2d pos) {
        this.members.add(animal);
        this.position = pos;
    }

    public LinkedList<Animal> getStrongestMembers() {
        LinkedList<Animal> strongest = new LinkedList<>();

        this.members.sort(Animal::compareEnergy);
        int maxEnergy = members.get(0).getEnergy();

        for (Animal member : members) {
            if (member.getEnergy() == maxEnergy)
                strongest.add(member);
            else break;
        }
        return strongest;
    }

    // Pick two strongest animal who are able to procreate
    public List<Animal> pickParentsToBe() {
        if (this.countMembers() >= 2) {
            this.members.sort(Animal::compareEnergy);

            if (members.get(0).canProcreate(members.get(1)))
                return members.subList(0, 2);   // a jeśli więcej zwierząt ma równą energię?
        }
        return null;
    }

    public boolean hasGrass() {
        return foodSupplies != null;
    }

    // Split grass among the strongest animals from herd
    public void splitGrass() {
        LinkedList<Animal> strongest = getStrongestMembers();
        int portion = Config.getPlantEnergy() / strongest.size();

        for (Animal member : strongest)
            member.eat(portion);

        this.foodSupplies = null;
    }

    public void removeMember(Animal animal) {
        if (this.hasMember(animal))
            members.remove(animal);
    }

    public void addMember(Animal animal) {
        if (animal.getPosition().equals(this.position))
            members.add(animal);
    }

    // If there is animal with dominant genotype return it first
    public Animal getAnimalWithDominantGenToDraw(Genotype dominant){
        for (Animal member : members)
            if (member.getGenotype() == dominant)
                return member;
        return getAnimalToDraw();
    }

    // If in herd is any tracked animal return it, accordingly to tracking priority: main > child > grandchild
    // if there are not any tracked return strongest
    public Animal getAnimalToDraw() {
        Animal tracked = null;
        for (Animal member : members)
            if (member.checkTrackStatus() == TrackStatus.MAIN)
                return member;
            else if (member.checkTrackStatus() == TrackStatus.CHILD)
                tracked = member;
            else if (member.checkTrackStatus() == TrackStatus.GRANDCHILD && tracked == null)
                tracked = member;

        if(tracked != null) return tracked;
        else return getStrongestMembers().get(0);
    }

    public void addGrass(Grass grass) {
        this.foodSupplies = grass;
    }

    private boolean hasMember(Animal animal) {
        return members.contains(animal);
    }

    public int countMembers() {
        return members.size();
    }
}
