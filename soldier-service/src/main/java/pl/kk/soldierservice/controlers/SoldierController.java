package pl.kk.soldierservice.controlers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kk.soldierservice.models.SoldiersList;
import pl.kk.soldierservice.services.SoldierService;


@RestController
@AllArgsConstructor
@RequestMapping("/soldiers")
public class SoldierController {

    private SoldierService soldierService;

    @GetMapping("/spawnSoldiers/{playerId}/{spawnRate}/{playerX}/{playerY}")
    public String spawnSoldiers(@PathVariable("playerId") long playerId, @PathVariable("spawnRate") int spawnRate
            , @PathVariable("playerX") int playerX, @PathVariable("playerY") int playerY) {
        return soldierService.spawnSoldiers(playerId, spawnRate, playerX, playerY);
    }

    @GetMapping("/moveSoldiers/{playerId}")
    public String moveSoldiers(@PathVariable("playerId") long playerId) {
        return soldierService.moveSoldiers(playerId);
    }

    @GetMapping("/soldiersFight/{playerId}/{enemyId}")
    public Long soldiersFight(@PathVariable("playerId") long playerId, @PathVariable("enemyId") long enemyId) {
        return soldierService.soldiersFight(playerId, enemyId);
    }


    @GetMapping("/getSoldiers")
    public SoldiersList getSoldiers() {
        return soldierService.getAllSoldiers();
    }

    @GetMapping("/removeSoldiersByPlayerId/{playerId}")
    public Long removeSoldiersByPlayerId(@PathVariable("playerId") long playerId) {
        return soldierService.removeSoldiersByPlayerId(playerId);
    }
}
