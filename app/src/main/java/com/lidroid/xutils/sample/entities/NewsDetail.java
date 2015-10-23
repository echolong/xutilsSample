package com.lidroid.xutils.sample.entities;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * @author：liuyulong on 2015/10/23 16:09
 */
// 建议加上注解， 混淆后表名不受影响
@Table(name = "news_detail")
public class NewsDetail {

    @Id
    @NoAutoIncrement
    @Column(column = "newsid")
    private String newsId;

    @Column(column = "type")
    private int newstype;

    @Column(column = "time")
    private int newstime;

    @Column(column = "content")
    private String newscontent;

    public NewsDetail() {

    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public int getNewstype() {
        return newstype;
    }

    public void setNewstype(int newstype) {
        this.newstype = newstype;
    }

    public int getNewstime() {
        return newstime;
    }

    public void setNewstime(int newstime) {
        this.newstime = newstime;
    }

    public String getNewscontent() {
        return newscontent;
    }

    public void setNewscontent(String newscontent) {
        this.newscontent = newscontent;
    }
}
