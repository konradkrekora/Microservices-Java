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
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class SoldierService {

    private RestTemplate restTemplate;
    private SoldierRepository soldierRepository;
    private static long conqueredPlayerId;

    public String spawnSoldiers(long playerId, int spawnRate, int playerX, int playerY) {
        List<Soldier> spawnedSoldiers = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i <spawnRate ; i++) {
            spawnedSoldiers.add(new Soldier(playerId, playerX, playerY,1));
        }
        soldierRepository.saveAll(spawnedSoldiers);
        return "Soldiers spawned";
    }

    @Transactional
    public String moveSoldiers(long playerId) {
        List<Soldier> soldiers = soldierRepository.findAllByPlayerId(playerId);
        Random rand = new Random();
        for (int i = 0; i <soldiers.size() ; i++) {
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
    public String soldiersFight(long playerId) {
        List<Soldier> currentPlayerSoldiers = soldierRepository.findAllByPlayerId(playerId);
        for (Soldier soldier: currentPlayerSoldiers) {
            List<Soldier> otherPlayerSoldiers = soldierRepository.findAllSoldiersWithDifferentPlayerId(playerId);
            PlayersList playersList = restTemplate.getForObject("http://player-service/players/getPlayers", PlayersList.class);

            for (Player player: playersList.getPlayers()) {
                if(soldier.getX() >= player.getX()-1 && soldier.getX() <= player.getX()+1
                        && soldier.getY() >= player.getY()-1 && soldier.getY() <= player.getY()+1
                        && soldier.getPlayerId() != player.getPlayerId()) {
                    soldierRepository.deleteByPlayerId(player.getPlayerId());
                    conqueredPlayerId = player.getPlayerId();
                    return restTemplate.getForObject("http://player-service/players/removePlayer/" + conqueredPlayerId, String.class);
                    //return "Player " + soldier.getPlayerId() + " conquered player " + player.getPlayerId();
                }
            }
            for (Soldier otherPlayerSoldier: otherPlayerSoldiers) {
                if(soldier.getX() >= otherPlayerSoldier.getX()-1 && soldier.getX() <= otherPlayerSoldier.getX()+1
                && soldier.getY() >= otherPlayerSoldier.getY()-1 && soldier.getY() <= otherPlayerSoldier.getY()+1) {
                    otherPlayerSoldier.setIsAlive(0);
                    soldierRepository.deleteById(otherPlayerSoldier.getId());
            }
        }


        }
        return "Fight over";
    }



    public SoldiersList getAllSoldiers() {
        SoldiersList soldiersList = new SoldiersList();
        soldiersList.setSoldiers(soldierRepository.findAll());
        return soldiersList;
    }

    public Long getConqueredPlayerId() {
        return conqueredPlayerId;
    }

    public String removeAllSoldiers() {

        soldierRepository.deleteAll();
        return "Soldiers removed";
    }
}
