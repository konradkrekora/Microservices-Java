package pl.kk.soldierservice.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private long playerId;
    private int x;
    private int y;
    private int spawnRate;
    private int isAlive;
}
