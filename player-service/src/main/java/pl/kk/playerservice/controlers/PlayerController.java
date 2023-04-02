package pl.kk.playerservice.controlers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kk.playerservice.models.PlayersList;
import pl.kk.playerservice.services.PlayerService;

@RestController
@AllArgsConstructor
@RequestMapping("/players")
public class PlayerController {

    private PlayerService playerService;

    @GetMapping("/spawnPlayers")
    public String spawnPlayers() {
        return playerService.spawnPlayers();
    }

    @GetMapping("/getPlayers")
    public PlayersList getPlayers() {
        return playerService.getAllPlayers();
    }
    @GetMapping("/switchPlayerTurn")
    public int switchPlayerTurn() {
        return playerService.switchPlayerTurn();
    }
    @GetMapping("/getPlayerTurn")
    public int getPlayerTurn() {
        return playerService.getPlayerTurn();
    }
    @GetMapping("/removePlayer/{playerId}")
    public String removePlayer(@PathVariable("playerId")long playerId) {
        return playerService.removePlayer(playerId);
    }
    @GetMapping("/removePlayers")
    public String removePlayers() {
        return playerService.removePlayers();
    }






//        return spawners.stream().map(spawner -> {
//            Spawner spawner1 = restTemplate.getForObject("http://localhost:8081/spawners/" + spawner.getId(), Spawner.class);
//            return new Spawner(spawner1.getId(), spawner1.getX(), spawner1.getY(), spawner1.getSpawnRate(),
//                    spawner1.getIsAlive(), spawner1.getColor());
//        })
//                .collect(Collectors.toList());
}

