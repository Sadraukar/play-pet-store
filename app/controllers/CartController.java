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
import java.util.ArrayList;
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

    public CompletionStage<Result> cart(int userId) {
        CompletionStage<Result> result =
                getCartForUser(userId).thenApply(
                        c -> ok(views.html.cart.render(c.orElse(new ArrayList<CartEntry>()))));

        return result;
    }

    public CompletionStage<Optional<List<CartEntry>>> getCartForUser(int userId) {
        //  TODO: CHECK IF ID = CURRENT USER ID
        return this.cache.get(String.valueOf(userId));
    }

    public CompletionStage<Long> getCartCountForUser(int userId) {
        //  TODO: CHECK IF ID = CURRENT USER ID
        CompletionStage<Long> cartCountResult = this.cache
                .get(String.valueOf(HARDCODED_USER_ID))
                .thenApply(c -> c.stream().count());

        return cartCountResult;
    }

    public CompletionStage<Result> addToCartForUser(int userId, long petId) {
        CompletionStage<Done> result = getCartForUser(userId).thenCompose(c -> {
            //  The user doesn't have a cart.  Make a new one that contains the pet they are adding.
            if(c.isEmpty()) {
                List<CartEntry> newCart = new ArrayList<>();
                newCart.add(new CartEntry(DB.find(Pet.class, petId), 1));
                return this.cache.set(String.valueOf(userId), newCart);
            } else {
                //  User has an existing cart
                List<CartEntry> cart = c.get();
                //  Is this pet in the cart already?
                //  If so, increment the quantity of it.
                //  Otherwise, add a new entry for it with a quantity of 1.
                cart.stream()
                        .filter(ce -> ce.getPet().getId() == petId)
                        .findFirst()
                        .ifPresentOrElse(
                                ce -> ce.setQuantity(ce.getQuantity() + 1),
                                () -> cart.add(new CartEntry(DB.find(Pet.class, petId), 1))
                        );
                return  this.cache.set(String.valueOf(userId), cart);
            }
        });

        return result.thenApply(r -> ok());
    }

    public CompletionStage<Result> removeFromCartForUser(int userId, long petId) {
        CompletionStage<Done> result = getCartForUser(userId).thenCompose(c -> {
            //  The user doesn't have a cart.  How were they able to call remove on a pet when they don't have a cart?!
            if(c.isEmpty()) {
                LOGGER.warn("removeFromCartForUser was called for userId: " + userId + ", who has no cart.");
                return CompletableFuture.completedFuture(Done.getInstance());
            } else {
                //  User has a cart
                List<CartEntry> cart = c.get();
                //  Is this pet in the cart already?
                //  If so, remove it
                //  Otherwise, log a warning, because this method should not have been called for a pet that was not in the cart
                cart.stream()
                        .filter(ce -> ce.getPet().getId() == petId)
                        .findFirst()
                        .ifPresentOrElse(
                                ce -> cart.remove(ce),
                                () -> LOGGER.warn("removeFromCartForUser was called for petId: " + petId + ", but that pet is not in the user's cart.")
                        );
                return this.cache.set(String.valueOf(userId), cart);
            }
        });

        return result.thenApply(r -> ok());
    }

    public CompletionStage<Result> clearCartForUser(int userId) {
        CompletionStage<Done> result = cache.remove(String.valueOf(userId));
        return result.thenApply(r -> ok());
    }

//    private CompletionStage<BigDecimal> calculateSubTotal(CompletionStage<Optional<List<CartEntry>>> cart) {
//        CompletionStage<List<CartEntry>> cartList = cart.thenApply(c -> c.orElse(new ArrayList<CartEntry>()));
//        CompletionStage<BigDecimal> result = cartList.thenApply(c -> c.stream()
//                .map(ce -> ce.getPet().getPrice().multiply(BigDecimal.valueOf(ce.getQuantity())))
//                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));
//
//        return result;
//    }
//
//    private CompletionStage<BigDecimal> calculateTax(CompletionStage<BigDecimal> subTotal) {
//        CompletionStage<BigDecimal> result = subTotal.thenApply(st -> st.multiply(BigDecimal.valueOf(0.06)));
//
//        return result;
//    }
//
//    private CompletionStage<BigDecimal> calculateTotal(CompletionStage<BigDecimal> subTotal, CompletionStage<BigDecimal> tax) {
//        CompletionStage<BigDecimal> result = subTotal.thenApply(st -> st.add(tax.toCompletableFuture().join()));
//
//        return result;
//    }
}
