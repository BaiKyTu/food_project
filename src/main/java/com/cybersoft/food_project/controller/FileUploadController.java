package com.cybersoft.food_project.controller;

import com.cybersoft.food_project.service.FileUploadService;
import com.cybersoft.food_project.service.FileUploadServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    FileUploadServiceImp fileUploadServiceImp;

//    @Autowired
//    FileUploadService fileUploadService;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file")MultipartFile file){
        boolean result = fileUploadServiceImp.storeFile(file);
        return new ResponseEntity<>(""+result, HttpStatus.OK);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) throws IOException {
        Resource resource = fileUploadServiceImp.loadFileByName(fileName);
        String contentType = "";
        if (resource != null){
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }
        if (contentType == null){
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
