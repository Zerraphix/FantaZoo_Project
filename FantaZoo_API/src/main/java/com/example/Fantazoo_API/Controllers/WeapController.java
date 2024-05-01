package com.example.Fantazoo_API.Controllers;

import com.example.Fantazoo_API.Models.Weapon;
import com.example.Fantazoo_API.Models.Zookeeper;
import com.example.Fantazoo_API.Repositories.WeapRepository;
import com.example.Fantazoo_API.Repositories.ZKRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/wpc")
public class WeapController {
    private final WeapRepository wpr;

    private final ZKRepository zkr;

    WeapController(WeapRepository wpr, ZKRepository zkr) {
        this.wpr = wpr;
        this.zkr = zkr;
    }

    @GetMapping()
    List<Weapon> getWeapons()
    {
        return wpr.findAll();
    }

    @GetMapping("/id/{id}")
    Weapon getWeaponById(@PathVariable int id)
    {
        return wpr.findById(id).get();
    }

    @PostMapping()
    void insertWeapon(@RequestBody Weapon weapon)
    {
        wpr.save(weapon);
    }

    @PutMapping("/id/{id}")
    Weapon updateWeapon(@RequestBody Weapon newWeapon, @PathVariable int id)
    {
        return wpr.findById(id)
                .map(weapon -> {
                    weapon.setName(newWeapon.getName());
                    weapon.setDamage(newWeapon.getDamage());
                    weapon.setZookeepers(newWeapon.getZookeepers());
                    return wpr.save(weapon);
                })
                .orElseGet(() -> {
                    newWeapon.setId(id);
                    return wpr.save(newWeapon);
                });
    }

    @DeleteMapping("/id/{id}")
    void deleteWeapon(@PathVariable int id)
    {
        Weapon weapon = wpr.findById(id).orElse(null);
        if (weapon != null) {
            for (Zookeeper zookeeper : weapon.getZookeepers()) {
                zookeeper.setWeapon(null);
                zkr.save(zookeeper);
            }
            wpr.deleteById(id);
        }
    }
}
