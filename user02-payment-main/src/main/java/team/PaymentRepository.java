package team;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {

    List<Payment> findByApplyId(String applyId);
}
