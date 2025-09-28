package com.ndh.ShopTechnology.controller.document;

import com.ndh.ShopTechnology.def.DefRes;
import com.ndh.ShopTechnology.dto.response.APIResponse;
import com.ndh.ShopTechnology.entities.doc.DocumentEntity;
import com.ndh.ShopTechnology.services.doc.DocumentService;
import com.ndh.ShopTechnology.utils.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/document")
public class DocumentController {

    private final DocumentService documentService;


    private static final String UPLOAD_DIR = "D:\\NDH-Project\\ShopTechnology\\uploads\\";

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse> uploadImages(
            @RequestParam("files") List<MultipartFile> files
    ) throws Exception {

        if (files == null || files.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.doResponse(
                            DefRes.STAT_CODE, DefRes.STATUS_BAD_REQUEST,
                            DefRes.RES_DES, "No files uploaded"
                    ));
        }

        List<DocumentEntity> documentEntities = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(APIResponse.doResponse(
                                DefRes.STAT_CODE, DefRes.STATUS_BAD_REQUEST,
                                DefRes.RES_DES, "One or more files are empty"
                        ));
            }
            DocumentEntity documentEntity = FileUtils.storeFile(file);

            documentEntity = documentService.createDocument(documentEntity);
            documentEntities.add(documentEntity);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.doResponse(
                        DefRes.STAT_CODE, DefRes.STATUS_SUCCESS,
                        DefRes.RES_DATA, documentEntities
                ));
    }


    @GetMapping("/{filename:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            File file = filePath.toFile();

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            // Đọc nội dung file thành byte array
            byte[] fileContent = Files.readAllBytes(filePath);

            // Xác định loại nội dung
            String contentType = Files.probeContentType(filePath);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                    .body(fileContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
