package com.lsy.test.markdown.util;

import com.lsy.test.markdown.model.MarkdownEntity;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 将 markdown 文档 转为 HTML
 * <p>
 * Created by liangsongying on 2017/12/11.
 */
public class MarkDown2HtmlWrapper {
    private static Pattern IMG_PATTERN = Pattern.compile("!\\[(.*)\\]\\((.*?)\\)");


    public static String url = System.getProperty("user.dir");
    public static String MD_CSS = null;

    static {
        try {
            MD_CSS = FileReadUtil.readAll(url + "\\src\\main\\resources\\public\\css\\md\\markdown.css");
            MD_CSS = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>title</title>\n" +
                    " <style type=\"text/css\">\n" + MD_CSS + "\n</style>\n" +
                    "</head>";
        } catch (Exception e) {
            MD_CSS = "";
        }
    }


    /**
     * 将本地的markdown文件，转为html文档输出
     *
     * @param path 相对地址or绝对地址 ("/" 开头)
     * @return
     * @throws IOException
     */
    public static MarkdownEntity ofFile(String path) throws IOException {
        return ofStream(FileReadUtil.getStreamByFileName(path));
    }


    /**
     * 将网络的markdown文件，转为html文档输出
     *
     * @param url http开头的url格式
     * @return
     * @throws IOException
     */
    public static MarkdownEntity ofUrl(String url) throws IOException {
        return ofStream(FileReadUtil.getStreamByFileName(url));
    }


    /**
     * 将流转为html文档输出
     *
     * @param stream
     * @return
     */
    public static MarkdownEntity ofStream(InputStream stream) {
        StringBuilder contentBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
        List<String> lines = bufferedReader.lines().collect(Collectors.toList());
        for (String line : lines) {
            Matcher matcher = IMG_PATTERN.matcher(line);
            if (matcher.find()) {
                String alt = matcher.group(1);
                String src = matcher.group(2);
                if (src != null)
                    src = "../static/images/help/" + src.replace("file:///E:\\portal\\smartdata360-portal-restweb\\src\\main\\resources\\static\\images\\help\\", "");
                line = String.format("<img src=\"%s\" alt=\"%s\"/><br />", src, alt);
            }
            contentBuilder.append(line).append("\n");
        }
        return ofContent(contentBuilder.toString());
    }


    /**
     * 直接将markdown语义的文本转为html格式输出
     *
     * @param content markdown语义文本
     * @return
     */
    public static MarkdownEntity ofContent(String content) {
        String html = parse(content);
        MarkdownEntity entity = new MarkdownEntity();
        entity.setCss(MD_CSS);
        entity.setHtml(html);
        entity.addDivStyle("class", "markdown-body ");
        return entity;
    }


    /**
     * markdown to html
     *
     * @param content markdown contents
     * @return parse html contents
     */
    public static String parse(String content) {
        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);

        // enable table parse!
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));


        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(content);
        return renderer.render(document);
    }

}
