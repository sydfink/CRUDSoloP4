package com.galvanzie.SoloP4;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.is;
import javax.transaction.Transactional;
import java.util.Calendar;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {

    @Autowired
    RecipeRepository rr;

    @Autowired
    MockMvc mvc;

    @Test
    @Transactional
    @Rollback
    public void addNewRecipeTest() throws Exception{
        this.mvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\": \"Potato Soup\", \"instructions\": \"instructions\", \"calories\": \"290\", \"dateCreated\": \"1980-11-25\"}"))
                    .andExpect(jsonPath("$.description", is("Potato Soup")))
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.instructions", is("instructions")))
                    .andExpect(jsonPath("$.calories", is(290)))
                    .andExpect(jsonPath("$.dateCreated", is("1980-11-25")));
    }

    @Test
    @Transactional
    @Rollback
    public void returnOneRecipeTest() throws Exception{
        Calendar test = Calendar.getInstance();
        test.set(1980, Calendar.NOVEMBER, 25);
        Recipe testr = new Recipe("Potato Soup", "instructions", 290, test.getTime());
        Recipe record = rr.save(testr);

        this.mvc.perform(get("/recipe/" + record.getId()))
                .andExpect(jsonPath("$.description", is("Potato Soup")))
                .andExpect(jsonPath("$.id").isNumber()) //or record.getId().intValue()
                .andExpect(jsonPath("$.instructions", is("instructions")))
                .andExpect(jsonPath("$.calories", is(290)))
                .andExpect(jsonPath("$.dateCreated", is("1980-11-25")));

    }

    @Test
    @Transactional
    @Rollback
    public void getAllRecipesTest() throws Exception{
        Calendar test = Calendar.getInstance();
        test.set(1980, Calendar.NOVEMBER, 25);
        Recipe testr = new Recipe("Potato Soup", "instructions", 290, test.getTime());
        Recipe record = rr.save(testr);

        Calendar test2 = Calendar.getInstance();
        test2.set(1999, Calendar.MARCH, 15);
        Recipe testr2 = new Recipe("Syd Special", "instructions", 110, test2.getTime());
        Recipe record2 = rr.save(testr2);

        this.mvc.perform(get("/recipe"))
                .andExpect(jsonPath("$[0].description", is("Potato Soup")))
                .andExpect(jsonPath("$[0].id", is(record.getId().intValue())))
                .andExpect(jsonPath("$[0].instructions", is("instructions")))
                .andExpect(jsonPath("$[0].calories", is(290)))
                .andExpect(jsonPath("$[0].dateCreated", is("1980-11-25")))
                .andExpect(jsonPath("$[1].description", is("Syd Special")))
                .andExpect(jsonPath("$[1].id", is(record2.getId().intValue())))
                .andExpect(jsonPath("$[1].instructions", is("instructions")))
                .andExpect(jsonPath("$[1].calories", is(110)))
                .andExpect(jsonPath("$[1].dateCreated", is("1999-03-15")));

    }

    @Test
    @Transactional
    @Rollback
    public void updateOneRecipe() throws Exception{
        Calendar test2 = Calendar.getInstance();
        test2.set(1999, Calendar.MARCH, 15);
        Recipe testr2 = new Recipe("Syd Special", "instructions", 110, test2.getTime());
        Recipe record2 = rr.save(testr2);

        this.mvc.perform(patch("/recipe/" + record2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\": \"Syd's Special\", \"instructions\": \"instructions\", \"calories\": \"110\", \"dateCreated\": \"1999-03-15\"}"))
                .andExpect(jsonPath("$.description", is("Syd's Special")))
                .andExpect(jsonPath("$.id", is(record2.getId().intValue())))
                .andExpect(jsonPath("$.instructions", is("instructions")))
                .andExpect(jsonPath("$.calories", is(110)))
                .andExpect(jsonPath("$.dateCreated", is("1999-03-15")));

    }

    @Test
    @Transactional
    @Rollback
    public void deleteOneRecipe() throws Exception{
        Calendar test = Calendar.getInstance();
        test.set(1980, Calendar.NOVEMBER, 25);
        Recipe testr = new Recipe("Potato Soup", "instructions", 290, test.getTime());
        Recipe record = rr.save(testr);

        Calendar test2 = Calendar.getInstance();
        test2.set(1999, Calendar.MARCH, 15);
        Recipe testr2 = new Recipe("Syd Special", "instructions", 110, test2.getTime());
        Recipe record2 = rr.save(testr2);

        this.mvc.perform(patch("/recipe/" + record2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Syd Special\", \"instructions\": \"instructions\", \"calories\": \"110\", \"dateCreated\": \"1999-03-15\"}"))
                .andExpect(jsonPath("$[1].description").doesNotHaveJsonPath());
    }

}
