package controllers;

import io.ebean.DB;
import io.ebean.Expr;
import io.ebean.ExpressionList;
import models.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class SearchController extends Controller {
    final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //  The columns searched during a keyword search
    private List<String> KEYWORD_SEARCH_COLUMNS = new ArrayList<>(Arrays.asList("name", "description", "color"));

    /**
     * @param criteria Key-value pair of what to search for.  For example, an entry of <"petType","Dog"> will search for all pets with a type of "Dog".
     * @return Search results
     */
    public CompletionStage<List<Pet>> search(Map<String, String> criteria) {
        ExpressionList<Pet> expressionList = DB.find(Pet.class)
                .where()
                .or();

        criteria.entrySet().stream()
                .forEach(c -> {
                    if(c.getKey().equals("description") || c.getKey().equals("name")) {
                        expressionList.add(Expr.icontains(c.getKey(), c.getValue()));
                    } else {
                        expressionList.add(Expr.ieq(c.getKey(), c.getValue()));
                    }
                });

        //  Wrap the blocking "findList" call in a CompletableFuture
        return CompletableFuture.supplyAsync(() -> expressionList.orderBy("productId").findList());
    }

    public CompletionStage<Result> searchPetByType(String petType) {
        //  Check that the petType requested is a valid one
        Pet.PetType petTypeEnum = Arrays.stream(Pet.PetType.values())
                .filter(pt -> pt.name().equalsIgnoreCase(petType))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        //  Wrap the blocking "findList" call in a CompletableFuture
        CompletionStage<List<Pet>> searchResults = CompletableFuture.supplyAsync(() -> DB.find(Pet.class)
                .where()
                .eq("petType", petTypeEnum)
                .findList());

        return searchResults.thenApply(r -> ok(views.html.petList.render(r)));
    }

    /**
     * Search a number of different columns for pets.  Searches:
     *  - name
     *  - description
     *  - color
     *  - pet type
     * @param keyword The keyword to use for searching
     * @return
     */
    public CompletionStage<Result> searchPetByKeyword(String keyword) {
        LOGGER.debug("Initiating keyword search for: " + keyword);

        //  If the keyword is a pet type, search by pet type
        Optional<Pet.PetType> petTypeKeyword =  Arrays.stream(Pet.PetType.values())
                .filter(pt -> pt.name().equalsIgnoreCase(keyword))
                .findFirst();
        if(petTypeKeyword.isPresent()) {
            return searchPetByType(petTypeKeyword.get().toString());
        } else {
            Map <String, String> searchCriteria = new HashMap<>();
            KEYWORD_SEARCH_COLUMNS.forEach((c) -> {
                searchCriteria.put(c, keyword);
            });
            CompletionStage<List<Pet>> searchResults = search(searchCriteria);

            return searchResults.thenApply(r -> ok(views.html.petList.render(r)));
        }
    }
}
