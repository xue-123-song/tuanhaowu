//package com.example.tuanhaowu.Controller;
//
//import com.example.tuanhaowu.util.FileUtil;
//import com.example.tuanhaowu.util.recommendUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//public class FileController {
//    @RequestMapping(value = "/tourist/doUpload")
//    public String uploadImg(@RequestParam("file") MultipartFile file, Model model) {
//        // 如果图片为空，显示error
//        if ((file.getOriginalFilename().isEmpty() )) {
//            model.addAttribute("error","error");
//            return "";
//        } else {
//            // 1. 创建一个 head 对象
//            // 2. 获取图片的名字
//            String fileName = file.getOriginalFilename();
//
//            // 3. 防止图片名字相同而出现bug，使用 uuid 加密
//            String hToken = UUID.randomUUID().toString();
//
//            // 4. 加密后的图片名字：uuid + 图片格式
//            String HeadName = hToken + fileName.substring(fileName.lastIndexOf('.'));
//
//            // 5. 图片存放的文件夹地址
//            String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images";
//            // 6.  图片的路径 = 文件夹地址 + 名字
//            String fileAddress = filePath + "\\" + HeadName;
//            try{
//                // 图片上传的工具类
//                // 参数一：图片的编码格式的字节数组
//                // 参数二：图片存放的文件夹地址
//                // 参数三：图片的名字（加密后）
//                FileUtil.uploadFile(file.getBytes(),filePath,HeadName);
//                return "/picture/" + HeadName;
//            } catch (Exception e){
//                return "";
//            }
//        }
//    }
//
//
//}
