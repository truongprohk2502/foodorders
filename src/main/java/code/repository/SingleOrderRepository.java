package code.repository;

import code.model.SingleOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingleOrderRepository extends JpaRepository<SingleOrder, Long> {
}
