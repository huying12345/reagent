    //�½�PDF
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
//�༭PDF
    public static void main(String[] args) throws IOException,DocumentException{
        try {
            PdfReader render = new PdfReader("C:\\Users\\Administrator\\Desktop\\HelloWorld.pdf");
            //����һ��pdfreader����һ��pdfStamper.���������µ�pdf.
            PdfStamper stamper = new PdfStamper(render,new FileOutputStream("C:\\Users\\Administrator\\Desktop\\01.pdf"));
            BaseFont bf = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);//��������
            Font font = new Font(bf,10);
            font.setStyle(Font.BOLD);
            font.getBaseFont();
            //ҳ����1��ʼ
            for(int i=1;i<=render.getNumberOfPages();i++){
                PdfContentByte bty = stamper.getOverContent(i);//��ȡԭ��PDFÿҳ������
                PdfDictionary dictionary = render.getPageN(i);//��ȡ��ҳ����������Ϣ
                PdfObject object = dictionary.get(new PdfName("MediaBox"));//�õ�mediaBox ������Ÿ�ҳpdf�Ĵ�С��Ϣ
                PdfArray pa = (PdfArray) object;
                System.out.print(pa.size());//po��һ���������.��������˸�ҳpdf�������᷶Χ
                System.out.println(pa.getAsNumber(pa.size()-1));//����y������ֵ
                //��ʼд��
                bty.beginText();
                //�����������ʽ
                bty.setFontAndSize(font.getBaseFont(),10);
                //������������λ��
                bty.setTextMatrix(107,540);
                //Ҫ�����text

                bty.endText();
                //����һ��image����
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
//���PDF������Ϣ 
Document document = new Document();
        try{
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Administrator\\Desktop\\HelloWorld.pdf"));
            document.open();
            document.add(new Paragraph("Some content here."));//�µĶ���
            document.addAuthor("Lokesh Gupta");//�������
            document.addCreationDate();//��������
            document.addCreator("HowToDoInJava.com");
            document.addTitle("Set Attribute Example");//����
            document.addSubject("An example to show how attributes can be added to pdf files.");//�������

            document.close();
            writer.close();

        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

=========================================================================================
//����PDF�ļ�
    public static ByteArrayOutputStream createPDF(HttpServletRequest request, String ftlName, Map root, String imageUrl) {
        String basePath = request.getSession().getServletContext().getRealPath("/");//����·��
        Configuration config = new Configuration();
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            config.setLocale(Locale.CHINA);
            config.setEncoding(Locale.CHINA, "UTF-8");
            //���ñ���
            config.setDefaultEncoding("UTF-8");
            //����ģ��·��
            config.setDirectoryForTemplateLoading(new File(basePath + "/WEB-INF/views_v4.0/itext/"));
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            String date = DateUtils.getCurrentYear() + "��" + (new Integer(DateUtils.getCurrentMonth()) + 1) + "��" +DateUtils.getDayOfMonth() + "��";
            root.put("date", date);
            log.info("ftlName: " + ftlName);
            File file = new File(ftlName);
            log.info("ftlName exist: " + file.exists());
            //��ȡģ��
            Template template = config.getTemplate(ftlName);
            template.setEncoding("UTF-8");
            StringWriter writer = new StringWriter();
//            BufferedWriter writer = new BufferedWriter(stringWriter);
//            Writer writer = new StringWriter();
            //�������ģ��
            template.process(root, writer);
            writer.flush();
            String str = writer.toString();
            //pdf����
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            ITextRenderer iTextRenderer = new ITextRenderer();
            iTextRenderer.setDocumentFromString(str);
            //��������  ����������Ҫ��������
            ITextFontResolver fontResolver = iTextRenderer.getFontResolver();
            iTextRenderer.getFontResolver().addFont(basePath + "/WEB-INF/views_v4.0/itext/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//            iTextRenderer.setDocument(builder.parse(new ByteArrayInputStream(str.getBytes())),null);
            String baseUrl = "/res_v4.0/images/itext/";
            if(StringUtils.isNotBlank(imageUrl)){
                basePath = "";
                baseUrl = imageUrl;
            }
            //���ͼƬ·������   ���ú�ͼƬ��ѡ���·��
            if(getCurrentOperatingSystem().indexOf("windows") != -1){
                iTextRenderer.getSharedContext().setBaseURL("file:/" + basePath + baseUrl);
            }else {
                iTextRenderer.getSharedContext().setBaseURL("file://" + basePath + baseUrl);
            }
            iTextRenderer.layout();
            //����PDF
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
//��ȡ��ǰ����ϵͳ   
 public static String getCurrentOperatingSystem(){
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("---------��ǰ����ϵͳ��-----------" + os);
        return os;
    }
=============================================================================================
//�ͻ�������PDF  
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
                    //ͨ������ʵ�����������
                    OutputStream output = new FileOutputStream(file);
                    //����д����
                    output.write(bytes);
                    //�ر������
                    output.close();
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return filePath;
    }














