package models;

import io.ebean.DB;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;

public class PetTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testInsertFindDelete() {
        Pet pet = new Pet();
        pet.setName("Sparky");

        // insert the customer in the DB
        DB.save(pet);

        // Find by Id
        Pet foundSparky = DB.find(Pet.class, 1);

        // delete the customer
        DB.delete(pet);
    }
}
