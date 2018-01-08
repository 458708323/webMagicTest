package FirstTest;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.management.JMException;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Grail on 2018/1/8.
 */
public class GithubRepoPageProcessor implements PageProcessor {

    //    抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    //    process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
//    public void process(Page page) {
//        //        从页面发现后续的url地址来抓取
//        //page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
//
//        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
//        page.putField("name", page.getHtml().xpath("//h1[@class='public']/strong/a/text()").toString());
//        if (page.getResultItems().get("name") == null) {
//            //      跳过此页面
//            page.setSkip(true);
//        }
//        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
//    }
    public void process(Page page){
        String imgUrl = page.getHtml().xpath("//div[@class='s-p-top']").css("img", "src").toString();
        try {
            DownloadImage.download("http:" + imgUrl, "tp.jpg","D:\\webmagic\\image");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class DownloadImage{
        public static void download(String imgUrl, String name, String path) throws IOException {
            // 构造URL
            URL url = new URL(imgUrl);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5*1000);
            // 输入流
            InputStream is = con.getInputStream();

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf=new File(path);
            if(!sf.exists()){
                sf.mkdirs();
            }
            OutputStream os = new FileOutputStream(sf.getPath()+"\\"+name);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider spider = Spider.create(new GithubRepoPageProcessor())
                .addUrl("https://www.baidu.com/");
//                .addPipeline(new FilePipeline("D:\\webmagic\\"));
        try {
            SpiderMonitor.instance().register(spider);
        } catch (JMException e) {
            e.printStackTrace();
        }
        spider.start();
    }
}
