package pl.kk.soldierservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.kk.soldierservice.models.Soldier;

import java.util.List;

@Repository
public interface SoldierRepository extends JpaRepository<Soldier, Long> {

    List<Soldier> findAllByPlayerId(Long playerId);

    @Query("select s from Soldier s where s.playerId not in ?1")
    List<Soldier> findAllSoldiersWithDifferentPlayerId(Long playerId);

    @Modifying
    @Query("delete from Soldier s where s.id = ?1")
    void deleteBySoldierId(Long soldierId);

    @Modifying
    @Query("delete from Soldier s where s.playerId = ?1")
    void deleteByPlayerId(Long playerId);

    @Modifying
    @Query("update Soldier s set s.x = ?1, s.y = ?2, s.isAlive = ?3 where s.id = ?4")
    void updateSoldierById(int x, int y, int isAlive, long id);

}
