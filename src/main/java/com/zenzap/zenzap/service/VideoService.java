package com.zenzap.zenzap.service;

import com.zenzap.zenzap.controller.dto.VideoRequest;
import com.zenzap.zenzap.entity.Category;
import com.zenzap.zenzap.entity.User;
import com.zenzap.zenzap.entity.Video;
import com.zenzap.zenzap.repository.CategoryRepository;
import com.zenzap.zenzap.repository.UserRepository;
import com.zenzap.zenzap.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository,
                        CategoryRepository categoryRepository,
                        UserRepository userRepository) {
        this.videoRepository = videoRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    /**
     * Obtiene videos por categoría con paginación.
     */
    public Page<Video> getVideosByCategory(Long categoryId, int size) {
        return videoRepository.findByCategoryId(categoryId, PageRequest.of(0, size));
    }

    /**
     * Obtiene videos aleatorios con paginación.
     */
    public Page<Video> getRandomVideos(int size) {
        return videoRepository.findAllRandom(PageRequest.of(0, size));
    }

    /**
     * Crea un nuevo video a partir del DTO y el ID del usuario.
     */
    public Video createVideo(VideoRequest req, Long userId) {
        // 1) Recuperar categoría
        Category cat = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no válida: " + req.getCategoryId()));

        // 2) Recuperar usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userId));

        // 3) Construir entidad Video
        Video v = new Video();
        v.setYoutubeUrl(req.getYoutubeUrl());
        v.setTitle(req.getTitle());
        v.setDescription(req.getDescription());
        v.setDurationInSeconds(req.getDurationInSeconds());
        v.setCategory(cat);
        v.setCreatedBy(user);
        v.setCreatedAt(OffsetDateTime.now());

        // 4) Guardar
        return videoRepository.save(v);
    }
}


