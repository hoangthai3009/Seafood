package ChuyenNganh.Seafood.Config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path imageUploadDir = Paths.get("./img/seafoods/");
        String imageUploadPath = imageUploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/img/seafoods/**").addResourceLocations("file:/" + imageUploadPath + "/");

    }
}
