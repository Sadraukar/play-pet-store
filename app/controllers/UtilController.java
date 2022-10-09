package controllers;

import akka.Done;
import io.ebean.DB;
import models.Pet;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.cache.AsyncCacheApi;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UtilController extends Controller {
    final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private AsyncCacheApi cache;

    private List<Pet> SEED_PET_DATA = new ArrayList<>(Arrays.asList(
            new Pet("West Highland Terrier", Pet.PetType.Dog, "Commonly known as the \"Westie\".", new BigDecimal("500.00"), "PPS-001-D", "White"),
            new Pet("Cavalier King Charles Spaniel", Pet.PetType.Dog, "Highly affectionate, playful, extremely patient and eager to please. Dogs of this breed are good with children and other dogs.", new BigDecimal("600.00"), "PPS-002-D", "Brown"),
            new Pet("Dachshund", Pet.PetType.Dog, "A short-legged, long-bodied hound known as the \"wiener dog\".", new BigDecimal("350.00"), "PPS-003-D", "Black"),
            new Pet("German Shepherd", Pet.PetType.Dog, "Working breed known for its intelligence.", new BigDecimal("650.00"), "PPS-004-D", "Brown"),
            new Pet("Golden Retriever", Pet.PetType.Dog, "Characterised by a gentle and affectionate nature and a striking golden coat.", new BigDecimal("650.00"), "PPS-005-D", "Gold"),
            new Pet("Boston Terrier", Pet.PetType.Dog, "Highly intelligent and easily trained, this breed is known for its small, compact frame.", new BigDecimal("450.00"), "PPS-006-D", "White"),
            new Pet("Jack Russel Terrier", Pet.PetType.Dog, "An energetic breed that rely on a high level of exercise and stimulation.", new BigDecimal("400.00"), "PPS-007-D", "Brown"),
            new Pet("Pekingese", Pet.PetType.Dog, "A toy breed favored by Chinese royalty.", new BigDecimal("500.00"), "PPS-008-D", "Brown"),
            new Pet("Mixed Breed", Pet.PetType.Dog, "A mixture of German Shepard and Basenji known for its photogenic appearance.", new BigDecimal("500.00"), "PPS-030-D", "Brown"),
            new Pet("American Pit Bull Terrier", Pet.PetType.Dog, "Famous Pit Bulls include Sergeant Stubby, Pete the Pup, and Sallie Ann Jarrett.", new BigDecimal("500.00"), "PPS-031-D", "Brown"),
            new Pet("Siamese", Pet.PetType.Cat, "A distinctive breed native in Thailand, this cat was one of the most popular in Europe during the 19th century.", new BigDecimal("500.00"), "PPS-009-C", "White"),
            new Pet("British Shorthair", Pet.PetType.Cat, "Possesses a distinctively stocky body, dense coat, and broad face.", new BigDecimal("450.00"), "PPS-010-C", "Grey"),
            new Pet("Persian", Pet.PetType.Cat, "A long-haired breed of cat characterized by a round face and short muzzle.", new BigDecimal("600.00"), "PPS-011-C", "Gold"),
            new Pet("Sphynx", Pet.PetType.Cat, "This breed is hairless due to a naturally-occuring genetic mutation.", new BigDecimal("750.00"), "PPS-012-C", "Grey"),
            new Pet("American Shorthair", Pet.PetType.Cat, "Descended from European cats brought to North America by early settlers to protect valuable cargo from mice and rats.", new BigDecimal("350.00"), "PPS-013-C", "Orange"),
            new Pet("Tortoiseshell", Pet.PetType.Cat, "Not a breed, but a cat coat coloring.  Occurs in many different breeds.  Almost exclusively female.", new BigDecimal("350.00"), "PPS-032-C", "Black"),
            new Pet("Green Cheek Conure", Pet.PetType.Bird, "Prized as household pets and companion parrots throughout the world and are relatively easy to breed.", new BigDecimal("150.00"), "PPS-014-B", "Green"),
            new Pet("African Grey Parrot", Pet.PetType.Bird, "A medium-sized, predominantly grey, black-billed parrot.  May live for 40–60 years in captivity.", new BigDecimal("450.00"), "PPS-015-B", "Grey"),
            new Pet("Budgerigar", Pet.PetType.Bird, "Commonly known as the shell parakeet, this breed is native to Australia", new BigDecimal("150.00"), "PPS-016-B", "Blue"),
            new Pet("Canary", Pet.PetType.Bird, "A small songbird of the finch family.  Brought to Europe from the Macronesian Islands in 17th century by Spanish sailors.", new BigDecimal("200.00"), "PPS-017-B", "Yellow"),
            new Pet("Amazon Parrot", Pet.PetType.Bird, "Many of these birds possess the ability to mimic human speech", new BigDecimal("400.00"), "PPS-018-B", "Green"),
            new Pet("Scarlet Macaw", Pet.PetType.Bird, "The national bird of Honduras, this breed is popular for its striking plumage.", new BigDecimal("500.00"), "PPS-019-B", "Red"),
            new Pet("Axolotl", Pet.PetType.Amphibian, "An unusual amphibian that undergoes metamorphisis to reach adulthood.", new BigDecimal("75.00"), "PPS-020-A", "White"),
            new Pet("Rainbow Tree Frog", Pet.PetType.Amphibian, "Excellent jumpers that spend most of their lives in trees.", new BigDecimal("20.00"), "PPS-021-A", "Green"),
            new Pet("American Toad", Pet.PetType.Amphibian, "Produces a poison that is non-lethal to humans, but should be handled with care.", new BigDecimal("40.00"), "PPS-022-A", "Green"),
            new Pet("Pickerel Frog", Pet.PetType.Amphibian, "characterized by the appearance of seemingly \"hand-drawn\" squares on its dorsal surface.", new BigDecimal("10.00"), "PPS-023-A", "Green"),
            new Pet("Poison Dart Frog", Pet.PetType.Amphibian, "A poisonous frog, their venom was used by Native American tribes for crafting darts.", new BigDecimal("35.00"), "PPS-024-A", "Blue"),
            new Pet("Box Turtle", Pet.PetType.Reptile, "Possess a massive shell on their backs which can be used for protection from predators.", new BigDecimal("100.00"), "PPS-025-R", "Green"),
            new Pet("Bearded Dragon", Pet.PetType.Reptile, "When threatened, will puff up their bodies and beards to ward off predators.", new BigDecimal("120.00"), "PPS-026-R", "Tan"),
            new Pet("Ball Python", Pet.PetType.Reptile, "Arguably the most popular pet snake.  Can live up to 35 years in captivity.", new BigDecimal("180.00"), "PPS-027-R", "Black"),
            new Pet("Amethystine Python", Pet.PetType.Reptile, "Use caution when handling due to their weight and size.  Feeding requires handling of mice.", new BigDecimal("250.00"), "PPS-028-R", "Brown"),
            new Pet("Red Eared Slider", Pet.PetType.Reptile, "One of the most popular pet turtles.  Unable to regulate their body temperature, they are frequently found sunbathing.", new BigDecimal("25.00"), "PPS-029-R", "Green")
    ));

    private List<User> SEED_USER_DATA = new ArrayList<>(Arrays.asList(
            new User("System Administrator", "Bdt2ybXr", "sysadmin@gregtaylor.net")
    ));

    @Inject
    public UtilController(AsyncCacheApi cache) {
        this.cache = cache;
    }

    /**
     * Render the utils page
     * @return
     */
    public Result utils() {
        return ok(views.html.utils.render());
    }

    /**
     * Seed database with sample data.
     * @return
     */
    public CompletableFuture<Result> seedDatabase() {
        LOGGER.debug("Seeding database...");
        return CompletableFuture.supplyAsync(() -> {
            seedPets();
            seedUsers();

            LOGGER.debug("Database seeded!");
            return ok();
        });
    }

    /**
     * Clear pet and users tables
     * @return
     */
    public CompletableFuture<Result> clearDatabase() {
        LOGGER.debug("Clearing database...");
        return CompletableFuture.supplyAsync(()-> {
            DB.beginTransaction();
            DB.deleteAll(DB.find(Pet.class).findList());
            DB.deleteAll(DB.find(User.class).findList());
            DB.endTransaction();

            LOGGER.debug("Database cleared!");
            return ok();
        });
    };

    private CompletableFuture<Void> seedPets() {
        LOGGER.debug("Seeding 'pet' table...");
        return CompletableFuture.runAsync(() -> {
            DB.insertAll(SEED_PET_DATA);
        });
    }

    private CompletableFuture<Void> seedUsers() {
        LOGGER.debug("Seeding 'users' table...");
        return CompletableFuture.runAsync(() -> {
            DB.insertAll(SEED_USER_DATA);
        });
    }

    public CompletionStage<Result> clearApplicationCartCache() {
        LOGGER.info("Clearing application cart cache...");
        CompletionStage<Done> result = cache.removeAll();
        return result
                .thenRun(() -> LOGGER.info("Application cart cache has been cleared!"))
                .thenApply(r -> ok());
    }
}
