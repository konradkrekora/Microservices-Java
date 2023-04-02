package pl.kk.boardservice.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayersList {
    private List<Player> players;
}
