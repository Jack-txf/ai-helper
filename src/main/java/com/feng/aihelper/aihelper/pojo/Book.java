package com.feng.aihelper.aihelper.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 书本
 * @Author: txf
 * @Date: 2026/2/26
 */
@TableName("book")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {

    @TableId
    private Integer id;

    private String bookName;
    private String author;
    private Date publishDate;
    private int pages;
    private BigDecimal price;
    private String picture;
    private String content;
}
