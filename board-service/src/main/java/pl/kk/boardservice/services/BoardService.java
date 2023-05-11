package pl.kk.boardservice.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import pl.kk.boardservice.models.GameData;
import pl.kk.boardservice.models.Player;
import pl.kk.boardservice.models.PlayersList;
import pl.kk.boardservice.models.Soldier;
import pl.kk.boardservice.models.SoldiersList;
import pl.kk.boardservice.models.Unit;
import pl.kk.boardservice.models.UnitType;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class BoardService {

    private RestTemplate restTemplate;
    private WebClient webClient;

    //    @CircuitBreaker(name = "getBoard", fallbackMethod = "getBoardFallback")
//    @Retry(name = "getBoard")
    public GameData getBoard() {

        int gameStartedFlag = 0;
        int gameOverFlag = 0;
        long currentPlayerId = 0;
        PlayersList playersList = new PlayersList();

        long totalTimeMillisecs;
        int totalTurns = 0;
        long startTime = System.currentTimeMillis();

        //todo index < 100 dla testow
        for (int index = 0; index < 10; index++) {

            /**
             * Starts the game
             */
            do {

                if (gameStartedFlag == 0) {
                    playersList = restTemplate.getForObject("http://player-service/api/player/spawnPlayers", PlayersList.class);
                    gameStartedFlag = 1;
                    gameOverFlag = 0;
                }

                /**
                 * Switch turn between players
                 */
                currentPlayerId = switchPlayerTurn(currentPlayerId, playersList);
                List<Player> players = playersList.getPlayers();

                if (players.size() > 1) {
                    Player currentPlayer = new Player();
                    for (Player player : players) {
                        if (player.getPlayerId() == currentPlayerId) {
                            currentPlayer = player;
                        }
                    }

                    /**
                     * Spawns new soldiers for currently active player
                     */
                    String spawnedSoldiers = restTemplate.getForObject("http://soldier-service/api/soldier/spawnSoldiers/" + currentPlayerId + "/"
                            + currentPlayer.getSpawnRate() + "/" + currentPlayer.getX() + "/" + currentPlayer.getY(), String.class);
                }
                /**
                 * Moves all soldiers of currently active player
                 */
                String moveSoldiers = restTemplate.getForObject("http://soldier-service/api/soldier/moveSoldiers/" + currentPlayerId, String.class);
                /**
                 * Attacks from current player soldiers against other players
                 */
                Long enemyPlayer = getEnemyPlayerId(currentPlayerId, playersList);
                System.out.println("enemyPlayer:" + enemyPlayer);
                System.out.println("currentPlayerId before fight:" + currentPlayerId);
                Long conqueredPlayerId = restTemplate.getForObject("http://soldier-service/api/soldier/soldiersFight/" + currentPlayerId + "/" + enemyPlayer, Long.class);
                System.out.println("conqueredPlayerId:" + conqueredPlayerId);
                System.out.println("currentPlayerId:" + currentPlayerId);
                if (conqueredPlayerId != null && conqueredPlayerId != 0) {
                    playersList.setPlayers(playersList.getPlayers().stream().filter(p -> p.getPlayerId() != conqueredPlayerId)
                            .toList());

                    //usunięcie wygranego gracza, zmiana ustawień na koniec gry
                    restTemplate.getForObject("http://player-service/api/player/removePlayer/" + playersList.getPlayers().get(0).getPlayerId(), Long.class);
                    restTemplate.getForObject("http://soldier-service/api/soldier/removeSoldiersByPlayerId/" + playersList.getPlayers().get(0).getPlayerId(), Long.class);
                    gameStartedFlag = 0;
                    gameOverFlag = 1;
                    playersList.setPlayers(new ArrayList<>());
                    System.out.println("GAME OVER");
                }

                SoldiersList soldiersList = restTemplate.getForObject("http://soldier-service/api/soldier/getSoldiers", SoldiersList.class);
                List<Soldier> soldiers = soldiersList.getSoldiers();


                /**
                 * Places all units(players and soldiers) on the board - for visualization purpouse
                 */
                List<List<Unit>> listOfUnitsLists = new ArrayList<>();
                listOfUnitsLists = placeUnitsOnBoard(players, soldiers, listOfUnitsLists);

                //koniec tury. dodanie do sumy tur
                totalTurns++;
            }
            while (gameOverFlag == 0);
        }
        //koniec gry. sumowanie czasu gry
        long endTime = System.currentTimeMillis();
        totalTimeMillisecs = endTime - startTime;

//        return listOfUnitsLists;
        return new GameData(totalTimeMillisecs, totalTurns, "OK");
    }


    private List<List<Unit>> placeUnitsOnBoard(List<Player> players, List<Soldier> soldiers, List<List<Unit>> listOfUnitsLists) {
        for (int i = 1; i <= 10; i++) {
            List<Unit> unitsList = new ArrayList<>();
            for (int j = 1; j <= 10; j++) {
                UnitType unitType = UnitType.empty;
                long playerId = 0;
                String name = null;

                for (Soldier soldier : soldiers) {
                    if (j == soldier.getX() && i == soldier.getY()) {
                        unitType = UnitType.soldier;
                        playerId = soldier.getPlayerId();
                        //name = "S" + playerId;
                    }
                }

                for (Player player : players) {
                    if (j == player.getX() && i == player.getY()) {
                        unitType = UnitType.player;
                        playerId = player.getPlayerId();
                        //name = "P" + playerId;
                    }
                }

                unitsList.add(new Unit(unitType, playerId, name));
            }
            listOfUnitsLists.add(unitsList);
        }
        return listOfUnitsLists;
    }

    public ModelAndView showHomePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index2.html");
        return modelAndView;
    }

    public GameData getBoardFallback(Exception ex) {

        return GameData.builder()
                .totalTimeMillisecs(1000L)
                .totalTurns(10)
                .build();
    }

    private long switchPlayerTurn(long currentPlayerTurn, PlayersList playersList) {

        if (currentPlayerTurn == playersList.getPlayers().get(0).getPlayerId())
        {
            currentPlayerTurn = playersList.getPlayers().get(1).getPlayerId();
        }
        else
        {
            currentPlayerTurn = playersList.getPlayers().get(0).getPlayerId();
        }
        return currentPlayerTurn;
    }

    private long getEnemyPlayerId(long currentPlayerId, PlayersList playersList) {
        long enemyPlayerId = 0;
        for (Player player: playersList.getPlayers()) {
            if (currentPlayerId != player.getPlayerId()) {
                enemyPlayerId = player.getPlayerId();
            }
        }
        return enemyPlayerId;
    }
}
