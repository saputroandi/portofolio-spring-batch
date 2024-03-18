package portofolio.springbatch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portofolio.springbatch.entity.Majestic;

@Repository
public interface MajesticRepository extends JpaRepository<Majestic, Long> {
}
