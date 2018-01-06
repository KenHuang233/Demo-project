package junit.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class LuceneTest {

    @Test
    public void testMultiSearch() throws IOException, ParseException {
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("/Users/HXL/Desktop/luceneindex")));

        IndexSearcher searcher = new IndexSearcher(reader);

        //3.准备查询关键词和要查询的字段
        /*
         * id	song_name			song_singer		song_album		song_lyric
         * 58	"hello apple"		"kate"			"apple album"	"a lot of apple"
         * 63	"say hello to you"	"bob"			"say hi"		"say hello to you,say hello to you"
         * 79	"good morning"		"jerry"			"morning one"	"good morning,good afternoon"
         */
        String keywords = "lot";
        String[] fields = new String[]{"song_name", "song_singer", "song_album", "song_lyric"};

        Analyzer analyzer = new StandardAnalyzer();

        //跨多个不同字段查询
        QueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);

        Query query = queryParser.parse(keywords);

        TopDocs docs = searcher.search(query, 10);

        int totalHits = docs.totalHits;
        System.out.println("查询结果命中数量：" + totalHits);

        ScoreDoc[] scoreDocs = docs.scoreDocs;
        for (int i = 0; i < scoreDocs.length; i++) {
            ScoreDoc doc = scoreDocs[i];
            int docId = doc.doc;
            System.out.println("当前文档id：" + docId);

            float score = doc.score;
            System.out.println("当前文档得分：" + score);

            Document document = searcher.doc(docId);
            String songId = document.getValues("id")[0];
            String songName = document.getValues("song_name")[0];
            String songSinger = document.getValues("song_singer")[0];
            String songAlbum = document.getValues("song_album")[0];
            String[] songLyric = document.getValues("song_lyric");

            System.out.println("songId：" + songId);
            System.out.println("songName：" + songName);
            System.out.println("songSinger：" + songSinger);
            System.out.println("songAlbum：" + songAlbum);
            System.out.println("songLyric：" + songLyric);

            System.out.println();
        }

    }

    @Test
    public void testSearch() throws IOException, ParseException {
        //1.读取硬盘上的索引库
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("/Users/HXL/Desktop/luceneindex")));

        //2.创建索引库核心对象
        IndexSearcher searcher = new IndexSearcher(reader);

        //3.准备查询关键词和要查询的字段
        /*
         * id	song_name			song_singer		song_album		song_lyric
         * 58	"hello apple"		"kate"			"apple album"	"a lot of apple"
         * 63	"say hello to you"	"bob"			"say hi"		"say hello to you,say hello to you"
         * 79	"good morning"		"jerry"			"morning one"	"good morning,good afternoon"
         */
        String keywords = "bob";
        String fieldName = "song_singer";

        //4.创建分词器对象
        Analyzer analyzer = new StandardAnalyzer();

        //5.创建查询解析器对象
        QueryParser queryParser = new QueryParser(fieldName, analyzer);

        //6.使用解析器对象解析关键词（对关键词字符串进行分词操作）
        Query query = queryParser.parse(keywords);

        //7.执行查询
        TopDocs docs = searcher.search(query, 10);

        //8.解析查询结果
        int totalHits = docs.totalHits;
        System.out.println("查询结果命中数量：" + totalHits);

        ScoreDoc[] scoreDocs = docs.scoreDocs;
        for (int i = 0; i < scoreDocs.length; i++) {
            ScoreDoc doc = scoreDocs[i];
            int docId = doc.doc;
            System.out.println("当前文档id：" + docId);

            float score = doc.score;
            System.out.println("当前文档得分：" + score);

            //根据文档id获取对应的Document对象
            Document document = searcher.doc(docId);
            String songId = document.getValues("id")[0];
            String songName = document.getValues("song_name")[0];
            String songSinger = document.getValues("song_singer")[0];
            String songAlbum = document.getValues("song_album")[0];
            String[] songLyric = document.getValues("song_lyric");

            System.out.println("songId：" + songId);
            System.out.println("songName：" + songName);
            System.out.println("songSinger：" + songSinger);
            System.out.println("songAlbum：" + songAlbum);
            System.out.println("songLyric：" + songLyric);

            System.out.println();
        }

    }

    @Test
    public void testAnalyzer() throws IOException {

        String text = "今天晚上咱们开黑，我打野，你辅助，上单就靠胖子了！";

        //1.创建分词器对象
        Analyzer analyzer = new IKAnalyzer();

        //2.获取TokenStream对象
        TokenStream tokenStream = analyzer.tokenStream("aaa", text);

        //3.重置tokenStream指针
        tokenStream.reset();

        //4.设置分词偏移量引用
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);

        //5.设置分词词语引用
        CharTermAttribute termAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        //6.通过tokenStream循环词汇列表
        while (tokenStream.incrementToken()) {

            int startOffset = offsetAttribute.startOffset();
            int endOffset = offsetAttribute.endOffset();

            System.out.println(termAttribute + "\t[" + startOffset + "," + endOffset + "]");
        }

        analyzer.close();
    }

    @Test
    public void testCreateIndex() throws IOException {
        //1.准备索引库位置路径
        String path = "/Users/HXL/Desktop/luceneindex";

        //2.创建File对象
        File file = new File(path);

        //3.使用FSDirectory对象打开索引库目录位置
        FSDirectory directory = FSDirectory.open(file);

        //4.创建分词器对象
        Analyzer analyzer = new StandardAnalyzer();

        //5.创建索引写入器配置对象
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);

        //6.创建索引写入器对象
        IndexWriter indexWriter = new IndexWriter(directory, config);

        /*
         * id	song_name			song_singer		song_album		song_lyric
         * 58	"hello apple"		"kate"			"apple album"	"a lot of apple"
         * 63	"say hello to you"	"bob"			"say hi"		"say hello to you,say hello to you"
         * 79	"good morning"		"jerry"			"morning one"	"good morning,good afternoon"
         */
        //7.创建Document对象
        Document document01 = new Document();
        Document document02 = new Document();
        Document document03 = new Document();

        //8.为每一个Document对象填充具体字段的值
        //①创建id字段
        IntField id = new IntField("id", 58, Store.YES);

        //②创建song_name字段
        TextField songName = new TextField("song_name", "hello apple", Store.YES);

        //③创建song_singer字段
        StringField songSinger = new StringField("song_singer", "kate", Store.YES);

        //④创建song_album字段
        TextField songAlbum = new TextField("song_album", "apple album", Store.YES);

        //⑤创建song_lyric字段
        TextField songLyric = new TextField("song_lyric", "a lot of apple", Store.NO);

        document01.add(id);
        document01.add(songName);
        document01.add(songSinger);
        document01.add(songAlbum);
        document01.add(songLyric);

        //document02
        id = new IntField("id", 63, Store.YES);

        songName = new TextField("song_name", "say hello to you", Store.YES);

        songSinger = new StringField("song_singer", "bob", Store.YES);

        songAlbum = new TextField("song_album", "say hi", Store.YES);

        songLyric = new TextField("song_lyric", "say hello to you,say hello to you", Store.NO);

        document02.add(id);
        document02.add(songName);
        document02.add(songSinger);
        document02.add(songAlbum);
        document02.add(songLyric);

        //document03
        id = new IntField("id", 79, Store.YES);

        songName = new TextField("song_name", "good morning", Store.YES);

        songSinger = new StringField("song_singer", "jerry", Store.YES);

        songAlbum = new TextField("song_album", "morning one", Store.YES);

        songLyric = new TextField("song_lyric", "good morning,good afternoon", Store.NO);

        document03.add(id);
        document03.add(songName);
        document03.add(songSinger);
        document03.add(songAlbum);
        document03.add(songLyric);

        //9.把文档添加到writer中
        indexWriter.addDocument(document01);
        indexWriter.addDocument(document02);
        indexWriter.addDocument(document03);

        //10.提交
        indexWriter.commit();

        //11.关闭
        indexWriter.close();
    }

}
