package com.taskflow.security;

import com.taskflow.repository.ProjectRepository;
import com.taskflow.repository.UserRepository;
import org.springframework.stereotype.Component;

/**
 * ProjectSecurity — el bean de autorización DATA-DRIVEN (MP-9). Un matcher de URL no puede saber quién
 * es el owner del proyecto 7 (eso es un DATO en la BD, no una ruta), así que la regla del capstone
 * "solo ADMIN u owner borra el proyecto" se expresa con @PreAuthorize invocando este bean:
 *
 *   @PreAuthorize("hasRole('ADMIN') or @projectSecurity.esOwner(#id, authentication.name)")
 *
 * El nombre del bean ('projectSecurity') es el de la clase con inicial minúscula: por eso el SpEL lo
 * referencia como @projectSecurity.
 */
@Component
public class ProjectSecurity {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectSecurity(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    /**
     * ¿El usuario 'username' es el owner del proyecto 'projectId'? Compara el ownerId del proyecto
     * contra el id del usuario (userRepository.findByUsername -> User::getId).
     *
     * IMPORTANTE: proyecto INEXISTENTE -> devuelve true A PROPÓSITO, para que el @PreAuthorize deje
     * pasar y sea el 404 del servicio (ProjectService.eliminar) el que hable (evitas un 403 mentiroso
     * "no puedes borrar algo que no existe").
     */
    public boolean esOwner(Long projectId, String username) {
        // TODO MP-9: projectRepository.findById(projectId)
        //   - vacío  -> return true (deja hablar al 404 del servicio)
        //   - presente -> comparar proyecto.getOwnerId() con el id del usuario
        //                 (userRepository.findByUsername(username).map(User::getId))
        throw new UnsupportedOperationException("TODO MP-9: implementar ProjectSecurity.esOwner");
    }
}
