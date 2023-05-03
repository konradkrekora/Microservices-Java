package pl.kk.playerservice.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pl.kk.playerservice.models.Player;
import pl.kk.playerservice.models.PlayersList;
import pl.kk.playerservice.repositories.PlayerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class PlayerService {

    private PlayerRepository playerRepository;


    public PlayersList spawnPlayers() {
        List<Player> spawnedPlayers = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 6 ; i = i + 5) {
            int x = rand.nextInt(5) + 1 + i;
            int y = rand.nextInt(5) + 1 + i;
            int spawnRate = 1;
            spawnedPlayers.add(Player.builder()
                    .x(x)
                    .y(y)
                    .spawnRate(spawnRate)
                    .isAlive(1)
                    .build());
        }
        playerRepository.saveAll(spawnedPlayers);
        return PlayersList.builder().players(spawnedPlayers).build();
    }

    public PlayersList getAllPlayers(long playerId, long enemyId) {
        List<Long> playersIds = List.of(playerId, enemyId);
        PlayersList playersList = new PlayersList();
        playersList.setPlayers(playerRepository.findByPlayerIdIn(playersIds));
        return playersList;
    }

    public Long removePlayer(long playerId) {
        playerRepository.deleteById(playerId);
        return playerId;
    }
}
