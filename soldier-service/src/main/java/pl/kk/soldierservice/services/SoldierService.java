package pl.kk.soldierservice.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.kk.soldierservice.models.Player;
import pl.kk.soldierservice.models.PlayersList;
import pl.kk.soldierservice.models.Soldier;
import pl.kk.soldierservice.models.SoldiersList;
import pl.kk.soldierservice.repositories.SoldierRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@AllArgsConstructor
public class SoldierService {

    private RestTemplate restTemplate;
    private SoldierRepository soldierRepository;

    public String spawnSoldiers(long playerId, int spawnRate, int playerX, int playerY) {
        List<Soldier> spawnedSoldiers = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < spawnRate; i++) {
            spawnedSoldiers.add(new Soldier(playerId, playerX, playerY, 1));
        }
        soldierRepository.saveAll(spawnedSoldiers);
        return "Soldiers spawned";
    }

    @Transactional
    public String moveSoldiers(long playerId) {
        List<Soldier> soldiers = soldierRepository.findAllByPlayerId(playerId);
        Random rand = new Random();
        for (int i = 0; i < soldiers.size(); i++) {
            Soldier soldier = soldiers.get(i);
            int x = rand.nextInt(3) - 1 + soldier.getX();
            int y = rand.nextInt(3) - 1 + soldier.getY();
            soldier.setX(x);
            soldier.setY(y);
            if (soldier.getX() == 0 && soldier.getY() == 0) {
                soldier.setX(10);
                soldier.setY(10);
            } else if (soldier.getX() == 11 && soldier.getY() == 0) {
                soldier.setX(1);
                soldier.setY(10);
            } else if (soldier.getX() == 0 && soldier.getY() == 11) {
                soldier.setX(10);
                soldier.setY(1);
            } else if (soldier.getX() == 11 && soldier.getY() == 11) {
                soldier.setX(1);
                soldier.setY(1);
            } else if (soldier.getX() == 0) {
                soldier.setX(10);
            } else if (soldier.getX() == 11) {
                soldier.setX(1);
            } else if (soldier.getY() == 0) {
                soldier.setY(10);
            } else if (soldier.getY() == 11) {
                soldier.setY(1);
            }
            soldierRepository.updateSoldierById(soldier.getX(), soldier.getY(), soldier.getIsAlive(), soldier.getId());
        }
        return "Soldiers moved";
    }

    @Transactional
    public Long soldiersFight(long playerId, long enemyId) {
        List<Soldier> currentPlayerSoldiers = soldierRepository.findAllByPlayerId(playerId);//TODO FIX 2 zapytania
        List<Soldier> enemySoldiers = soldierRepository.findAllByPlayerId(enemyId);//TODO FIX 2 zapytania
        Set<Long> soldiersToDelete = new HashSet<>();
        for (Soldier soldier : currentPlayerSoldiers) {
            PlayersList playersList = restTemplate.getForObject("http://player-service/api/player/getPlayers/" + playerId + "/" + enemyId, PlayersList.class);
            for (Player player : playersList.getPlayers()) {
                if (soldier.getX() >= player.getX() - 1 && soldier.getX() <= player.getX() + 1
                        && soldier.getY() >= player.getY() - 1 && soldier.getY() <= player.getY() + 1
                        && soldier.getPlayerId() != player.getPlayerId()) {
                    soldierRepository.deleteByPlayerId(player.getPlayerId());
                    return restTemplate.getForObject("http://player-service/api/player/removePlayer/" + player.getPlayerId(), Long.class);
                }
            }
            for (Soldier otherPlayerSoldier : enemySoldiers) {
                if (soldier.getX() >= otherPlayerSoldier.getX() - 1 && soldier.getX() <= otherPlayerSoldier.getX() + 1
                        && soldier.getY() >= otherPlayerSoldier.getY() - 1 && soldier.getY() <= otherPlayerSoldier.getY() + 1) {
                    otherPlayerSoldier.setIsAlive(0);
                    soldiersToDelete.add(otherPlayerSoldier.getId());
                }
            }
        }
        soldierRepository.deleteByIdIn(soldiersToDelete);
        return 0L;
    }


    public SoldiersList getAllSoldiers() {
        SoldiersList soldiersList = new SoldiersList();
        soldiersList.setSoldiers(soldierRepository.findAll());
        return soldiersList;
    }

    @Transactional
    public Long removeSoldiersByPlayerId(Long playerId) {
        soldierRepository.deleteByPlayerId(playerId);
        return playerId;
    }
}
