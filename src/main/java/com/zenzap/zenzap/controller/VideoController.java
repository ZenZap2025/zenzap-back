package com.zenzap.zenzap.controller;

import com.zenzap.zenzap.controller.dto.VideoRequest;
import com.zenzap.zenzap.entity.Video;
import com.zenzap.zenzap.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    /**
     * GET /api/videos
     * Listar vídeos, opcionalmente filtrados por subcategoría, con paginación.
     */
    @GetMapping
    public ResponseEntity<Page<Video>> listVideos(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        Page<Video> page = (categoryId != null)
                ? videoService.getVideosByCategory(categoryId, size)
                : videoService.getRandomVideos(size);
        return ResponseEntity.ok(page);
    }

    /**
     * GET /api/videos/{id}
     * Obtener un vídeo por su ID (para rellenar el formulario de edición).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable Long id) {
        Video video = videoService.getById(id);
        return ResponseEntity.ok(video);
    }

    /**
     * POST /api/videos
     * Crear un nuevo vídeo.
     */
    @PostMapping
    public ResponseEntity<Video> createVideo(@RequestBody VideoRequest req) {
        // En producción tomarías el ID real del usuario autenticado
        Video created = videoService.createVideo(req, 2L);
        return ResponseEntity.ok(created);
    }

    /**
     * PUT /api/videos/{id}
     * Actualizar un vídeo existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Video> updateVideo(
            @PathVariable Long id,
            @RequestBody VideoRequest req
    ) {
        Video updated = videoService.updateVideo(id, req);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/videos/{id}
     * Eliminar un vídeo por su ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }
}
