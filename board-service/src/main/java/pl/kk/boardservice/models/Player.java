package pl.kk.boardservice.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Player {
    private long playerId;
    private int x;
    private int y;
    private int spawnRate;
    private int isAlive;
}
