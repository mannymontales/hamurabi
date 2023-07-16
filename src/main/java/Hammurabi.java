import java.util.InputMismatchException;
import java.util.Random;         // imports go here
import java.util.Scanner;
public class Hammurabi {
    Random rand = new Random();  // this is an instance variable
    Scanner scanner = new Scanner(System.in);
    boolean sanityCheck;
    int startingPop = 95;
    int startingBush = 3000;
    int startingYield = 3;

    public static void main(String[] args) { // required in every Java program
        new Hammurabi().playGame();
    }

    void playGame() {
        // declare local variables here: grain, population, etc.
        // statements go after the declations
        int immigrants = 5;
        int starvedPeople = 0;
        int ratsEatingBread = 200;
        int landValue = 19;
        int year = 1;
        int plantedAcres = 1;
        boolean result = false;
        int yield = 0;
        int totalPopulation = 95 + immigrants - starvedPeople;
        int storedBushels = 3000 - ratsEatingBread;
        int totalAcresOfLand = 1000;

        printIntro();
        while (year <= 3 && result != true) {

            //how many acres to buy
            totalAcresOfLand += askHowManyAcresToBuy(landValue, storedBushels);

            //how many acres to sell
            totalAcresOfLand -= askHowManyAcresToSell(totalAcresOfLand);

            //feed people bushels
            storedBushels -= askHowMuchGrainToFeedPeople(storedBushels);

            //how many acres to plant
            plantedAcres = askHowManyAcresToPlant(totalAcresOfLand, totalPopulation, storedBushels);

            //plague death
            totalPopulation = plagueDeaths(totalPopulation);

            //starvation death
            starvedPeople = starvationDeaths(totalPopulation, storedBushels);

            //uprising true or false
            result = uprising(totalPopulation, starvedPeople);

            //calculate immigrates
            //immigrants = immigrants(totalPopulation, totalAcresOfLand, storedBushels);

            //harvest
            yield = harvest(plantedAcres);
            storedBushels += yield;

            //rats eating bread
            ratsEatingBread = grainEatenByRats(storedBushels);
            storedBushels -= ratsEatingBread;

            //calc cost of land
            landValue = newCostOfLand();

            printSummary(year, starvedPeople, immigrants, totalPopulation, plantedAcres, yield, ratsEatingBread, storedBushels, totalAcresOfLand, landValue);

            year++;
        }
    }

    //other methods go here

