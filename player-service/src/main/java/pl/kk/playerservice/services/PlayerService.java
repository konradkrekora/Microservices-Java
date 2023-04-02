package pl.kk.playerservice.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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

    private RestTemplate restTemplate;
    private PlayerRepository playerRepository;
    private static int currentPlayerTurn = 0;
    private static int firstPlayer = 0;

    public String spawnPlayers() {
        List<Player> spawnedPlayers = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 6 ; i = i + 5) {
            int x = rand.nextInt(5) + 1 + i;
            int y = rand.nextInt(5) + 1 + i;
//            int spawnRate = rand.nextInt(5) + 1;
            int spawnRate = 1;
            spawnedPlayers.add(new Player(x, y, spawnRate ,1));
        }
        playerRepository.saveAll(spawnedPlayers);
        return "Players spawned";
    }

    public PlayersList getAllPlayers() {
        PlayersList playersList = new PlayersList();
        playersList.setPlayers(playerRepository.findAll());
        return playersList;
    }

    public int switchPlayerTurn() {
        if (currentPlayerTurn == 0)
        {
            PlayersList playersList = new PlayersList();
            List<Player> players = playerRepository.findAll();
            playersList.setPlayers(players);
            currentPlayerTurn = (int) players.get(0).getPlayerId();
            firstPlayer = (int)players.get(0).getPlayerId();
            return currentPlayerTurn;
        }

        if (currentPlayerTurn == firstPlayer)
        {
            currentPlayerTurn = firstPlayer + 1;
        }
        else if (currentPlayerTurn == firstPlayer + 1)
        {
            currentPlayerTurn = firstPlayer;
        }
        return currentPlayerTurn;
    }

    public int getPlayerTurn() {
        return currentPlayerTurn;
    }

    public String removePlayer(long playerId) {
        playerRepository.deleteById(playerId);
        return "Player" + playerId + " conquered!";
    }

    public String removePlayers() {
        playerRepository.deleteAll();
        currentPlayerTurn = 0;
        return "Players removed!";
    }
}
