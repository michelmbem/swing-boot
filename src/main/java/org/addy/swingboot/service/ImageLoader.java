package org.addy.swingboot.service;

import lombok.extern.slf4j.Slf4j;
import org.addy.swingboot.model.FilmPoster;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class ImageLoader {
    @Value("${images.basedir}")
    private String imageBaseDir;

    public Image loadImage(FilmPoster poster) {
        if (poster == null) return null;

        var imageFile = new File(imageBaseDir, poster.getFilename());

        try {
            return ImageIO.read(imageFile);
        } catch (IOException e) {
            log.error("Could not load image '{}'", imageFile.getPath(), e);
            return null;
        }
    }
}
