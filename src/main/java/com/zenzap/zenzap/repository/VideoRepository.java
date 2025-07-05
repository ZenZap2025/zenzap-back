package com.zenzap.zenzap.repository;

import com.zenzap.zenzap.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VideoRepository extends JpaRepository<Video, Long> {

    /**
     * Vídeos de una subcategoría (p.ej. manos, espalda, etc.).
     * @param categoryId id de la categoría.
     * @param pageable paginación y orden.
     */
    Page<Video> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * Vídeos de cualquier categoría (para Pausas Activas en general),
     * ordenados de forma aleatoria.
     */
    @Query("SELECT v FROM Video v ORDER BY function('random')")
    Page<Video> findAllRandom(Pageable pageable);
}

