package com.example.Fantazoo_API.Controllers;

import com.example.Fantazoo_API.Models.Animal;
import com.example.Fantazoo_API.Models.Cage;
import com.example.Fantazoo_API.Repositories.AnimRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ac")
public class AnimController {
    private final AnimRepository ar;

    AnimController(AnimRepository animRepository) {
        this.ar = animRepository;
    }

    @GetMapping()
    List<Animal> getAllAnimals()
    {
        return ar.findAll();
    }

    @GetMapping("/id/{id}")
    Animal getAnimalById(@PathVariable int id)
    {
        return ar.findById(id).get();
    }

    @PostMapping()
    void insertAnimal(@RequestBody Animal animal)
    {
        ar.save(animal);
    }

    @PutMapping("/id/{id}")
    Animal updateAnimal(@RequestBody Animal newAnimal, @PathVariable int id)
    {
        return ar.findById(id)
                .map(animal -> {
                    animal.setName(newAnimal.getName());
                    animal.setImgsrc(newAnimal.getImgsrc());
                    animal.setAge(newAnimal.getAge());
                    animal.setGender(newAnimal.getGender());
                    animal.setCage(newAnimal.getCage());
                    return ar.save(animal);
                })
                .orElseGet(() -> {
                    newAnimal.setId(id);
                    return ar.save(newAnimal);
                });
    }

    @DeleteMapping("/id/{id}")
    void deleteAnimal(@PathVariable int id)
    {
        ar.deleteById(id);
    }
}
