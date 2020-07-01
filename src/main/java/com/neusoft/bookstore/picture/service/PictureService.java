package com.neusoft.bookstore.picture.service;

import com.neusoft.bookstore.picture.model.Picture;
import com.neusoft.bookstore.util.ResponseVo;

public interface PictureService {
    ResponseVo addPic(Picture picture);

    ResponseVo listPic(Picture picture);

    ResponseVo updatePic(Picture picture);
}
