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
     * Obtiene vídeos de una subcategoría específica.
     */
    public Page<Video> getVideosByCategory(Long categoryId, int size) {
        return videoRepository.findByCategoryId(categoryId, PageRequest.of(0, size));
    }

    /**
     * Obtiene vídeos aleatorios de todas las categorías.
     */
    public Page<Video> getRandomVideos(int size) {
        return videoRepository.findAllRandom(PageRequest.of(0, size));
    }

    /**
     * Recupera un vídeo por su ID.
     */
    public Video getById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vídeo no encontrado: " + id));
    }

    /**
     * Crea un nuevo vídeo a partir del DTO y del ID de usuario.
     */
    public Video createVideo(VideoRequest req, Long userId) {
        Category cat = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no válida: " + req.getCategoryId()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userId));

        Video v = new Video();
        v.setYoutubeUrl(req.getYoutubeUrl());
        v.setTitle(req.getTitle());
        v.setDescription(req.getDescription());
        v.setDurationInSeconds(req.getDurationInSeconds());
        v.setCategory(cat);
        v.setCreatedBy(user);
        v.setCreatedAt(OffsetDateTime.now());

        return videoRepository.save(v);
    }

    /**
     * Actualiza un vídeo existente.
     */
    public Video updateVideo(Long id, VideoRequest req) {
        Video existing = getById(id);

        Category cat = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no válida: " + req.getCategoryId()));

        existing.setYoutubeUrl(req.getYoutubeUrl());
        existing.setTitle(req.getTitle());
        existing.setDescription(req.getDescription());
        existing.setDurationInSeconds(req.getDurationInSeconds());
        existing.setCategory(cat);
        // dejamos createdBy y createdAt originales

        return videoRepository.save(existing);
    }

    /**
     * Elimina un vídeo por su ID.
     */
    public void deleteVideo(Long id) {
        // lanzará excepción si no existe
        Video toDelete = getById(id);
        videoRepository.delete(toDelete);
    }
}



