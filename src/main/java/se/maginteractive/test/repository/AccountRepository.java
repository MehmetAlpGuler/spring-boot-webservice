package se.maginteractive.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.maginteractive.test.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
