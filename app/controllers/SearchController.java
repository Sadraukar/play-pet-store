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

public class SearchController extends Controller {
    final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //  The columns searched during a keyword search
    private List<String> KEYWORD_SEARCH_COLUMNS = new ArrayList<>(Arrays.asList("name", "description", "color"));

    /**
     * @param criteria Key-value pair of what to search for.  For example, an entry of <"petType","Dog"> will search for all pets with a type of "Dog".
     * @return Search results
     */
    public List<Pet> search(Map<String, String> criteria) {
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
        return expressionList.orderBy("productId").findList();
    }

    public Result searchPetByType(String petType) {
        //  Check that the petType requested is a valid one
        Pet.PetType petTypeEnum = Arrays.stream(Pet.PetType.values())
                .filter(pt -> pt.name().equalsIgnoreCase(petType))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        List<Pet> searchResults = DB.find(Pet.class)
                .where()
                .eq("petType", petTypeEnum)
                .findList();

        return ok(views.html.petList.render(searchResults));
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
    public Result searchPetByKeyword(String keyword) {
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
            List<Pet> searchResults = search(searchCriteria);

            return ok(views.html.petList.render(searchResults));
        }
    }
}
