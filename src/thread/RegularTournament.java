package thread;

import Graphics.CompetitionFrame;
import animals.Animal;

import java.util.concurrent.atomic.AtomicBoolean;

public class RegularTournament extends Tournament {
    AtomicBoolean startSignal;
    Scores scores;

    public RegularTournament(Animal[][] animals, CompetitionFrame frame, int index) {
        super(animals, frame, index);
    }

    public double calcNeededDistance(Animal animal, int animalIndex) {
        double neededDistance = 0;
        if (animal.getFamilyType().contains("Water")) {
            neededDistance = animal.calcDistance(CompetitionFrame.getEndPointWater()[animalIndex]) * 2;
        } else if (animal.getFamilyType().contains("Air")) {
            neededDistance = animal.calcDistance(CompetitionFrame.getEndPoint()[animalIndex]) * 2;
        } else if (animal.getFamilyType().contains("Terr")) {
            neededDistance = animal.calcDistance(CompetitionFrame.getEndPoint()[animalIndex]) * 2 + animal.calcDistance(CompetitionFrame.getStartPoint()[animalIndex + 1]) * 2;
        }
        return neededDistance;

    }


    @Override
    public void setup(Animal[][] animals) {
        System.out.println("RegularTour setup");
        scores = new Scores();
        startSignal = new AtomicBoolean(false);
        AnimalThread[][] animalThread = new AnimalThread[animals.length][];


        animalThread[super.tourIndex] = new AnimalThread[animals[super.tourIndex].length];
        for (int j = 0; j < animals[super.tourIndex].length; j++) {
            System.out.println("RegularTour setup Loop " + super.tourIndex + " " + j + "Build animal");
            AtomicBoolean endSignal = new AtomicBoolean(false);
            endSignal.set(false);
            Animal tempAnimal = animals[super.tourIndex][j];
            Referee ref = new Referee(animals[super.tourIndex][j].getName(), scores, endSignal);
            animalThread[super.tourIndex][j] = new AnimalThread(tempAnimal, calcNeededDistance(tempAnimal, j), startSignal, endSignal, ref,true,this);
            Thread animalThreads = new Thread(animalThread[super.tourIndex][j],tempAnimal.getName());
            animalThreads.start();
            Thread refThread = new Thread(ref);
            refThread.start();
            tempAnimal.setThread(animalThreads);

        }
        super.frame.setAnimalVector(animals[super.tourIndex]);
        TournamentThread thread = new TournamentThread(animalThread, scores, startSignal, tourIndex,animals[super.tourIndex].length);
        Thread t = new Thread(thread);
        System.out.println("Thread t = new Thread(thread); i am here" );
        super.setTournamentThread(t);
        t.start();
    }

    @Override
    public void notifyNextAnimal(int x) {

    }


}
