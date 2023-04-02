package pl.kk.soldierservice.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Soldier")
public class Soldier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long playerId;
    private int x;
    private int y;
    private int isAlive;

    public Soldier(long playerId, int x, int y, int isAlive) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.isAlive = isAlive;
    }
}
