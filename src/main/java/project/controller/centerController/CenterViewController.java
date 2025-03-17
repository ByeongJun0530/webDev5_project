package project.controller.centerController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CenterViewController {
    //센터 메인
    @GetMapping("/center")
    public String center() {
        return "html/main/center/center.html";
    }


    // 게시판(센터) 리스트 페이지
    @GetMapping("/center/findall")
    public String centerFindall() {
        return "html/main/center/findall";
    }


    // 게시판(센터) 등록 페이지
    @GetMapping("/center/upload")
    public String centerUpload() {
        return "center/upload";
    }

    // 게시판(센터) 개별 조회 페이지
    @GetMapping("/center/find")
    public String centerFind() {
        return "html/main/center/find.html";
    }
}
