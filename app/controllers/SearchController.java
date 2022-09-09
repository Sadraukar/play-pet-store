package controllers;

import io.ebean.DB;
import io.ebean.Expression;
import models.Pet;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class SearchController extends Controller {
    public Result searchPetByType(Pet.PetType petType) {

        List<Pet> results = DB.find(Pet.class).where()
                .eq("petType", petType)
                .findList();

        return Json.;
    }
}