    /**
     * Prints the given message (which should ask the user for some integral
     * quantity), and returns the number entered by the user. If the user's
     * response isn't an integer, the question is repeated until the user
     * does give a integer response.
     *
     * @param message The request to present to the user.
     * @return The user's numeric response.
     */
    int getNumber(String message) {
        while (true) {
            System.out.print(message);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\"" + scanner.next() + "\" isn't a number!");
            }
        }
    }

    //Asks the player how many acres of land to buy, and returns that number. You must have enough grain to pay for your purchase.
    int askHowManyAcresToBuy(int price, int bushels) {
        sanityCheck = false;

        while (sanityCheck != true) {
            int userChoice = getNumber("\nHOW MANY ACRES DO YOU WISH TO BUY? ");

            if (userChoice * price < bushels) {
                sanityCheck = true;
                return userChoice;
            } else {
                System.out.println("HAMURABI:  THINK AGAIN. YOU HAVE ONLY\n" +
                        bushels + " BUSHELS OF GRAIN. NOW THEN,");
            }
        }
        return 0;
    }

    //Asks the player how many acres of land to sell, and returns that number. You can't sell more than you have.
    //Do not ask this question if the player is buying land; it doesn't make sense to do both in one turn.
    int askHowManyAcresToSell(int acresOwned) {
        sanityCheck = false;

        while (sanityCheck != true) {
            int userChoice = getNumber("\nHOW MANY ACRES DO YOU WISH TO SELL? ");

            if (userChoice < acresOwned) {
                sanityCheck = true;
                return userChoice;
            } else {
                System.out.println("HAMURABI:  THINK AGAIN. YOU OWN ONLY " + acresOwned + " ACRES. NOW THEN,");
            }

        }
        return 0;
    }

    //Ask the player how much grain to feed people, and returns that number. You can't feed them more grain than you have. You can feed them more than they need to survive.
    int askHowMuchGrainToFeedPeople(int bushels) {
        sanityCheck = false;

        while (sanityCheck != true) {
            int userChoice = getNumber("\nHOW MANY BUSHELS DO YOU WISH TO FEED YOUR PEOPLE? ");

            if (userChoice < bushels) {
                sanityCheck = true;
                return userChoice;
            } else {
                System.out.println("HAMURABI:  THINK AGAIN. YOU HAVE ONLY\n" +
                        bushels + " BUSHELS OF GRAIN. NOW THEN,");
            }
        }
        return 0;
    }

    //Ask the player how many acres to plant with grain, and returns that number.
    // You must have enough acres, enough grain, and enough people to do the planting. Any grain left over goes into storage for next year.
    int askHowManyAcresToPlant(int acresOwned, int population, int bushels) {
        sanityCheck = false;

        while (sanityCheck != true) {
            int userChoice = getNumber("\nHOW MANY ACRES DO YOU WISH TO PLANT WITH SEED? ");

            if (acresOwned <= userChoice) {
                System.out.println("HAMURABI:  THINK AGAIN. YOU OWN ONLY " + acresOwned + " ACRES. NOW THEN,");
            }
            if (bushels < userChoice) {
                System.out.println("HAMURABI:  THINK AGAIN. YOU HAVE ONLY\n" + bushels + " BUSHELS OF GRAIN. NOW THEN,");
            }
            if (population * 10 <= userChoice) {
                System.out.println("BUT YOU HAVE ONLY " + population + " PEOPLE TO TEND THE FIELDS. NOW THEN,");
            }
            if (acresOwned > userChoice) {
                sanityCheck = true;
                return userChoice;
            }
        }
        return 0;
    }

    //Each year, there is a 15% chance of a horrible plague. When this happens, half your people die. Return the number of plague deaths (possibly zero).
    int plagueDeaths(int population){
        int randomChance = rand.nextInt(100);

        if (randomChance < 16){
            return population / 2;
        } else {
            return population;
        }
    }

    //Each person needs 20 bushels of grain to survive. If you feed them more than this, they are happy, but the grain is still gone.
    // You don't get any benefit from having happy subjects. Return the number of deaths from starvation (possibly zero).
    int starvationDeaths(int population, int bushelsFedToPeople){
        int requiredBushels = population * 20;
        if (requiredBushels < bushelsFedToPeople){
            return 0;
        } else {
            int starved = (population - (bushelsFedToPeople / 20));
            return starved;
        }
    }

    //Return true if more than 45% of the people starve. (This will cause you to be immediately thrown out of office, ending the game.)
    boolean uprising(int population, int howManyPeopleStarved){
        float percentStarved = 0;
        percentStarved = (float) howManyPeopleStarved / population * 100;
        if (percentStarved > 45){
            return true;
        } else {
            return false;
        }
    }

    int immigrants(int population, int acresOwned, int grainInStorage){
        int immigrants = (20 * acresOwned + grainInStorage) / (100 * population) + 1;
        return immigrants;
    }

    //Choose a random integer between 1 and 6, inclusive.
    // Each acre that was planted with seed will yield this many bushels of grain.
    // (Example: if you planted 50 acres, and your number is 3, you harvest 150 bushels of grain).
    // Return the number of bushels harvested.
    int harvest(int acres){
        int harvest = (int) (6 * Math.random() + 1);
        int total = acres * harvest;
        return total;
    }

    //There is a 40% chance that you will have a rat infestation.
    // When this happens, rats will eat somewhere between 10% and 30% of your grain.
    // Return the amount of grain eaten by rats (possibly zero).
    int grainEatenByRats(int bushels){
        int randomChance = rand.nextInt(100);

        if (randomChance < 41){
            int rats = rand.nextInt(30 - 10 + 1) + 10;
            //getting the decimals first before multiplying to total bushels
            float percentage = (float) rats / 100;

            return (int) (bushels * percentage);
        } else {
            return 0;
        }
    }

    //The price of land is random, and ranges from 17 to 23 bushels per acre.
    //Return the new price for the next set of decisions the player has to make. (The player will need this information in order to buy or sell land.)
    int newCostOfLand(){

        int cost = rand.nextInt(23 - 17 + 1) + 17;
        return cost;
    }

    public void printSummary(int year, int starvedPeople, int immigrants, int totalPopulation, int plantedAcres, int yield, int ratsEatingBread, int totalBushels,
                             int totalAcresOfLand, int landValue) {
        System.out.println();
        System.out.println("O great Hammurabi! ");
        System.out.println("You are in year " + year + " of your ten year rule. ");
        System.out.println("In the previous year " + starvedPeople + " people starved to death.");
        System.out.println("In the previous year " + immigrants + " people entered the kingdom.");
        System.out.println("The population is now " + totalPopulation);
        System.out.println("We harvested " + yield + " at " + (yield / plantedAcres) + " bushels per acre. ");
        System.out.println("Rats destroyed " + ratsEatingBread + " bushels leaving " + totalBushels + " in storage.");
        System.out.println("The city owns " + totalAcresOfLand + " acres of land");
        System.out.println("Land is currently worth " + landValue + " bushels per acre.");
    }

    public void printIntro(){
        System.out.println("O great Hammurabi! ");
        System.out.println("You are in year 0 of your ten year rule. ");
        System.out.println("In the previous year 0 people starved to death. ");
        System.out.println("In the previous year 5 people entered the kingdom. ");
        System.out.println("The population is now 100. ");
        System.out.println("We harvested 3000 bushels at 3 bushels per acre. ");
        System.out.println("Rats destroyed 200 bushels, leaving 2800 bushels in storage. ");
        System.out.println("The city owns 1000 acres of land. ");
        System.out.println("Land is currently worth 19 bushels per acre. ");
    }

}
