package code.repository;

import code.model.Ordered;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedRepository extends JpaRepository<Ordered, Long> {

    Page<Ordered> findAllByStatus(String status, Pageable pageable);
}
