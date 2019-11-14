package code.service;

import code.model.Ordered;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderedService {

    void save(Ordered ordered);

    Ordered findById(Long id);

    Page<Ordered> findAll(String status, Pageable pageable);
}
