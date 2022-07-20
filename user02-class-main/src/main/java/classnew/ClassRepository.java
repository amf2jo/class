package classnew;

import org.springframework.data.repository.PagingAndSortingRepository;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

//@RepositoryRestResource(collectionResourceRel="classes", path="classes")
public interface ClassRepository extends PagingAndSortingRepository<Class, Long>{

    List <Class> findByCourseId(Long id);
}
