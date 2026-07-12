package com.taskflow.service;

import com.taskflow.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * JpaUserDetailsService — el punto donde Spring Security DEJA de inventar usuarios y consulta NUESTRA
 * tabla users (MP-4). Implementa el contrato UserDetailsService: loadUserByUsername -> UserDetails.
 *
 * Termómetro del día (punto de dolor 9): en cuanto este bean queda registrado (@Service, dentro del
 * component scan), el password generado DESAPARECE de la consola. Si sigue saliendo, este bean no se
 * registró.
 *
 * hasRole/prefijo (punto de dolor 5): hasRole("ADMIN") espera la authority ROLE_ADMIN. El builder
 * .roles(role.name()) AÑADE el prefijo ROLE_ por nosotros — olvidarlo produce el "403 misterioso".
 */
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO MP-4:
        //   1) userRepository.findByUsername(username).orElseThrow(() ->
        //          new UsernameNotFoundException("No existe usuario con username: " + username));
        //   2) mapear a un UserDetails de Spring con el builder:
        //        org.springframework.security.core.userdetails.User.builder()
        //            .username(user.getUsername())
        //            .password(user.getPasswordHash())   // el HASH de la BD (MP-3 añade el campo)
        //            .roles(user.getRole().name())        // añade el prefijo ROLE_
        //            .build();
        throw new UnsupportedOperationException("TODO MP-4: implementar loadUserByUsername");
    }
}
