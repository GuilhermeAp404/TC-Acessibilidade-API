package tc.accessibility.market.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tc.accessibility.market.DTOs.ProductDetectionDTO;
import tc.accessibility.market.services.VisionService;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class VisionController {

    private final VisionService visionService;

    public VisionController(VisionService visionService) {
        this.visionService = visionService;
    }

    @PostMapping("/vision")
    public ResponseEntity<ProductDetectionDTO> analyzeImage(@RequestParam("image") MultipartFile image) throws IOException {
        ProductDetectionDTO detectionDTO = visionService.analyzeImage(image);
        return new ResponseEntity<>(detectionDTO, HttpStatus.OK);
    }
}
