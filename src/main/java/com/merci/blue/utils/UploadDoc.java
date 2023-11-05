package com.merci.blue.utils;

import com.cloudinary.utils.ObjectUtils;
import com.merci.blue.config.CloudinaryConfig;
import com.merci.blue.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploadDoc {
    private final CloudinaryConfig cloudinaryConfig;

    public String uploadDoc(MultipartFile doc) throws IOException, ServiceException {
        if(doc == null){
            throw new ServiceException("File cannot be null");
        }

        Map<?, ?> uploadResult = cloudinaryConfig.cloudinary().uploader().upload(doc.getBytes(), ObjectUtils.asMap(
                "resource_type", "raw"
        ));
        return (String) uploadResult.get("url");
    }
}
