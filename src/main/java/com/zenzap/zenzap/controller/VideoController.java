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
     * @param categoryId opcional, filtrar por subcategoría
     * @param size       opcional, número de vídeos a devolver (por defecto 5)
     * @return página de vídeos
     */
    @GetMapping
    public ResponseEntity<Page<Video>> listVideos(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        Page<Video> page;
        if (categoryId != null) {
            page = videoService.getVideosByCategory(categoryId, size);
        } else {
            page = videoService.getRandomVideos(size);
        }
        return ResponseEntity.ok(page);
    }

    /**
     * POST /api/videos
     * Crea un nuevo vídeo a partir de los datos del DTO.
     */
    @PostMapping
    public ResponseEntity<Video> createVideo(@RequestBody VideoRequest req) {
        // Aquí usamos 1L como ID de admin; en producción extraerías el ID del usuario autenticado
        Video created = videoService.createVideo(req, 2L);
        return ResponseEntity.ok(created);
    }
}
