package project.model.repository.centerRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.centerEntity.CenterEntity;

@Repository
public interface CenterRepository extends JpaRepository<CenterEntity, Integer> {
}
