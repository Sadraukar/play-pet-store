package controllers;

import akka.Done;
import io.ebean.DB;
import models.CartEntry;
import models.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.cache.AsyncCacheApi;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CartController extends Controller {
    //  Until we have a true multi-user system implementation, use this value for the user ID.
    private long HARDCODED_USER_ID = 1;
    private AsyncCacheApi cache;

    final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Inject
    public CartController(AsyncCacheApi cache) {
        this.cache = cache;
    }

    public CompletionStage<Optional<List<CartEntry>>> getCartForUser(int userId) {
        //  TODO: CHECK IF ID = CURRENT USER ID
        return this.cache.get(String.valueOf(HARDCODED_USER_ID));
    }

    public CompletionStage<Long> getCartCountForUser(int userId) {
        //  TODO: CHECK IF ID = CURRENT USER ID
        CompletionStage<Long> cartCountResult = this.cache
                .get(String.valueOf(HARDCODED_USER_ID))
                .thenApply(c -> c.stream().count());

        return cartCountResult;
    }

    public CompletionStage<Result> addToCartForUser(int userId, long petId) {
        CompletionStage<Done> result = this.cache.set(String.valueOf(HARDCODED_USER_ID), DB.find(Pet.class, petId));
        return result.thenApply(r -> ok());
    }

    public CompletionStage<Result> clearApplicationCartCache() {
        LOGGER.info("Clearing application cart cache...");
        CompletionStage<Done> result = cache.removeAll();
        return result
                .thenRun(() -> LOGGER.info("Application cart cache has been cleared!"))
                .thenApply(r -> ok());
    }

    public CompletionStage<Result> clearCartForUser(int userId) {
        CompletionStage<Done> result = cache.remove(String.valueOf(userId));
        return result.thenApply(r -> ok());
    }
}
