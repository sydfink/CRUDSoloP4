package com.galvanzie.SoloP4;


import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class RecipeController {
    RecipeRepository rr;

    public RecipeController(RecipeRepository rr) {
        this.rr = rr;
    }

    @PostMapping("/recipe")
    public Recipe createNewRecipe(@RequestBody Recipe recipe){
        return rr.save(recipe);
    }

    @GetMapping("/recipe")
    public Iterable<Recipe> getAllRecipes(){
        return rr.findAll();
    }

    @GetMapping("/recipe/{id}")
    public Optional<Recipe> getOneRecipe(@PathVariable Long id){
        return rr.findById(id);
    }

    @PatchMapping("/recipe/{id}")
    public Recipe updateOneRecipe(@PathVariable Long id, @RequestBody Map<String, String> recipes){
        Recipe r = this.rr.findById(id).get();
        recipes.forEach((key, value) -> {
            if (key.equals("name")){
                r.setDescription(value);
            } else if ((key.equals("dateCreated"))) {
                r.setDateCreated(r.getDateCreated());
            } else if((key.equals("calories"))){
                r.setCalories(Integer.parseInt(value));
            } else if((key.equals("description"))){
                r.setDescription(value);
            }
        });
        return this.rr.save(r);
    }

    @DeleteMapping("/recipe/{id}")
    public void deleteOneRecipe(@PathVariable Long id){
        rr.deleteById(id);
    }

}
