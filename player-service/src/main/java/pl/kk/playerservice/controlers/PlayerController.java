package pl.kk.playerservice.controlers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kk.playerservice.models.PlayersList;
import pl.kk.playerservice.services.PlayerService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/players")
public class PlayerController {

    private PlayerService playerService;

    @GetMapping("/spawnPlayers")
    public PlayersList spawnPlayers() {
        return playerService.spawnPlayers();
    }

    @GetMapping("/getPlayers/{playerId}/{enemyId}")
    public PlayersList getPlayers(@PathVariable("playerId") long playerId, @PathVariable("enemyId") long enemyId) {
        return playerService.getAllPlayers(playerId, enemyId);
    }

    @GetMapping("/removePlayer/{playerId}")
    public Long removePlayer(@PathVariable("playerId") long playerId) {
        return playerService.removePlayer(playerId);
    }
}

