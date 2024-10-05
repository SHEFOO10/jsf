package tech.shefoo.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import javax.servlet.ServletContext;

@ManagedBean
@ViewScoped
public class ImageUploadBean implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -1066633078354091478L;
	private UploadedFile file;

	private String uploadPath;


	
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    // Method to generate a random filename
    private String generateRandomFilename(String originalFileName) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String randomUUID = UUID.randomUUID().toString();
        return randomUUID + fileExtension; // Append the original file extension
    }
    
    
    @PostConstruct
    public void init() {
        // Get the FacesContext and then the ServletContext
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        
        // Get the real path for the uploads directory
        uploadPath = servletContext.getRealPath("/resources/uploads");
        
        // Create the uploads directory if it doesn't exist
        try {
            Path path = Paths.get(uploadPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path); // Create the uploads directory here
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void upload() {
        if (file != null) {
            try (InputStream input = file.getInputStream()) {
                // Save the uploaded file to the uploads directory

                Path filePath = Paths.get(uploadPath, generateRandomFilename(file.getFileName()));
                Files.copy(input, filePath);
                System.out.println("File successfully uploaded: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            	setFile(null);
            }
        } else {
            System.out.println("No file selected!");
        }
    }

}
