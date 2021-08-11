package com.victor.hm.http.data;

import java.io.Serializable;

public class CategoryInfo implements Serializable {
    public int id;
    public int defaultAuthorId;
    public int tagId;
    public String name;
    public String alias;
    public String description;
    public String bgPicture;
    public String bgColor;
    public String headerImage;
}
