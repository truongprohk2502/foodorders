package code.service;

import code.model.Ordered;
import code.repository.OrderedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class OrderedServiceImpl implements OrderedService {

    @Autowired
    private OrderedRepository orderedRepository;

    @Override
    public void save(Ordered ordered) {
        orderedRepository.save(ordered);
    }

    @Override
    public Ordered findById(Long id) {
        return orderedRepository.findOne(id);
    }

    @Override
    public Page<Ordered> findAll(String status, Pageable pageable) {
        return orderedRepository.findAllByStatus(status, pageable);
    }
}
