package tech.shefoo.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class ImageBean {
    private List<String> imagePaths = new ArrayList<>();

    @PostConstruct
    public void init() {
        loadImages();
    }

    public void loadImages() {
        // Get the FacesContext and then the ServletContext
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        
        // Get the real path for the uploads directory
        String uploadDir = servletContext.getRealPath("/resources/uploads");
        File dir = new File(uploadDir);
        File[] files = dir.listFiles();

        System.out.println(dir.listFiles());
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    imagePaths.add("/uploads/" + file.getName());
                }
            }
        }
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }
}
