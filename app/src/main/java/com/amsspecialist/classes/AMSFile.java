package com.amsspecialist.classes;

public class AMSFile {
    private String filename;
    private String filetype;
    private String filesize;
    private String comment;
    private String url;


    public AMSFile(String name, String type, String size, String comment,String url) {
        this.filename = name;
        this.filetype = type;
        this.filesize = size;
        this.comment = comment;
        this.url=url;

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}