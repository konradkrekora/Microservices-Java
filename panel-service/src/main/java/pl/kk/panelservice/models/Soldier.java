package pl.kk.panelservice.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Soldier {
    private long id;
    private long playerId;
    private int x;
    private int y;
    private int isAlive;

}
