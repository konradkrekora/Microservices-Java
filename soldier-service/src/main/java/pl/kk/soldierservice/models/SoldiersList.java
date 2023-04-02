package pl.kk.soldierservice.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SoldiersList {
    private List<Soldier> soldiers;
}
