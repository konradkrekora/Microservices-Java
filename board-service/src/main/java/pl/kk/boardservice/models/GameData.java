package pl.kk.boardservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class GameData {

    private long totalTimeMillisecs;
    private int totalTurns;
    private long spawnPlayersTime = 0;
    private long switchPlayerAndSpawnSoldiersTime = 0;
    private long moveSoldiersTime = 0;
    private long soldiersFightTime = 0;
    private long resetGameTime = 0;
    private long getSoldiersAndPlaceAllUnitsOnBoardTime = 0;
    private String status;
}
