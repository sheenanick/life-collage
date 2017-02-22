package com.doandstevensen.lifecollage.data.model;

/**
 * Created by Sheena on 2/21/17.
 */

public class CollageListResponse {
    private CollageResponse collage;
    private PictureResponse collagePic;

    public CollageListResponse(CollageResponse collage, PictureResponse picture) {
        this.collage = collage;
        this.collagePic = picture;
    }

    public CollageResponse getCollage() {
        return collage;
    }

    public PictureResponse getCollagePic() {
        return collagePic;
    }

    public void setCollage(CollageResponse collage) {
        this.collage = collage;
    }

    public void setCollagePic(PictureResponse collagePic) {
        this.collagePic = collagePic;
    }
}
