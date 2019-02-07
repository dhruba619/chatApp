package org.tomlang.livechat.json;

public class ImageUploadResponse {
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

    public ImageUploadResponse() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ImageUploadResponse(String upload, String imagePath) {
        super();
        this.upload = upload;
        this.imagePath = imagePath;
    }

}
