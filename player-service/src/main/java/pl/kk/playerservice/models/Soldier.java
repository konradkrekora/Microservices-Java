package pl.kk.playerservice.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Soldier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long playerId;
    private int x;
    private int y;
    private int isAlive;

}
