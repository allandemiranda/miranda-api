package lu.forex.system.repositories;

import java.util.UUID;
import lu.forex.system.entities.Tick;
import org.springframework.data.repository.CrudRepository;

public interface TickRepository extends CrudRepository<Tick, UUID> {

}
