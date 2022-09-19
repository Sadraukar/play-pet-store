package controllers;

import io.ebean.DB;
import io.ebean.Expr;
import io.ebean.Expression;
import io.ebean.ExpressionList;
import io.ebean.Query;
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

public class SearchController extends Controller {
    final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //  The columns searched during a keyword search
    private List<String> KEYWORD_SEARCH_COLUMNS = new ArrayList<>(Arrays.asList("name", "petType", "description", "color"));

    /**
     * @param criteria Key-value pair of what to search for.  For example, an entry of <"petType","Dog"> will search for all pets with a type of "Dog".
     * @return Search results
     */
    public List<Pet> search(Map<String, String> criteria) {
        ExpressionList<Pet> expressionList = DB.find(Pet.class)
                .where()
                .or();

        criteria.entrySet().stream()
                .forEach(c -> expressionList.add(Expr.eq(c.getKey(), c.getValue())));

        return expressionList.orderBy("productId").findList();
    }

    public Result searchPetByType(String petType) {
        //  Check that the petType requested is a valid one
        Arrays.stream(Pet.PetType.values())
                .filter(pt -> pt.name().equalsIgnoreCase(petType))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("petType", petType);

        List<Pet> searchResults = search(searchCriteria);

        return ok(views.html.petList.render(searchResults));
    }

    /**
     * Search a number of different columns for pets.  Searches:
     *  - name
     *  - petType
     *  - description
     *  - color
     * @param keyword The keyword to use for searching
     * @return
     */
    public Result searchPetByKeyword(String keyword) {
        LOGGER.debug("Initiating keyword search for: " + keyword);
        Map <String, String> searchCriteria = new HashMap<>();
        KEYWORD_SEARCH_COLUMNS.forEach((c) ->
                {
                    //  PetType is an enum, we need to make sure that the keyword is valid if we are going to use it for
                    //  searching on PetType
                    if(c.equals("petType")) {
                        if(Arrays.stream(Pet.PetType.values())
                                .anyMatch(pt -> pt.toString().equalsIgnoreCase(keyword))) {
                            searchCriteria.put(c, keyword);
                        } else {
                            LOGGER.debug("Excluding keyword from petType search, not a valid petType: " + keyword);
                        }
                    } else {
                        //  For other non-enum columns, add the keyword
                        searchCriteria.put(c, keyword);
                    }
                });
        List<Pet> searchResults = search(searchCriteria);

        return ok(views.html.petList.render(searchResults));
    }

//    public Result renderSearchView() {
//        return ok(views.html.petList.render());
//    }
}
