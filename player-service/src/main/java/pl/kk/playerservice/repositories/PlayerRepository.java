package pl.kk.playerservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kk.playerservice.models.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
