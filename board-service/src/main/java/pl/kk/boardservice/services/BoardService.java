package pl.kk.boardservice.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class BoardService {

    private static int gameStartedFlag = 0;
    private static int gameOverFlag = 0;
    private static PlayersList playersList;
    private RestTemplate restTemplate;
    private WebClient webClient;

    //    public List<List<Unit>> getBoard() {

//    @CircuitBreaker(name = "getBoard", fallbackMethod = "getBoardFallback")
//    @Retry(name = "getBoard")
    public GameData getBoard() {

//        List<GameData> games = new ArrayList<>();

        long totalTimeMillisecs = 0;
        int totalTurns = 0;
        long startTime = System.currentTimeMillis();

        //todo index < 100 dla testow
        for (int index = 0; index < 5; index++) {

            /**
             * Starts the game
             */
            do {

                if (gameStartedFlag == 0) {
                    String removePlayers = restTemplate.getForObject("http://player-service/players/removePlayers", String.class);
                    String spawnedPlayers = restTemplate.getForObject("http://player-service/players/spawnPlayers", String.class);
                    String removeSoldiers = restTemplate.getForObject("http://soldier-service/soldiers/removeSoldiers", String.class);
                    gameStartedFlag = 1;
                    gameOverFlag = 0;
                }

                /**
                 * Switch turn between players
                 */
                int switchPlayerTurn = restTemplate.getForObject("http://player-service/players/switchPlayerTurn", Integer.class);
                playersList = restTemplate.getForObject("http://player-service/players/getPlayers", PlayersList.class);
                List<Player> players = playersList.getPlayers();


                if (players.size() > 1) {
                    Player currentPlayer = new Player();
                    for (Player player : players) {
                        if (player.getPlayerId() == switchPlayerTurn) {
                            currentPlayer = player;
                        }
                    }

                    /**
                     * Spawns new soldiers for currently active player
                     */
                    String spawnedSoldiers = restTemplate.getForObject("http://soldier-service/soldiers/spawnSoldiers/" + switchPlayerTurn + "/"
                            + currentPlayer.getSpawnRate() + "/" + currentPlayer.getX() + "/" + currentPlayer.getY(), String.class);
                } else {

                    gameStartedFlag = 0;
                    gameOverFlag = 1;
                    System.out.println("GAME OVER");
                }
                /**
                 * Moves all soldiers of currently active player
                 */
                String moveSoldiers = restTemplate.getForObject("http://soldier-service/soldiers/moveSoldiers/" + switchPlayerTurn, String.class);
                /**
                 * Attacks from current player soldiers against other players
                 */
                String soldiersFight = restTemplate.getForObject("http://soldier-service/soldiers/soldiersFight/" + switchPlayerTurn, String.class);
                System.out.println(soldiersFight);

                SoldiersList soldiersList = restTemplate.getForObject("http://soldier-service/soldiers/getSoldiers", SoldiersList.class);
                List<Soldier> soldiers = soldiersList.getSoldiers();


                List<List<Unit>> listOfUnitsLists = new ArrayList<>();

                /**
                 * Places all units(players and soldiers) on the board
                 */
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
                //koniec tury. dodanie do sumy tur
                totalTurns++;
            }
            while (gameOverFlag == 0);
        }
        //koniec gry. sumowanie czasu gry
        long endTime = System.currentTimeMillis();
        totalTimeMillisecs = endTime - startTime;

//        return listOfUnitsLists;
        return new GameData(totalTimeMillisecs, totalTurns);
    }

    public ModelAndView showHomePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index2.html");
        return modelAndView;
    }

    public GameData getBoardFallback(Exception ex) {
        gameStartedFlag = 0;
        gameOverFlag = 0;

        return GameData.builder()
                .totalTimeMillisecs(1000L)
                .totalTurns(10)
                .build();
    }
}
