package pl.kk.playerservice.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long playerId;
    private int x;
    private int y;
    private int spawnRate;
    private int isAlive;

    public Player(int x, int y, int spawnRate, int isAlive) {
        this.x = x;
        this.y = y;
        this.spawnRate = spawnRate;
        this.isAlive = isAlive;
    }
}
