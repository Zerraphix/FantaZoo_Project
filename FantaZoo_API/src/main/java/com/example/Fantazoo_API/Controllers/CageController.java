package com.example.Fantazoo_API.Controllers;

import com.example.Fantazoo_API.Models.Animal;
import com.example.Fantazoo_API.Models.Cage;
import com.example.Fantazoo_API.Models.Weapon;
import com.example.Fantazoo_API.Models.Zookeeper;
import com.example.Fantazoo_API.Repositories.AnimRepository;
import com.example.Fantazoo_API.Repositories.CageRepository;
import com.example.Fantazoo_API.Repositories.ZKRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cc")
public class CageController {
    private final CageRepository cr;
    private final ZKRepository zkr;
    private final AnimRepository ar;

    CageController(CageRepository cr, ZKRepository zkr, AnimRepository ar) {
        this.cr = cr;
        this.zkr = zkr;
        this.ar = ar;
    }

    @GetMapping()
    List<Cage> getCages()
    {
        return cr.findAll();
    }

    @GetMapping("/id/{id}")
    Cage getCageById(@PathVariable int id)
    {
        return cr.findById(id).get();
    }

    @PostMapping()
    void insertCage(@RequestBody Cage cage)
    {
        cr.save(cage);
    }

    @PutMapping("/id/{id}")
    Cage updateCage(@RequestBody Cage newCage, @PathVariable int id)
    {
        return cr.findById(id)
                .map(cage -> {
                    cage.setName(newCage.getName());
                    cage.setZookeepers(newCage.getZookeepers());
                    cage.setAnimals(newCage.getAnimals());
                    return cr.save(cage);
                })
                .orElseGet(() -> {
                    newCage.setId(id);
                    return cr.save(newCage);
                });
    }

    @DeleteMapping("/id/{id}")
    void deleteCage(@PathVariable int id)
    {
        cr.deleteById(id);
        Cage cage = cr.findById(id).orElse(null);
        if (cage != null) {
            for (Zookeeper zookeeper : cage.getZookeepers()) {
                zookeeper.setCage(null);
                zkr.save(zookeeper);
            }
            for (Animal animal : cage.getAnimals()) {
                animal.setCage(null);
                ar.save(animal);
            }
            cr.deleteById(id);
        }
    }
}
