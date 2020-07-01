package com.neusoft.bookstore.picture.mapper;

import com.neusoft.bookstore.picture.model.Picture;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PictureMapper {
    int addPicture(Picture picture);

    List<Picture> listPic(Picture picture);

    int updatePic(Picture picture);
}
