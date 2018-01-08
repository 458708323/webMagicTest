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
 * Created by wb-zh306310 on 2018/1/8.
 */
public class GithubRepoPageProcessor implements PageProcessor {

    //    ץȡ��վ��������ã��������롢ץȡ��������Դ�����
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    //    process�Ƕ��������߼��ĺ��Ľӿڣ��������д��ȡ�߼�
//    public void process(Page page) {
//        //        ��ҳ�淢�ֺ�����url��ַ��ץȡ
//        //page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
//
//        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
//        page.putField("name", page.getHtml().xpath("//h1[@class='public']/strong/a/text()").toString());
//        if (page.getResultItems().get("name") == null) {
//            //      ������ҳ��
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
            // ����URL
            URL url = new URL(imgUrl);
            // ������
            URLConnection con = url.openConnection();
            //��������ʱΪ5s
            con.setConnectTimeout(5*1000);
            // ������
            InputStream is = con.getInputStream();

            // 1K�����ݻ���
            byte[] bs = new byte[1024];
            // ��ȡ�������ݳ���
            int len;
            // ������ļ���
            File sf=new File(path);
            if(!sf.exists()){
                sf.mkdirs();
            }
            OutputStream os = new FileOutputStream(sf.getPath()+"\\"+name);
            // ��ʼ��ȡ
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // ��ϣ��ر���������
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
