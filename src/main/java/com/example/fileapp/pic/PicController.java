package com.example.fileapp.pic;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class PicController {

    private final PicRepository picRepository;

    @PostMapping("/upload")
    public String upload(PicRequest.UploadDTO requestDTO){
        // 1. 데이터 전달 받고
        String title = requestDTO.getTitle();
        MultipartFile imgFile = requestDTO.getImgFile();

        // 2. 파일저장 위치 설정해서 파일을 저장 (UUID 붙여서 롤링)
        String imgFilename = UUID.randomUUID()+"_"+imgFile.getOriginalFilename();
        Path imgPath = Paths.get("./upload/"+imgFilename);
        try {
            Files.write(imgPath, imgFile.getBytes());

            // 3. DB에 저장 (title, realFileName)
            picRepository.insert(title, imgFilename);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(requestDTO);


        return "redirect:/";
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/uploadForm")
    public String uploadForm(){
        return "uploadForm";
    }

    @GetMapping("/uploadCheck")
    public String uploadCheck(HttpServletRequest request){
        Pic pic = picRepository.findById(1);
        request.setAttribute("pic", pic);
        return "uploadCheck";
    }
}