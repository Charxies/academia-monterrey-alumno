package com.taskflow.repository;

import com.taskflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository — creado en el integrador de D4 (paso 1). "Mañana lo usa el login": ese mañana es HOY
 * (S2D5, MP-4). Necesita dos derived queries para el mundo de auth (misma gramática de D4:
 * findBy/existsBy + propiedad; Spring Data escribe el SQL):
 *
 *   - findByUsername:   lo usará JpaUserDetailsService (login) y ProjectSecurity (regla de owner).
 *   - existsByUsername: lo usará AuthService.register para rechazar duplicados (409) sin traer la fila.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // TODO MP-4: declara los dos query methods (Spring Data los implementa por el NOMBRE):
    //   Optional<User> findByUsername(String username);
    //   boolean existsByUsername(String username);
}
