package com.lsy.test.markdown;

import com.lsy.test.markdown.model.MarkdownEntity;
import com.lsy.test.markdown.util.MarkDown2HtmlWrapper;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by liangsongying on 2017/12/16.
 */
public class MarkdownToHtmlTest {
    @Test
    public void markdownToHtmlTest() throws IOException{
        String url = System.getProperty("user.dir");//获取当前项目路径
        String file = url+"\\markdown\\"+"个税查询.md";
        MarkdownEntity html = MarkDown2HtmlWrapper.ofFile(file);
        System.out.println(html.toString());
        PrintStream printStream = new PrintStream(new FileOutputStream(url+"\\src\\main\\resources\\templates\\index.html"));
        printStream.println(html.toString());
    }
}
