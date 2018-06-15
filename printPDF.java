    //新建PDF
    public static void main(String[] args) throws IOException,DocumentException{
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Administrator\\Desktop\\HelloWorld.pdf"));
            document.open();
            document.add(new Paragraph("A Hello World PDF document."));
            document.close();
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
=============================================================================================
//编辑PDF
    public static void main(String[] args) throws IOException,DocumentException{
        try {
            PdfReader render = new PdfReader("C:\\Users\\Administrator\\Desktop\\HelloWorld.pdf");
            //根据一个pdfreader创建一个pdfStamper.用来生成新的pdf.
            PdfStamper stamper = new PdfStamper(render,new FileOutputStream("C:\\Users\\Administrator\\Desktop\\01.pdf"));
            BaseFont bf = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);//设置字体
            Font font = new Font(bf,10);
            font.setStyle(Font.BOLD);
            font.getBaseFont();
            //页数从1开始
            for(int i=1;i<=render.getNumberOfPages();i++){
                PdfContentByte bty = stamper.getOverContent(i);//获取原先PDF每页的内容
                PdfDictionary dictionary = render.getPageN(i);//获取该页的坐标轴信息
                PdfObject object = dictionary.get(new PdfName("MediaBox"));//拿到mediaBox 里面放着该页pdf的大小信息
                PdfArray pa = (PdfArray) object;
                System.out.print(pa.size());//po是一个数组对象.里面包含了该页pdf的坐标轴范围
                System.out.println(pa.getAsNumber(pa.size()-1));//看看y轴的最大值
                //开始写入
                bty.beginText();
                //设置字体和样式
                bty.setFontAndSize(font.getBaseFont(),10);
                //设置字体的输出位置
                bty.setTextMatrix(107,540);
                //要输出的text

                bty.endText();
                //创建一个image对象
                Image image = Image.getInstance("C:\\Users\\Administrator\\Desktop\\01.jpg");
                image.setAbsolutePosition(0,pa.getAsNumber(pa.size()-1).floatValue()-100);
                bty.addImage(image);
            }
            stamper.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
===========================================================================================
//添加PDF设置信息 
Document document = new Document();
        try{
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Administrator\\Desktop\\HelloWorld.pdf"));
            document.open();
            document.add(new Paragraph("Some content here."));//新的段落
            document.addAuthor("Lokesh Gupta");//添加作者
            document.addCreationDate();//创建日期
            document.addCreator("HowToDoInJava.com");
            document.addTitle("Set Attribute Example");//标题
            document.addSubject("An example to show how attributes can be added to pdf files.");//添加主题

            document.close();
            writer.close();

        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

=========================================================================================
//生成PDF文件
    public static ByteArrayOutputStream createPDF(HttpServletRequest request, String ftlName, Map root, String imageUrl) {
        String basePath = request.getSession().getServletContext().getRealPath("/");//绝对路径
        Configuration config = new Configuration();
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            config.setLocale(Locale.CHINA);
            config.setEncoding(Locale.CHINA, "UTF-8");
            //设置编码
            config.setDefaultEncoding("UTF-8");
            //设置模板路径
            config.setDirectoryForTemplateLoading(new File(basePath + "/WEB-INF/views_v4.0/itext/"));
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            String date = DateUtils.getCurrentYear() + "年" + (new Integer(DateUtils.getCurrentMonth()) + 1) + "月" +DateUtils.getDayOfMonth() + "日";
            root.put("date", date);
            log.info("ftlName: " + ftlName);
            File file = new File(ftlName);
            log.info("ftlName exist: " + file.exists());
            //获取模板
            Template template = config.getTemplate(ftlName);
            template.setEncoding("UTF-8");
            StringWriter writer = new StringWriter();
//            BufferedWriter writer = new BufferedWriter(stringWriter);
//            Writer writer = new StringWriter();
            //数据填充模板
            template.process(root, writer);
            writer.flush();
            String str = writer.toString();
            //pdf生成
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            ITextRenderer iTextRenderer = new ITextRenderer();
            iTextRenderer.setDocumentFromString(str);
            //设置字体  其他字体需要添加字体库
            ITextFontResolver fontResolver = iTextRenderer.getFontResolver();
            iTextRenderer.getFontResolver().addFont(basePath + "/WEB-INF/views_v4.0/itext/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//            iTextRenderer.setDocument(builder.parse(new ByteArrayInputStream(str.getBytes())),null);
            String baseUrl = "/res_v4.0/images/itext/";
            if(StringUtils.isNotBlank(imageUrl)){
                basePath = "";
                baseUrl = imageUrl;
            }
            //解决图片路径问题   设置好图片所选择的路径
            if(getCurrentOperatingSystem().indexOf("windows") != -1){
                iTextRenderer.getSharedContext().setBaseURL("file:/" + basePath + baseUrl);
            }else {
                iTextRenderer.getSharedContext().setBaseURL("file://" + basePath + baseUrl);
            }
            iTextRenderer.layout();
            //生成PDF
            iTextRenderer.createPDF(baos);
            baos.flush();
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(null != baos){
                try {
                    baos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
        return baos;
    }
=============================================================================================
//获取当前操作系统   
 public static String getCurrentOperatingSystem(){
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("---------当前操作系统是-----------" + os);
        return os;
    }
=============================================================================================
//客户端下载PDF  
public static String renderPdf(HttpServletResponse response, final byte[] bytes, final String filename, boolean flag) {
        String filePath = "";
        String basePath = CommonConstants.FILE_BASEPATH;///home/websvr/filebase
//        String basePath = "C:\\Users\\Administrator\\Desktop\\home\\websvr\\filebase";
        if (null != bytes) {
            try {
                if(flag){
                    filePath = filename + ".pdf";
                    setFileDownloadHeader(response, filename, ".pdf");
                    response.getOutputStream().write(bytes);
                    response.getOutputStream().flush();
                }else {
                    filePath = Constants.PDF_URL + filename + ".pdf";
                    File file = new File(basePath  + filePath);
                    //通过子类实例化父类对象
                    OutputStream output = new FileOutputStream(file);
                    //进行写操作
                    output.write(bytes);
                    //关闭输出流
                    output.close();
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return filePath;
    }














