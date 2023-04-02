package pl.kk.boardservice.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SoldiersList {
    private List<Soldier> soldiers;
}
