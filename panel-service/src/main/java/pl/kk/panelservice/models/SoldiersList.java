package pl.kk.panelservice.models;

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
