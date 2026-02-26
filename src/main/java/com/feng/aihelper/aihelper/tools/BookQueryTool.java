package com.feng.aihelper.aihelper.tools;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.feng.aihelper.aihelper.mapper.BookMapper;
import com.feng.aihelper.aihelper.pojo.Book;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: 图书查询工具
 * @Author: txf
 * @Date: 2026/2/26
 */
@Component
public class BookQueryTool {

    @Resource
    private BookMapper bookMapper;

    @Tool( name= "queryBookCount", description = "The book inquiry tool returns the total number of books currently in the database " +
            "or the total number of book titles currently being sold")
    public String queryBookCount() {
        // 模拟图书查询逻辑
        System.out.println("bookCount Tool calling........");
        Long count = bookMapper.selectCount(null);
        return "There are a total of " + count + " books now";
    }

    // 工具 2：根据作者查找（新增）
    @Tool(name = "findBooksByAuthor", description = "根据作者姓名查找该作者编写的所有图书列表")
    public String findBooksByAuthor(@ToolParam(description = "作者姓名") String authorName) {
        System.out.println("findBooksByAuthor Tool calling, author: " + authorName);
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author", authorName);
        List<Book> books = bookMapper.selectList(queryWrapper);
        StringBuilder list = new StringBuilder();
        books.forEach(book -> list.append(book.getBookName()).append("、"));
        return String.format("找到作者 %s 的书籍如下：%s", authorName, list);
    }
}
