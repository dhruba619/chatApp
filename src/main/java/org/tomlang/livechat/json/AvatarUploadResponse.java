package org.tomlang.livechat.json;

public class AvatarUploadResponse {
    private String upload;
    private String imagePath;
    public String getUpload() {
        return upload;
    }
    public void setUpload(String upload) {
        this.upload = upload;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public AvatarUploadResponse() {
        super();
        // TODO Auto-generated constructor stub
    }
    public AvatarUploadResponse(String upload, String imagePath) {
        super();
        this.upload = upload;
        this.imagePath = imagePath;
    }

 
}
